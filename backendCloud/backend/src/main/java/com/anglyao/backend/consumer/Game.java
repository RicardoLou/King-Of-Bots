package com.anglyao.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.anglyao.backend.pojo.Bot;
import com.anglyao.backend.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.anglyao.backend.consumer.WebSocketServer.recordMapper;


public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final Bot botA;
    private final Bot botB;
    private final int[][] g;
    private final Player playerA, playerB;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private Integer nextStepAIndex = null;
    private Integer nextStepBIndex = null;
    private final ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; // playing -> finished
    private String winner = ""; // all -> 平局  A -> A win  B -> B win
    private static final String addBotURL = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer playerAId, Bot botA, Integer playerBId, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        this.botA = botA;
        this.botB = botB;
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getCode();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getCode();
        }


        this.playerA = new Player(playerAId, rows - 2, 1, botIdA, botCodeA, new ArrayList<>());
        this.playerB = new Player(playerBId, 1, cols - 2, botIdB, botCodeB, new ArrayList<>());
    }

    int[][] getG() {
        return g;
    }

    public Player getPlayerA() {
        return playerA;
    }
    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepAIndex(Integer nextStepAIndex) {
        lock.lock();
        try {
            this.nextStepAIndex = nextStepAIndex;
        } finally {
            lock.unlock();
        }

    }
    public void setNextStepBIndex(Integer nextStepBIndex) {
        lock.lock();
        try {
            this.nextStepBIndex = nextStepBIndex;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 核心绘图逻辑：包含边界生成、随机对称加墙及连通性检查
     */
    private boolean draw() {
        // 1. 初始化地图（0为平地，1为墙）
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g[r][c] = 0;
            }
        }

        // 2. 绘制四周边界墙
        for (int r = 0; r < rows; r++) {
            g[r][0] = g[r][cols - 1] = 1;
        }
        for (int c = 0; c < cols; c++) {
            g[0][c] = g[rows - 1][c] = 1;
        }

        // 3. 随机对称加墙
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                // 如果已经是墙或者是中心对称位置已经是墙，则跳过
                if (g[r][c] == 1 || g[rows - 1 - r][cols - 1 - c] == 1) continue;

                // 避开玩家出生点（左下角和右上角）
                if ((r == rows - 2 && c == 1) || (r == 1 && c == cols - 2)) continue;

                // 对称生成墙
                g[r][c] = 1;
                break;
            }
        }

        // 4. 连通性检查 (使用复制的地图进行 DFS)
        int[][] copyG = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(g[i], 0, copyG[i], 0, cols);
        }

        return checkConnectivity(copyG, rows - 2, 1, 1, cols - 2);
    }

    /**
     * 使用深度优先搜索（DFS）检查地图两点是否连通
     */
    private boolean checkConnectivity(int[][] map, int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        map[sx][sy] = 1; // 标记已访问

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < rows && y >= 0 && y < cols && map[x][y] == 0) {
                if (checkConnectivity(map, x, y, tx, ty)) return true;
            }
        }
        return false;
    }

    /**
     * 尝试生成一个合法地图
     */
    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) break;
        }
    }

    /**
     * 将当前局面信息转换为字符串
     * @param player
     * @return
     */
    private String getRecordInfo(Player player) {
        // 信息用 # 分割
        Player me, you;
        if (player.getId().equals(playerA.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString()
                + "#" + me.getSx()
                + "#" + me.getSy()
                + "#(" + me.getStepsString()
                + ")#" + you.getSx()
                + "#" + you.getSy()
                + "#(" + you.getStepsString() + ")";
    }
    public void sendBotCode(Player player) {
        if (player.getBotId().equals(-1))
            // 人操作 无需执行代码
            return;
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getRecordInfo(player));
        WebSocketServer.restTemplate.postForObject(addBotURL, data, String.class);

    }
    /**
     * 等待玩家下一步操作
     */
    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for(int i = 0; i < 50; i ++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    // 两个操作都读到了, 先写入
                    if (nextStepAIndex != null && nextStepBIndex != null) {
                        playerA.getSteps().add(nextStepAIndex);
                        playerB.getSteps().add(nextStepBIndex);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 向两个 Client 发送消息
     */
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("winner", winner);
        recordInfo();
        sendAllMessage(resp.toJSONString());
    }

    private boolean check_valid(List<Cell> aCells, List<Cell> bCells) {
        int n = aCells.size();
        Cell cell = aCells.get(n - 1);
        if (g[cell.x][cell.y] == 1) return false;

        for(int i = 0; i < n - 1; i ++) {
            if (aCells.get(i).x == cell.x && aCells.get(i).y == cell.y)
                return false;
        }
        for(int i = 0; i < n - 1; i ++) {
            if (bCells.get(i).x == cell.x && bCells.get(i).y == cell.y)
                return false;
        }
        return true;
    }
    /**
     * 判断蛇操作是否合法
     */
    private void judge() {
        List<Cell> aCells = playerA.getCells(), bCells = playerB.getCells();
        boolean validA = check_valid(aCells, bCells);
        boolean validB = check_valid(bCells, aCells);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                winner = "all";
            } else if (!validA) {
                winner = "B";
            } else {
                winner = "A";
            }
        }
    }
    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }
    private void recordInfo() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                winner,
                new Date()
        );
        recordMapper.insert(record);
    }

    /**
     * 传递蛇移动信息
     */
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepAIndex);
            resp.put("b_direction", nextStepBIndex);
            sendAllMessage(resp.toJSONString());
            nextStepAIndex = nextStepBIndex = null;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void run() {
        for(int i = 0; i < 100000; i ++) {
            if (nextStep()) {
                judge();
                if (status.equals("playing")) {
                   sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepAIndex == null && nextStepBIndex == null) {
                        winner = "all";
                    } else if (nextStepAIndex == null) {
                        winner = "B";
                    } else {
                        winner = "A";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
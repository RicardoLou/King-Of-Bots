package com.anglyao.botRunningSystem.utils;

import java.util.*;

public class Bot implements com.anglyao.botRunningSystem.utils.BotInterface {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) { this.x = x; this.y = y; }
    }

    private int rows = 13, cols = 14;
    private int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    // 评价函数：计算当前蛇 A 的“生存空间”
    // 使用简单的洪水填充（Flood Fill）统计可达空格数
    private int evaluate(int[][] g, int sx, int sy) {
        int count = 0;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Cell> q = new LinkedList<>();
        q.add(new Cell(sx, sy));
        visited[sx][sy] = true;

        while (!q.isEmpty()) {
            Cell cur = q.poll();
            for (int i = 0; i < 4; i++) {
                int nx = cur.x + dx[i], ny = cur.y + dy[i];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && g[nx][ny] == 0 && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.add(new Cell(nx, ny));
                    count++;
                }
            }
        }
        return count;
    }

    // Minimax 搜索
    // depth: 搜索深度
    // isMax: 当前是否是我的轮次
    private int minimax(int[][] g, int ax, int ay, int bx, int by, int depth, boolean isMax) {
        if (depth == 0) return evaluate(g, ax, ay);

        if (isMax) {
            int maxScore = -1000000;
            for (int i = 0; i < 4; i++) {
                int nx = ax + dx[i], ny = ay + dy[i];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && g[nx][ny] == 0) {
                    g[nx][ny] = 1; // 虚拟占用
                    maxScore = Math.max(maxScore, minimax(g, nx, ny, bx, by, depth - 1, false));
                    g[nx][ny] = 0; // 回溯
                }
            }
            return maxScore;
        } else {
            // 假设对手 B 也会随机走或尝试封堵，这里简化为对手也在找自己的最大空间
            // 在真正的博弈中，这里应该取对手让 A 得分最低的走法
            int minScore = 1000000;
            for (int i = 0; i < 4; i++) {
                int nx = bx + dx[i], ny = by + dy[i];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && g[nx][ny] == 0) {
                    g[nx][ny] = 1;
                    minScore = Math.min(minScore, minimax(g, ax, ay, nx, ny, depth - 1, true));
                    g[nx][ny] = 0;
                }
            }
            return minScore == 1000000 ? evaluate(g, ax, ay) : minScore;
        }
    }

    @Override
    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[rows][cols];
        for (int i = 0, k = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++, k++) {
                if (strs[0].charAt(k) == '1') g[i][j] = 1;
            }
        }

        // 解析 A 和 B 的位置（简化版：只取头，实际应还原全身）
        List<Cell> cellsA = getCells(Integer.parseInt(strs[1]), Integer.parseInt(strs[2]), strs[3]);
        List<Cell> cellsB = getCells(Integer.parseInt(strs[4]), Integer.parseInt(strs[5]), strs[6]);

        for (Cell c : cellsA) g[c.x][c.y] = 1;
        for (Cell c : cellsB) g[c.x][c.y] = 1;

        int bestMove = 0;
        int maxScore = -1000000;
        int headAx = cellsA.get(cellsA.size() - 1).x;
        int headAy = cellsA.get(cellsA.size() - 1).y;
        int headBx = cellsB.get(cellsB.size() - 1).x;
        int headBy = cellsB.get(cellsB.size() - 1).y;

        // 第一层决策：枚举我的四个方向
        for (int i = 0; i < 4; i++) {
            int nx = headAx + dx[i], ny = headAy + dy[i];
            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && g[nx][ny] == 0) {
                g[nx][ny] = 1;
                // 计算未来 3 步后的分值
                int score = minimax(g, nx, ny, headBx, headBy, 3, false);
                g[nx][ny] = 0;
                if (score > maxScore) {
                    maxScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }
    private boolean check_tail_increasing(int step) { // 检验当前回合，蛇的长度是否增加

        if (step <= 10) return true;

        return step % 3 == 1;

    }



    public List<Cell> getCells(int sx, int sy, String steps) {

        steps = steps.substring(1, steps.length() - 1);

        List<Cell> res = new ArrayList<>();



        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

        int x = sx, y = sy;

        int step = 0;

        res.add(new Cell(x, y));

        for (int i = 0; i < steps.length(); i ++) {

            int d = steps.charAt(i) - '0';

            x += dx[d];

            y += dy[d];

            res.add(new Cell(x, y));

            if (!check_tail_increasing( ++ step)) {

                res.remove(0);

            }

        }

        return res;

    }
}
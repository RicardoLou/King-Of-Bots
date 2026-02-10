package com.anglyao.backend.consumer;

import lombok.Data;

import java.util.Random;


public class Game {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
    }

    int[][] getG() {
        return g;
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
                g[r][c] = g[rows - 1 - r][cols - 1 - c] = 1;
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
}
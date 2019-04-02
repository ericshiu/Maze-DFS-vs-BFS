package com.eric;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Maze {
    private int n;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] dfsVisited;
    private boolean[][] bfsVisited;
    private boolean dfsDone = false;
    private boolean bfsDone = false;

    public Maze(int n, int x, int y) {
        this.n = n;
        StdDraw.setXscale(0, x * 2 + 2);
        StdDraw.setYscale(0, y + 2);
        init();
        generate();
    }

    private void init() {
        // 初始化邊界單元格已經訪問過
        dfsVisited = new boolean[n + 2][n + 2];
        bfsVisited = new boolean[n + 2][n + 2];
        for (int x = 0; x < n + 2; x++) {
            dfsVisited[x][0] = true;
            dfsVisited[x][n + 1] = true;
        }
        for (int y = 0; y < n + 2; y++) {
            dfsVisited[0][y] = true;
            dfsVisited[n + 1][y] = true;
        }


        // 初始化所有牆壁
        north = new boolean[n + 2][n + 2];
        east = new boolean[n + 2][n + 2];
        south = new boolean[n + 2][n + 2];
        west = new boolean[n + 2][n + 2];
        for (int x = 0; x < n + 2; x++) {
            for (int y = 0; y < n + 2; y++) {
                north[x][y] = true;
                east[x][y] = true;
                south[x][y] = true;
                west[x][y] = true;
            }
        }
    }


    // 產生迷宮
    private void generate(int x, int y) {
        dfsVisited[x][y] = true;

        // while 一個未經訪問的鄰居
        while (!dfsVisited[x][y + 1] || !dfsVisited[x + 1][y] || !dfsVisited[x][y - 1] || !dfsVisited[x - 1][y]) {

            // 選擇隨機鄰居 (could use Knuth's trick instead)
            while (true) {
                double r = StdRandom.uniform(4);
                if (r == 0 && !dfsVisited[x][y + 1]) {
                    north[x][y] = false;
                    south[x][y + 1] = false;
                    generate(x, y + 1);
                    break;
                } else if (r == 1 && !dfsVisited[x + 1][y]) {
                    east[x][y] = false;
                    west[x + 1][y] = false;
                    generate(x + 1, y);
                    break;
                } else if (r == 2 && !dfsVisited[x][y - 1]) {
                    south[x][y] = false;
                    north[x][y - 1] = false;
                    generate(x, y - 1);
                    break;
                } else if (r == 3 && !dfsVisited[x - 1][y]) {
                    west[x][y] = false;
                    east[x - 1][y] = false;
                    generate(x - 1, y);
                    break;
                }
            }
        }
    }

    // 從左下角開始迷宮
    private void generate() {
        generate(1, 1);

/*
        // delete some random walls
        for (int i = 0; i < n; i++) {
            int x = 1 + StdRandom.uniform(n-1);
            int y = 1 + StdRandom.uniform(n-1);
            north[x][y] = south[x][y+1] = false;
        }

        // add some random walls
        for (int i = 0; i < 10; i++) {
            int x = n/2 + StdRandom.uniform(n/2);
            int y = n/2 + StdRandom.uniform(n/2);
            east[x][y] = west[x+1][y] = true;
        }
*/

    }


    // 使用深度優先搜索解決迷宮
    private void dfs(int x, int y) {
        if (x == 0 || y == 0 || x == n + 1 || y == n + 1) return;
        if (dfsDone || dfsVisited[x][y]) return;
        dfsVisited[x][y] = true;

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);

        // reached middle
        if (x == n / 2 && y == n / 2) dfsDone = true;

        if (!north[x][y]) dfs(x, y + 1);
        if (!east[x][y]) dfs(x + 1, y);
        if (!south[x][y]) dfs(x, y - 1);
        if (!west[x][y]) dfs(x - 1, y);

        if (dfsDone) return;

        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);
    }

    private void bfs(int x, int y) {
        if (x == 0 || y == 0 || x == n + 1 || y == n + 1) return;
        System.out.println(bfsVisited[x][y]);
        if (bfsDone || bfsVisited[x][y]) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(n + x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);
        Queue<int[]> q = new LinkedList<int[]>();
        q.add(new int[]{1, 1});
        while (!q.isEmpty()) {
            int[] v = q.poll();
            if (v[0] == n / 2 && v[1] == n / 2) {
                break;
            }
            System.out.println(Arrays.toString(v));
            if (!north[v[0]][v[1]] && !bfsVisited[v[0]][v[1] + 1]) {
                System.out.println("north");
                q.add(new int[]{v[0], v[1] + 1});
                bfsVisited[v[0]][v[1] + 1] = true;
                StdDraw.filledCircle(n + v[0] + 0.5, v[1] + 1 + 0.5, 0.25);
                StdDraw.show();
                StdDraw.pause(30);
            }
            if (!east[v[0]][v[1]] && !bfsVisited[v[0] + 1][v[1]]) {
                System.out.println("east");
                q.add(new int[]{v[0] + 1, v[1]});
                bfsVisited[v[0] + 1][v[1]] = true;
                StdDraw.filledCircle(n + v[0] + 1 + 0.5, v[1] + 0.5, 0.25);
                StdDraw.show();
                StdDraw.pause(30);
            }
            if (!south[v[0]][v[1]] && !bfsVisited[v[0]][v[1] - 1]) {
                System.out.println("south");
                q.add(new int[]{v[0], v[1] - 1});
                bfsVisited[v[0]][v[1] - 1] = true;
                StdDraw.filledCircle(n + v[0] + 0.5, v[1] - 1 + 0.5, 0.25);
                StdDraw.show();
                StdDraw.pause(30);
            }
            if (!west[v[0]][v[1]] && !bfsVisited[v[0] - 1][v[1]]) {
                System.out.println("west");
                q.add(new int[]{v[0] - 1, v[1]});
                bfsVisited[v[0] - 1][v[1]] = true;
                StdDraw.filledCircle(n + v[0] - 1 + 0.5, v[1] + 0.5, 0.25);
                StdDraw.show();
                StdDraw.pause(30);
            }
            bfsVisited[v[0]][v[1]] = true;
            System.out.println(q.size());
        }
    }

    // solve the 迷宮從開始狀態開始
    public void solve() {
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                dfsVisited[x][y] = false;
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                bfsVisited[x][y] = false;
        dfsDone = false;
        bfsDone = false;
        dfs(1, 1);
        bfs(1, 1);
    }

    // 畫迷宮
    public void draw() {

        // 更改作畫顏色
        StdDraw.setPenColor(StdDraw.RED);
        // 劃正中心
        StdDraw.filledCircle(n / 2.0 + 0.5, n / 2.0 + 0.5, 0.375);
        StdDraw.filledCircle(n * 1.5 + 0.5, n / 2.0 + 0.5, 0.375);
        // 更改作畫顏色
        StdDraw.setPenColor(StdDraw.BLACK);
        // 劃左下角
        StdDraw.filledCircle(1.5, 1.5, 0.375);
        StdDraw.filledCircle(n + 1.5, 1.5, 0.375);

        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) StdDraw.line(x, y, x + 1, y);
                if (north[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
                if (west[x][y]) StdDraw.line(x, y, x, y + 1);
                if (east[x][y]) StdDraw.line(x + 1, y, x + 1, y + 1);
            }
        }
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) StdDraw.line(x + n, y, x + n + 1, y);
                if (north[x][y]) StdDraw.line(x + n, y + 1, x + n + 1, y + 1);
                if (west[x][y]) StdDraw.line(x + n, y, x + n, y + 1);
                if (east[x][y]) StdDraw.line(x + n + 1, y, x + n + 1, y + 1);
            }
        }
        StdDraw.show();
        StdDraw.pause(1000);
    }


    // a test client
    public static void main(String[] args) {
        int n = Integer.parseInt("30");
        Maze maze = new Maze(n, n, n);
        StdDraw.enableDoubleBuffering();
        maze.draw();
        maze.solve();
    }
}

package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;

import java.util.LinkedList;
import java.util.Queue;

public class ShortestPathFinder {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public static int findShortestPath(MapEntity[][] map, Location start, Location end) {
        if (start.equals(end)) {
            return 0;
        }

        int rows = map.length;
        int cols = map[0].length;

        Queue<Location> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];
        int[][] distances = new int[rows][cols];

        return bfs(queue, visited, distances, map, start, end);
    }

    private static int bfs(Queue<Location> queue, boolean[][] visited, int[][] distances,
                           MapEntity[][] map, Location start, Location end) {

        queue.offer(start);
        visited[start.x()][start.y()] = true;

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (current.equals(end)) {
                return distances[current.x()][current.y()];
            }

            for (int[] dir : DIRECTIONS) {
                int newX = current.x() + dir[0];
                int newY = current.y() + dir[1];

                if (isValidMove(map, newX, newY) && !visited[newX][newY]) {
                    Location next = new Location(newX, newY);
                    queue.offer(next);
                    visited[newX][newY] = true;
                    distances[newX][newY] = distances[current.x()][current.y()] + 1;
                }
            }
        }

        return -1;

    }

    private static boolean isValidMove(MapEntity[][] map, int x, int y) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            return false;
        }

        MapEntityType type = map[x][y].type();
        return type != MapEntityType.WALL;
    }

    // helper methods that can be used to check how the algorithm traverses and updates
    // the visited and distance values on the map
    private static void printVisitedMatrix(boolean[][] visited) {
        for (boolean[] row : visited) {
            for (boolean cell : row) {
                System.out.print((cell ? "1" : "0") + " ");
            }
            System.out.println();
        }
    }

    private static void printDistancesMatrix(int[][] distances) {
        for (int[] row : distances) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
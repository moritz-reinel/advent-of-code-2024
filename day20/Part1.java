import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    record Point(int y, int x) {};
    record PointWithScore(Point point, int score) {};

    private static final Point[] MOVES = {
        new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)
    };

    private static char[][] map;

    private static boolean isInMap(Point p) {
        return p.y >= 0 && p.y < map.length && p.x >= 0 && p.x < map[0].length;
    }

    public static void main(String[] args) throws IOException
    {
        var lines = Files.readAllLines(Path.of("input.txt"));

        map = new char[lines.size()][lines.getFirst().length()];
        Point start = null, end = null;

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.getFirst().length(); x++) {
                char c = lines.get(y).charAt(x);
                map[y][x] = c;
                
                if (c == 'S') {
                    start = new Point(y, x);
                } else if (c == 'E') {
                    end = new Point(y, x);
                }
            }
        }
        assert start != null && end != null;

        Set<Point> path = new HashSet<>();
        int[][] costsOnPath = new int[map.length][map[0].length];

        findPath(start, end, path, costsOnPath);
        var cheatsMap = calcPossibleCheats(costsOnPath, path, start, end, 2);

        int result = cheatsMap.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println(result);
    }

    private static void findPath(Point start, Point end, Set<Point> visited, int[][] costOnPath)
    {
        Queue<PointWithScore> queue = new LinkedList<>();
        queue.add(new PointWithScore(end, 0));

        while (!queue.isEmpty()) {
            var current = queue.poll();
            Point point = current.point;
            int score = current.score;

            if (!visited.add(point)) continue;

            costOnPath[point.y][point.x] = score;

            if (start.equals(point)) return;

            for (Point move : MOVES) {
                Point newPoint = new Point(point.y + move.y, point.x + move.x);

                if (isInMap(newPoint) && map[newPoint.y][newPoint.x] != '#')
                    queue.add(new PointWithScore(newPoint, score + 1));
            }
        }

        assert false;
    }

    private static Map<Integer, Integer> calcPossibleCheats(int[][] costsOnPath, Set<Point> pointsOnPath, Point start, Point end, int steps)
    {
        Map<Integer, Integer> cheatsCount = new HashMap<>();

        for (Point p : pointsOnPath) {
            int hereCosts = costsOnPath[p.y][p.x];
            for (int ny = -steps; ny <= steps; ny++) {
                for (int nx = -steps + Math.abs(ny); nx <= steps - Math.abs(ny); nx++) {
                    Point cheatPoint = new Point(p.y + ny, p.x + nx);
                    if (!pointsOnPath.contains(cheatPoint)) continue;

                    int costAtCheatPoint = costsOnPath[cheatPoint.y][cheatPoint.x];
                    int timeSaved = hereCosts - costAtCheatPoint - Math.abs(ny) - Math.abs(nx);

                    if (timeSaved >= 100)
                        cheatsCount.put(timeSaved, cheatsCount.getOrDefault(timeSaved, 0) + 1);
                }
            }
        }

        return cheatsCount;
    }
}

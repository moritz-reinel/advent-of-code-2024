import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    record Dir(int x, int y) {};
    private final static Dir[] DIRS = { new Dir(0, -1), new Dir(1, 0), new Dir(0, 1), new Dir(-1, 0) };

    record Point(int x, int y, int dir) {};

    record PointCost(Point pos, int cost) implements Comparable<PointCost> {
        @Override
        public int compareTo(PointCost other) {
            return Integer.compare(this.cost, other.cost);
        }
    };

    public static void main(String[] args) throws IOException
    {
        var lines = Files.readString(Path.of("input.txt")).split("\n");

        char[][] map = new char[lines.length][lines[0].length()];

        int sx = -1, sy = -1, tx = -1, ty = -1;

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[0].length(); x++) {
                char c = lines[y].charAt(x);
                map[y][x] = c;

                if (c == 'S') {
                    sx = x; sy = y;
                }
                if (c == 'E') {
                    tx = x; ty = y;
                }
            }
        }
        assert sx != -1 && sy != -1 && tx != -1 && ty != -1;

        var todo = new PriorityQueue<PointCost>();
        todo.add(new PointCost(new Point(sx, sy, 1), 0));

        Set<Point> visited = new HashSet<>();
        Map<Point, Integer> costs = new HashMap<>();

        int result = Integer.MAX_VALUE;

        while (!todo.isEmpty()) {
            var next = todo.poll();

            costs.putIfAbsent(next.pos, next.cost);

            if (next.pos.x == tx && next.pos.y == ty) {
                result = Math.min(result, next.cost);
            }

            if (!visited.add(next.pos)) continue;

            int nx = next.pos.x + DIRS[next.pos.dir].x;
            int ny = next.pos.y + DIRS[next.pos.dir].y;

            if (map[ny][nx] != '#') {
                todo.add(new PointCost(new Point(nx, ny, next.pos.dir), next.cost + 1));
            }

            todo.add(new PointCost(new Point(next.pos.x, next.pos.y, (next.pos.dir + 1) % 4), next.cost + 1000));
            todo.add(new PointCost(new Point(next.pos.x, next.pos.y, (next.pos.dir + 3) % 4), next.cost + 1000));
        }

        System.out.println(result);
    }
}

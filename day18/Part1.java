import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    record Point(int x, int y) {};
    record PointDist(Point pos, int dist) implements Comparable<PointDist> {  // needed for retrieving the item with the lowest distance from the Queue
        @Override
        public int compareTo(PointDist other) {
            return Integer.compare(this.dist, other.dist);
        }
    };

    private final static Point[] DIRS = { new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0) };

    private final static int FIELD_SIZE = 71;
    private final static int BYTE_COUNT = 1024;
    private static boolean[][] field;

    public static void main(String[] args) throws IOException
    {
        var bytes = Files.lines(Path.of("input.txt")).map(line -> {
            var nums = line.split(",");
            int x = Integer.parseInt(nums[0]);
            int y = Integer.parseInt(nums[1]);
            return new Point(x, y);
        }).collect(Collectors.toList());
        
        field = new boolean[FIELD_SIZE][FIELD_SIZE];

        for (int i = 0; i < BYTE_COUNT; i++) {
            field[bytes.get(i).y][bytes.get(i).x] = true;
        }

        Queue<PointDist> todo = new PriorityQueue<>();
        todo.add(new PointDist(new Point(0, 0), 0));

        Set<Point> visited = new HashSet<>();
        int targetDist = -1;

        while (!todo.isEmpty()) {
            var next = todo.poll();
            if (next.pos.x == FIELD_SIZE - 1 && next.pos.y == FIELD_SIZE - 1) {
                targetDist = next.dist;
                break;
            }

            if (!visited.add(next.pos)) continue;

            for (var dir : DIRS) {
                int nx = next.pos.x + dir.x;
                int ny = next.pos.y + dir.y;
                if (nx >= 0 && nx < FIELD_SIZE && ny >= 0 && ny < FIELD_SIZE && !field[ny][nx])
                    todo.add(new PointDist(new Point(nx, ny), next.dist + 1));
            }
        }
        assert targetDist != -1;

        System.out.println(targetDist);
    }

    @SuppressWarnings("unused")
    private static void printField()
    {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[0].length; x++) {
                boolean isObstructed = field[y][x];
                System.out.print(isObstructed ? '#' : '.');
            }
            System.out.println();
        }
    }
}

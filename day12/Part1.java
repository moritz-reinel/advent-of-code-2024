import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    record Point(int x, int y) {};
    private static Point[] DIRS = { new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0) };

    private static List<List<Character>> farm, originalFarm;

    private static char getPlant(int x, int y)
    {
        if (y < 0 || y >= farm.size() || x < 0 || x >= farm.getFirst().size()) return '\0';
        return farm.get(y).get(x);
    }

    public static void main(String[] args) throws IOException
    {
        farm = Files.lines(Path.of("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        originalFarm = farm.stream().map(row -> new ArrayList<>(row)).collect(Collectors.toList());

        int result = 0;

        for (int y = 0; y < farm.size(); y++) {
            for (int x = 0; x < farm.getFirst().size(); x++) {
                if (getPlant(x, y) != '.')
                    result += calcRegionPrice(x, y);
            }
        }

        System.out.println(result);
    }

    private static int calcEdges(int x, int y)
    {
        int edges = 0;
        char plant = getPlant(x, y);

        for (var dir : DIRS) {
            int nx = x + dir.x;
            int ny = y + dir.y;

            if ((ny < 0 || ny >= farm.size() || nx < 0 || nx >= farm.getFirst().size()) || (originalFarm.get(ny).get(nx) != plant)) {
                edges++;
            }
        }

        return edges;
    }

    private static int calcRegionPrice(int x, int y)
    {
        int area = 1;
        int perimeter = calcEdges(x, y);

        char plant = farm.get(y).get(x);
        List<Point> todo = new LinkedList<>();
        todo.add(new Point(x, y));
        farm.get(y).set(x, '.');

        while (!todo.isEmpty()) {
            Point next = todo.removeFirst();

            for (var dir : DIRS) {
                int nx = next.x + dir.x;
                int ny = next.y + dir.y;

                if (getPlant(nx, ny) == plant) {
                    area++;
                    perimeter += calcEdges(nx, ny);
                    farm.get(ny).set(nx, '.');
                    todo.add(new Point(nx, ny));
                }
            }
        }

        return area * perimeter;
    }
}

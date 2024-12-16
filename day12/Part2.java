import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    record Point(int x, int y) {};
    private static Point[] DIRS = { new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0) };

    private static List<List<Character>> farm, originalFarm;

    private static char getPlant(int x, int y)
    {
        if (y < 0 || y >= farm.size() || x < 0 || x >= farm.getFirst().size()) return '\0';
        return farm.get(y).get(x);
    }

    private static boolean isInsideFarm(int x, int y)
    {
        return y >= 0 && y < farm.size() && x >= 0 && x < farm.getFirst().size();
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
                if (getPlant(x, y) != '.') {
                    result += calcRegionPrice(x, y);
                }
            }
        }

        System.out.println(result);
    }

    private static int calcSides(int x, int y)
    {
        int plantSides = 0;
        char plant = getPlant(x, y);

        for (int i = 0; i < DIRS.length; i++) {
            int nx = x + DIRS[i].x;
            int ny = y + DIRS[i].y;

            if (!isInsideFarm(nx, ny) || (originalFarm.get(ny).get(nx) != plant)) {
                int nx90Deg = x + DIRS[(i - 1 + 4) % 4].x;
                int ny90Deg = y + DIRS[(i - 1 + 4) % 4].y;
                boolean isBeginEdge = !isInsideFarm(nx90Deg, ny90Deg) || originalFarm.get(ny90Deg).get(nx90Deg) != plant;

                int nxCorner = nx + DIRS[(i - 1 + 4) % 4].x;
                int nyCorner = ny + DIRS[(i - 1 + 4) % 4].y;
                boolean isConcaveBeginEdge = isInsideFarm(nxCorner, nyCorner) && originalFarm.get(nyCorner).get(nxCorner) == plant;

                if (isBeginEdge || isConcaveBeginEdge)
                    plantSides++;
            }
        }

        return plantSides;
    }

    private static int calcRegionPrice(int x, int y)
    {
        int area = 1;
        int sides = calcSides(x, y);

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
                    sides += calcSides(nx, ny);
                    farm.get(ny).set(nx, '.');
                    todo.add(new Point(nx, ny));
                }
            }
        }

        return area * sides;
    }
}

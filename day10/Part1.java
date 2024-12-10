import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    record Point(int x, int y){};

    private static List<List<Integer>> map;

    private static int getField(int x, int y)
    {
        if (y >= 0 && y < map.size() && x >= 0 && x < map.getFirst().size()) return map.get(y).get(x);
        return -1;
    }

    public static void main(String[] args) throws IOException
    {
        map = Files.lines(Path.of("input.txt"))
            .map(line -> line.chars().map(c -> c - '0').boxed().collect(Collectors.toList()))
            .collect(Collectors.toList());

        Set<Point> trailheads = new HashSet<>();
        for (int r = 0; r < map.getFirst().size(); r++) {
            for (int c = 0; c < map.size(); c++) {
                if (getField(c, r) == 0) trailheads.add(new Point(c, r));
            }
        }

        int result = 0;
        for (var th : trailheads) {
            var targetsVisited = new HashSet<Point>();
            calculateScore(th, -1, new HashSet<>(), targetsVisited);
            result += targetsVisited.size();
        }
        System.out.println(result);
    }

    private static void calculateScore(Point th, int lastVal, Set<Point> visited, Set<Point> targetsVisited) {
        int thisVal = getField(th.x, th.y);

        if (thisVal != lastVal + 1 || visited.contains(th)) return;

        if (thisVal == 9) {
            targetsVisited.add(th);
            return;
        }

        visited.add(th);
        
        calculateScore(new Point(th.x - 1, th.y), thisVal, visited, targetsVisited);
        calculateScore(new Point(th.x + 1, th.y), thisVal, visited, targetsVisited);
        calculateScore(new Point(th.x, th.y - 1), thisVal, visited, targetsVisited);
        calculateScore(new Point(th.x, th.y + 1), thisVal, visited, targetsVisited);
    }
}

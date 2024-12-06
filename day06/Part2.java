import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    record Position(int x, int y, int direction){};

    public static void main(String[] args) throws IOException
    {
        List<List<Character>> map = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        int ox = -1, oy = -1;
        outer:
        for (int r = 0; r < map.size(); r++) {
            for (int c = 0; c < map.getFirst().size(); c++) {
                if (map.get(r).get(c) == '^') {
                    ox = c;
                    oy = r;
                    break outer;
                }
            }
        }
        assert(ox != -1);
        assert(oy != -1);
        
        int result = 0;
        for (int rr = 0; rr < map.getFirst().size(); rr++) {
            for (int cc = 0; cc < map.size(); cc++) {
                if (map.get(rr).get(cc) != '.') continue;

                map.get(rr).set(cc, '#');
                result++;

                Set<Position> visited = new HashSet<>();
                int x = ox;
                int y = oy;
                int direction = 0;

                try {
                    while (true) {
                        var here = new Position(x, y, direction);
                        if (visited.contains(here)) break;

                        visited.add(here);

                        if (direction == 0) {
                            if (map.get(y-1).get(x) == '#') {
                                direction = 1;
                            } else {
                                y--;
                            }
                        } else if (direction == 1) {
                            if (map.get(y).get(x+1) == '#') {
                                direction = 2;
                            } else {
                                x++;
                            }
                        } else if (direction == 2) {
                            if (map.get(y+1).get(x) == '#') {
                                direction = 3;
                            } else {
                                y++;
                            }
                        } else {
                            if (map.get(y).get(x-1) == '#') {
                                direction = 0;
                            } else {
                                x--;
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    result--;
                }
                map.get(rr).set(cc, '.');
            }
        }

        System.out.println(result);
    }
}

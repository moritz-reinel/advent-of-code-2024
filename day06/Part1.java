import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        List<List<Character>> map = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        int x = -1, y = -1;
        outer:
        for (int r = 0; r < map.size(); r++) {
            for (int c = 0; c < map.getFirst().size(); c++) {
                if (map.get(r).get(c) == '^') {
                    x = c;
                    y = r;
                    break outer;
                }
            }
        }
        assert(x != -1);
        assert(y != -1);
        
        int result = 1;
        int direction = 0;
        try {
            while (true) {
                boolean turn = false;
                if (direction == 0) {
                    if (map.get(y-1).get(x) == '#') {
                        direction = 1;
                        turn = true;
                    } else {
                        y--;
                    }
                } else if (direction == 1) {
                    if (map.get(y).get(x+1) == '#') {
                        direction = 2;
                        turn = true;
                    } else {
                        x++;
                    }
                } else if (direction == 2) {
                    if (map.get(y+1).get(x) == '#') {
                        direction = 3;
                        turn = true;
                    } else {
                        y++;
                    }
                } else {
                    if (map.get(y).get(x-1) == '#') {
                        direction = 0;
                        turn = true;
                    } else {
                        x--;
                    }
                }

                if (!turn) {
                    if (map.get(y).get(x) == '.') result++;
                    map.get(y).set(x, 'X');
                }
            }
        } catch (IndexOutOfBoundsException e) {}

        System.out.println(result);
    }
}

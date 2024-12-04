import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    private static List<List<Character>> field;

    private static char getOrDefault(int r, int c)
    {
        if (r >= 0 && r < field.size() && c >= 0 && c < field.getFirst().size()) return field.get(r).get(c);
        return '.';
    }

    public static void main(String[] args) throws IOException
    {
        field = Arrays.stream(Files.readString(Path.of("input.txt")).strip().split("\n"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        int result = 0;

        for (int r = 0; r < field.getFirst().size(); r++) {
            for (int c = 0; c < field.size(); c++) {
                if (getOrDefault(r, c) != 'X') continue;

                if (getOrDefault(r, c-1) == 'M' && getOrDefault(r, c-2) == 'A' && getOrDefault(r, c-3) == 'S') {  // horizontal left
                    result++;
                }
                if (getOrDefault(r, c+1) == 'M' && getOrDefault(r, c+2) == 'A' && getOrDefault(r, c+3) == 'S') {  // horizontal right
                    result++;
                }
                if (getOrDefault(r-1, c) == 'M' && getOrDefault(r-2, c) == 'A' && getOrDefault(r-3, c) == 'S') {  // vertical up
                    result++;
                }
                if (getOrDefault(r+1, c) == 'M' && getOrDefault(r+2, c) == 'A' && getOrDefault(r+3, c) == 'S') {  // vertical down
                    result++;
                }
                if (getOrDefault(r-1, c-1) == 'M' && getOrDefault(r-2, c-2) == 'A' && getOrDefault(r-3, c-3) == 'S') {  // diag left up
                    result++;
                }
                if (getOrDefault(r-1, c+1) == 'M' && getOrDefault(r-2, c+2) == 'A' && getOrDefault(r-3, c+3) == 'S') {  // diag right up)
                    result++;
                }
                if (getOrDefault(r+1, c-1) == 'M' && getOrDefault(r+2, c-2) == 'A' && getOrDefault(r+3, c-3) == 'S') {  // diag left down
                    result++;
                }
                if (getOrDefault(r+1, c+1) == 'M' && getOrDefault(r+2, c+2) == 'A' && getOrDefault(r+3, c+3) == 'S') {  // diag right down
                    result++;
                }
            }
        }

        System.out.println(result);
    }
}

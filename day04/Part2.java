import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
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
                if (getOrDefault(r, c) != 'A') continue;

                if (((getOrDefault(r-1, c-1) == 'M' && getOrDefault(r+1, c+1) == 'S') || (getOrDefault(r-1, c-1) == 'S' && getOrDefault(r+1, c+1) == 'M')) &&  // top left to bottom right "MAS" or "SAM"
                    ((getOrDefault(r-1, c+1) == 'M' && getOrDefault(r+1, c-1) == 'S') || (getOrDefault(r-1, c+1) == 'S' && getOrDefault(r+1, c-1) == 'M'))) {  // top right to bottom left "MAS" or "SAM"
                    result++;
                }
            }
        }

        System.out.println(result);
    }
}

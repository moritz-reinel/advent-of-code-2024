import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        long result = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n"))
            .map(line -> {
                List<Integer> levels = Arrays.stream(line.replaceAll("\\s+", " ").split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

                int first = levels.getFirst();
                int last = levels.getLast();
                
                int prev = levels.getFirst();
                for (int i = 1; i < levels.size(); i++) {
                    int x = levels.get(i);
                    if (first > last) {
                        if (prev > x && Math.abs(prev - x) > 0 && Math.abs(prev-x) <=3) {
                            prev = x;
                            continue;
                        }
                        return false;
                    } else if (first < last) {
                        if (prev < x && Math.abs(prev - x) > 0 && Math.abs(prev-x) <=3) {
                            prev = x;
                            continue;
                        }
                        return false;
                    } else {
                        return false;
                    }
                }
                return true;
            })
            .filter(b -> b)
            .count();

        System.out.println(result);
    }
}

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part2
{
    private static Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;

        var towels = input[0].split(", ");

        long result = input[1].lines()
            .map(pattern -> calcCombinations(pattern, towels))
            .reduce(0L, (sum, x) -> sum + x);

        System.out.println(result);
    }

    public static long calcCombinations(String pattern, String[] towels) {
        if (cache.containsKey(pattern)) return cache.get(pattern);

        long combinations = pattern.isEmpty() ? 1 : 0;
        combinations += Arrays.stream(towels)
            .filter(t -> pattern.startsWith(t))
            .map(t -> calcCombinations(pattern.substring(t.length()), towels))
            .reduce(0L, (sum, x) -> sum + x);

        cache.put(pattern, combinations);

        return combinations;
    }
}

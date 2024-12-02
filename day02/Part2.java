import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Part2
{
    private static List<Integer> copyWithoutXthElement(List<Integer> list, int idx) {
        return IntStream.range(0, list.size())
            .filter(i -> i != idx)
            .mapToObj(list::get)
            .collect(Collectors.toList());
    }

    private static boolean isOrdered(List<Integer> list) {
        var copy = new ArrayList<>(list);
        // check ascending
        Collections.sort(copy);
        if (list.equals(copy)) return true;
        
        // check descending
        Collections.reverse(copy);
        return list.equals(copy);
    }

    private static boolean isSafe(List<Integer> list) {
        if (!isOrdered(list)) return false;

        for (int i = 1; i < list.size(); i++) {
            int diff = Math.abs(list.get(i) - list.get(i-1));
            if (!(diff >= 1 && diff <= 3)) return false;
        }

        return true;
    }

    public static void main(String[] args) throws IOException
    {
        long result = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n"))
            .map(line -> {
                List<Integer> levels = Arrays.stream(line.replaceAll("\\s+", " ").split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

                for (int i = 0; i < levels.size(); i++) {
                    var tmp = copyWithoutXthElement(levels, i);
                    if (isSafe(tmp)) return true;
                }
                return false;
            })
            .filter(b -> b)
            .count();

        System.out.println(result);
    }
}

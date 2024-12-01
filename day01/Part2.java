import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        Arrays.stream(Files.readString(Path.of("input.txt")).split("\n"))
            .forEach(line -> {
                var clean = line.replaceAll("\\s+", " ").split(" ");
                left.add(Integer.parseInt(clean[0]));
                right.add(Integer.parseInt(clean[1]));
            });

        int result = left.stream()
            .map(l -> l * Collections.frequency(right, l))
            .reduce(0, (sum, l) -> sum + l);

        System.out.println(result);
    }
}

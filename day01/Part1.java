import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
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

        Collections.sort(left);
        Collections.sort(right);

        long result = 0;
        for (int i = 0; i < left.size(); i++) {
            result += Math.abs(left.get(i) - right.get(i));
        }

        System.out.println(result);
    }
}

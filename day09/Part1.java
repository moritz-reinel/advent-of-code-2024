import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.IntStream;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt"));

        List<Integer> out = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            int x = input.charAt(i) - '0';
            for (int j = 0; j < x; j++) {
                if (i % 2 == 0) {
                    out.add(i / 2);
                } else {
                    out.add(-1);
                }
            }
        }

        int used = (int) out.stream().filter(i -> i != -1).count();
        int space = out.indexOf(-1);
        int lastUsed = out.size() - 1;

        while (space != used) {
            if (out.get(lastUsed) == -1) {
                lastUsed--;
                continue;
            }

            out.set(space, out.get(lastUsed));
            out.set(lastUsed, -1);

            space = out.indexOf(-1);
            lastUsed--;
        }

        long result = IntStream.range(0, used).mapToLong(i -> i * Math.max(out.get(i), 0)).sum();
        System.out.println(result);
    }
}

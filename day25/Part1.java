import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.IntStream;

public class Part1
{
    record KeyOrLock(int a, int b, int c, int d, int e) {
        boolean matches(KeyOrLock other) {
            return this.a + other.a <= 5 && this.b + other.b <= 5 && this.c + other.c <= 5 && this.d + other.d <= 5 && this.e + other.e <= 5;
        }
    };

    public static void main(String[] args) throws IOException
    {
        List<KeyOrLock> keys  = new ArrayList<>();
        List<KeyOrLock> locks = new ArrayList<>();

        Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n")).forEach(block -> {
            var lines = block.split("\n");
            assert lines.length == 7;

            boolean isKey = IntStream.range(0, lines[0].length()).allMatch(i -> lines[0].charAt(i) == '#');
            int a = 0, b = 0, c = 0, d = 0, e = 0;
            for (var line : lines) {
                a += (line.charAt(0) == '#') ? 1 : 0;
                b += (line.charAt(1) == '#') ? 1 : 0;
                c += (line.charAt(2) == '#') ? 1 : 0;
                d += (line.charAt(3) == '#') ? 1 : 0;
                e += (line.charAt(4) == '#') ? 1 : 0;
            }

            var item = new KeyOrLock(a - 1, b - 1, c - 1, d - 1, e - 1);
            if (isKey) keys.add(item);
            else locks.add(item);
        });

        int matches = 0;
        for (var key : keys) {
            for (var lock: locks) {
                if (key.matches(lock)) matches++;
            }
        }

        System.out.println(matches);
    }
}

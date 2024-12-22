import java.io.*;
import java.nio.file.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        long result = Files.lines(Path.of("input.txt")).map(Integer::parseInt)
            .map(Part1::generateNextSecret)
            .reduce(0L, (sum, x) -> sum + x);

        System.out.println(result);
    }

    private static long generateNextSecret(long secret)
    {
        for (int i = 0; i < 2000; i++) {
            long result = secret * 64;
            secret ^= result;
            secret %= 16777216;

            result = secret / 32;
            secret ^= result;
            secret %= 16777216;

            result = secret * 2048;
            secret ^= result;
            secret %= 16777216;
        }

        return secret;
    }
}

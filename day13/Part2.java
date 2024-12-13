import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        Pattern pattern = Pattern.compile("\\d+");

        long result = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
            .map(block -> {
                var nums = new ArrayList<Integer>(6);
                Matcher matcher = pattern.matcher(block);
                while (matcher.find()) nums.add(Integer.parseInt(matcher.group()));
                assert nums.size() == 6;

                long correction = 10_000_000_000_000L;
                long ax = nums.get(0), ay = nums.get(1), bx = nums.get(2), by = nums.get(3),
                    tx = correction + nums.get(4), ty = correction + nums.get(5);

                // From testing, there seems to be only one solution anyways -> equation system
                long commonDenominator  = ax * by - bx * ay;
                long equationANumerator = tx * by - bx * ty;
                long equationBNumerator = ax * ty - tx * ay;

                return (equationBNumerator % commonDenominator == 0 && equationANumerator % commonDenominator == 0)
                    ? 3 * (equationANumerator / commonDenominator) + (equationBNumerator / commonDenominator)
                    : 0L;
            })
            .reduce(0L, (sum, x) -> sum + x);

        System.out.println(result);
    }
}

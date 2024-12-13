import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        Pattern pattern = Pattern.compile("\\d+");

        int result = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n\n"))
            .map(block -> {
                var nums = new ArrayList<Integer>(6);
                Matcher matcher = pattern.matcher(block);
                while (matcher.find()) nums.add(Integer.parseInt(matcher.group()));
                assert nums.size() == 6;

                int ax = nums.get(0), ay = nums.get(1), bx = nums.get(2), by = nums.get(3), tx = nums.get(4), ty = nums.get(5);
                int minCost = Integer.MAX_VALUE;

                for (int a = 0; a <= tx / Math.min(ax, bx); a++) {
                    int b = (tx - a * ax) / bx;
                    if (a * ax + b * bx == tx && a * ay + b * by == ty) {
                        int cost = 3 * a + b;
                        minCost = Math.min(minCost, cost);
                    }
                }

                return (minCost == Integer.MAX_VALUE) ? 0 : minCost;
            })
            .reduce(0, (sum, x) -> sum + x);

        System.out.println(result);
    }
}

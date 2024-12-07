import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        long result = Arrays.stream(Files.readString(Path.of("input.txt")).split("\n"))
            .mapToLong(line -> {
                List<Long> nums = Arrays.stream(line.replaceFirst(":", "").split(" ")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
                long target = nums.getFirst();
                for (int comb = 0; comb < Math.pow(3, nums.size()-2); comb++) {
                    long currentResult = nums.get(1);
                    for (int i = 2; i < nums.size(); i++) {
                        int function = getTernaryDigit(comb, i-2);
                        if (function == 0) {
                            currentResult += nums.get(i);
                        } else if (function == 1) {
                            currentResult *= nums.get(i);
                        } else {
                            currentResult = Long.parseLong("" + currentResult + nums.get(i));
                        }
                    }
                    if (currentResult == target) return target;
                }
                return 0;
            })
            .reduce(0, (sum, x) -> sum + x);

        System.out.println(result);
    }

    private static int getTernaryDigit(int number, int pos) {
        for (int i = 0; i <= pos; i++) {
            int digit = number % 3;
            number /= 3;
            if (i == pos) return digit;
        }
        return 0;
    }
}

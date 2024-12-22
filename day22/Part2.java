import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

// Beware: Takes about 8 minutes on my machine, but at least it works ':D
public class Part2
{
    record Price(int price, int change) {};

    record Sequence(int first, int second, int third, int fourth) {};

    public static void main(String[] args) throws IOException
    {
        List<List<Price>> prices = Files.lines(Path.of("input.txt")).map(Integer::parseInt)
            .map(Part2::generatePrices)
            .collect(Collectors.toList());

        System.out.println(testSequences(prices));
    }

    private static List<Price> generatePrices(long secret)
    {
        List<Price> history = new ArrayList<>(2000);
        history.add(new Price((int) secret % 10, 0));

        for (int i = 1; i < 2000; i++) {
            long result = secret * 64;
            secret ^= result;
            secret %= 16777216;

            result = secret / 32;
            secret ^= result;
            secret %= 16777216;

            result = secret * 2048;
            secret ^= result;
            secret %= 16777216;

            int price = (int) secret % 10;
            int change = price - history.get(i - 1).price;
            history.add(new Price(price, change));
        }

        return history;
    }

    private static List<Map<Sequence, Integer>> cache = null;

    private static int getSequenceSum(List<List<Price>> histories, Sequence seq)
    {
        if (cache == null) {
            cache = new ArrayList<>(histories.size());
            for (int i = 0; i < histories.size(); i++) cache.add(new HashMap<>());
        }

        int result = 0;

        for (int h = 0; h < histories.size(); h++) {
            var history = histories.get(h);
            if (cache.get(h).getOrDefault(seq, null) != null) {
                result += cache.get(h).get(seq);
                continue;
            }

            for (int i = 1; i < history.size() - 3; i++) {
                if (history.get(i).change == seq.first && history.get(i + 1).change == seq.second
                    && history.get(i + 2).change == seq.third && history.get(i + 3).change == seq.fourth
                ) {
                    int value = history.get(i + 3).price;
                    cache.get(h).put(seq, value);
                    result += value;
                    break;
                }
            }
        }

        return result;
    }
    
    private static int testSequences(List<List<Price>> histories)
    {
        Set<Sequence> tested = new HashSet<>();
        int max = 0;

        for (var refHistory : histories) {
            for (int i = 1; i < histories.getFirst().size() - 3; i++) {
                var sequence = new Sequence(refHistory.get(i).change, refHistory.get(i + 1).change, refHistory.get(i + 2).change, refHistory.get(i + 3).change);
                if (!tested.add(sequence)) continue;
    
                int foundSequenceSum = getSequenceSum(histories, sequence);
                max = Math.max(max, foundSequenceSum);
            }
        }

        return max;
    }
}

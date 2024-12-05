import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        Map<Integer, Set<Integer>> rules = new HashMap<>();
        List<List<Integer>> updates;

        var parts = Files.readString(Path.of("input.txt")).split("\n\n");

        Arrays.stream(parts[0].split("\n"))
            .forEach(line -> {
                var nums = line.split("\\|");
                var l = rules.computeIfAbsent(Integer.parseInt(nums[0]), HashSet::new);
                l.add(Integer.parseInt(nums[1]));
            });

        updates = Arrays.stream(parts[1].split("\n"))
            .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).collect(Collectors.toList()))
            .collect(Collectors.toList());

        int result = updates.stream()
            .map(update -> {
                for (int i = 0; i < update.size(); i++) {
                    int u = update.get(i);
                    if (rules.containsKey(u)) {
                        for (int j = 0; j < i; j++) {
                            if (rules.get(u).contains(update.get(j))) {
                                return 0;
                            }
                        }
                    }
                }
                return update.get(update.size()/2);
            })
            .reduce(0, (sum, i) -> sum + i);

        System.out.println(result);
    }
}

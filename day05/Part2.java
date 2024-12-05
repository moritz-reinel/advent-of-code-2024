import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        Map<Integer, Set<Integer>> rulesAfter = new HashMap<>();
        Map<Integer, Set<Integer>> rulesBefore = new HashMap<>();
        List<List<Integer>> updates;

        var parts = Files.readString(Path.of("input.txt")).split("\n\n");

        Arrays.stream(parts[0].split("\n"))
            .forEach(line -> {
                var nums = line.split("\\|");
                var l = rulesAfter.computeIfAbsent(Integer.parseInt(nums[0]), HashSet::new);
                l.add(Integer.parseInt(nums[1]));
                var l2 = rulesBefore.computeIfAbsent(Integer.parseInt(nums[1]), HashSet::new);
                l2.add(Integer.parseInt(nums[0]));
            });

        updates = Arrays.stream(parts[1].split("\n"))
            .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).collect(Collectors.toList()))
            .collect(Collectors.toList());

        List<List<Integer>> wrong = updates.stream()
            .filter(update -> {
                for (int i = 0; i < update.size(); i++) {
                    int u = update.get(i);
                    if (rulesAfter.containsKey(u)) {
                        for (int j = 0; j < i; j++) {
                            if (rulesAfter.get(u).contains(update.get(j))) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            })
            .collect(Collectors.toList());

        int result = wrong.stream()
            .map(update -> {
                List<Integer> correct = new ArrayList<>();
                Queue<Integer> next = new ArrayDeque<>();
                Map<Integer, Integer> beforeLeft = new HashMap<>();
                update.stream().forEach(u -> {
                    List<Integer> n = new ArrayList<>();
                    if (rulesBefore.containsKey(u)) {
                        for (int m : rulesBefore.get(u)) {
                            if (update.contains(m)) n.add(m);
                        }
                    }
                    beforeLeft.put(u, n.size());
                });
                update.stream().filter(u -> beforeLeft.getOrDefault(u, 0) == 0).forEach(u -> {
                    next.add(u);
                });
                while (!next.isEmpty()) {
                    int n = next.remove();
                    correct.add(n);
                    if (!rulesAfter.containsKey(n)) break;
                    rulesAfter.get(n).forEach(a -> {
                        if (update.contains(a) && beforeLeft.containsKey(a)) {
                            beforeLeft.put(a, beforeLeft.get(a)-1);
                            if (beforeLeft.get(a) == 0) {
                                next.add(a);
                            } 
                        }
                    });
                }
                return correct.get(correct.size()/2);
            })
            .reduce(0, (sum, i) -> sum + i);

        System.out.println(result);
    }
}

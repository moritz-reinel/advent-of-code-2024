import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    record Action(char target, char direction) {};

    // possible next moves depending on the current button
    private static Map<Character, List<Action>> NUM_PAD_NEIGHBOURS = Map.ofEntries(
        Map.entry('0', List.of(new Action('2', '^'), new Action('A', '>'))),
        Map.entry('1', List.of(new Action('4', '^'), new Action('2', '>'))),
        Map.entry('2', List.of(new Action('5', '^'), new Action('3', '>'), new Action('0', 'v'), new Action('1',  '<'))),
        Map.entry('3', List.of(new Action('6', '^'), new Action('A', 'v'), new Action('2', '<'))),
        Map.entry('4', List.of(new Action('7', '^'), new Action('5', '>'), new Action('1', 'v'))),
        Map.entry('5', List.of(new Action('8', '^'), new Action('6', '>'), new Action('2', 'v'), new Action('4', '<'))),
        Map.entry('6', List.of(new Action('9', '^'), new Action('3', 'v'), new Action('5', '<'))),
        Map.entry('7', List.of(new Action('8', '>'), new Action('4', 'v'))),
        Map.entry('8', List.of(new Action('9', '>'), new Action('5', 'v'), new Action('7', '<'))),
        Map.entry('9', List.of(new Action('8', '<'), new Action('6', 'v'))),
        Map.entry('A', List.of(new Action('3', '^'), new Action('0', '<')))
    );

    private static Map<Character, List<Action>> DIR_PAD_NEIGHBOURS = Map.of(
        'A', List.of(new Action('>', 'v'), new Action('^', '<')),
        '^', List.of(new Action('A', '>'), new Action('v', 'v')),
        'v', List.of(new Action('^', '^'), new Action('>', '>'), new Action('<', '<')),
        '<', List.of(new Action('v', '>')),
        '>', List.of(new Action('A', '^'), new Action('v', '<'))
    );

    public static void main(String[] args) throws IOException
    {
        var codes = Files.readAllLines(Path.of("input.txt"));

        long result = codes.stream().map(code -> calcButtons(code)).reduce(0L, (sum, x) -> sum + x);
        System.out.println(result);
    }

    record TodoItem(char button, List<Character> path) {};

    private static List<List<Character>> bfs(char start, char target, Map<Character, List<Action>> pad)
    {
        List<List<Character>> result = new LinkedList<>();
        int shortest = -1;

        var todo = new LinkedList<TodoItem>();
        todo.add(new TodoItem(start, new LinkedList<>()));

        while (!todo.isEmpty()) {
            var current = todo.removeFirst();
            var path = current.path;

            if (current.button == target) {
                if (shortest == -1) shortest = path.size();

                if (path.size() == shortest) {
                    var newPath = new LinkedList<>(path);
                    newPath.add('A');
                    result.add(newPath);
                }

                continue;
            }

            if (shortest != -1 && path.size() >= shortest) continue;

            for (var neighbour : pad.get(current.button)) {
                var nextPath = new LinkedList<>(path);
                nextPath.add(neighbour.direction);

                todo.add(new TodoItem(neighbour.target, nextPath));
            }
        }

        assert shortest != -1;
        return result;
    }

    private static Map<String, Long> cache = new HashMap<>();

    private static long dfs(List<Character> sequence, int level, boolean isNumpad)
    {
        String key = sequence.toString() + level + isNumpad;
        if (cache.containsKey(key)) return cache.get(key);

        var pad = isNumpad ? NUM_PAD_NEIGHBOURS : DIR_PAD_NEIGHBOURS;
        sequence.addFirst('A');
        long result = 0;

        for (int i = 0; i < sequence.size() - 1; i++) {
            var paths = bfs(sequence.get(i), sequence.get(i + 1), pad);
            result += (level == 0)
                ? paths.stream().map(p -> p.size()).min(Integer::compare).orElseThrow()
                : paths.stream().map(p -> dfs(p, level - 1, false)).min(Long::compare).orElseThrow();
        }

        cache.put(key, result);
        return result;
    }

    private static long calcButtons(String code)
    {
        var btns = code.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        long buttons = dfs(btns, 25, true);

        int value = Integer.parseInt(code.substring(0, code.length() - 1));

        return value * buttons;
    }
}

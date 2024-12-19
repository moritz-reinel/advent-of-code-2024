import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    record SearchItem(String soFar, String append) {};

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;

        var towels = input[0].split(", ");

        int result = input[1].lines().map(pattern -> {
            Stack<SearchItem> todo = new Stack<>();
            todo.add(new SearchItem("", ""));

            while (!todo.isEmpty()) {
                var next = todo.pop();
                var tmp = next.soFar + next.append;

                if (tmp.equals(pattern)) return 1;
                if (!pattern.startsWith(tmp) || tmp.length() >= pattern.length()) continue;

                char nextStart = pattern.charAt(tmp.length());
                Arrays.stream(towels).filter(t -> t.charAt(0) == nextStart).forEach(t -> todo.push(new SearchItem(tmp, t)));
            }

            return 0;
        }).reduce(0, (sum, x) -> sum + x);

        System.out.println(result);
    }
}

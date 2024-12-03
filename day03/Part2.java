import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        String in = Files.readString(Path.of("input.txt"));

        String regex = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(in);

        long result = 0;
        boolean enabled = true;

        while (matcher.find()) {
            String match = matcher.group();

            if (match.startsWith("don't")) {
                enabled = false;
                continue;
            }

            if (match.startsWith("do")) {
                enabled = true;
                continue;
            }

            if (enabled) {
                int first = Integer.parseInt(matcher.group(1));
                int second = Integer.parseInt(matcher.group(2));
                result += first * second;
            }
        }

        System.out.println(result);
    }
}

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        String in = Files.readString(Path.of("input.txt"));

        String regex = "mul\\((\\d+),(\\d+)\\)";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(in);

        long result = 0;
        while (matcher.find()) {
            int first = Integer.parseInt(matcher.group(1));
            int second = Integer.parseInt(matcher.group(2));
            result += first * second;
        }

        System.out.println(result);
    }
}

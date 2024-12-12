import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        List<Long> stones = Arrays.stream(Files.readString(Path.of("input.txt")).trim().split(" "))
            .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());

        for (int i = 0; i < 25; i++) {
            for (int s = 0; s < stones.size(); s++) {
                long stone = stones.get(s);
                String stoneStr;

                if (stone == 0) {
                    stones.set(s, 1L);
                } else if ((stoneStr = String.valueOf(stone)).length() % 2 == 0) {
                    long front = Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2));
                    long back = Long.parseLong(stoneStr.substring(stoneStr.length() / 2));

                    stones.set(s, front);
                    stones.add(s + 1, back);
                    s++;
                } else {
                    stones.set(s, stone * 2024);
                }
            }
        }

        System.out.println(stones.size());
    }
}

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part2
{
    record Point(int x, int y) {};

    private static int FIELD_WIDTH  = 101;
    private static int FIELD_HEIGHT = 103;

    // height of the "tree pyramid" to be searched for;
    // after looking at the vizualization of the correct solution, the maximum to be tested for is 11 (pyramid
    // basis is the first tree part upwards the stem); however for me, every value starting with 3 up to 11 worked
    private static int CHECK_HEIGHT       = 11;
    private static boolean SHOW_FINAL_MAP = true;  // show vizualization after finding the tree

    public static void main(String[] args) throws IOException
    {
        List<Point> robots = new ArrayList<>();
        List<Point> movements = new ArrayList<>();

        Pattern pattern = Pattern.compile("(-?\\d+)");

        Files.lines(Path.of("input.txt")).forEach(line -> {
            var nums = new ArrayList<Integer>(4);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) nums.add(Integer.parseInt(matcher.group()));
            assert nums.size() == 4;

            robots.add(new Point(nums.get(0), nums.get(1)));
            movements.add(new Point(nums.get(2), nums.get(3)));
        });

        boolean treeFound = false;
        int seconds = 0;
        clock: for (; ; seconds++) {
            boolean[][] map = new boolean[FIELD_HEIGHT][FIELD_WIDTH];
            for (int i = 0; i < robots.size(); i++) {
                var robot = robots.get(i);
                var move = movements.get(i);
                
                int newX = robot.x + move.x;
                if (newX < 0) newX += FIELD_WIDTH;
                else if (newX >= FIELD_WIDTH) newX -= FIELD_WIDTH;
                
                int newY = robot.y + move.y;
                if (newY < 0) newY += FIELD_HEIGHT;
                else if (newY >= FIELD_HEIGHT) newY -= FIELD_HEIGHT;
                
                robots.set(i, new Point(newX, newY));
                map[newY][newX] = true;
            }

            for (var r : robots) {
                if (r.x < CHECK_HEIGHT || r.x > FIELD_WIDTH - CHECK_HEIGHT
                    || r.y < CHECK_HEIGHT || r.y > FIELD_HEIGHT - CHECK_HEIGHT) continue;

                treeCheck: {
                    for (int y = 0; y < CHECK_HEIGHT; y++) {
                        int absY = r.y + y;
                        int width = 2 * y + 1;
                        for (int x = 0; x < width; x++) {
                            int absX = r.x - width / 2 + x;
                            if (!map[absY][absX]) break treeCheck;
                        }
                    }
                    treeFound = true;
                }

                if (treeFound) {
                    if (SHOW_FINAL_MAP) {
                        for (int y = 0; y < FIELD_HEIGHT; y++) {
                            for (int x = 0; x < FIELD_WIDTH; x++) {
                                boolean isRobot = map[y][x];
                                System.out.print(isRobot ? '#' : ' ');
                            }
                            System.out.println();
                        }
                    }

                    break clock;
                }
            }
        }

        System.out.println(seconds + 1);
    }
}

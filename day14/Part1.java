import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part1
{
    record Point(int x, int y) {};

    private static int FIELD_WIDTH  = 101;
    private static int FIELD_HEIGHT = 103;
    private static int SECONDS      = 100;

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

        for (int _s = 0; _s < SECONDS; _s++) {
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
            }
        }

        int quadrant1 = 0, quadrant2 = 0, quadrant3 = 0, quadrant4 = 0;
        for (var r : robots) {
            if (r.x < FIELD_WIDTH / 2) {
                if (r.y < FIELD_HEIGHT / 2) quadrant2++;
                else if (r.y > FIELD_HEIGHT / 2) quadrant3++;
            } else if (r.x > FIELD_WIDTH / 2) {
                if (r.y < FIELD_HEIGHT / 2) quadrant1++;
                else if (r.y > FIELD_HEIGHT / 2) quadrant4++;
            }
        }

        long result = quadrant1 * quadrant2 * quadrant3 * quadrant4;
        System.out.println(result);
    }
}

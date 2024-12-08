import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    record Antenna(int x, int y, char v){};

    public static void main(String[] args) throws IOException
    {
        List<String> map = Files.readAllLines(Paths.get("input.txt"));
        int rows = map.size();
        int cols = map.getFirst().length();

        List<Antenna> antennas = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char x;
                if ((x = map.get(r).charAt(c)) != '.') {
                    antennas.add(new Antenna(c, r, x));
                }
            }
        }

        Set<Antenna> antinodes = new HashSet<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (Antenna a : antennas) {
                    for (Antenna b : antennas) {
                        if (a.equals(b) || a.v != b.v) continue;

                        int aManhattan = Math.abs(r-a.y) + Math.abs(c-a.x);
                        int bManhattan = Math.abs(r-b.y) + Math.abs(c-b.x);

                        int aDistX = c - a.x;
                        int aDistY = r - a.y;
                        int bDistX = c - b.x;
                        int bDistY = r - b.y;

                        if ((aManhattan == 2 * bManhattan || bManhattan == 2 * aManhattan) && (aDistY * bDistX == aDistX * bDistY)) {
                            antinodes.add(new Antenna(c, r, 'x'));
                        }
                    }
                }
            }
        }

        System.out.println(antinodes.size());
    }
}

import java.io.*;
import java.nio.file.*;

public class Part1
{
    enum Tile { WALL, AIR, BOX };

    private static Tile[][] map;
    private static int rx = -1, ry = -1;

    private final static boolean DEBUG_SHOW_MAP = false;

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;
        
        var moves = input[1].replace("\n", "").trim();

        var lines = input[0].split("\n");
        map = new Tile[lines.length][lines[0].length()];

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[0].length(); x++) {
                switch (lines[y].charAt(x)) {
                    case '#': map[y][x] = Tile.WALL; break;
                    case '.': map[y][x] = Tile.AIR;  break;
                    case 'O': map[y][x] = Tile.BOX;  break;
                    case '@': rx = x; ry = y;
                              map[y][x] = Tile.AIR;  break;
                    default:  assert false; break;
                }
            }
        }
        assert rx != -1 && ry != -1;

        printMap();
        moves.chars().forEach(move -> {
            if (DEBUG_SHOW_MAP) System.out.println("Moving " + (char) move);
            switch ((char) move) {
                case '^': moveUp();    break;
                case 'v': moveDown();  break;
                case '<': moveLeft();  break;
                case '>': moveRight(); break;
            }
            printMap();
        });

        int result = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == Tile.BOX) {
                    result += 100 * y + x;
                }
            }
        }

        System.out.println(result);
    }

    private static void moveUp()
    {
        Tile up = map[ry-1][rx];
        if (up == Tile.WALL) return;
        else if (up == Tile.AIR) ry--;
        else {
            for (int y = ry-2; y > 0; y--) {
                Tile tmp = map[y][rx];
                if (tmp == Tile.WALL) return;
                else if (tmp == Tile.BOX) continue;
                else {
                    map[y][rx] = Tile.BOX;
                    map[ry-1][rx] = Tile.AIR;
                    ry--;
                    return;
                }
            }
        }
    }

    private static void moveDown()
    {
        Tile down = map[ry+1][rx];
        if (down == Tile.WALL) return;
        else if (down == Tile.AIR) ry++;
        else {
            for (int y = ry+2; y < map.length; y++) {
                Tile tmp = map[y][rx];
                if (tmp == Tile.WALL) return;
                else if (tmp == Tile.BOX) continue;
                else {
                    map[y][rx] = Tile.BOX;
                    map[ry+1][rx] = Tile.AIR;
                    ry++;
                    return;
                }
            }
        }
    }

    private static void moveLeft()
    {
        Tile left = map[ry][rx-1];
        if (left == Tile.WALL) return;
        else if (left == Tile.AIR) rx--;
        else {
            for (int x = rx-2; x > 0; x--) {
                Tile tmp = map[ry][x];
                if (tmp == Tile.WALL) return;
                else if (tmp == Tile.BOX) continue;
                else {
                    map[ry][x] = Tile.BOX;
                    map[ry][rx-1] = Tile.AIR;
                    rx--;
                    return;
                }
            }
        }
    }

    private static void moveRight()
    {
        Tile right = map[ry][rx+1];
        if (right == Tile.WALL) return;
        else if (right == Tile.AIR) rx++;
        else {
            for (int x = rx+2; x < map[0].length; x++) {
                Tile tmp = map[ry][x];
                if (tmp == Tile.WALL) return;
                else if (tmp == Tile.BOX) continue;
                else {
                    map[ry][x] = Tile.BOX;
                    map[ry][rx+1] = Tile.AIR;
                    rx++;
                    return;
                }
            }
        }
    }

    private static void printMap()
    {
        if (!DEBUG_SHOW_MAP) return;

        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (r == ry && c == rx) {
                    System.out.print('@');
                    continue;
                }

                switch (map[r][c]) {
                    case WALL: System.out.print('#'); break;
                    case AIR:  System.out.print('.'); break;
                    case BOX:  System.out.print('O'); break;
                }
            }
            System.out.println();
        }
        System.out.println("Robot x: " + rx + ", y: " + ry + "\n");
    }
}

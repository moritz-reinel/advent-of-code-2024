import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part2
{
    enum Tile { WALL, AIR, BOX_LEFT, BOX_RIGHT };

    record Point(int x, int y) {};

    private static Tile[][] map;
    private static int rx = -1, ry = -1;

    private final static boolean DEBUG_SHOW_MAP = false;

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;
        
        var moves = input[1].replace("\n", "").trim();

        var lines = input[0].split("\n");
        map = new Tile[lines.length][lines[0].length() * 2];

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[0].length(); x++) {
                switch (lines[y].charAt(x)) {
                    case '#': map[y][x * 2] = map[y][x * 2 + 1] = Tile.WALL; break;
                    case '.': map[y][x * 2] = map[y][x * 2 + 1] = Tile.AIR;  break;
                    case 'O': map[y][x * 2] = Tile.BOX_LEFT;
                              map[y][x * 2 + 1] = Tile.BOX_RIGHT; break;
                    case '@': rx = x * 2; ry = y;
                              map[y][x * 2] = map[y][x * 2 + 1] = Tile.AIR;  break;
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
                if (map[y][x] == Tile.BOX_LEFT) {
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
            Set<Point> connected = new HashSet<>();
            boolean possible = testUpRec(connected, rx, ry-1);
            if (possible) _moveUp(rx, ry-1, connected);
        }
    }

    private static boolean testUpRec(Set<Point> connected, int x, int y)
    {
        if (connected.contains(new Point(x, y))) return true;

        Tile here = map[y][x];
        if (here == Tile.WALL) {
            return false;
        } else if (here == Tile.AIR) {
            return true;
        } else if (here == Tile.BOX_LEFT) {
            connected.add(new Point(x, y));
            return testUpRec(connected, x+1, y) && testUpRec(connected, x, y-1);
        } else {
            connected.add(new Point(x, y));
            return testUpRec(connected, x-1, y) && testUpRec(connected, x, y-1);
        }
    }
    
    private static void _moveUp(int x, int y, Set<Point> boxes)
    {
        Tile[][] copy = Arrays.stream(map).map(Tile[]::clone).toArray(Tile[][]::new);

        var sorted = new ArrayList<>(boxes);
        Collections.sort(sorted, (a, b) -> a.y - b.y);
        for (var b : sorted) {
            map[b.y][b.x] = Tile.AIR;
            map[b.y - 1][b.x] = copy[b.y][b.x];
        }

        ry--;
    }

    private static void moveDown()
    {
        Tile down = map[ry+1][rx];
        if (down == Tile.WALL) return;
        else if (down == Tile.AIR) ry++;
        else {
            Set<Point> connected = new HashSet<>();
            boolean possible = testDownRec(connected, rx, ry+1);
            if (possible) _moveDown(rx, ry+1, connected);
        }
    }

    private static boolean testDownRec(Set<Point> connected, int x, int y)
    {
        if (connected.contains(new Point(x, y))) return true;

        Tile here = map[y][x];
        if (here == Tile.WALL) {
            return false;
        } else if (here == Tile.AIR) {
            return true;
        } else if (here == Tile.BOX_LEFT) {
            connected.add(new Point(x, y));
            return testDownRec(connected, x+1, y) && testDownRec(connected, x, y+1);
        } else {
            connected.add(new Point(x, y));
            return testDownRec(connected, x-1, y) && testDownRec(connected, x, y+1);
        }
    }
    
    private static void _moveDown(int x, int y, Set<Point> boxes)
    {
        Tile[][] copy = Arrays.stream(map).map(Tile[]::clone).toArray(Tile[][]::new);

        var sorted = new ArrayList<>(boxes);
        Collections.sort(sorted, (a, b) -> b.y - a.y);
        for (var b : sorted) {
            map[b.y][b.x] = Tile.AIR;
            map[b.y + 1][b.x] = copy[b.y][b.x];
        }

        ry++;
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
                else if (tmp == Tile.BOX_LEFT || tmp == Tile.BOX_RIGHT) continue;
                else {
                    map[ry][x] = Tile.BOX_LEFT;
                    for (int xn = x+1; xn < rx - 1; xn++) {
                        map[ry][xn] = (map[ry][xn] == Tile.BOX_LEFT) ? Tile.BOX_RIGHT : Tile.BOX_LEFT;
                    }
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
                else if (tmp == Tile.BOX_LEFT || tmp == Tile.BOX_RIGHT) continue;
                else {
                    map[ry][x] = Tile.BOX_RIGHT;
                    for (int xn = x-1; xn > rx + 1; xn--) {
                        map[ry][xn] = (map[ry][xn] == Tile.BOX_LEFT) ? Tile.BOX_RIGHT : Tile.BOX_LEFT;
                    }
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
                    case WALL:      System.out.print('#'); break;
                    case AIR:       System.out.print('.'); break;
                    case BOX_LEFT:  System.out.print('['); break;
                    case BOX_RIGHT: System.out.print(']'); break;
                }
            }
            System.out.println();
        }
        System.out.println("Robot x: " + rx + ", y: " + ry + "\n");
    }
}

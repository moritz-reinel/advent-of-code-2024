import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    record Gate(String a, String operation, String b, String result) {};

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;
        
        Map<String, Boolean> wires = new HashMap<>();
        input[0].lines().forEach(line -> {
            var parts = line.split(": ");
            assert parts.length == 2;
            wires.put(parts[0], Integer.parseInt(parts[1]) == 0 ? false : true);
        });
        
        Queue<Gate> gates = new LinkedList<>();
        input[1].lines().forEach(line -> {
            var parts = line.split(" ");
            assert parts.length == 5;
            gates.add(new Gate(parts[0], parts[1], parts[2], parts[4]));
        });

        while (!gates.isEmpty()) {
            var element = gates.poll();

            if (!wires.containsKey(element.a) || !wires.containsKey(element.b)) {
                gates.add(element);
                continue;
            }

            if (element.operation.equals("AND")) {
                wires.put(element.result, wires.get(element.a) && wires.get(element.b));
            } else if (element.operation.equals("OR")) {
                wires.put(element.result, wires.get(element.a) || wires.get(element.b));
            } else if (element.operation.equals("XOR")) {
                wires.put(element.result, wires.get(element.a) ^ wires.get(element.b));
            } else {
                assert false;
            }
        }

        var numberStr = wires.entrySet().stream()
            .filter(e -> e.getKey().charAt(0) == 'z')
            .sorted(Comparator.comparing(Map.Entry<String, Boolean>::getKey).reversed())
            .map(e -> e.getValue() ? "1" : "0")
            .collect(Collectors.joining());

        long result = Long.parseLong(numberStr, 2);
        System.out.println(result);
    }
}

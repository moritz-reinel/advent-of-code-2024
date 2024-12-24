import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Part2
{
    record Gate(String a, String operation, String b, String output)
    {
        boolean isFirstInput() {
            return this.a.equals("x00") || this.b.equals("x00");
        }

        boolean isInput() {
            return this.a.charAt(0) == 'x' || this.b.charAt(0) == 'x';
        }

        boolean isOutput() {
            return this.output.charAt(0) == 'z';
        }

        boolean isGate(String gate) {
            return this.operation.equals(gate);
        }

        boolean hasInput(String input) {
            return this.a.equals(input) || this.b.equals(input);
        }

        boolean hasOutput(String output) {
            return this.output.equals(output);
        }
    };

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;

        int bitCount = input[0].split("\n").length / 2;

        Queue<Gate> gates = new LinkedList<>();
        input[1].lines().forEach(line -> {
            var parts = line.split(" ");
            assert parts.length == 5;
            gates.add(new Gate(parts[0], parts[1], parts[2], parts[4]));
        });

        // From now on, basically checking the circuit for errors that would make it not a full adder
        Set<String> wrongs = new HashSet<>();

        // Full adder: https://circuitdigest.com/tutorial/full-adder-circuit-theory-truth-table-construction
        // A    XOR  B    ->  IM0   (GateType0)
        // A    AND  B    ->  IM1   (GateType1)
        // IM0  AND  CIN  ->  IM3   (GateType2)
        // IM0  XOR  CIN  ->  SUM   (GateType3)
        // IM1  OR   IM3  ->  COUT  (GateType4)

        // check all GateType0s to be XORs resulting in IM0s
        var gateType0s = gates.stream().filter(Gate::isInput).filter(g -> g.isGate("XOR")).collect(Collectors.toList());
        gateType0s.forEach(g -> {
            if (g.isFirstInput()) {
                // only first gate shall yield the output z00
                if (!g.output.equals("z00")) {
                    wrongs.add(g.output);
                }
                return;
            }

            if (g.output.equals("z00")) wrongs.add(g.output);

            if (g.isOutput()) wrongs.add(g.output);
        });

        // check all GateType3s to be intermediate gates and to output to a z
        var gateType3s = gates.stream().filter(Predicate.not(Gate::isInput)).filter(g -> g.isGate("XOR")).collect(Collectors.toList());
        gateType3s.forEach(g -> {
            if (!g.isOutput())
            wrongs.add(g.output);
        });

        // check all output gates to be IM0 XOR CIN to output to SUM
        var gateOutputs = gates.stream().filter(Gate::isOutput).collect(Collectors.toList());
        gateOutputs.forEach(g -> {
            boolean isLast = g.output.equals("z" + bitCount);
            if (isLast) {
                // only the last output gate has to be IM1 OR IM2 to be COUT
                if (!g.isGate("OR")) {
                    wrongs.add(g.output);
                }
                return;
            }

            if (!g.isGate("XOR")) wrongs.add(g.output);
        });

        // check all GateType0s to yield a GateType3
        List<Gate> toCheck = new ArrayList<>();
        gateType0s.forEach(gate -> {
            if (wrongs.contains(gate.output) || gate.output.equals("z00")) return;

            var matchesCount = gateType3s.stream().filter(g -> g.hasInput(gate.output)).count();
            if (matchesCount == 0) {
                toCheck.add(gate);
                wrongs.add(gate.output);
            }
        });

        toCheck.forEach(gate -> {
            // from vizualization of the graph can be concluded that xXX and yXX always yield zXX
            var expected = "z" + gate.a.substring(1);

            // should yield only one match
            var matches = gateType3s.stream().filter(g -> g.hasOutput(expected)).collect(Collectors.toList());
            assert matches.size() == 1;
            var match = matches.getFirst();

            // should yield only one match
            var orMatches = gates.stream().filter(g -> g.isGate("OR")).filter(g -> g.output.equals(match.a) || g.output.equals(match.b)).collect(Collectors.toList());
            assert orMatches.size() == 1;

            var correctOutput = match.a.equals(orMatches.getFirst().output) ? match.b : match.a;
            wrongs.add(correctOutput);
        });
        assert wrongs.size() == 8;

        var result = wrongs.stream().sorted().collect(Collectors.joining(","));
        System.out.println(result);
    }
}

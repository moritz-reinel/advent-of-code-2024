import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;
        
        Map<Character, Integer> registers = new HashMap<>();
        input[0].lines().forEach(line -> {
            var l = line.split(": ");
            assert l.length == 2;
            char register = l[0].charAt(l[0].length() - 1);
            int value = Integer.parseInt(l[1].trim());
            registers.put(register, value);
        });

        List<Integer> program = Arrays.stream(input[1].trim().split(": ")[1].split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        int ip = 0;
        String output = "";

        while (ip < program.size() - 1) {
            int opcode = program.get(ip++);

            int literalOperand = program.get(ip++);

            int comboOperand;
            switch (literalOperand) {
                case 0, 1, 2, 3 -> comboOperand = literalOperand;
                case 4          -> comboOperand = registers.get('A');
                case 5          -> comboOperand = registers.get('B');
                case 6          -> comboOperand = registers.get('C');
                default         -> { comboOperand = 0; assert false; }
            }

            switch (opcode) {
                case 0 -> registers.put('A', registers.get('A') / (int) Math.pow(2, comboOperand));
                case 1 -> registers.put('B', registers.get('B') ^ literalOperand);
                case 2 -> registers.put('B', comboOperand % 8);
                case 3 -> { if (registers.get('A') != 0) ip = (int) comboOperand; }
                case 4 -> registers.put('B', registers.get('B') ^ registers.get('C'));
                case 5 -> output += (output.length() == 0 ? "" : ",") + (comboOperand % 8);
                case 6 -> registers.put('B', registers.get('A') / (int) Math.pow(2, comboOperand));
                case 7 -> registers.put('C', registers.get('A') / (int) Math.pow(2, comboOperand));
            }
        }

        System.out.println(output);
    }
}

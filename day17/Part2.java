import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt")).split("\n\n");
        assert input.length == 2;

        Map<Character, Integer> originalRegisters = new HashMap<>();
        input[0].lines().forEach(line -> {
            var l = line.split(": ");
            assert l.length == 2;
            char register = l[0].charAt(l[0].length() - 1);
            int value = Integer.parseInt(l[1].trim());
            originalRegisters.put(register, value);
        });

        String programString = input[1].trim().split(": ")[1];
        List<Integer> program = Arrays.stream(programString.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        // printInstructions(program, originalRegisters);
        
        long a = 0;
        for (int i = 0; i < program.size(); i++) {
            List<Integer> target = program.subList(program.size() - i - 1, program.size());
            List<Integer> output = new ArrayList<>();
            long aTemp = a << 3;

            while (!target.equals(output)) {
                Map<Character, Long> registers = new HashMap<>();
                registers.put('A', aTemp);
                registers.put('B', 0L);
                registers.put('C', 0L);

                output = run(program, registers);
                aTemp++;
            }

            a = aTemp - 1;
        }

        System.out.println(a);
    }

    private static List<Integer> run(List<Integer> program, Map<Character, Long> registers)
    {
        List<Integer> output = new ArrayList<>();
        int ip = 0;

        while (ip < program.size() - 1) {
            int opcode = program.get(ip++);
            int literalOperand = program.get(ip++);

            long comboOperand;
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
                case 5 -> output.add((int) (comboOperand % 8));
                case 6 -> registers.put('B', registers.get('A') / (int) Math.pow(2, comboOperand));
                case 7 -> registers.put('C', registers.get('A') / (int) Math.pow(2, comboOperand));
            }
        }
        return output;
    }

    @SuppressWarnings("unused")
    private static void printInstructions(List<Integer> program, Map<Character, Integer> registers)
    {
        char operands[] = { '0', '1', '2', '3', 'A', 'B', 'C' };
        int ip = 0;

        while (ip < program.size() - 1) {
            int opcode = program.get(ip++);
            int literalOperand = program.get(ip++);

            switch (opcode) {
                case 0 -> System.out.println("A = A / 2 ** " + operands[literalOperand]);
                case 1 -> System.out.println("B = B ^ " + literalOperand);
                case 2 -> System.out.println("B = " + operands[literalOperand] + " % 8");
                case 3 -> System.out.println("jnz " + operands[literalOperand]);
                case 4 -> System.out.println("B = B ^ C");
                case 5 -> System.out.println("output " + operands[literalOperand] + " % 8");
                case 6 -> System.out.println("B = A / 2 ** " + operands[literalOperand]);
                case 7 -> System.out.println("C = A / 2 ** " + operands[literalOperand]);
            }
        }
    }
}

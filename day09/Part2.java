import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.IntStream;

public class Part2
{
    record File(int id, int size){};

    public static void main(String[] args) throws IOException
    {
        var input = Files.readString(Path.of("input.txt"));

        List<Integer> out = new ArrayList<>();
        List<File> files = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            int x = input.charAt(i) - '0';
            for (int j = 0; j < x; j++) {
                if (i % 2 == 0) {
                    out.add(i / 2);
                } else {
                    out.add(-1);
                }
            }

            if (i % 2 == 0) files.add(new File(i / 2, x));
        }

        Collections.sort(files, (f1, f2) -> f2.id - f1.id);

        for (var file : files) {
            boolean fits = false;
            int space = out.indexOf(-1);

            space: for (int s = space; s < out.size() && !fits; s++) {
                for (int i = 0; i < file.size; i++) {
                    if (out.get(s + i) != -1 || s + i >= out.size()) continue space;
                }

                space = s;
                fits = true;
            }

            if (fits && space < out.indexOf(file.id)) {
                int removeIdx = out.indexOf(file.id);
                for (int s = 0; s < file.size; s++) {
                    out.set(space + s, file.id);
                    out.set(removeIdx + s, -1);
                }
            }
        }
        
        long result = IntStream.range(0, out.size()).mapToLong(i -> i * Math.max(out.get(i), 0)).sum();
        System.out.println(result);
    }
}

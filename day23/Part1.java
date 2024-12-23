import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        Map<String, List<String>> connections = new HashMap<>();

        Files.lines(Path.of("input.txt")).forEach(line -> {
            var hosts = line.split("-");
            connections.computeIfAbsent(hosts[0], h -> new ArrayList<String>()).add(hosts[1]);
            connections.computeIfAbsent(hosts[1], h -> new ArrayList<String>()).add(hosts[0]);
        });

        var hosts = new ArrayList<>(connections.keySet());

        int counter = 0;
        for (int ai = 0; ai < hosts.size(); ai++) {
            var a = hosts.get(ai);
            for (int bi = ai + 1; bi < hosts.size(); bi++) {
                var b = hosts.get(bi);
                for (int ci = bi + 1; ci < hosts.size(); ci++) {
                    var c = hosts.get(ci);

                    if (connections.get(b).contains(a) && connections.get(c).contains(a) && connections.get(c).contains(b)
                        && (a.charAt(0) == 't' || b.charAt(0) == 't' || c.charAt(0) == 't'))
                        counter++;
                }
            }
        }

        System.out.println(counter);
    }
}

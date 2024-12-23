import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        Map<String, List<String>> connections = new HashMap<>();

        Files.lines(Path.of("input.txt")).forEach(line -> {
            var hosts = line.split("-");
            connections.computeIfAbsent(hosts[0], h -> new ArrayList<String>()).add(hosts[1]);
            connections.computeIfAbsent(hosts[1], h -> new ArrayList<String>()).add(hosts[0]);
        });

        Set<String> biggestParty = new HashSet<>();

        for (var initialHost : connections.keySet()) {
            Set<String> party = new HashSet<>();
            party.add(initialHost);

            testNewAttendee:
            for (var otherHost : connections.keySet()) {
                for (var attendee : party) {
                    if (!connections.get(attendee).contains(otherHost)) continue testNewAttendee;
                }
                party.add(otherHost);
            }

            if (party.size() > biggestParty.size()) biggestParty = party;
        }

        var result = biggestParty.stream().sorted().collect(Collectors.joining(","));
        System.out.println(result);
    }
}

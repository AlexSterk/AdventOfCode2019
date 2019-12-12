import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
        Map<String, String> keyOrbitsValue = Files.lines(Paths.get("resources/day6.txt")).map(s -> s.split("\\)")).collect(Collectors.toMap(x -> x[1], x -> x[0]));

        System.out.println(keyOrbitsValue.keySet().stream().mapToInt(k -> findPath(k, new ArrayList<>(), keyOrbitsValue).size()).sum());
        System.out.println(pathLength("YOU", "SAN", keyOrbitsValue));
    }

    public static List<String> findPath(String node, List<String> path, Map<String, String> orbits) {
        if (orbits.containsKey(node)) {
            ArrayList<String> nPath = new ArrayList<>(path);
            nPath.add(orbits.get(node));
            return findPath(orbits.get(node), nPath, orbits);
        } else {
            return path;
        }
    }

    public static Number pathLength(String from, String to, Map<String, String> orbits) {
        List<String> pathFrom = findPath(from, new ArrayList<>(), orbits);
        List<String> pathTo = findPath(to, new ArrayList<>(), orbits);

        ArrayList<String> intersect = new ArrayList<>(pathFrom);
        intersect.retainAll(pathTo);

        return pathFrom.stream().takeWhile(s -> !s.equals(intersect.get(0))).count() + pathTo.stream().takeWhile(s -> !s.equals(intersect.get(0))).count();
    }
}

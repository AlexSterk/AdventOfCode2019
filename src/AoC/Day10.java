package AoC;

import other.Vector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("resources/day10.txt"));
        Collection<other.Vector> asteroids = new HashSet<>();

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) == '#') asteroids.add(new other.Vector(x, y));
            }
        }

        Map<other.Vector, SortedSet<other.Vector>> vision = getVisionMap(asteroids);
        other.Vector station = Collections.max(vision.keySet(), Comparator.comparingInt(o -> vision.get(o).size()));
        System.out.println(station);

        SortedSet<other.Vector> detected = vision.get(station);
        System.out.println(detected.size());

        int i = 0;
        int stop = 200;

        while (i + detected.size() < stop) {
            i += detected.size();
            asteroids.removeAll(detected);
            detected = getVisionMap(asteroids).get(station);
        }
        for (other.Vector e : detected) {
            if (++i == stop) {
                System.out.println(e.x * 100 + e.y);
                break;
            }
        }

    }

    private static Map<other.Vector, SortedSet<other.Vector>> getVisionMap(Collection<other.Vector> asteroids) {
        Map<other.Vector, SortedSet<other.Vector>> vision = new HashMap<>();

        for (other.Vector asteroid : asteroids) {
            Collection<other.Vector> distances = new HashSet<>();
            for (other.Vector other : asteroids) {
                if (asteroid == other) continue;
                distances.add(other.minus(asteroid));
            }

            Set<other.Vector> copy = new HashSet<>(distances);
            for (other.Vector vector : distances) {
                copy.removeIf(o -> (Math.abs(o.x) > Math.abs(vector.x) || Math.abs(o.y) > Math.abs(vector.y)) && 1 - vector.cosineSimilarity(o) < 0.0000000001);
            }
            vision.put(asteroid, copy.stream().map(asteroid::add).collect(Collectors.toCollection(() -> new TreeSet<>((o1, o2) -> {
                other.Vector d1 = o1.minus(asteroid);
                Vector d2 = o2.minus(asteroid);

                return Double.compare(d1.getAngle(), d2.getAngle());
            }))));
        }
        return vision;
    }
}

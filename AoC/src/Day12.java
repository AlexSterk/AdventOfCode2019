import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 {
    public static void main(String[] args) throws IOException {
        partOne();
        partTwo();
    }

    private static void partOne() throws IOException {
        List<Vector> moonPositions = Files.lines(Paths.get("AoC/resources/day12.txt")).map(s -> s.split(", ")).map(a -> {
            int x = Integer.parseInt(a[0].substring(3));
            int y = Integer.parseInt(a[1].substring(2));
            int z = Integer.parseInt(a[2].substring(2, a[2].length() - 1));

            return new Vector(x, y, z);
        }).collect(Collectors.toList());
        List<Vector> velocities = new ArrayList<>(Collections.nCopies(moonPositions.size(), new Vector(0, 0, 0)));

        int steps = 1000;

        for (int i = 0; i < steps; i++) {
            for (int m1 = 0; m1 < moonPositions.size(); m1++) {
                applyGravity(moonPositions, velocities, m1);
                updatePosition(moonPositions, m1, velocities.get(m1));
            }
        }
        int energy = IntStream.range(0, moonPositions.size()).map(i -> moonPositions.get(i).asStream().mapToInt(Math::abs).sum() * velocities.get(i).asStream().mapToInt(Math::abs).sum()).sum();
        System.out.println(energy);
    }

    private static void partTwo() throws IOException {
        List<Vector> moonPositions = Files.lines(Paths.get("AoC/resources/day12.txt")).map(s -> s.split(", ")).map(a -> {
            int x = Integer.parseInt(a[0].substring(3));
            int y = Integer.parseInt(a[1].substring(2));
            int z = Integer.parseInt(a[2].substring(2, a[2].length() - 1));

            return new Vector(x, y, z);
        }).collect(Collectors.toList());
        List<Vector> velocities = new ArrayList<>(Collections.nCopies(moonPositions.size(), new Vector(0, 0, 0)));

        List<Integer> periods = new ArrayList<>(Collections.nCopies(3, null));

        List<Vector> initialP = new ArrayList<>(moonPositions);
        List<Vector> initialV = new ArrayList<>(velocities);

        int c = 0;
        while (periods.contains(null)) {
            c++;
            for (int m = 0; m < moonPositions.size(); m++) {
                applyGravity(moonPositions, velocities, m);
                updatePosition(moonPositions, m, velocities.get(m));
            }

            for (int i = 0; i < periods.size(); i++) {
                boolean match = true;
                for (int j = 0; j < initialP.size(); j++) {
                    match = match && (initialP.get(j).get(i) == moonPositions.get(j).get(i) || initialV.get(j).get(i) == velocities.get(j).get(i));
                }
                if (match && periods.get(i) == null) {
                    periods.set(i, c);
                }
            }
        }
        System.out.println(periods);
        System.out.println(periods.stream().mapToLong(x -> x).reduce(Day12::lcm).getAsLong() * 2);
    }

    private static long gcd(long a, long b) {
        return a > 0 ? gcd(b % a, a) : b;
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    private static void applyGravity(List<Vector> moonPositions, List<Vector> velocities, int m1) {
        for (int m2 = m1 + 1; m2 < moonPositions.size(); m2++) {
            Vector moonOne = moonPositions.get(m1);
            Vector moonTwo = moonPositions.get(m2);

            Vector gravity = new Vector(Integer.compare(moonTwo.get(0), moonOne.get(0)), Integer.compare(moonTwo.get(1), moonOne.get(1)), Integer.compare(moonTwo.get(2), moonOne.get(2)));
            velocities.set(m1, velocities.get(m1).add(gravity));
            velocities.set(m2, velocities.get(m2).add(gravity.minus()));
        }
    }

    private static void updatePosition(List<Vector> moonPositions, int m1, Vector vector) {
        moonPositions.set(m1, moonPositions.get(m1).add(vector));
    }
}

package AoC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day3 {
    public static void main(String[] args) throws IOException {
        List<String> wires = Files.readAllLines(Paths.get("resources/day3.txt"));

        List<List<Map.Entry<Integer, Integer>>> orderedPositions = new ArrayList<>();
        List<SortedSet<Map.Entry<Integer, Integer>>> sortedPositions = new ArrayList<>();

        for (String wire : wires) {
            String[] actions = wire.split(",");

            ArrayList<Map.Entry<Integer, Integer>> positions = new ArrayList<>();
            orderedPositions.add(positions);
            TreeSet<Map.Entry<Integer, Integer>> sortedPos = new TreeSet<>((o1, o2) -> {
                int c1 = Integer.compare(o1.getKey(), o2.getKey());
                return (c1 == 0) ? Integer.compare(o1.getValue(), o2.getValue()) : c1;
            });
            sortedPositions.add(sortedPos);

            int x = 0;
            int y = 0;
            for (String move : actions) {
                char action = move.toCharArray()[0];
                int times = Integer.parseInt(move.substring(1));

                int vx = 0, vy = 0;

                switch (action) {
                    case 'U':
                        vy = 1;
                        break;
                    case 'D':
                        vy = -1;
                        break;
                    case 'L':
                        vx = -1;
                        break;
                    case 'R':
                        vx = 1;
                        break;
                }

                for (int i = 0; i < times; i++) {
                    x += vx;
                    y += vy;
                    AbstractMap.SimpleEntry<Integer, Integer> e = new AbstractMap.SimpleEntry<>(x, y);
                    positions.add(e);
                    sortedPos.add(e);
                }
            }
        }
        SortedSet<Map.Entry<Integer, Integer>> intersection = new TreeSet<>(sortedPositions.get(0));
        intersection.retainAll(sortedPositions.get(1));
        System.out.println(intersection.stream().mapToInt(p -> Math.abs(p.getKey()) + Math.abs(p.getValue())).min());
        System.out.println(intersection.stream().mapToInt(p -> orderedPositions.get(0).indexOf(p) + orderedPositions.get(1).indexOf(p) + 2).min());
    }
}

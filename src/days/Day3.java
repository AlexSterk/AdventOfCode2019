package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 extends Day {
    Map<Integer, Rectangle> rectangles;

    @Override
    public void processInput() {
        rectangles = Arrays.stream(input.split("\n")).map(Rectangle::stringToRectangle).collect(Collectors.toMap(Rectangle::id, r -> r));
    }

    @Override
    public void part1() {
        int maxWidth = rectangles.values().stream().map(r -> r.width() + r.x()).max(Comparator.comparingInt(i -> i)).get();
        int maxHeight = rectangles.values().stream().map(r -> r.height() + r.y()).max(Comparator.comparingInt(i -> i)).get();

        int[][] fabric = new int[maxWidth+1][maxHeight+1];

        for (Rectangle r : rectangles.values()) {
            for (int i = r.x(); i < r.x() + r.width(); i++) {
                for (int j = r.y(); j < r.y() + r.height(); j++) {
                    fabric[i][j]++;
                }
            }
        }

        int c = 0;

        for (int[] ints : fabric) {
            for (int anInt : ints) {
                if (anInt >= 2) c++;
            }
        }

        System.out.println(c);
    }

    @Override
    public void part2() {
        Map<Integer, Map<Integer, Set<Integer>>> grid = new HashMap<>();

        HashSet<Integer> noOverlap = new HashSet<>(rectangles.keySet());

        for (Rectangle r : rectangles.values()) {
            for (int i = r.x(); i < r.x() + r.width(); i++) {
                for (int j = r.y(); j < r.y() + r.height(); j++) {
                    Map<Integer, Set<Integer>> grid2 = grid.computeIfAbsent(i, n -> new HashMap<>());
                    Set<Integer> integers = grid2.computeIfAbsent(j, n -> new HashSet<>());
                    integers.add(r.id());
                    if (integers.size() > 1) {
                        noOverlap.removeAll(integers);
                    }
                }
            }
        }

        System.out.println(noOverlap);
    }

    @Override
    public int getDay() {
        return 3;
    }
}

record Rectangle(int id, int x, int y, int width, int height) {
    Rectangle(String id, String x, String y, String width, String height) {
        this(Integer.parseInt(id), Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width), Integer.parseInt(height));
    }

    static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    static Rectangle stringToRectangle(String s) {
        Matcher matcher = PATTERN.matcher(s.trim());
        matcher.matches();
        return new Rectangle(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
    }
}

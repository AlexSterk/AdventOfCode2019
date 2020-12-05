package days;

import setup.Day;

import java.util.*;

public class Day3 extends Day {
    int Input;

    @Override
    public void processInput() {
        Input = Integer.parseInt(input.trim());
    }

    @Override
    public void part1() {
        Level s = new Level(1, 1, 1);
        while (Input > s.end() || Input < s.start()) {
            s = new Level(s.ring() + 2, s.end() + 1);
        }

        int x = s.ring() - 1;
        int y = s.ring() - 1;

        int i = s.end();

        while (x >= 0) {
            if (i == Input) {
                _part1(x, y, s.ring());
                return;
            }
            i--;
            x--;
        }

        x = 0;

        while (y >= 0) {
            if (i == Input) {
                _part1(x, y, s.ring());
                return;
            }
            i--;
            y--;
        }

        y = 0;

        while (x < s.ring()) {
            if (i == Input) {
                _part1(x, y, s.ring());
                return;
            }
            i--;
            x++;
        }

        x = s.ring() - 1;

        while (y < s.ring()) {
            if (i == Input) {
                _part1(x, y, s.ring());
                return;
            }
            i--;
            y++;
        }
    }

    public void _part1(int x, int y, int ring) {
        int mid = (ring - 1) / 2 - 1;
        System.out.println(Math.abs(mid - x) + Math.abs(mid - y));
    }

    @Override
    public void part2() {
        HashMap<Pair, Integer> grid = new HashMap<>();
        grid.put(new Pair(0, 0), 1);

        int i = 1;
        int ring = 1;

        Level l = new Level(ring, 1);
        Pair p = new Pair(0, 0);

        while (true) {
            l = new Level(l.ring() + 2, 1);
//            System.out.println(l);
            
            List<Direction> spiral = new ArrayList<>(List.of(Direction.RIGHT));
            spiral.addAll(Collections.nCopies(l.ring() - 2, Direction.UP));
            spiral.addAll(Collections.nCopies(l.ring() - 1, Direction.LEFT));
            spiral.addAll(Collections.nCopies(l.ring() - 1, Direction.DOWN));
            spiral.addAll(Collections.nCopies(l.ring() - 1, Direction.RIGHT));
            
            for (Direction d : spiral) {
                p = d.update(p);
//                System.out.println(p);
                int value = neighboursSum(getNeighbours(p), grid);
//                System.out.println(value);
                if (value > Input) {
                    System.out.println(value);
                    return;
                }
                grid.put(p, value);
            }
        }
    }

    @Override
    public int getDay() {
        return 3;
    }

    private static Set<Pair> getNeighbours(Pair p) {
        HashSet<Pair> res = new HashSet<>();
        res.add(new Pair(p.x() - 1, p.y() - 1));
        res.add(new Pair(p.x(), p.y() - 1));
        res.add(new Pair(p.x() + 1, p.y() - 1));
        res.add(new Pair(p.x() - 1, p.y()));
        res.add(new Pair(p.x()+1, p.y()));
        res.add(new Pair(p.x() - 1, p.y() + 1));
        res.add(new Pair(p.x(), p.y() + 1));
        res.add(new Pair(p.x() + 1, p.y() + 1));

        return res;
    }

    private static int neighboursSum(Set<Pair> s, Map<Pair, Integer> m) {
        return s.stream().mapToInt(p -> m.getOrDefault(p, 0)).sum();
    }
}

record Level(int ring, Integer start, int end) {
    Level(int ring, int start) {
        this(ring, start, start + ring * 4 - 5);
    }
}

record Pair(int x, int y) {
}

enum Direction {
    UP(0, -1),
    LEFT(-1, 0),
    DOWN(0, 1),
    RIGHT(1, 0);

    int vx, vy;

    Direction(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    Pair update(Pair pair) {
        return new Pair(pair.x() + vx, pair.y() + vy);
    }
}
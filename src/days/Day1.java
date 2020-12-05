package days;

import misc.Pair;
import setup.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day1 extends Day {


    private List<String> instructions;

    @Override
    public void processInput() {
        instructions = Arrays.asList(input.trim().split(", "));
    }

    @Override
    public void part1() {
        Direction dir = Direction.NORTH;
        Pair p = new Pair(0, 0);

        for (String ins : instructions) {
            if (ins.charAt(0) == 'R') dir = dir.right();
            else dir = dir.left();
            int times = Integer.parseInt(ins.substring(1));

            for (int i = 0; i < times; i++) {
                p = dir.walk(p);
            }
        }

        System.out.println(Math.abs(p.x()) + Math.abs(p.y()));
    }

    @Override
    public void part2() {
        Direction dir = Direction.NORTH;
        Pair p = new Pair(0, 0);

        HashSet<Pair> visited = new HashSet<>();
        visited.add(p);

        o: for (String ins : instructions) {
            if (ins.charAt(0) == 'R') dir = dir.right();
            else dir = dir.left();
            int times = Integer.parseInt(ins.substring(1));

            for (int i = 0; i < times; i++) {
                p = dir.walk(p);
                if (visited.contains(p)) {
                    break o;
                }
                visited.add(p);
            }
        }

        System.out.println(Math.abs(p.x()) + Math.abs(p.y()));
    }

    @Override
    public int getDay() {
        return 1;
    }
}

enum Direction {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);
    
    int vx, vy;

    Direction(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public Direction left() {
        List<Direction> directions = Arrays.asList(Direction.values());
        int index = directions.indexOf(this);
        return directions.get((4 + index - 1) % 4);
    }

    public Direction right() {
        List<Direction> directions = Arrays.asList(Direction.values());
        int index = directions.indexOf(this);
        return directions.get((index + 1) % 4);
    }
    
    Pair walk(Pair p) {
        return new Pair(p.x() + vx, p.y() + vy);
    }
}


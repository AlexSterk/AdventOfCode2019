package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day17 extends Day {
    private Map<Position, Cube> grid;
    
    @Override
    public void processInput() {
        grid = new HashMap<>();

        int i = 12;
        for (int x = 0; x < i; x++) {
            for (int y = 0; y < i; y++) {
                for (int z = 0; z < i*2-1; z++) {
                    Position position = new Position(x, y, z-i+1);
                    grid.put(position, new Cube(position));
                }
            }
        }
        
        
        String[] split = input.trim().split("\r?\n");
        for (int y = 0; y < split.length; y++) {
            char[] chars = split[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                Position key = new Position(i/2 + - chars.length / 2 + x, i/2 - split.length / 2 + y,0);
                grid.put(key, new Cube(key, chars[x] == '#'));
            }
        }
    }

    @Override
    public void part1() {
        printGrid();
        for (int i = 0; i < 6; i++) {
            Collection<Cube> values = new HashSet<>(grid.values());
            values.forEach(c -> c.doCycle(grid));
            values.forEach(Cube::executeCycle);
            printGrid();
        }

        System.out.println(grid.values().stream().filter(Cube::isActive).count());
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public boolean isTest() {
        return false;
    }
    
    void printGrid() {
        List<Integer> zs = grid.keySet().stream().map(Position::z).distinct().sorted().collect(Collectors.toList());
        List<Integer> xs = grid.keySet().stream().map(Position::x).distinct().sorted().collect(Collectors.toList());
        List<Integer> ys = grid.keySet().stream().map(Position::y).distinct().sorted().collect(Collectors.toList());
        
        for (Integer z : zs) {
            System.out.printf("Z: %d%n", z);

            for (Integer y : ys) {
                for (Integer x : xs) {
                    Cube cube = grid.get(new Position(x, y, z));
                    if (cube == null) System.out.print(' ');
                    else System.out.print(cube.isActive() ? '#' : '.');
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("-----------");
    }
}

record Position(int x, int y, int z) {

    static final Set<Position> NEIGHBOUR_DIRECTIONS = new HashSet<>();
    
    static {
        int[] arr = new int[]{-1,0,1};

        for (int x : arr) {
            for (int y : arr) {
                for (int z : arr) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    NEIGHBOUR_DIRECTIONS.add(new Position(x,y,z));
                }
            }
        }
    }

    Position add(Position toADd) {
        return new Position(x + toADd.x(), y + toADd.y(), z + toADd.z());
    }
}

class Cube {
    final Position position;
    private boolean currentStatus, nextCycleStatus;

    Cube(Position position) {
        this.position = position;
        currentStatus = false;
    }

    Cube(Position position, boolean active) {
        this.position = position;
        currentStatus = active;
    }

    void doCycle(Map<Position, Cube> grid) {
        long activeNeighbours = getNeighbours(grid).stream().filter(Cube::isActive).count();
        if (currentStatus) {
            nextCycleStatus = activeNeighbours == 2 || activeNeighbours == 3;
        } else {
            nextCycleStatus = activeNeighbours == 3;
        }
    }
    
    void executeCycle() {
        currentStatus = nextCycleStatus;
    }
    
    Set<Cube> getNeighbours(Map<Position, Cube> grid) {
        return Position.NEIGHBOUR_DIRECTIONS.stream().map(position::add).map(p -> grid.computeIfAbsent(p, Cube::new)).collect(Collectors.toSet());
    }
    
    boolean isActive() {
        return currentStatus;
    }

    @Override
    public String toString() {
        return Boolean.toString(currentStatus);
    }
}

package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day17 extends Day {
    private Map<Position, Cube> grid;
    
    @Override
    public void processInput() {
        // Delegated to parts bc it's different for each part
    }

    @Override
    public void part1() {
        grid = new HashMap<>();

        int s = 12;
        for (int x = 0; x < s; x++) {
            for (int y = 0; y < s; y++) {
                for (int z = 0; z < s*2-1; z++) {
                    Position position = new Position(x, y, z-s+1);
                    grid.put(position, new Cube(position));
                }
            }
        }


        String[] split = input.trim().split("\r?\n");
        for (int y = 0; y < split.length; y++) {
            char[] chars = split[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                Position key = new Position(s/2 + - chars.length / 2 + x, s/2 - split.length / 2 + y,0);
                grid.put(key, new Cube(key, chars[x] == '#'));
            }
        }
        
        for (int i = 0; i < 6; i++) {
            Collection<Cube> values = new HashSet<>(grid.values());
            values.forEach(c -> c.doCycle(grid));
            values.forEach(Cube::executeCycle);

        }
        System.out.println(grid.values().stream().filter(Cube::isActive).count());
    }

    @Override
    public void part2() {
        grid = new HashMap<>();

        int s = 12;
        for (int x = 0; x < s; x++) {
            for (int y = 0; y < s; y++) {
                for (int z = 0; z < s*2-1; z++) {
                    for (int w = 0; w < s*2-1; w++) {
                        Position position = new Position4D(x, y, z-s+1, w-s+1);
                        grid.put(position, new Cube(position));
                    }
                }
            }
        }
        
        String[] split = input.trim().split("\r?\n");
        for (int y = 0; y < split.length; y++) {
            char[] chars = split[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                Position key = new Position4D(s/2 + - chars.length / 2 + x, s/2 - split.length / 2 + y,0, 0);
                grid.put(key, new Cube(key, chars[x] == '#'));
            }
        }

        for (int i = 0; i < 6; i++) {
            Collection<Cube> values = new HashSet<>(grid.values());
            values.forEach(c -> c.doCycle(grid));
            values.forEach(Cube::executeCycle);

        }
        System.out.println(grid.values().stream().filter(Cube::isActive).count());
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

class Position {

    private static final Set<Position> NEIGHBOUR_DIRECTIONS = new HashSet<>();
    private final int x;
    private final int y;
    private final int z;

    static {
        int[] arr = new int[]{-1, 0, 1};

        for (int x : arr) {
            for (int y : arr) {
                for (int z : arr) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    NEIGHBOUR_DIRECTIONS.add(new Position(x, y, z));
                }
            }
        }
    }

    Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Position add(Position toAdd) {
        return new Position(x + toAdd.x(), y + toAdd.y(), z + toAdd.z());
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Position) obj;
        return this.x == that.x &&
                this.y == that.y &&
                this.z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    Set<Position> getNeighbours() {
        return NEIGHBOUR_DIRECTIONS.stream().map(this::add).collect(Collectors.toSet());
    }
}

class Position4D extends Position {
    private static final Set<Position4D> NEIGHBOUR_DIRECTIONS = new HashSet<>();

    static {
        int[] arr = new int[]{-1, 0, 1};

        for (int x : arr) {
            for (int y : arr) {
                for (int z : arr) {
                    for (int w : arr) {
                        if (x == 0 && y == 0 && z == 0 && w == 0) continue;
                        NEIGHBOUR_DIRECTIONS.add(new Position4D(x, y, z, w));   
                    };
                }
            }
        }
    }
    
    private final int w;

    Position4D(int x, int y, int z, int w) {
        super(x, y, z);
        this.w = w;
    }

    public int w() {
        return w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Position4D that = (Position4D) o;
        return w == that.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), w);
    }
    
    Position4D add(Position4D toAdd) {
        return new Position4D(x() + toAdd.x(), y() + toAdd.y(), z() + toAdd.z(), w() + toAdd.w());
    }

    @Override
    Set<Position> getNeighbours() {
        return NEIGHBOUR_DIRECTIONS.stream().map(this::add).collect(Collectors.toSet());
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
        return position.getNeighbours().stream().map(p -> grid.computeIfAbsent(p, Cube::new)).collect(Collectors.toSet());
    }
    
    boolean isActive() {
        return currentStatus;
    }

    @Override
    public String toString() {
        return Boolean.toString(currentStatus);
    }
}

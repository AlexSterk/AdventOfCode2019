package misc;

import java.util.Arrays;

public class Direction extends Pair {
    private Direction(Integer vx, Integer vy, char id) {
        super(vx, vy);
        this.id = id;
    }
    
    private final char id;
    
    public static final Direction NORTH = new Direction(0, -1, 'N');
    public static final Direction EAST = new Direction(1, 0, 'E');
    public static final Direction SOUTH = new Direction(0, 1, 'S');
    public static final Direction WEST = new Direction(-1, 0, 'W');
    
    public static final Direction[] values = new Direction[] {NORTH, EAST, SOUTH, WEST};
    
    public static Direction getById(char c) {
        return Arrays.stream(values).filter(d -> d.id == c).findFirst().get();
    }
    
    public static Direction getByValues(KVPair vs) {
        return Arrays.stream(values).filter(d -> d.key() == vs.key() && d.value() == vs.value()).findFirst().get();
    }
    
    public Direction getClockwise() {
        return getByValues(this.rotateClockWise());
    }

    public Direction getCounterClockwise() {
        return getByValues(this.rotateCounterClockWise());
    }
}

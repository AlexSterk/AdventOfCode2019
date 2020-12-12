package misc;

import java.util.Arrays;

public class Direction extends Pair {
    private Direction(Integer vx, Integer vy, char id) {
        super(vx, vy);
        this.id = id;
    }
    
    private Direction right;
    private Direction left;
    private final char id;
    
    public Direction getRight() {
        return right;
    }

    public Direction getLeft() {
        return left;
    }
    
    public static final Direction NORTH = new Direction(0, -1, 'N');
    public static final Direction EAST = new Direction(1, 0, 'E');
    public static final Direction SOUTH = new Direction(0, 1, 'S');
    public static final Direction WEST = new Direction(-1, 0, 'W');
    
    public static final Direction[] values = new Direction[] {NORTH, EAST, SOUTH, WEST};
    
    public static Direction getById(char c) {
        return Arrays.stream(values).filter(d -> d.id == c).findFirst().get();
    }

    static {
        NORTH.left = WEST;
        NORTH.right = EAST;

        EAST.left = NORTH;
        EAST.right = SOUTH;

        SOUTH.left = EAST;
        SOUTH.right = WEST;

        WEST.left = SOUTH;
        WEST.right = NORTH;
    }
}

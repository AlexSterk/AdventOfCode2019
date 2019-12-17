package misc;

public enum Direction {
    NORTH(1, 0, -1),
    EAST(4, 1, 0),
    WEST(3, -1, 0),
    SOUTH(2, 0, 1);

    public final int command;
    public final int vx;
    public final int vy;

    Direction(int command, int vx, int vy) {
        this.command = command;
        this.vx = vx;
        this.vy = vy;
    }

    public Direction opposite() {
        if (this == NORTH) return SOUTH;
        if (this == SOUTH) return NORTH;
        if (this == EAST) return WEST;
        if (this == WEST) return EAST;
        return null;
    }

    public Direction next() {
        if (this == NORTH) return EAST;
        if (this == EAST) return SOUTH;
        if (this == SOUTH) return WEST;
        if (this == WEST) return NORTH;
        return null;
    }

    public Direction prev() {
        return next().opposite();
    }
}

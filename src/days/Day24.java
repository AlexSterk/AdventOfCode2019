package days;

import setup.Day;

import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

enum Direction {
    EAST("e", 1, -1, 0),
    WEST("w", -1, 1, 0),
    NORTHEAST("ne", 1, 0, -1),
    NORTHWEST("nw", 0, 1, -1),
    SOUTHEAST("se", 0, -1, 1),
    SOUTHWEST("sw", -1, 0, 1);

    private static final Pattern PARSE = Pattern.compile("se|ne|sw|nw|[ew]");
    public final int vx, vy, vz;
    private final String id;

    Direction(String id, int vx, int vy, int vz) {
        this.id = id;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    static Direction stringToDirection(String s) {
        return Arrays.stream(Direction.values()).filter(d -> d.id.equals(s)).findAny().get();
    }

    static List<Direction> parseLine(String s) {
        return PARSE.matcher(s).results()
                .map(MatchResult::group)
                .map(Direction::stringToDirection).collect(Collectors.toList());
    }

    Direction opposite() {
        return switch (this) {
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTHEAST -> SOUTHWEST;
            case NORTHWEST -> SOUTHEAST;
            case SOUTHEAST -> NORTHWEST;
            case SOUTHWEST -> NORTHEAST;
        };
    }
}

public class Day24 extends Day {
    private List<List<Direction>> instructions;
    private HexGrid grid;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\r?\n")).map(Direction::parseLine).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        grid = new HexGrid();
        HexGrid.Position position = new HexGrid.Position(0, 0, 0);
        HexTile reference = new HexTile(grid, position);
        grid.put(position, reference);

        for (List<Direction> route : instructions) {
            HexTile current = reference;

            current = current.walk(route);

            current.flip();
        }

        System.out.println(getBlackTiles().size());
    }

    @Override
    public void part2() {
        Set<HexTile> blackTiles = new HashSet<>(getBlackTiles());
        for (int i = 0; i < 100; i++) {
            Map<HexTile, Integer> neighbourCounts = new HashMap<>();
            for (HexTile blackTile : blackTiles) {
                for (Direction value : Direction.values()) {
                    neighbourCounts.merge(blackTile.getNeighbour(value), 1, Integer::sum);
                }
            }

            Set<HexTile> newTiles = new HashSet<>();

            for (Map.Entry<HexTile, Integer> entry : neighbourCounts.entrySet()) {
                HexTile t = entry.getKey();
                Integer c = entry.getValue();
                if (blackTiles.contains(t)) {
                    if (c > 0 && c <= 2) newTiles.add(t);
                } else if (c == 2) newTiles.add(t);
            }

            blackTiles = newTiles;
        }
        System.out.println(blackTiles.size());
    }

    List<HexTile> getBlackTiles() {
        return grid.values().stream().filter(HexTile::isBlack).collect(Collectors.toList());
    }

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

class HexTile {
    private final HexGrid grid;
    private final HexGrid.Position position;
    private boolean black = false;

    HexTile(HexGrid grid, HexGrid.Position position) {
        this.grid = grid;
        this.position = position;
    }

    HexTile getNeighbour(Direction d) {
        return grid.computeIfAbsent(position.applyDirection(d), pos -> new HexTile(grid, pos));
    }

    boolean isBlack() {
        return black;
    }

    void flip() {
        black = !black;
    }

    HexTile walk(List<Direction> dirs) {
        HexTile current = this;

        for (Direction dir : dirs) {
            current = current.getNeighbour(dir);
        }

        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexTile hexTile = (HexTile) o;
        return position.equals(hexTile.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}

record HexGrid(Map<Position, HexTile> grid) {

    HexGrid() {
        this(new HashMap<>());
    }

    public HexTile put(Position key, HexTile value) {
        return grid.put(key, value);
    }

    public HexTile computeIfAbsent(Position key, Function<? super Position, ? extends HexTile> mappingFunction) {
        return grid.computeIfAbsent(key, mappingFunction);
    }

    static record Position(int x, int y, int z) {
        Position applyDirection(Direction d) {
            return new Position(x + d.vx, y + d.vy, z + d.vz);
        }
    }

    public Collection<HexTile> values() {
        return grid.values();
    }

    public Set<Position> keySet() {
        return grid.keySet();
    }
}

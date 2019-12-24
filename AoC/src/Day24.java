import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("AoC/resources/day24.txt"));
        List<List<Tile>> grid = lines.stream().map(s -> s.chars().mapToObj(c -> c == '#' ? Tile.BUG : Tile.EMPTY).collect(Collectors.toList())).collect(Collectors.toList());
        int height = grid.size(), width = grid.get(0).size();

        partOne(grid, height, width);
        partTwo(grid, height, width);
    }

    private static void partTwo(List<List<Tile>> grid, int height, int width) {
        Map<Integer, List<List<Tile>>> levels = new TreeMap<>();
        List<Tile> row = Collections.nCopies(width, Tile.EMPTY);
        List<List<Tile>> empty = Collections.nCopies(height, row);
        int minutes = 200;
        for (int level = -minutes; level <= minutes; level++) {
            levels.put(level, empty);
        }
        levels.put(0, grid);

        for (int i = 0; i < minutes; i++) {
            Map<Integer, List<List<Tile>>> nLevels = new TreeMap<>();
            for (int level = -minutes; level <= minutes; level++) {
                List<List<Tile>> nGrid = new ArrayList<>();
                for (int y = 0; y < height; y++) {
                    List<Tile> nRow = new ArrayList<>();
                    for (int x = 0; x < width; x++) {
                        if (x == 2 && y == 2) {
                            nRow.add(Tile.EMPTY);
                            continue;
                        }
                        List<List<Tile>> curLevel = levels.get(level);
                        Tile current = curLevel.get(y).get(x);
                        int adjacentBugs = 0;
                        if (levels.containsKey(level - 1)) {
                            adjacentBugs = previousLevelAdjacent(levels, level, y, x, adjacentBugs);
                        }
                        if (levels.containsKey(level + 1)) {
                            adjacentBugs = nextLevelAdjacent(levels, level, y, x, adjacentBugs);
                        }
                        adjacentBugs = getAdjacentBugs(curLevel, height, width, y, x, adjacentBugs);
                        setNextTile(nRow, current, adjacentBugs);
                    }
                    nGrid.add(nRow);
                }
                nLevels.put(level, nGrid);
            }
            levels = nLevels;
        }
        System.out.println(levels.values().stream().flatMap(Collection::stream).flatMap(Collection::stream).filter(t -> t == Tile.BUG).count());
    }

    private static int nextLevelAdjacent(Map<Integer, List<List<Tile>>> levels, int level, int y, int x, int adjacentBugs) {
        List<List<Tile>> nextLevel = levels.get(level + 1);
        if (y == 2 && x == 1) {
            adjacentBugs += IntStream.range(0, 5).mapToObj(a -> nextLevel.get(a).get(0)).filter(t -> t == Tile.BUG).count();
        }
        if (y == 2 && x == 3) {
            adjacentBugs += IntStream.range(0, 5).mapToObj(a -> nextLevel.get(a).get(4)).filter(t -> t == Tile.BUG).count();
        }
        if (y == 1 && x == 2) {
            adjacentBugs += IntStream.range(0, 5).mapToObj(a -> nextLevel.get(0).get(a)).filter(t -> t == Tile.BUG).count();
        }
        if (y == 3 && x == 2) {
            adjacentBugs += IntStream.range(0, 5).mapToObj(a -> nextLevel.get(4).get(a)).filter(t -> t == Tile.BUG).count();
        }
        return adjacentBugs;
    }

    private static int previousLevelAdjacent(Map<Integer, List<List<Tile>>> levels, int level, int y, int x, int adjacentBugs) {
        List<List<Tile>> prevLevel = levels.get(level - 1);
        if (x == 0 && prevLevel.get(2).get(1) == Tile.BUG) adjacentBugs++;
        if (x == 4 && prevLevel.get(2).get(3) == Tile.BUG) adjacentBugs++;
        if (y == 0 && prevLevel.get(1).get(2) == Tile.BUG) adjacentBugs++;
        if (y == 4 && prevLevel.get(3).get(2) == Tile.BUG) adjacentBugs++;
        return adjacentBugs;
    }

    private static void partOne(List<List<Tile>> grid, int height, int width) {
        Set<List<List<Tile>>> grids = new HashSet<>();

        while (!grids.contains(grid)) {
            grids.add(grid);
            List<List<Tile>> nGrid = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                List<Tile> nRow = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    Tile current = grid.get(y).get(x);
                    int adjacentBugs = getAdjacentBugs(grid, height, width, y, x, 0);
                    setNextTile(nRow, current, adjacentBugs);
                }
                nGrid.add(nRow);
            }
            grid = nGrid;
        }
        List<Tile> allTiles = grid.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(IntStream.range(0, allTiles.size()).filter(i -> allTiles.get(i) == Tile.BUG).map(i -> (int) Math.pow(2, i)).sum());
    }

    private static void setNextTile(List<Tile> nRow, Tile current, int adjacentBugs) {
        if (current == Tile.BUG && adjacentBugs == 1) nRow.add(Tile.BUG);
        else if (current == Tile.EMPTY && (adjacentBugs == 1 || adjacentBugs == 2)) nRow.add(Tile.BUG);
        else nRow.add(Tile.EMPTY);
    }

    private static int getAdjacentBugs(List<List<Tile>> grid, int height, int width, int y, int x, int adjacentBugs) {
        if (y > 0 && grid.get(y - 1).get(x) == Tile.BUG) adjacentBugs++;
        if (y < height - 1 && grid.get(y + 1).get(x) == Tile.BUG) adjacentBugs++;
        if (x > 0 && grid.get(y).get(x - 1) == Tile.BUG) adjacentBugs++;
        if (x < width - 1 && grid.get(y).get(x + 1) == Tile.BUG) adjacentBugs++;
        return adjacentBugs;
    }

    public static void printGrid(List<List<Tile>> grid) {
        grid.forEach(l -> {
            System.out.println(l.stream().map(t -> t == Tile.BUG ? "#" : ".").collect(Collectors.joining()));
        });
        System.out.println();
    }
}

enum Tile {
    BUG,
    EMPTY
}
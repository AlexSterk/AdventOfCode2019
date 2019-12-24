import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("AoC/resources/day24.txt"));
        List<List<Tile>> state = lines.stream().map(s -> s.chars().mapToObj(c -> c == '#' ? Tile.BUG : Tile.EMPTY).collect(Collectors.toList())).collect(Collectors.toList());
        state.get(2).set(2, Tile.RECURSION);
        int height = state.size(), width = state.get(0).size();

//        partOne(state, height, width);

        Map<Integer, List<List<Tile>>> levels = new TreeMap<>();
        List<Tile> row = Collections.nCopies(width, Tile.EMPTY);
        List<List<Tile>> empty = Collections.nCopies(height, row);
        int minutes = 200;
        for (int level = -minutes; level <= minutes; level++) {
            levels.put(level, empty);
        }
        levels.put(0, state);

        for (int i = 0; i < minutes; i++) {
            Map<Integer, List<List<Tile>>> nLevels = new TreeMap<>();
            for (int level = -minutes; level <= minutes; level++) {
                List<List<Tile>> nState = new ArrayList<>();
                for (int y = 0; y < height; y++) {
                    List<Tile> nRow = new ArrayList<>();
                    for (int x = 0; x < width; x++) {
                        if (x == 2 && y == 2) {
                            nRow.add(Tile.RECURSION);
                            continue;
                        }
                        List<List<Tile>> curLevel = levels.get(level);
                        Tile current = curLevel.get(y).get(x);
                        int adjacentBugs = 0;
                        if (levels.containsKey(level - 1)) {
                            List<List<Tile>> prevLevel = levels.get(level - 1);
                            if (x == 0 && prevLevel.get(2).get(1) == Tile.BUG) adjacentBugs++;
                            if (x == 4 && prevLevel.get(2).get(3) == Tile.BUG) adjacentBugs++;
                            if (y == 0 && prevLevel.get(1).get(2) == Tile.BUG) adjacentBugs++;
                            if (y == 4 && prevLevel.get(3).get(2) == Tile.BUG) adjacentBugs++;
                        }
                        if (levels.containsKey(level + 1)) {
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
                        }
                        if (y > 0 && curLevel.get(y - 1).get(x) == Tile.BUG) adjacentBugs++;
                        if (y < height - 1 && curLevel.get(y + 1).get(x) == Tile.BUG) adjacentBugs++;
                        if (x > 0 && curLevel.get(y).get(x - 1) == Tile.BUG) adjacentBugs++;
                        if (x < width - 1 && curLevel.get(y).get(x + 1) == Tile.BUG) adjacentBugs++;
                        if (current == Tile.BUG && adjacentBugs == 1) nRow.add(Tile.BUG);
                        else if (current == Tile.EMPTY && (adjacentBugs == 1 || adjacentBugs == 2)) nRow.add(Tile.BUG);
                        else nRow.add(Tile.EMPTY);
                    }
                    nState.add(nRow);
                }
                nLevels.put(level, nState);
            }
            levels = nLevels;
        }
        System.out.println(levels.values().stream().flatMap(Collection::stream).flatMap(Collection::stream).filter(t -> t == Tile.BUG).count());
    }

    private static void partOne(List<List<Tile>> state, int height, int width) {
        Set<List<List<Tile>>> states = new HashSet<>();

        while (!states.contains(state)) {
            states.add(state);
            List<List<Tile>> nState = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                List<Tile> nRow = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    Tile current = state.get(y).get(x);
                    int adjacentBugs = 0;
                    if (y > 0 && state.get(y - 1).get(x) == Tile.BUG) adjacentBugs++;
                    if (y < height - 1 && state.get(y + 1).get(x) == Tile.BUG) adjacentBugs++;
                    if (x > 0 && state.get(y).get(x - 1) == Tile.BUG) adjacentBugs++;
                    if (x < width - 1 && state.get(y).get(x + 1) == Tile.BUG) adjacentBugs++;
                    if (current == Tile.BUG && adjacentBugs == 1) nRow.add(Tile.BUG);
                    else if (current == Tile.EMPTY && (adjacentBugs == 1 || adjacentBugs == 2)) nRow.add(Tile.BUG);
                    else nRow.add(Tile.EMPTY);
                }
                nState.add(nRow);
            }
            state = nState;
        }
        List<Tile> allTiles = state.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(IntStream.range(0, allTiles.size()).filter(i -> allTiles.get(i) == Tile.BUG).map(i -> (int) Math.pow(2, i)).sum());
    }

    public static void printState(List<List<Tile>> state) {
        state.forEach(l -> {
            System.out.println(l.stream().map(t -> t == Tile.BUG ? "#" : t == Tile.RECURSION ? "?" : ".").collect(Collectors.joining()));
        });
        System.out.println();
    }
}

enum Tile {
    BUG,
    EMPTY,
    RECURSION;
}
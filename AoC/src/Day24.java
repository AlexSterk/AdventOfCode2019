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
        int height = state.size(), width = state.get(0).size();

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
                    else if (current == Tile.EMPTY &&  (adjacentBugs == 1 || adjacentBugs == 2)) nRow.add(Tile.BUG);
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
            System.out.println(l.stream().map(t -> t == Tile.BUG ? "#" : ".").collect(Collectors.joining()));
        });
        System.out.println();
    }
}

enum Tile {
    BUG,
    EMPTY;
}
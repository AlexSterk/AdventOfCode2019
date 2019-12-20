import misc.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day20 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("AoC/resources/day20.txt"));
        char[][] grid = new char[lines.size()][lines.get(0).length()];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = lines.get(y).charAt(x);
            }
        }

        System.out.println(new MazeGraph(grid).shortestPathDistance("AA", "ZZ"));
    }


}

class MazeGraph {
    private final Set<String> vertices;
    private final Map<Tuple2<String, String>, Integer> edges;

    public MazeGraph(char[][] grid) {
        Map<String, List<Tuple2<Integer, Integer>>> portals = getPortals(grid);

        vertices = new HashSet<>(portals.keySet());
        edges = new HashMap<>();
        
        portals.forEach((portal, nodes) -> {
            for (Tuple2<Integer, Integer> position : nodes) {
                Map<String, Integer> bfs = bfs(grid, position);
                bfs.remove(portal);
                bfs.forEach((p2, dist) -> edges.put(new Tuple2<>(portal, p2), dist));
            }
        });
    }
    
    public int shortestPathDistance(String from, String to) {
        Map<String, Integer> distances = new HashMap<>();
        vertices.forEach(v -> distances.put(v, Integer.MAX_VALUE));
        distances.put(from, 0);
        Set<String> toVisit = new HashSet<>(vertices);
        
        while (!toVisit.isEmpty()) {
            String cur = Collections.min(toVisit, Comparator.comparingInt(distances::get));
            if (cur.equals(to)) break;
            toVisit.remove(cur);
            
            int dist = distances.get(cur);
            
            getNeighbours(cur).forEach((n, d) -> distances.computeIfPresent(n, (n2, d2) -> Math.min(d + dist + 1, d2)));
        }
        
        return distances.get(to) - 1;
    }
    
    private Map<String, Integer> getNeighbours(String of) {
        return edges.keySet().stream().filter(t -> t.first.equals(of)).collect(Collectors.toMap(p -> p.second, edges::get));
    }

    private Map<String, Integer> bfs(char[][] grid, Tuple2<Integer, Integer> from) {
        Queue<Tuple2<Integer, Integer>> toVisit = new LinkedList<>();
        Map<Tuple2<Integer, Integer>, Integer> distances = new HashMap<>();
        Map<String, Integer> portals = new HashMap<>();
        toVisit.add(from);
        distances.put(from, 0);

        while (!toVisit.isEmpty()) {
            Tuple2<Integer, Integer> cur = toVisit.poll();
            for (Direction dir : Direction.values()) {
                Tuple2<Integer, Integer> nPos = new Tuple2<>(cur.first + dir.vx, cur.second + dir.vy);
                char c = grid[nPos.second][nPos.first];
                if (distances.containsKey(nPos)) continue;
                if (c == '.') {
                    toVisit.offer(nPos);
                    distances.put(nPos, distances.get(cur) + 1);
                }
                else if (Character.isLetter(c)) {
                    String label;
                    char c2 = grid[nPos.second + dir.vy][nPos.first + dir.vx];
                    if (dir == Direction.NORTH || dir == Direction.WEST) label = new String(new char[]{c2, c});
                    else label = new String(new char[]{c, c2});
                    portals.put(label, distances.get(cur));
                }
            }
        }
        return portals;
    }

    private Map<String, List<Tuple2<Integer, Integer>>> getPortals(char[][] grid) {
        Map<String, List<Tuple2<Integer, Integer>>> portals = new HashMap<>();

        for (int y = 1; y < grid.length; y++) {
            for (int x = 1; x < grid[0].length; x++) {
                char c = grid[y][x];
                if (Character.isLetter(c)) {
                    String label;
                    List<Character> o = List.of(grid[y - 1][x], grid[y][x - 1]);
                    for (Character c2 : o) {
                        if (Character.isLetter(c2)) {
                            label = new String(new char[]{c2, c});
                            for (Tuple2<Integer, Integer> p : List.of(
                                    new Tuple2<>(x, y + 1),
                                    new Tuple2<>(x, y - 2),
                                    new Tuple2<>(x - 2, y),
                                    new Tuple2<>(x + 1, y)
                            )) {
                                try {
                                    if (grid[p.second][p.first] == '.') {
                                        portals.putIfAbsent(label, new ArrayList<>());
                                        portals.get(label).add(p);
                                        break;
                                    }
                                } catch (ArrayIndexOutOfBoundsException ignored) {}
                            }
                            break;
                        }
                    }
                }
            }
        }
        assert portals.values().stream().filter(l -> l.size() == 2).count() == portals.keySet().size();
        return portals;
    }
}
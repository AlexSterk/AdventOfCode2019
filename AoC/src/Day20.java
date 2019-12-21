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

        System.out.println(new MazeGraph(grid, 0).shortestPathDistance("AA", "ZZ"));
        System.out.println(new MazeGraph(grid, 50).recursiveShortestPathDistance("AA", "ZZ"));
    }


}

class MazeGraph {
    private final Map<String, List<Tuple2<Integer, Integer>>> vertices;
    private final Map<Tuple2<Integer, Integer>, Map<Tuple2<Integer, Integer>, Integer>> edges;
    private final Map<Tuple3<Integer, Integer, Integer>, Map<Tuple3<Integer, Integer, Integer>, Integer>> zEdges;

    public MazeGraph(char[][] grid, int levels) {
        Map<String, List<Tuple2<Integer, Integer>>> portals = getPortals(grid);

        vertices = portals;
        edges = new HashMap<>();
        zEdges = new HashMap<>();
        
        portals.forEach((portal, nodes) -> {
            for (Tuple2<Integer, Integer> position : nodes) {
                Map<Tuple2<Integer, Integer>, Tuple2<Integer, Boolean>> bfs = bfs(grid, position);
                bfs.remove(position);
                bfs.forEach((p2, t) -> {
                    edges.putIfAbsent(position, new HashMap<>());
                    edges.get(position).put(p2, t.first);
                    for (int i = 0; i < levels; i++) {
                        Tuple3<Integer, Integer, Integer> key = new Tuple3<>(position.first, position.second, i);
                        zEdges.putIfAbsent(key, new HashMap<>());
                        zEdges.get(key).put(new Tuple3<>(p2.first, p2.second, i), t.first);
                    }
                });
            }
            if (nodes.size() == 2) {
                Tuple2<Integer, Integer> t1 = nodes.get(0);
                Tuple2<Integer, Integer> t2 = nodes.get(1);
                
                edges.putIfAbsent(t1, new HashMap<>());
                edges.putIfAbsent(t2, new HashMap<>());
                edges.get(t1).put(t2, 1);
                edges.get(t2).put(t1, 1);
                
                Tuple2<Integer, Integer> inner = isInner(grid, t1.first, t1.second) ? t1 : t2;
                Tuple2<Integer, Integer> outer = inner == t1 ? t2 : t1;

                for (int i = 0; i < levels - 1; i++) {
                    Tuple3<Integer, Integer, Integer> innerT = new Tuple3<>(inner.first, inner.second, i);
                    Tuple3<Integer, Integer, Integer> outerT = new Tuple3<>(outer.first, outer.second, i + 1);
                    zEdges.putIfAbsent(innerT, new HashMap<>());
                    zEdges.putIfAbsent(outerT, new HashMap<>());
                    zEdges.get(innerT).put(outerT, 1);
                    zEdges.get(outerT).put(innerT, 1);
                }
            }
        });
    }
    
    public int shortestPathDistance(String from, String to) {
        Map<Tuple2<Integer, Integer>, Integer> distances = new HashMap<>();
        vertices.values().forEach(l -> l.forEach(p -> distances.put(p, Integer.MAX_VALUE)));
        distances.put(vertices.get(from).get(0), 0);
        Set<Tuple2<Integer, Integer>> toVisit = vertices.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        
        while (!toVisit.isEmpty()) {
            Tuple2<Integer, Integer> cur = Collections.min(toVisit, Comparator.comparingInt(distances::get));
            if (cur.equals(vertices.get(to).get(0))) break;
            toVisit.remove(cur);
            
            int dist = distances.get(cur);
            
            edges.get(cur).forEach((n, d) -> distances.computeIfPresent(n, (n2, d2) -> Math.min(d + dist, d2)));
        }
        
        return distances.get(vertices.get(to).get(0));
    }

    public int recursiveShortestPathDistance(String from, String to) {
        Map<Tuple3<Integer, Integer, Integer>, Integer> distances = new HashMap<>();
        Tuple2<Integer, Integer> fromT = vertices.get(from).get(0);
        Tuple2<Integer, Integer> toT = vertices.get(to).get(0);
        Tuple3<Integer, Integer, Integer> nFromT = new Tuple3<>(fromT.first, fromT.second, 0);
        Tuple3<Integer, Integer, Integer> nToT = new Tuple3<>(toT.first, toT.second, 0);
        distances.put(nFromT, 0);
        Set<Tuple3<Integer, Integer, Integer>> toVisit = new HashSet<>(zEdges.keySet());

        while (!toVisit.isEmpty()) {
            Tuple3<Integer, Integer, Integer> cur = Collections.min(toVisit, Comparator.comparingInt(key -> distances.getOrDefault(key, Integer.MAX_VALUE)));
            if (cur.equals(nToT)) break;
            toVisit.remove(cur);

            int dist = distances.getOrDefault(cur, Integer.MAX_VALUE);

            zEdges.get(cur).forEach((n, d) -> {
                if (dist + d < distances.getOrDefault(n, Integer.MAX_VALUE)) {
                    distances.put(n, dist + d);
                }
            });
        }

        return distances.get(nToT);
    }

    private Map<Tuple2<Integer, Integer>, Tuple2<Integer, Boolean>> bfs(char[][] grid, Tuple2<Integer, Integer> from) {
        Queue<Tuple2<Integer, Integer>> toVisit = new LinkedList<>();
        Map<Tuple2<Integer, Integer>, Integer> distances = new HashMap<>();
        Map<Tuple2<Integer, Integer>, Tuple2<Integer, Boolean>> portals = new HashMap<>();
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
                    portals.put(new Tuple2<>(nPos.first - dir.vx, nPos.second - dir.vy), new Tuple2<>(distances.get(cur), isInner(grid,nPos.first - dir.vx, nPos.second - dir.vy)));
                }
            }
        }
        return portals;
    }

    private boolean isInner(char[][] grid, int x, int y) {
        int w1 = 2, w2 = grid[0].length - 3;
        int h1 = 2, h2 = grid.length - 3;
        
        return x != w1 && x != w2 && y != h1 && y != h2;
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


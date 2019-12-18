import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    public static class MazeRunner {
        public final Map<Tuple2<Set<Tuple2<Integer, Integer>>, String>, Integer> memory = new HashMap<>();

        public Map<Character, Tuple2<Integer, Tuple2<Integer, Integer>>> canReach(char[][] grid, Tuple2<Integer, Integer> from, String owned) {
            Queue<Tuple2<Integer, Integer>> toVisit = new LinkedList<>();
            Map<Character, Tuple2<Integer, Tuple2<Integer, Integer>>> keys = new TreeMap<>();
            Map<Tuple2<Integer, Integer>, Integer> distances = new HashMap<>();
            distances.put(from, 0);
            toVisit.add(from);

            while (!toVisit.isEmpty()) {
                Tuple2<Integer, Integer> current = toVisit.poll();
                for (Tuple2<Integer, Integer> neighbour : Set.of(new Tuple2<>(current.first + 1, current.second),
                        new Tuple2<>(current.first - 1, current.second), new Tuple2<>(current.first, current.second + 1), new Tuple2<>(current.first, current.second - 1))) {
                    if (neighbour.second >= grid.length || neighbour.first >= grid[0].length) continue;
                    char c = grid[neighbour.second][neighbour.first];
                    if (c == '#') continue;



                    if (distances.containsKey(neighbour)) continue;
                    distances.put(neighbour, distances.get(current) + 1);


                    boolean doesNotHaveKey = owned.indexOf(Character.toLowerCase(c)) < 0;
                    if (Character.isUpperCase(c) && doesNotHaveKey) {
                        continue;
                    } if (Character.isLowerCase(c) && doesNotHaveKey) {
                        keys.put(c, new Tuple2<>(distances.get(neighbour), neighbour));
                    } else toVisit.offer(neighbour);
                }
            }

            return keys;
        }

        public Map<Character, Tuple3<Integer, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>> canReach(char[][] grid, Set<Tuple2<Integer, Integer>> froms, String owned) {
            Map<Character, Tuple3<Integer, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>> keys = new TreeMap<>();
            for (Tuple2<Integer, Integer> from : froms) {
                Map<Character, Tuple2<Integer, Tuple2<Integer, Integer>>> out = canReach(grid, from, owned);
                out.forEach((k, p) -> keys.put(k, new Tuple3<>(p.first, p.second, from)));
            }
            return keys;
        }

        public int findWalk(char[][] grid, Set<Tuple2<Integer, Integer>> froms, String owned) {
            int toRet = 0;
            String sorted = sortString(owned);
            if (memory.containsKey(new Tuple2<>(froms, sorted))) return memory.get(new Tuple2<>(froms, sorted));

            Map<Character, Tuple3<Integer, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>> canCollect = canReach(grid, froms, owned);
            if (!canCollect.isEmpty()) {
                ArrayList<Integer> dists = new ArrayList<>();
                canCollect.forEach((k, t) -> {
                    Set<Tuple2<Integer, Integer>> nFroms = froms.stream().map(from -> from == t.third ? t.second : from).collect(Collectors.toSet());
                    String nOwned = owned + k;
                    int nDist = t.first + findWalk(grid, nFroms, nOwned);
                    dists.add(nDist);
                });
                toRet = Collections.min(dists);
            }
            memory.put(new Tuple2<>(froms, sorted), toRet);
            return toRet;
        }

        private String sortString(String s) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            return new String(chars);
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> rows = Files.readAllLines(Paths.get("AoC/resources/day18-2.txt"));
        MazeRunner mazeRunner = new MazeRunner();
        HashSet<Tuple2<Integer, Integer>> froms = new HashSet<>();

        char[][] grid = new char[rows.size()][rows.get(0).length()];
        for (int y = 0; y < rows.size(); y++) {
            String row = rows.get(y);
            for (int x = 0; x < row.length(); x++) {
                grid[y][x] = row.charAt(x);
                if (row.charAt(x) == '@') froms.add(new Tuple2<>(y, x));
            }
        }

        System.out.println(mazeRunner.findWalk(grid, froms, ""));
    }
}

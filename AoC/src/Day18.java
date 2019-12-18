import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import misc.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    public static class Maze {
        public final Map<Node, Tile> map;
        public final Map<Node, Node> nodeMap;
        public final Node ENTRANCE;
        public final Map<Pair<Node>, Integer> distanceMap = new HashMap<>();

        private Map<Node, Node> predecessors;

        public Maze(List<String> rows) {
            map = new HashMap<>();
            Node entrance = null;
            for (int y = 0; y < rows.size(); y++) {
                String s = rows.get(y);
                for (int x = 0; x < s.length(); x++) {
                    char c = s.charAt(x);
                    Node n = new Node(x, y);
                    if (c == '@') {
                        map.put(n, Tile.EMPTY);
                        entrance = n;
                    }
                    else if (c == '.') map.put(n, Tile.EMPTY);
                    else if (Character.isLowerCase(c)) map.put(n, new Key(c, n));
                    else if (Character.isUpperCase(c)) map.put(n, new Door(c, n));
                }
            }
            ENTRANCE = entrance;
            nodeMap = new HashMap<>();
            map.keySet().forEach(n -> nodeMap.put(n, n));
            map.keySet().forEach(n -> n.initNeighbours(this));
        }

        private void initShortestPaths() {
            Map<Node, Integer> steps = new HashMap<>();
            Map<Node, Node> predecessor = new HashMap<>();
            Comparator<Node> comparator = Comparator.comparing(n -> steps.getOrDefault(n, Integer.MAX_VALUE));
            steps.put(ENTRANCE, 0);
            Set<Node> toVisit = new HashSet<>(map.keySet());

            while (!toVisit.isEmpty()) {
                Node current = Collections.min(toVisit, comparator);
                Integer dist = steps.getOrDefault(current, Integer.MAX_VALUE);
                int newDist = dist + 1;
                for (Node neighbour : current.neighbours) {
                    if (newDist < steps.getOrDefault(neighbour, Integer.MAX_VALUE)) {
                        steps.put(neighbour, newDist);
                        predecessor.put(neighbour, current);
                    }
                }
                toVisit.remove(current);
            }
            this.predecessors = predecessor;
        }

        public List<Node> getPath(Node from, Node to) {
            if (predecessors == null) initShortestPaths();

            LinkedList<Node> path = new LinkedList<>();

            Node current = to;
            while (current != from) {
                path.addFirst(current);
                current = predecessors.get(current);
                if (current == null) return getPath(to, from);
            }
            return path;
        }

        public Integer getDistance(Key from, Key to) {
            return getDistance(from.node, to);
        }

        public Integer getDistance(Node from, Key to) {
            Node n = to.node;
            return distanceMap.computeIfAbsent(new Pair<>(from, n), (p -> getPath(p.one, p.two).size()));
        }

        public List<Key> keysRequired(Key key) {
            List<Node> path = getPath(ENTRANCE, key.node);
            return path.stream().map(map::get).filter(t -> t instanceof Door).map(t -> (Door) t).map(d -> new Key(Character.toLowerCase(d.letter), d.node)).collect(Collectors.toList());
        }

        public List<Key> isRequiredFor(Key key) {
            return getKeys().stream().map(this::keysRequired).filter(l -> l.contains(key)).flatMap(Collection::stream).collect(Collectors.toList());
        }

        public Set<Key> getKeys() {
            return map.values().stream().filter(t -> t instanceof Key).map(t -> (Key) t).collect(Collectors.toSet());
        }

        public Set<Door> getDoors() {
            return map.values().stream().filter(t -> t instanceof Door).map(t -> (Door) t).collect(Collectors.toSet());
        }
    }

    @Data
    public static class Node {
        public final int x,y;
        @EqualsAndHashCode.Exclude
        private Set<Node> neighbours;

        public void initNeighbours(Maze m) {
            if (neighbours != null) return;
            neighbours = new HashSet<>();
            for (Direction dir : Direction.values()) {
                Node n = new Node(x + dir.vx, y + dir.vy);
                if (m.map.containsKey(n)) neighbours.add(m.nodeMap.get(n));
            }
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }

        public List<Node> findPath(Node to, Maze m) {
            Map<Node, Integer> steps = new HashMap<>();
            Map<Node, Node> predecessor = new HashMap<>();
            Comparator<Node> comparator = Comparator.comparing(n -> steps.getOrDefault(n, Integer.MAX_VALUE));
            steps.put(this, 0);
            Set<Node> toVisit = new HashSet<>(m.map.keySet());

            while (!toVisit.isEmpty()) {
                Node current = Collections.min(toVisit, comparator);
                if (current == to) break;
                Integer dist = steps.getOrDefault(current, Integer.MAX_VALUE);
                int newDist = dist + 1;
                for (Node neighbour : current.neighbours) {
                    if (newDist < steps.getOrDefault(neighbour, Integer.MAX_VALUE)) {
                        steps.put(neighbour, newDist);
                        predecessor.put(neighbour, current);
                    }
                }
                toVisit.remove(current);
            }

            LinkedList<Node> path = new LinkedList<>();

            Node current = to;
            while (current != this) {
                path.addFirst(current);
                current = predecessor.get(current);
            }

            return path;
        }
    }

    @EqualsAndHashCode
    public static class Tile {
        public static final Tile EMPTY = new Tile();

        @Override
        public String toString() {
            if (this == EMPTY) {
                return "Empty";
            }
            return super.toString();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Key extends Tile {
        public final char letter;
        @EqualsAndHashCode.Exclude
        public final Node node;

        @Override
        public String toString() {
            return "Key(" + letter + ")";
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Door extends Tile {
        public final char letter;
        @EqualsAndHashCode.Exclude
        public final Node node;

        @Override
        public String toString() {
            return "Door(" + letter + ")";
        }
    }

    @RequiredArgsConstructor
    public static class MazeRunner {
        public final Maze maze;
        @NonNull
        private Node position;
        public final Set<Key> ownedKeys = new HashSet<>();

        public Set<Door> getUnlockedDoors() {
            HashSet<Tile> tiles = new HashSet<>(maze.map.values());
            tiles.removeIf(t -> !(t instanceof Door));
            tiles.removeIf(t -> !ownedKeys.contains(Character.toLowerCase(((Door) t).letter)));
            HashSet<Door> doors = new HashSet<>();
            tiles.forEach(t -> doors.add((Door) t));
            return doors;
        }

        public Set<Door> getLockedDoors() {
            HashSet<Tile> tiles = new HashSet<>(maze.map.values());
            tiles.removeIf(t -> !(t instanceof Door));
            tiles.removeIf(t -> ownedKeys.contains(Character.toLowerCase(((Door) t).letter)));
            HashSet<Door> doors = new HashSet<>();
            tiles.forEach(t -> doors.add((Door) t));
            return doors;
        }

        public Map<Key, Node> getAvailableKeys() {
            Map<Key, Node> keys = new HashMap<>();

            Queue<Node> toVisit = new LinkedList<>();
            Set<Node> visited = new HashSet<>();
            toVisit.add(position);
            while (!toVisit.isEmpty()) {
                Node n = toVisit.poll();
                visited.add(n);
                for (Node neighbour : n.neighbours) {
                    if (visited.contains(neighbour)) continue;
                    Tile tile = maze.map.get(neighbour);
                    if (tile == Tile.EMPTY) toVisit.add(neighbour);
                    else if (tile instanceof Key) {
                        toVisit.add(neighbour);
                        keys.put((Key) tile, neighbour);
                    }
                }
            }

            return keys;
        }
    }

    public static void main(String[] args) throws IOException {
        Maze maze = new Maze(Files.readAllLines(Paths.get("AoC/resources/day18.txt")));
        System.out.println(maze.getKeys().stream().filter(key -> maze.isRequiredFor(key).isEmpty()).collect(Collectors.toSet()));
    }
}

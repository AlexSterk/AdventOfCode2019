import misc.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day18 {
    public static class Maze {
        public final Map<Node, Tile> map;
        public final Node ENTRANCE;

        public Maze(List<String> rows) {
            map = new HashMap<>();
            Node entrance = null;
            for (int y = 0; y < rows.size(); y++) {
                String s = rows.get(y);
                for (int x = 0; x < s.length(); x++) {
                    char c = s.charAt(x);
                    Node n = new Node(x, y);
                    if (c == '#') map.put(n, Tile.WALL);
                    else if (c == '@') {
                        map.put(n, Tile.EMPTY);
                        entrance = n;
                    }
                    else if (c == '.') map.put(n, Tile.EMPTY);
                    else if (Character.isLowerCase(c)) map.put(n, new Key(c));
                    else if (Character.isUpperCase(c)) map.put(n, new Door(c));
                }
            }
            ENTRANCE = entrance;
            map.keySet().forEach(n -> n.initNeighbours(this));
        }
    }

    public static class Node {
        public final int x,y;
        private Set<Node> neighbours;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void initNeighbours(Maze m) {
            if (neighbours != null) return;
            neighbours = new HashSet<>();
            for (Direction dir : Direction.values()) {
                Node n = new Node(x + dir.vx, y + dir.vy);
                if (m.map.get(n) != Tile.WALL) neighbours.add(n);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x &&
                    y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    public static class Tile {
        public static final Tile EMPTY = new Tile();
        public static final Tile WALL = new Tile();

        @Override
        public String toString() {
            if (this == EMPTY) {
                return "Empty";
            } else if (this == WALL) return "Wall";
            return super.toString();
        }
    }

    public static class Key extends Tile {
        public final char letter;

        public Key(char letter) {
            this.letter = letter;
        }

        @Override
        public String toString() {
            return "Key(" + letter + ")";
        }
    }

    public static class Door extends Tile {
        public final char letter;

        public Door(char letter) {
            this.letter = letter;
        }

        @Override
        public String toString() {
            return "Door(" + letter + ")";
        }
    }

    public static class MazeRunner {

    }

    public static void main(String[] args) throws IOException {
        Maze maze = new Maze(Files.readAllLines(Paths.get("AoC/resources/day18.txt")));
    }
}

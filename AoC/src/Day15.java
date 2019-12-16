import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day15 {
    public static class Node {
        public static Node OXYGEN_SYSTEM;
        public static final Map<Node, Node> nodes = new HashMap<>();
        public static final List<List<Object>> moves = new ArrayList<>();

        public final int x, y;
        public final IntCodeInterpreter cpu;
        public final Set<Node> neighbours = new HashSet<>();

        public Node(int x, int y, IntCodeInterpreter cpu) {
            this.x = x;
            this.y = y;
            this.cpu = cpu;
        }

        void exploreNeighbours(Set<Node> visited, DirectionY from) {
            if (visited.contains(this)) {
                assert from == null || move(from) > 0;
                return;
            }
            visited.add(this);
            for (DirectionY dir : DirectionY.values()) {
                long out = move(dir);
                if (out == 0) {
                    continue;
                }
                Node node = new Node(x + dir.vx, y + dir.vy, cpu);
                nodes.putIfAbsent(node, node);
                neighbours.add(nodes.get(node));
                if (out == 2) OXYGEN_SYSTEM = node;
                node.exploreNeighbours(visited, dir.opposite());
            }
            assert from == null || move(from) > 0;
        }

        private int move(DirectionY dir) {
            cpu.in.offer((long) dir.command);
            cpu.run();
            long out = cpu.out.poll();

            ArrayList<Object> e = new ArrayList<>();
            e.add(this);
            e.add(dir);
            e.add(out);
            moves.add(e);

            return (int) out;
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
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        String program = Files.readString(Paths.get("AoC/resources/day15.txt"));

        IntCodeInterpreter cpu = new IntCodeInterpreter(program);

        Node position = new Node(0, 0, cpu);
        Node.nodes.put(position, position);
        position.exploreNeighbours(new HashSet<>(), null);

        System.out.println(Node.nodes);
        System.out.println(Node.nodes.size());
        System.out.println(Node.OXYGEN_SYSTEM);

        int partOne = dijkstra(position, Node.nodes.keySet()).get(Node.OXYGEN_SYSTEM);
        System.out.println(partOne);
        int partTwo = Collections.max(dijkstra(Node.OXYGEN_SYSTEM, Node.nodes.keySet()).values());
        System.out.println(partTwo);
    }

    public static Map<Node, Integer> dijkstra(Node start, Set<Node> nodes) {
        Map<Node, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        Set<Node> toVisit = new HashSet<>(nodes);
        toVisit.addAll(nodes);

        while (!toVisit.isEmpty()) {
            Node lowestDistance = Collections.min(toVisit, Comparator.comparing(n -> distances.getOrDefault(n, Integer.MAX_VALUE)));
            toVisit.remove(lowestDistance);

            int distance = distances.getOrDefault(lowestDistance, Integer.MAX_VALUE);



            lowestDistance.neighbours.forEach(n -> {
                if (distance + 1 < distances.getOrDefault(n, Integer.MAX_VALUE)) distances.put(n, distance + 1);
            });
        }
        return distances;
    }

    public enum DirectionY {
        NORTH(1, 0, -1),
        EAST(4, 1, 0),
        WEST(3, -1, 0),
        SOUTH(2, 0, 1);

        public final int command;
        public final int vx;
        public final int vy;

        DirectionY(int command, int vx, int vy) {
            this.command = command;
            this.vx = vx;
            this.vy = vy;
        }

        public DirectionY opposite() {
            if (this == NORTH) return SOUTH;
            if (this == SOUTH) return NORTH;
            if (this == EAST) return WEST;
            if (this == WEST) return EAST;
            return null;
        }

        public DirectionY next() {
            if (this == NORTH) return EAST;
            if (this == EAST) return SOUTH;
            if (this == SOUTH) return WEST;
            if (this == WEST) return NORTH;
            return null;
        }

        public DirectionY prev() {
            return next().opposite();
        }
    }
}

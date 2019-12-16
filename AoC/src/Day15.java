import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 {
    public static class Node {
        private static Node OXYGEN_SYSTEM;
        public static final Set<Node> nodes = new HashSet<>();
        public static final List<List<Object>> moves = new ArrayList<>();

        public final int x, y;
        public final IntCodeInterpreter cpu;
        private Map<Direction, Node> neighbours;

        public Node(int x, int y, IntCodeInterpreter cpu) {
            this.x = x;
            this.y = y;
            this.cpu = cpu;
        }

        void exploreNeighbours(Set<Node> visited, Direction from) {
            if (visited.contains(this)) {
                assert from == null || move(from) > 0;
                return;
            }
            visited.add(this);
            for (Direction dir : Direction.values()) {
                long out = move(dir);
                if (out == 0) {
                    continue;
                }
                new Node(x+dir.vx,y+dir.vy,cpu).exploreNeighbours(visited, dir.opposite());
            }
            assert from == null || move(from) > 0;
        }

//        void visitNeighbours(Node from) {
//            if (neighbours == null) initNeighbours();
//            for (Direction dir : neighbours.keySet()) {
//                Node neighbour = neighbours.get(dir);
//                if (neighbour == from) continue;
//                assert move(dir) > 0;
//                neighbour.visitNeighbours(this);
//                assert neighbour.move(dir.opposite()) > 0;
//            }
//        }

//        void initNeighbours() {
//            if (neighbours != null) return;
//            neighbours = new HashMap<>();
//            for (Direction dir : Direction.values()) {
//                Node potentialNeighbour = hasNeighbour(dir);
//                if (potentialNeighbour != null) {
//                    neighbours.put(dir, potentialNeighbour);
//                    nodes.add(potentialNeighbour);
//                }
//            }
//        }

//        Node hasNeighbour(Direction dir) {
//            long out = move(dir);
//            boolean hasNeighbour = out == 1 || out == 2;
//            Node node = new Node(x + dir.vx, y + dir.vy, cpu);
//            if (out == 2) {
//                OXYGEN_SYSTEM = node;
//            }
//            assert !hasNeighbour || node.move(dir.opposite()) > 0;
//            return hasNeighbour ? node : null;
//        }

        private int move(Direction dir) {
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
        Node.nodes.add(position);
        try {
            position.exploreNeighbours(new HashSet<>(), null);
        } catch (Error e) {
            List<Long> broken = Node.moves.stream().filter(l -> (long) l.get(2) > 0).map(l -> (Direction) l.get(1)).map(d -> d.command).map(i -> (long) i).collect(Collectors.toList());

            System.out.println(broken);

            IntCodeInterpreter cpu2 = new IntCodeInterpreter(program);
            cpu2.in.addAll(broken);

            cpu2.run();
            System.out.println(cpu2.out);

            cpu2.in.offer(1L);
            cpu2.run();
            System.out.println(cpu2.out);
            cpu2.in.offer(2L);
            cpu2.run();
            System.out.println(cpu2.out);
            cpu2.in.offer(3L);
            cpu2.run();
            System.out.println(cpu2.out);
            cpu2.in.offer(4L);
            cpu2.run();
            System.out.println(cpu2.out);


            e.printStackTrace();
        }

        System.out.println(Node.nodes);

//        Map<Vector, Cell> map = new HashMap<>();
//        Vector position = new Vector(0, 0);
//
//        Direction travelingIn = Direction.NORTH;
//        map.put(position, Cell.EMPTY);

//        cpu.in.offer((long) 1);
//        cpu.in.offer((long) 2);
//        cpu.in.offer((long) 1);
//
//        cpu.run();
//        System.out.println(cpu.out);
//
//
//        if (true) return;

//
//
//
//
//        LinkedList<Direction> toExplore = new LinkedList<>();
//        toExplore.offer(Direction.NORTH);
//        toExplore.offer(Direction.EAST);
//        toExplore.offer(Direction.SOUTH);
//        toExplore.offer(Direction.WEST);
//
//        int backtrack = 0;
//
//        while (!toExplore.isEmpty()) {
//            Direction moveIn = toExplore.poll();
//
//            Vector newPos = position.add(new Vector(moveIn.vx, moveIn.vy));
//
//            if (map.getOrDefault(newPos, Cell.EMPTY) == Cell.WALL) {
//                continue;
//            }
//
//            cpu.in.offer((long) moveIn.command);
//            cpu.run();
//            long out = cpu.out.pop();
//
//            if (out == 0) {
//                map.putIfAbsent(newPos, Cell.WALL);
//                continue;
//            }
//            if (out == 1) {
//                map.putIfAbsent(newPos, Cell.EMPTY);
//                position = newPos;
//            }
//            if (out == 2) {
//                map.putIfAbsent(newPos, Cell.OXYGEN_SYSTEM);
//                position = newPos;
//            }
//            toExplore.offer(Direction.WEST);
//            toExplore.offer(Direction.SOUTH);
//            toExplore.offer(Direction.EAST);
//            toExplore.offer(Direction.NORTH);
//        }
//        drawMap(map, position);

//        while (true) {
//            drawMap(map, position);
//            System.out.println();
//            Vector newPos = position.add(new Vector(travelingIn.vx, travelingIn.vy));
//
//            if (map.getOrDefault(newPos, Cell.EMPTY) == Cell.WALL) {
//                travelingIn = travelingIn.next();
//                continue;
//            }
//
//            if (map.containsKey(newPos)) {
//                position = newPos;
//                travelingIn = travelingIn.prev();
//                continue;
//            }
//
//            cpu.in.offer((long) travelingIn.command);
//            cpu.run();
//
//            if (map.containsKey(newPos)) {
//                position = newPos;
//                travelingIn = travelingIn.prev();
//                continue;
//            }
//
//
//            long output = cpu.out.pop();
//
//            if (output == 0) {
//                map.putIfAbsent(newPos, Cell.WALL);
//                travelingIn = travelingIn.next();
//                continue;
//            }
//
//            if (map.containsKey(newPos)) break;
//
//            if (output == 1) {
//                map.putIfAbsent(newPos, Cell.EMPTY);
//                position = newPos;
//            }
//            if (output == 2) {
//                map.putIfAbsent(newPos, Cell.OXYGEN_SYSTEM);
//                position = newPos;
//            }
//        }

    }

    private static void drawMap(Map<Vector, Cell> map, Vector position) {
        int minX = Collections.min(map.keySet(), Comparator.comparingInt(o -> o.get(0))).get(0);
        int maxX = Collections.max(map.keySet(), Comparator.comparingInt(o -> o.get(0))).get(0);
        int minY = Collections.min(map.keySet(), Comparator.comparingInt(o -> o.get(1))).get(1);
        int maxY = Collections.max(map.keySet(), Comparator.comparingInt(o -> o.get(1))).get(1);

        Cell[][] mmap = new Cell[maxY - minY + 1][maxX - minX + 1];
        map.forEach((v,c) -> mmap[v.get(1) - minY][v.get(0) - minX] = c);

        for (int y = 0; y < mmap.length; y++) {
            Cell[] row = mmap[y];
            for (int x = 0; x < row.length; x++) {
                Cell cell = row[x];
                if (position.equals(new Vector(x + minX, y + minY))) System.out.print('.');
                else if (cell == Cell.WALL) System.out.print('▓');
                else if (cell == Cell.EMPTY) System.out.print('▒');
                else if (cell == Cell.OXYGEN_SYSTEM) System.out.print('X');
                else System.out.print('░');
            }
            System.out.println();
        }

    }

    public enum Direction {
        NORTH(1, 0, -1),
        EAST(4, 1, 0),
        WEST(3, -1, 0),
        SOUTH(2, 0, 1);

        public final int command;
        public final int vx;
        public final int vy;

        Direction(int command, int vx, int vy) {
            this.command = command;
            this.vx = vx;
            this.vy = vy;
        }

        public Direction opposite() {
            if (this == NORTH) return SOUTH;
            if (this == SOUTH) return NORTH;
            if (this == EAST) return WEST;
            if (this == WEST) return EAST;
            return null;
        }

        public Direction next() {
            if (this == NORTH) return EAST;
            if (this == EAST) return SOUTH;
            if (this == SOUTH) return WEST;
            if (this == WEST) return NORTH;
            return null;
        }

        public Direction prev() {
            return next().opposite();
        }
    }

    public enum Cell {
        WALL,
        EMPTY,
        OXYGEN_SYSTEM
    }
}

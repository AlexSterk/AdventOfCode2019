import misc.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day17 {
    public static void main(String[] args) throws IOException {
        IntCodeInterpreter cpu = new IntCodeInterpreter(Files.readString(Paths.get("AoC/resources/day17.txt")));

        // Part One --- Running the bot and processing where the scaffolds are.

        cpu.run();
        Set<Node> scaffolds = new HashSet<>();
        int x = 0, y = 0;
        Vector robot = null;
        List<Character> directions = List.of('^', '>', '<', 'v');
        for (long currentPiece : (InterpreterQueue) cpu.out) {
            char c = (char) currentPiece;
            System.out.print(c);
            if (directions.contains(c)) {
                robot = new Vector(x, y, directions.indexOf(c) + 1);
            } else if (currentPiece == 35) {
                scaffolds.add(new Node(x, y));
            } else if (currentPiece == 10) {
                x = 0;
                y++;
                continue;
            }
            x++;
        }

        // Part One --- Finding the scaffold intersections.

        scaffolds.forEach(n -> n.findNeighbours(scaffolds));
        Set<Node> intersections = new HashSet<>(scaffolds);
        intersections.removeIf(n -> n.numberOfNeighbours() < 3);

        // Part One --- Finding the sum of alignment parameters.

        int sumAlignmentParameters = intersections.stream().mapToInt(n -> n.x * n.y).sum();
        System.out.println(sumAlignmentParameters);

        // Part Two --- Finding possible paths across the scaffolds

        Node start = new Node(robot.get(0), robot.get(1));
        start.findNeighbours(scaffolds);
        Set<List<Node>> allPaths = new HashSet<>();
        start.findPath(new ArrayList<>(), allPaths, scaffolds);

        // Part Two --- Finding a path which meets the criteria of the movement logic

        Vector finalRobot = robot;
        Path validPath = allPaths.stream().map(l -> new Path(finalRobot, l.stream().skip(1).map(n -> new Vector(n.x, n.y)).collect(Collectors.toList()))).filter(Path::isValid).findAny().get();

        // Part Two --- Running the robot again

        cpu.reset();
        cpu.setMemory(0, 2);
        InterpreterQueue in = (InterpreterQueue) cpu.in;
        in.addAll(validPath.getMainFunction().chars().mapToLong(c -> c).boxed().collect(Collectors.toList()));
        in.addAll(validPath.getMovementFunction("A").chars().mapToLong(c -> c).boxed().collect(Collectors.toList()));
        in.addAll(validPath.getMovementFunction("B").chars().mapToLong(c -> c).boxed().collect(Collectors.toList()));
        in.addAll(validPath.getMovementFunction("C").chars().mapToLong(c -> c).boxed().collect(Collectors.toList()));
        in.add((long) 'n');
        in.add((long) '\n');
        System.out.println(cpu.run());

        // Part Two --- Output
        System.out.println(((LinkedList<Long>) cpu.out).getLast());
    }

    public static class Node {
        public final int x, y;
        private Set<Node> neighbours;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
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

        public void findPath(List<Node> path, Set<List<Node>> paths, Set<Node> nodes) {
            ArrayList<Node> nPath = new ArrayList<>(path);

            nPath.add(this);
            if (nPath.containsAll(nodes)) {
                paths.add(nPath);
                return;
            }

            Set<Node> toVisit = new HashSet<>(neighbours);
            toVisit.removeIf(n -> (nPath.contains(n) && (n.numberOfNeighbours() < 3)) || (path.size() > 0 && path.lastIndexOf(n) == (path.size() - 1)));

            for (Node node : toVisit) {
                node.findPath(nPath, paths, nodes);
            }
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public void findNeighbours(Set<Node> nodes) {
            if (neighbours != null) return;
            neighbours = new HashSet<>(nodes);
            neighbours.removeIf(n -> {
                boolean xSame = n.x == x;
                boolean ySame = n.y == y;

                boolean xDifferent = n.x == x + 1 || n.x == x - 1;
                boolean yDifferent = n.y == y + 1 || n.y == y - 1;

                return !((xSame && yDifferent) || (ySame && xDifferent));
            });
        }

        public int numberOfNeighbours() {
            return neighbours.size();
        }
    }

    public static class Path {
        public static final Pattern VALID_PATH = Pattern.compile("^(.{1,20})\\1*(.{1,20})(?:\\1|\\2)*(.{1,20})(?:\\1|\\2|\\3)*$");

        public final List<String> path;
        private Map<String, String> abc;

        public Path(Vector start, List<Vector> steps) {
            path = new ArrayList<>();
            Vector position = start;

            int count = -1;

            for (int i = 0; i < steps.size(); i++) {
                Vector nextStep = steps.get(i);

                String turn = null;
                Direction currentlyFacing = null, newDirection = null;
                for (Direction dir : Direction.values()) {
                    if (dir.command == position.get(2)) currentlyFacing = dir;
                    if (new Vector(position.get(0), position.get(1)).add(new Vector(dir.vx, dir.vy)).equals(nextStep))
                        newDirection = dir;
                }

                if (currentlyFacing.next() == newDirection) {
                    turn = "R";
                } else if (currentlyFacing.prev() == newDirection) {
                    turn = "L";
                }
                count++;
                if (turn != null) {
                    if (count > 0) path.add(Integer.toString(count));
                    path.add(turn);
                    count = 0;
                }
                position = new Vector(nextStep.get(0), nextStep.get(1), newDirection.command);
            }
            count++;
            if (count > 0) path.add(Integer.toString(count));
        }

        @Override
        public String toString() {
            return path.stream().map(Objects::toString).collect(Collectors.joining(","));
        }

        public int turns() {
            return (int) (path.stream().filter(c -> c.equals("R")).count() + path.stream().filter(c -> c.equals("L")).count());
        }

        public boolean isValid() {
            String commaEnded = toString() + ",";
            Matcher matcher = VALID_PATH.matcher(commaEnded);

            if (!matcher.matches()) return false;
            String A = matcher.group(1);
            String B = matcher.group(2);
            String C = matcher.group(3);

            A = A.substring(0, A.length() - 1);
            B = B.substring(0, B.length() - 1);
            C = C.substring(0, C.length() - 1);

            abc = new HashMap<>();
            abc.put("A", A);
            abc.put("B", B);
            abc.put("C", C);

            String ABC = getMainFunction();

            if (ABC.replaceAll("\n", "").length() > 20) {
                abc = null;
                return false;
            }

            return true;
        }

        public String getMovementFunction(String letter) {
            if (abc == null) return null;

            return abc.get(letter) + "\n";
        }

        public String getMainFunction() {
            if (abc == null) return null;

            return toString().replaceAll(abc.get("A"), "A").replaceAll(abc.get("B"), "B").replaceAll(abc.get("C"), "C") + "\n";
        }
    }
}

package AoC;

import other.IntCodeInterpreter;
import other.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 {
    public static void main(String[] args) throws IOException {
        String inputString = Files.readString(Paths.get("resources/day11.txt"));
        List<Long> initialState = Stream.of(inputString.split("[,\n]")).map(Long::parseLong).collect(Collectors.toList());

        partOne(initialState);
        partTwo(initialState);
    }

    private static void partOne(List<Long> initialState) {
        Map<Vector, Long> painted = runBot(initialState, 0L);

        System.out.println(painted.size());
    }

    private static Map<Vector, Long> runBot(List<Long> initialState, long startPanel) {
        Stack<Long> in = new Stack<>();
        Stack<Long> out = new Stack<>();
        IntCodeInterpreter cpu = new IntCodeInterpreter(initialState, in, out);

        Map<Vector, Long> painted = new HashMap<>();

        Vector location = new Vector(0, 0);
        int currentlyFacing = 0;

        boolean halted = false;

        in.push(startPanel);

        while (!halted) {
            halted = cpu.run();
            long direction = out.pop();
            long color = out.pop();
            painted.put(location, color);
            currentlyFacing = direction == 0 ? turnLeft(currentlyFacing) : turnRight(currentlyFacing);
            location = updatePosition(location, currentlyFacing);
            in.push(painted.getOrDefault(location, 0L));
        }
        return painted;
    }

    private static void partTwo(List<Long> initialState) throws IOException {
        Map<Vector, Long> painted = runBot(initialState, 1L);

        int minX = Collections.min(painted.keySet(), Comparator.comparingInt(o -> o.x)).x;
        int minY = Collections.min(painted.keySet(), Comparator.comparingInt(o -> o.y)).y;

        int maxX = Collections.max(painted.keySet(), Comparator.comparingInt(o -> o.x)).x;
        int maxY = Collections.max(painted.keySet(), Comparator.comparingInt(o -> o.y)).y;

        BufferedImage bufferedImage = new BufferedImage(maxX - minX + 1, maxY - minY + 1, BufferedImage.TYPE_BYTE_BINARY);

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                long pixel = painted.getOrDefault(new Vector(x, y), 0L);
                bufferedImage.setRGB(x - minX,y - minY, (pixel == 1) ? Integer.MAX_VALUE : 0);
            }
        }

        ImageIO.write(bufferedImage,"png", new File("resources/id.png"));

    }

    public static int turnLeft(int currentDirection) {
        return (currentDirection - 1 + 4) % 4;
    }

    public static int turnRight(int currentDirection) {
        return (currentDirection + 1) % 4;
    }

    public static Vector updatePosition(Vector currentPosition, int direction) {
        int x = 0, y = 0;
        if (direction == 0) y = -1;
        if (direction == 1) x = 1;
        if (direction == 2) y = 1;
        if (direction == 3) x = -1;

        return currentPosition.add(new Vector(x, y));
    }
}

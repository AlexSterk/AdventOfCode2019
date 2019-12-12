import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day1 {
    public static void main(String[] args) throws IOException {
        partOne();
        partTwo();
    }

    private static void partOne() throws IOException {
        Stream<String> modules = Files.lines(Paths.get("resources/day1.txt"));
        System.out.println(modules.mapToInt(Integer::parseInt).map(m -> Math.floorDiv(m, 3) - 2).sum());
    }

    private static void partTwo() throws IOException {
        Stream<String> modules = Files.lines(Paths.get("resources/day1.txt"));
        System.out.println(modules.mapToInt(Integer::parseInt).map(Day1::calcFuel).sum());
    }

    private static int calcFuel(int mass) {
        int fuel = Math.floorDiv(mass, 3) - 2;
        if (fuel <= 0) return 0;
        return fuel + calcFuel(fuel);
    }
}

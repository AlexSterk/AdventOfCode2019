import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day19 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("AoC/resources/day19.txt"));
        partOne(input);
        partTwo(input);
    }

    private static void partTwo(String input) throws IOException {
        IntCodeInterpreter cpu = new IntCodeInterpreter(input);

        int y = 100;
        int x = 0;

        while (true) {
            while (!inTractorBeam(x, y, cpu)) x++;
            if (inTractorBeam(x + 99, y - 99, cpu)) {
                System.out.println(x + " " + y);
                System.out.println(10000 * x + y - 99);
                break;
            }
            y++;
        }
    }

    private static boolean inTractorBeam(int x, int y, IntCodeInterpreter cpu) throws IOException {
        cpu.reset();
        ((InterpreterQueue) cpu.in).addAll(List.of((long) x, (long) y));
        cpu.run();
        return ((InterpreterQueue) cpu.out).poll() == 1;
    }

    private static void partOne(String input) throws IOException {
        IntCodeInterpreter cpu = new IntCodeInterpreter(input);
        int width = 50, height = 50;
        int totalPulled = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ((InterpreterQueue) cpu.in).addAll(List.of((long) x, (long) y));
                cpu.run();
                Long poll = ((InterpreterQueue) cpu.out).poll();
                totalPulled += poll;
                cpu.reset();
            }
        }
        System.out.println(totalPulled);
    }
}

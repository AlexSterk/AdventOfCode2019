import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 {
    public static void main(String[] args) throws IOException {
        test();
        partOne();
        partTwo();
    }

    private static void partOne() throws IOException {
        String inputString = Files.readString(Paths.get("resources/day9.txt"));
        List<Long> initialState = Stream.of(inputString.split("[,\n]")).map(Long::parseLong).collect(Collectors.toList());

        Stack<Long> in = new Stack<>();
        Stack<Long> out = new Stack<>();
        IntCodeInterpreter cpu = new IntCodeInterpreter(initialState, in, out);
        in.push((long) 1);
        cpu.run();
        System.out.println(out);
    }

    private static void partTwo() throws IOException {
        String inputString = Files.readString(Paths.get("resources/day9.txt"));
        List<Long> initialState = Stream.of(inputString.split("[,\n]")).map(Long::parseLong).collect(Collectors.toList());

        Stack<Long> in = new Stack<>();
        Stack<Long> out = new Stack<>();
        IntCodeInterpreter cpu = new IntCodeInterpreter(initialState, in, out);
        in.push((long) 2);
        cpu.run();
        System.out.println(out);
    }

    private static void test() throws IOException {
        List<List<Long>> programs = Files.lines(Paths.get("resources/day9-test.txt")).map(l -> l.split(",")).map(List::of).map(l -> l.stream().map(Long::parseLong).collect(Collectors.toList())).collect(Collectors.toList());

        for (List<Long> program : programs) {
            Stack<Long> in = new Stack<>();
            Stack<Long> out = new Stack<>();
            IntCodeInterpreter cpu = new IntCodeInterpreter(program, in, out);
            cpu.run();
            System.out.println(out);
        }
    }
}
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day21 {
    public static void main(String[] args) throws IOException {
        IntCodeInterpreter cpu = new IntCodeInterpreter(Files.readString(Paths.get("AoC/resources/day21.txt")));
        String collect = partTwoProgram();
        runProgram(cpu, collect);
    }

    private static void runProgram(IntCodeInterpreter cpu, String collect) throws IOException {
        ((InterpreterQueue) cpu.in).addAll(collect.chars().mapToLong(i -> i).boxed().collect(Collectors.toList()));
        cpu.run();
        ((InterpreterQueue) cpu.out).stream().map(aLong -> aLong > 127 ? aLong.toString() : Character.toString((char) aLong.intValue())).forEach(System.out::print);
    }

    private static String partOneProgram() {
        List<String> program = new ArrayList<>();
        program.add("NOT A J");
        program.add("NOT B T");
        program.add("OR T J");
        program.add("NOT C T");
        program.add("OR T J");
        program.add("AND D J");
        program.add("WALK\n");
        return String.join("\n", program);
    }

    private static String partTwoProgram() {
        List<String> program = List.of(
                "NOT A T",
                "NOT T T",
                "AND B T",
                "AND C T",
                "NOT T J",
                "AND D J",  // until now, this is the same as part 1
                "NOT E T",
                "NOT T T",
                "OR  H T",
                "AND T J",
                "RUN\n"
        );
        return String.join("\n", program);
    }
}

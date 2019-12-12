import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
    private static Map<Integer, BiFunction<Integer, Integer, Integer>> opCodes = new HashMap<>();

    static {
        opCodes.put(1, Integer::sum);
        opCodes.put(2, (i, j) -> i * j);
    }

    public static void main(String[] args) throws IOException {
        String inputString = Files.readString(Paths.get("resources/day2.txt"));
        List<Integer> initialState = Stream.of(inputString.split("[,\n]")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> program;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                program = new ArrayList<>(initialState);
                List<Integer> result = runProgram(program, i, j);
                if (result.get(0) == 19690720) {
                    System.out.println(100 * i + j);
                }
            }
        }
    }

    private static List<Integer> runProgram(List<Integer> program, int noun, int verb) {
        program.set(1, noun);
        program.set(2, verb);

        int i = 0;
        int opcode = program.get(0);
        while (opcode != 99) {
            int pos1 = program.get(++i);
            int pos2 = program.get(++i);
            int save = program.get(++i);

            int one = program.get(pos1);
            int two = program.get(pos2);

            int result = opCodes.get(opcode).apply(one, two);
            program.set(save, result);
            opcode = program.get(++i);
        }
        return program;
    }
}

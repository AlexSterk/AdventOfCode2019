package AoC;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {
    public static void main(String[] args) throws IOException {
        String inputString = Files.readString(Paths.get("resources/day5.txt"));
        List<Integer> program = Stream.of(inputString.split("[,\n]")).map(Integer::parseInt).collect(Collectors.toList());

        int input = 5;

        int instructionPointer = 0;
        String opcode = String.valueOf(program.get(0));
        while (!opcode.equals("99")) {
            OpCode instruction = null;

            for (OpCode c : OpCode.values()) {
                if (opcode.endsWith(c.code.toString())) instruction = c;
            }

            List<Integer> arguments = new ArrayList<>(program.subList(++instructionPointer, (instructionPointer += instruction.argumentCount)));

            if (instruction == OpCode.Store) {
                arguments.add(input);
            }

            instructionPointer = instruction.apply(opcode, program, arguments, instructionPointer);

            opcode = program.get(instructionPointer).toString();
        }
    }

    public enum OpCode {

        Add(1, 3, i -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0) + arguments.get(1));
            return i;
        }, 2),
        Multiply(2, 3, i -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0) * arguments.get(1));
            return i;
        }, 2),
        Store(3, 1, i -> (program, arguments) -> {
            program.set(arguments.get(0), arguments.get(1));
            return i;
        }, 0),
        Print(4, 1, i -> (program, arguments) -> {
            System.out.println(program.get(arguments.get(0)));
            return i;
        }, 0),
        JumpIfTrue(5, 2, i -> (program, arguments) -> arguments.get(0) != 0 ? arguments.get(1) : i, 2),
        JumpIfFalse(6, 2, i -> (program, arguments) -> arguments.get(0) == 0 ? arguments.get(1) : i, 2),
        LessThan(7, 3, i -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0) < arguments.get(1) ? 1 : 0);
            return i;
        }, 2),
        Equals(8, 3, i -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0).equals(arguments.get(1)) ? 1 : 0);
            return i;
        }, 2);

        static BiFunction<List<Integer>, List<Integer>, BiConsumer<String, Integer>> fixArgs = (program, arguments) -> (opcode, args) -> {
            for (Integer i = 0; i < args; i++) {
                if (opcode.charAt(2 - i) == '0') arguments.set(i, program.get(arguments.get(i)));
            }
        };

        Integer code;
        int argumentCount;
        Function<Integer, BiFunction<List<Integer>, List<Integer>, Integer>> execution;
        Function<String, BiConsumer<List<Integer>, List<Integer>>> argumentFixer;

        OpCode(int code, int arguments, Function<Integer, BiFunction<List<Integer>, List<Integer>, Integer>> execution, int paramModeArgs) {
            this.code = code;
            this.argumentCount = arguments;
            this.execution = execution;

            this.argumentFixer = (opcode) -> ((program, args) -> {
                for (int i = 0; i < paramModeArgs; i++) {
                    if (opcode.charAt(2 - i) == '0') args.set(i, program.get(args.get(i)));
                }
            });
        }

        int apply(String opcode, List<Integer> program, List<Integer> arguments, int instructionPointer) {
            String fullCode = "0".repeat(5 - opcode.length()) + opcode;

            this.argumentFixer.apply(fullCode).accept(program, arguments);
            instructionPointer = execution.apply(instructionPointer).apply(program, arguments);
            return instructionPointer;
        }
    }
}

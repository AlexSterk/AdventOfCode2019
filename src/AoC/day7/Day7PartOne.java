package AoC.day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7PartOne {
    public static <T> void findPermutations(int n, T[] elements, Set<T[]> permutations) {

        if(n == 1) {
            T[] permutation = Arrays.copyOf(elements, elements.length);
            permutations.add(permutation);
        } else {
            for(int i = 0; i < n-1; i++) {
                findPermutations(n - 1, elements, permutations);
                if(n % 2 == 0) {
                    swap(elements, i, n-1);
                } else {
                    swap(elements, 0, n-1);
                }
            }
            findPermutations(n - 1, elements, permutations);
        }
    }

    private static <T> void swap(T[] input, int a, int b) {
        T tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    public static void main(String[] args) throws IOException {
        String inputString = Files.readString(Paths.get("resources/day7.txt"));
        List<Integer> initialState = Stream.of(inputString.split("[,\n]")).map(Integer::parseInt).collect(Collectors.toList());

        Integer[] phaseSettings = {0,1,2,3,4};
        HashSet<Integer[]> permutations = new HashSet<>();
        findPermutations(5, phaseSettings, permutations);

        for (Integer[] permutation : permutations) {
            for (int i = 0; i < permutation.length; i++) {
                if (i == 0) OpCode.STACK.push(0);
                OpCode.STACK.push(permutation[i]);
                runProgram(initialState);
            }
        }


        System.out.println(OpCode.STACK.stream().max(Integer::compareTo));
//
//        int[] t = {4,3,2,1,0};
//
//        for (int i = 0; i < 5; i++) {
//            if (i == 0) OpCode.STACK.push(0);
//            OpCode.STACK.push(t[i]);
//            System.out.println(OpCode.STACK);
//
//            runProgram(initialState);
//        }
    }

    private static void runProgram(List<Integer> initialState) {
        List<Integer> program = new ArrayList<>(initialState);
        int instructionPointer = 0;
        String opcode = String.valueOf(program.get(0));
        while (!opcode.equals("99")) {
            OpCode instruction = null;

            for (OpCode c : OpCode.values()) {
                if (opcode.endsWith(c.code.toString())) instruction = c;
            }

            List<Integer> arguments = new ArrayList<>(program.subList(++instructionPointer, (instructionPointer += instruction.argumentCount)));

            instructionPointer = instruction.apply(opcode, program, arguments, instructionPointer);

            opcode = program.get(instructionPointer).toString();
        }
    }

    public enum OpCode {

        Add(1, 3, (i, s) -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0) + arguments.get(1));
            return i;
        }, 2),
        Multiply(2, 3, (i, s) -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0) * arguments.get(1));
            return i;
        }, 2),
        Store(3, 1, (i, s) -> (program, arguments) -> {
            program.set(arguments.get(0), s.pop());
            return i;
        }, 0),
        Print(4, 1, (i, s) -> (program, arguments) -> {
            s.push(program.get(arguments.get(0)));
            System.out.println(s.peek());
            return i;
        }, 0),
        JumpIfTrue(5, 2, (i, s) -> (program, arguments) -> arguments.get(0) != 0 ? arguments.get(1) : i, 2),
        JumpIfFalse(6, 2, (i, s) -> (program, arguments) -> arguments.get(0) == 0 ? arguments.get(1) : i, 2),
        LessThan(7, 3, (i, s) -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0) < arguments.get(1) ? 1 : 0);
            return i;
        }, 2),
        Equals(8, 3, (i, s) -> (program, arguments) -> {
            program.set(arguments.get(2), arguments.get(0).equals(arguments.get(1)) ? 1 : 0);
            return i;
        }, 2);

        static BiFunction<List<Integer>, List<Integer>, BiConsumer<String, Integer>> fixArgs = (program, arguments) -> (opcode, args) -> {
            for (Integer i = 0; i < args; i++) {
                if (opcode.charAt(2 - i) == '0') arguments.set(i, program.get(arguments.get(i)));
            }
        };
        static Stack<Integer> STACK = new Stack<>();

        Integer code;
        int argumentCount;
        BiFunction<Integer, Stack<Integer>, BiFunction<List<Integer>, List<Integer>, Integer>> execution;
        Function<String, BiConsumer<List<Integer>, List<Integer>>> argumentFixer;

        OpCode(int code, int arguments, BiFunction<Integer, Stack<Integer>, BiFunction<List<Integer>, List<Integer>, Integer>> execution, int paramModeArgs) {
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
            instructionPointer = execution.apply(instructionPointer, STACK).apply(program, arguments);
            return instructionPointer;
        }
    }
}

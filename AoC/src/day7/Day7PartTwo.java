package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7PartTwo {
    @FunctionalInterface
    public interface IntCodeFunction {
        int run(int pointer, Stack<Integer> in, Stack<Integer> out, List<Integer> program, List<Integer> arguments, String name) throws IOException;
    }
    
    public static class Amplifier {
        public final List<Integer> program;
        private final String name;
        public final Stack<Integer> in = new Stack<>();
        private int instructionPointer = 0;

        public Amplifier(List<Integer> initialState, String name) {
            this.program = new ArrayList<>(initialState);
            this.name = name;
        }

        public boolean run(Stack<Integer> out) throws IOException {
            String opcode = String.valueOf(program.get(instructionPointer));
            while (!opcode.equals("99")) {
                OpCode instruction = null;

                for (OpCode c : OpCode.values()) {
                    if (opcode.endsWith(c.code.toString())) instruction = c;
                }

                if (instruction == OpCode.Store && in.isEmpty()) return false;

                List<Integer> arguments = new ArrayList<>(program.subList(++instructionPointer, (instructionPointer += instruction.argumentCount)));

                instructionPointer = instruction.apply(opcode, program, arguments, instructionPointer, in, out, name);

                opcode = program.get(instructionPointer).toString();
            }
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        String inputString = Files.readString(Paths.get("resources/day7.txt"));
        List<Integer> initialState = Stream.of(inputString.split("[,\n]")).map(Integer::parseInt).collect(Collectors.toList());

        Integer[] phaseSettings = {9,8,7,6,5};
        HashSet<Integer[]> permutations = new HashSet<>();
        findPermutations(5, phaseSettings, permutations);

        String[] names = {"A", "B", "C", "D", "E"};

//        Integer[] permutation = phaseSettings;

        Stack<Integer> stack = new Stack<>();
        for (Integer[] permutation : permutations) {
            List<Amplifier> amps = new ArrayList<>();
            for (int i = 0; i < permutation.length; i++) {
                Amplifier amplifier = new Amplifier(initialState, names[i]);
                if (i == 0) amplifier.in.push(0);
                amps.add(amplifier);
            }

            boolean halted = false, firstRun = true;

            while (!halted) {
                for (int i = 0; i < amps.size(); i++) {
                    Amplifier amplifier = amps.get(i);
                    if (firstRun) amplifier.in.push(permutation[i]);

                    halted = amplifier.run(amps.get((i + 1) % 5).in);
                }
                firstRun = false;
            }
            stack.push(amps.get(0).in.pop());
        }
        System.out.println(Collections.max(stack));
    }

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

    public enum OpCode {

        Add(1, 3, (i, in, out, program, arguments, name) -> {
            program.set(arguments.get(2), arguments.get(0) + arguments.get(1));
            return i;
        }, 2),
        Multiply(2, 3, (i, in, out, program, arguments, name) -> {
            program.set(arguments.get(2), arguments.get(0) * arguments.get(1));
            return i;
        }, 2),
        Store(3, 1, (i, in, out, program, arguments, name) -> {
            Integer pop = in.pop();
//            System.out.println(name + " READS " + pop);
            program.set(arguments.get(0), pop);
            return i;
        }, 0),
        Print(4, 1, (i, in, out, program, arguments, name) -> {
            out.push(program.get(arguments.get(0)));
//            System.out.println(out.peek());
            return i;
        }, 0),
        JumpIfTrue(5, 2, (i, in, out, program, arguments, name) -> arguments.get(0) != 0 ? arguments.get(1) : i, 2),
        JumpIfFalse(6, 2, (i, in, out, program, arguments, name) -> arguments.get(0) == 0 ? arguments.get(1) : i, 2),
        LessThan(7, 3, (i, in, out, program, arguments, name) -> {
            program.set(arguments.get(2), arguments.get(0) < arguments.get(1) ? 1 : 0);
            return i;
        }, 2),
        Equals(8, 3, (i, in, out, program, arguments, name) -> {
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
        IntCodeFunction execution;
        Function<String, BiConsumer<List<Integer>, List<Integer>>> argumentFixer;

        OpCode(int code, int arguments, IntCodeFunction execution, int paramModeArgs) {
            this.code = code;
            this.argumentCount = arguments;
            this.execution = execution;

            this.argumentFixer = (opcode) -> ((program, args) -> {
                for (int i = 0; i < paramModeArgs; i++) {
                    if (opcode.charAt(2 - i) == '0') args.set(i, program.get(args.get(i)));
                }
            });
        }

        int apply(String opcode, List<Integer> program, List<Integer> arguments, int instructionPointer, Stack<Integer> in, Stack<Integer> out, String name) throws IOException {
            String fullCode = "0".repeat(5 - opcode.length()) + opcode;

            this.argumentFixer.apply(fullCode).accept(program, arguments);
            instructionPointer = execution.run(instructionPointer, in, out, program, arguments, name);
            return instructionPointer;
        }
    }
}

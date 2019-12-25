import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntCodeInterpreter {
    private final List<Long> initialState;
    private List<Long> program;
    public final InterpreterInput in;
    public final InterpreterOutput out;
    public final List<IntCodeAST.Instruction> executedInstructions = new ArrayList<>();
    private int pointer = 0;
    private boolean halted = false;
    private int relBase = 0;
    private Map<Long, Long> extraMemory = new HashMap<>();

    private static List<Long> toProgram(String program) {
        return Stream.of(program.split("[^-0-9]+")).map(Long::parseLong).collect(Collectors.toList());
    }

    public IntCodeInterpreter(String program) {
        this(toProgram(program));
    }

    public IntCodeInterpreter(List<Long> program) {
        this(program, new InterpreterQueue(), new InterpreterQueue());
    }

    public IntCodeInterpreter(List<Long> program, InterpreterInput in, InterpreterOutput out) {
        this.initialState = program;
        this.program = new ArrayList<>(program);
        this.in = in;
        this.out = out;
    }

    public void reset() {
        program = new ArrayList<>(initialState);
        in.clear();
        out.clear();
        pointer = 0;
        relBase = 0;
        extraMemory.clear();
        halted = false;
        executedInstructions.clear();
    }

    public Long getMemory(long address) {
        if (address < program.size()) return program.get(Math.toIntExact(address));
        return extraMemory.getOrDefault(address, 0L);
    }

    public void setMemory(long address, long value) {
        if (address < program.size()) program.set(Math.toIntExact(address), value);
        else extraMemory.put(address, value);
    }

    public boolean run() throws IOException {
        while (!halted) {
            int currentPointer = pointer;
            Long opCode = program.get(pointer);

            String stringCode = String.valueOf(opCode);
            String fullOpCode = "0".repeat(5 - stringCode.length()) + stringCode;

            Optional<OpCode> instruction = Arrays.stream(OpCode.values()).filter(c -> fullOpCode.endsWith(c.opcode)).findFirst();
            if (instruction.isEmpty()) {
                System.out.println("UNIDENTIFIED OPCODE");
                return false;
            }

            IntCodeAST.Argument a1;
            IntCodeAST.Argument a2;
            IntCodeAST.Argument a3;
            switch (instruction.get()) {
                case Add:
                case Multiply:
                    a1 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    a2 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    a3 = new IntCodeAST.Argument.Abs(getMemory(++pointer));

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a1, 2);
                    if (isNotImmediateMode(fullOpCode, 1)) a2 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a2, 1);
                    if (isRelativeMode(fullOpCode, 0)) a3 = new IntCodeAST.Argument.Rel(a3.value + relBase, a3.value);
                    else a3 = new IntCodeAST.Argument.Pos(a3.value, a3.value);

                    setMemory(a3.value, (instruction.get() == OpCode.Add) ?  a1.value + a2.value : a1.value * a2.value);
                    pointer++;

                    executedInstructions.add((instruction.get() == OpCode.Add) ? new IntCodeAST.Instruction.Add(relBase, currentPointer, a1, a2, (IntCodeAST.Argument.PositionalArgument) a3) : new IntCodeAST.Instruction.Mul(relBase, currentPointer, a1, a2, (IntCodeAST.Argument.PositionalArgument) a3));

                    break;
                case Input:
                    if (in.isEmpty()) return false;

                    a1 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    a2 = new IntCodeAST.Argument.Abs(in.getNext());

                    if (isRelativeMode(fullOpCode, 2)) a1 = new IntCodeAST.Argument.Rel(a1.value + relBase, a1.value);
                    else a1 = new IntCodeAST.Argument.Pos(a1.value, a1.value);

                    setMemory(Math.toIntExact(a1.value), a2.value);
                    pointer++;

                    executedInstructions.add(new IntCodeAST.Instruction.In(relBase, pointer, (IntCodeAST.Argument.PositionalArgument) a1, (IntCodeAST.Argument.Abs) a2));

                    break;
                case Output:
                    a1 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a1, 2);
                    out.write(a1.value);
                    pointer++;

                    executedInstructions.add(new IntCodeAST.Instruction.Out(relBase, pointer, a1));

                    break;
                case JumpIfTrue:
                case JumpIfFalse:
                    a1 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    a2 = new IntCodeAST.Argument.Abs(getMemory(++pointer));

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a1, 2);
                    if (isNotImmediateMode(fullOpCode, 1)) a2 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a2, 1);

                    pointer = Math.toIntExact((instruction.get() == OpCode.JumpIfTrue && a1.value != 0 || instruction.get() == OpCode.JumpIfFalse && a1.value == 0) ? a2.value : pointer + 1);

                    executedInstructions.add((instruction.get() == OpCode.JumpIfTrue) ? new IntCodeAST.Instruction.JumpIfTrue(relBase, currentPointer, a1, a2) : new IntCodeAST.Instruction.JumpIfFalse(relBase, currentPointer, a1, a2));

                    break;
                case LessThan:
                case Equals:
                    a1 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    a2 = new IntCodeAST.Argument.Abs(getMemory(++pointer));
                    a3 = new IntCodeAST.Argument.Abs(getMemory(++pointer));

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a1, 2);
                    if (isNotImmediateMode(fullOpCode, 1)) a2 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a2, 1);
                    if (isRelativeMode(fullOpCode, 0)) a3 = new IntCodeAST.Argument.Rel(a3.value + relBase, a3.value);
                    else a3 = new IntCodeAST.Argument.Pos(a3.value, a3.value);


                    if (instruction.get() == OpCode.LessThan) setMemory(Math.toIntExact(a3.value), (a1.value < a2.value) ? 1 : 0);
                    else setMemory(a3.value, (a1.value == a2.value) ? 1 : 0);

                    pointer++;

                    executedInstructions.add((instruction.get() == OpCode.LessThan) ? new IntCodeAST.Instruction.LessThan(relBase, currentPointer, a1, a2, (IntCodeAST.Argument.PositionalArgument) a3) : new IntCodeAST.Instruction.Equals(relBase, currentPointer, a1, a2, (IntCodeAST.Argument.PositionalArgument) a3));

                    break;
                case AdjustBase:
                    a1 = new IntCodeAST.Argument.Abs(getMemory(++pointer));

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, (IntCodeAST.Argument.Abs) a1, 2);

                    relBase += a1.value;
                    pointer++;

                    executedInstructions.add(new IntCodeAST.Instruction.AdjustBase(relBase, pointer, a1));

                    break;
                case Halt:
                    halted = true;

                    executedInstructions.add(new IntCodeAST.Instruction.Halt(relBase, pointer));

                    return true;
            }
        }
        return halted;
    }

    private IntCodeAST.Argument getArgument(String fullOpCode, IntCodeAST.Argument.Abs a, int i) {
        if ((isRelativeMode(fullOpCode, i)))
            return new IntCodeAST.Argument.Rel(getMemory(a.value + relBase), a.value);
        return new IntCodeAST.Argument.Pos(getMemory(a.value), a.value);
    }

    private static boolean isNotImmediateMode(String fullOpCode, int i) {
        return fullOpCode.charAt(i) != '1';
    }

    private static boolean isRelativeMode(String fullOpCode, int i) {
        return fullOpCode.charAt(i) == '2';
    }

    public enum OpCode {
        Add("01"),
        Multiply("02"),
        Input("03"),
        Output("04"),
        JumpIfTrue("05"),
        JumpIfFalse("06"),
        LessThan("07"),
        Equals("08"),
        AdjustBase("09"),
        Halt("99");

        public final String opcode;

        OpCode(String opcode) {
            this.opcode = opcode;
        }
    }

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            String inputString = Files.readString(Paths.get(arg));
            List<Long> initialState = Stream.of(inputString.split(",")).map(Long::parseLong).collect(Collectors.toList());

            IntCodeInterpreter cpu = new IntCodeInterpreter(initialState);
            boolean halted;
            Scanner scanner = new Scanner(System.in);
            do {
                halted = cpu.run();
                if (!halted) ((InterpreterQueue) cpu.in).write(scanner.nextLong());
            } while (!halted);
            System.out.println("Output for: " + arg);
            System.out.println(cpu.out);
        }
    }
}

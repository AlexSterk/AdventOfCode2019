package intcode;

import java.util.*;

public class IntCodeInterpreter {
    private final List<Long> program;
    private final Stack<Long> in;
    private final Stack<Long> out;
    private int pointer = 0;
    private boolean halted = false;
    private int relBase = 0;
    private Map<Long, Long> extraMemory = new HashMap<>();

    public IntCodeInterpreter(List<Long> program, Stack<Long> in, Stack<Long> out) {
        this.program = new ArrayList<>(program);
        this.in = in;
        this.out = out;
    }

    private Long getMemory(long address) {
        if (address < program.size()) return program.get(Math.toIntExact(address));
        return extraMemory.getOrDefault(address, 0L);
    }

    private void setMemory(long address, long value) {
        if (address < program.size()) program.set(Math.toIntExact(address), value);
        else extraMemory.put(address, value);
    }

    public boolean run() {
        while (!halted) {
            Long opCode = program.get(pointer);

            String stringCode = String.valueOf(opCode);
            String fullOpCode = "0".repeat(5 - stringCode.length()) + stringCode;

            Optional<OpCode> instruction = Arrays.stream(OpCode.values()).filter(c -> fullOpCode.endsWith(c.opcode)).findFirst();
            if (instruction.isEmpty()) {
                System.out.println("UNIDENTIFIED OPCODE");
                return false;
            }

            Long a1;
            Long a2;
            Long a3;
            switch (instruction.get()) {
                case Add:
                case Multiply:
                    a1 = getMemory(++pointer);
                    a2 = getMemory(++pointer);
                    a3 = getMemory(++pointer);

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, a1, 2);
                    if (isNotImmediateMode(fullOpCode, 1)) a2 = getArgument(fullOpCode, a2, 1);
                    if (isRelativeMode(fullOpCode, 0)) a3 += relBase;

                    setMemory(a3, (instruction.get() == OpCode.Add) ?  a1 + a2 : a1 * a2);
                    pointer++;

                    break;
                case Input:
                    if (in.isEmpty()) return false;

                    a1 = getMemory(++pointer);
                    a2 = in.pop();

                    if (isRelativeMode(fullOpCode, 2)) a1 += relBase;

                    setMemory(Math.toIntExact(a1), a2);
                    pointer++;
                    break;
                case Output:
                    a1 = getMemory(++pointer);
                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, a1, 2);
                    out.push(a1);
                    pointer++;
                    break;
                case JumpIfTrue:
                case JumpIfFalse:
                    a1 = getMemory(++pointer);
                    a2 = getMemory(++pointer);

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, a1, 2);
                    if (isNotImmediateMode(fullOpCode, 1)) a2 = getArgument(fullOpCode, a2, 1);

                    pointer = Math.toIntExact((instruction.get() == OpCode.JumpIfTrue && a1 != 0 || instruction.get() == OpCode.JumpIfFalse && a1 == 0) ? a2 : pointer + 1);
                    break;
                case LessThan:
                case Equals:
                    a1 = getMemory(++pointer);
                    a2 = getMemory(++pointer);
                    a3 = getMemory(++pointer);

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, a1, 2);
                    if (isNotImmediateMode(fullOpCode, 1)) a2 = getArgument(fullOpCode, a2, 1);
                    if (isRelativeMode(fullOpCode, 0)) a3 += relBase;

                    if (instruction.get() == OpCode.LessThan) setMemory(Math.toIntExact(a3), (long) ((a1 < a2) ? 1 : 0));
                    else setMemory(a3, (a1.equals(a2)) ? 1 : 0);

                    pointer++;
                    break;
                case AdjustBase:
                    a1 = getMemory(++pointer);

                    if (isNotImmediateMode(fullOpCode, 2)) a1 = getArgument(fullOpCode, a1, 2);

                    relBase += a1;
                    pointer++;
                    break;
                case Halt:
                    halted = true;
                    return true;
            }
        }
        return halted;
    }

    private Long getArgument(String fullOpCode, Long a, int i) {
        return getMemory(Math.toIntExact(a) + (isRelativeMode(fullOpCode, i) ? relBase : 0));
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
}

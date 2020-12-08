package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static days.GameConsole.*;
import static days.Operation.*;

public class Day8 extends Day {

    private List<Instruction> instructions;
    private GameConsole gc;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\r?\n")).map(Instruction::stringToInstruction).collect(Collectors.toList());
        gc = new GameConsole();
        gc.loadProgram(instructions);
    }

    @Override
    public void part1() {
        try {
            System.out.println(gc.loop());
        } catch (ProgramEndReached programEndReached) {
            System.exit(1);
        }
    }

    @Override
    public void part2() {
        gc.reset();
        System.out.println(gc.fixProgram());
    }

    @Override
    public int getDay() {
        return 8;
    }
}

class GameConsole {
    private int accumulator;
    private int pc;
    private List<Instruction> instructions;
    
    GameConsole() {
        reset();
    }
    
    void reset() {
        accumulator = 0;
        pc = 0;
        if (instructions != null) instructions.forEach(Instruction::reset);
    }
    
    void loadProgram(List<Instruction> instructions) {
        this.instructions = instructions;
    }
    
    void step() {
        Instruction instruction = instructions.get(pc);
        
        switch (instruction.operation) {
            case ACC -> accumulator += instruction.argument;
            case JMP -> pc += instruction.argument;
            case NOP -> {}
        }
        
        if (instruction.operation != JMP) pc++;
        instruction.execute();
    }
    
    int loop() throws ProgramEndReached {
        do {
            step();
            if (pc >= instructions.size()) throw new ProgramEndReached(accumulator);
        } while (instructions.get(pc).getExecuted() < 1);

        return accumulator;
    }
    
    int fixProgram() {
        for (int i = 0; i < instructions.size(); i++) {
            Instruction current = instructions.get(i);
            if (current.operation == ACC) continue;
            Instruction fixed = new Instruction(current.operation == JMP ? NOP : JMP, current.argument);
            List<Instruction> newInsts = new ArrayList<>(instructions);
            newInsts.set(i, fixed);

            GameConsole gc = new GameConsole();
            gc.instructions = newInsts;
            gc.reset();

            try {
                gc.loop();
            } catch (ProgramEndReached programEndReached) {
                return programEndReached.accumulator;
            }
        }
        
        return -1;
    }

    static class ProgramEndReached extends Throwable {
        private final int accumulator;

        public ProgramEndReached(int accumulator) {
            this.accumulator = accumulator;
        }
    }
}

final class Instruction {
    final Operation operation;
    final int argument;
    
    private int executed = 0;

    Instruction(Operation operation, int argument) {
        this.operation = operation;
        this.argument = argument;
    }

    static Instruction stringToInstruction(String s) {
        String[] split = s.split(" ");
        return switch (split[0]) {
            case "acc" -> new Instruction(ACC, Integer.parseInt(split[1]));
            case "jmp" -> new Instruction(JMP, Integer.parseInt(split[1]));
            default -> new Instruction(NOP, Integer.parseInt(split[1]));
        };
    }

    @Override
    public String toString() {
        return "Instruction[" +
                "operation=" + operation + ", " +
                "argument=" + argument + ']';
    }
    
    void execute() {
        executed++;
    }
    
    int getExecuted() {
        return executed;
    }
    
    void reset() {
        executed = 0;
    }
}

enum Operation {
    ACC,
    JMP,
    NOP;
}

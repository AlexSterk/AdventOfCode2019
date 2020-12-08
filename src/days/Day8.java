package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        gc.runUntilSecondExecute();
    }

    @Override
    public void part2() {

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
    
    void runUntilSecondExecute() {
        do {
            step();
        } while (instructions.get(pc).getExecuted() < 1);

        System.out.println(accumulator);
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
}

enum Operation {
    ACC,
    JMP,
    NOP;
}

package days;

import misc.Direction;
import misc.Pair;
import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static misc.Direction.*;

public class Day12 extends Day {

    private List<String> instructions;
    private Pair pos;
    private Direction facing;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\r?\n")).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        pos = new Pair(0,0);
        facing = EAST;
        
        instructions.forEach(this::followInstruction);
        Pair absolute = pos.absolute();
        System.out.println(absolute.key() + absolute.value());
    }
    
    private void followInstruction(String ins) {
        char c = ins.charAt(0);
        int value = Integer.parseInt(ins.substring(1));

        switch (c) {
            case 'F' -> {
                for (int i = 0; i < value; i++) {
                    pos = pos.add(facing);
                }
            }
            case 'L', 'R' -> {
                int times = value / 90;
                if (c == 'L') {
                    for (int i = 0; i < times; i++) {
                        facing = facing.getLeft();
                    }
                } else {
                    for (int i = 0; i < times; i++) {
                        facing = facing.getRight();
                    }
                }
            }
            case 'N', 'S', 'E', 'W' -> {
                Direction to = Direction.getById(c);
                for (int i = 0; i < value; i++) {
                    pos = pos.add(to);
                }
            }
        }
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 12;
    }
}

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
    private Pair facing;


    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\r?\n")).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        pos = new Pair(0,0);
        facing = EAST;
        
        instructions.forEach(ins -> followInstructionBoat(ins, false));
        Pair absolute = pos.absolute();
        System.out.println(absolute.key() + absolute.value());
    }
    
    @Override
    public void part2() {
        pos = new Pair(0,0);
        facing = new Pair(10, -1);

        instructions.forEach(ins -> followInstructionBoat(ins, true));
        Pair absolute = pos.absolute();
        System.out.println(absolute.key() + absolute.value());
    }

    private void followInstructionBoat(String ins, boolean moveWaypoint) {
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
                        facing = facing.rotateCounterClockWise();
                    }
                } else {
                    for (int i = 0; i < times; i++) {
                        facing = facing.rotateClockWise();
                    }
                }
            }
            case 'N', 'S', 'E', 'W' -> {
                Direction to = Direction.getById(c);
                if (moveWaypoint) {
                    for (int i = 0; i < value; i++) {
                        facing = facing.add(to);
                    }
                } else {
                    for (int i = 0; i < value; i++) {
                        pos = pos.add(to);
                    }
                }
            }
        }
    }

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

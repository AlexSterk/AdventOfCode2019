package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Day {

    private List<Box> boxes;

    @Override
    public void processInput() {
        boxes = Arrays.stream(input.trim().split("\n")).map(Box::stringToBox).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        System.out.println(boxes.stream().mapToInt(Box::surfaceArea).sum());
    }

    @Override
    public void part2() {
        System.out.println(boxes.stream().mapToInt(Box::ribbonLength).sum());
    }

    @Override
    public int getDay() {
        return 2;
    }
}

record Box(int width, int height, int depth) {
    static Box stringToBox(String s) {
        String[] split = s.split("x");
        return new Box(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
        );
    }

    int surfaceArea() {
        List<Integer> areas = new ArrayList<>();
        
        areas.add(width * height);
        areas.add(width * depth);
        areas.add(depth * height);
        
        return areas.stream().mapToInt(a -> a*2).sum() + Collections.min(areas);
    }
    
    int ribbonLength() {
        List<Integer> sides = new ArrayList<>(List.of(width, height, depth));
        sides.sort(Integer::compare);
        
        return 2 * (sides.get(0) + sides.get(1)) + width * height * depth;
    }
}

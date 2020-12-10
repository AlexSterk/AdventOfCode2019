package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day10 extends Day {

    private List<Integer> numbers;

    @Override
    public void processInput() {
        numbers = Arrays.stream(input.split("\r?\n")).map(Integer::parseInt).sorted().collect(Collectors.toList());
    }

    @Override
    public void part1() {
//        int highest = Collections.max(numbers) + 3;

        Map<Integer, Integer> counts = new HashMap<>();

        int prev = 0;

        for (Integer number : numbers) {
            counts.merge(number - prev, 1, Integer::sum);
            prev = number;
        }

        System.out.println(counts);
        System.out.println(counts.get(1) * (counts.get(3) + 1));
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

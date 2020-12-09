package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 extends Day {

    private List<Long> numbers;
    private Long part1Solution;

    @Override
    public void processInput() {
        numbers = Arrays.stream(input.split("\r?\n")).map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        for (int i = 25; i < numbers.size(); i++) {
            long n = numbers.get(i);
            List<Long> previous = numbers.subList(i - 25, i);
            boolean found = false;
            for (Long first : previous) {
                long target = n - first;
                found = found || previous.contains(target);
            }
            if (!found) {
                System.out.println(n);
                part1Solution = n;
            }
        }
    }

    @Override
    public void part2() {
        List<Long> attempt = new ArrayList<>();
        o: for (int i = 0; i < numbers.size(); i++) {
            for (int j = i+1; j < numbers.size(); j++) {
                attempt = numbers.subList(i, j);
                long sum = attempt.stream().mapToLong(l -> l).sum();
                if (sum == part1Solution) {
                    break o;
                }
            }
        }

        long solution = Collections.min(attempt) + Collections.max(attempt);
        System.out.println(solution);
    }

    @Override
    public int getDay() {
        return 9;
    }
}

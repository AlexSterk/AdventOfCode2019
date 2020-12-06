package days;

import setup.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 extends Day {

    private List<List<String>> groups;

    @Override
    public void processInput() {
        groups = Arrays.stream(input.split("\r?\n\r?\n")).map(s -> Arrays.asList(s.split("\r?\n"))).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        int sum = 0;
        for (List<String> group : groups) {
            Set<Character> yes = new HashSet<>();
            group.forEach(s -> s.chars().forEach(i -> yes.add((char) i)));
            sum += yes.size();
        }
        System.out.println(sum);
    }

    @Override
    public void part2() {
        int sum = 0;
        for (List<String> group : groups) {
            Set<Character> yes = new HashSet<>();
            group.get(0).chars().forEach(i -> yes.add((char) i));
            group.forEach(s -> yes.retainAll(s.chars().mapToObj(i -> (char) i).collect(Collectors.toSet())));
            sum += yes.size();
        }
        System.out.println(sum);
    }

    @Override
    public int getDay() {
        return 6;
    }
}

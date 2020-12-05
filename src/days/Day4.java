package days;

import setup.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 extends Day {

    private List<String> lines;

    @Override
    public void processInput() {
        lines = Arrays.asList(input.trim().split("\n"));
    }

    @Override
    public void part1() {
        int c = 0;
        for (String line : lines) {
            String[] split = line.split("\\s+");
            HashSet<String> passphrases = new HashSet<>(Arrays.asList(split));
            if (passphrases.size() == split.length) c++;
        }
        System.out.println(c);
    }

    @Override
    public void part2() {
        int c = 0;
        for (String line : lines) {
            HashSet<List<Integer>> passphrases = new HashSet<>();
            String[] split = line.split("\\s+");
            for (String s : split) {
                passphrases.add(s.chars().sorted().boxed().collect(Collectors.toList()));
            }
            if (passphrases.size() == split.length) c++;
        }
        System.out.println(c);
    }

    @Override
    public int getDay() {
        return 4;
    }
}

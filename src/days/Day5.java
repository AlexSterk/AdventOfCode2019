package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 extends Day {

    public static final List<String> ALPHABET = IntStream.rangeClosed('a', 'z').boxed().map(Character::toString).collect(Collectors.toList());

    @Override
    public void processInput() {
        
    }

    @Override
    public void part1() {
        String polymer = input.trim();

        polymer = react(polymer);

        System.out.println(polymer.length());
    }

    private static String react(String polymer) {
        int oldLength;
        do {
            oldLength = polymer.length();
            for (String c : ALPHABET) {
                polymer = polymer.replaceAll(c + c.toUpperCase(), "");
                polymer = polymer.replaceAll(c.toUpperCase() + c, "");
            }
        } while (polymer.length() != oldLength);
        return polymer;
    }

    @Override
    public void part2() {
        List<Integer> sizes = new ArrayList<>();
        
        String polymer = input.trim();
        for (String c : ALPHABET) {
            System.out.println("TESTING " + c);
            String test = polymer.replaceAll(c + "|" + c.toUpperCase(), "");
            test = react(test);
            sizes.add(test.length());
        }
        System.out.println(Collections.min(sizes));
    }

    @Override
    public int getDay() {
        return 5;
    }
}

package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends Day {
    public List<Integer> numbers;

    @Override
    public void processInput() {
        numbers = Arrays.stream(input.trim().split("\r?\n")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                var a = numbers.get(i);
                var b = numbers.get(j);
                if (a + b == 2020) {
                    System.out.println(a * b);
                    return;
                }
            }
        }
    }

    @Override
    public void part2() {
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                for (int k = j + 1; k < numbers.size(); k++) {
                    var a = numbers.get(i);
                    var b = numbers.get(j);
                    var c = numbers.get(k);
                    if (a + b + c == 2020) {
                        System.out.println(a * b * c);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public int getDay() {
        return 1;
    }
}

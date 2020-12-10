package days;

import setup.Day;

import java.math.BigInteger;
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

//        System.out.println(counts);
        System.out.println(counts.get(1) * (counts.get(3) + 1));
    }

    @Override
    public void part2() {
        List<Integer> copy = new ArrayList<>(numbers);
        copy.add(0);
        copy.sort(Comparator.naturalOrder());
        long[] DP = new long[copy.size()];
        DP[0] = 1;

        for (int i = 1; i < copy.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (copy.get(i) - copy.get(j) <= 3) DP[i] += DP[j];
            }
        }

        System.out.println(DP[copy.size() - 1]);
    }
    
    static boolean validChain(List<Integer> chain) {
        int prev = 0;
        
        for (Integer number : chain) {
            if (number - prev > 3) return false;
            prev = number;
        }
        
        return true;
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

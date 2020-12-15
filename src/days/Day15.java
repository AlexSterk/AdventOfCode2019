package days;

import misc.Pair;
import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day15 extends Day {
    List<Integer> starting;
    
    @Override
    public void processInput() {
        starting = Arrays.stream(input.trim().split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        playGame(2020);
    }

    private void playGame(int goFor) {
        Map<Integer, Pair> spoken = new HashMap<>();

        for (int i = 0; i < starting.size(); i++) {
            spoken.put(starting.get(i), new Pair(null, i+1));
        }

        int last = starting.get(starting.size() - 1);

        for (int i = starting.size(); i < goFor; i++) {
            int num;
            Pair spoke = spoken.get(last);
            if (spoke.key() == null) {
                num = 0;
            } else {
                num = spoke.value() - spoke.key();
            }
            spoken.put(num, new Pair(spoken.getOrDefault(num, new Pair(null, (Integer) null)).value(), i + 1));
            last = num;
        }

        System.out.println(last);
    }

    @Override
    public void part2() {
        playGame(30000000);
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

package days;

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
        Map<Integer, List<Integer>> spoken = new HashMap<>();

        for (int i = 0; i < starting.size(); i++) {
            spoken.put(starting.get(i), new ArrayList<>(List.of(i+1)));
        }

        int last = starting.get(starting.size() - 1);

        for (int i = starting.size(); i < goFor; i++) {
            int num;
            if (spoken.get(last).size() == 1) {
                num = 0;
            } else {
                List<Integer> spoke = spoken.get(last);
                num = spoke.get(spoke.size() - 1) - spoke.get(spoke.size() - 2);
            }
            spoken.computeIfAbsent(num, x -> new ArrayList<>()).add(i+1);
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

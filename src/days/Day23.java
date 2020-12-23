package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Day {

    private List<Integer> startingCups;

    @Override
    public void processInput() {
        startingCups = Arrays.stream(input.trim().split("")).map(Integer::parseInt).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void part1() {

    }

    @Override
    public void part2() {

    }

    private void playGame(List<Integer> cups, int moves) {

    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

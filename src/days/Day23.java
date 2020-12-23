package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day23 extends Day {

    private List<Integer> startingCups;

    @Override
    public void processInput() {
        startingCups = Arrays.stream(input.trim().split("")).map(Integer::parseInt).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void part1() {
        var cups = new ArrayList<>(startingCups);

        int[] result = playGame(cups, 100);

        int i = 1;
        do {
            System.out.print(i = result[i]);
        } while (result[i] != 1);
        System.out.println();
    }

    @Override
    public void part2() {
        ArrayList<Integer> cups = new ArrayList<>(startingCups);

        int million = 1000000;
        IntStream.rangeClosed(Collections.max(cups) + 1, million)
            .forEach(cups::add);

        int[] res = playGame(cups, 10 * million);

        int first = res[1];
        int second = res[first];

        System.out.println(first * second);
    }

    private int[] playGame(List<Integer> startingCups, int moves) {
        Queue<Integer> cupsQueue = new ArrayDeque<>(startingCups);

        int[] cups = new int[startingCups.size() + 1];

        int first, current;
        first = current = cupsQueue.poll();
        while (!cupsQueue.isEmpty()) {
            cups[current] = cupsQueue.poll();
            current = cups[current];
        }
        cups[current] = first;

        current = first;
        for (int move = 1; move <= moves; move++) {
            int a,b,c;
            var removed = List.of(
                    a = cups[current],
                    b = cups[a],
                    c = cups[b]
            );

            int destination = current > 1 ? current - 1 : cups.length - 1;
            while (removed.contains(destination)) {
                destination = destination > 1 ? destination - 1 : cups.length - 1;
            }

            cups[current] = cups[c];
            cups[c] = cups[destination];
            cups[destination] = a;

            current = cups[current];
        }

        return cups;
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

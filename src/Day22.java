import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day22 extends Day {
    private List<Integer> p1Cards;
    private List<Integer> p2Cards;

    @Override
    public void processInput() {
        String[] split = input.trim().split("(\r?\n){2}");

        p1Cards = Arrays.stream(split[0].trim().split("\r?\n")).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        p2Cards = Arrays.stream(split[1].trim().split("\r?\n")).skip(1).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        Queue<Integer> playerOne = new ArrayDeque<>(p1Cards);
        Queue<Integer> playerTwo = new ArrayDeque<>(p2Cards);

        while (!playerOne.isEmpty() && !playerTwo.isEmpty()) {
            Integer one = playerOne.poll();
            Integer two = playerTwo.poll();

            if (one > two) {
                playerOne.offer(one);
                playerOne.offer(two);
            } else {
                playerTwo.offer(two);
                playerTwo.offer(one);
            }
        }

        var winner = new ArrayList<>(playerOne.isEmpty() ? playerTwo : playerOne);
        Collections.reverse(winner);

        System.out.println(IntStream.range(0, winner.size()).map(i -> (i + 1) * winner.get(i)).sum());
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 22;
    }
}

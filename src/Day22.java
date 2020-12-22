import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        announceWinnerScore(playerOne, playerTwo);
    }

    private void announceWinnerScore(Queue<Integer> playerOne, Queue<Integer> playerTwo) {
        var winner = new ArrayList<>(playerOne.isEmpty() ? playerTwo : playerOne);
        Collections.reverse(winner);
        System.out.println(IntStream.range(0, winner.size()).map(i -> (i + 1) * winner.get(i)).sum());
    }

    @Override
    public void part2() {
        recursiveGame(p1Cards, p2Cards, 0);
    }

    int recursiveGame(Collection<Integer> p1, Collection<Integer> p2, int level) {
        Queue<Integer> playerOne = new LinkedList<>(p1);
        Queue<Integer> playerTwo = new LinkedList<>(p2);

        List<Integer> p1Hashes = new ArrayList<>();
        List<Integer> p2Hashes = new ArrayList<>();

        while (!playerOne.isEmpty() && !playerTwo.isEmpty()) {
            if (p1Hashes.contains(playerOne.hashCode()) && p2Hashes.contains(playerTwo.hashCode())) {
                return 1;
            }
            p1Hashes.add(playerOne.hashCode());
            p2Hashes.add(playerTwo.hashCode());

            Integer one = playerOne.poll();
            Integer two = playerTwo.poll();

            int winner;

            if (playerOne.size() >= one && playerTwo.size() >= two) {
                winner = recursiveGame(
                        playerOne.stream().limit(one).collect(Collectors.toList()),
                        playerTwo.stream().limit(two).collect(Collectors.toList()),
                        level + 1);
            } else {
                winner = one > two  ? 1 : 2;
            }

            if (winner == 1) {
                playerOne.offer(one);
                playerOne.offer(two);
            } else {
                playerTwo.offer(two);
                playerTwo.offer(one);
            }
        }

        if (level == 0) {
            announceWinnerScore(playerOne, playerTwo);
        }

        return playerOne.size() > playerTwo.size() ? 1 : 2;
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

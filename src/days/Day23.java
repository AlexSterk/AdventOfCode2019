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
        List<Integer> cups = new CircularList<>(startingCups);
        playGame(cups, 100);

        int afterOne = cups.indexOf(1) + 1;
        for (int i = 0; i < cups.size() - 1; i++) {
            System.out.print(cups.get(afterOne++));
        }
        System.out.println();
    }

    private void playGame(List<Integer> cups, int moves) {
        int current = cups.get(0);

        for (int i = 0; i < moves; i++) {
            int cI = cups.indexOf(current);
            List<Integer> picked = List.of(
                    cups.get(cI + 1),
                    cups.get(cI + 2),
                    cups.get(cI + 3)
            );
            cups.removeAll(picked);
            int destination = cups.get(cups.indexOf(current));
            do {
                destination--;
                if (destination < Collections.min(cups)) destination = Collections.max(cups) + 1;
            } while (!cups.contains(destination));
            int insertPosition = cups.indexOf(destination);

            for (Integer p : picked) {
                cups.add(++insertPosition, p);
            }

            current = cups.get(cups.indexOf(current) + 1);
        }
    }

    @Override
    public void part2() {
//        List<Integer> cups = new CircularList<>(startingCups);
//
//        int million = 1000000;
//
//        for (int i = Collections.max(cups) + 1; i <= million; i++) {
//            cups.add(i);
//        }
//
//        playGame(cups, 10*million);
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

class CircularList<T> extends ArrayList<T> {
    public CircularList(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public T get(int index) {
        return super.get(index % size());
    }

    @Override
    public void add(int index, T element) {
        if (index - 1 == size() - 1) add(element);
        else super.add(index % size(), element);
    }

    @Override
    public boolean add(T t) {
        return super.add(t);
    }
}

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
        LinkedList<Integer> cups = new LinkedList<>(startingCups);
        int moves = 100;
        playGame(cups, moves);

        CircularListIterator<Integer> iterator = new CircularListIterator<>(cups);
        iterator.setToIndex(cups.indexOf(1) + 1);
        for (int i = 0; i < cups.size() - 1; i++) {
            System.out.print(iterator.next());
        }
        System.out.println();
    }

    @Override
    public void part2() {
        LinkedList<Integer> cups = new LinkedList<>(startingCups);
        int million = 1000000;
        IntStream.rangeClosed(Collections.max(cups), million)
            .forEach(cups::add);

        playGame(cups, million * 10);

        int index = cups.indexOf(1);

        CircularListIterator<Integer> it = new CircularListIterator<>(cups);
        it.setToIndex((index + 1) % cups.size());

        System.out.println(it.next() * it.next());
    }

    private void playGame(LinkedList<Integer> cups, int moves) {
        CircularListIterator<Integer> cupsClockwise = new CircularListIterator<>(cups);
        Integer current = cupsClockwise.next();

        for (int move = 1; move <= moves; move++) {
            System.out.println(move);

//            System.out.println("Move: " + i);
//            System.out.println("Cups: " + cups);
//            System.out.println("Current: " + current);

            List<Integer> picked = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                picked.add(cupsClockwise.next());
                cupsClockwise.remove();
            }

//            System.out.println("Picked: " + picked);

            int min = 1;
            while (picked.contains(min)) min++;
            int max = cups.size() + 3;
            while (picked.contains(max)) max--;

            int destination = current;
            do {
                destination--;
                if (destination < min) destination = max;
            } while (picked.contains(destination));

//            System.out.println("Destination: " + destination);

            cupsClockwise.setToIndex((cups.indexOf(destination) + 1) % cups.size());
            picked.forEach(cupsClockwise::add);
            cupsClockwise.setToIndex((cups.indexOf(current) + 1) % cups.size());
            current = cupsClockwise.next();
//            System.out.println();
        }
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

class CircularListIterator<T> implements ListIterator<T> {
    private final LinkedList<T> _list;
    private ListIterator<T> _iterator;

    CircularListIterator(LinkedList<T> list) {
        _list = list;
        _iterator = list.listIterator();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (!_iterator.hasNext()) {
            _iterator = _list.listIterator();
        }

        return _iterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return true;
    }

    @Override
    public T previous() {
        if (!_iterator.hasPrevious()) {
            _iterator = _list.listIterator(_list.size() - 1);
        }

        return _iterator.previous();
    }

    @Override
    public int nextIndex() {
        return _iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return _iterator.previousIndex();
    }

    @Override
    public void remove() {
        _iterator.remove();
    }

    @Override
    public void set(T t) {
        _iterator.set(t);
    }

    @Override
    public void add(T t) {
        _iterator.add(t);
    }

    public void setToIndex(int index) {
        _iterator = _list.listIterator(index);
    }
}

package days;

import setup.Day;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 extends Day {

    public static final int N = 20201227;
    private List<Long> publicKeys;

    @Override
    public void processInput() {
        publicKeys = Arrays.stream(input.trim().split("\r?\n")).map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        var doorKey = publicKeys.get(0);
        var cardKey = publicKeys.get(1);

        System.out.println(transform(doorKey, getLoopSize(cardKey)));
    }

    long transform(long subjectNumber, int loopSize) {
        long value = 1;
        for (int i = 0; i < loopSize; i++) {
            value *= subjectNumber;
            value %= N;
        }
        return value;
    }

    int getLoopSize(long key) {
        long value = 1;
        int loopSize = 0;
        while (value != key) {
            value *= 7;
            value %= N;
            loopSize++;
        }

        return loopSize;
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

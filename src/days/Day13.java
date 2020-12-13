package days;

import misc.Pair;
import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 extends Day {
    int earliest;
    List<Integer> times;

    @Override
    public void processInput() {
        String[] split = input.trim().split("\r?\n");
        earliest = Integer.parseInt(split[0]);
        times = Arrays.stream(split[1].split(",")).map(s1 -> s1.equals("x") ? null : Integer.parseInt(s1)).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        Pair min = times.stream().filter(Objects::nonNull).map(i -> new Pair(i, (int) getLowestWaitingTime(i, earliest))).min(Comparator.comparing(Pair::value)).get();
        System.out.println(min.key() * min.value());
    }


    @Override
    public void part2() {
        long product = IntStream.range(0, times.size()).filter(i -> times.get(i) != null).mapToLong(times::get).reduce((a, b) -> a * b).getAsLong();
        long sum = IntStream.range(0, times.size()).filter(i -> times.get(i) != null).mapToLong(i -> i * (product / times.get(i) * inverseModulo(product / times.get(i), times.get(i)))).sum();

        System.out.println(product - sum % product);
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    long getLowestWaitingTime(int i, long t) {
        long div = t / i * i;
        if (div < t) div += i;

        return div - t;
    }

    long inverseModulo(long x, long y) {
        if (x != 0) {
            long modulo = y % x;
            return modulo == 0 ? 1 : y - inverseModulo(modulo, x) * y / x;
        }
        return 0;
    }
}

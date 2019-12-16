import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {
    public static void main(String[] args) throws IOException {
        test();
        partOne();
//        partTwo();
    }

    private static void test() throws IOException {
        int rounds = 100;
        String input = Files.readString(Paths.get("AoC/resources/day16-test.txt"));
        List<Pair> out = runFFT(rounds, input);

        System.out.println(out.stream().map(p -> Long.toString(p.value)).collect(Collectors.joining("")));
    }

    private static List<Pair> runFFT(int rounds, String input) {
        List<Pair> in = IntStream.range(0, input.length()).mapToObj(i -> new Pair(i, Character.getNumericValue(input.charAt(i)))).collect(Collectors.toList());
        final List<Integer> pattern = Collections.unmodifiableList(List.of(0, 1, 0, -1));

        for (int i = 0; i < rounds; i++) {
            in = getNewInput(in, pattern);
        }
        return in;
    }

    private static List<Pair> getNewInput(List<Pair> in, final List<Integer> originalPattern) {
        List<Pair> out = new ArrayList<>();

        return IntStream.range(0, in.size()).mapToObj(i -> {
            List<Integer> pattern = getNewPattern(originalPattern, i);
            return getNthPair(in, i, pattern);
        }).collect(Collectors.toList());
//
//
//        for (int i = 0; i < in.size(); i++) {
//            pattern = getNewPattern(originalPattern, i);
//            Pair nthPair = getNthPair(in, i, pattern);
//            out.add(nthPair);
//        }
//        return out;
    }

    private static LinkedList<Integer> getNewPattern(List<Integer> pattern, int i) {
        LinkedList<Integer> nPattern = new LinkedList<>();
        for (Integer integer : pattern) {
            for (int j = 0; j < (i+1); j++) {
                nPattern.add(integer);
            }
        }
        nPattern.add(nPattern.remove());
        return nPattern;
    }

    private static Pair getNthPair(List<Pair> in, int finalI, List<Integer> pattern) {
        Pair nthPair = in.stream().filter(p -> pattern.get(p.index % pattern.size()) != 0).map(p -> p.applyPattern(pattern)).reduce((pair, pair2) -> new Pair(finalI, pair.value + pair2.value)).get();
        nthPair = new Pair(nthPair.index, Math.abs(nthPair.value) % 10);
        return nthPair;
    }

    private static void partOne() throws IOException {
        int rounds = 100;
        String input = Files.readString(Paths.get("AoC/resources/day16.txt"));
        List<Pair> out = runFFT(rounds, input);

        System.out.println(out.stream().map(p -> Long.toString(p.value)).collect(Collectors.joining("")));
    }

    private static void partTwo() throws IOException {
        int rounds = 1;
        String input = Files.readString(Paths.get("AoC/resources/day16.txt")).repeat(10000);
        List<Pair> out = runFFT(rounds, input);

        System.out.println(out.stream().map(p -> Long.toString(p.value)).collect(Collectors.joining("")));
    }

    @AllArgsConstructor
    @ToString
    public static class Pair {
        public final int index;
        public final int value;

        public Pair applyPattern(List<Integer> pattern) {
            int i = index % pattern.size();
            int n = value * pattern.get(i);

            return new Pair(index, n);
        }
    }
}

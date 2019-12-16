import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {
    public static void main(String[] args) throws IOException {
        test();
//        partOne();
    }

    private static void test() throws IOException {
        String input = Files.readString(Paths.get("AoC/resources/day16-test.txt"));
        List<Pair> in = IntStream.range(0, input.length()).mapToObj(i -> new Pair(i, Character.getNumericValue(input.charAt(i)))).collect(Collectors.toList());
        List<Integer> pattern = List.of(0, 1, 0, -1);
        List<Pair> out = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            int finalI = i;
            List<Integer> finalPattern = pattern;
            Pair nthPair = in.stream().map(p -> p.applyPattern(finalPattern)).reduce((pair, pair2) -> new Pair(finalI, pair.value + pair2.value)).get();
            String value = Integer.toString(nthPair.value);
            nthPair = new Pair(nthPair.index, Character.getNumericValue(value.charAt(value.length() - 1)));
            out.add(nthPair);

            LinkedList<Integer> nPattern = new LinkedList<>();
            for (Integer integer : pattern) {
                for (int j = 0; j < (i+1); j++) {
                    nPattern.add(integer);
                }
            }
            nPattern.remove();
            pattern = nPattern;
        }
        System.out.println(out);
    }

    private static void partOne() throws IOException {
        String input = Files.readString(Paths.get("AoC/resources/day16.txt"));
        List<Integer> in = input.chars().mapToObj(Character::getNumericValue).collect(Collectors.toList());
    }

    @AllArgsConstructor
    public static class Pair {
        public final int index, value;

        public Pair applyPattern(List<Integer> pattern) {
            int i = index % pattern.size();
            int n = value * pattern.get(i);

            return new Pair(index, n);
        }

    }
}

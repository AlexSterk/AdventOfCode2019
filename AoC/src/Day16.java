import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day16 {
    public static void main(String[] args) throws IOException {
        test();
//        partOne();
        partTwo();
    }

    private static void test() throws IOException {
        int rounds = 100;
        String input = Files.readString(Paths.get("AoC/resources/day16-test.txt")).repeat(10000);
        List<Integer> fullInput = input.chars().mapToObj(Character::getNumericValue).collect(Collectors.toList());
        int offset = Integer.parseInt(fullInput.subList(0, 7).stream().map(Object::toString).collect(Collectors.joining()));
        System.out.println(offset);

        assert fullInput.size() < 2 * offset - 1;


        List<Integer> in = new ArrayList<>(fullInput);
        for (int i = 0; i < rounds; i++) {
            int s = in.stream().skip(offset).mapToInt(j -> j).sum();
            List<Integer> out = new ArrayList<>(Collections.nCopies(offset, 0));
            out.add(s % 10);
            for (int j = offset + 2; j < fullInput.size() + 1; j++) {
                s -= in.get(j - 2);
                out.add(s % 10);
            }
            in = out;
        }
        System.out.println(in.subList(offset, offset + 8));
    }

    private static List<Integer> runFFT(int rounds, List<Integer> in) {
        final List<Integer> pattern = Collections.unmodifiableList(List.of(0, 1, 0, -1));

        for (int i = 0; i < rounds; i++) {
            in = getNewInput(in, pattern);
        }
        return in;
    }

    private static List<Integer> getNewInput(List<Integer> in, final List<Integer> originalPattern) {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            List<Integer> pattern = getNewPattern(originalPattern, i);
            out.add(getNthValue(in, pattern));
        }
        return out;
    }

    private static Integer getNthValue(List<Integer> in, List<Integer> pattern) {
        int c = 0;
        for (int i = 0; i < in.size(); i++) {
            c += in.get(i) * pattern.get(i % pattern.size());
        }
        return Math.abs(c) % 10;
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
        List<Integer> out = runFFT(rounds, input.chars().mapToObj(Character::getNumericValue).collect(Collectors.toList()));

        System.out.println(out.stream().map(Objects::toString).collect(Collectors.joining("")));
    }

    private static void partTwo() throws IOException {
        int rounds = 100;
        String input = Files.readString(Paths.get("AoC/resources/day16.txt")).repeat(10000);
        List<Integer> fullInput = input.chars().mapToObj(Character::getNumericValue).collect(Collectors.toList());
        int offset = Integer.parseInt(fullInput.subList(0, 7).stream().map(Object::toString).collect(Collectors.joining()));
        System.out.println(offset);

        assert fullInput.size() < 2 * offset - 1;


        List<Integer> in = new ArrayList<>(fullInput);
        for (int i = 0; i < rounds; i++) {
            int s = in.stream().skip(offset).mapToInt(j -> j).sum();
            List<Integer> out = new ArrayList<>(Collections.nCopies(offset, 0));
            out.add(s % 10);
            for (int j = offset + 2; j < fullInput.size() + 1; j++) {
                s -= in.get(j - 2);
                out.add(s % 10);
            }
            in = out;
        }
        System.out.println(in.subList(offset, offset + 8).stream().map(Object::toString).collect(Collectors.joining()));
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

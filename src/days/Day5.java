package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 extends Day {

    private List<String> strings;

    @Override
    public void processInput() {
        strings = Arrays.asList(input.trim().split("\n"));
    }

    @Override
    public void part1() {
        List<String> strings = new ArrayList<>(this.strings);
        strings.removeIf(Predicate.not(Day5::hasThreeVowels));
        strings.removeIf(Predicate.not(Day5::hasDoubleLetters));
        strings.removeIf(Day5::hasBigrams);
        System.out.println(strings.size());
    }

    @Override
    public void part2() {
        List<String> strings = new ArrayList<>(this.strings);
        strings.removeIf(Predicate.not(Day5::hasDoubleBigram));
        strings.removeIf(Predicate.not(Day5::hasInterruptedDouble));
        System.out.println(strings.size());
    }

    static final List<Character> vowels = List.of('a', 'e', 'i', 'o', 'u');
    static final List<String> alphabet = IntStream.rangeClosed('a', 'z').mapToObj(Character::toString).collect(Collectors.toUnmodifiableList());
    static final List<String> bigrams = List.of("ab", "cd", "pq", "xy");

    static boolean hasThreeVowels(String s) {
        return s.chars().mapToObj(i -> (char) i).filter(vowels::contains).count() >= 3;
    }

    static boolean hasDoubleLetters(String s) {
        for (String c : alphabet) {
            if (s.contains(c + c)) return true;
        }
        return false;
    }

    static boolean hasBigrams(String s) {
        for (String bigram : bigrams) {
            if (s.contains(bigram)) return true;
        }
        return false;
    }
    
    static boolean hasDoubleBigram(String s) {
        List<String> bigrams = new ArrayList<>();
        for (int i = 0; i < s.length()-1; i++) {
            String bigram = s.substring(i, i+2);
            if (bigrams.contains(bigram)) {
                int index = bigrams.indexOf(bigram);
                if (i - index > 1) return true;
            }
            bigrams.add(bigram);
        }
        return false;
    }
    
    static boolean hasInterruptedDouble(String s) {
        for (int i = 0; i < s.length() - 2; i++) {
            if (s.charAt(i) == s.charAt(i+2)) return true;
        }
        return false;
    }

    @Override
    public int getDay() {
        return 5;
    }
}

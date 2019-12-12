import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day4 {
    public static void main(String[] args) {
        System.out.println(IntStream.rangeClosed(171309, 643603).filter(Day4::meetsCriteria).count());
    }

    private static boolean meetsCriteria(int i) {
        String s = String.valueOf(i);
        Map<Character, List<Integer>> charIndexes = new HashMap<>();

        char[] numbers = s.toCharArray();
        char prevChar = 0;
        for (int j = 0; j < numbers.length; j++) {
            char number = numbers[j];

            if (number < prevChar) return false;

            if (!charIndexes.containsKey(number)) {
                charIndexes.put(number, new ArrayList<>());
            }
            charIndexes.get(number).add(j);
            prevChar = number;
        }

        charIndexes.values().removeIf(l -> l.size() != 2);

        return !charIndexes.isEmpty();
    }
}

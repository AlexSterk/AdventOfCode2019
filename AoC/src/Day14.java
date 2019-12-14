import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 {
    public static class Reaction {
        public final List<ReactionPair> input;
        public final ReactionPair output;

        public Reaction(List<ReactionPair> input, ReactionPair output) {
            this.input = input;
            this.output = output;
        }

        public Reaction(String s) {
            String[] split = s.split(" => ");
            String left = split[0], right = split[1];
            String[] pairs = left.split(", ");

            this.input = Stream.of(pairs).map(ReactionPair::new).collect(Collectors.toList());
            this.output = new ReactionPair(right);
        }

        public Reaction scale(int factor) {
            return new Reaction(input.stream().map(r -> r.scale(factor)).collect(Collectors.toList()), output.scale(factor));
        }

        public boolean requires(String chemical) {
            for (ReactionPair reactionPair : input) {
                if (reactionPair.chemical.equals(chemical)) return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return input + " => " + output;
        }
    }
    public static class ReactionPair {
        public final int quantity;
        public final String chemical;

        public ReactionPair(int quantity, String chemical) {
            this.quantity = quantity;
            this.chemical = chemical;
        }

        public ReactionPair(String pair) {
            String[] s = pair.split(" ");

            this.quantity = Integer.parseInt(s[0]);
            this.chemical = s[1];
        }

        public ReactionPair scale(int factor) {
            return new ReactionPair(quantity * factor, chemical);
        }

        @Override
        public String toString() {
            return quantity + " " + chemical;
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Reaction> keyProducedByBalue = Files.lines(Paths.get("AoC/resources/day14.txt")).map(Reaction::new).collect(Collectors.toMap(r -> r.output.chemical, r -> r));

        int ore = getOre(keyProducedByBalue);

        System.out.println(ore);
    }

    private static int getOre(Map<String, Reaction> keyProducedByBalue) {
        LinkedList<ReactionPair> needed = new LinkedList<>();
        needed.add(new ReactionPair(1, "FUEL"));
        Map<String, Integer> surplus = new HashMap<>();

        int ore = 0;

        while (!needed.isEmpty()) {
            ReactionPair pair = needed.poll();

            Map<String, Integer> ingredientsNeeded = getNeeded(keyProducedByBalue, pair, surplus);

            ore += ingredientsNeeded.getOrDefault("ORE", 0);

            ingredientsNeeded.forEach((s, integer) -> {
                for (int i = 0; i < needed.size(); i++) {
                    ReactionPair pair1 = needed.get(i);
                    if (pair1.chemical.equals(s)) {
                        needed.set(i, new ReactionPair(pair1.quantity + integer, s));
                        return;
                    }
                }
                needed.offer(new ReactionPair(integer, s));
            });
        }
        return ore;
    }

    private static Map<String, Integer> getNeeded(Map<String, Reaction> keyProducedByBalue, ReactionPair pair, Map<String, Integer> surplus) {
        Reaction reaction = keyProducedByBalue.get(pair.chemical);
        if (reaction == null) return Collections.emptyMap();
        Integer factor = (pair.quantity + reaction.output.quantity - 1) / reaction.output.quantity;

        Integer surpl = factor * reaction.output.quantity - pair.quantity;
        surplus.computeIfPresent(pair.chemical, (a, b) -> b + surpl);
        surplus.putIfAbsent(pair.chemical, surpl);

        Map<String, Integer> needed = new HashMap<>();
        for (ReactionPair ingredient : reaction.input) {
            int totalNeeded = ingredient.quantity * factor;
            int leftover = Math.min(totalNeeded, surplus.getOrDefault(ingredient.chemical, 0));
            totalNeeded -= leftover;
            surplus.computeIfPresent(ingredient.chemical, (c, n) -> n - leftover);
            needed.put(ingredient.chemical, totalNeeded);
        }
        return needed;
    }
}

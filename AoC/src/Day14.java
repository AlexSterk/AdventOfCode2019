import java.io.IOException;
import java.math.BigInteger;
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

        @Override
        public String toString() {
            return input + " => " + output;
        }
    }
    public static class ReactionPair {
        public final BigInteger quantity;
        public final String chemical;

        public ReactionPair(BigInteger quantity, String chemical) {
            this.quantity = quantity;
            this.chemical = chemical;
        }

        public ReactionPair(String pair) {
            String[] s = pair.split(" ");

            this.quantity = new BigInteger(s[0]);
            this.chemical = s[1];
        }

        @Override
        public String toString() {
            return quantity + " " + chemical;
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Reaction> keyProducedByBalue = Files.lines(Paths.get("AoC/resources/day14.txt")).map(Reaction::new).collect(Collectors.toMap(r -> r.output.chemical, r -> r));

        BigInteger ore = getOre(keyProducedByBalue, BigInteger.ONE);

        System.out.println(ore);

        BigInteger trillion = new BigInteger("1000000000000");
        BigInteger lowerBound = BigInteger.ZERO;
        BigInteger upperBound = trillion;

        BigInteger toTry = null;
        while (upperBound.subtract(lowerBound).compareTo(BigInteger.ONE) == 1) {
            toTry = lowerBound.add(upperBound.subtract(lowerBound).divide(BigInteger.TWO));
            BigInteger oreNeeded = getOre(keyProducedByBalue, toTry);
            int compare = oreNeeded.compareTo(trillion);
            if (compare == 1) upperBound = toTry;
            if (compare == -1) lowerBound = toTry;
        }
        System.out.println(lowerBound);
    }

    private static BigInteger getOre(Map<String, Reaction> keyProducedByBalue, BigInteger qt) {
        LinkedList<ReactionPair> needed = new LinkedList<>();
        needed.add(new ReactionPair(qt, "FUEL"));
        Map<String, BigInteger> surplus = new HashMap<>();

        BigInteger ore = BigInteger.ZERO;

        while (!needed.isEmpty()) {
            ReactionPair pair = needed.poll();

            Map<String, BigInteger> ingredientsNeeded = getNeeded(keyProducedByBalue, pair, surplus);

            ore = ore.add(ingredientsNeeded.getOrDefault("ORE", BigInteger.ZERO));

            ingredientsNeeded.forEach((s, integer) -> {
                for (int i = 0; i < needed.size(); i++) {
                    ReactionPair pair1 = needed.get(i);
                    if (pair1.chemical.equals(s)) {
                        needed.set(i, new ReactionPair(pair1.quantity.add(integer), s));
                        return;
                    }
                }
                needed.offer(new ReactionPair(integer, s));
            });
        }
        return ore;
    }

    private static Map<String, BigInteger> getNeeded(Map<String, Reaction> keyProducedByBalue, ReactionPair pair, Map<String, BigInteger> surplus) {
        Reaction reaction = keyProducedByBalue.get(pair.chemical);
        if (reaction == null) return Collections.emptyMap();
        BigInteger factor = pair.quantity.add(reaction.output.quantity).subtract(BigInteger.ONE).divide(reaction.output.quantity);
//        = (pair.quantity + reaction.output.quantity - 1) / reaction.output.quantity;

        BigInteger surpl = factor.multiply(reaction.output.quantity).subtract(pair.quantity);
//        = factor * reaction.output.quantity - pair.quantity;
        surplus.computeIfPresent(pair.chemical, (a, b) -> b.add(surpl));
        surplus.putIfAbsent(pair.chemical, surpl);

        Map<String, BigInteger> needed = new HashMap<>();
        for (ReactionPair ingredient : reaction.input) {
            BigInteger totalNeeded = ingredient.quantity.multiply(factor);
            BigInteger leftover = totalNeeded.min(surplus.getOrDefault(ingredient.chemical, BigInteger.ZERO));
            totalNeeded = totalNeeded.subtract(leftover);
            surplus.computeIfPresent(ingredient.chemical, (c, n) -> n.subtract(leftover));
            needed.put(ingredient.chemical, totalNeeded);
        }
        return needed;
    }
}

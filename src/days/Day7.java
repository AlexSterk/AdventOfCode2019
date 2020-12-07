package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 extends Day {

    private List<Bag> bags;

    @Override
    public void processInput() {
        bags = Arrays.stream(input.split("\r?\n")).map(Bag::stringToBag).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        Bag shinyGold = Bag.getBag("shiny gold");
        System.out.println(bags.stream().filter(b -> ultimatelyContains(b, shinyGold)).count());
    }

    @Override
    public void part2() {
        Bag shinyGold = Bag.getBag("shiny gold");
        System.out.println(bagsRequired(shinyGold));
    }

    @Override
    public int getDay() {
        return 7;
    }
    
    static int bagsRequired(Bag bag) {
        return bag.bags().entrySet().stream().mapToInt(e -> (bagsRequired(e.getKey()) + 1) * e.getValue()).sum();
    }
    
    static boolean ultimatelyContains(Bag hayStack, Bag needle) {        
        for (Bag bag : hayStack.bags().keySet()) {
            if (bag == needle) return true;
            if (ultimatelyContains(bag, needle)) return true;
        }
        return false;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

record Bag(String color, Map<Bag, Integer> bags) {
    static final Pattern BAG_PATTERN = Pattern.compile("(.+) bags contain (.+)\\.");
    private static final Map<String, Bag> BAGS = new HashMap<>();
    
    Bag(String color) {
        this(color, new HashMap<>());
    }
    
    static Bag getBag(String color) {
        return BAGS.computeIfAbsent(color, Bag::new);
    }

    static Bag stringToBag(String s) {
        Matcher matcher = BAG_PATTERN.matcher(s);
        matcher.matches();

        String color = matcher.group(1);
        Bag bag = getBag(color);
        String contains = matcher.group(2);

        String[] containsArray = contains.split(", ");
        for (String bagDefinition : containsArray) {
            if (bagDefinition.startsWith("no")) break;
            String[] defs = bagDefinition.split(" ");
            int amount = Integer.parseInt(defs[0]);
            String c = defs[1] + " " + defs[2];
            bag.bags().put(Bag.getBag(c), amount);
        }
        return bag;
    }

    @Override
    public String toString() {
        return "Bag{" +
                "color='" + color + '\'' +
                ", bags=[" + bags.entrySet().stream().map(e -> e.getValue() + " " + e.getKey().color()).collect(Collectors.joining(", ")) +
                "]}";
    }
}

package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 extends Day {
    
    private Map<Set<String>, Set<String>> foodsAndAllergens;
    private Map<String, Set<String>> possible;

    @Override
    public void processInput() {
        foodsAndAllergens = new HashMap<>();

        for (String s : input.trim().split("\r?\n")) {
            int i = s.indexOf("(");
            String ingredients = s.substring(0, i);
            String allergens = s.substring(i);
            
            allergens = allergens.replaceFirst("\\(contains (.+)\\)", "$1");

//            System.out.println(ingredients);
//            System.out.println(allergens);
            
            foodsAndAllergens.put(Set.of(ingredients.split(" ")), Set.of(allergens.split(", ")));
        }
    }

    @Override
    public void part1() {
        Set<String> allAllergens = foodsAndAllergens.values().stream().reduce(new HashSet<>(), (acc, inc) -> {
            acc.addAll(inc);
            return acc;
        });

        possible = new HashMap<>();

        for (String allergen : allAllergens) {
            foodsAndAllergens.forEach((ingredients, allergens) -> {
                if (allergens.contains(allergen)) {
                    possible.merge(allergen, new HashSet<>(ingredients), (curr, inc) -> {
                        curr.retainAll(inc);
                        return curr;
                    });
                }
            });
        }

        Set<String> allIngredients = foodsAndAllergens.keySet().stream().reduce(new HashSet<>(), (acc, inc) -> {
            acc.addAll(inc);
            return acc;
        });

        Set<String> allergenIngredients = possible.values().stream().reduce(new HashSet<>(), (acc, inc) -> {
            acc.addAll(inc);
            return acc;
        });

        Set<String> definitelyNotAllergens = allIngredients.stream().filter(ing -> !allergenIngredients.contains(ing)).collect(Collectors.toSet());
        
        int sum = 0;

        for (String ingredient : definitelyNotAllergens) {
            for (Set<String> food : foodsAndAllergens.keySet()) {
                if (food.contains(ingredient)) sum++;
            }
        }

        System.out.println(sum);
    }

    @Override
    public void part2() {
        
    }

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

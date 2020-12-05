package days;

import setup.Day;

import java.util.*;

public class Day3 extends Day {
    String directions;
    
    @Override
    public void processInput() {
        directions = input.trim();
    }

    @Override
    public void part1() {
        Pair pos = new Pair(0,0);
        Map<Pair, Integer> delivered = new HashMap<>();

        Iterator<Character> iterator = directions.chars().mapToObj(i -> (char) i).iterator();

        do {
            delivered.merge(pos, 1, Integer::sum);
            pos = move(pos, iterator.next());
        } while (iterator.hasNext());

        System.out.println(delivered.size());
    }

    @Override
    public void part2() {
        Map<Pair, Integer> delivered = new HashMap<>();
        List<Pair> directions = new ArrayList<>();


        char[] chars = this.directions.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            directions.add(new Pair(i, chars[i]));
        }

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            var iterator = directions.stream().filter(p -> p.x() % 2 == finalI).iterator();

            Pair pos = new Pair(0,0);
            
            do {
                delivered.merge(pos, 1, Integer::sum);
                pos = move(pos, iterator.next().y());
            } while (iterator.hasNext());
        }
        
        System.out.println(delivered.size());
    }

    private static Pair move(Pair pos, int y) {
        pos = switch (y) {
            case '^' -> new Pair(pos.x(), pos.y() - 1);
            case '>' -> new Pair(pos.x() + 1, pos.y());
            case 'v' -> new Pair(pos.x(), pos.y() + 1);
            case '<' -> new Pair(pos.x() - 1, pos.y());
            default -> throw new RuntimeException();
        };
        return pos;
    }

    @Override
    public int getDay() {
        return 3;
    }
}

record Pair(int x, int y) {
    
}

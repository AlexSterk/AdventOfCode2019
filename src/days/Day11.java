package days;

import misc.KVPair;
import setup.Day;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static days.Seat.*;

public class Day11 extends Day {
    
    private Map<KVPair<Integer, Integer>, Seat> seatsMap;
    private int width, height;

    @Override
    public void processInput() {
        seatsMap = new HashMap<>();

        String[] split = input.split("\r?\n");
        height = split.length;
        width = split[0].toCharArray().length;
        for (int y = 0; y < height; y++) {
            var l = split[y].toCharArray();
            for (int x = 0; x < width; x++) {
                seatsMap.put(new KVPair<>(x, y), switch (l[x]) {
                    case '.' -> FLOOR;
                    case 'L' -> EMPTY;
                    case '#' -> OCCUPIED;
                    default -> throw new RuntimeException();
                });
            }
        }
    }

    @Override
    public void part1() {

        while (true) {
            Map<KVPair<Integer, Integer>, Seat> previousRound = new HashMap<>(seatsMap);
            previousRound.entrySet().stream().forEach(e -> seatsMap.put(e.getKey(), getNewSeat(e.getKey(), e.getValue(), previousRound)));
            
            if (seatsMap.equals(previousRound)) {
                long occupied = seatsMap.values().stream().filter(s -> s == OCCUPIED).count();
                System.out.println(occupied);
                return;
            }
        }
    }
    
    static Seat getNewSeat(KVPair<Integer, Integer> position, Seat currentValue, Map<KVPair<Integer, Integer>, Seat> seatsMap) {
        if (currentValue == EMPTY && getAdjacentOccupation(position, seatsMap) == 0) return OCCUPIED;
        if (currentValue == OCCUPIED && getAdjacentOccupation(position, seatsMap) >= 4) return EMPTY;
        return currentValue;
    }
    
    static int getAdjacentOccupation(KVPair<Integer, Integer> position, Map<KVPair<Integer, Integer>, Seat> seatsMap) {
        return (int) Stream.of(
                new KVPair<>(position.key() - 1, position.value() - 1),
                new KVPair<>(position.key(), position.value() - 1),
                new KVPair<>(position.key() + 1, position.value() - 1),
                new KVPair<>(position.key() - 1, position.value()),
                new KVPair<>(position.key() + 1, position.value()),
                new KVPair<>(position.key() - 1, position.value() + 1),
                new KVPair<>(position.key(), position.value() + 1),
                new KVPair<>(position.key() + 1, position.value() + 1)
        ).map(k -> seatsMap.getOrDefault(k, FLOOR)).filter(s -> s == OCCUPIED).count();
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }
    
    void printSeats(Map<KVPair<Integer, Integer>, Seat> seatsMap) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                KVPair<Integer, Integer> pos = new KVPair<>(x, y);
                System.out.print(seatsMap.get(pos));
            }
            System.out.println();
        }
    }
}

enum Seat {
    EMPTY('L'),
    OCCUPIED('#'),
    FLOOR('.');

    private Character c;

    Seat(char c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return c.toString();
    }
}

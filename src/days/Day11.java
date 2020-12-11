package days;

import misc.KVPair;
import setup.Day;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static days.Seat.*;
import static misc.KVPair.*;

public class Day11 extends Day {
    
    private Map<Pair, Seat> seatsMap;
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
                seatsMap.put(new Pair(x, y), switch (l[x]) {
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
        var seatsMap = new HashMap<>(this.seatsMap);
        
        while (true) {
            Map<Pair, Seat> previousRound = new HashMap<>(seatsMap);
            previousRound.entrySet().parallelStream().forEach(e -> seatsMap.put(e.getKey(), getNewSeatPart1(e.getKey(), e.getValue(), previousRound)));
            
            if (seatsMap.equals(previousRound)) {
                long occupied = seatsMap.values().stream().filter(s -> s == OCCUPIED).count();
                System.out.println(occupied);
                return;
            }
        }
    }

    @Override
    public void part2() {
        var seatsMap = new HashMap<>(this.seatsMap);
        while (true) {
            Map<Pair, Seat> previousRound = new HashMap<>(seatsMap);
            previousRound.entrySet().parallelStream().forEach(e -> seatsMap.put(e.getKey(), getNewSeatPart2(e.getKey(), e.getValue(), previousRound)));

            if (seatsMap.equals(previousRound)) {
                long occupied = seatsMap.values().stream().filter(s -> s == OCCUPIED).count();
                System.out.println(occupied);
                return;
            }
        }
    }

    static Seat getNewSeatPart1(Pair position, Seat currentValue, Map<Pair, Seat> seatsMap) {
        if (currentValue == EMPTY && getAdjacentOccupation(position, seatsMap) == 0) return OCCUPIED;
        if (currentValue == OCCUPIED && getAdjacentOccupation(position, seatsMap) >= 4) return EMPTY;
        return currentValue;
    }

    static Seat getNewSeatPart2(Pair position, Seat currentValue, Map<Pair, Seat> seatsMap) {
        if (currentValue == EMPTY && getVisibleOccupation(position, seatsMap) == 0) return OCCUPIED;
        if (currentValue == OCCUPIED && getVisibleOccupation(position, seatsMap) >= 5) return EMPTY;
        return currentValue;
    }

    static int getAdjacentOccupation(Pair position, Map<Pair, Seat> seatsMap) {
        return (int) Stream.of(
                new Pair(position.key() - 1, position.value() - 1),
                new Pair(position.key(), position.value() - 1),
                new Pair(position.key() + 1, position.value() - 1),
                new Pair(position.key() - 1, position.value()),
                new Pair(position.key() + 1, position.value()),
                new Pair(position.key() - 1, position.value() + 1),
                new Pair(position.key(), position.value() + 1),
                new Pair(position.key() + 1, position.value() + 1)
        ).map(k -> seatsMap.getOrDefault(k, FLOOR)).filter(s -> s == OCCUPIED).count();
    }

    static int getVisibleOccupation(Pair position, Map<Pair, Seat> seatsMap) {
        return (int) Stream.of(
                new KVPair<>(position, new Pair(-1, -1)),
                new KVPair<>(position, new Pair(0, -1)),
                new KVPair<>(position, new Pair(1, -1)),
                new KVPair<>(position, new Pair(-1, 0)),
                new KVPair<>(position, new Pair(1, 0)),
                new KVPair<>(position, new Pair(-1, 1)),
                new KVPair<>(position, new Pair(0, 1)),
                new KVPair<>(position, new Pair(1, 1))
        ).map(pp -> nearestOccupationInDirection(pp.key(), pp.value(), seatsMap)).filter(s -> s == OCCUPIED).count();
    }

    static Seat nearestOccupationInDirection(Pair position, Pair direction, Map<Pair, Seat> seatsMap) {
        Seat foundSeat;
        do {
            position = position.add(direction);
            foundSeat = seatsMap.getOrDefault(position, FLOOR);
        } while (seatsMap.containsKey(position) && foundSeat == FLOOR);
            
        return foundSeat;
    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }
    
    void printSeats(Map<Pair, Seat> seatsMap) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pair pos = new Pair(x, y);
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

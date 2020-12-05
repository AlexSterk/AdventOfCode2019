package days;

import setup.Day;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 extends Day {

    private List<Record> log;
    private Map<Integer, Map<Integer, Integer>> minutes;

    @Override
    public void processInput() {
        log = Arrays.stream(input.trim().split("\n")).map(Record::stringToRecord).collect(Collectors.toList());
        log.sort(Comparator.comparing(Record::timestamp));
    }

    @Override
    public void part1() {
        minutes = new HashMap<>();
        
        int onDuty = -1;
        Date startsSleep = null;

        for (Record record : log) {
            if (record.log() instanceof StartsShift s) {
                onDuty = s.guard();
                startsSleep = null;
            } else if (record.log() == Action.SLEEPS) {
                startsSleep = record.timestamp();
            } else if (record.log() == Action.WAKES && startsSleep != null) {
//                long between = ChronoUnit.MINUTES.between(startsSleep.toInstant(), record.timestamp().toInstant());
                int start = startsSleep.getMinutes();
                int end = record.timestamp().getMinutes();
                int finalOnDuty = onDuty;
                
                IntStream.range(start, end).forEach(i -> minutes.computeIfAbsent(finalOnDuty, x -> new HashMap<>()).merge(i, 1, Integer::sum) );
                startsSleep = null;
            }
        }
        
        int longestSleeper = minutes.entrySet().stream().max(Comparator.comparing(e -> e.getValue().values().stream().mapToInt(i -> i).sum())).get().getKey();
        int minute = minutes.get(longestSleeper).entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

        System.out.println(longestSleeper * minute);
    }

    @Override
    public void part2() {
        Map<Integer, Map<Integer, Integer>> inverted = new HashMap<>();
        
        minutes.forEach((k,v) -> v.forEach((m, c) -> inverted.computeIfAbsent(m, i -> new HashMap<>()).put(k,c)));

        int mostFrequentMinute = inverted.entrySet().stream().max(Comparator.comparing(e -> Collections.max(e.getValue().values()))).get().getKey();
        int guard = inverted.get(mostFrequentMinute).entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        System.out.println(mostFrequentMinute * guard);
    }

    @Override
    public int getDay() {
        return 4;
    }
}

sealed interface Log {
    
}

record StartsShift(int guard) implements Log {}

enum Action implements Log {
    SLEEPS,
    WAKES
}

record Record(Date timestamp, Log log) {
    static final Pattern ENTRY_PATTERN = Pattern.compile("\\[(.+)\\] (.+)");
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    static Record stringToRecord(String s) {
        var m = ENTRY_PATTERN.matcher(s.trim());
        m.matches();

        Date timestamp = null;
        try {
            timestamp = DATE_FORMAT.parse(m.group(1));
        } catch (ParseException e) {
           throw new RuntimeException();
        }
        Log log = switch (m.group(2)) {
            case "wakes up" -> Action.WAKES;
            case "falls asleep" -> Action.SLEEPS;
           default -> {
               var ma = Pattern.compile("\\d+").matcher(m.group(2));
               ma.find();
               yield new StartsShift(Integer.parseInt(ma.group()));
           }
        };
        
        return new Record(timestamp, log);
    }
}




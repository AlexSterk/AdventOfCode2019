package days;

import misc.Pair;
import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 extends Day {
    private Map<String, Set<Pair>> fields;
    private List<Integer> myTicket;
    private List<List<Integer>> tickets;

    private static final Pattern FIELD_PATTERN = Pattern.compile("(.+): (\\d+)-(\\d+)(?: or (\\d+)-(\\d+))*");
    
    private int errorRate;

    @Override
    public void processInput() {
        String[] split = input.trim().split("(\r?\n){2}");
        

        fields = new HashMap<>();

        Arrays.stream(split[0].split("\r?\n")).forEach(this::processField);
        myTicket = parseTicket(split[1].replaceAll("your ticket:", ""));
        tickets = Arrays.stream(split[2].replace("nearby tickets:", "").trim().split("\r?\n")).map(Day16::parseTicket).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        errorRate = 0;
        tickets.forEach(this::isValidTicket);
        System.out.println(errorRate);
    }

    @Override
    public void part2() {
        
    }

    @Override
    public int getDay() {
        return 16;
    }
    
    private boolean isValidTicket(List<Integer> ticket) {
        Set<Pair> allRanges = new HashSet<>();
        fields.values().forEach(allRanges::addAll);

        for (Integer n : ticket) {
            if (!validForOneRange(n, allRanges)) {
                errorRate += n;
                return false;
            }
        }
        
        return true;
    }
    
    private boolean validForOneRange(int n, Set<Pair> ranges) {
        return ranges.stream().anyMatch(r -> inRange(n, r));
    }
    
    private boolean inRange(int n, Pair range) {
        return n >= range.key() && n <= range.value();
    }

    private void processField(String s) {
        Matcher m = FIELD_PATTERN.matcher(s);
        if (!m.matches()) {
            throw new RuntimeException();
        }

        Set<Pair> ranges = new HashSet<>();

        for (int i = 2; i < m.groupCount(); i += 2) {
            ranges.add(new Pair(m.group(i), m.group(i+1)));
        }

        fields.put(m.group(1), ranges);
    }
    
    private static List<Integer> parseTicket(String s) {
        return Arrays.stream(s.trim().split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

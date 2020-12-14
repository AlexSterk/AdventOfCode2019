package days;

import misc.Pair;
import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 extends Day {
    
    private static final Pattern MEM_PATTERN = Pattern.compile("mem\\[(\\d+)] = (\\d+)");
    
    private List<String> instructions;
    private List<Boolean> mask;
    private Map<Long, Long> memory;

    @Override
    public void processInput() {
        instructions = Arrays.asList(input.trim().split("\r?\n"));
    }

    @Override
    public void part1() {
        memory = new HashMap<>();

        for (String instruction : instructions) {
            if (instruction.startsWith("mask")) setMask(instruction.substring(7));
            else {
                Pair inst = stringToInstruction(instruction);
                List<Boolean> bits = longToBits(inst.value());
                applyMask(bits);
                long l = bitsToLong(bits);
                memory.put((long) inst.key(), l);
            }
        }

        System.out.println(memory.values().stream().mapToLong(l -> l).sum());
    }

    @Override
    public void part2() {
        memory = new HashMap<>();

        for (String instruction : instructions) {
            if (instruction.startsWith("mask")) setMask(instruction.substring(7));
            else {
                Pair inst = stringToInstruction(instruction);
                List<Boolean> bits = longToBits(inst.key());

                List<Long> longs = applyFloatingMask(bits);
                longs.forEach(l -> memory.put(l, (long) inst.value()));
            }
        }

        System.out.println(memory.values().stream().mapToLong(l -> l).sum());
    }

    @Override
    public int getDay() {
        return 14;
    }
    
    private void setMask(String m) {
        mask = m.chars().mapToObj(c -> switch(c) {
            case '0' -> false;
            case '1' -> true;
            default -> null;
        }).collect(Collectors.toList());
    }
    
    private Pair stringToInstruction(String s) {
        Matcher matcher = MEM_PATTERN.matcher(s);
        matcher.matches();
        return new Pair(matcher.group(1), matcher.group(2));
    }
    
    private List<Boolean> longToBits(long n) {
        BitSet bitSet = BitSet.valueOf(new long[]{n});
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 36; i++) {
            String c = bitSet.get(i) ? "1" : "0";
            s.append(c);
        }
        s.reverse();
        String o = s.toString();
        
        return o.chars().mapToObj(c -> switch (c) {
            case '0' -> false;
            case '1' -> true;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        }).collect(Collectors.toList());
    }
    
    private long bitsToLong(List<Boolean> bits) {
        String collect = bits.stream().map(b -> b ? "1" : "0").collect(Collectors.joining());

        return Long.parseLong(collect, 2);
    }
    
    private void applyMask(List<Boolean> bits) {
        for (int i = 0; i < mask.size(); i++) {
            if (mask.get(i) == null) continue;
            bits.set(i, mask.get(i));
        }
    }
    
    private List<Long> applyFloatingMask(List<Boolean> bits) {
        for (int i = 0; i < mask.size(); i++) {
            if (mask.get(i) != Boolean.FALSE) bits.set(i, mask.get(i));
        }

        ArrayList<List<Boolean>> acc = new ArrayList<>();
        recursiveFloating(bits, acc);

        return acc.stream().map(this::bitsToLong).collect(Collectors.toList());
    }
    
    private void recursiveFloating(List<Boolean> bits, List<List<Boolean>> acc) {
        int nullIndex = bits.indexOf(null);
        if (nullIndex == -1) {
            acc.add(bits);
        } else {
            var zero = new ArrayList<>(bits);
            var one = new ArrayList<>(bits);
            
            zero.set(nullIndex, false);
            one.set(nullIndex, true);
            
            recursiveFloating(zero, acc);
            recursiveFloating(one, acc);
        }
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

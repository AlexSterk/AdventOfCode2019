package days;

import misc.KVPair;
import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day18 extends Day {

    private List<String> expressions;

    @Override
    public void processInput() {
        expressions = Arrays.stream(input.trim().split("\r?\n")).map(s -> "(" + s + ")").collect(Collectors.toList());
    }

    @Override
    public void part1() {
        System.out.println(expressions.stream().mapToLong(this::evalLine).sum());
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 18;
    }
    
    final Pattern EXP = Pattern.compile("(\\d+)( *([+*]) *(\\d+) *)*");
    final Pattern PAREN = Pattern.compile("\\(%s\\)".formatted(EXP));
    
    Long evalLine(String s) {
        Matcher m;
        Long eval = 0L;
        
        do {
            m = PAREN.matcher(s);
            if (!m.find()) break;
            m.reset();
            for (MatchResult matchResult : m.results().collect(Collectors.toList())) {
                eval = evalInner(matchResult);
                s = s.replaceAll(Pattern.quote(matchResult.group()), eval.toString());
            }
        } while (true);
        
        return eval;
    }
    
    final Pattern EXP2 = Pattern.compile("(\\d+) *([+*]) *(\\d+)");
    
    Long evalInner(MatchResult r) {
        String s = r.group();
        Matcher m = EXP2.matcher(s);
        Long eval = 0L;

        while (m.find()) {
            Long e1 = Long.parseLong(m.group(1));
            String op = m.group(2);
            Long e2 = Long.parseLong(m.group(3));
            
            eval = switch (op) {
                case "+" -> e1 + e2;
                case "*" -> e1 * e2;
                default -> throw new IllegalStateException("Unexpected value: " + op);
            };

            m = EXP2.matcher(s = s.replaceFirst(Pattern.quote(m.group()), eval.toString()));
        }
        
        return eval;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

package days;

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
        System.out.println(expressions.stream().mapToLong(s -> evalLine(s, false)).sum());
    }

    @Override
    public void part2() {
        System.out.println(expressions.stream().mapToLong(s -> evalLine(s, true))
//                .peek(System.out::println)
                .sum());
    }

    @Override
    public int getDay() {
        return 18;
    }
    
    final Pattern PAREN = Pattern.compile("\\([^()]*\\)");
    
    Long evalLine(String s, boolean precedence) {
        Matcher m;
        Long eval = 0L;
        
        do {
            m = PAREN.matcher(s);
            if (!m.find()) break;
            m.reset();
            for (MatchResult matchResult : m.results().collect(Collectors.toList())) {
                eval = evalInner(matchResult, precedence);
                s = s.replaceAll(Pattern.quote(matchResult.group()), eval.toString());
            }
        } while (true);
        
        return eval;
    }
    
    final Pattern EXP = Pattern.compile("(\\d+) *([+*]) *(\\d+)");
    final Pattern PRECEDENCE_EXP = Pattern.compile("(\\d+) *([+]) *(\\d+)");
    
    Long evalInner(MatchResult r, boolean precedence) {
        String s = r.group();
        Matcher m = ((precedence = precedence && s.contains("+")) ? PRECEDENCE_EXP : EXP).matcher(s);
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

            s = s.replaceFirst(Pattern.quote(m.group()), eval.toString());
            if (!s.contains("+")) precedence = false;
            m = (precedence ? PRECEDENCE_EXP : EXP).matcher(s);
        }
        
        return eval;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

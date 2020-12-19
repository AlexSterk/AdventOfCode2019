package days;

import setup.Day;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 extends Day {
    
    private List<String> messages;

    @Override
    public void processInput() {
        String[] split = input.trim().split("(\r?\n){2}");
        String rules = split[0];
        String messages = split[1];

        Arrays.stream(rules.split("\r?\n")).forEach(Rule::stringToRule);
        Rule.RULES.values().forEach(Rule::init);

        this.messages = Arrays.stream(messages.split("\r?\n")).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        Rule rule = Rule.RULES.get("0");
        System.out.println(messages.stream().filter(m -> "".equals(rule.matches(m))).count());
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

interface Rule {
    Map<String, Rule> RULES = new HashMap<>();
    String matches(String s);
    void init();
     
    static Rule stringToRule(String s) {
        RulePatterns pattern = Arrays.stream(RulePatterns.values()).filter(p -> p.pattern.asMatchPredicate().test(s)).findAny().get();
        
        Matcher m = pattern.pattern.matcher(s);
        assert m.matches();
        
        var id = m.group(1);
        
        Rule rule = switch (pattern) {
            case STRING -> new StringRule(id, m.group(2));
            case CONJ -> new Conjunction(id, m.group(2).split(" "));
            case DISJ -> {
                String left = m.group(2);
                String right = m.group(3);
                
                if (!RULES.containsKey(left)) RULES.put(left, new Conjunction(left, left.split(" ")));
                if (!RULES.containsKey(right)) RULES.put(right, new Conjunction(right, right.split(" ")));
                
                yield new Disjunction(id, left, right);
            }
        };
        
        RULES.put(id, rule);

        return rule;
    }
    
    enum RulePatterns {
        STRING(Pattern.compile("(\\d+): \"(.+)\"")),
        CONJ(Pattern.compile("(\\d+): ((?:\\d+ *)+)")),
        DISJ(Pattern.compile("(\\d+): ((?:\\d+ *)+) \\| ((?:\\d+ *)+)"));
        
        final Pattern pattern;

        RulePatterns(Pattern pattern) {
            this.pattern = pattern;
        }
    }
}

record StringRule(String id, String s) implements Rule {
    @Override
    public String matches(String s) {
        if (s.startsWith(this.s)) return s.replaceFirst(this.s, "");
        return null;
    }

    @Override
    public void init() {
        // Nothing
    }

    @Override
    public String toString() {
        return s;
    }
}

record Conjunction(String id, List<String> rs, List<Rule> rules) implements Rule {
    Conjunction(String id, String... rs) {
        this(id, Arrays.asList(rs), new ArrayList<>());
    }

    @Override
    public String matches(String s) {
        for (Rule toMatch : rules) {
            String leftOver = toMatch.matches(s);
            if (leftOver != null) {
                s = leftOver;
            } else return null;
        }
        return s;
    }

    @Override
    public void init() {
        rules.clear();
        rs.stream().map(RULES::get).map(Objects::requireNonNull).forEach(rules::add);
    }

    @Override
    public String toString() {
        return String.join(" ", rs);
    }
}

record Disjunction(String id, List<String> rs, List<Rule> rules) implements Rule {
    Disjunction(String id, String... rs) {
        this(id, Arrays.asList(rs), new ArrayList<>());
    }

    @Override
    public String matches(String s) {
        for (Rule r : rules) {
            String matches = r.matches(s);
            if (matches != null) {
                return matches;
            }
        }
        return null;
    }

    @Override
    public void init() {
        rules.clear();
        rs.stream().map(RULES::get).map(Objects::requireNonNull).forEach(rules::add);
    }

    @Override
    public String toString() {
        return "(" + String.join( "|", rs) + ")";
    }
}
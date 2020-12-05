package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 extends Day {
    public static final Pattern POLICY_PATTERN = Pattern.compile("(\\d+)-(\\d+) (.+): (.+)");
    

    private List<PasswordPolicy> passwordPolicies;

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public void processInput() {
        passwordPolicies = Arrays.stream(input.split("\r?\n")).map(Day2::stringToPolicy).collect(Collectors.toList());
    }
    
    public static PasswordPolicy stringToPolicy(String input) {
        Matcher m = POLICY_PATTERN.matcher(input);
        boolean matches = m.matches();
        assert matches;

        return new PasswordPolicy(m.group(1), m.group(2), m.group(3), m.group(4));
    }

    @Override
    public void part1() {
        System.out.println(passwordPolicies.stream().filter(PasswordPolicy::isValidPartOne).count());
    }

    @Override
    public void part2() {
        System.out.println(passwordPolicies.stream().filter(PasswordPolicy::isValidPartTwo).count());
    }
}

record PasswordPolicy(int start, int end, String character, String password) {
    public PasswordPolicy(String start, String end, String character, String password) {
        this(Integer.parseInt(start), Integer.parseInt(end), character, password);
    }
    
    public boolean isValidPartOne() {
        Pattern p = Pattern.compile(character);
        Matcher m = p.matcher(password);
        long count = m.results().count();
        return count >= start && count <= end;
    }
    
    public boolean isValidPartTwo() {
        char c = character.charAt(0);
        return password.charAt(start-1) == c ^ password.charAt(end-1) == c; // XOR :D
    }
}

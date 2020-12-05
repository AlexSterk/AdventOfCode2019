package days;

import misc.KVPair;
import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 extends Day {

    private List<Passport> passports;

    @Override
    public void processInput() {
        String[] split = input.trim().split("\r?\n\r?\n");
        passports = Arrays.stream(split).map(Passport::stringToPassport).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        System.out.println(passports.stream().filter(Passport::hasFields).count());
    }

    @Override
    public void part2() {
        System.out.println(passports.stream().filter(Passport::isValid).count());
    }

    @Override
    public int getDay() {
        return 4;
    }
}

record Passport(String byr, String iyr, String eyr, String hgt, String hcl, String ecl, String pid, String cid) {
    boolean hasFields() {
        return byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null;
    }
    
    boolean isValid() {
        if (!hasFields()) return false;
        
        boolean byr, iyr, eyr, hgt, hcl, ecl, pid;

        try {
            int birthYear = Integer.parseInt(this.byr);
            byr = birthYear >= 1920 && birthYear <= 2002;

            int issueYear = Integer.parseInt(this.iyr);
            iyr = issueYear >= 2010 && issueYear <= 2020;

            int expYear = Integer.parseInt(this.eyr);
            eyr = expYear >= 2020 && expYear <= 2030;

            String unit = this.hgt.substring(this.hgt.length() - 2);
            if (unit.equals("cm")) {
                int height = Integer.parseInt(this.hgt.substring(0, 3));
                hgt = height >= 150 && height <= 193;
            } else if (unit.equals("in")) {
                int height = Integer.parseInt(this.hgt.substring(0, 2));
                hgt = height >= 59 && height <= 76;
            } else hgt = false;

            hcl = HEX_PATTERN.matcher(this.hcl).matches();

            ecl = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(this.ecl);

            pid = this.pid.chars().boxed().filter(Character::isDigit).count() == 9;
        } catch (NumberFormatException e) {
            return false;
        }

        return byr && iyr && eyr && hgt && hcl && ecl && pid;
    }
    static final Pattern HEX_PATTERN = Pattern.compile("#[0-9a-f]{6}");
    static Passport stringToPassport(String s) {
        var split = s.split("\\s+");
        var map = Arrays.stream(split).map(p -> new KVPair<>(p.substring(0, 3), p.substring(4))).collect(Collectors.toMap(KVPair::key, KVPair::value));

        return new Passport(
                map.getOrDefault("byr", null),
                map.getOrDefault("iyr", null),
                map.getOrDefault("eyr", null),
                map.getOrDefault("hgt", null),
                map.getOrDefault("hcl", null),
                map.getOrDefault("ecl", null),
                map.getOrDefault("pid", null),
                map.getOrDefault("cid", null)
        );
    }
}


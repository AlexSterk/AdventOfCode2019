package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day2 extends Day {
    List<List<Integer>> spreadsheet;

    @Override
    public void processInput() {
        spreadsheet = new ArrayList<>();
        for (String s : input.split("\n")) {
            ArrayList<Integer> e = new ArrayList<>();
            spreadsheet.add(e);
            for (String s1 : s.split("\\s")) {
                e.add(Integer.parseInt(s1));
            }
        }
    }

    @Override
    public void part1() {
        System.out.println(spreadsheet.stream().mapToInt(l -> Math.abs(Collections.max(l) - Collections.min(l))).sum());
    }

    @Override
    public void part2() {
        int sum = 0;
        r: for (List<Integer> row : spreadsheet) {
            for (Integer i : row) {
                for (Integer j : row) {
                    if (i.equals(j)) continue;
                    if (i % j == 0) {
                        sum +=i/j;
                        continue r;
                    }
                }
            }
        }
        System.out.println(sum);
    }

    @Override
    public int getDay() {
        return 2;
    }
}

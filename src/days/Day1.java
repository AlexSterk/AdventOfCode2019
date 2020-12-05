package days;

import setup.Day;

public class Day1 extends Day {
    String command;
    
    @Override
    public void processInput() {
        command = input.trim();
    }

    @Override
    public void part1() {
        int floor = 0;
        for (char c : command.toCharArray()) {
            switch (c) {
                case '(' -> floor++;
                case ')' -> floor--;
            }
        }
        System.out.println(floor);
    }

    @Override
    public void part2() {
        int floor = 0;
        char[] chars = command.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '(' -> floor++;
                case ')' -> floor--;
            }
            if (floor == -1) {
                System.out.println(i+1);
                return;
            }
        }
    }

    @Override
    public int getDay() {
        return 1;
    }
}

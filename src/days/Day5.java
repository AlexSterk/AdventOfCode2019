package days;

import setup.Day;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 extends Day {

    private List<String> boardingPasses;

    @Override
    public void processInput() {
        boardingPasses = Arrays.asList(input.trim().split("\r?\n"));
    }

    @Override
    public void part1() {
        System.out.println(boardingPasses.stream().map(Day5::getID).max(Comparator.naturalOrder()));
    }

    private static int getID(String boardingPass) {
        int lowerRows = 0;
        int upperRows = 127;

        int lowerCols = 0;
        int upperCols = 7;

        int row = -1;
        int column = -1;

        for (char c : boardingPass.substring(0,6).toCharArray()) {
            switch(c) {
                case 'F' -> upperRows = (lowerRows + upperRows) / 2;
                case 'B' -> lowerRows = lowerRows + (upperRows - lowerRows) / 2 + 1;
            }
        }

        switch (boardingPass.charAt(6)) {
            case 'F' -> row = lowerRows;
            case 'B' -> row = upperRows;
        }

        for (char c : boardingPass.substring(7,9).toCharArray()) {
            switch (c) {
                case 'L' -> upperCols = (lowerCols + upperCols) / 2;
                case 'R' -> lowerCols = lowerCols + (upperCols - lowerCols) / 2 + 1;
            }
        }

        switch (boardingPass.charAt(9)) {
            case 'L' -> column = lowerCols;
            case 'R' -> column = upperCols;
        }

        return row * 8 + column;
    }

    @Override
    public void part2() {
        List<Integer> sorted = boardingPasses.stream().map(Day5::getID).sorted().collect(Collectors.toList());
        for (int i = 0; i < sorted.size() - 1; i++) {
            Integer n = sorted.get(i);
            if (sorted.get(i+1) - n == 2) {
                System.out.println(n+1);
                return;
            }
        }
    }

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}

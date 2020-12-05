package days;

import misc.Pair;
import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Day {

    private List<Line> lines;

    @Override
    public void processInput() {
        lines = Arrays.stream(input.split("\n")).map(String::trim).map(Line::new).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        
        char[][] keypad = {{'1','2','3'},{'4','5','6'},{'7','8','9'}};
        
        List<Character> code = new ArrayList<>();
        
        Pair p = new Pair(1,1);

        for (Line line : lines) {
            p = line.execute(p, keypad);
            code.add(keypad[p.y()][p.x()]);
        }

        System.out.println(code.stream().map(Object::toString).collect(Collectors.joining()));
    }

    @Override
    public void part2() {
        char[][] keypad = {
                {' ', ' ', '1', ' ', ' '},
                {' ', '2', '3', '4', ' '},
                {'5', '6', '7', '8', '9'},
                {' ', 'A', 'B', 'C', ' '},
                {' ', ' ', 'D', ' ', ' '}
        };
        
        Pair p = new Pair(0, 2);

        List<Character> code = new ArrayList<>();

        for (Line line : lines) {
            p = line.execute(p, keypad);
            code.add(keypad[p.y()][p.x()]);
        }

        System.out.println(code.stream().map(Object::toString).collect(Collectors.joining()));
    }

    @Override
    public int getDay() {
        return 2;
    }
}

class Line {
    final List<Movement> movements;
    
    Line(String s) {
        movements = s.chars().mapToObj(Movement::charToMovement).collect(Collectors.toList());
    }
    
    Pair execute(Pair p, char[][] keypad) {
        for (Movement movement : movements) {
            p = movement.execute(p, keypad);
        }
        return p;
    }
}

enum Movement {
    UP('U', 0, -1),
    DOWN('D', 0, 1),
    LEFT('L', -1, 0),
    RIGHT('R', 1, 0);
    
    final char c;
    final int vx, vy;
    
    Movement(char c, int vx, int vy) {
        this.c = c;
        this.vx = vx;
        this.vy = vy;
    }
    
    static Movement charToMovement(int c) {
        for (Movement mv : Movement.values()) {
            if (mv.c == c) return mv;
        }
        return UP;
    }
    
    Pair execute(Pair p, char[][] keypad) {
        int x = p.x() + vx;
        int i = keypad.length - 1;
        if (x > i || x < 0) x = p.x();

        int y = p.y() + vy;
        if (y > i || y < 0) y = p.y();
        
        if (keypad[y][x] == ' ') return p;
        
        return new Pair(x, y);
    }
}

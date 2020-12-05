package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;

public class Day3 extends Day {
    private List<String> lines;
    private int length;

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public void processInput() {
        lines = Arrays.asList(input.split("\r?\n"));
        length = lines.get(0).length();
    }

    @Override
    public void part1() {
        System.out.println(traverse(3, 1));
    }

    @Override
    public void part2() {
        long a = traverse(1,1);
        System.out.println(a);
        long b = traverse(3,1);
        System.out.println(b);
        long c = traverse(5,1);
        System.out.println(c);
        long d = traverse(7,1);
        System.out.println(d);
        long e = traverse(1,2);
        System.out.println(e);

        System.out.println("======");
        System.out.println(a*b*c*d*e);
    }
    
    public long traverse(int vx, int vy) {
        int x = 0;
        int y = 0;
        int end = lines.size();
        int trees = lines.get(y).charAt(x) == '#' ? 1 : 0;

        while (y < end - 1) {
            x += vx;
            y += vy;
            if (lines.get(y).charAt(x % length) == '#') trees++;
        }
        
        return trees;
    }
}

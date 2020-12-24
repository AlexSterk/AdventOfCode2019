package days;

import setup.Day;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


public class Day20 extends Day {

    private List<PuzzleTile> tiles;
    private List<PuzzleTile> corners;

    @Override
    public void processInput() {
        tiles = Arrays.stream(input.split("(\r?\n){2}")).map(PuzzleTile::stringToTile).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        corners = new ArrayList<>();
        
        for (PuzzleTile tileOne : tiles) {
            int sharesBorderWith = 0;
            for (PuzzleTile tileTwo : tiles) {
                if (tileOne == tileTwo) continue;
                var sides = new HashSet<>(List.of(
                        tileOne.getRightSide(),
                        tileOne.getLeftSide(),
                        tileOne.getTopSide(),
                        tileOne.getBottomSide(),
                        tileTwo.getRightSide(),
                        tileTwo.getLeftSide(),
                        tileTwo.getTopSide(),
                        tileTwo.getBottomSide(),
                        reversed(tileOne.getRightSide()),
                        reversed(tileOne.getLeftSide()),
                        reversed(tileOne.getTopSide()),
                        reversed(tileOne.getBottomSide()),
                        reversed(tileTwo.getRightSide()),
                        reversed(tileTwo.getLeftSide()),
                        reversed(tileTwo.getTopSide()),
                        reversed(tileTwo.getBottomSide())
                ));

                if (sides.size() != 16) sharesBorderWith++;
            }
            if (sharesBorderWith == 2) corners.add(tileOne);
        }

        System.out.println(corners.stream().mapToLong(t -> t.id).reduce((a, b) -> a*b).getAsLong());
    }

    private void writeTiles() {
        for (PuzzleTile tile : tiles) {
            BufferedImage img = new BufferedImage(tile.width * tile.scale, tile.height * tile.scale, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = img.createGraphics();

            for (int y = 0; y < tile.height; y++) {
                for (int x = 0; x < tile.width; x++) {
                    graphics.setColor(tile.grid[y][x] == '#' ? Color.GREEN : Color.RED);
                    graphics.fillRect(x*tile.scale, y*tile.scale, tile.scale, tile.scale);
                }
            }
            
            graphics.setColor(Color.BLACK);
            
            try {
                ImageIO.write(img, "png", new File("data/day20/tiles/%d.png".formatted(tile.id)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void part2() {
        
    }

    @Override
    public int getDay() {
        return 20;
    }
    
    static <T> List<T> reversed(List<T> in) {
        ArrayList<T> ts = new ArrayList<>(in);
        Collections.reverse(ts);
        return ts;
    }
}

class PuzzleTile {
    final char[][] grid;
    final int width, height;
    final int id;
    final int scale = 10;

    PuzzleTile(char[][] grid, int id) {
        this.grid = grid;
        width = grid[0].length;
        height = grid.length;
        this.id = id;
    }
    
    static PuzzleTile stringToTile(String s) {
        String[] lines = s.split("\r?\n");

        var id = Integer.parseInt(lines[0].replace("Tile ", "").replace(":", ""));

        char[][] grid = new char[lines.length - 1][lines[1].length()];
        for (int y = 0; y < grid.length; y++) {
            grid[y] = lines[y+1].toCharArray();
        }
        
        return new PuzzleTile(grid, id);
    }
    
    List<Character> getLeftSide() {
        ArrayList<Character> side = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            side.add(grid[y][0]);
        }
        return side;
    }

    List<Character> getRightSide() {
        ArrayList<Character> side = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            side.add(grid[y][width - 1]);
        }
        return side;
    }

    List<Character> getTopSide() {
        ArrayList<Character> side = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            side.add(grid[0][x]);
        }
        return side;
    }

    List<Character> getBottomSide() {
        ArrayList<Character> side = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            side.add(grid[height - 1][x]);
        }
        return side;
    }
}



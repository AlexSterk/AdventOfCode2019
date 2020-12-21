package days;

import setup.Day;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Day20 extends Day {

    private List<Tile> tiles;

    @Override
    public void processInput() {
        tiles = Arrays.stream(input.split("(\r?\n){2}")).map(Tile::stringToTile).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        HashMap<Tile, List<List<Character>>> sides = new HashMap<>();
        tiles.forEach(t -> sides.put(t, List.of(t.getLeftSide(), t.getLRightSide(), t.getBottomSide(), t.getTopSide())));
        List<Tile> corners = new ArrayList<>();

        for (Tile tile : tiles) {
            List<List<Character>> side = sides.get(tile);
            int borders = 0;
            for (Tile other : tiles) {
                if (tile == other) continue;
                Set<List<Character>> lists = new HashSet<>(sides.get(other));
                lists.retainAll(side);
                List<List<Character>> reversed = side.stream().map(ArrayList::new).peek(Collections::reverse).collect(Collectors.toList());
                lists.retainAll(reversed);
                if (lists.size() > 0) borders++;
            }
            if (borders == 2) corners.add(tile);
        }

        System.out.println(corners);
        
    }

    private void writeTiles() {
        for (Tile tile : tiles) {
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
}

class Tile {
    final char[][] grid;
    final int width, height;
    final int id;
    final int scale = 10;

    Tile(char[][] grid, int id) {
        this.grid = grid;
        width = grid[0].length;
        height = grid.length;
        this.id = id;
    }
    
    static Tile stringToTile(String s) {
        String[] lines = s.split("\r?\n");

        var id = Integer.parseInt(lines[0].replace("Tile ", "").replace(":", ""));

        char[][] grid = new char[lines.length - 1][lines[1].length()];
        for (int y = 0; y < grid.length; y++) {
            grid[y] = lines[y+1].toCharArray();
        }
        
        return new Tile(grid, id);
    }
    
    List<Character> getLeftSide() {
        ArrayList<Character> side = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            side.add(grid[y][0]);
        }
        return side;
    }

    List<Character> getLRightSide() {
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



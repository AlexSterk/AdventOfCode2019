package AoC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("resources/day8.txt"));
        List<Integer> digits = input.chars().map(Character::getNumericValue).boxed().collect(Collectors.toList());

        int width = 25;
        int height = 6;

        int layerSize = width * height;
        int numLayers = digits.size() / layerSize;

        List<List<Integer>> layers = new ArrayList<>(numLayers);

        for (int i = 0; i < numLayers; i++) {
            layers.add(new ArrayList<>(digits.subList(i * layerSize, i * layerSize + layerSize)));
        }

        partOne(layers);
        partTwo(width, height, layers);
    }

    private static void partTwo(int width, int height, List<List<Integer>> layers) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Integer pixel = null;
                for (List<Integer> layer : layers) {
                    int i = x + y * width;
                    pixel = layer.get(i);
                    if (pixel != 2) break;
                }
                bufferedImage.setRGB(x,y, (pixel == 1) ? Integer.MAX_VALUE : 0);
            }
        }

        ImageIO.write(bufferedImage,"png", new File("resources/password.png"));
    }

    private static void partOne(List<List<Integer>> layers) {
        List<Integer> fewestZeros = layers.stream().min(Comparator.comparingLong(l -> l.stream().filter(i -> i == 0).count())).get();
        long ones = fewestZeros.stream().filter(i -> i == 1).count();
        long twos = fewestZeros.stream().filter(i -> i == 2).count();

        System.out.println(ones * twos);
    }
}

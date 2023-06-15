package org.app;

import org.apache.commons.codec.digest.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Algorithm {
    private final int PIXELS_WIDTH = 9;
    private final int PIXELS_HEIGHT = 8;

    public BufferedImage getResizedImage(String path) throws IOException {
        BufferedImage image = getImageFromResources(path);
        BufferedImage greyscaleImage = generateGreyScaleImageFrom(image);
        return resizeImage(greyscaleImage);
    }

    private BufferedImage getImageFromResources(String path) throws IOException {
        return ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
    }

    public String calculateDHash(String path) throws IOException {
        BufferedImage resizedImage = getResizedImage(path);
        writePngImageToFileSystem(resizedImage, "resized_greyscaled_image");

        Double[][] blackness = calculateBlacknessOf(resizedImage);
        Boolean[][] booleans = compareDifferences(blackness);
        Boolean[] flattened = flattenBooleanMatrix(booleans);
        Integer[] bits = convertBooleansToBits(flattened);

        String toBeHashed = Arrays.toString(bits);
        System.out.println("Boolean matrix");
        displayMatrix(booleans);
        System.out.println("Blackness matrix");
        displayMatrix(blackness);
        System.out.println("Bits array");
        System.out.println(toBeHashed);
        return DigestUtils.sha1Hex(toBeHashed);
    }

    private Boolean[][] compareDifferences(Double[][] blackness) {
        Boolean[][] booleans = initBooleanArray();

        for (int i = 0; i < blackness.length; i++) {
            for (int j = 0; j < blackness[0].length - 1; j++) {
                if (blackness[i][j + 1] > blackness[i][j]) {
                    booleans[i][j] = true;
                }
            }
        }

        return booleans;
    }

    private Boolean[][] initBooleanArray() {
        Boolean[][] booleans = new Boolean[8][8];
        for (int i = 0; i < booleans.length; i++) {
            for (int j = 0; j < booleans[0].length; j++) {
                booleans[i][j] = false;
            }
        }
        return booleans;
    }

    private Boolean[] flattenBooleanMatrix(Boolean[][] booleanArray) {
        Boolean[] flattened = new Boolean[64];

        int counter = 0;
        for (Boolean[] row : booleanArray) {
            for (Boolean b : row) {
                flattened[counter] = b;
                counter++;
            }
        }

        return flattened;
    }

    private Integer[] convertBooleansToBits(Boolean[] booleans) {
        Integer[] bits = new Integer[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            bits[i] = booleans[i] ? 1 : 0;
        }

        return bits;
    }

    private void displayMatrix(Object[][] matrix) {
        for (Object[] row : matrix) {
            for (Object value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    private Double[][] calculateBlacknessOf(BufferedImage image) {
        Double[][] blackness = new Double[PIXELS_HEIGHT][PIXELS_WIDTH];

        for(int y = 0; y < PIXELS_HEIGHT; y++) {
            for(int x = 0; x < PIXELS_WIDTH; x++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                double blacknessValue = 1.0 - red / 255.0;
                blackness[y][x] = blacknessValue;
            }
        }

        return blackness;
    }

    private void writePngImageToFileSystem(BufferedImage image, String name) throws IOException {
        File outputfile = new File(name + ".png");
        ImageIO.write(image, "png", outputfile);
    }

    private BufferedImage generateGreyScaleImageFrom(BufferedImage image) {
        BufferedImage greyscaleImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = greyscaleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return greyscaleImage;
    }

    private BufferedImage resizeImage(BufferedImage image) {
        BufferedImage resizedImage = new BufferedImage(
                PIXELS_WIDTH,
                PIXELS_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, PIXELS_WIDTH, PIXELS_HEIGHT, null);
        graphics2D.dispose();
        return resizedImage;
    }
}

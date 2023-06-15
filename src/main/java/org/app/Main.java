package org.app;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Algorithm algo = new Algorithm();
        System.out.println("pug1.jpg");
        String hash1 = algo.calculateDHash("pug1.jpg");
        System.out.println("\npug2.jpg");
        String hash2 = algo.calculateDHash("pug2.jpg");
        System.out.println("\npug3.jpg");
        String hash3 = algo.calculateDHash("pug3.jpg");
        System.out.println("\npug4.jpg");
        String hash4 = algo.calculateDHash("pug4.jpg");

        System.out.println();
        System.out.println("hash pug1: " + hash1);
        System.out.println("hash pug2: " + hash2);
        System.out.println("hash pug3: " + hash3);
        System.out.println("hash pug4: " + hash4);
    }
}
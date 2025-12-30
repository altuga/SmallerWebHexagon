package com.example.smallerwebhexagon;

public class InCodeRater implements Rater {
    @Override
    public double rate(int value) {
        if (value <= 100) return 1.01;
        return 1.5;
    }
}

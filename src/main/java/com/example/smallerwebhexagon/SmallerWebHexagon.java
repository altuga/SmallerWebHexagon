package com.example.smallerwebhexagon;

public class SmallerWebHexagon {
    private final Rater rater;

    public SmallerWebHexagon(Rater rater) {
        this.rater = rater;
    }

    public RateResult rateAndResult(int value) {
        double rate = rater.rate(value);
        double result = value * rate;
        return new RateResult(value, rate, result);
    }

    public static class RateResult {
        public final int value;
        public final double rate;
        public final double result;

        public RateResult(int value, double rate, double result) {
            this.value = value;
            this.rate = rate;
            this.result = result;
        }
    }
}

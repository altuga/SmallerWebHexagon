package com.example.smallerwebhexagon;


/**
 * Core business logic
 */
public class SmallerWebHexagon {
    private final Rater rater;

    public SmallerWebHexagon(Rater rater) {
        this.rater = rater;
    }

    public RateResult rateAndResult(int value) {
        double rate = rater.rate(value); //  where business logic and port- adapters meet
        double result = value * (1-rate/100); // core business logic
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

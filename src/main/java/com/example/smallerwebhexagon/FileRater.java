package com.example.smallerwebhexagon;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileRater implements Rater {
    private final List<double[]> rates = new ArrayList<>();

    /**
     * Create a {@code FileRater} by reading numeric rules from a plain-text file.
     *
     * <p>Each line in the file must contain one or more whitespace-separated numbers.
     * The constructor parses each token as a {@code double} and stores the numeric
     * arrays in memory for later use by {@link #rate(int)}.
     *
     * <p>Column definitions (per line):
     * <ul>
     *   <li><b>first column</b> - threshold / minimum value (double). In code: {@code rates.get(i)[0]}.</li>
     *   <li><b>second column</b> - rate value to return when the input matches the rule (double). In code: {@code rates.get(i)[1]}.</li>
     * </ul>
     *
     * <p>Example file (two rules):
     * <pre>
     * 0 0.8
     * 100 1.2
     * </pre>
     *
     * @param filename path to the rules file
     * @throws IOException if the file cannot be read
     * @throws NumberFormatException if any token is not a valid number
     */
    public FileRater(String filename) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(Path.of(filename))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                double[] vals = new double[parts.length];
                for (int i = 0; i < parts.length; i++)
                    vals[i] = Double.parseDouble(parts[i]);
                rates.add(vals);
            }
        }
    }

   
    /**
     * Rates the given value based on the rules loaded from the file.
     *
     * <p>The method applies the first rule where the value meets the threshold.
     * If fewer than 2 rules are loaded, returns 1.0 as default.
     *
     * @param value the integer value to rate
     * @return the rating as a double, or 1.0 if no rules apply
     *
     * <p>Example with rules file:
     * <pre>
     * 0 0.8
     * 100 1.2
     * </pre>
     *
     * <ul>
     *   <li>rate(50) returns 0.8 (50 >= 0 and 50 < 100)</li>
     *   <li>rate(150) returns 1.2 (150 >= 100)</li>
     *   <li>rate(-10) returns 1.0 (less than first threshold)</li>
     * </ul>
     */
    public double rate(int value) {
        if (rates.size() >= 2) {
            double min0 = rates.get(0)[0];
            double r0 = rates.get(0)[1];
            double r1 = rates.get(1)[1];
            if (value >= min0 && value < rates.get(1)[0])
                return r0;
            if (value >= min0)
                return r1;
        }
        return 1.0;
    }
}

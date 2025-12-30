package com.example.smallerwebhexagon;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileRater implements Rater {
    private final List<double[]> rates = new ArrayList<>();

    public FileRater(String filename) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(Path.of(filename))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                double[] vals = new double[parts.length];
                for (int i = 0; i < parts.length; i++) vals[i] = Double.parseDouble(parts[i]);
                rates.add(vals);
            }
        }
    }

    @Override
    public double rate(int value) {
        if (rates.size() >= 2) {
            double min0 = rates.get(0)[0];
            double r0 = rates.get(0)[1];
            double r1 = rates.get(1)[1];
            if (value >= min0 && value < rates.get(1)[0]) return r0;
            if (value >= min0) return r1;
        }
        return 1.0;
    }
}

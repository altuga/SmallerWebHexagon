package com.example.smallerwebhexagon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class SmallerWebHexagonTest {

    @Test
    public void testWithInCodeRater() {
        SmallerWebHexagon app = new SmallerWebHexagon(new InCodeRater());
        SmallerWebHexagon.RateResult r1 = app.rateAndResult(100);
        assertEquals(1.01, r1.rate, 0.00001);
        assertEquals(100 * 1.01, r1.result, 0.00001);

        SmallerWebHexagon.RateResult r2 = app.rateAndResult(200);
        assertEquals(1.5, r2.rate, 0.00001);
        assertEquals(200 * 1.5, r2.result, 0.00001);
    }

    @Test
    public void testWithFileRater() throws IOException {
        // copy the test file from original project if present, else use inline temp file
        String tmp = "target/test_file_rater.txt";
        java.nio.file.Files.createDirectories(java.nio.file.Path.of("target"));
        java.nio.file.Files.writeString(java.nio.file.Path.of(tmp), "0 1.00\n100 2.0\n");

        SmallerWebHexagon app = new SmallerWebHexagon(new FileRater(tmp));
        SmallerWebHexagon.RateResult r1 = app.rateAndResult(10);
        assertEquals(1.00, r1.rate, 0.00001);
        assertEquals(10 * 1.00, r1.result, 0.00001);

        SmallerWebHexagon.RateResult r2 = app.rateAndResult(100);
        assertEquals(2.0, r2.rate, 0.00001);
        assertEquals(100 * 2.0, r2.result, 0.00001);
    }
}

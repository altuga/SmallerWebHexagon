package com.example.smallerwebhexagon;

import static spark.Spark.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebAdapter {
    private final SmallerWebHexagon hex;
    private final String template;

    public WebAdapter(SmallerWebHexagon hex) throws IOException {
        this.hex = hex;
        this.template = Files.readString(Path.of("src/main/resources/templates/result_view.html"), StandardCharsets.UTF_8);
    }

    public void start() {
        get("/:value", (req, res) -> {
            String raw = req.params(":value");
            int value = numberOrZero(raw);
            SmallerWebHexagon.RateResult rr = hex.rateAndResult(value);
            String page = render(rr);
            res.type("text/html");
            return page;
        });
    }

    private int numberOrZero(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String render(SmallerWebHexagon.RateResult r) {
        return template.replace("${value}", Integer.toString(r.value))
                .replace("${rate}", Double.toString(r.rate))
                .replace("${result}", Double.toString(r.result));
    }
}

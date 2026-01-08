package com.example.smallerwebhexagon;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(4567), 0);
            server.createContext("/", exchange -> {
                String path = exchange.getRequestURI().getPath();
                // Simple parsing to mimic spark's /:value
                String raw = path.length() > 1 ? path.substring(1) : "";

                int value = numberOrZero(raw);
                SmallerWebHexagon.RateResult rr = hex.rateAndResult(value);
                String page = render(rr);

                exchange.getResponseHeaders().set("Content-Type", "text/html");
                byte[] responseBytes = page.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            });
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
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


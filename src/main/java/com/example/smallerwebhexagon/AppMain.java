package com.example.smallerwebhexagon;

public class AppMain {
    public static void main(String[] args) throws Exception {
        SmallerWebHexagon hex = new SmallerWebHexagon(new FileRater("src/main/resources/test_file_rater.txt"));
        WebAdapter adapter = new WebAdapter(hex);
        adapter.start();
        System.out.println("Server started. GET /{value} e.g. http://localhost:4567/100");
    }
}

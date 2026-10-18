package com.shopsphere.labs.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class UrlConnectionDemo {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: UrlConnectionDemo <https URL>");
            return;
        }
        URLConnection connection = new URL(args[0]).openConnection();
        connection.setConnectTimeout(5000); connection.setReadTimeout(5000);
        System.out.println("Content type: " + connection.getContentType());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines().limit(30).forEach(System.out::println);
        }
    }
}

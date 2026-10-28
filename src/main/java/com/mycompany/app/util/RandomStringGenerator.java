package com.mycompany.app.util;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private final int length;

    public RandomStringGenerator(int lenght) {
        this.length = lenght;
    }

    public String generate() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        String result = random.ints(length, 0, chars.length())
                .mapToObj(chars::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return result;
    }
}

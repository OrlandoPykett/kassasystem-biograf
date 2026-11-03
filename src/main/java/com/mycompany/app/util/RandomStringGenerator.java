package com.mycompany.app.util;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomStringGenerator {
    private final int length;

    private static final char[] CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
                    .toCharArray();

    public RandomStringGenerator(int length) {
        this.length = length;
    }

    public String generate() {
        char[] result = new char[length];
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            result[i] = CHARS[random.nextInt(CHARS.length)];
        }

        return new String(result);
    }
}

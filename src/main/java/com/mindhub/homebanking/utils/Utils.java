package com.mindhub.homebanking.utils;

import java.util.Random;

public class Utils {

    public static String random3() {
        StringBuilder ranNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            ranNumberBuilder.append(random.nextInt(10));
        }

        return ranNumberBuilder.toString();
    }

    public static String fakeCardNumber() {
        StringBuilder fakeCardNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                fakeCardNumberBuilder.append("-");
            }
            fakeCardNumberBuilder.append(random.nextInt(10));
        }

        return fakeCardNumberBuilder.toString();
    }
}

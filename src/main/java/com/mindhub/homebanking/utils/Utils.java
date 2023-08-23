package com.mindhub.homebanking.utils;

import java.util.Random;

public class Utils {

    public static String random3() {
        StringBuilder fakeCardNumberBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            fakeCardNumberBuilder.append(random.nextInt(10));
        }

        return fakeCardNumberBuilder.toString();
    }

}

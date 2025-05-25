package com.example.ecomarce.generic_logic;

import java.util.Random;

public class PinGenerator {
    public static String generateSixDigitPin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }

}

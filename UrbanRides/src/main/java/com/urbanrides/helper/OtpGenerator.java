package com.urbanrides.helper;

import java.util.Random;

public class OtpGenerator {

    public static int generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // generate a random 4-digit number
        return otp;
    }
}

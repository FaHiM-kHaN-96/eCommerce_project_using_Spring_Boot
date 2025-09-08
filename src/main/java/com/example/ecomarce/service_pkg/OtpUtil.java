package com.example.ecomarce.service_pkg;

import com.example.ecomarce.repo.Otp_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class OtpUtil {
    @Autowired
    private Otp_Repo otpRepo;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /** Returns a numeric 6-digit OTP as a String (e.g., "004197"). */
    public  String generate6DigitOtp() {
        int number = SECURE_RANDOM.nextInt(1_000_000);   // 0..999999
        System.out.println(number);
        if (!otpRepo.existsByOtppass(String.valueOf(number))) {
            return String.format("%06d", number);
        }
        return generate6DigitOtp();
    }

//    public static void main(String[] args) {
//        System.out.println(generate6DigitOtp());
//    }
}

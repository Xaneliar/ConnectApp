package com.connect.events.otp;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Random;

public class OtpVerification {

    // Twilio Account SID and Auth Token
    public static final String ACCOUNT_SID = "AC969cf67c8da5edcd0085ab95ecfb9974";
    public static final String AUTH_TOKEN = "3c7eca01cf0791167a65ec14de09dd9e";

    // Ensure Twilio is initialized before sending OTP
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Generate a random 6-digit OTP
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // generates a 6-digit OTP
        return String.valueOf(otp);
    }

    // Send OTP via SMS
    public static void sendOTP(String to, String otp) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),  // to
                    new PhoneNumber("19383483434"),  // from (Twilio provided number)
                    "Your OTP is: " + otp
            ).create();
            System.out.println("OTP sent: " + message.getSid());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send OTP: " + e.getMessage());
        }
    }
}

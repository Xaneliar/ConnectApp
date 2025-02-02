import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OTPSystem {
    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes
    private static final String FAST2SMS_API_KEY = "hALH7fgZA8tZEv1NW3oYfqj518AIl6DFzXz5uKdNLNhX7UMEHcGjeqDNIUzv";  // Replace with your actual API key

    private final Map<String, OTPRecord> otpStorage = new HashMap<>();

    private static class OTPRecord {
        String otp;
        long expirationTime;

        OTPRecord(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }
    }

    // Generate OTP
    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    // Send OTP via Fast2SMS
    public void sendOTP(String phoneNumber, String otp) {
        try {
            String encodedOTP = URLEncoder.encode(otp, StandardCharsets.UTF_8.toString());
            String encodedPhoneNumber = URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8.toString());
            
            String urlString = "https://www.fast2sms.com/dev/bulkV2" +
                    "?authorization=" + FAST2SMS_API_KEY +
                    "&variables_values=" + encodedOTP +
                    "&route=otp" +
                    "&numbers=" + encodedPhoneNumber;

            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("cache-control", "no-cache");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            System.out.println("API Response: " + response.toString());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("OTP sent successfully");
            } else {
                System.out.println("Failed to send OTP");
            }

        } catch (IOException e) {
            System.out.println("Error in sending OTP: " + e.getMessage());
        }
    }

    // Store OTP
    public void storeOTP(String phoneNumber, String otp) {
        long expirationTime = System.currentTimeMillis() + OTP_VALID_DURATION;
        otpStorage.put(phoneNumber, new OTPRecord(otp, expirationTime));
    }

    // Verify OTP
    public boolean verifyOTP(String phoneNumber, String userInputOTP) {
        OTPRecord storedOTP = otpStorage.get(phoneNumber);
        if (storedOTP != null) {
            if (System.currentTimeMillis() <= storedOTP.expirationTime) {
                boolean isValid = storedOTP.otp.equals(userInputOTP);
                if (isValid) {
                    otpStorage.remove(phoneNumber);  // Remove OTP after successful verification
                }
                return isValid;
            } else {
                otpStorage.remove(phoneNumber);  // Remove expired OTP
                System.out.println("OTP expired.");
            }
        }
        return false;
    }

    public static void main(String[] args) {
        OTPSystem otpSystem = new OTPSystem();
        try (Scanner scanner = new Scanner(System.in)) {
            // Input phone number
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();
            
            // Generate and send OTP
            String otp = otpSystem.generateOTP();
            otpSystem.sendOTP(phoneNumber, otp);
            otpSystem.storeOTP(phoneNumber, otp);
            
            // Input OTP for verification
            System.out.print("Enter the OTP you received: ");
            String userInputOTP = scanner.nextLine();
            
            // Verify OTP
            boolean isValid = otpSystem.verifyOTP(phoneNumber, userInputOTP);
            System.out.println("OTP is valid: " + isValid);
        }
    }
}



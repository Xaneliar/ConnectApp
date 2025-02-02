package com.connect.events.database;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

public class DatabaseController {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/connect_db";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "Xaneliar@00";

    // Connection and statement objects
    private static Connection conn = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        connectToDatabase();

        while (true) {
            System.out.println("\n1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Show Database");
            System.out.println("4. Update Credentials");
            System.out.println("5. Enter Location");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    showDatabase();
                    break;
                case 4:
                    updateCredentials(scanner);
                    break;
                case 5:
                    enterLocation();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    closeConnection();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to connect to database
    public static void connectToDatabase() {
        try {
            conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void enterLocation() {
        try {
            String apiKey = "8c1d08bbcc44bd"; // Replace with your IPinfo API key
            String apiUrl = "https://ipinfo.io/json?token=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject json = new JSONObject(response.toString());
            String city = json.getString("city");
            String region = json.getString("region");
            String country = json.getString("country");
            String loc = json.getString("loc"); // Latitude and longitude as a single string
            String postal = json.getString("postal"); // Get the postal/pincode

            String query = "INSERT INTO location (postal_code, city, country, loc) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, postal);
            pstmt.setString(2, city);
            pstmt.setString(3, country);
            pstmt.setString(4, loc);
            pstmt.executeUpdate();

            System.out.println("Location data stored successfully in the database.");
        } catch (Exception e) {
            System.out.println("Error storing location data: " + e.getMessage());
        }
    }
            // Method for signup with username/email validation
    public static void signup(String u, String m, String p) {
        String username = u;
        String password = p;
        String email = m;

        try {
            // Check if username or email already exists
            String checkSql = "SELECT * FROM users WHERE username = ? OR email = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nUser with this username or email already exists. Please try again.");
            } else {
                // Proceed with signup if no existing user is found
                String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println("\nSignup successful!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for login
    public static boolean login(String u, String p) {
        String username = u;
        String password = p;

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nLogin successful!");
            } else {
                System.out.println("\nUser not registered.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to show all users in the database
    public static void showDatabase() {
        try {
            String sql = "SELECT * FROM users";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("UserID | Username | Email | Password");
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                System.out.println(id + " | " + username + " | " + email + " | " + password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update credentials (username or password)
    public static void updateCredentials(Scanner scanner) {
        System.out.print("Enter current username: ");
        String currentUsername = scanner.nextLine();
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        try {
            // Verify the user exists
            String checkSql = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, currentUsername);
            pstmt.setString(2, currentPassword);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("User authenticated. What do you want to update?");
                System.out.println("1. Username");
                System.out.println("2. Password");
                int updateChoice = scanner.nextInt();
                scanner.nextLine();

                switch (updateChoice) {
                    case 1:
                        // Update username
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();

                        // Check if the new username is already taken
                        String checkUsernameSql = "SELECT * FROM users WHERE username = ?";
                        pstmt = conn.prepareStatement(checkUsernameSql);
                        pstmt.setString(1, newUsername);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            System.out.println("\nUsername already taken. Try a different one.");
                        } else {
                            String updateSql = "UPDATE users SET username = ? WHERE username = ? AND password = ?";
                            pstmt = conn.prepareStatement(updateSql);
                            pstmt.setString(1, newUsername);
                            pstmt.setString(2, currentUsername);
                            pstmt.setString(3, currentPassword);
                            int result = pstmt.executeUpdate();
                            if (result > 0) {
                                System.out.println("\nUsername updated successfully!");
                            }
                        }
                        break;
                    case 2:
                        // Update password
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();

                        String updatePasswordSql = "UPDATE users SET password = ? WHERE username = ? AND password = ?";
                        pstmt = conn.prepareStatement(updatePasswordSql);
                        pstmt.setString(1, newPassword);
                        pstmt.setString(2, currentUsername);
                        pstmt.setString(3, currentPassword);
                        int result = pstmt.executeUpdate();
                        if (result > 0) {
                            System.out.println("Password updated successfully!");
                        }
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to close database connection
    public static void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.connect.events.database;

import java.awt.*;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.sql.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.json.JSONObject;

public class DatabaseConnection extends Component {

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/connect_db";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "Xaneliar@00";
    private static final String API_KEY = "8c1d08bbcc44bd";
    private static Connection connection;

    public static String[] fetchLocation() throws Exception {
        String apiUrl = "https://ipinfo.io/json?token=" + API_KEY;

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

        JSONObject json = new JSONObject(response.toString());
        String city = json.getString("city");
        String country = json.getString("country");
        String loc = json.getString("loc"); // Latitude and longitude
        String pincode = json.getString("postal");

        return new String[] { pincode, city, country, loc };
    }

    // Method to store the location in the database
    public static Boolean storeLocation(String pincode, String city, String country, String loc) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO location (pincode, city, country, loc) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, pincode);
            statement.setString(2, city);
            statement.setString(3, country);
            statement.setString(4, loc);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error storing location: " + e.getMessage());
            return false;
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String getPhoneNumber(String username) {
        String phoneNumber = null;
        try {
            // Assume you have a method to get the connection
            Connection conn = getConnection();
            String query = "SELECT phone FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                phoneNumber = rs.getString("phone");
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    // Method to handle user signup
    public static boolean signUpUser(String username, String password, String email, String phone) {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertQuery = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";

        try (PreparedStatement checkStmt = getConnection().prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                System.out.println("Sign up failed: Username already taken.");
                return false;  // Username already exists
            }

            try (PreparedStatement insertStmt = getConnection().prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);  // Hash your passwords in real apps!
                insertStmt.setString(3, email);
                insertStmt.setString(4, phone);
                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Sign up failed: " + e.getMessage());
            return false;
        }
    }


    // Method to handle user login
    public static boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);  // Hash your passwords in real apps!
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // Returns true if a matching user is found
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
    }
}


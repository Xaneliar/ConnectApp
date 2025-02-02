package com.connect.events.panels;

import com.connect.events.ConnectEventsApp;
import com.connect.events.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class LocationApp extends JPanel {

    private ConnectEventsApp parent;
    private JButton locateMeButton;
    private JButton enterLocationButton;
    private JLabel titleLabel;

    public LocationApp(ConnectEventsApp parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Create title label
        titleLabel = new JLabel("Enter Your Location");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(67, 24, 98)); // Dark purple
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Create "Locate Me" button
        locateMeButton = new JButton("Locate Me");
        styleButton(locateMeButton, new Color(168, 88, 206), Color.BLACK);  // Adjust button color
        gbc.gridy = 1;
        add(locateMeButton, gbc);

        // Create "Enter Location" button
        enterLocationButton = new JButton("Enter Location");
        styleButton(enterLocationButton, new Color(67, 24, 98), Color.BLACK);  // Adjust button color
        gbc.gridy = 2;
        add(enterLocationButton, gbc);

        // Set background color for the panel
        setBackground(new Color(244, 228, 252)); // Light lavender color

        // Action listeners for buttons
        locateMeButton.addActionListener(e -> locateMe());
        enterLocationButton.addActionListener(e -> enterLocationManually());
    }

    // Styling method for buttons to ensure proper colors
    private void styleButton(JButton button, Color backgroundColor, Color textColor) {
        button.setFocusPainted(false);
        button.setForeground(textColor);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createLineBorder(new Color(102, 45, 145), 2));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(150, 40));
        button.setOpaque(true);
        button.setBorderPainted(true);
    }

    // Method to close the location dialog after entering the location
    protected void closeLocationDialog() {
        // This method will be overridden in the parent class when the dialog is instantiated
    }

    private void locateMe() {
        try {
            // Fetch location using the DatabaseConnection class
            String[] locationData = DatabaseConnection.fetchLocation();
            String pincode = locationData[0];
            String city = locationData[1];
            String country = locationData[2];

            // Display fetched location information
            JOptionPane.showMessageDialog(this, "Location Detected: \nPincode: " + pincode +
                    "\nCity: " + city + "\nCountry: " + country);

            // Store location in the database
            boolean isStored = DatabaseConnection.storeLocation(pincode, city, country, null);

            if (isStored) {
                JOptionPane.showMessageDialog(this, "Location stored successfully.");
                closeLocationDialog();  // Close location dialog
            } else {
                JOptionPane .showMessageDialog(this, "Failed to store location.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching location: " + ex.getMessage());
        }
    }

    private void enterLocationManually() {
        String pincode = JOptionPane.showInputDialog(this, "Enter Pincode:");
        String city = JOptionPane.showInputDialog(this, "Enter City:");
        String country = JOptionPane.showInputDialog(this, "Enter Country:");

        if (pincode != null && city != null && country != null) {
            // Storing manually entered location
            boolean isStored = DatabaseConnection.storeLocation(pincode, city, country, null);

            if (isStored) {
                JOptionPane.showMessageDialog(this, "Location stored successfully.");
                closeLocationDialog();  // Close location dialog after storing location
            } else {
                JOptionPane.showMessageDialog(this, "Failed to store location.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create an instance of ConnectEventsApp (or pass an existing one)
            ConnectEventsApp connectApp = new ConnectEventsApp();  // Assuming ConnectEventsApp is already defined

            // Create and show AboutPage
            new LocationApp(connectApp).setVisible(true);
        });
    }
}

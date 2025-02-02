package com.connect.events.panels;

import com.connect.events.APIHandler.ApiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Map;

public class EventPage extends JFrame {

    public EventPage() {
        // Set up the frame
        setTitle("Nearby Events");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel to hold the dropdown and button, center-aligned
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Create a label for event category selection
        JLabel selectLabel = new JLabel("Select Event Category:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        centerPanel.add(selectLabel);

        // Drop-down for user to select event category
        String[] eventCategories = {"Social Events", "Food & Drink", "Cultural & Religious", "Explore", "Movies"};
        JComboBox<String> eventCategoryDropdown = new JComboBox<>(eventCategories);
        eventCategoryDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(eventCategoryDropdown);

        // Button to fetch and display events
        JButton fetchEventsButton = new JButton("Fetch Events");
        fetchEventsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(fetchEventsButton);

        add(centerPanel, BorderLayout.CENTER); // Add the panel to the center

        // Panel to display the list of events
        JPanel eventListPanel = new JPanel();
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(eventListPanel);
        add(scrollPane, BorderLayout.EAST);

        // Button action listener to fetch events based on selected category
        fetchEventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user choice from the drop-down
                String userChoice = (String) eventCategoryDropdown.getSelectedItem();

                // Check if the user selected "Movies"
                if (userChoice.equals("Movies")) {
                    // Redirect to a new page to show movies in the local theater
                    new MoviesPage().setVisible(true);
                    return; // Exit early so we don't process other events here
                }

                // Otherwise, handle other categories
                Map<String, Map<String, String>> events;
                try {
                    events = ApiHandler.fetchEvents();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error fetching events: " + ex.getMessage());
                    return;
                }

                // Clear any existing events from the panel
                eventListPanel.removeAll();

                // Add the fetched events and their details to the panel
                for (Map.Entry<String, Map<String, String>> entry : events.entrySet()) {
                    String eventTitle = entry.getKey();
                    Map<String, String> eventDetails = entry.getValue();

                    // Create a label for the event title
                    JLabel eventLabel = new JLabel("Event: " + eventTitle);
                    eventLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    eventListPanel.add(eventLabel);

                    // Add event details below the title
                    for (Map.Entry<String, String> detail : eventDetails.entrySet()) {
                        JLabel detailLabel = new JLabel(detail.getKey() + ": " + detail.getValue());
                        eventListPanel.add(detailLabel);
                    }

                    // Add a separator between events
                    eventListPanel.add(Box.createVerticalStrut(15));
                }

                // Refresh the panel to display new events
                eventListPanel.revalidate();
                eventListPanel.repaint();
            }
        });
    }

    public static void main(String[] args) {
        // Create and display the Nearby Events page window
        EventPage eventsPage = new EventPage();
        eventsPage.setVisible(true);
    }
}

class MoviesPage extends JFrame {

    public MoviesPage() {
        // Set up the frame for movies page
        setTitle("Ongoing Movies in Local Theaters");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Dispose only this window when closed
        setLayout(new BorderLayout());

        // Create a panel to hold the list of movies
        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch ongoing movies based on user's location (backend API call)
        Map<String, Map<String, String>> movies;
        try {
            // Assuming ApiHandler has a method to fetch ongoing movies
            movies = ApiHandler.fetchMovies();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error fetching movies: " + ex.getMessage());
            return;
        }

        // Add fetched movies and their details to the panel
        for (Map.Entry<String, Map<String, String>> entry : movies.entrySet()) {
            String movieTitle = entry.getKey();
            Map<String, String> movieDetails = entry.getValue();

            // Create a label for the movie title
            JLabel movieLabel = new JLabel("Movie: " + movieTitle);
            movieLabel.setFont(new Font("Arial", Font.BOLD, 14));
            movieListPanel.add(movieLabel);

            // Add movie details below the title
            for (Map.Entry<String, String> detail : movieDetails.entrySet()) {
                JLabel detailLabel = new JLabel(detail.getKey() + ": " + detail.getValue());
                movieListPanel.add(detailLabel);
            }

            // Create a clickable label for a link (e.g., to buy tickets)
            String link = "http://example.com/tickets"; // Replace with the actual link
            JLabel linkLabel = new JLabel("<html><a href=''>" + "Buy Tickets" + "</a></html>");
            linkLabel.setForeground(Color.BLUE.darker()); // Set link color
            linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor on hover

            // Add mouse listener to handle link clicks
            linkLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Open the link in the browser
                    try {
                        Desktop.getDesktop().browse(new URI(link));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error opening link: " + ex.getMessage());
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    linkLabel.setText("<html><u>" + "Buy Tickets" + "</u></html>"); // Underline on hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    linkLabel.setText("<html><a href=''>" + "Buy Tickets" + "</a></html>"); // Remove underline
                }
            });

            movieListPanel.add(linkLabel); // Add the link label to the panel

            // Add a separator between movies
            movieListPanel.add(Box.createVerticalStrut(15));
        }

        // Refresh the panel to display new movies
        movieListPanel.revalidate();
        movieListPanel.repaint();
    }
}

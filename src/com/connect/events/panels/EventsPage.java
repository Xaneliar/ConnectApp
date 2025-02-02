package com.connect.events.panels;

import com.connect.events.APIHandler.ApiHandler;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class EventsPage extends JFrame {
    private static final String[] CATEGORIES = {"Movies", "Live Shows", "Standup", "Theater Shows", "Workshop & Other", "Night Life"};
    private static final String[] IMAGE_PATHS = {"../images/movie.jpg", "../images/concert.jpg", "../images/standup.jpg", "../images/theater.jpg", "../images/workshop.jpg", "../images/nightlife.jpg"};
    private Image backgroundImage;

    public EventsPage() {
        // Set up the frame
        setTitle("Your Life Begins Here");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        // Load background image
        try {
            URL imageUrl = getClass().getResource("../images/b1.jpg");
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            // Set a default color if image fails to load
            backgroundImage = null;
        }

        // Create main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Set a gradient background as fallback
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 50),
                            0, getHeight(), new Color(0, 0, 100));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setOpaque(false);

        // Create content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Add title
        JLabel titleLabel = new JLabel("Your Life Begins Here");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        contentPanel.add(titleLabel, gbc);

        // Add subtitle
        JLabel subtitleLabel = new JLabel("Discover, Explore, Experience");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitleLabel.setForeground(Color.WHITE);
        contentPanel.add(subtitleLabel, gbc);

        // Create categories panel
        JPanel categoriesPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        categoriesPanel.setOpaque(false);

        // Add category panels
        for (int i = 0; i < CATEGORIES.length; i++) {
            final int index = i;
            JPanel categoryPanel = createCategoryPanel(CATEGORIES[i], IMAGE_PATHS[i], new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleCategoryClick(CATEGORIES[index]);
                }
            });
            categoriesPanel.add(categoryPanel);
        }

        gbc.insets = new Insets(20, 0, 0, 0);
        contentPanel.add(categoriesPanel, gbc);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createCategoryPanel(String category, String imagePath, MouseAdapter mouseAdapter) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);

        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                Image image = ImageIO.read(imageUrl);
                if (image != null) {
                    Image scaledImage = image.getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    panel.add(imageLabel, BorderLayout.CENTER);
                }
            }
        } catch (IOException e) {
            JPanel placeholderPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(50, 50, 50));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            placeholderPanel.setPreferredSize(new Dimension(180, 120));
            panel.add(placeholderPanel, BorderLayout.CENTER);
        }

        JLabel categoryLabel = new JLabel(category, SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setForeground(Color.WHITE);
        panel.add(categoryLabel, BorderLayout.SOUTH);

        panel.addMouseListener(mouseAdapter);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                panel.setBorder(null);
            }
        });

        return panel;
    }

    private void handleCategoryClick(String category) {
        if (category.equals("Movies")) {
            openMoviesPage();
        } else {
            // Handle other categories with existing API handler
            try {
                Map<String, Map<String, String>> events = ApiHandler.fetchEvents();
                showEventsDialog(category, events);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error fetching events: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openMoviesPage() {
        SwingUtilities.invokeLater(() -> {
            MoviesPage moviesPage = new MoviesPage();
            moviesPage.setLocationRelativeTo(this);
            moviesPage.setVisible(true);
        });
    }

    private void showEventsDialog(String category, Map<String, Map<String, String>> events) {
        JDialog dialog = new JDialog(this, category + " Events", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel eventListPanel = new JPanel();
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));

        for (Map.Entry<String, Map<String, String>> entry : events.entrySet()) {
            String eventTitle = entry.getKey();
            Map<String, String> eventDetails = entry.getValue();

            JPanel eventPanel = new JPanel();
            eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
            eventPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createLineBorder(Color.GRAY)
            ));

            JLabel eventLabel = new JLabel("Event: " + eventTitle);
            eventLabel.setFont(new Font("Arial", Font.BOLD, 14));
            eventPanel.add(eventLabel);

            for (Map.Entry<String, String> detail : eventDetails.entrySet()) {
                JLabel detailLabel = new JLabel(detail.getKey() + ": " + detail.getValue());
                eventPanel.add(detailLabel);
            }

            eventListPanel.add(eventPanel);
            eventListPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(eventListPanel);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            EventsPage eventsPage = new EventsPage();
            eventsPage.setVisible(true);
        });
    }
}

class MoviessPage extends JFrame {
    public void MoviesPage() {
        setTitle("Ongoing Movies in Local Theaters");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add a header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(30, 30, 30));
        JLabel titleLabel = new JLabel("Now Showing");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
        movieListPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        add(scrollPane, BorderLayout.CENTER);

        try {
            Map<String, Map<String, String>> movies = ApiHandler.fetchMovies();
            displayMovies(movieListPanel, movies);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error fetching movies: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayMovies(JPanel movieListPanel, Map<String, Map<String, String>> movies) {
        for (Map.Entry<String, Map<String, String>> entry : movies.entrySet()) {
            String movieTitle = entry.getKey();
            Map<String, String> movieDetails = entry.getValue();

            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
            moviePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(new Color(200, 200, 200))
            ));
            moviePanel.setBackground(Color.WHITE);

            // Movie Title
            JLabel movieLabel = new JLabel(movieTitle);
            movieLabel.setFont(new Font("Arial", Font.BOLD, 16));
            moviePanel.add(movieLabel);
            moviePanel.add(Box.createVerticalStrut(5));

            // Movie Details
            for (Map.Entry<String, String> detail : movieDetails.entrySet()) {
                JLabel detailLabel = new JLabel(detail.getKey() + ": " + detail.getValue());
                detailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                moviePanel.add(detailLabel);
            }

            // Buy Tickets Button
            JButton buyTicketsButton = new JButton("Buy Tickets");
            buyTicketsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            buyTicketsButton.setBackground(new Color(0, 123, 255));
            buyTicketsButton.setForeground(Color.WHITE);
            buyTicketsButton.setFocusPainted(false);
            buyTicketsButton.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URI("http://example.com/tickets"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error opening ticket page: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            moviePanel.add(Box.createVerticalStrut(10));
            moviePanel.add(buyTicketsButton);

            movieListPanel.add(moviePanel);
            movieListPanel.add(Box.createVerticalStrut(10));
        }
    }
}
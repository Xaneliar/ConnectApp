package com.connect.events.panels;

import com.connect.events.ConnectEventsApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestimonialPage extends JPanel {
    private JPanel[] testimonialPanels;
    private int currentTestimonial = 0;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel testimonialContainer;

    public TestimonialPage() {
        // Custom colors matching the CSS
        Color backgroundColor = new Color(61, 38, 76); // #3d264c
        Color gradientStart = new Color(128, 64, 171); // #8040ab
        Color gradientEnd = new Color(44, 32, 52); // #2c2034
        Color textColor = new Color(231, 198, 255); // #e7c6ff
        Color titleColor = new Color(252, 229, 252); // #fce5fc

        // Main panel with solid background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(backgroundColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("What the Tribe's Vibing with");
        titleLabel.setForeground(titleColor);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Testimonial container with navigation buttons
        JPanel testimonialWithNavPanel = new JPanel(new BorderLayout(20, 0));
        testimonialWithNavPanel.setOpaque(false);
        testimonialWithNavPanel.setMaximumSize(new Dimension(500, 250)); // Reduced size

        // Background panel for testimonial section
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Load background image from resources
                BufferedImage bgImageUrl = null;
                try {
                    bgImageUrl = ImageIO.read(new File("resources/b1.jpg"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (bgImageUrl != null) {
                    ImageIcon bgImage = new ImageIcon(bgImageUrl);
                    g2d.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Fallback gradient
                    GradientPaint gradient = new GradientPaint(
                            0, 0, gradientStart,
                            0, getHeight(), gradientEnd);
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        testimonialContainer = new JPanel();
        cardLayout = new CardLayout();
        testimonialContainer.setLayout(cardLayout);
        testimonialContainer.setBackground(backgroundColor);

        // Create testimonials
        String[][] testimonialData = {
                {"Prasad Patil", "Mumbai is wild in the best way, and Connect helped me unlock the city's real pulse. From catching street art in Bandra to stumbling into random Bollywood shoots, every corner's got a story. Late-night pav bhaji under the city lights? Unmatched.", "../images/prasad.jpg"},
                {"Debosmita Paul", "Chennai's got a whole vibe that hits different, and Connect plugged me right into the scene. From rooftop gigs with Carnatic beats to sneaky late-night dosa spots, it's a whole mood. The city's culture? Next-level!", "../images/debo.jpg"},
                {"Ashish Kumar", "Kolkata's a total vibe, and Connect hooked me up with the real deal! From munching on killer street food to vibing at a rooftop poetry jam, this city's culture is next-level. Durga Puja? Absolute madness—in the best way!", "../images/ashish.jpg"}
        };

        testimonialPanels = new JPanel[testimonialData.length];

        for (int i = 0; i < testimonialData.length; i++) {
            testimonialPanels[i] = createTestimonialPanel(
                    testimonialData[i][0],
                    testimonialData[i][1],
                    gradientStart,
                    gradientEnd,
                    textColor,
                    testimonialData[i][2] // image path
            );
            testimonialContainer.add(testimonialPanels[i], String.valueOf(i));
        }
        // Navigation buttons inside testimonial container
        JButton prevButton = createNavigationButton("< <", -1);
        JButton nextButton = createNavigationButton("> >", 1);

        testimonialWithNavPanel.add(prevButton, BorderLayout.WEST);
        testimonialWithNavPanel.add(testimonialContainer, BorderLayout.CENTER);
        testimonialWithNavPanel.add(nextButton, BorderLayout.EAST);

        // Add testimonial section to background panel
        backgroundPanel.add(testimonialWithNavPanel, BorderLayout.CENTER);

        // User input panel (smaller size)
        JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BorderLayout(10, 10));
        userInputPanel.setBackground(gradientStart);
        userInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        userInputPanel.setMaximumSize(new Dimension(600, 80)); // Further reduced size

        JLabel inputLabel = new JLabel("Give Us The Deets:");
        inputLabel.setForeground(titleColor);
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JTextArea inputArea = new JTextArea(1, 30); // Single line
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(textColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(80, 25)); // Smaller button
        submitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Thank you for your testimonial: " + inputArea.getText());
            inputArea.setText("");
        });

        JPanel inputWrapper = new JPanel(new BorderLayout(10, 0));
        inputWrapper.setOpaque(false);
        inputWrapper.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        inputWrapper.add(submitButton, BorderLayout.EAST);

        userInputPanel.add(inputLabel, BorderLayout.WEST);
        userInputPanel.add(inputWrapper, BorderLayout.CENTER);

        // Add components to main panel with proper spacing
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(backgroundPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Center the user input panel
        JPanel inputCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputCenterPanel.setOpaque(false);
        inputCenterPanel.add(userInputPanel);
        mainPanel.add(inputCenterPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // Add the main panel to this TestimonialPanel (JPanel)
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
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
            new AboutPage(connectApp).setVisible(true);
        });
    }

    private JButton createNavigationButton(String text, int direction) {
        JButton button = new JButton(text);
        button.setFont(new Font("Dialog", Font.PLAIN, 20)); // Smaller font
        button.setPreferredSize(new Dimension(50, 50)); // Reduced size
        button.setBackground(new Color(92, 64, 111, 200));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            currentTestimonial = (currentTestimonial + direction + testimonialPanels.length) % testimonialPanels.length;
            cardLayout.show(testimonialContainer, String.valueOf(currentTestimonial));
        });
        return button;
    }


    private JPanel createTestimonialPanel(String author, String text,
                                          Color gradientStart, Color gradientEnd, Color textColor, String imagePath) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, gradientStart,
                        0, getHeight(), gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Load and display the avatar image
        JLabel avatarLabel = new JLabel();
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            ImageIcon avatarIcon = new ImageIcon(imageUrl);
            Image scaledImage = avatarIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            avatarLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            System.err.println("Image not found: " + imagePath);
            // Optionally: Load a placeholder image or continue without setting an icon.
            avatarLabel.setText("No Image Available");
        }
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Testimonial text
        JTextArea testimonialText = new JTextArea(text);
        testimonialText.setWrapStyleWord(true);
        testimonialText.setLineWrap(true);
        testimonialText.setOpaque(false);
        testimonialText.setEditable(false);
        testimonialText.setForeground(textColor);
        testimonialText.setFont(new Font("Segoe UI", Font.ITALIC, 24)); // Smaller font
        testimonialText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Author name
        JLabel authorLabel = new JLabel("- " + author);
        authorLabel.setForeground(textColor);
        authorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Smaller font
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(avatarLabel);
        panel.add(Box.createVerticalStrut(15)); // Reduced spacing
        panel.add(testimonialText);
        panel.add(Box.createVerticalStrut(15)); // Reduced spacing
        panel.add(authorLabel);

        return panel;
    }
}

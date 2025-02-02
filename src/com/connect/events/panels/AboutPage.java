package com.connect.events.panels;

import com.connect.events.ConnectEventsApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AboutPage extends JPanel {
    private ConnectEventsApp parent; // Reference to parent for navigation
    private static final String PURPLE_DARK = "#2E073F";
    private static final String PURPLE_MEDIUM = "#8040AB";
    private static final String PURPLE_LIGHT = "#E7C6FF";
    private static final String TEXT_LIGHT = "#FCE5FC";

    public AboutPage(ConnectEventsApp parent) {
        this.parent = parent; // Save parent reference
        setLayout(new BorderLayout());

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.decode(PURPLE_DARK),
                        0, getHeight(), Color.decode(PURPLE_MEDIUM)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Add header with back button
        JPanel headerPanel = createHeaderWithBackButton();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content ScrollPane with proper background
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createFooter();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Create the header panel with the back button
    private JPanel createHeaderWithBackButton() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode(PURPLE_DARK));
        header.setPreferredSize(new Dimension(getWidth(), 60));

        // Add a back button with a left arrow icon
        JButton backButton = new JButton("Back");
        backButton.setIcon(new ImageIcon("resources/left-arrow.png")); // Make sure to have this image in the resources
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.decode(TEXT_LIGHT));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Action listener to go back to the home page
        backButton.addActionListener(e -> parent.showHome()); // Switch back to the Home panel

        // Title label
        JLabel titleLabel = new JLabel("About Our Company");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode(TEXT_LIGHT));

        // Add button and title to the header
        header.add(backButton, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.decode(PURPLE_DARK),
                        0, getHeight(), Color.decode(PURPLE_MEDIUM)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components with proper spacing
        contentPanel.add(createHighlightPanel());
        contentPanel.add(Box.createVerticalStrut(15));

        // Add dropdowns
        contentPanel.add(createDropdownPanel("Who Are We?",
                "We're the squad that knows the struggle of moving somewhere new. " +
                        "You don't know the streets, the hangouts, or where the crew is vibing. " +
                        "That's why we built CONNECT—to make sure you can explore your new digs like a local from day one."));

        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(createDropdownPanel("What We Offer?",
                "• Social Events: Epic college fests and underground music gigs\n" +
                        "• Food & Drink: Best places for quick bites and study sessions\n" +
                        "• Cultural Vibes: Diwali parties and art festivals\n" +
                        "• Nightlife: Hottest bars and clubs\n" +
                        "• Explore: Cool parks and hidden gems"));

        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(createDropdownPanel("Why Roll with CONNECT?",
                "We're not just another app—we're your personal guide to your new life, " +
                        "making sure you're never out of the loop."));

        contentPanel.add(Box.createVerticalStrut(20));

        // Add team section
        contentPanel.add(createTeamSection());

        return contentPanel;
    }

    private JPanel createHighlightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode(PURPLE_LIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create a left-aligned container for the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(Color.decode(PURPLE_LIGHT));

        JLabel title = new JLabel("Welcome to CONNECT: YOUR FRIEND IN NEED");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(title);

        // Text area for description
        JTextArea description = new JTextArea(
                "Your personal guide to discovering the best of social life, dining, " +
                        "and local culture right at your fingertips. Whether you're a university " +
                        "student diving into a new city or a high schooler exploring fresh horizons, " +
                        "CONNECT is designed to make your journey easier, more fun, and full of excitement."
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setBackground(Color.decode(PURPLE_LIGHT));
        description.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        description.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(titlePanel);
        panel.add(description);

        return panel;
    }

    private JPanel createDropdownPanel(String title, String content) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode(PURPLE_LIGHT));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.decode(PURPLE_MEDIUM));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel headerLabel = new JLabel(title);
        headerLabel.setForeground(Color.decode(TEXT_LIGHT));
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.decode(PURPLE_LIGHT));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JTextArea contentArea = new JTextArea(content);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.decode(PURPLE_LIGHT));
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));

        contentPanel.add(contentArea, BorderLayout.CENTER);
        contentPanel.setVisible(false);

        // Add click listener
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPanel.setVisible(!contentPanel.isVisible());
                mainPanel.revalidate();
                SwingUtilities.getWindowAncestor(mainPanel).repaint();
            }
        });

        mainPanel.add(headerPanel);
        mainPanel.add(contentPanel);

        return mainPanel;
    }


    private JPanel createTeamSection() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 0, 0, 0));

        JLabel titleLabel = new JLabel("Our Team");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel teamGrid = new JPanel(new GridLayout(1, 4, 10, 0));
        teamGrid.setBackground(new Color(0, 0, 0, 0));

        // Add team members
        teamGrid.add(createTeamMemberPanel("Tejashwin", "Frontend Developer",
                "tb9407@srmist.edu.in", "resources/tej.jpg"));
        teamGrid.add(createTeamMemberPanel("Vaishnavi Surada", "Frontend Developer",
                "vs0473@srmist.edu.in", "resources/vaishu.jpg"));
        teamGrid.add(createTeamMemberPanel("Aditya Nikhoria", "Backend Developer",
                "an1980@srmist.edu.in", "resources/adi.jpg"));
        teamGrid.add(createTeamMemberPanel("Gautam Soni", "Backend Developer",
                "gs2505@srmist.edu.in", "resources/soni.jpg"));

        mainPanel.add(teamGrid, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createTeamMemberPanel(String name, String role, String email, String imagePath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode(PURPLE_LIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Profile picture
        CircularImagePanel imagePanel = new CircularImagePanel(imagePath);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Labels
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components with proper spacing
        panel.add(Box.createVerticalStrut(5));
        panel.add(imagePanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(roleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(Color.decode(PURPLE_DARK));
        footer.setPreferredSize(new Dimension(getWidth(), 40));

        JLabel copyrightLabel = new JLabel("© 2024 Our Company. All rights reserved.");
        copyrightLabel.setForeground(Color.decode(TEXT_LIGHT));
        footer.add(copyrightLabel);

        return footer;
    }
}

// New class for handling circular profile images
class CircularImagePanel extends JPanel {
    private BufferedImage image;
    private BufferedImage circularImage;
    private final int SIZE = 120; // Reduced size for better fit

    public CircularImagePanel(String imagePath) {
        try {
            // Load and scale the image
            image = ImageIO.read(new File(imagePath));
            createCircularImage();
        } catch (IOException e) {
            createPlaceholder();
        }
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void createPlaceholder() {
        image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.GRAY);
        g2d.fillOval(0, 0, SIZE-1, SIZE-1);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, SIZE/3));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "?";
        int textX = (SIZE - fm.stringWidth(text)) / 2;
        int textY = (SIZE + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
        g2d.dispose();
        createCircularImage();
    }

    private void createCircularImage() {
        circularImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Create circular clip
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, SIZE, SIZE);
        g2.setClip(circle);

        // Calculate dimensions for filling the circle while maintaining aspect ratio
        double scale;
        int targetWidth = SIZE;
        int targetHeight = SIZE;

        if (image.getWidth() > image.getHeight()) {
            scale = (double) SIZE / image.getHeight();
            targetWidth = (int) (image.getWidth() * scale);
            int x = (SIZE - targetWidth) / 2;
            g2.drawImage(image, x, 0, targetWidth, SIZE, null);
        } else {
            scale = (double) SIZE / image.getWidth();
            targetHeight = (int) (image.getHeight() * scale);
            int y = (SIZE - targetHeight) / 2;
            g2.drawImage(image, 0, y, SIZE, targetHeight, null);
        }

        // Add a subtle border
        g2.setClip(null);
        g2.setColor(new Color(0, 0, 0, 50));
        g2.setStroke(new BasicStroke(2));
        g2.draw(circle);

        g2.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (circularImage != null) {
            int x = (getWidth() - SIZE) / 2;
            int y = (getHeight() - SIZE) / 2;
            g.drawImage(circularImage, x, y, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE + 20, SIZE + 20); // Add padding
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
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
}


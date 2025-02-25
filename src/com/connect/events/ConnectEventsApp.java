package com.connect.events;

import com.connect.events.database.DatabaseConnection;
import com.connect.events.otp.OtpVerification;
import com.connect.events.panels.AboutPage;
import com.connect.events.panels.LocationApp;
import com.connect.events.panels.TestimonialPage;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.*; // Add this at the top of ConnectEventsApp.java

// Custom color scheme
class ColorScheme {
    static final Color DARK_PURPLE = new Color(46, 7, 63);
    static final Color MEDIUM_PURPLE = new Color(122, 28, 172);
    static final Color LIGHT_PURPLE = new Color(173, 73, 225);
    static final Color PALE_PURPLE = new Color(235, 211, 248);
}

// Main application window
public class ConnectEventsApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HomePanel homePanel;
    private EventCategoriesPanel categoriesPanel;
    private AboutPage aboutPage;
    private TestimonialPage testimonialPage;


    public ConnectEventsApp() {
        setTitle("CONNECT - Local Events Near You");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        homePanel = new HomePanel(this);
        categoriesPanel = new EventCategoriesPanel(this);
        aboutPage = new AboutPage(this);
        testimonialPage = new TestimonialPage();

        mainPanel.add(homePanel, "HOME");
        mainPanel.add(categoriesPanel, "CATEGORIES");
        mainPanel.add(aboutPage, "ABOUT");
        mainPanel.add(testimonialPage, "TESTIMONIALS");

        add(mainPanel);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to show About page
    public void showAbout() {
        cardLayout.show(mainPanel, "ABOUT");
    }

    public void showCategories() {
        cardLayout.show(mainPanel, "CATEGORIES");
    }

    public void showHome() {
        cardLayout.show(mainPanel, "HOME");
    }

    public void showTestimonials() {
        cardLayout.show(mainPanel, "TESTIMONIALS");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConnectEventsApp app = new ConnectEventsApp();
            app.setVisible(true);
        });
    }


    // Custom styled button
    class StyledButton extends JButton {
        public StyledButton(String text, Color bgColor, Color fgColor) {
            super(text);
            setBackground(bgColor);
            setForeground(fgColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Arial", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setPreferredSize(new Dimension(120, 40));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgColor.brighter());
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }
    }

    class AboutPanel extends JPanel {
        private ConnectEventsApp parent;

        public AboutPanel(ConnectEventsApp parent) {
            this.parent = parent;
            setLayout(new BorderLayout());


            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(
                            0, 0, Color.decode("#2E073F"),
                            0, getHeight(), Color.decode("#8040AB")
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            mainPanel.setLayout(new BorderLayout());
            JScrollPane scrollPane = new JScrollPane(createContentPanel());
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            add(mainPanel, BorderLayout.CENTER);
        }

        private JPanel createContentPanel() {
            JPanel contentPanel = new JPanel();
            return contentPanel;
        }
    }


    // Home Panel with background and overlay
    // HomePanel class with About Us functionality
    // Home Panel with background and overlay
    class HomePanel extends JPanel {
        private ConnectEventsApp parent;
        private Image backgroundImage;

        public HomePanel(ConnectEventsApp parent) {
            this.parent = parent;
            setLayout(new BorderLayout());

            try {
                backgroundImage = ImageIO.read(new File("resources/c1.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create navigation panel
            JPanel navPanel = createNavPanel();
            add(navPanel, BorderLayout.NORTH);

            // Create content panel
            JPanel contentPanel = createContentPanel();
            add(contentPanel, BorderLayout.CENTER);
        }

        private JPanel createNavPanel() {
            JPanel nav = new JPanel(new BorderLayout());
            nav.setBackground(new Color(46, 7, 63, 230));
            nav.setPreferredSize(new Dimension(0, 60));

            // Logo
            JLabel logo = new JLabel("CONNECT");
            logo.setFont(new Font("Arial", Font.BOLD, 24));
            logo.setForeground(ColorScheme.LIGHT_PURPLE);
            logo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

            // Nav links
            JPanel links = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            links.setOpaque(false);

            String[] linkTexts = {"Home", "About Us", "Events", "Reviews"};
            for (String text : linkTexts) {
                JLabel link = new JLabel(text);
                link.setForeground(ColorScheme.PALE_PURPLE);
                link.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
                link.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Add action listener for "About Us"
                if (text.equals("About Us")) {
                    link.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            parent.showAbout(); // Navigate to About page
                        }
                    });
                }

                // Add action listener for "Testimonials"
                // Inside HomePanel class
                if (text.equals("Reviews")) {
                    link.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            parent.showTestimonials(); // Open the Testimonials page
                        }
                    });
                }


                links.add(link);
            }

            nav.add(logo, BorderLayout.WEST);
            nav.add(links, BorderLayout.EAST);

            return nav;
        }

        private JPanel createContentPanel() {
            JPanel content = new JPanel(new GridBagLayout());
            content.setOpaque(false);

            // CTA Panel
            JPanel cta = new JPanel();
            cta.setLayout(new BoxLayout(cta, BoxLayout.Y_AXIS));
            cta.setBackground(new Color(106, 11, 157, 204));
            cta.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            // Title
            JLabel title = new JLabel("From Screen to Street:");
            title.setFont(new Font("Arial", Font.BOLD, 32));
            title.setForeground(ColorScheme.PALE_PURPLE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Description
            JLabel description = new JLabel("<html><center>Discover the hottest concerts, festivals, and local happenings instantly.<br>Your city's live experiences, curated for you in just a few taps.</center></html>");
            description.setFont(new Font("Arial", Font.PLAIN, 16));
            description.setForeground(ColorScheme.PALE_PURPLE);
            description.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Buttons panel
            JPanel buttons = new JPanel();
            buttons.setOpaque(false);

            StyledButton loginBtn = new StyledButton("Log In", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);
            StyledButton signupBtn = new StyledButton("Sign Up", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);
            StyledButton adminBtn = new StyledButton("Admin Login", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);

            buttons.add(loginBtn);
            buttons.add(Box.createRigidArea(new Dimension(20, 0)));
            buttons.add(signupBtn);
            buttons.add(Box.createRigidArea(new Dimension(20, 0)));
            buttons.add(adminBtn);

            cta.add(title);
            cta.add(Box.createRigidArea(new Dimension(0, 20)));
            cta.add(description);
            cta.add(Box.createRigidArea(new Dimension(0, 30)));
            cta.add(buttons);

            content.add(cta);

            // Add action listeners to buttons to open the dialogs
            loginBtn.addActionListener(e -> new LoginDialog(parent).setVisible(true));
            signupBtn.addActionListener(e -> new SignupDialog(parent).setVisible(true));
            adminBtn.addActionListener(e -> new AdminLoginDialog(parent).setVisible(true));

            return content;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw background image
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }

            // Draw overlay
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(46, 7, 63, 204));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }


    // Event Categories Panel
    class EventCategoriesPanel extends JPanel {
        private ConnectEventsApp parent;
        private Image backgroundImage;

        public EventCategoriesPanel(ConnectEventsApp parent) {
            this.parent = parent;
            setLayout(new BorderLayout());

            try {
                backgroundImage = ImageIO.read(new File("resources/b1.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create the categories content
            createContent();
        }

        private void createContent() {
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);

            // Title and subtitle
            JLabel title = new JLabel("Your Life Begins Here");
            title.setFont(new Font("Arial", Font.BOLD, 48));
            title.setForeground(ColorScheme.PALE_PURPLE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subtitle = new JLabel("Discover, Explore, Experience");
            subtitle.setFont(new Font("Arial", Font.PLAIN, 24));
            subtitle.setForeground(ColorScheme.PALE_PURPLE);
            subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Categories grid
            JPanel categoriesGrid = new JPanel(new GridLayout(2, 3, 30, 30));
            categoriesGrid.setOpaque(false);

            String[] categories = {"Movies", "Live Concerts", "Standup", "Theater Shows", "Workshop & Other", "Night Life"};
            for (String category : categories) {
                categoriesGrid.add(createCategoryCard(category));
            }

            contentPanel.add(Box.createVerticalStrut(40));
            contentPanel.add(title);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(subtitle);
            contentPanel.add(Box.createVerticalStrut(40));
            contentPanel.add(categoriesGrid);

            add(new JScrollPane(contentPanel));
        }

        private JPanel createCategoryCard(String category) {
            JPanel card = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw category background
                    g2d.setColor(ColorScheme.MEDIUM_PURPLE);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    // Draw category name
                    g2d.setColor(ColorScheme.PALE_PURPLE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(category);
                    int textHeight = fm.getHeight();
                    g2d.drawString(category, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2);
                }
            };

            card.setPreferredSize(new Dimension(300, 200));
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(ColorScheme.LIGHT_PURPLE);
                    card.repaint();
                }

                public void mouseExited(MouseEvent e) {
                    card.setBackground(ColorScheme.MEDIUM_PURPLE);
                    card.repaint();
                }
            });

            return card;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw background image
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }

            // Draw overlay
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(20, 14, 54, 230));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class OtpDialog extends ModalDialog {
        private String generatedOtp;  // Store the generated OTP

        public OtpDialog(JFrame parent, String otp) {
            super(parent, "Enter OTP");
            this.generatedOtp = otp;

            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

            JTextField otpField = new JTextField(6);  // Text field for OTP input
            StyledButton verifyButton = new StyledButton("Verify OTP", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);

            contentPanel.add(new JLabel("Enter the OTP sent to your phone:"));
            contentPanel.add(otpField);
            contentPanel.add(Box.createVerticalStrut(20));
            contentPanel.add(verifyButton);

            verifyButton.addActionListener(e -> {
                String enteredOtp = otpField.getText();
                if (enteredOtp.equals(generatedOtp)) {
                    JOptionPane.showMessageDialog(parent, "OTP verified. Login successful!");
                    dispose();  // Close the OTP dialog
                    // Here you can navigate to the main application after successful login
                } else {
                    JOptionPane.showMessageDialog(parent, "Invalid OTP. Please try again.");
                }
            });
        }
    }

    // Modal dialog base class
    class ModalDialog extends JDialog {
        protected JPanel contentPanel;

        public ModalDialog(JFrame parent, String title) {
            super(parent, title, true);
            setSize(400, 300);
            setLocationRelativeTo(parent);

            contentPanel = new JPanel();
            contentPanel.setBackground(new Color(240, 240, 250)); // Updated background to match image
            contentPanel.setBorder(BorderFactory.createLineBorder(ColorScheme.MEDIUM_PURPLE, 2)); // Border added

            setContentPane(contentPanel);
        }
    }

    // Login dialog
    class LoginDialog extends ModalDialog {
        public LoginDialog(JFrame parent) {
            super(parent, "Log In");

            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

            JTextField username = new JTextField(20);
            JPasswordField password = new JPasswordField(20);
            StyledButton loginButton = new StyledButton("Log In", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);

            contentPanel.add(new JLabel("Username:"));
            contentPanel.add(username);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(new JLabel("Password:"));
            contentPanel.add(password);
            contentPanel.add(Box.createVerticalStrut(20));
            contentPanel.add(loginButton);

            loginButton.addActionListener(e -> {
                // Collect the username and password
                String user = username.getText();
                String pass = new String(password.getPassword());


                // Validate the user credentials
                boolean success = DatabaseConnection.loginUser(user, pass);
                if (success) {
                    // User login successful, now send OTP
                    String otp = OtpVerification.generateOTP(); // Generate OTP
                    String phoneNumber = DatabaseConnection.getPhoneNumber(user); // Get user's phone number

                    if (phoneNumber != null && !phoneNumber.isEmpty()) {
                        OtpVerification.sendOTP(phoneNumber, otp); // Send OTP to user's phone number

                        // Open OTP dialog to enter the OTP
                        OtpDialog otpDialog = new OtpDialog(parent, otp);
                        otpDialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(parent, "Phone number not found for user.");
                    }
                } else {
                    JOptionPane.showMessageDialog(parent, "Invalid credentials. Please try again.");
                }
            });


            dispose();  // Close the dialog

        }
    }

    // Signup dialog
    // Signup dialog
    class SignupDialog extends ModalDialog {
        private ConnectEventsApp parent;
        private JTextField username;
        private JTextField phone;
        private JTextField email;
        private JPasswordField password;
        private JPasswordField confirmPassword;

        public SignupDialog(ConnectEventsApp parent) {
            super(parent, "Sign Up");
            this.parent = parent;
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

            // Initialize the fields
            username = new JTextField(20);
            phone = new JTextField(20);
            email = new JTextField(20);
            password = new JPasswordField(20);
            confirmPassword = new JPasswordField(20);

            // Add components to the content panel
            contentPanel.add(new JLabel("Username:"));
            contentPanel.add(username);
            contentPanel.add(Box.createVerticalStrut(10));

            contentPanel.add(new JLabel("Phone Number:"));
            contentPanel.add(phone);
            contentPanel.add(Box.createVerticalStrut(10));

            contentPanel.add(new JLabel("Email:"));
            contentPanel.add(email);
            contentPanel.add(Box.createVerticalStrut(10));

            contentPanel.add(new JLabel("Password:"));
            contentPanel.add(password);
            contentPanel.add(Box.createVerticalStrut(10));

            contentPanel.add(new JLabel("Confirm Password:"));
            contentPanel.add(confirmPassword);
            contentPanel.add(Box.createVerticalStrut(20));

            // Initialize the sign-up button
            StyledButton signupButton = new StyledButton("Sign Up", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);
            contentPanel.add(signupButton);

            // Action listener for the signup button
            // Inside SignupDialog class
            signupButton.addActionListener(e -> {
                // Collect form data
                String user = username.getText();
                String pass = new String(password.getPassword());
                String confirmPass = new String(confirmPassword.getPassword());
                String emailValue = email.getText();
                String phoneValue = phone.getText();

                // Check if passwords match
                if (!pass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(parent, "Passwords do not match!");
                } else {
                    // Send OTP for phone verification
                    String otp = OtpVerification.generateOTP();
                    OtpVerification.sendOTP(phoneValue, otp);

                    String enteredOtp = JOptionPane.showInputDialog("Enter the OTP you received:");
                    if (otp.equals(enteredOtp)) {
                        // Proceed with signup
                        boolean success = DatabaseConnection.signUpUser(user, pass, emailValue, phoneValue);
                        if (success) {
                            JOptionPane.showMessageDialog(parent, "Sign Up successful! You can now log in.");
                            dispose();

                            // Open Location Dialog
                            JDialog locationDialog = new JDialog(parent, "Enter Your Location", true);
                            locationDialog.setSize(400, 300);
                            locationDialog.setLocationRelativeTo(parent);
                            LocationApp locationApp = new LocationApp(parent) {
                                @Override
                                protected void closeLocationDialog() {
                                    locationDialog.dispose();
                                }
                            };
                            locationDialog.add(locationApp);
                            locationDialog.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(parent, "Sign Up failed. Username might be taken.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(parent, "Invalid OTP. Sign Up failed.");
                    }
                }
            });

        }
    }


    class AdminLoginDialog extends ModalDialog {
        // Assume you have a predefined admin phone number
        private static final String ADMIN_PHONE_NUMBER = "19383483434"; // Replace with actual phone number

        public AdminLoginDialog(JFrame parent) {
            super(parent, "Admin Login");

            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

            JTextField adminUsername = new JTextField(20);  // Variable for admin username
            JPasswordField adminPassword = new JPasswordField(20);
            StyledButton loginButton = new StyledButton("Admin Login", ColorScheme.LIGHT_PURPLE, ColorScheme.DARK_PURPLE);

            contentPanel.add(new JLabel("Admin Username:"));
            contentPanel.add(adminUsername);  // Use the correct variable
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(new JLabel("Admin Password:"));
            contentPanel.add(adminPassword);
            contentPanel.add(Box.createVerticalStrut(20));
            contentPanel.add(loginButton);

            loginButton.addActionListener(e -> {
                // Collect the username and password for admin login
                String adminUser = adminUsername.getText();  // Admin username input
                String adminPass = new String(adminPassword.getPassword());  // Admin password input

                // Check admin login using the DatabaseConnection class
                boolean isValidAdmin = DatabaseConnection.loginUser(adminUser, adminPass);
                if (isValidAdmin) {
                    // Admin login successful, now send OTP
                    String otp = OtpVerification.generateOTP(); // Generate OTP
                    OtpVerification.sendOTP(ADMIN_PHONE_NUMBER, otp); // Send OTP to admin's phone number

                    // Prompt for OTP input
                    String enteredOtp = JOptionPane.showInputDialog("Enter the OTP sent to your phone:");
                    if (otp.equals(enteredOtp)) {
                        JOptionPane.showMessageDialog(parent, "Admin Login successful for " + adminUser);
                        dispose();  // Close the dialog on successful login
                    } else {
                        JOptionPane.showMessageDialog(parent, "Invalid OTP. Login failed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(parent, "Invalid Admin credentials. Please try again.");
                }
            });
        }
    }
}

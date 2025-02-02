import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public ConnectApp() {
        setTitle("CONNECT - Local Events Near You");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Home screen panel
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(46, 7, 63));

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setBackground(new Color(46, 7, 63));
        navbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel logo = new JLabel("CONNECT");
        logo.setForeground(new Color(173, 73, 225));
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        navbar.add(logo);

        // Nav links (could add more as needed)
        JButton btnHome = createNavButton("Home");
        JButton btnAbout = createNavButton("About Us");
        JButton btnEvents = createNavButton("Events");
        navbar.add(btnHome);
        navbar.add(btnAbout);
        navbar.add(btnEvents);

        homePanel.add(navbar, BorderLayout.NORTH);

        // CTA Content
        JPanel ctaPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        ctaPanel.setBackground(new Color(106, 11, 157, 180));
        ctaPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 60, 20));
        ctaPanel.setOpaque(true);

        JLabel ctaTitle = new JLabel("From Screen to Street:");
        ctaTitle.setFont(new Font("Arial", Font.BOLD, 24));
        ctaTitle.setForeground(Color.WHITE);
        ctaPanel.add(ctaTitle);

        JLabel ctaDescription = new JLabel("<html>Discover the hottest concerts, festivals, and local happenings instantly.<br>Your city's live experiences, curated for you in just a few taps.</html>");
        ctaDescription.setForeground(Color.WHITE);
        ctaPanel.add(ctaDescription);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(46, 7, 63));
        JButton btnLogin = createButton("Log In");
        JButton btnSignUp = createButton("Sign Up");
        JButton btnAdminLogin = createButton("Admin Login");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnAdminLogin);

        ctaPanel.add(buttonPanel);
        homePanel.add(ctaPanel, BorderLayout.CENTER);

        mainPanel.add(homePanel, "home");

        // Add modal dialogs for Login, Signup, Admin Login
        createModalDialog("Log In", "Username", "Password", "Log In", this);
        createModalDialog("Sign Up", "Name", "Phone Number", "Next", this);
        createModalDialog("Admin Login", "Admin Username", "Admin Password", "Log In", this);

        add(mainPanel);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(new Color(235, 211, 248));
        button.setBackground(new Color(106, 11, 157));
        button.setBorderPainted(false);
        return button;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(new Color(46, 7, 63));
        button.setBackground(new Color(173, 73, 225));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private void createModalDialog(String title, String label1Text, String label2Text, String buttonText, JFrame parent) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField field1 = new JTextField();
        field1.setBorder(BorderFactory.createTitledBorder(label1Text));

        JTextField field2 = new JTextField();
        field2.setBorder(BorderFactory.createTitledBorder(label2Text));

        JButton button = new JButton(buttonText);
        button.addActionListener(e -> dialog.setVisible(false));

        dialog.add(field1);
        dialog.add(field2);
        dialog.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConnectApp app = new ConnectApp();
            app.setVisible(true);
        });
    }
}
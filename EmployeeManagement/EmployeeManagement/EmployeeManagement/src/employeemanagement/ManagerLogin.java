package employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerLogin extends JFrame {

    private JTextField usernameField;  // TextField for username
    private JPasswordField passwordField;  // PasswordField for password

    public ManagerLogin() {
        setTitle("Manager Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);  // Center the window

        setLayout(new BorderLayout(0, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#FCE0B5"));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Welcome Manager!");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6, 1, 5, 5)); // Modified grid to accommodate the link
        loginPanel.setBackground(Color.decode("#2c67f2"));

        JLabel pleaseLoginLabel = new JLabel("Please Login");
        pleaseLoginLabel.setFont(new Font("Arial", Font.BOLD, 25));
        pleaseLoginLabel.setHorizontalAlignment(JLabel.CENTER);
        pleaseLoginLabel.setForeground(Color.BLACK);
        loginPanel.add(pleaseLoginLabel);

        loginPanel.add(createTextField("Username:", 18, "#FFFFFF", true));
        loginPanel.add(createTextField("Password:", 18, "#FFFFFF", false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(Color.decode("#2c67f2"));
        JButton loginButton = createButtonCustom("Login", "#8694f0", 150, 40);
        JButton mainMenuButton = createButtonCustom("Main Menu", "#8694f0", 150, 40);
        loginButton.addActionListener(e -> openManagerPortal());
        mainMenuButton.addActionListener(e -> backToMainMenu());
        buttonPanel.add(loginButton);
        buttonPanel.add(mainMenuButton);

        loginPanel.add(buttonPanel);

        JLabel signupLabel = new JLabel("I don't have an account");
        signupLabel.setForeground(Color.WHITE);  // Set text color to white
        signupLabel.setFont(new Font("Arial", Font.BOLD, 14));
        signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openSignup();
            }
        });
        loginPanel.add(signupLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private JPanel createTextField(String label, int fontSize, String bgColor, boolean isUsername) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.decode("#2c67f2"));  // Uniform background color
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        JTextField textField;
        if (isUsername) {
            textField = new JTextField(15);
            usernameField = (JTextField) textField;
        } else {
            textField = new JPasswordField(15);
            passwordField = (JPasswordField) textField;
        }
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(Color.decode(bgColor));  // Specific color for the text field background
        textField.setPreferredSize(new Dimension(120, 30));
        panel.add(jLabel);
        panel.add(textField);
        return panel;
    }

    private JButton createButtonCustom(String text, String color, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.decode(color));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        return button;
    }

    private void openManagerPortal() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticateManager(username, password)) {
            this.dispose();
            new Manager().setVisible(true);  // Ensure this is properly defined
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticateManager(String username, String password) {
        String query = "SELECT * FROM Manager WHERE Username = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void backToMainMenu() {
        this.dispose();
        new Main().setVisible(true);
    }

    private void openSignup() {
        try {
            Signup signupWindow = new Signup();
            signupWindow.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to open the signup window: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerLogin().setVisible(true));
    }
}

package employeemanagement;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signup extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Signup() {
        setTitle("Manager Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);  // Center the window

        setLayout(new BorderLayout(0, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#FCE0B5"));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Create Manager Account");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6, 1, 5, 5));
        loginPanel.setBackground(Color.decode("#2c67f2"));

        JLabel pleaseLoginLabel = new JLabel("Please Enter Details");
        pleaseLoginLabel.setFont(new Font("Arial", Font.BOLD, 25));
        pleaseLoginLabel.setHorizontalAlignment(JLabel.CENTER);
        pleaseLoginLabel.setForeground(Color.BLACK);
        loginPanel.add(pleaseLoginLabel);

        loginPanel.add(createTextField("Username:", 18, "#FFFFFF", true));
        loginPanel.add(createTextField("Password:", 18, "#FFFFFF", false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(Color.decode("#2c67f2"));
        JButton createAccountButton = createButtonCustom("Create Account", "#8694f0", 200, 40);
        createAccountButton.addActionListener(e -> registerManager());
        buttonPanel.add(createAccountButton);

        loginPanel.add(buttonPanel);

        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backToLogin();
            }
        });
        loginPanel.add(loginLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private JPanel createTextField(String label, int fontSize, String bgColor, boolean isUsername) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.decode("#2c67f2"));
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
        textField.setBackground(Color.decode(bgColor));
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

    private void registerManager() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String query = "INSERT INTO Manager (Username, Password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                backToLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create account", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        new ManagerLogin().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Signup().setVisible(true));
    }
}

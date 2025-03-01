package employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeSignup extends JFrame {

    private JTextField usernameField;  // TextField for username
    private JPasswordField passwordField;  // PasswordField for password

    public EmployeeSignup() {
        setTitle("Employee Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);  // Center the window

        setLayout(new BorderLayout(0, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#FCE0B5"));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Create Employee Account");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6, 1, 5, 5));
        loginPanel.setBackground(Color.decode("#2c67f2"));

        JLabel pleaseEnterDetailsLabel = new JLabel("Please Enter Details");
        pleaseEnterDetailsLabel.setFont(new Font("Arial", Font.BOLD, 25));
        pleaseEnterDetailsLabel.setHorizontalAlignment(JLabel.CENTER);
        pleaseEnterDetailsLabel.setForeground(Color.BLACK);
        loginPanel.add(pleaseEnterDetailsLabel);

        loginPanel.add(createTextField("Username:", 18, "#FFFFFF", true));
        loginPanel.add(createTextField("Password:", 18, "#FFFFFF", false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBackground(Color.decode("#2c67f2"));
        JButton createAccountButton = createButtonCustom("Create Account", "#8694f0", 200, 40);
        createAccountButton.addActionListener(e -> registerEmployee());
        buttonPanel.add(createAccountButton);

        loginPanel.add(buttonPanel);

        JLabel backToLoginLabel = new JLabel("Already have an account?");
        backToLoginLabel.setForeground(Color.WHITE);
        backToLoginLabel.setFont(new Font("Arial", Font.BOLD, 14));
        backToLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backToLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backToLogin();
            }
        });
        loginPanel.add(backToLoginLabel);

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

    private void registerEmployee() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String query = "INSERT INTO employee (Employee_Name, Password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Employee account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                backToLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create employee account", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        new EmployeeLogin().setVisible(true); // Assuming ManagerLogin is the main login screen for all users
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeSignup().setVisible(true));
    }
}

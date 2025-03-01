package employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public EmployeeLogin() {
        setTitle("Employee Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#FCE0B5"));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Welcome Employee!");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6, 1, 5, 5));
        loginPanel.setBackground(Color.decode("#2c67f2"));

        JLabel pleaseLoginLabel = new JLabel("Please Login");
        pleaseLoginLabel.setFont(new Font("Arial", Font.BOLD, 25));
        pleaseLoginLabel.setHorizontalAlignment(JLabel.CENTER);
        pleaseLoginLabel.setForeground(Color.BLACK);
        loginPanel.add(pleaseLoginLabel);

        loginPanel.add(createTextField("Username:", 18));
        loginPanel.add(createPasswordField("Password:", 18));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.decode("#2c67f2"));
        JButton loginButton = createButtonCustom("Login", "#8694f0", 150, 40);
        loginButton.addActionListener(this::authenticateEmployee);
        JButton mainMenuButton = createButtonCustom("Main Menu", "#8694f0", 150, 40);
        mainMenuButton.addActionListener(e -> backToMainMenu());
        buttonPanel.add(loginButton);
        buttonPanel.add(mainMenuButton);

        loginPanel.add(buttonPanel);

        JLabel noAccountLabel = new JLabel("I don't have an account");
        noAccountLabel.setForeground(Color.WHITE);
        noAccountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        noAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openSignup();
            }
        });
        loginPanel.add(noAccountLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);

        validate();
        repaint();
    }

    private JPanel createTextField(String label, int fontSize) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.decode("#2c67f2"));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(jLabel);
        panel.add(textField);
        if (label.startsWith("Username")) {
            usernameField = textField;
        }
        return panel;
    }

    private JPanel createPasswordField(String label, int fontSize) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.decode("#2c67f2"));
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(jLabel);
        panel.add(passwordField);
        if (label.startsWith("Password")) {
            this.passwordField = passwordField;
        }
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

    private void authenticateEmployee(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String query = "SELECT * FROM employee WHERE  Employee_Name = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                // new EmployeeDashboard().setVisible(true); // Assuming such a class exists
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToMainMenu() {
        this.dispose();
        new Main().setVisible(true); // Assuming Main is the main menu class
    }

    private void openSignup() {
        new EmployeeSignup().setVisible(true); // Assuming EmployeeSignup class exists
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeLogin().setVisible(true));
    }
}

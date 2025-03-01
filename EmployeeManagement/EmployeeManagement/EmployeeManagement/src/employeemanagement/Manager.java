package employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Manager extends JFrame {

    private JPanel titlePanel;
    private JPanel buttonPanel;
    private JButton employeeDatabaseButton;
    private JButton scheduleButton;
    private JLabel titleLabel;

    public Manager() {
        initComponents();
    }

    private void initComponents() {

        // Set JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 410);
        setLocationRelativeTo(null);

        // Initialize panels
        titlePanel = new JPanel();
        buttonPanel = new JPanel();

        // Initialize buttons
        employeeDatabaseButton = new JButton();
        scheduleButton = new JButton();

        // Title Panel setup
        titlePanel.setBackground(new java.awt.Color(252, 224, 181)); // Pale yellow background
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        titleLabel = new JLabel();
        titleLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 50)); // Title font
        titleLabel.setForeground(new java.awt.Color(0, 0, 0)); // Black text
        titleLabel.setText("Manager Portal");

        titlePanel.add(titleLabel);

        // Button Panel setup
        buttonPanel.setBackground(new java.awt.Color(44, 103, 242)); // Blue background
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 80)); // Adjusted button spacing

        // Employee Database Button setup
        employeeDatabaseButton.setFont(new java.awt.Font("Arial", Font.BOLD, 20)); // Font for button
        employeeDatabaseButton.setText("Employee Database");
        employeeDatabaseButton.setBackground(new java.awt.Color(252, 224, 181)); // Pale yellow
        employeeDatabaseButton.setForeground(new java.awt.Color(0, 0, 0));
        employeeDatabaseButton.setPreferredSize(new java.awt.Dimension(250, 100)); // Button size
        employeeDatabaseButton.setBorderPainted(false);
        employeeDatabaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEmployeeDatabase();
            }
        });

        // Schedule Button setup
        scheduleButton.setFont(new java.awt.Font("Arial", Font.BOLD, 20)); // Font for button
        scheduleButton.setText("Schedule");
        scheduleButton.setBackground(new java.awt.Color(252, 224, 181)); // Pale yellow
        scheduleButton.setForeground(new java.awt.Color(0, 0, 0));
        scheduleButton.setPreferredSize(new java.awt.Dimension(250, 100)); // Button size
        scheduleButton.setBorderPainted(false);
        scheduleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSchedulePanel();
            }
        });

        // Add buttons to the panel
        buttonPanel.add(employeeDatabaseButton);
        buttonPanel.add(scheduleButton);

        // Add panels to JFrame
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack(); // Adjust layout
    }

    private void openEmployeeDatabase() {
        // Close the current Manager window and open Employee Database window
        this.dispose();
        new Employee().setVisible(true);  // Open the Employee database window
    }

    private void openSchedulePanel() {
        // Logic to open the Schedule management panel
        System.out.println("Opening Schedule Management Panel...");
        // You can add the actual window or functionality here
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manager().setVisible(true);
            }
        });
    }
}

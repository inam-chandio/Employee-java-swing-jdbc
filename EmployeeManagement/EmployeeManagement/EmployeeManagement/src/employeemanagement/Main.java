package employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends javax.swing.JFrame {

    public Main() {
        initComponents();
    }

    // Generated code by NetBeans GUI Builder
    private void initComponents() {

        // Create panels and buttons programmatically or using the designer (NetBeans does this automatically).
        titlePanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        managerButton = new javax.swing.JButton();
        employeeButton = new javax.swing.JButton();

        // Set up JFrame properties
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(720, 410); // Adjusted to a more suitable size
        setLocationRelativeTo(null); // Center the window

        // Title Panel setup
        titlePanel.setBackground(new java.awt.Color(252, 224, 181)); // Pale yellow background
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        titleLabel = new javax.swing.JLabel();
        titleLabel.setFont(new java.awt.Font("Arial", 1, 50)); // Font for the title
        titleLabel.setForeground(new java.awt.Color(0, 0, 0)); // Black text
        titleLabel.setText("Welcome!");

        titlePanel.add(titleLabel);

        // Button Panel setup
        buttonPanel.setBackground(new java.awt.Color(44, 103, 242)); // Blue background
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 80)); // Increased spacing

        // Manager Button setup
        managerButton.setFont(new java.awt.Font("Arial", 1, 20));
        managerButton.setText("Manager Portal");
        managerButton.setBackground(new java.awt.Color(252, 224, 181)); // Pale yellow
        managerButton.setForeground(new java.awt.Color(0, 0, 0));
        managerButton.setPreferredSize(new java.awt.Dimension(250, 100)); // Button size
        managerButton.setBorderPainted(false);
        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openManagerLoginPanel();
            }
        });

        // Employee Button setup
        employeeButton.setFont(new java.awt.Font("Arial", 1, 20));
        employeeButton.setText("Employee Portal");
        employeeButton.setBackground(new java.awt.Color(252, 224, 181)); // Pale yellow
        employeeButton.setForeground(new java.awt.Color(0, 0, 0));
        employeeButton.setPreferredSize(new java.awt.Dimension(250, 100)); // Button size
        employeeButton.setBorderPainted(false);
        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEmployeeLoginPanel();
            }
        });

        // Adding buttons to the panel
        buttonPanel.add(managerButton);
        buttonPanel.add(employeeButton);

        // Add panels to JFrame
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack(); // Adjust the layout of components
    }

    // Action method for Manager Button
    private void openManagerLoginPanel() {
        // Create and show the ManagerLogin panel
        new ManagerLogin().setVisible(true);
        this.setVisible(false);  // Hide the current window
    }

    // Action method for Employee Button
    private void openEmployeeLoginPanel() {
        // Create and show the EmployeeLogin panel
        new EmployeeLogin().setVisible(true);
        this.setVisible(false);  // Hide the current window
    }

    // Main method
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel titlePanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton managerButton;
    private javax.swing.JButton employeeButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration
}

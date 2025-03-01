package employeemanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class Employee extends JFrame {
    private JTextField searchIdField;
    private JTextField searchNameField;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton deleteBtn;

    public Employee() {
        setTitle("Employee Database!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.decode("#FCE0B5"));
        titlePanel.setPreferredSize(new Dimension(1400, 100));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Employee Database!");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 1, 5, 5));
        controlPanel.setBackground(Color.decode("#2c67f2"));
        controlPanel.setPreferredSize(new Dimension(1400, 150));

        // Search panels
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        row1.setBackground(Color.decode("#2c67f2"));
        searchIdField = new JTextField(15);
        searchIdField.setPreferredSize(new Dimension(150, 30));
        JLabel searchIdLabel = new JLabel("Search Employee ID:      ");
        searchIdLabel.setForeground(Color.BLACK);
        searchIdLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Change 16 to your preferred size

        row1.add(searchIdLabel);
        row1.add(searchIdField);
        JButton addBtn = createButtonCustom("Add", "#8694f0", 120, 30);
        addBtn.addActionListener(e -> addNewEmployee());
        row1.add(addBtn);
        controlPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        row2.setBackground(Color.decode("#2c67f2"));
        searchNameField = new JTextField(15);
        searchNameField.setPreferredSize(new Dimension(150, 30));
        JLabel searchNameLabel = new JLabel("Search Employee Name:");
        searchNameLabel.setForeground(Color.BLACK);
        searchNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        row2.add(searchNameLabel);
        row2.add(searchNameField);
        deleteBtn = createButtonCustom("Delete", "#8694f0", 120, 30);
        row2.add(deleteBtn);
        controlPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        row3.setBackground(Color.decode("#2c67f2"));
        JButton mainMenuBtn = createButtonCustom("Main Menu", "#8694f0", 120, 30);
        JButton searchBtn = createButtonCustom("Search", "#8694f0", 120, 30);
        JButton editBtn = createButtonCustom("Edit", "#8694f0", 120, 30);
        row3.add(mainMenuBtn);
        row3.add(searchBtn);
        row3.add(editBtn);
        controlPanel.add(row3);

        // Table Panel
        String[] columnNames = {
                "<html><center>Employee<br>ID</center></html>",
                "<html><center>Employee<br>Name</center></html>",
                "<html><center>Employee<br>Email</center></html>",
                "<html><center>Employee<br>Number</center></html>",
                "<html><center>Employee<br>Position</center></html>",
                "<html><center>Employee<br>Salary</center></html>",
                "<html><center>Passport<br>Number</center></html>",
                "<html><center>Passport<br>Expiry</center></html>",
                "<html><center>Emirates<br>ID</center></html>",
                "<html><center>Emirates ID<br>Expiry</center></html>"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        employeeTable.setRowHeight(30);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure table header
        JTableHeader header = employeeTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(Color.decode("#8694f0"));
        header.setForeground(Color.BLACK);

        // Set column widths proportionally
        int totalWidth = 1380;
        int[] columnWidths = {8, 12, 15, 10, 10, 8, 10, 10, 9, 8}; // Percentages
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = employeeTable.getColumnModel().getColumn(i);
            column.setPreferredWidth((totalWidth * columnWidths[i]) / 100);
        }

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(1400, 500));

        // Add components to frame
        add(titlePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Add button listeners
        mainMenuBtn.addActionListener(e -> backToMainMenu());
        searchBtn.addActionListener(e -> searchEmployees());
        editBtn.addActionListener(e -> editSelectedEmployee());
        deleteBtn.addActionListener(e -> deleteSelectedEmployee());

        // Load initial data
        loadEmployeeData();
        pack();
    }

    private JButton createButtonCustom(String text, String color, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.decode(color));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        return button;
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT e.Employee_ID, e.Employee_Name, e.Employee_Email, e.Employee_Number,
                       ep.Employee_Position, ep.Employee_Salary,
                       pass.Employee_Passport_Number, pass.Employee_Passport_Expiry_Date,
                       eid.Employee_Emirates_ID_Number, eid.Employee_Emirates_ID_Expiry_Date
                FROM Employee e
                LEFT JOIN Employee_Position ep ON e.Employee_ID = ep.Employee_ID
                LEFT JOIN Employee_Passport pass ON e.Employee_ID = pass.Employee_ID
                LEFT JOIN Employee_Emirates_ID eid ON e.Employee_ID = eid.Employee_ID
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("Employee_ID"));
                    row.add(rs.getString("Employee_Name"));
                    row.add(rs.getString("Employee_Email"));
                    row.add(rs.getLong("Employee_Number"));
                    row.add(rs.getString("Employee_Position"));
                    row.add(rs.getInt("Employee_Salary"));
                    row.add(rs.getLong("Employee_Passport_Number"));
                    row.add(rs.getDate("Employee_Passport_Expiry_Date"));
                    row.add(rs.getLong("Employee_Emirates_ID_Number"));
                    row.add(rs.getDate("Employee_Emirates_ID_Expiry_Date"));
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage());
        }
    }

    private void searchEmployees() {
        String idSearch = searchIdField.getText().trim();
        String nameSearch = searchNameField.getText().trim();

        try (Connection conn = DatabaseConnection.getConnection()) {
            StringBuilder queryBuilder = new StringBuilder("""
                SELECT e.Employee_ID, e.Employee_Name, e.Employee_Email, e.Employee_Number,
                       ep.Employee_Position, ep.Employee_Salary,
                       pass.Employee_Passport_Number, pass.Employee_Passport_Expiry_Date,
                       eid.Employee_Emirates_ID_Number, eid.Employee_Emirates_ID_Expiry_Date
                FROM Employee e
                LEFT JOIN Employee_Position ep ON e.Employee_ID = ep.Employee_ID
                LEFT JOIN Employee_Passport pass ON e.Employee_ID = pass.Employee_ID
                LEFT JOIN Employee_Emirates_ID eid ON e.Employee_ID = eid.Employee_ID
                WHERE 1=1
            """);

            if (!idSearch.isEmpty()) {
                queryBuilder.append(" AND e.Employee_ID = ?");
            }
            if (!nameSearch.isEmpty()) {
                queryBuilder.append(" AND e.Employee_Name LIKE ?");
            }

            try (PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
                int paramIndex = 1;
                if (!idSearch.isEmpty()) {
                    stmt.setInt(paramIndex++, Integer.parseInt(idSearch));
                }
                if (!nameSearch.isEmpty()) {
                    stmt.setString(paramIndex, "%" + nameSearch + "%");
                }

                ResultSet rs = stmt.executeQuery();
                tableModel.setRowCount(0);

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("Employee_ID"));
                    row.add(rs.getString("Employee_Name"));
                    row.add(rs.getString("Employee_Email"));
                    row.add(rs.getLong("Employee_Number"));
                    row.add(rs.getString("Employee_Position"));
                    row.add(rs.getInt("Employee_Salary"));
                    row.add(rs.getLong("Employee_Passport_Number"));
                    row.add(rs.getDate("Employee_Passport_Expiry_Date"));
                    row.add(rs.getLong("Employee_Emirates_ID_Number"));
                    row.add(rs.getDate("Employee_Emirates_ID_Expiry_Date"));
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching employees: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Employee ID number");
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Delete from child tables first
                String[] deleteQueries = {
                        "DELETE FROM Employee_Emirates_ID WHERE Employee_ID = ?",
                        "DELETE FROM Employee_Passport WHERE Employee_ID = ?",
                        "DELETE FROM Employee_Position WHERE Employee_ID = ?",
                        "DELETE FROM Employee WHERE Employee_ID = ?"
                };

                for (String query : deleteQueries) {
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, employeeId);
                        stmt.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                loadEmployeeData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting employee: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.");
            return;
        }

        JDialog editDialog = new JDialog(this, "Edit Employee", true);
        editDialog.setSize(800, 600);
        editDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Create fields for all employee data
        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        JTextField nameField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString());
        JTextField emailField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
        JTextField numberField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
        JTextField positionField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
        JTextField salaryField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());
        JTextField passportField = new JTextField(tableModel.getValueAt(selectedRow, 6).toString());
        JTextField passportExpiryField = new JTextField(tableModel.getValueAt(selectedRow, 7).toString());
        JTextField emiratesIdField = new JTextField(tableModel.getValueAt(selectedRow, 8).toString());
        JTextField emiratesExpiryField = new JTextField(tableModel.getValueAt(selectedRow, 9).toString());

        // Add fields to panel
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);
        fieldsPanel.add(new JLabel("Phone Number:"));
        fieldsPanel.add(numberField);
        fieldsPanel.add(new JLabel("Position:"));
        fieldsPanel.add(positionField);
        fieldsPanel.add(new JLabel("Salary:"));
        fieldsPanel.add(salaryField);
        fieldsPanel.add(new JLabel("Passport Number:"));
        fieldsPanel.add(passportField);
        fieldsPanel.add(new JLabel("Passport Expiry (YYYY-MM-DD):"));
        fieldsPanel.add(passportExpiryField);
        fieldsPanel.add(new JLabel("Emirates ID:"));
        fieldsPanel.add(emiratesIdField);
        fieldsPanel.add(new JLabel("Emirates ID Expiry (YYYY-MM-DD):"));
        fieldsPanel.add(emiratesExpiryField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false); // Start transaction
                try {
                    // Update Employee table
                    String updateEmployee = "UPDATE Employee SET Employee_Name=?, Employee_Email=?, Employee_Number=? WHERE Employee_ID=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateEmployee)) {
                        stmt.setString(1, nameField.getText());
                        stmt.setString(2, emailField.getText());
                        stmt.setLong(3, Long.parseLong(numberField.getText()));
                        stmt.setInt(4, employeeId);
                        stmt.executeUpdate();
                    }

                    // Update Position
                    String updatePosition = "UPDATE Employee_Position SET Employee_Position=?, Employee_Salary=? WHERE Employee_ID=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updatePosition)) {
                        stmt.setString(1, positionField.getText());
                        stmt.setInt(2, Integer.parseInt(salaryField.getText()));
                        stmt.setInt(3, employeeId);
                        stmt.executeUpdate();
                    }

                    // Update Passport
                    String updatePassport = "UPDATE Employee_Passport SET Employee_Passport_Number=?, Employee_Passport_Expiry_Date=? WHERE Employee_ID=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updatePassport)) {
                        stmt.setLong(1, Long.parseLong(passportField.getText()));
                        stmt.setDate(2, java.sql.Date.valueOf(passportExpiryField.getText()));
                        stmt.setInt(3, employeeId);
                        stmt.executeUpdate();
                    }

                    // Update Emirates ID
                    String updateEmiratesId = "UPDATE Employee_Emirates_ID SET Employee_Emirates_ID_Number=?, Employee_Emirates_ID_Expiry_Date=? WHERE Employee_ID=?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateEmiratesId)) {
                        stmt.setLong(1, Long.parseLong(emiratesIdField.getText()));
                        stmt.setDate(2, java.sql.Date.valueOf(emiratesExpiryField.getText()));
                        stmt.setInt(3, employeeId);
                        stmt.executeUpdate();
                    }

                    conn.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(editDialog, "Employee information updated successfully!");
                    editDialog.dispose();
                    loadEmployeeData(); // Refresh the table
                } catch (Exception ex) {
                    conn.rollback(); // Rollback on error
                    throw ex;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(editDialog,
                        "Error updating employee information: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog,
                        "Please enter valid numbers for numeric fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(editDialog,
                        "Please enter valid dates in YYYY-MM-DD format",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        editDialog.add(mainPanel);
        editDialog.setVisible(true);
    }

    private void backToMainMenu() {
        this.dispose();
        new Main().setVisible(true);
    }

    private void addNewEmployee() {
        JDialog addDialog = new JDialog(this, "Add New Employee", true);
        addDialog.setSize(800, 600);
        addDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Create fields for new employee data
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField numberField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField passportField = new JTextField();
        JTextField passportExpiryField = new JTextField();
        JTextField emiratesIdField = new JTextField();
        JTextField emiratesExpiryField = new JTextField();

        // Add fields to panel with labels
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);
        fieldsPanel.add(new JLabel("Phone Number:"));
        fieldsPanel.add(numberField);
        fieldsPanel.add(new JLabel("Position:"));
        fieldsPanel.add(positionField);
        fieldsPanel.add(new JLabel("Salary:"));
        fieldsPanel.add(salaryField);
        fieldsPanel.add(new JLabel("Passport Number:"));
        fieldsPanel.add(passportField);
        fieldsPanel.add(new JLabel("Passport Expiry (YYYY-MM-DD):"));
        fieldsPanel.add(passportExpiryField);
        fieldsPanel.add(new JLabel("Emirates ID:"));
        fieldsPanel.add(emiratesIdField);
        fieldsPanel.add(new JLabel("Emirates ID Expiry (YYYY-MM-DD):"));
        fieldsPanel.add(emiratesExpiryField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false); // Start transaction
                try {
                    // Insert into Employee table first to get the ID
                    String insertEmployee = "INSERT INTO Employee (Employee_Name, Employee_Email, Employee_Number) VALUES (?, ?, ?)";
                    int employeeId;

                    try (PreparedStatement stmt = conn.prepareStatement(insertEmployee, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, nameField.getText());
                        stmt.setString(2, emailField.getText());
                        stmt.setLong(3, Long.parseLong(numberField.getText()));
                        stmt.executeUpdate();

                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                employeeId = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("Failed to get generated employee ID.");
                            }
                        }
                    }

                    // Insert into Position table
                    String insertPosition = "INSERT INTO Employee_Position (Employee_ID, Employee_Position, Employee_Salary) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertPosition)) {
                        stmt.setInt(1, employeeId);
                        stmt.setString(2, positionField.getText());
                        stmt.setInt(3, Integer.parseInt(salaryField.getText()));
                        stmt.executeUpdate();
                    }

                    // Insert into Passport table
                    String insertPassport = "INSERT INTO Employee_Passport (Employee_ID, Employee_Passport_Number, Employee_Passport_Expiry_Date) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertPassport)) {
                        stmt.setInt(1, employeeId);
                        stmt.setLong(2, Long.parseLong(passportField.getText()));
                        stmt.setDate(3, java.sql.Date.valueOf(passportExpiryField.getText()));
                        stmt.executeUpdate();
                    }

                    // Insert into Emirates ID table
                    String insertEmiratesId = "INSERT INTO Employee_Emirates_ID (Employee_ID, Employee_Emirates_ID_Number, Employee_Emirates_ID_Expiry_Date) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertEmiratesId)) {
                        stmt.setInt(1, employeeId);
                        stmt.setLong(2, Long.parseLong(emiratesIdField.getText()));
                        stmt.setDate(3, java.sql.Date.valueOf(emiratesExpiryField.getText()));
                        stmt.executeUpdate();
                    }

                    conn.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(addDialog, "New employee added successfully!");
                    addDialog.dispose();
                    loadEmployeeData(); // Refresh the table
                } catch (Exception ex) {
                    conn.rollback(); // Rollback on error
                    throw ex;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(addDialog,
                        "Error adding new employee: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog,
                        "Please enter valid numbers for numeric fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(addDialog,
                        "Please enter valid dates in YYYY-MM-DD format",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> addDialog.dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        addDialog.add(mainPanel);
        addDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Employee().setVisible(true);
        });
    }}
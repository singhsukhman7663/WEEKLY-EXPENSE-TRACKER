import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Weekly Expenditure Tracker GUI Application
 * This application allows users to track their weekly expenses with categorization,
 * validation, and reporting features.
 */
public class WeeklyExpenseTracker extends JFrame {
    
    // GUI Components
    private JComboBox<String> dayComboBox;
    private JComboBox<String> categoryComboBox;
    private JTextField amountField;
    private JTextArea descriptionArea;
    private JButton addButton;
    private JButton totalButton;
    private JButton viewByCategoryButton;
    private JButton clearButton;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    
    // Data Storage
    private List<Expense> expenses;
    private DecimalFormat decimalFormat;
    
    // Constants
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", 
                                        "Friday", "Saturday", "Sunday"};
    private static final String[] CATEGORIES = {"Groceries", "Eating Out", "Petrol", 
                                              "Taxi", "Bills", "Rent", "Others"};
    
    /**
     * Expense data class to store individual expense records
     */
    private static class Expense {
        String day;
        String category;
        double amount;
        String description;
        
        Expense(String day, String category, double amount, String description) {
            this.day = day;
            this.category = category;
            this.amount = amount;
            this.description = description;
        }
        
        @Override
        public String toString() {
            return day + " | " + category + " | $" + String.format("%.2f", amount) + 
                   " | " + description;
        }
    }
    
    /**
     * Constructor - Initializes the GUI and data structures
     */
    public WeeklyExpenseTracker() {
        // Initialize data structures
        expenses = new ArrayList<>();
        decimalFormat = new DecimalFormat("#,##0.00");
        
        // Set up main window
        setTitle("Weekly Expenditure Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Center the window
        
        initComponents();
        setupLayout();
        
        // Add action listeners
        addActionListeners();
    }
    
    /**
     * Initialize all GUI components
     */
    private void initComponents() {
        // Day ComboBox
        dayComboBox = new JComboBox<>(DAYS);
        dayComboBox.setPreferredSize(new Dimension(120, 25));
        
        // Category ComboBox
        categoryComboBox = new JComboBox<>(CATEGORIES);
        categoryComboBox.setPreferredSize(new Dimension(120, 25));
        
        // Amount TextField with validation
        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(100, 25));
        
        // Description TextArea
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        // Buttons
        addButton = new JButton("Add Expense");
        totalButton = new JButton("Total Weekly Expenses");
        viewByCategoryButton = new JButton("View by Category");
        clearButton = new JButton("Clear All");
        
        // Table for displaying expenses
        String[] columns = {"Day", "Category", "Amount", "Description"};
        tableModel = new DefaultTableModel(columns, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        expenseTable.setFillsViewportHeight(true);
        
        // Style buttons - FIXED: Black text on colored backgrounds
        styleButton(addButton, Color.GREEN);
        styleButton(totalButton, Color.BLUE);
        styleButton(viewByCategoryButton, Color.ORANGE);
        styleButton(clearButton, Color.RED);
    }
    
    /**
     * Style buttons with colors and BLACK TEXT (Updated)
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);  // ✅ CHANGED TO BLACK TEXT
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createRaisedBevelBorder());  // Added nice border
    }
    
    /**
     * Setup the main layout using BorderLayout
     */
    private void setupLayout() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Day
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Day:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(dayComboBox, gbc);
        
        // Category
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(categoryComboBox, gbc);
        
        // Amount
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.gridheight = 2;
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(300, 60));
        inputPanel.add(descScroll, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridheight = 1;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(totalButton, gbc);
        gbc.gridx = 2;
        inputPanel.add(viewByCategoryButton, gbc);
        gbc.gridx = 3;
        inputPanel.add(clearButton, gbc);
        
        // Table Panel
        JScrollPane tableScroll = new JScrollPane(expenseTable);
        
        // Main layout
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        
        // Status label
        JLabel statusLabel = new JLabel("Ready to track expenses...", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Add action listeners to all interactive components
     */
    private void addActionListeners() {
        // Add expense button
        addButton.addActionListener(e -> addExpense());
        
        // Total expenses button
        totalButton.addActionListener(e -> showTotalExpenses());
        
        // View by category button
        viewByCategoryButton.addActionListener(e -> showExpensesByCategory());
        
        // Clear all button
        clearButton.addActionListener(e -> clearAllExpenses());
        
        // Amount field validation (only numbers and decimal)
        amountField.addActionListener(e -> validateAmountInput());
    }
    
    /**
     * Add a new expense with validation
     */
    private void addExpense() {
        try {
            // Validate amount input
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an amount!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                amountField.setText("");
                return;
            }
            
            // Get other inputs
            String day = (String) dayComboBox.getSelectedItem();
            String category = (String) categoryComboBox.getSelectedItem();
            String description = descriptionArea.getText().trim();
            
            // Validate description (optional but recommended)
            if (description.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Description is empty. Continue anyway?", "Confirm", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Create and add expense
            Expense expense = new Expense(day, category, amount, description);
            expenses.add(expense);
            
            // Add to table
            tableModel.addRow(new Object[]{
                day, category, decimalFormat.format(amount), description
            });
            
            // Clear inputs
            clearInputs();
            
            JOptionPane.showMessageDialog(this, 
                "Expense added successfully!\nTotal expenses: " + 
                expenses.size(), "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid amount (e.g., 25.50)!", 
                "Invalid Amount", JOptionPane.ERROR_MESSAGE);
            amountField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "An unexpected error occurred: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Calculate and display total weekly expenses
     */
    private void showTotalExpenses() {
        if (expenses.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No expenses recorded yet!", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        double total = expenses.stream()
                              .mapToDouble(exp -> exp.amount)
                              .sum();
        
        String message = "📊 WEEKLY EXPENSE SUMMARY 📊\n\n" +
                        "Total Expenses: $" + decimalFormat.format(total) + "\n" +
                        "Number of transactions: " + expenses.size() + "\n\n" +
                        "Average daily spend: $" + decimalFormat.format(total / 7);
        
        JOptionPane.showMessageDialog(this, message, "Total Expenses", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show expenses grouped by category
     */
    private void showExpensesByCategory() {
        if (expenses.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No expenses recorded yet!", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Group expenses by category
        Map<String, List<Expense>> categoryMap = new TreeMap<>();
        Map<String, Double> categoryTotals = new TreeMap<>();
        
        for (Expense exp : expenses) {
            categoryMap.computeIfAbsent(exp.category, k -> new ArrayList<>()).add(exp);
            categoryTotals.merge(exp.category, exp.amount, Double::sum);
        }
        
        // Build report
        StringBuilder report = new StringBuilder("📈 EXPENSES BY CATEGORY 📈\n\n");
        double grandTotal = 0;
        
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            String category = entry.getKey();
            double total = entry.getValue();
            grandTotal += total;
            report.append("💰 ").append(category).append(":\n")
                  .append("   Total: $").append(decimalFormat.format(total)).append("\n")
                  .append("   Items: ").append(categoryMap.get(category).size()).append("\n\n");
        }
        
        report.append("==================\n");
        report.append("GRAND TOTAL: $").append(decimalFormat.format(grandTotal));
        
        // Show in dialog with scrollable text area
        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
                                    "Expenses by Category", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Clear all expenses and reset table
     */
    private void clearAllExpenses() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete ALL expenses?\nThis action cannot be undone!", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            expenses.clear();
            tableModel.setRowCount(0);
            clearInputs();
            JOptionPane.showMessageDialog(this, "All expenses cleared!", 
                                        "Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Clear input fields
     */
    private void clearInputs() {
        amountField.setText("");
        descriptionArea.setText("");
        dayComboBox.setSelectedIndex(0);
        categoryComboBox.setSelectedIndex(0);
        amountField.requestFocus();
    }
    
    /**
     * Validate amount input format
     */
    private void validateAmountInput() {
        String text = amountField.getText().trim();
        if (!text.matches("\\d*\\.?\\d*")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter numbers only (e.g., 25.50)", 
                "Invalid Format", JOptionPane.WARNING_MESSAGE);
            amountField.setText("");
        }
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the GUI
        SwingUtilities.invokeLater(() -> {
            new WeeklyExpenseTracker().setVisible(true);
        });
    }
}
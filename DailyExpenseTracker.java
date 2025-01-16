import java.io.*;
import java.util.*;

public class DailyExpenseTracker {

    // Define the Expense class
    static class Expense {
        private double amount;
        private String category;
        private String description;

        // Constructor
        public Expense(double amount, String category, String description) {
            this.amount = amount;
            this.category = category;
            this.description = description;
        }

        // Getters
        public double getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Amount: " + amount + ", Category: " + category + ", Description: " + description;
        }
    }

    // Define the ExpenseManager class
    static class ExpenseManager {
        private List<Expense> expenses = new ArrayList<>();
        private final String FILE_NAME = "expenses.txt";

        // Constructor to load data from file
        public ExpenseManager() {
            loadExpenses();
        }

        // Add an expense
        public void addExpense(Expense expense) {
            expenses.add(expense);
            saveExpenses();
        }

        // View all expenses
        public void viewExpenses() {
            if (expenses.isEmpty()) {
                System.out.println("No expenses recorded yet!");
            } else {
                System.out.println("\n=== Recorded Expenses ===");
                double total = 0;
                for (Expense expense : expenses) {
                    System.out.println(expense);
                    total += expense.getAmount();
                }
                System.out.println("Total Expenses: " + total);
            }
        }

        // View expenses by category
        public void viewExpensesByCategory() {
            if (expenses.isEmpty()) {
                System.out.println("No expenses recorded yet!");
            } else {
                System.out.println("\n=== Expenses by Category ===");
                Map<String, Double> categoryTotals = new HashMap<>();
                for (Expense expense : expenses) {
                    categoryTotals.put(expense.getCategory(),
                            categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
                }
                for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                    System.out.println("Category: " + entry.getKey() + ", Total: " + entry.getValue());
                }
            }
        }

        // Save expenses to file
        private void saveExpenses() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Expense expense : expenses) {
                    writer.write(expense.getAmount() + "," + expense.getCategory() + "," + expense.getDescription());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving expenses: " + e.getMessage());
            }
        }

        // Load expenses from file
        private void loadExpenses() {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1];
                    String description = parts[2];
                    expenses.add(new Expense(amount, category, description));
                }
            } catch (FileNotFoundException e) {
                // File not found; no expenses to load
            } catch (IOException e) {
                System.out.println("Error loading expenses: " + e.getMessage());
            }
        }
    }
// Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();

        while (true) {
            // Display menu
            System.out.println("\n=== Daily Expense Tracker ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Expenses by Category");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add an expense
                    try {
                        System.out.print("Enter amount: ");
                        double amount = Double.parseDouble(scanner.nextLine());
                        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive!");

                        System.out.print("Enter category (e.g., Food, Travel): ");
                        String category = scanner.nextLine();

                        System.out.print("Enter description: ");
                        String description = scanner.nextLine();

                        Expense expense = new Expense(amount, category, description);
                        manager.addExpense(expense);
                        System.out.println("Expense added successfully!");
                    } catch (Exception e) {
                        System.out.println("Invalid input! Please try again.");
                    }
                    break;

                case 2:
                    // View all expenses
                    manager.viewExpenses();
                    break;

                case 3:
                    // View expenses by category
                    manager.viewExpensesByCategory();
                    break;

                case 4:
                    // Exit program
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

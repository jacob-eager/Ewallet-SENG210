import java.awt.*;          // added for login GUI
import java.awt.event.*;             // added for login GUI layout
import java.util.ArrayList;       // added for button event
import javax.swing.*;

public class EWalletApp {
    // this is the app class, has the GUI and create one object of your expense
    // calculator class. The expense calculator class is the implementation of the
    // Expenser interface
    private ArrayList<User> allData;

    public void createUser(String username, String password) {
        // method for creating a user (future implementation)
    }

<<<<<<< HEAD
	public static void main(String[] args) {
		DatabaseAccess.startDB();
		DatabaseAccess.shutdown();
	}
=======
    public static void main(String[] args) {
        // Show login screen first before running demos
        displayLoginScreen();
    }
>>>>>>> a55d8a569830016a0b28b06fcd7c7569dea4db6b

    // === New Login Screen Method ===
    private static void displayLoginScreen() {
        JFrame frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JLabel feedbackLabel = new JLabel();

        // Action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                // For Sprint 0
                if (username.equals("testUser") && password.equals("password123")) {
                    feedbackLabel.setText("Login Successful!");
                    frame.dispose();      // close login window
                    featureDemoDD();      // run the original demo
                } else {
                    feedbackLabel.setText("Invalid Credentials!");
                }
            }
        });

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(feedbackLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void featureDemoDD() {
        User testUser = new User("testUser", "password123");
        ExpenseCalculator calculator = new ExpenseCalculator(testUser);

        System.out.println("__Full report (and add expense/monthly income) demo__");

        calculator.addExpense(new Expense("Groceries", 500, 12));
        calculator.addMonthlyIncome(new Wage("Salary", 3000, "January"));
        calculator.printFullReport();

        System.out.println("__Update montly savings demo__");

        System.out.println("Monthly Savings Before: " + testUser.getMonthlySavings(calculator));
        calculator.addExpense(new Expense("Rent", 50, 24));
        calculator.addMonthlyIncome(new Wage("Sold old car", 4000, "February"));
        System.out.println("Monthly Savings After: " + testUser.getMonthlySavings(calculator));

        System.out.println("__Print income/expense report demo__");

        calculator.printExpenseReport();
        calculator.printIncomeReport();

        System.out.println("__Export report and \"When Can I Buy Demo\"__");

        calculator.exportReport("income");
        System.out.println("Test Item: Nintendo Switch 2, $500");
        calculator.whenCanIBuy("Nintendo Switch 2", 500);
    }

    static void featureDemoJA() {
        User testUser = new User("testUser", "password123");
        ExpenseCalculator calculator = new ExpenseCalculator(testUser);

        System.out.println("__Load expense file and load income file demo__");

        calculator.loadExpenseFile("expenses.csv");
        calculator.loadIncomeFile("incomes.csv");

        System.out.println("__Print expense and income by type demo__");

        calculator.printExpenseByType();
        calculator.printIncomeReportByType();

        System.out.println("__Convert foreign currency demo__");

        Currency yuan = new Currency();
        yuan.rate = 0.14;
        yuan.name = "CNY";
        calculator.convertForeignCurrency(yuan, 10000, false);
    }
}


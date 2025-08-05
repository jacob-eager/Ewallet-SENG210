import java.awt.*;          // added for login GUI
import java.awt.event.*;             // added for login GUI layout
import java.util.ArrayList;       // added for button event
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

public class EWalletApp {
	// this is the app class, has the GUI and create one object of your expense
	// calculator class. The expense calculator class is the implementation of the
	// Expenser interface
	private static ArrayList<User> allData;
	private static int currUserIndex;

	private static ExpenseCalculator expenseCalculator = new ExpenseCalculator();


    public void createUser(String username, String password) {
        // method for creating a user (future implementation)
    }

	public static void main(String[] args) {
		DatabaseAccess.getURL(); 
		DatabaseAccess.createConnection(); 
		
		ExpenseCalculator expenseCalculator = new ExpenseCalculator();

		//Putting note here: obviously they all show up at once.
		//Will need changing in future
		displayLoginScreen();
		showReportGUI();
		initalizeMainScreen(expenseCalculator);
	}
	
	public static void showReportGUI() {
		
		// REMOVE LATER: Debug test user
		allData = new ArrayList<User>();
		allData.add(createTestUser());
		currUserIndex = 0;
		new ReportFrame(allData.get(currUserIndex));
	}


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

		//copy+pasted by Ethan from previous code
	private static void initalizeMainScreen(ExpenseCalculator expenseCalculator) {
		// inital Jframe stuff
		JFrame jframe = new JFrame();
		jframe.setTitle("E-Wallet App");
		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
		jframe.setSize(400, 300);

		// Creating GUI stuff
		JLabel incomeLabel = new JLabel("Add Income (per month)");
		JLabel expenseLabel = new JLabel("Add Expense");

		JTextField incomeInput = new JTextField();
		JTextField expenseInput = new JTextField();

		// little messages that show up under the input areas to show it went through
		JLabel incomeConfirmation = new JLabel("");
		JLabel expenseConfirmation = new JLabel("");

		// Admittedly code taken from chatGPT, to prevent none numbers from being
		// inputed
		DocumentFilter numberFilter = new DocumentFilter(); //changed to DocumentFilter. We'll see how that works out...
		((AbstractDocument) incomeInput.getDocument()).setDocumentFilter(numberFilter);
		((AbstractDocument) expenseInput.getDocument()).setDocumentFilter(numberFilter);

		JButton confirmIncomeButton = new JButton("Add");
		JButton confirmExpenseButton = new JButton("Add");
		// JButton reportButton = new JButton("Print an Expense Report");


		// action listeners, I.E button functionality
		confirmIncomeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (incomeInput.getText() == null || incomeInput.getText().isEmpty()) {
					incomeConfirmation.setText("Please insert a number, it can't be blank");

					// Functionally, resets the text to be blank after 3 seconds
					javax.swing.Timer time = new javax.swing.Timer(3000, event -> incomeConfirmation.setText(""));
					time.setRepeats(false);
					time.start();
				} else {
					String newIncomeText = incomeInput.getText();
					double newIncomeAmount = Double.parseDouble(newIncomeText);
					// defaults to "Unspecified" because there currently isn't a way TO specify
					Wage newWage = new Wage("Unspecified", newIncomeAmount);
					expenseCalculator.userAtHand.addIncome(newWage);

					incomeConfirmation.setText("New Income Submitted!");

					// yes Copy+Paste from GPT again, kinda works
					// would be better if there was 1 timer shared, hence why its a little funky,
					// but eh this works
					// Functionally, resets the text to be blank after 3 seconds
					javax.swing.Timer time = new javax.swing.Timer(3000, event -> incomeConfirmation.setText(""));
					time.setRepeats(false);
					time.start();

				}

			}

		});

		confirmExpenseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (expenseInput.getText() == null || expenseInput.getText().isEmpty()) {
					// potentially display error message
					expenseConfirmation.setText("Please insert a number, it can't be blank");

					// Functionally, resets the text to be blank after 3 seconds
					javax.swing.Timer time = new javax.swing.Timer(3000, event -> expenseConfirmation.setText(""));
					time.setRepeats(false);
					time.start();
				} else {
					String newExpenseText = expenseInput.getText();
					double newExpenseAmount = Double.parseDouble(newExpenseText);
					// defaults to "Unspecified" because there currently isn't a way TO specify
					Expense expense = new Expense("Unspecified", newExpenseAmount);
					expenseCalculator.userAtHand.addExpense(expense);

					expenseConfirmation.setText("New Expense Submitted!");

					// Functionally, resets the text to be blank after 3 seconds
					javax.swing.Timer time = new javax.swing.Timer(3000, event -> expenseConfirmation.setText(""));
					time.setRepeats(false);
					time.start();
				}
			}

		});
		
		// Panels, to organize page
		JPanel incomePanel = new JPanel();
		incomePanel.setLayout(new BoxLayout(incomePanel, BoxLayout.Y_AXIS));
		JPanel expensePanel = new JPanel();
		expensePanel.setLayout(new BoxLayout(expensePanel, BoxLayout.Y_AXIS));

		// Add features to GUI
		incomePanel.add(incomeLabel);
		incomePanel.add(incomeInput);
		incomePanel.add(confirmIncomeButton);
		incomePanel.add(incomeConfirmation);

		expensePanel.add(expenseLabel);
		expensePanel.add(expenseInput);
		expensePanel.add(confirmExpenseButton);
		expensePanel.add(expenseConfirmation);

		jframe.add(incomePanel, BorderLayout.NORTH);
		jframe.add(expensePanel, BorderLayout.CENTER);

		// Wrap up stuff
		jframe.setVisible(true);
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
	
	private static User createTestUser() {
		User testUser = new User("Test User", "Password1");
		testUser.addIncome(new Wage("Walmart", 400.00, "May"));
		testUser.addIncome(new Wage("Walmart", 700.51345465768, "June"));
		testUser.addIncome(new Wage("Erbert and Gerbert's", 500.0, "May"));
		testUser.addIncome(new Wage("Instacart", 10.00, "May"));
		testUser.addIncome(new Wage("Instacart", 40.00, "June"));
		testUser.addSpending(new Expense("Shopping", 40.00, 1));
		testUser.addSpending(new Expense("Subscription", 12.00, 12));
		testUser.addSpending(new Expense("Groceries", 100.00, 24));
		testUser.addSpending(new Expense("Doordash", 50.398, 6));
		
		return testUser;
	}

}


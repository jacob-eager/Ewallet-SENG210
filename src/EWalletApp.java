import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class EWalletApp {
	// this is the app class, has the GUI and create one object of your expense
	// calculator class. The expense calculator class is the implementation of the
	// Expenser interface
	private static ArrayList<User> allData;
	private static int currUserIndex;

	public void createUser(String username, String password) {
	}

	public static void main(String[] args) {
		showReportGUI();
		// featureDemoDD();
	}
	
	public static void showReportGUI() {
		
		
		JFrame reportFrame = new JFrame("Generate Report");
		reportFrame.setSize(700, 500);
		reportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		reportFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// Panel to keep things centered
		JPanel centerLock = new JPanel();
		centerLock.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		reportFrame.add(centerLock);
		
		
		// REMOVE LATER: Debug test user
		allData = new ArrayList<User>();
		allData.add(createTestUser());
		currUserIndex = 0;
		
		
		String[] incomeTableColumnNames = {
				"Source",
				"Amount",
				"Month"
		};
		
		ArrayList<String[]> incomeTableDataArrayList = new ArrayList<String[]>();
		
		// Reads data from the current user's income and adds to array
		for (Wage incomeSource : allData.get(currUserIndex).getIncome()) {
			String[] row = new String[3];
			row[0] = incomeSource.source;
			row[1] = "$" + formatMoney(incomeSource.amount);
			row[2] = incomeSource.month;
			incomeTableDataArrayList.add(row);
		}
		
		// Converts to array for table model
		String[][] incomeTableData = incomeTableDataArrayList.toArray(new String[0][0]);
		
		JTable incomeTable = new JTable(new ReportTableModel(incomeTableData, incomeTableColumnNames));
		JScrollPane tablePane = new JScrollPane(incomeTable);
		
		
		JPanel reportPanel = new JPanel();
		reportPanel.setPreferredSize(new Dimension(500,500));
		reportPanel.add(tablePane);
		centerLock.add(reportPanel, constraints);
		
		String[] reportTypes = {
				"Full Report", "Income Report", "Expense Report"
		};
		JComboBox<String> typeSelector = new JComboBox<String>(reportTypes);
		typeSelector.setPreferredSize(new Dimension(400,25));
		constraints.gridy = 1;
		centerLock.add(typeSelector, constraints);
		
		
		JPanel buttonsPanel = new JPanel();
		constraints.gridy = 2;
		centerLock.add(buttonsPanel, constraints);
		
		JButton importButton = new JButton("Import");
		JButton export = new JButton("Export");
		JButton filter = new JButton("Filter");
		JButton close = new JButton("Close");
		close.addActionListener(event -> reportFrame.dispose());
		
		buttonsPanel.add(importButton);
		buttonsPanel.add(export);
		buttonsPanel.add(filter);
		buttonsPanel.add(close);
		
		
		reportFrame.setVisible(true);
	}

	private static String formatMoney(double amount) {

		String s = Double.toString(amount);
		boolean afterDecimal = false;
		int decimalPlaces = 0;
		
		for (char c : s.toCharArray()) {
			if (c == '.') {
				afterDecimal = true;
				continue;
			}
			if (afterDecimal) {
				++decimalPlaces;
			}
		}
		
		switch (decimalPlaces) {
		
		case 1:
			return s + "0";
			
		case 2:
			return s;
			
		// > 2 decimal places
		default:
			return s.substring(0, s.length() - (decimalPlaces - 2));  // Gets rid of excess decimal places (FIXME: Doesn't round, just cuts off)
		
		}
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
		testUser.addIncome(new Wage("Side hustle", 10.00, "May"));
		testUser.addIncome(new Wage("Side hustle", 40.00, "June"));
		testUser.addSpending(new Expense("Shopping", 40.00, 1));
		testUser.addSpending(new Expense("Subscription", 12.00, 12));
		testUser.addSpending(new Expense("Groceries", 100.00, 24));
		
		return testUser;
	}
}

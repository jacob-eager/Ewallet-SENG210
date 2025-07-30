import java.util.ArrayList;

public class EWalletApp {
	// this is the app class, has the GUI and create one object of your expense
	// calculator class. The expense calculator class is the implementation of the
	// Expenser interface
	private ArrayList<User> allData;

	public void createUser(String username, String password) {
	}

	public static void main(String[] args) {
		featureDemoDD();
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

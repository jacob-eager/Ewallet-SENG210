import java.util.ArrayList;

public class EWalletApp {
	//this is the app class, has the GUI and create one object of your expense calculator class. The expense calculator class is the implementation of the Expenser interface 
	private ArrayList<User> AllData;
	public void CreateUser(String username, String password) {}
	
	public static void main(String[] args) {
		featureDemoDD();
	}
	
	static void featureDemoDD() {
		User testUser = new User("testUser", "password123");
		ExpenseCalculator calculator = new ExpenseCalculator(testUser);
		
		System.out.println("__Full report (and add expense/monthly income) demo__");
		
		calculator.addExpense(new Expense("Groceries", 500, 12));
		calculator.addMonthlyIncome(new Wage("Salary", 3000, "January"));
		calculator.PrintFullreport();
		
		System.out.println("__Update montly savings demo__");

		System.out.println("Monthly Savings Before: " + testUser.getMonthlySavings(calculator));
		calculator.addExpense(new Expense("Rent", 50, 24));
	    calculator.addMonthlyIncome(new Wage("Sold old car", 4000, "February"));
	    System.out.println("Monthly Savings After: " + testUser.getMonthlySavings(calculator));
	    
	    
	}
}

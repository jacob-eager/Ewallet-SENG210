import java.util.ArrayList;

public class EWalletApp {
	//this is the app class, has the GUI and create one object of your expense calculator class. The expense calculator class is the implementation of the Expenser interface 
	private ArrayList<User> AllData;
	public void CreateUser(String username, String password) {}
	
	public static void main(String[] args) {
		User testUser = new User("testUser", "password123");
		ExpenseCalculator calculator = new ExpenseCalculator(testUser);
		
		calculator.addExpense(new Expense("Groceries", 5000, 12));
		calculator.addMonthlyIncome(new Wage("Salary", 3000, "January"));
		calculator.PrintFullreport();
	}
}

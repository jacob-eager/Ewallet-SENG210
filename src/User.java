import java.util.ArrayList;

public class User {
	private ArrayList<Currency> currencyRates;
	private ArrayList<Wage> Income; // user income sources that user can record or view or search by type or month
	private ArrayList<Expense> Spending; // user's expenses
	String username;
	String pwd;
	// current total income - total
	double balance;
	// possible monthly savings, calculated using monthly income (most recent)
	// assuming the data we have is for one year, and monthly and biweekly expenses,
	// here you can assume yearly expenses that are recorded have already been paid.
	double monthlysavings;

	// should add constructor(s)
	User(String username, String password) {
		this.username = username;
		this.pwd = password;
		this.Income = new ArrayList<Wage>();
		this.Spending = new ArrayList<Expense>();
		this.currencyRates = new ArrayList<Currency>();
		this.balance = 0.0;
		this.monthlysavings = 0.0;
	}

	void setMonthlySavings(double newSavings) {
		this.monthlysavings = newSavings;
	}

	double getMonthlySavings(ExpenseCalculator calculator) {
		calculator.updateMonthlySavings();
		return monthlysavings;
	}

	public ArrayList<Wage> getIncome() {
		return Income;
	}

	public ArrayList<Expense> getSpending() {
		return Spending;
	}

	public void addIncome(Wage w) {
		this.Income.add(w);
	}

	public void addSpending(Expense e) {
		this.Spending.add(e);
	}
}

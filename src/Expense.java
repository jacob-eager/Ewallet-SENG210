import java.util.HashMap;

public class Expense {

	int expenseID; // Unique identifier for each expense
	String username; // Username of the user whose expense this is
	String source;
	double amount;
	int yearlyFrequency; // 1 for 1 time or once a year, 12 for monthly or or 24 for biweekly
	

	public static final HashMap<Integer, String> yearlyFrequencyKey = new HashMap<Integer, String>();

	static {
		yearlyFrequencyKey.put(1, "Yearly");
		yearlyFrequencyKey.put(12, "Monthly");
		yearlyFrequencyKey.put(24, "Biweekly");
		yearlyFrequencyKey.put(52, "Weekly");
	}

	Expense(String source, double amount, int yearlyFrequency) {
		this.source = source;
		this.amount = amount;
		this.yearlyFrequency = yearlyFrequency;
	}
	
	public Expense(int expenseID, String username, String source, double amount, int yearlyFrequency) {
		this.expenseID = expenseID;
		this.username = username;
		this.source = source;
		this.amount = amount;
		this.yearlyFrequency = yearlyFrequency;
	}
	
}

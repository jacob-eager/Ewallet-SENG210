import java.util.HashMap;

public class Expense {
	
	String source;
	double amount;
	int yearlyFrequency; // 1 for 1 time or once a year, 12 for monthly or or 24 for biweekly
	
	public static final HashMap<Integer, String> yearlyFrequencyKey = new HashMap<Integer, String>();

	
	{
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
}

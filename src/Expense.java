
public class Expense {
	String source;
	double amount;
	int yearlyFrequency; // 1 for 1 time or once a year, 12 for monthly or or 24 for biweekly

	Expense(String source, double amount, int yearlyFrequency) {
		this.source = source;
		this.amount = amount;
		this.yearlyFrequency = yearlyFrequency;
	}
}

//adding a comment for testing - 
public class Expense {
	String source;
	double amount;
	int yearlyfrequency; // 1 for 1 time or once a year, 12 for monthly or or 24 for biweekly
	// should add contructor(s)

	Expense(String source, double amount, int yearlyfrequency) {
		this.source = source;
		this.amount = amount;
		this.yearlyfrequency = yearlyfrequency;
	}
}

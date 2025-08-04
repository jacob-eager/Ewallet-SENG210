
public class Wage {
	int wageID; // Unique identifier for each wage
	int userID; // Unique identifier for the user who receives the wage
	String source;
	double amount;
	String month;

	Wage(String source, double amount, String Month) {
		this.source = source;
		this.amount = amount;
		this.month = Month;
	}
}

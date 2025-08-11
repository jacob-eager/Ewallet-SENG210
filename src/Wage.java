
public class Wage {
	int wageID; // Unique identifier for each wage
	String username; // Unique identifier for the user who receives the wage
	String source;
	double amount;
	String month;

	Wage(String source, double amount, String Month) {
		this.source = source;
		this.amount = amount;
		this.month = Month;
	}
	
	Wage(int wageID, String username, String source, double amount, String month) {
		this.wageID = wageID;
		this.username = username;
		this.source = source;
		this.amount = amount;
		this.month = month;
	}
}

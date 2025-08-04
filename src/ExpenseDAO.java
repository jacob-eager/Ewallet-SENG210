import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ExpenseDAO implements DatabaseAccessObject<Expense> {

	@Override
	public Optional<Expense> get(int id) {
		Expense selectedExpense = null;
		ResultSet results = DatabaseAccess.query("SELECT * FROM Expense WHERE expense_id = " + id);
		try {

			// Since id is a primary key, this can only happen once
			while (results.next()) {

				String source = results.getString(2);
				double amount = results.getDouble(3);
				int yearlyFrequency = results.getInt(4);

				selectedExpense = new Expense(source, amount, yearlyFrequency);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.ofNullable(selectedExpense);
	}

	@Override
	public ArrayList<Expense> getAll() {
		ArrayList<Expense> allExpenses = new ArrayList<Expense>();
		ResultSet results = DatabaseAccess.query("SELECT * FROM User");
		try {
			while (results.next()) {
				int id = results.getInt(1);
				String source = results.getString(2);
				double amount = results.getDouble(3);
				int yearlyFrequency = results.getInt(4);

				allExpenses.add(new Expense(source, amount, yearlyFrequency));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allExpenses;
	}

	@Override
	public void save(Expense newExpense) {
		DatabaseAccess.query("INSERT INTO Expense (source, amount, yearlyFrequency) VALUES ("
								+ newExpense.source + ", " + newExpense.amount + ", " + newExpense.yearlyFrequency + ")");
		
	}

	@Override
	public void update(Expense t, String params) {
		// Placeholder
		
	}

	@Override
	public void delete(Expense deletedExpense) {
		DatabaseAccess.query("DELETE FROM Expense WHERE expense_id = " + deletedExpense.expenseID);
	}

}

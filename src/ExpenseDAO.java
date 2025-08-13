import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseDAO implements DatabaseAccessObject<Expense> {

	
	User currentUser;
	
	
	public ExpenseDAO(User currentUser) {
		this.currentUser = currentUser;
	}
	
	
	@Override
	public Expense get(int id) {
		Expense selectedExpense = null;
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWALLET.expense WHERE expense_id = " + id);
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
		
		return selectedExpense;
	}

	@Override
	public ArrayList<Expense> getAll() {
		ArrayList<Expense> allExpenses = new ArrayList<Expense>();
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWALLET.expense");
		try {
			while (results.next()) {
				int id = results.getInt(1);
				String username = results.getString(2);
				String source = results.getString(3);
				double amount = results.getDouble(4);
				int yearlyFrequency = results.getInt(5);

				allExpenses.add(new Expense(id, username, source, amount, yearlyFrequency));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allExpenses;
	}
	
	public ArrayList<Expense> getUserExpenses() {
		ArrayList<Expense> userExpenses = new ArrayList<Expense>();
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWALLET.expense WHERE expense_user = '" + currentUser.username + "'");
		try {
			while (results.next()) {
				String username = results.getString(1);
				int id = results.getInt(2);
				String source = results.getString(3);
				double amount = results.getDouble(4);
				int yearly_frequency = results.getInt(5);

				userExpenses.add(new Expense(id, username, source, amount, yearly_frequency));
			}
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        try {
	            if (results != null) results.close();
	            if (DatabaseAccess.stmt != null) DatabaseAccess.stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		
		return userExpenses;
		
	}

	@Override
	public void create(Expense newExpense) {
		DatabaseAccess.voidQuery("INSERT INTO EWALLET.expense (expense_user, source, amount, yearly_frequency) VALUES ('"
								+ newExpense.username + "', '"
								+ newExpense.source + "', " + newExpense.amount + ", " 
								+ Integer.valueOf(newExpense.yearlyFrequency) + ")");
		
	}

	@Override
	public void update(Expense t, String params) {
		// Placeholder
		
	}

	@Override
	public void delete(Expense deletedExpense) {
		DatabaseAccess.voidQuery("DELETE FROM EWALLET.expense WHERE expense_id = " + deletedExpense.expenseID);
	}

}

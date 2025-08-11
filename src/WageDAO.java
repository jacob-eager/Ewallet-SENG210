import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WageDAO implements DatabaseAccessObject<Wage> {

	private User currentUser;
	
	public WageDAO(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public void setUser(User newUser) {
		this.currentUser = newUser;
	}
	
	@Override
	public Wage get(int id) {
		Wage selectedWage = null;
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWallet.wage WHERE wage_id = " + id);
		try {

			// Since id is a primary key, this can only happen once
			while (results.next()) {

				String source = results.getString(2);
				double amount = results.getDouble(3);
				String month = results.getString(4);

				selectedWage = new Wage(source, amount, month);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return selectedWage;
	}

	@Override
	public ArrayList<Wage> getAll() {
		ArrayList<Wage> allWages = new ArrayList<Wage>();
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWallet.wage");
		try {
			while (results.next()) {
				int id = results.getInt(1);
				String username = results.getString(2);
				String source = results.getString(3);
				double amount = results.getDouble(4);
				String month = results.getString(5);

				allWages.add(new Wage(id, username, source, amount, month));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allWages;
	}
	
	public ArrayList<Wage> getUserWages() {
		ArrayList<Wage> userWages = new ArrayList<Wage>();
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWallet.wage WHERE wage_user = '" + currentUser.username + "'");
		try {
			while (results.next()) {
				String username = results.getString(1);
				Integer id = results.getInt(2);
				String source = results.getString(3);
				double amount = results.getDouble(4);
				String month = results.getString(5);

				userWages.add(new Wage(id, username, source, amount, month));
			}
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userWages;
		
	}

	@Override
	public void create(Wage newWage) {
		DatabaseAccess.voidQuery("INSERT INTO EWallet.wage (wage_user, source, amount, month) VALUES ('"
				+ newWage.username + "', '"
				+ newWage.source + "', " + newWage.amount + ", '" + newWage.month + "')");
		
	}

	@Override
	public void update(Wage t, String params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Wage deletedWage) {
		DatabaseAccess.voidQuery("DELETE FROM EWallet.wage WHERE wage_id = " + deletedWage.wageID);
		
	}

}

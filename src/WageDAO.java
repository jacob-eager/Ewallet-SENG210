import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class WageDAO implements DatabaseAccessObject<Wage> {

	@Override
	public Optional<Wage> get(int id) {
		Wage selectedWage = null;
		ResultSet results = DatabaseAccess.query("SELECT * FROM Wage WHERE wage_id = " + id);
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
		
		return Optional.ofNullable(selectedWage);
	}

	@Override
	public ArrayList<Wage> getAll() {
		ArrayList<Wage> allWages = new ArrayList<Wage>();
		ResultSet results = DatabaseAccess.query("SELECT * FROM Wage");
		try {
			while (results.next()) {
				int id = results.getInt(1);
				String source = results.getString(2);
				double amount = results.getDouble(3);
				String month = results.getString(4);

				allWages.add(new Wage(source, amount, month));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allWages;
	}

	@Override
	public void save(Wage newWage) {
		DatabaseAccess.query("INSERT INTO Expense (source, amount, month) VALUES ("
				+ newWage.source + ", " + newWage.amount + ", " + newWage.month + ")");
		
	}

	@Override
	public void update(Wage t, String params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Wage deletedWage) {
		DatabaseAccess.query("DELETE FROM Wage WHERE wage_id = " + deletedWage.wageID);
		
	}

}

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO implements DatabaseAccessObject<User> {

	// Currently there is no user_id and username is used as the primary key instead
	@Override
	public User get(int id) {
		User selectedUser = null;
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWALLET.users WHERE user_id = " + id);
		try {
			while (results.next()) {
                String username = results.getString(1);
                String password = results.getString(2);
                
                selectedUser = new User(username, password);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return selectedUser;
	}
	
	public User get(String username) {
		User selectedUser = null;
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWALLET.users WHERE username = " + username);
		try {
			
			// Since username is a primary key, this can only happen once
			while (results.next()) {
                String selected_username = results.getString(1);
                String password = results.getString(2);
                
                selectedUser = new User(selected_username, password);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return selectedUser;
	}

	@Override
	public ArrayList<User> getAll() {
		ArrayList<User> allUsers = new ArrayList<User>();
		ResultSet results = DatabaseAccess.resultsQuery("SELECT * FROM EWallet.users");
		try {
			while (results.next()) {
                String username = results.getString(1);
                String password = results.getString(2);
                
                allUsers.add(new User(username, password));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allUsers;
	}

	@Override
	public void create(User newUser) {
		DatabaseAccess.resultsQuery("INSERT INTO EWALLET.users (username, password) VALUES (" + newUser.username + ", " + newUser.pwd + ")");
		
	}

	@Override
	public void update(User updatedUser, String params) {
		// IDK if we ever actually need to use this so I'll fill this in later
		
	}

	@Override
	public void delete(User deletedUser) {
		DatabaseAccess.resultsQuery("DELETE FROM EWALLET.users WHERE username = " + deletedUser.username);
	}

}

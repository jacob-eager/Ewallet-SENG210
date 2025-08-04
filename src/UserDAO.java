import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;


public class UserDAO implements DatabaseAccessObject<User> {

	@Override
	public Optional<User> get(int id) {
		User selectedUser = null;
		ResultSet results = DatabaseAccess.query("SELECT * FROM USER WHERE user_id = " + id);
		try {
			
			// Since id is a primary key, this can only happen once
			while (results.next()) {
                String username = results.getString(2);
                String password = results.getString(3);
                
                selectedUser = new User(id, username, password);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.ofNullable(selectedUser);
	}

	@Override
	public ArrayList<User> getAll() {
		ArrayList<User> allUsers = new ArrayList<User>();
		ResultSet results = DatabaseAccess.query("SELECT * FROM User");
		try {
			while (results.next()) {
				int id = results.getInt(1);
                String username = results.getString(2);
                String password = results.getString(3);
                
                allUsers.add(new User(id, username, password));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allUsers;
	}

	@Override
	public void save(User newUser) {
		DatabaseAccess.query("INSERT INTO User (username, password) VALUES (" + newUser.username + ", " + newUser.pwd + ")");
		
	}

	@Override
	public void update(User updatedUser, String params) {
		// IDK if we actually need to do this so I'll fill this in later
		
	}

	@Override
	public void delete(User deletedUser) {
		DatabaseAccess.query("DELETE FROM User WHERE user_id = " + deletedUser.id);
	}

}

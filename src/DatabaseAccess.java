import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;                  // added
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;          // added
import java.sql.ResultSet;                  // added
import java.sql.SQLException;               // added
import java.util.Scanner;

public class DatabaseAccess {

	private static String dbURL = "";
	
	private static Connection conn = null;
	private static Statement stmt = null;
	 
	 
	// Establishes connection from .env file
	public static void getURL() {
		
		try {
			FileInputStream fileByteStream = new FileInputStream(".env");
			Scanner inFS = new Scanner(fileByteStream);
			
			while(inFS.hasNext()) {
				String currLine = inFS.nextLine().trim();
				if (currLine.startsWith("DB_URL")) {
					int eq = currLine.indexOf('=');
					if (eq >= 0) {
						dbURL = currLine.substring(eq + 1).trim(); // safer parsing
					}
					System.out.println(dbURL);
				}
			}
			inFS.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// default so login GUI isn't blocked if .env is missing
			dbURL = "jdbc:derby:ewalletdb;create=true";
		}
		catch (IOException e) {                 // added
			e.printStackTrace();
		}
	}

    public static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(dbURL); 
            System.out.println("no worries");
        }
        catch (Exception except)
        {
        	System.out.println("fail");
            except.printStackTrace();
        }
    }

    // ===== Added for login GUI: create USERS table if missing =====
    public static void initUsersTable() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE USERS ("
            		+ "USERNAME VARCHAR(100) PRIMARY KEY, "
            		+ "PASSWORD VARCHAR(200) NOT NULL"
            		+ ");");
        } catch (SQLException e) {
            // X0Y32 = table already exists
            if (!"X0Y32".equals(e.getSQLState())) e.printStackTrace();
        }
    }

    // ===== Added for login GUI: seed a demo user for testing the login screen =====
    public static void seedTestUser() {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?)")) {
            ps.setString(1, "testUser");
            ps.setString(2, "password123");
            ps.executeUpdate();
        } catch (SQLException e) {
            // ignore duplicate seed
        }
    }

    // ===== Added for login GUI: validate username/password against USERS table =====
    public static boolean authenticate(String username, String password) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM USERS WHERE USERNAME=? AND PASSWORD=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

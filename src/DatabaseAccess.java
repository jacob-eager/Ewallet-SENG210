import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;                  // added
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;          // added
import java.sql.ResultSet;                  // added
import java.sql.SQLException;               // added
import java.sql.Statement;
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
					// System.out.println(dbURL);
				}
			}
			inFS.close();
		} 
		catch (FileNotFoundException e) {
			// default so login GUI isn't blocked if .env is missing
			dbURL = "jdbc:derby:ewalletdb;create=true";
			
			e.printStackTrace();
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
            // System.out.println("no worries");
        }
        catch (Exception except)
        {
        		// System.out.println("fail");
            except.printStackTrace();
        }
    }
    
	public static void initiateDB() {

		String schemaText = "";

		try (Statement st = conn.createStatement();
				FileInputStream fileByteStream = new FileInputStream("schema.sql");
				Scanner inFS = new Scanner(fileByteStream);) {

			while (inFS.hasNext()) {
				schemaText += inFS.nextLine() + "\n";
			}

			// Splits the different statements since Derby can only run one statement at a
			// time
			String[] statements = schemaText.split(";");

			// Executes CREATE SCHEMA if the EWallet schema doesn't exist
			ResultSet results = st.executeQuery("SELECT SCHEMANAME FROM SYS.SYSSCHEMAS");
			boolean schemaExists = false;

			while (results.next()) {
				String schemaName = results.getString(1);
				if (schemaName.equals("EWALLET")) {
					schemaExists = true;
					// System.out.println("Schema exists!");
				}
			}

			if (!schemaExists) {
				st.executeUpdate(statements[0]);
			}

			// Creates tables if they don't exist
			DatabaseMetaData dbmd = conn.getMetaData();
			String[] types = { "TABLE" };

			results = dbmd.getTables(null, "EWALLET", "%", types);
			boolean usersExists = false;
			boolean expenseExists = false;
			boolean wageExists = false;

			while (results.next()) {
				String tableName = results.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase("users")) {
					usersExists = true;
					// System.out.println("Users exists!");
				}
				if (tableName.equalsIgnoreCase("expense")) {
					expenseExists = true;
					// System.out.println("Expense exists!");
				}
				if (tableName.equalsIgnoreCase("wage")) {
					wageExists = true;
					// System.out.println("Wage exists!");
				}
			}

			if (!usersExists) {
				st.executeUpdate(statements[1]);
			}

			if (!expenseExists) {
				st.executeUpdate(statements[2]);
			}

			if (!wageExists) {
				st.executeUpdate(statements[3]);
			}

		} catch (FileNotFoundException e) {
			System.out.println("schema.sql not found!");
			e.printStackTrace();
		} catch (SQLException e) {
			// X0Y32 = table already exists
			if (!"X0Y32".equals(e.getSQLState()))
				e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

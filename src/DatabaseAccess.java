
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseAccess {

	private static String dbURL = "";
	
	private static Connection conn = null;
	private static Statement stmt = null;
	 
	
	// Public interface for starting database
	public static void startDB() {
		getURL();
		createConnection();
	}
	
	// Fetches URL from .env file
	public static void getURL() {

		try {
			FileInputStream fileByteStream = new FileInputStream(".env");
			Scanner inFS = new Scanner(fileByteStream);

			while (inFS.hasNext()) {
				String currLine = inFS.nextLine();
				if (currLine.contains("DB_URL")) {
					dbURL = currLine.substring(currLine.indexOf("j"), currLine.length());
				}
			}
			inFS.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		}
	}
	
	// Starts connection to local DB
    @SuppressWarnings("deprecation")
	private static void createConnection()
    {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			conn = DriverManager.getConnection(dbURL);
			System.out.println("Connection successful");
		} catch (Exception except) {
			System.out.println("fail");
			except.printStackTrace();
		}
    }
    
    // Shuts down connection
    public static void shutdown()
    {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

    }
    
    // Executes a given query
	public static ResultSet query(String sqlStatement) {
		ResultSet results = null;
		try {
			stmt = conn.createStatement();
			results = stmt.executeQuery(sqlStatement);
			stmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
    
}

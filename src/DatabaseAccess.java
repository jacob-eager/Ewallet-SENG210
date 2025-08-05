
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
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
				String currLine = inFS.nextLine();
				if (currLine.contains("DB_URL")) {
					dbURL = currLine.substring(currLine.indexOf("j"), currLine.length());
					System.out.println(dbURL);
				}
			}
			inFS.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
}

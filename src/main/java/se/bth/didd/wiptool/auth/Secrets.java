package se.bth.didd.wiptool.auth;

import java.sql.*;
import java.io.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Scanner;

/**
 * Contains secrets for demo purposes only. These values should be stored and
 * used securely.
 *
 * @author Hendrik van Huyssteen
 * @since 21 Sep 2017
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Secrets {

	// loadFromFile("config.yml");

	public Secrets() {
	}

	
	public static final byte[] JWT_SECRET_KEY = "dfwzsdzwh823zebdwdz772632gdsbd3333".getBytes();

	// JDBC driver name and database URL

	// Database credentials

	String databaseUsername = null;
	String databasePassword = null;
	String databaseDriver = null;
	String DB_URL = null;

	public Boolean test(String userMailId, String password) throws IOException {

		@SuppressWarnings("resource")
		Scanner fileIn = new Scanner(new File("config.yml"));

		while ((fileIn.hasNextLine())) {

			if (fileIn.nextLine().contains("JDBC driver")) {
				String trim = fileIn.nextLine();
				databaseDriver = trim.substring(15);
				System.out.println(databaseDriver);
				System.out.println(fileIn.nextLine());
			}
			if (fileIn.nextLine().contains("user")) {
				String trim = fileIn.nextLine();
				if (trim.substring(0, 7).contentEquals("  user:")) {
					databaseUsername = trim.substring(8);
					System.out.println(databaseUsername);
					System.out.println(fileIn.nextLine());
				}

			}
			if (fileIn.nextLine().contains("password")) {
				String trim = fileIn.nextLine();
				if (trim.substring(0, 11).contentEquals("  password:")) {
					databasePassword = trim.substring(12);
					System.out.println(databasePassword);
					System.out.println(fileIn.nextLine());
				}

			}
			if (fileIn.nextLine().contains("JDBC URL")) {
				String trim = fileIn.nextLine();
				DB_URL = trim.substring(7);
				System.out.println(DB_URL);
				break;
				
			}

		}

		Boolean exists = false;
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			// System.out.println(usernameis );

			// STEP 3: Open a connection
			//System.out.println("Connecting to a selected database");
			conn = DriverManager.getConnection(DB_URL, databaseUsername, databasePassword);
			//System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			//System.out.println("Creating table in given database...");
			stmt = conn.createStatement();

			String sql = "select * from LOGINCREDENTIALS where userMailId = '" + userMailId + "' and password = '" +password + "' or userName = '" + userMailId + "' and password = '" +password + "'";
			
			ResultSet result = stmt.executeQuery(sql);
			if (result.next()) {
				//System.out.println(result);
				exists = true;
			}
			return exists;
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		//System.out.println("Goodbye!");
		return exists;

	}

}

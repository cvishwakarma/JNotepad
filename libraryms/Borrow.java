package libraryms;



//import the packages for using the classes in them into the program

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Borrow {

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	private int bookID;
	private int memberID;
	private Date dayOfBorrowed;
	private Date dayOfReturn;

	public Borrow() {
	}

	public int getBookID() {
		return bookID;
	}

	public int getMemberID() {
		return memberID;
	}

	public Date getDayOfBorrowed() {
		return dayOfBorrowed;
	}

	public Date getDayOfReturn() {
		return dayOfReturn;
	}

	public void connection() {
		try {
            Class.forName("com.mysql.jdbc.Driver").getInterfaces();
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("Borrow.java\n" + cnfe.toString());
		}
		catch (Exception e) {
			System.out.println("Borrow.java\n" + e.toString());
		}
		try { String DB_URL = "jdbc:mysql://localhost:3306/librarymsdb";
            String user = "root";
            String pass = "1230";
            Class.forName("com.mysql.jdbc.Driver").getInterfaces();
			connection = DriverManager.getConnection(DB_URL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Borrow");
			while (resultSet.next()) {
				bookID = resultSet.getInt(1);
				memberID = resultSet.getInt(2);
				dayOfBorrowed = resultSet.getDate(3);
				dayOfReturn = resultSet.getDate(4);
			}
			resultSet.close();
			statement.close();
			connection.close();
		}
		catch (SQLException SQLe) {
			System.out.println("Borrow.java\n" + SQLe.toString());
		} catch (ClassNotFoundException ex) {
                Logger.getLogger(Borrow.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	public void update(String Query) throws ClassNotFoundException {
		try {
            Class.forName("com.mysql.jdbc.Driver").getInterfaces();
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("Borrow.java\n" + cnfe.toString());
		}
		catch (Exception e) {
			System.out.println("Borrow.java\n" + e.toString());
		}
		try {
			String DB_URL = "jdbc:mysql://localhost:3306/librarymsdb";
            String user = "root";
            String pass = "1230";
            connection = DriverManager.getConnection(DB_URL, user, pass);
			statement.executeUpdate(Query);
			statement.close();
			connection.close();
		}
		catch (SQLException SQLe) {
			System.out.println("Borrow.java\n" + SQLe.toString());
		}
	}
}
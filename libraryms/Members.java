package libraryms;



//import the packages for using the classes in them into the program

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Members {
	/***************************************************************************
	 ***      declaration of the private variables used in the program       ***
	 ***************************************************************************/

	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	private int memberID;
	private int ID;
	private String password;
	private String name;
	private String email;
	private String major;
	private int numberOfBooks;
	private int mony;
	private Date expired;

	
	public Members(int memberID, int ID, String password, String name, String email, String major, int numberOfBooks, int mony, Date expired) {
		this.memberID = memberID;
		this.ID = ID;
		this.password = password;
		this.name = name;
		this.email = email;
		this.major = major;
		this.numberOfBooks = numberOfBooks;
		this.mony = mony;
		this.expired = expired;
	}

    Members() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	public int getMemberID() {
		return memberID;
	}

	public int getID() {
		return ID;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getMajor() {
		return major;
	}

	public int getNumberOfBooks() {
		return numberOfBooks;
	}

	public int getMony() {
		return mony;
	}

	public Date getExpired() {
		return expired;
	}

	public void connection(String Query) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("Members.java\n" + cnfe.toString());
		}
		catch (Exception e) {
			System.out.println("Members.java\n" + e.toString());
		}
		try {
                     String DB_URL = "jdbc:mysql://localhost:3306/librarymsdb";
            String user = "root";
            String pass = "1230";
            Class.forName("com.mysql.jdbc.Driver").getInterfaces();
			connection = DriverManager.getConnection(DB_URL);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(Query);
			while (resultSet.next()) {
				memberID = resultSet.getInt(1);
				ID = resultSet.getInt(2);
				password = resultSet.getString(3);
				name = resultSet.getString(4);
				email = resultSet.getString(5);
				major = resultSet.getString(6);
				numberOfBooks = resultSet.getInt(7);
				mony = resultSet.getInt(8);
				expired = resultSet.getDate(9);
			}
			resultSet.close();
			statement.close();
			connection.close();
		}
		catch (SQLException SQLe) {
			System.out.println("Members.java\n" + SQLe.toString());
		} catch (ClassNotFoundException ex) {
                Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	public void update(String Query) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println("Members.java\n" + cnfe.toString());
		}
		catch (Exception e) {
			System.out.println("Members.java\n" + e.toString());
		}
		try {
                     String DB_URL = "jdbc:mysql://localhost:3306/librarymsdb";
                    String user = "root";
                    String pass = "1230";
                    Class.forName("com.mysql.jdbc.Driver").getInterfaces();
                    connection = DriverManager.getConnection(DB_URL);
			statement = connection.createStatement();
			statement.executeUpdate(Query);
			statement.close();
			connection.close();
		}
		catch (SQLException SQLe) {
			System.out.println("Members.java\n" + SQLe.toString());
		} catch (ClassNotFoundException ex) {
                Logger.getLogger(Members.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
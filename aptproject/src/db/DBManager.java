package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	static private DBManager instance;
	private String diriver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.1.10:1521:XE";
	private String user = "apt";
	private String password ="aptapt";
	private Connection con;

	private DBManager() {
		try {
			Class.forName(diriver);
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static public DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();

		}
		return instance;
	}

	public void setInstance(DBManager instance) {
		this.instance = instance;
	}

	public Connection getConnection() {
		return con;
	}

	public void disConnect(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

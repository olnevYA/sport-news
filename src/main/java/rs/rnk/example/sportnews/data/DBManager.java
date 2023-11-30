package rs.rnk.example.sportnews.data;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
	
	public DBManager() throws ClassNotFoundException {
		
	}

	public Connection getConnection() throws SQLException { 
		return DataSource.getConnection();
	}
	
}

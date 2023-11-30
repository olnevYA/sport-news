package rs.rnk.example.sportnews.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {
	
	private static final String DBMS = "mysql";
	private static final String SERVER_NAME = "localhost";
	private static final String PORT_NUMBER = "3306";
	private static final String DB_NAME = "sport_news";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	
	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    static {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        config.setJdbcUrl( "jdbc:" + DBMS + "://" + SERVER_NAME +
				":" + PORT_NUMBER + "/" + DB_NAME + "?serverTimezone=" + 
				TimeZone.getDefault().getID() );
        config.setUsername( USER );
        config.setPassword( PASSWORD );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }
    
    private DataSource() {}
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}

package rs.rnk.example.sportnews.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRole {
	private int id;
	private String name;
	private String description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static UserRole createFromResultSet(ResultSet rs) throws SQLException {
		var userRole = new UserRole();
		
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		
		userRole.setId(id);
		userRole.setName(name);
		userRole.setDescription(description);
		
		return userRole;
	}
}

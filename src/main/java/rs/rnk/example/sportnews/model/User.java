package rs.rnk.example.sportnews.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private int id;
	private String email;
	private String username;
	private String password;
	private String name;
	private UserRole userRole;
	private boolean approved;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	
	
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public static User createFromResultSet(ResultSet rs) throws SQLException{
		var user = new User();
		int id = rs.getInt("id");
		String email = rs.getString("email");
		String username = rs.getString("username");
		String password = rs.getString("password");
		String name = rs.getString("name");
		boolean approved = rs.getBoolean("approved");
		
		UserRole userRole = new UserRole();
		userRole.setId(rs.getInt("user_role_id"));
		user.setUserRole(userRole);
		
		user.setId(id);
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		user.setName(name);
		user.setApproved(approved);
		
		return user;
	}
}

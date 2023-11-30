package rs.rnk.example.sportnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.rnk.example.sportnews.data.DBManager;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.model.UserRole;

public class UserDao {

	private DBManager dbManager;

	public UserDao() {
		try {
			dbManager = new DBManager();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private PreparedStatement createPreparedStatement(Connection conn, String username, String password) throws SQLException {
		String sqlQuery = "SELECT user.id, user.email, user.username, user.password, user.name, user.user_role_id, "
				+ "user.approved, user_role.id AS user_role_id, user_role.name as user_role_name, "
				+ "user_role.description as user_role_description "
				+ "FROM users user, user_roles user_role "
				+ "WHERE user.user_role_id = user_role.id AND user.username = ? AND user.password = ?";
		PreparedStatement ps = conn.prepareStatement(sqlQuery);
		ps.setString(1, username);
		ps.setString(2, password);
		return ps;
	}
	private PreparedStatement createPreparedStatement(Connection conn, String username) throws SQLException {
		String sqlQuery = "SELECT user.id, user.email, user.username, user.password, user.name, user.user_role_id, "
				+ "user.approved, user_role.id AS user_role_id, user_role.name as user_role_name, "
				+ "user_role.description as user_role_description "
				+ "FROM users user, user_roles user_role "
				+ "WHERE user.user_role_id = user_role.id AND user.username = ?";
		PreparedStatement ps = conn.prepareStatement(sqlQuery);
		ps.setString(1, username);
		return ps;
	}
	
	public User get(String username) {
		try (Connection conn = dbManager.getConnection();
				PreparedStatement ps = this.createPreparedStatement(conn, username);
				ResultSet rs = ps.executeQuery();

		) {
			if (rs.next()) {
				User user = User.createFromResultSet(rs);
				int userRoleId = rs.getInt("user_role_id");
				String userRoleName = rs.getString("user_role_name");
				String userRoleDescription = rs.getString("user_role_description");
				UserRole userRole = new UserRole();
				userRole.setId(userRoleId);
				userRole.setName(userRoleName);
				userRole.setDescription(userRoleDescription);
				user.setUserRole(userRole);
				return user;
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatementForGetById(Connection conn, int userId) throws SQLException {
		var ps = conn.prepareStatement("SELECT * FROM users WHERE id = ? LIMIT 1");
		ps.setInt(1, userId);
		return ps;
	}
	
	public User get(int userId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForGetById(conn, userId);
				ResultSet rs = ps.executeQuery()){
			User user = null;
			if(rs.next()) {
				user = User.createFromResultSet(rs);
			}
			return user;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private PreparedStatement createPsForUsernameDifferentId(Connection conn, String username, int notUserId) throws SQLException {
		var ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND "
				+ "NOT id = ? ");
		ps.setString(1, username);
		ps.setInt(2, notUserId);
		return ps;
	}
	
	private PreparedStatement createPsForEmailDifferentId(Connection conn, String email, int notUserId) throws SQLException {
		var ps = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND "
				+ "NOT id = ? ");
		ps.setString(1, email);
		ps.setInt(2, notUserId);
		return ps;
	}
	
	public User getWithUsernameDifferentId(String username, int notUserId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPsForUsernameDifferentId(conn, username, notUserId);
				ResultSet rs = ps.executeQuery()){
			User user = null;
			if(rs.next()) {
				user = User.createFromResultSet(rs);
			}
			return user;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User getWithEmailDifferentId(String email, int notUserId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPsForEmailDifferentId(conn, email, notUserId);
				ResultSet rs = ps.executeQuery()){
			User user = null;
			if(rs.next()) {
				user = User.createFromResultSet(rs);
			}
			return user;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public User get(String username, String password) {

		try (Connection conn = dbManager.getConnection();
				PreparedStatement ps = this.createPreparedStatement(conn, username, password);
				ResultSet rs = ps.executeQuery();

		) {
			if (rs.next()) {
				User user = User.createFromResultSet(rs);
				int userRoleId = rs.getInt("user_role_id");
				String userRoleName = rs.getString("user_role_name");
				String userRoleDescription = rs.getString("user_role_description");
				UserRole userRole = new UserRole();
				userRole.setId(userRoleId);
				userRole.setName(userRoleName);
				userRole.setDescription(userRoleDescription);
				user.setUserRole(userRole);
				return user;
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<UserRole> findAllUserRoles(){
		try(Connection conn = dbManager.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM user_roles")){
			List<UserRole> userRoles = new ArrayList<>();
			while(rs.next()) {
				userRoles.add(UserRole.createFromResultSet(rs));
			}
			return userRoles;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public UserRole getUserRole(int id) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForUserRole(conn, id);
				ResultSet rs = ps.executeQuery();){
			UserRole userRole = null;
			if(rs.next()) {
				userRole = UserRole.createFromResultSet(rs);
			}
			return userRole;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatementForUserRole(Connection connection, int id) throws SQLException {
		PreparedStatement ps = connection.prepareStatement("SELECT * FROM user_roles WHERE id = ?");
		ps.setInt(1, id);
		return ps;
	}
	
	public int insert(User user) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForInsert(conn, user);){
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public User getWithEmail(String email) {
		try (Connection conn = dbManager.getConnection();
				PreparedStatement ps = this.createPreparedStatementForEmail(conn, email);
				ResultSet rs = ps.executeQuery();

		) {
			if (rs.next()) {
				User user = User.createFromResultSet(rs);
				int userRoleId = rs.getInt("user_role_id");
				String userRoleName = rs.getString("user_role_name");
				String userRoleDescription = rs.getString("user_role_description");
				UserRole userRole = new UserRole();
				userRole.setId(userRoleId);
				userRole.setName(userRoleName);
				userRole.setDescription(userRoleDescription);
				user.setUserRole(userRole);
				return user;
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatementForEmail(Connection conn, String email) throws SQLException {
		String sqlQuery = "SELECT user.id, user.email, user.username, user.password, user.name, user.user_role_id, "
				+ "user.approved, user_role.id AS user_role_id, user_role.name as user_role_name, "
				+ "user_role.description as user_role_description "
				+ "FROM users user, user_roles user_role "
				+ "WHERE user.user_role_id = user_role.id AND user.email = ?";
		PreparedStatement ps = conn.prepareStatement(sqlQuery);
		ps.setString(1, email);
		return ps;
	}
	
	private PreparedStatement createPreparedStatementForInsert(Connection conn, User user) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO users (email, username, password, "
				+ "name, user_role_id, approved) VALUES "
				+ "(?, ?, ?, ?, ?, ?)");
		ps.setString(1, user.getEmail());
		ps.setString(2, user.getUsername());
		ps.setString(3, user.getPassword());
		ps.setString(4, user.getName());
		ps.setInt(5, user.getUserRole().getId());
		ps.setBoolean(6, user.isApproved());
		
		return ps;
	}
	
	private PreparedStatement createPreparedStatementForUpdateApproved(Connection conn, int userId, boolean approved) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE users user SET user.approved = ? WHERE user.id = ?");
		ps.setBoolean(1, approved);
		ps.setInt(2, userId);
		return ps;
	}
	
	public boolean updateUser(int userId, boolean approved) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForUpdateApproved(conn, userId, approved);) {
			int result = ps.executeUpdate();
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private PreparedStatement createPreparedStatementForUpdate(Connection conn, User newUser) throws SQLException {
		String sql = "UPDATE users user SET "
				+ "user.username = ?, "
				+ "user.email = ?, "
				+ "user.name = ?, "
				+ "user.approved = ?, "
				+ "user.password = ?, "
				+ "user.user_role_id = ? "
				+ "WHERE user.id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setString(1, newUser.getUsername());
		ps.setString(2, newUser.getEmail());
		ps.setString(3, newUser.getName());
		ps.setBoolean(4, newUser.isApproved());
		ps.setString(5, newUser.getPassword());
		ps.setInt(6, newUser.getUserRole().getId());
		ps.setInt(7, newUser.getId());
		
		return ps;
	}
	
	public int updateUser(User newUser) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForUpdate(conn, newUser);) {
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private PreparedStatement createPreparedStatementForFindUsers(Connection conn, boolean approved) throws SQLException {
		String sqlQuery = "SELECT user.id, user.email, user.username, user.password, user.name, user.user_role_id, "
				+ "user.approved, user_role.id AS user_role_id, user_role.name as user_role_name, "
				+ "user_role.description as user_role_description "
				+ "FROM users user, user_roles user_role "
				+ "WHERE user.user_role_id = user_role.id AND user.approved = ?";
		var ps = conn.prepareStatement(sqlQuery);
		ps.setBoolean(1, approved);
		return ps;
	}
	
	public List<User> findUsers(boolean approved) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForFindUsers(conn, approved);
				ResultSet rs = ps.executeQuery()) {
			
			List<User> users = new ArrayList<>();
			while(rs.next()) {
				User user = User.createFromResultSet(rs);
				user.getUserRole().setName(rs.getString("user_role_name"));
				user.getUserRole().setDescription(rs.getString("user_role_description"));
				users.add(user);
			}
			return users;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatementForDelete(Connection conn, int userId) throws SQLException {
		var ps = conn.prepareStatement("DELETE FROM users WHERE id = ?");
		ps.setInt(1, userId);
		return ps;
	}
	
	public boolean delete(int userId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForDelete(conn, userId)){
			int result = ps.executeUpdate();
			return result == 1;
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

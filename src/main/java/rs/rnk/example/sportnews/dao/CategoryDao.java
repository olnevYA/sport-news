package rs.rnk.example.sportnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.rnk.example.sportnews.data.DBManager;
import rs.rnk.example.sportnews.model.Category;

public class CategoryDao {
	
	private DBManager dbManager;
	
	public CategoryDao() {
		try {
			dbManager = new DBManager();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<Category> findAll(){
		try(Connection conn = dbManager.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM categories ORDER BY id ASC");){
			List<Category> categories = new ArrayList<>();
			while(rs.next()) {
				categories.add(Category.createFromResultSet(rs));
			}
			return categories;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPsForFindByName(Connection conn, String name) throws SQLException {
		String sql = "SELECT * FROM categories WHERE name = ?";
		var ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		return ps;
	}
	
	public Category find(String name) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPsForFindByName(conn, name);
				ResultSet rs = ps.executeQuery();){
			Category category = null;
			if(rs.next()) {
				category = Category.createFromResultSet(rs);
			}
			return category;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPsForInsert(Connection conn, Category category) throws SQLException {
		String sql = "INSERT INTO categories (name, description) "
				+ "VALUES(?, ?)";
		var ps = conn.prepareStatement(sql);
		ps.setString(1, category.getName());
		ps.setString(2, category.getDescription());
		return ps;
	}
	
	public int insert(Category category) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPsForInsert(conn, category)){
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Category find(int categoryId) {
		return null;
	}
	
	private PreparedStatement createPsForFindWithNameDifferentId(Connection conn, String name, int notCategoryId) throws SQLException {
		String sql = "SELECT * FROM categories WHERE "
				+ "name = ? AND NOT id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		ps.setInt(2, notCategoryId);
		return ps;
	}
	
	public Category findWithNameDifferentId(String name, int notCategoryId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPsForFindWithNameDifferentId(conn, name, notCategoryId);
				ResultSet rs = ps.executeQuery();){
			Category category = null;
			
			if(rs.next()) {
				category = Category.createFromResultSet(rs);
			}
			
			return category;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPsForUpdate(Connection conn, Category newCategory) throws SQLException {
		String sql = "UPDATE categories SET name = ?, description = ? WHERE "
				+ "id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setString(1, newCategory.getName());
		ps.setString(2, newCategory.getDescription());
		ps.setInt(3, newCategory.getId());
		return ps;
	}
	
	public int update(Category category) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPsForUpdate(conn, category);
				){
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private PreparedStatement createPSForDelete(Connection conn, int categoryId) throws SQLException{
		String sql = "DELETE FROM categories WHERE id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setInt(1, categoryId);
		return ps;
	}

	public int delete(int categoryId) {
		try(var conn = dbManager.getConnection();
				var ps = createPSForDelete(conn, categoryId);){
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}


}

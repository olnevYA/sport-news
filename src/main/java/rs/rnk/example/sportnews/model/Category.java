package rs.rnk.example.sportnews.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
	
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
	
	public static Category createFromResultSet(ResultSet rs) throws SQLException {
		var category = new Category();
		
		category.setId(rs.getInt("id"));
		category.setName(rs.getString("name"));
		category.setDescription(rs.getString("description"));
		
		return category;
	}
	
	@Override
	public boolean equals(Object obj) {
		var other = (Category) obj;
		return this.id == other.id;
	}
	
}

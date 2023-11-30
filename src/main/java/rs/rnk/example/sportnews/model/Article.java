package rs.rnk.example.sportnews.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
	private static final int EXCERPT_SIZE = 730;
	private int id;
	private String title;
	private String content;
	private Category category;
	private Date published;
	private Date edited;
	private int views;
	private String imagePath;
	private String imageCredits;
	private User author;
	private boolean approved;
	
	public String getExcerpt() {
		if(content.length() < EXCERPT_SIZE) {
			return content;
		}
		return content.substring(0, EXCERPT_SIZE) + "...";
	}
	
	public String getExcerpt(int excerptSize) {
		return content.substring(0, excerptSize) + "...";
	}
	
	public boolean isApproved() {
		return approved;
	}



	public void setApproved(boolean approved) {
		this.approved = approved;
	}



	public Date getPublished() {
		return published;
	}



	public void setPublished(Date published) {
		this.published = published;
	}



	public Date getEdited() {
		return edited;
	}



	public void setEdited(Date edited) {
		this.edited = edited;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public Category getCategory() {
		return category;
	}

	

	public int getViews() {
		return views;
	}



	public void setViews(int views) {
		this.views = views;
	}



	public void setCategory(Category category) {
		this.category = category;
	}
	
	

	public String getImagePath() {
		return imagePath;
		
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	

	public String getImageCredits() {
		return imageCredits;
	}

	public void setImageCredits(String imageCredits) {
		this.imageCredits = imageCredits;
	}

	public String getPublishedFormatted() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy H:mm");
		return sdf.format(published);
	}
	
	public String getPublishedTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		return sdf.format(published);
	}
	
	public String getPublishedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(published);
	}
	
	public String getEditedTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		return sdf.format(edited);
	}
	
	public String gedEditedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(edited);
	}
	
	public String getEditedFormatted() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy H:mm");
		return sdf.format(edited);
	}
	
	public static Article createFromResultSet(ResultSet rs) throws SQLException {
		var article = new Article();
		article.setId(rs.getInt("id"));
		article.setTitle(rs.getString("title"));
		article.setContent(rs.getString("content"));
		article.setViews(rs.getInt("views"));
		
		Timestamp tsPublished = rs.getTimestamp("published");
		Timestamp tsEdited = rs.getTimestamp("edited");
		
		article.setPublished(new Date(tsPublished.getTime()));
		article.setEdited(new Date(tsEdited.getTime()));
		
		int categoryId = rs.getInt("category_id");
		var category = new Category();
		category.setId(categoryId);
		article.setCategory(category);
		article.setImagePath(rs.getString("image"));
		User author = new User();
		author.setId(rs.getInt("author_id"));
		article.setAuthor(author);
		article.setApproved(rs.getBoolean("approved"));
		article.setImageCredits(rs.getString("image_credits"));
		return article;
	}
}

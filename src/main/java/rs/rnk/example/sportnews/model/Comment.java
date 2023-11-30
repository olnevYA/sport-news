package rs.rnk.example.sportnews.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
	private int id;
	private Date dateTime;
	private boolean approved;
	private String content;
	private Article article;
	private String author;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	
	public String getFormattedDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy H:mm");
		return sdf.format(dateTime);
	}
	
	public static Comment createFromResultSet(ResultSet rs) throws SQLException { 
		var comment = new Comment();
		comment.setId(rs.getInt("id"));
		comment.setAuthor(rs.getString("author"));
		comment.setApproved(rs.getBoolean("approved"));
		comment.setContent(rs.getString("content"));
		var timestamp = rs.getTimestamp("date_time");
		comment.setDateTime(new Date(timestamp.getTime()));
		var article = new Article();
		article.setId(rs.getInt("article_id"));
		comment.setArticle(article);
		return comment;
	}
}

package rs.rnk.example.sportnews.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.rnk.example.sportnews.data.DBManager;
import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Comment;

public class CommentDao {
	
	private DBManager dbManager;
	
	public CommentDao() {
		try {
			dbManager = new DBManager();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int insert(Comment comment) {
		try(var conn = dbManager.getConnection();
				var ps = createPreparedStatementForInsert(conn, comment);){
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private PreparedStatement createPreparedStatementForInsert(Connection conn, Comment comment) throws SQLException {
		String sql = "INSERT INTO comments (author, content, approved, article_id, date_time) "
				+ "VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP())";
		var ps = conn.prepareStatement(sql);
		ps.setString(1,  comment.getAuthor());
		ps.setString(2, comment.getContent());
		ps.setBoolean(3, comment.isApproved());
		ps.setInt(4, comment.getArticle().getId());
		return ps;
	}
	
	public List<Comment> getNotApproved() {
		try(var conn = dbManager.getConnection();
				var stmt = conn.createStatement();
				var rs = stmt.executeQuery("SELECT * FROM comments WHERE approved = 0")) {
			List<Comment> comments = new ArrayList<>();
			while(rs.next()) {
				var comment = Comment.createFromResultSet(rs);
				comments.add(comment);
			}
			return comments;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Comment> getApproved() {
		try(var conn = dbManager.getConnection();
				var stmt = conn.createStatement();
				var rs = stmt.executeQuery("SELECT * FROM comments WHERE approved = 1")) {
			List<Comment> comments = new ArrayList<>();
			while(rs.next()) {
				var comment = Comment.createFromResultSet(rs);
				comments.add(comment);
			}
			return comments;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatementForGetApproved(Connection conn, Article article) throws SQLException {
		String sql = "SELECT * FROM comments WHERE approved = 1 AND article_id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setInt(1, article.getId());
		return ps;
	}
	
	public List<Comment> getApproved(Article article) {
		try(var conn = dbManager.getConnection();
				var ps = createPreparedStatementForGetApproved(conn, article);
				var rs = ps.executeQuery()) {
			List<Comment> comments = new ArrayList<>();
			while(rs.next()) {
				var comment = Comment.createFromResultSet(rs);
				comments.add(comment);
			}
			return comments;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean approve(int commentId) {
		try(var conn = dbManager.getConnection();
				var ps = createPreparedStatementForApproval(conn, commentId);) {
			int result = ps.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(int commentId) {
		try(var conn = dbManager.getConnection();
				var ps = createPreparedStatementForDelete(conn, commentId);) {
			int result = ps.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private PreparedStatement createPreparedStatementForApproval(Connection conn, int commentId) throws SQLException {
		var sql = "UPDATE comments SET approved = ? WHERE id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setBoolean(1, true);
		ps.setInt(2, commentId);
		return ps;
	}
	
	private PreparedStatement createPreparedStatementForDelete(Connection conn, int commentId) throws SQLException {
		var sql = "DELETE FROM comments WHERE id = ?";
		var ps = conn.prepareStatement(sql);
		ps.setInt(1, commentId);
		return ps;
	}
	
}

package rs.rnk.example.sportnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import rs.rnk.example.sportnews.data.DBManager;
import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.model.UserRole;

public class ArticleDao {
	
	private DBManager dbManager;
	
	public ArticleDao() {
		try {
			dbManager = new DBManager();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<Article> findByCategory(int categoryId){
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatement(conn, categoryId);
				ResultSet rs = ps.executeQuery();){
			List<Article> articles = new ArrayList<>();
			while(rs.next()) {
				articles.add(Article.createFromResultSet(rs));
			}
			return articles;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatement(Connection conn, int categoryId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM articles article WHERE article.category_id = ? AND article.approved = ?");
		ps.setInt(1, categoryId);
		ps.setBoolean(2, true);
		return ps;
	}
	
	public List<Article> findByCategory(Category category, int numberOfArticles){
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatement(conn, category, numberOfArticles);
				ResultSet rs = ps.executeQuery();){
			List<Article> articles = new ArrayList<>();
			while(rs.next()) {
				var article = Article.createFromResultSet(rs);
				article.setCategory(category);
				articles.add(article);
			}
			return articles;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatement(Connection conn, Category category, int numberOfArticles) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM articles article WHERE article.category_id = ? AND article.approved = ? LIMIT ?");
		ps.setInt(1, category.getId());
		ps.setBoolean(2, true);;
		ps.setInt(3, numberOfArticles);
		return ps;
	}
	
	private PreparedStatement createPreparedStatementForMostPopular(Connection conn, int categoryId, int numberOfArticles) throws SQLException {
		var preparedStatement = conn.prepareStatement("SELECT * FROM articles article WHERE article.category_id = ? AND article.approved = ? "
				+ "ORDER BY article.views DESC LIMIT ?");
		preparedStatement.setInt(1, categoryId);
		preparedStatement.setBoolean(2, true);
		preparedStatement.setInt(3, numberOfArticles);
		return preparedStatement;
	}
	
	private PreparedStatement createPreparedStatementForLatest(Connection conn, int categoryId, int numberOfArticles) throws SQLException {
		var preparedStatement = conn.prepareStatement("SELECT * FROM articles article WHERE article.category_id = ? AND article.approved = ? "
				+ "ORDER BY article.edited DESC LIMIT ?");
		if(numberOfArticles <= 0) {
			preparedStatement = conn.prepareStatement("SELECT * FROM articles article WHERE article.category_id = ? AND article.approved = ? "
					+ "ORDER BY article.edited DESC");
		}
		preparedStatement.setInt(1, categoryId);
		preparedStatement.setBoolean(2, true);
		if(numberOfArticles > 0) preparedStatement.setInt(3, numberOfArticles);
		return preparedStatement;
	}
	
	public List<Article> getLatest(int categoryId, int numberOfArticles) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForLatest(conn, categoryId, numberOfArticles);
				ResultSet rs = ps.executeQuery();){
			List<Article> articles = new ArrayList<>();
			while(rs.next()) {
				articles.add(Article.createFromResultSet(rs));
			}
			return articles;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Article> getMostPopular(int categoryId, int numberOfArticles) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForMostPopular(conn, categoryId, numberOfArticles);
				ResultSet rs = ps.executeQuery();){
			List<Article> articles = new ArrayList<>();
			while(rs.next()) {
				articles.add(Article.createFromResultSet(rs));
			}
			return articles;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedStatement createPreparedStatementForInsert(Connection conn, Article article) throws SQLException {
		String sql = "INSERT INTO articles (title, content, category_id, published, edited, views, image, approved, author_id, image_credits) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, article.getTitle());
		ps.setString(2, article.getContent());
		ps.setInt(3, article.getCategory().getId());
		Timestamp tsPublished = new Timestamp(article.getPublished().getTime());
		Timestamp tsEdited = new Timestamp(article.getEdited().getTime());
		ps.setTimestamp(4, tsPublished);
		ps.setTimestamp(5, tsEdited);
		ps.setInt(6, 0);
		ps.setString(7, article.getImagePath());
		ps.setBoolean(8, false);
		ps.setInt(9, article.getAuthor().getId());
		ps.setString(10, article.getImageCredits());
		return ps;
	}
	
	public int insert(Article article) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPreparedStatementForInsert(conn, article);){
			return ps.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<Article> getNotApproved(){
		try(Connection conn = dbManager.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT \n" +
						"  article.id, \n" +
						"  article.title, \n" +
						"  article.content, \n" +
						"  article.category_id, \n" +
						"  article.published, \n" +
						"  article.edited, \n" +
						"  article.views, \n" +
						"  article.image, \n" +
						"  article.approved, \n" +
						"  article.image_credits, \n" +
						"  article.author_id, \n" +
						"  category.id AS article_category_id, \n" +
						"  category.name AS article_category_name, \n" +
						"  category.description AS article_category_description, \n" +
						"  user.id as article_author_id, \n" +
						"  user.email AS article_author_email, \n" +
						"  user.username AS article_author_username, \n" +
						"  user.name AS article_author_name, \n" +
						"  user.user_role_id AS author_role_id, \n" +
						"  user_role.name AS author_role_name, \n" +
						"  user_role.description AS author_role_description \n" +
						"FROM \n" +
						"  articles article, \n" +
						"  categories category, \n" +
						"  users user, \n" +
						"  user_roles user_role \n" +
						"WHERE \n" +
						"  article.category_id = category.id AND \n" +
						"  article.author_id = user.id AND \n" +
						"  article.approved = 0 \n" +
						"GROUP BY \n" +
						"  article.id,\n" +
						"  article.title, \n" +
						"  article.content, \n" +
						"  article.category_id, \n" +
						"  article.published, \n" +
						"  article.edited, \n" +
						"  article.views, \n" +
						"  article.image, \n" +
						"  article.approved, \n" +
						"  article.image_credits, \n" +
						"  article.author_id, \n" +
						"  category.id,\n" +
						"  category.name,\n" +
						"  category.description,\n" +
						"  user.id,\n" +
						"  user.email,\n" +
						"  user.username,\n" +
						"  user.name,\n" +
						"  user.user_role_id,\n" +
						"  user_role.name,  -- Включаем user_role.name в GROUP BY\n" +
						"  user_role.description;")){
			List<Article> articles = new ArrayList<>();
			while(rs.next()) {
				Article article = Article.createFromResultSet(rs);
				Category category = article.getCategory();
				category.setName(rs.getString("article_category_name"));
				category.setDescription(rs.getString("article_category_description"));
				User author = article.getAuthor();
				author.setEmail(rs.getString("article_author_email"));
				author.setUsername(rs.getString("article_author_username"));
				author.setName(rs.getString("article_author_name"));
				UserRole authorRole = new UserRole();
				authorRole.setId(rs.getInt("author_role_id"));
				authorRole.setName(rs.getString("author_role_name"));
				authorRole.setDescription(rs.getString("author_role_description"));
				author.setUserRole(authorRole);
				articles.add(article);
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Article> getAllArticles(){
		try(Connection conn = dbManager.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT\n" +
						"article.id,\n" +
						"article.title,\n" +
						"article.content,\n" +
						"article.category_id,\n" +
						"article.published,\n" +
						"article.edited,\n" +
						"article.views,\n" +
						"article.image,\n" +
						"article.approved,\n" +
						"article.image_credits,\n" +
						"article.author_id,\n" +
						"category.id AS article_category_id,\n" +
						"category.name AS article_category_name,\n" +
						"category.description AS article_category_description,\n" +
						"user.id as article_author_id,\n" +
						"user.email AS article_author_email,\n" +
						"user.username AS article_author_username,\n" +
						"user.name AS article_author_name,\n" +
						"user.user_role_id AS author_role_id,\n" +
						"user_role.name AS author_role_name,\n" +
						"user_role.description AS author_role_description\n" +
						"FROM\n" +
						"articles article,\n" +
						"categories category,\n" +
						"users user,\n" +
						"user_roles user_role\n" +
						"WHERE\n" +
						"article.category_id = category.id AND\n" +
						"article.author_id = user.id AND\n" +
						"article.approved = 1\n" +
						"GROUP BY\n" +
						"article.id,\n" +
						"article.title,\n" +
						"article.content,\n" +
						"article.category_id,\n" +
						"article.published,\n" +
						"article.edited,\n" +
						"article.views,\n" +
						"article.image,\n" +
						"article.approved,\n" +
						"article.image_credits,\n" +
						"article.author_id,\n" +
						"category.id,\n" +
						"category.name,\n" +
						"category.description,\n" +
						"user.id,\n" +
						"user.email,\n" +
						"user.username,\n" +
						"user.name,\n" +
						"user.user_role_id,\n" +
						"user_role.name, \n" +
						"user_role.description;");){
			List<Article> articles = new ArrayList<>();
			while(rs.next()) {
				Article article = Article.createFromResultSet(rs);
				Category category = article.getCategory();
				category.setName(rs.getString("article_category_name"));
				category.setDescription(rs.getString("article_category_description"));
				User author = article.getAuthor();
				author.setEmail(rs.getString("article_author_email"));
				author.setUsername(rs.getString("article_author_username"));
				author.setName(rs.getString("article_author_name"));
				UserRole authorRole = new UserRole();
				authorRole.setId(rs.getInt("author_role_id"));
				authorRole.setName(rs.getString("author_role_name"));
				authorRole.setDescription(rs.getString("author_role_description"));
				author.setUserRole(authorRole);
				articles.add(article);
			}
			return articles;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	private PreparedStatement createPSForApprove(Connection conn, int articleId) throws SQLException {
		String sql = "UPDATE articles SET approved = 1 WHERE id = ?";
		var preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, articleId);
		return preparedStatement;
	}
	
	private PreparedStatement createPSForReject(Connection conn, int articleId) throws SQLException {
		String sql = "DELETE FROM articles WHERE id = ?";
		var preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, articleId);
		return preparedStatement;
	}
	
	
	public int approveArticle(int articleId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPSForApprove(conn, articleId);){
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int rejectArticle(int articleId) {
		try(Connection conn = dbManager.getConnection();
				PreparedStatement ps = createPSForReject(conn, articleId);){
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private PreparedStatement createPSForGetById(Connection conn, int articleId) throws SQLException{
		var ps = conn.prepareStatement("SELECT article.id, article.title, article.content, "
				+ "article.category_id, article.published, article.edited, article.image_credits,"
				+ " article.views, article.image, article.approved, article.author_id, "
				+ "category.id AS category_id, category.name AS category_name, "
				+ "category.description AS category_description, user.username AS author_username, "
				+ "user.name AS author_name, user.user_role_id AS author_role_id, "
				+ "user_role.id AS user_role_id, user_role.name AS user_role_name "
				+ "FROM articles article, categories category, users user, "
				+ "user_roles AS user_role WHERE article.category_id = category.id "
				+ "AND article.author_id = user.id AND user.user_role_id = user_role.id "
				+ "AND article.id = ? GROUP BY article.id");
		ps.setInt(1, articleId);
		return ps;
	}
	
	public Article get(int articleId) {
		try(var conn = dbManager.getConnection();
				var ps = createPSForGetById(conn, articleId);
				var rs = ps.executeQuery()){
			Article article = null;
			if(rs.next()) {
				article = Article.createFromResultSet(rs);
				Category category = article.getCategory();
				category.setName(rs.getString("category_name"));
				User author = article.getAuthor();
				author.setUsername(rs.getString("author_username"));
				author.setName(rs.getString("author_name"));
				UserRole authorRole = new UserRole();
				authorRole.setId(rs.getInt("author_role_id"));
				authorRole.setName(rs.getString("user_role_name"));
				author.setUserRole(authorRole);
			}
			return article;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

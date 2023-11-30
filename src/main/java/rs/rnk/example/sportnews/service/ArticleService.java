package rs.rnk.example.sportnews.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.rnk.example.sportnews.dao.ArticleDao;
import rs.rnk.example.sportnews.dao.CategoryDao;
import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.util.Messages;

public class ArticleService extends Service {
	
	private ArticleDao articleDao;
	private CategoryDao categoryDao;
	
	public ArticleService( ) {
		articleDao = new ArticleDao();
		categoryDao = new CategoryDao();
	}
	
	public boolean add(MessageService messageService, Article article) {
		int result = articleDao.insert(article);
		
		if(result < 0) {
			messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
		}
		
		return result > 0;
	}
	
	
	public Map<Category, List<Article>> getCategoryArticles(int numberOfArticles){
		var categoryArticles = new HashMap<Category, List<Article>>();
		List<Category> categories = categoryDao.findAll();
		for(var category : categories) {
			List<Article> articles = articleDao.findByCategory(category, numberOfArticles);
			categoryArticles.put(category, articles);
		}
		return categoryArticles;
	}
	
	public boolean approveArticle(int articleId) {
		return  articleDao.approveArticle(articleId) == 1;
	}
	
	public boolean rejectArticle(int articleId) {
		return articleDao.rejectArticle(articleId) == 1;
	}
	
	public List<Article> getMostPopular(int categoryId, int numberOfArticles) {
		return articleDao.getMostPopular(categoryId, numberOfArticles);
	}
	
	public List<Article> getNotApproved() {
		return articleDao.getNotApproved();
	}
	
	public List<Article> getAllArticles(){
		return articleDao.getAllArticles();
	}
	
	public List<Article> getLatest(int categoryId, int numberOfArticles) {
		return articleDao.getLatest(categoryId, numberOfArticles);
	}
	
	public Article get(int articleId) {
		return articleDao.get(articleId);
	}
	
	public List<Article> getAllArticlesForCategory(int categoryId){
		return null;
	}
	
	public List<Article> getArticlesForCategory(int categoryId, int numberOfArticles){
		return null;
	}
}

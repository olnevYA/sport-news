package rs.rnk.example.sportnews.service;

import rs.rnk.example.sportnews.dao.CommentDao;
import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Comment;

import java.util.List;

public class CommentService extends Service{
	
	private CommentDao commentDao;

	public CommentService() {
		commentDao = new CommentDao();
	}
	
	public List<Comment> getCommentsForArticle(Article article) {
		return commentDao.getApproved(article);
	}

	public boolean add(Comment comment) {
		return commentDao.insert(comment) > 0;
	}

	public List<Comment> getNotApproved() {
		return commentDao.getNotApproved();
	}

	public List<Comment> getApproved() {
		return commentDao.getApproved();
	}

	public boolean approveComment(int commentId) {
		return commentDao.approve(commentId);
	}

	public boolean rejectComment(int commentId) {
		return commentDao.delete(commentId);
	}
	
	

}

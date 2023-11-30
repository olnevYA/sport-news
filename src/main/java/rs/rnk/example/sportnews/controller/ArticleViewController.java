package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.service.ArticleService;
import rs.rnk.example.sportnews.service.CommentService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/article"})
public class ArticleViewController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var articleIdStr = req.getParameter("articleId");
		var articleId = Integer.parseInt(articleIdStr);
		
		var rsf = new RequestServiceFinder(req);
		var articleService = (ArticleService) rsf.find("articleService");
		var commentService = (CommentService) rsf.find("commentService");
		
		
		var article = articleService.get(articleId);
		var comments = commentService.getCommentsForArticle(article);
		req.setAttribute("article", article);
		req.setAttribute("comments", comments);
		
		NavigationService navigationService = (NavigationService) rsf.find("navigationService");
		navigationService.setContext(getServletContext());
		
		var commentStatus = req.getParameter("commentStatus");
		if(commentStatus != null) {
			var messageService = (MessageService) rsf.find("messageService");
			if(commentStatus.equalsIgnoreCase("success")) {
				messageService.addMessage(Messages.ADD_COMMENT_SUCCESS, MessageType.SUCCESS);
			} else if(commentStatus.equalsIgnoreCase("error")) {
				messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
			}
		}
		
		req.getRequestDispatcher("/WEB-INF/views/article.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

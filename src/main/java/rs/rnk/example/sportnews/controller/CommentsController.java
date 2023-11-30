package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Comment;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.service.CommentService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/comments/do/*", "/dashboard/comments/do/*"})
public class CommentsController extends HttpServlet{

	private static final long serialVersionUID = 6533839767230556513L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServiceFinder rsf = new RequestServiceFinder(req);
		var commentService = (CommentService) rsf.find("commentService");
		var messageService = (MessageService) rsf.find("messageService");
		
		String action = req.getPathInfo().substring(1);
		
		if(action.equalsIgnoreCase("approve")) {
			var commentIdStr = req.getParameter("commentId");
			var commentId = Integer.parseInt(commentIdStr);
			var approve = req.getParameter("approve");
			var reject = req.getParameter("reject");
			
			if(approve != null) {
				boolean result = commentService.approveComment(commentId);
				if(!result) {
					messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
				} else {
					messageService.addMessage(Messages.APPROVE_COMMENT_SUCCESS, MessageType.SUCCESS);
				}
			}
			if(reject != null) {
				boolean result = commentService.rejectComment(commentId);
				if(!result) {
					messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
				} else {
					messageService.addMessage(Messages.REJECT_COMMENT_SUCCESS, MessageType.SUCCESS);
				}
			}
			req.getRequestDispatcher("/dashboard/comments/approve").forward(req, resp);
		} else if(action.equalsIgnoreCase("add")) {
			var addCommentSubmit = req.getParameter("addCommentSubmit");
			if(addCommentSubmit != null) {
				var content = req.getParameter("commentContent");
				var author = req.getParameter("commentAuthor");
				var articleIdStr = req.getParameter("articleId");
				var comment = new Comment();
				comment.setApproved(false);
				comment.setAuthor(author);
				comment.setContent(content);
				var article = new Article();
				article.setId(Integer.parseInt(articleIdStr));
				comment.setArticle(article);
				var result = commentService.add(comment);
				
				resp.sendRedirect(getServletContext().getContextPath() + "/article?articleId=" + 
						articleIdStr + "&commentStatus=" + (result ? "success" : "error"));
				return;
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}

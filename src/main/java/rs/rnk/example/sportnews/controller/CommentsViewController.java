package rs.rnk.example.sportnews.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Comment;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.service.CommentService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/dashboard/comments/*"})
public class CommentsViewController extends HttpServlet {

	private static final long serialVersionUID = 5155627927771708934L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServiceFinder ssf = new SessionServiceFinder(req.getSession());
		UserService userService = (UserService) ssf.find("userService");
		ServiceFinder rsf = new RequestServiceFinder(req);
		MessageService messageService = (MessageService) rsf.find("messageService");
		CommentService commentService = (CommentService) rsf.find("commentService");
		User currentUser = userService.getCurrentUser();
		
		if(currentUser == null) {
			messageService.addMessage(Messages.NOT_LOGGED_IN, MessageType.ERROR);
			req.getRequestDispatcher("/login").forward(req, resp);
			return;
		}else if(currentUser.getUserRole().getId() == 7 || currentUser.getUserRole().getId() == 6) {
			String action = req.getPathInfo().substring(1).toLowerCase();
			
			if(action.equalsIgnoreCase("approve")) {
				List<Comment> notApproved = commentService.getNotApproved();
				req.setAttribute("notApproved", notApproved);
				req.setAttribute("action", "approve");
			} else if(action.equalsIgnoreCase("list")) {
				List<Comment> approvedArticles = commentService.getApproved();
				req.setAttribute("approved", approvedArticles);
				req.setAttribute("action", "list");
			}
			

			req.setAttribute("dashboardView", "comments");
			NavigationService navigationService = (NavigationService) new RequestServiceFinder(req).find("navigationService");
			navigationService.setCurrentNavItem(new NavigationItem("Comments"));
			req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
			return;
			
		} else {
			messageService.addMessage(Messages.ACCESS_ERROR, MessageType.ERROR);
			req.getRequestDispatcher("/logout").forward(req, resp);
			return;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

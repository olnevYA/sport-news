package rs.rnk.example.sportnews.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.service.ArticleService;
import rs.rnk.example.sportnews.service.CategoryService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/dashboard/articles/*"})
public class ArticlesViewController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceFinder ssf = new SessionServiceFinder(request.getSession());
		UserService userService = (UserService) ssf.find("userService");
		ServiceFinder rsf = new RequestServiceFinder(request);
		MessageService messageService = (MessageService) rsf.find("messageService");
		ArticleService articleService = (ArticleService) rsf.find("articleService");
		User currentUser = userService.getCurrentUser();
		
		if(currentUser == null) {
			messageService.addMessage(Messages.NOT_LOGGED_IN, MessageType.ERROR);
			request.getRequestDispatcher("/login").forward(request, response);
			return;
		} else if(currentUser.getUserRole().getId() == 7 || currentUser.getUserRole().getId() == 6) {
			String action = request.getPathInfo().substring(1).toLowerCase();
			
			if(action.equalsIgnoreCase("approve")) {
				List<Article> notApproved = articleService.getNotApproved();
				request.setAttribute("notApproved", notApproved);
				request.setAttribute("action", "approve");
			} else if(action.equalsIgnoreCase("list")) {
				List<Article> approvedArticles = articleService.getAllArticles();
				request.setAttribute("approvedArticles", approvedArticles);
				request.setAttribute("action", "list");
			} else if(action.equalsIgnoreCase("add")) {
				request.setAttribute("action", "add");
			} else if(action.equalsIgnoreCase("details")) {
				String articleIdStr = request.getParameter("articleId");
				request.setAttribute("action", "list/details");
				int articleId = Integer.parseInt(articleIdStr);
				request.setAttribute("articleDetailId", articleId);
				
				Article articleDetail = articleService.get(articleId);
				request.setAttribute("articleDetail", articleDetail);
			} else if(action.equalsIgnoreCase("edit")) {
				String articleIdStr = request.getParameter("articleId");
				request.setAttribute("action", "list/details");
				int articleId = Integer.parseInt(articleIdStr);
				request.setAttribute("editEnabled", true);
				request.setAttribute("articleDetailId", articleId);
			}
			
			CategoryService categoryService = (CategoryService) rsf.find("categoryService");
			List<Category> allCategories = categoryService.getAllCategories();
			request.setAttribute("allCategories", allCategories);
			request.setAttribute("dashboardView", "articles");
			NavigationService navigationService = (NavigationService) new RequestServiceFinder(request).find("navigationService");
			navigationService.setCurrentNavItem(new NavigationItem("Articles"));
			request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
			return;
			
		} else {
			messageService.addMessage(Messages.ACCESS_ERROR, MessageType.ERROR);
			request.getRequestDispatcher("/logout").forward(request, response);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}

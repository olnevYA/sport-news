package rs.rnk.example.sportnews.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.service.CategoryService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = { "/dashboard/categories/*" })
public class CategoriesViewController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServiceFinder ssf = new SessionServiceFinder(req.getSession());
		UserService userService = (UserService) ssf.find("userService");
		ServiceFinder rsf = new RequestServiceFinder(req);
		MessageService messageService = (MessageService) rsf.find("messageService");

		User currentUser = userService.getCurrentUser();
		CategoryService categoryService = (CategoryService) rsf.find("categoryService");

		if (currentUser == null) {
			messageService.addMessage(Messages.NOT_LOGGED_IN, MessageType.ERROR);
			req.getRequestDispatcher("/login").forward(req, resp);
			return;
		} else if (currentUser.getUserRole().getId() == 7 || currentUser.getUserRole().getId() == 6
				|| currentUser.getUserRole().getId() == 5) {
			String action = req.getPathInfo().substring(1).toLowerCase();
			if (action.equalsIgnoreCase("add")) {
				req.setAttribute("action", "add");
			} else if (action.equalsIgnoreCase("list")) {
				List<Category> categories = categoryService.getAllCategories();
				req.setAttribute("allCategories", categories);
				req.setAttribute("action", "list");
			} else if (action.equalsIgnoreCase("list/edit")) {
				String articleIdStr = req.getParameter("categoryId");

				if (articleIdStr != null) {
					List<Category> categories = categoryService.getAllCategories();
					req.setAttribute("allCategories", categories);
					req.setAttribute("editCategoryId", articleIdStr);
					req.setAttribute("action", "list/edit");
				} else {
					req.getRequestDispatcher("/dashboard/categories/list").forward(req, resp);
				}
			} else {
				req.getRequestDispatcher("/dashboard/categories/add").forward(req, resp);
			}
			req.setAttribute("dashboardView", "categories");
			NavigationService navigationService = (NavigationService) rsf.find("navigationService");
			navigationService.setCurrentNavItem(new NavigationItem("Categories"));
			req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);

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

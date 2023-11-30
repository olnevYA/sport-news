package rs.rnk.example.sportnews.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/dashboard/users/*"})
public class UsersViewController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServiceFinder ssf = new SessionServiceFinder(request.getSession());
		UserService userService = (UserService) ssf.find("userService");
		MessageService messageService = (MessageService) new RequestServiceFinder(request).find("messageService");
		User currentUser = userService.getCurrentUser();
		
		if(currentUser == null) {
			messageService.addMessage(Messages.NOT_LOGGED_IN, MessageType.ERROR);
			request.getRequestDispatcher("/login").forward(request, response);
			return;
		} else if(currentUser.getUserRole().getId() == 7 || currentUser.getUserRole().getId() == 6) {
			String action = request.getPathInfo().substring(1).toLowerCase();
			
			if(action.equalsIgnoreCase("approve")) {
				List<User> notApproved = userService.findUsers(false);
				request.setAttribute("notApproved", notApproved);
				request.setAttribute("action", "approve");
			} else if(action.equalsIgnoreCase("list")) {
				List<User> approvedUsers = userService.findUsers(true);
				request.setAttribute("approvedUsers", approvedUsers);
				request.setAttribute("action", "list");
			} else if(action.equalsIgnoreCase("add")) {
				request.setAttribute("action", "add");
			} else if(action.equalsIgnoreCase("list/edit")) {
				String userIdStr = request.getParameter("userId");
				if(userIdStr != null) {
					List<User> approvedUsers = userService.findUsers(true);
					request.setAttribute("approvedUsers", approvedUsers);
					request.setAttribute("editUserId", userIdStr);
					request.setAttribute("action", "list/edit");
				} else {
					request.getRequestDispatcher("/dashboard/users/list").forward(request, response);
				}
			}
			request.setAttribute("dashboardView", "users");
			NavigationService navigationService = (NavigationService) new RequestServiceFinder(request).find("navigationService");
			navigationService.setCurrentNavItem(new NavigationItem("Users"));
			request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
			
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

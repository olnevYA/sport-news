package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.model.UserRole;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/dashboard/users/do/*"})
public class UsersController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private void approveUser(int userId, UserService userService, MessageService messageService, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean result = userService.approveUser(userId);
		if(!result) {
			messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
		} else {
			messageService.addMessage(Messages.APPROVE_USER_SUCCESS, MessageType.SUCCESS);
		}
		request.getRequestDispatcher("/dashboard/users/approve").forward(request, response);
	}
	
	private void rejectUser(int userId, UserService userService, MessageService messageService, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean result = userService.delete(userId);
		request.setAttribute("dashboardView", "users");
		if(!result) {
			messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);	
		} else {
			messageService.addMessage(Messages.REJECT_USER_SUCCESS, MessageType.SUCCESS);
		}
		request.getRequestDispatcher("/dashboard/users/approve").forward(request, response);
	}
	
	private void addUser(UserService userService, MessageService messageService, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String userRoleIdStr = request.getParameter("userRoleId");
		int userRoleId = Integer.parseInt(userRoleIdStr);
		
		request.setAttribute("addUsername", username);
		request.setAttribute("addEmail", email);
		request.setAttribute("addName", name);
		request.setAttribute("addPassword", password);
		request.setAttribute("addUserRoleId", userRoleId);
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		UserRole userRole = new UserRole();
		userRole.setId(userRoleId);
		user.setUserRole(userRole);
		user.setApproved(true);
		boolean success = userService.signUp(messageService, user);
		
		if(success) {
			messageService.addMessage(Messages.ADD_USER_SUCCESS, MessageType.SUCCESS);
		}
		request.getRequestDispatcher("/dashboard/users/add").forward(request, response);
	}
	
	private void edit(UserService userService, MessageService messageService, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userIdStr = request.getParameter("userId");
		String username = request.getParameter("editUsername");
		String email = request.getParameter("editEmail");
		String password = request.getParameter("editPassword");
		String name = request.getParameter("editName");
		String userRoleStr = request.getParameter("editUserRole");
		String approvedStr = request.getParameter("editApproved");
		String submitEdit = request.getParameter("submitEdit");
		
		if(submitEdit != null) {
			int userRoleId = Integer.parseInt(userRoleStr);
			
			UserRole userRole = new UserRole();
			userRole.setId(userRoleId);
			int userId = Integer.parseInt(userIdStr);
			User user = new User();
			user.setUserRole(userRole);
			user.setId(userId);
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(password);
			user.setName(name);
			boolean approved = approvedStr != null && approvedStr.equals("on");
			user.setApproved(approved);
			
			boolean success = userService.edit(user, messageService);
			if(success) {
				messageService.addMessage(Messages.EDIT_USER_SUCCESS, MessageType.SUCCESS);
			}
		}
		
		request.getRequestDispatcher("/dashboard/users/list").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceFinder ssf = new SessionServiceFinder(request.getSession());
		UserService userService = (UserService) ssf.find("userService");
		MessageService messageService = (MessageService) new RequestServiceFinder(request).find("messageService");

		// action = approve, add, edit
		String action = request.getPathInfo().substring(1);
		
		if(action.equalsIgnoreCase("approve")) {
			String userIdStr = request.getParameter("userId");
			int userId = Integer.parseInt(userIdStr);
			String approve = request.getParameter("approve");
			String reject = request.getParameter("reject");
			if(approve != null)
				this.approveUser(userId, userService, messageService, request, response);
			if(reject != null)
				this.rejectUser(userId, userService, messageService, request, response);
		} else if(action.equalsIgnoreCase("add")) {
			String add = request.getParameter("add");
			if(add != null)
				this.addUser(userService, messageService, request, response);
		} else if(action.equalsIgnoreCase("edit")) {
			String submitEdit = request.getParameter("submitEdit");
			
			if(submitEdit != null) {
				this.edit(userService, messageService, request, response);
			}
			
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/dashboard/users/" + request.getPathInfo().substring(1)).forward(request, response);
	}
}

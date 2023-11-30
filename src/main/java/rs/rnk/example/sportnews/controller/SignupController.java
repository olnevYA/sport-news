package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Message;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.model.UserRole;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/sign-up"})
public class SignupController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NavigationService navigationService = (NavigationService) new RequestServiceFinder(request).find("navigationService");
		navigationService.setContext(getServletContext());
		navigationService.setCurrentNavItem(new NavigationItem("Sign up"));
		new SessionServiceFinder(request.getSession()).find("userService");

		request.setAttribute("navigationService", navigationService);
		request.getServletContext().getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String userRoleIdStr = request.getParameter("userRoleId");
		int userRoleId = Integer.parseInt(userRoleIdStr);
		
		request.setAttribute("regUsername", username);
		request.setAttribute("regEmail", email);
		request.setAttribute("regName", name);
		request.setAttribute("regPassword", password);
		request.setAttribute("regUserRoleId", userRoleId);
		
		ServiceFinder ssf = new SessionServiceFinder(request.getSession());
		UserService userService = (UserService)  ssf.find("userService");
		
		ServiceFinder rsf = new RequestServiceFinder(request);
		MessageService messageService = (MessageService) rsf.find("messageService");
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		UserRole userRole = new UserRole();
		userRole.setId(userRoleId);
		user.setUserRole(userRole);
		user.setApproved(false);
		
		boolean success = userService.signUp(messageService, user);
		NavigationService navigationService = (NavigationService) new RequestServiceFinder(request).find("navigationService");
		navigationService.setContext(getServletContext());
		navigationService.setCurrentNavItem(new NavigationItem("Sign up"));

		request.setAttribute("navigationService", navigationService);
		
		if(!success) {
			request.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(request, response);
			return;
		}
		
		messageService.addMessage(new Message(MessageType.INFO, Messages.SIGN_UP_SUCCESS));
		request.getRequestDispatcher("/WEB-INF/views/sign-in.jsp").forward(request, response);
		
	}

}

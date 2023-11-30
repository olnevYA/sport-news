package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import rs.rnk.example.sportnews.model.Message;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/do/login"})
public class LoginController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		HttpSession session = request.getSession();
		request.setAttribute("loginUsername", username);
		
		ServiceFinder rsf = new RequestServiceFinder(request);
		MessageService messageService = (MessageService) rsf.find("messageService");
		
		if(username == null || username.equals("")) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.USERNAME_EMPTY));
		}
		if(password == null || password.equals("")) {
			messageService.addMessage(new Message(MessageType.ERROR, Messages.PASSWORD_EMPTY));
		}
		
		if(messageService.getNumberOfMessages() > 0) {
			request.getRequestDispatcher("/login").forward(request, response);
			return;
		}
		
		ServiceFinder ssf = new SessionServiceFinder(session);
		UserService userService = (UserService) ssf.find("userService");
		
		boolean success = userService.logIn(session, messageService, username, password);
		if(!success) {
			request.getRequestDispatcher("/login").forward(request, response);
			return;
		}
		User user = userService.getCurrentUser();
		switch(user.getUserRole().getId()) {
		case 7: case 6: case 5: case 4:
			response.sendRedirect(getServletContext().getContextPath() + "/dashboard");
			return;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/login").forward(request, response);
	}
}

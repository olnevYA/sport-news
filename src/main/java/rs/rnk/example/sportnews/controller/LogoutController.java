package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Message;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.model.User;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = ((UserService) new SessionServiceFinder(request.getSession()).find("userService")).getCurrentUser();
		if(user != null) {
			request.getSession().invalidate();
			MessageService messageService = (MessageService) new RequestServiceFinder(request).find("messageService");
			messageService.addMessage(new Message(MessageType.SUCCESS, Messages.LOGOUT_SUCCESS));
			request.getRequestDispatcher("/login").forward(request, response);
			return;
		} else {
			response.sendRedirect(getServletContext().getContextPath() + "/login");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

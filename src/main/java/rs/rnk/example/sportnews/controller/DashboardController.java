package rs.rnk.example.sportnews.controller;

import java.io.IOException;

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
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = "/dashboard")
public class DashboardController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = (UserService) new SessionServiceFinder(request.getSession()).find("userService");
		User currentUser = userService.getCurrentUser();
		NavigationService navigationService = (NavigationService) new RequestServiceFinder(request).find("navigationService");
		
		
		if(currentUser == null) {
			MessageService messageService = (MessageService) new RequestServiceFinder(request).find("messageService");
			messageService.addMessage(Messages.NOT_LOGGED_IN, MessageType.ERROR);
			
			navigationService.setContext(getServletContext());
			navigationService.setCurrentNavItem(new NavigationItem("Sign in"));

			request.setAttribute("navigationService", navigationService);
			request.getRequestDispatcher("/WEB-INF/views/sign-in.jsp").forward(request, response);
		} else if(currentUser.getUserRole().getId() == 7) {
			// Administrator 
			navigationService.setCurrentNavItem(new NavigationItem("Users"));
			request.getRequestDispatcher("/dashboard/users/approve").forward(request, response);
			
		} else if (currentUser.getUserRole().getId() == 6) {
			// Manager
			navigationService.setCurrentNavItem(new NavigationItem("Articles"));
			request.getRequestDispatcher("/dashboard/articles/approve").forward(request, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

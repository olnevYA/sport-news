package rs.rnk.example.sportnews.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;


@WebServlet(urlPatterns = {"/login"})
public class LoginViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = (UserService) new SessionServiceFinder(request.getSession()).find("userService");
		if(userService.getCurrentUser() != null) {
			switch(userService.getCurrentUser().getUserRole().getId()) {
			case 7: case 6: case 5: case 4:
				response.sendRedirect(getServletContext().getContextPath() + "/dashboard");
				return;
			}
		}
		
		NavigationService navigationService = (NavigationService) new RequestServiceFinder(request).find("navigationService");
		navigationService.setContext(getServletContext());
		navigationService.setCurrentNavItem(new NavigationItem("Sign in"));

		request.setAttribute("navigationService", navigationService);
		
		request.getServletContext().getRequestDispatcher("/WEB-INF/views/sign-in.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

}

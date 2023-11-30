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
import rs.rnk.example.sportnews.service.ServiceFinder;

@WebServlet(urlPatterns = {"/home"})
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceFinder rsf = new RequestServiceFinder(request);
		rsf.find("articleService");

		NavigationService navigationService = (NavigationService) rsf.find("navigationService");
		navigationService.setContext(getServletContext());
		navigationService.setCurrentNavItem(new NavigationItem("Home", getServletContext().getContextPath() + "/home"));

		request.getServletContext().getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

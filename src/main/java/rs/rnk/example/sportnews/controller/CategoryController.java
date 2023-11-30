package rs.rnk.example.sportnews.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.NavigationItem;
import rs.rnk.example.sportnews.service.ArticleService;
import rs.rnk.example.sportnews.service.NavigationService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;


@WebServlet(urlPatterns = {"/category"})
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int categoryId = Integer.parseInt(request.getParameter("id"));
		ServiceFinder sf = new RequestServiceFinder(request);
		ArticleService articleService = (ArticleService) sf.find("articleService");
		List<Article> mostPopular = articleService.getMostPopular(categoryId, 4);
		List<Article> latest = articleService.getLatest(categoryId, -1);
		
		request.setAttribute("mostPopular", mostPopular);
		request.setAttribute("latest", latest);
		
		NavigationService navigationService = (NavigationService) sf.find("navigationService");
		navigationService.setContext(getServletContext());
		navigationService.setCurrentNavItem(new NavigationItem(categoryId, "Category"));

		request.setAttribute("navigationService", navigationService);
		
		request.getRequestDispatcher("/WEB-INF/views/category.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

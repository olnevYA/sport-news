package rs.rnk.example.sportnews.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import rs.rnk.example.sportnews.model.Article;
import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.service.ArticleService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.service.SessionServiceFinder;
import rs.rnk.example.sportnews.service.UserService;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = {"/dashboard/articles/do/*"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
		maxFileSize = 1024 * 1024 * 5,
		maxRequestSize = 1024 * 1024 * 5 * 5)
public class ArticlesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceFinder rsf = new RequestServiceFinder(request);
		ServiceFinder ssf = new SessionServiceFinder(request.getSession());
		ArticleService articleService = (ArticleService) rsf.find("articleService");
		MessageService messageService = (MessageService) new RequestServiceFinder(request).find("messageService");
		UserService userService = (UserService) ssf.find("userService");
		String action = request.getPathInfo().substring(1);
		
		if(action.equals("approve")) {
			String articleIdStr = request.getParameter("articleId");
			int articleId = Integer.parseInt(articleIdStr);
			String approve = request.getParameter("approve");
			String reject = request.getParameter("reject");
			if(approve != null) {
				boolean result = articleService.approveArticle(articleId);
				if(!result) {
					messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
				} else {
					messageService.addMessage(Messages.APPROVE_ARTICLE_SUCCESS, MessageType.SUCCESS);
				}
			}
			if(reject != null) {
				boolean result = articleService.rejectArticle(articleId);
				if(!result) {
					messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
				} else {
					messageService.addMessage(Messages.REJECT_ARTICLE_SUCCESS, MessageType.SUCCESS);
				}
			}
			
			request.getRequestDispatcher("/dashboard/articles/approve").forward(request, response);
		} else if(action.equals("add")) {
			String addArticleSubmit = request.getParameter("addArticleSubmit");
			if(addArticleSubmit != null) {
				String title = request.getParameter("addArticleTitle");
				String categoryIdStr = request.getParameter("addArticleCategoryId");
				String content = request.getParameter("addArticleContent");
				String imageCredits = request.getParameter("addArticleImageCredits");
				
				request.setAttribute("addArticleTitle", title);
				request.setAttribute("addArticleCategoryId", categoryIdStr);
				request.setAttribute("addArticleContent", content);
				request.setAttribute("addArticleImageCredits", imageCredits);
				
				Article article = new Article();
				article.setTitle(title);
				article.setContent(content);
				article.setPublished(new Date());
				article.setEdited(new Date());
				article.setViews(0);
				var category = new Category();
				category.setId(Integer.parseInt(categoryIdStr));
				article.setCategory(category);
				article.setAuthor(userService.getCurrentUser());
				article.setImageCredits(imageCredits);
				
				var uploadDirectory = getServletContext().getAttribute("snUploadDirectory");
				var fileName = "";
				var currentDate = new Date();
				var dateFormatter = new SimpleDateFormat("dd_MM_yyy_h_mm_ss");
				var formattedDate = dateFormatter.format(currentDate);
				var extension = "";
				try {
					for (Part part: request.getParts()) {
						
						if(part != null && part.getSize() > 0) {
							
							fileName = part.getSubmittedFileName();
							
							if(fileName != null) {
								int dotIndex = fileName.lastIndexOf('.');
								extension = fileName.substring(dotIndex);
							}
							
							var contentType = part.getContentType();
							
							if(contentType == null || !contentType.equalsIgnoreCase("image/jpeg")
									|| fileName == null) {
								continue;
							}
							part.write(uploadDirectory + File.separator + formattedDate + extension);
						}
						
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					messageService.addMessage(Messages.FILE_UPLOAD_ERROR, MessageType.ERROR);
					request.getRequestDispatcher("/dashboard/articles/add").forward(request, response);
					return;
				}
				if(extension != null && !extension.equalsIgnoreCase("")) {
					article.setImagePath(formattedDate + extension);
				} else {
					article.setImagePath("");
				}
				boolean success = articleService.add(messageService, article);
				
				if(success) {
					messageService.addMessage(Messages.ADD_ARTICLE_SUCCESS, MessageType.SUCCESS);
				}
				
				request.getRequestDispatcher("/dashboard/articles/add").forward(request, response);
			}
		} else if(action.equals("edit")) {
			
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/dashboard/articles/" + request.getPathInfo().substring(1)).forward(request, response);
	}
	
}

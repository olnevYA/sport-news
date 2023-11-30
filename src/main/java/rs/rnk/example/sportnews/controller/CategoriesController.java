package rs.rnk.example.sportnews.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.service.CategoryService;
import rs.rnk.example.sportnews.service.MessageService;
import rs.rnk.example.sportnews.service.RequestServiceFinder;
import rs.rnk.example.sportnews.service.ServiceFinder;
import rs.rnk.example.sportnews.util.Messages;

@WebServlet(urlPatterns = { "/dashboard/categories/do/*" })
public class CategoriesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServiceFinder rsf = new RequestServiceFinder(req);
		MessageService messageService = (MessageService) rsf.find("messageService");
		CategoryService categoryService = (CategoryService) rsf.find("categoryService");

		String action = req.getPathInfo().substring(1).toLowerCase();

		if (action.equalsIgnoreCase("add")) {
			String addCategoryName = req.getParameter("addCategoryName");
			String addCategoryDesc = req.getParameter("addCategoryDesc");

			req.setAttribute("addCategoryName", addCategoryName);
			req.setAttribute("addCategoryDesc", addCategoryDesc);

			if (addCategoryName == null || addCategoryName.equals("") || addCategoryDesc == null
					|| addCategoryDesc.equals("")) {
				messageService.addMessage(Messages.MANDATORY_FIELDS_ERROR, MessageType.ERROR);
				req.getRequestDispatcher("/dashboard/categories").forward(req, resp);
				return;
			}

			Category category = new Category();
			category.setName(addCategoryName);
			category.setDescription(addCategoryDesc);

			categoryService.add(category, messageService);

			req.getRequestDispatcher("/dashboard/categories/add").forward(req, resp);
		} else if (action.equalsIgnoreCase("edit")) {
			String submitEdit = req.getParameter("submitEdit");

			if (submitEdit != null) {
				String editCategoryName = req.getParameter("editCategoryName");
				String editCategoryDescription = req.getParameter("editCategoryDesc");
				String editCategoryIdStr = req.getParameter("categoryId");
				Category category = new Category();
				category.setId(Integer.parseInt(editCategoryIdStr));
				category.setName(editCategoryName);
				category.setDescription(editCategoryDescription);
				
				boolean result = categoryService.edit(category, messageService);
				
				if(result) {
					messageService.addMessage(Messages.EDIT_CATEGORY_SUCCESS, MessageType.SUCCESS);
				}
				
			}
			
			req.getRequestDispatcher("/dashboard/categories/list").forward(req, resp);
			return;

		} else if(action.equalsIgnoreCase("delete")) {
			String submitDelete = req.getParameter("submitDelete");
			
			if(submitDelete != null) {
				String categoryIdStr = req.getParameter("categoryId");
				boolean result = categoryService.delete(Integer.parseInt(categoryIdStr));
				if(result) {
					messageService.addMessage(Messages.DELETE_CATEGORY_SUCCESS, MessageType.SUCCESS);
				} else {
					messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.SUCCESS);
				}
				
				req.getRequestDispatcher("/dashboard/categories/list").forward(req, resp);
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/dashboard/categories/add");
	}
}

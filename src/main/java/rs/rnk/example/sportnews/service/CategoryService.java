package rs.rnk.example.sportnews.service;

import java.util.List;

import rs.rnk.example.sportnews.dao.CategoryDao;
import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.MessageType;
import rs.rnk.example.sportnews.util.Messages;

public class CategoryService extends Service{
	
	private CategoryDao categoryDao;
	
	public CategoryService() {
		categoryDao = new CategoryDao();
	}
	
	public List<Category> getAllCategories(){
		return categoryDao.findAll();
	}
	
	public boolean add(Category category, MessageService messageService) {
		Category dbCategory = categoryDao.find(category.getName());
		if(dbCategory == null) {
			int result = categoryDao.insert(category);
			if(result == 1) {
				messageService.addMessage(Messages.ADD_CATEGORY_SUCCESS, MessageType.SUCCESS);
				return true;
			} else {
				messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
				return false;
			}
		} else {
			messageService.addMessage(Messages.CATEGORY_WITH_NAME_EXISTS, MessageType.ERROR);
			return false;
		}
		
	}
	
	public boolean edit(Category newCategory, MessageService messageService) {
		int categoryId = newCategory.getId();
		boolean error = false;
		Category dbCategory = categoryDao.findWithNameDifferentId(newCategory.getName(), categoryId);
		if(dbCategory != null) {
			messageService.addMessage(Messages.CATEGORY_WITH_NAME_EXISTS, MessageType.ERROR);
			error = true;
		}
		
		
		if(error) {
			return false;
		}
		
		int result = categoryDao.update(newCategory);
		if(result < 0) {
			messageService.addMessage(Messages.UNKNOWN_ERROR, MessageType.ERROR);
		}
		
		return result > 0;
	}
	
	public boolean delete(int categoryId) {
		return categoryDao.delete(categoryId) == 1;
	}
	
}

package rs.rnk.example.sportnews.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import rs.rnk.example.sportnews.model.Category;
import rs.rnk.example.sportnews.model.NavigationItem;

public class NavigationService extends Service {
	
	private NavigationItem currentNavItem;
	private List<NavigationItem> navigationItems;
	
	private ServletContext context;
	
	public NavigationService() {
		navigationItems = new ArrayList<>();
	}
	
	public void setContext(ServletContext context) {
		this.context = context;
	}
	
	public void updateNavigationItems() {
		CategoryService categoryService = (CategoryService) new ServiceFinder.DefaultServiceFinder().find("categoryService");
		List<Category> categories = categoryService.getAllCategories();
		for(var category : categories) {
			this.addNavItem(new NavigationItem(category.getId(), category.getName(), this.context.getContextPath() + "/category?id=" + category.getId()));
		}
	}
	
	public List<NavigationItem> getNavigationItems(){
		this.updateNavigationItems();
		return this.navigationItems;
	}
	
	public void addNavItem(NavigationItem item) {
		navigationItems.add(item);
	}

	public NavigationItem getCurrentNavItem() {
		return currentNavItem;
	}

	public void setCurrentNavItem(NavigationItem currentNavItem) {
		this.currentNavItem = currentNavItem;
	}
	
}

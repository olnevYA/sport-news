package rs.rnk.example.sportnews.service;

import javax.servlet.ServletContext;

public class ContextServiceFinder implements ServiceFinder{
	
	private ServletContext context;
	
	public ContextServiceFinder(ServletContext context) {
		this.context = context;
	}

	@Override
	public Service find(String serviceName) {
		var service = (Service) context.getAttribute(serviceName);
		if(service == null) {
			service = new ServiceFinder.DefaultServiceFinder().find(serviceName);
			context.setAttribute(serviceName, service);
			return service;
		} else {
			return service;
		}
	}
	
	

}

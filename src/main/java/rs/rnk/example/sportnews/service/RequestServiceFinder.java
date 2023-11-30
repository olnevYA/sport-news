package rs.rnk.example.sportnews.service;

import javax.servlet.http.HttpServletRequest;

public class RequestServiceFinder implements ServiceFinder {
	private HttpServletRequest request;
	
	public RequestServiceFinder(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public Service find(String serviceName) {
		var service = (Service) request.getAttribute(serviceName);
		if(service == null) {
			ServiceFinder sf = new ServiceFinder.DefaultServiceFinder();
			service = sf.find(serviceName);
			request.setAttribute(serviceName, service);
			return service;
		} else {
			return service;
		}
		
	}
	
}

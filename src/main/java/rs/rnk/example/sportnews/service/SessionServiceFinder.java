package rs.rnk.example.sportnews.service;

import javax.servlet.http.HttpSession;

public class SessionServiceFinder implements ServiceFinder{
	
	private HttpSession session;
	
	public SessionServiceFinder(HttpSession session) {
		this.session = session;
	}

	@Override
	public Service find(String serviceName) {
		var service = (Service) session.getAttribute(serviceName);
		if(service == null) {
			service = new ServiceFinder.DefaultServiceFinder().find(serviceName);
			session.setAttribute(serviceName, service);
			return service;
		} else {
			return service;
		}
		
	}
	
	

}

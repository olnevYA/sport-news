package rs.rnk.example.sportnews.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class WebContextListener implements ServletContextListener{
	
	private ServletContext context = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		context = sce.getServletContext();
		String uploadDirectory = System.getenv("snUploadDirectory");
		
		if(uploadDirectory == null) {
			uploadDirectory = context.getInitParameter("snUploadDirectory");
		}
		
		var uploadDir = new File(uploadDirectory);
		if(!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		context.setAttribute("snUploadDirectory", uploadDirectory);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		context = sce.getServletContext();
	}
	
}

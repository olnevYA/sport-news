package rs.rnk.example.sportnews.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/images/*" })
public class ImageController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		var imageName = req.getPathInfo().substring(1);

		var out = resp.getOutputStream();

		var context = getServletContext();
		var uploadDirectory = (String) context.getAttribute("snUploadDirectory");

		var fullImagePath = uploadDirectory + File.separator + imageName;
		if (imageName == null || imageName.isEmpty()) {
			fullImagePath = uploadDirectory + File.separator + "placeholder.png";
		}

		var mime = context.getMimeType(fullImagePath);

		if (mime == null) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		resp.setContentType(mime);
		File file = new File(fullImagePath);
		resp.setContentLength((int) file.length());

		var in = new FileInputStream(file);

		byte[] buffer = new byte[1024];
		int count = 0;

		while ((count = in.read(buffer)) >= 0) {
			out.write(buffer, 0, count);
		}

		out.close();
		in.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

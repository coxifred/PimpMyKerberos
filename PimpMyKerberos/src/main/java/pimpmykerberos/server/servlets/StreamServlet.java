package pimpmykerberos.server.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pimpmykerberos.utils.Fonctions;

public class StreamServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 474719652648270932L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("multipart/x-mixed-replace; boundary=mjpegstream");
		ServletOutputStream out;
		out = response.getOutputStream();
		String url = request.getParameter("url");
		Fonctions.trace("DBG", "Start streaming to " + url, "StreamServlet");
		InputStream fin = new URL(url).openStream();
		BufferedInputStream bin = new BufferedInputStream(fin);
		BufferedOutputStream bout = new BufferedOutputStream(out);
		int ch = 0;
		;
		while ((ch = bin.read()) != -1) {
			bout.write(ch);
		}

		bin.close();
		fin.close();
		bout.close();
		out.close();
	}

}

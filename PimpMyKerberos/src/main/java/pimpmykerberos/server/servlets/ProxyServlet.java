package pimpmykerberos.server.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ProxyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 474719652648270932L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url=request.getParameter("url");
		proxyResponse(url,request,response);
	}

	protected void proxyResponse(String targetURL, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpGet get = new HttpGet(targetURL);

		/*
		 * Proxy the request headers from the browser to the target server
		 */
		Enumeration<String> headers = request.getHeaderNames();
		while (headers != null && headers.hasMoreElements()) {
			String headerName = (String) headers.nextElement();

			String headerValue = request.getHeader(headerName);

			if (headerValue != null) {
				get.addHeader(headerName, headerValue);
			}
		}

		/* Make a request to the target server */
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse aResponse = httpClient.execute(get);
		/*
		 * Set the status code
		 */
		response.setStatus(aResponse.getStatusLine().getStatusCode());

		/*
		 * proxy the response headers to the browser
		 */
		Header responseHeaders[] = aResponse.getAllHeaders();
		for (int i = 0; i < responseHeaders.length; i++) {
			String headerName = responseHeaders[i].getName();
			String headerValue = responseHeaders[i].getValue();

			if (headerValue != null) {
				response.addHeader(headerName, headerValue);
			}
		}

		/*
		 * Proxy the response body to the browser
		 */
		OutputStream out = response.getOutputStream();
		aResponse.getEntity().writeTo(out);

	}

}

package pimpmykerberos.server;

import java.io.IOException;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import pimpmykerberos.core.Core;
import pimpmykerberos.server.servlets.AdminServlet;
import pimpmykerberos.server.servlets.CameraServlet;
import pimpmykerberos.server.servlets.DisplayServlet;
import pimpmykerberos.server.servlets.StreamServlet;
import pimpmykerberos.server.websocket.JettyWebSocketServlet;
import pimpmykerberos.utils.Fonctions;
import pimpmykerberos.utils.JettyLogger;

public class JettyWebServer extends Thread {

	Integer port;
	String ip;
	Server serverWeb;
	Server serverSocket;
	Boolean http2;

	public JettyWebServer(String ip, Integer port, Boolean http2) {
		this.ip = ip;
		this.port = port;
		this.http2 = http2;
		start();
	}

	public void run() {

		org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());

		serverWeb = new Server();
		serverSocket = new Server();
		http2 = false;
		try {

			Fonctions.trace("DBG", "Starting classic server", "CORE");
			startWebSimple(serverWeb, serverSocket);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void startWebSimple(Server serverWeb, Server ServerSocket) throws Exception, InterruptedException {

		setContextsWeb(serverWeb);
		// setContextsSocket(ServerSocket);

		SslContextFactory contextFactory = new SslContextFactory();

		String jettyDistKeystore = "ssl/keystore";
		String keyStorePath = getClass().getClassLoader().getResource(jettyDistKeystore).toExternalForm();

		contextFactory.setKeyStorePath(keyStorePath);
		contextFactory.setKeyStorePassword("pimpMyGps");
		contextFactory.setKeyManagerPassword("pimpMyGps");
		// contextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

		SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(contextFactory,
				org.eclipse.jetty.http.HttpVersion.HTTP_1_1.toString());

		HttpConfiguration config_ssl = new HttpConfiguration();
		config_ssl.setSecureScheme("https");
		config_ssl.setSecurePort(Core.getInstance().getWebServerPort());
		config_ssl.setOutputBufferSize(32786);
		config_ssl.setRequestHeaderSize(8192);
		config_ssl.setResponseHeaderSize(8192);
		HttpConfiguration sslConfiguration = new HttpConfiguration(config_ssl);
		sslConfiguration.addCustomizer(new SecureRequestCustomizer());
		HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(sslConfiguration);

		ServerConnector connector = new ServerConnector(serverWeb, sslConnectionFactory, httpConnectionFactory);
		connector.setHost(ip);
		connector.setPort(Core.getInstance().getWebServerPort());
		serverWeb.addConnector(connector);

//		// Socket
//		ServerConnector connectorSocket = new ServerConnector(serverSocket, sslConnectionFactory,
//				httpConnectionFactory);
//		connectorSocket.setPort(Core.getInstance().getWebSocketPort());
//		serverSocket.addConnector(connectorSocket);

		serverWeb.start();
		// serverSocket.start();
		serverWeb.join();
		// serverSocket.join();

	}

	private void setContextsWeb(Server server) throws IOException {
		String webInterfaceDir = getClass().getClassLoader().getResource("webInterface").toExternalForm();
		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
		context.setResourceBase(webInterfaceDir);
		context.addServlet(AdminServlet.class, "/admin/*");
		context.addServlet(CameraServlet.class, "/camera/*");
		context.addServlet(DisplayServlet.class, "/display/*");
		context.addServlet(StreamServlet.class, "/stream/*");
		context.addServlet(new ServletHolder(new JettyWebSocketServlet()), "/hello");
		server.setHandler(context);
	}

//	private void setContextsSocket(Server server) {
//
//		ServletContextHandler socketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
//
//		server.setHandler(socketContext);
//
//		// Add a websocket to a specific path spec
//		ServletHolder holderSocket = new ServletHolder("ws-adminwebsocket", JettyWebSocketServlet.class);
//		socketContext.addServlet(holderSocket, "/hello/*");
//		socketContext.setContextPath("/");
//
//	}

	public Server getServerWeb() {
		return serverWeb;
	}

	public void setServerWeb(Server serverWeb) {
		this.serverWeb = serverWeb;
	}

	public Server getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(Server serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getHttp2() {
		return http2;
	}

	public void setHttp2(Boolean http2) {
		this.http2 = http2;
	}

}

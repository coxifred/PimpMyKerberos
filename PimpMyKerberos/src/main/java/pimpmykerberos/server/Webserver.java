package pimpmykerberos.server;

import java.util.Set;
import java.util.TreeSet;

import pimpmykerberos.utils.Fonctions;

public class Webserver {

	String ip;
	Integer port;
	JettyWebServer webSocketThread;

	public static Set<Integer> icons = new TreeSet<Integer>();

	public void startWebSocket(Boolean http2) {
		Fonctions.trace("INF", "Starting JettyWebServer on ip " + ip + " listening on port " + port + " hardware",
				"CORE");
		webSocketThread = new JettyWebServer(ip, getPort(), http2);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public JettyWebServer getWebSocketThread() {
		return webSocketThread;
	}

	public void setWebSocketThread(JettyWebServer webSocketThread) {
		this.webSocketThread = webSocketThread;
	}
}

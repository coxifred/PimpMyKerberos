package pimpmykerberos.server.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;

import com.google.gson.Gson;

import pimpmykerberos.server.messages.Message;
import pimpmykerberos.utils.Fonctions;



@ServerEndpoint(value = "/hello")
public class AdminWebSocket implements WebSocketListener {
	
	private static Map<String, Session> allSessions=new HashMap<String,Session>();

	Boolean close = false;

	@OnWebSocketClose
	public void onWebSocketClose(int statusCode, String reason) {
		close = true;
		Fonctions.trace("DBG", "onWebSocketClose " + statusCode + " " + reason, "CORE");
	}

	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		Fonctions.trace("DBG", "onWebSocketConnect", "CORE");
		allSessions.put(session.toString(), session);
	}

	@OnWebSocketError
	public void onWebSocketError(Throwable e) {
		Fonctions.trace("DBG", "onWebSocketError", "CORE");

	}

	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		Fonctions.trace("DBG", "onWebSocketBinary", "CORE");
	}

	@OnMessage
	public void onWebSocketText(String message) {
		Fonctions.trace("DBG", "onWebSocketText " + message, "CORE");
		if (!close) {}


	}

	public synchronized static void broadcastMessage(Message message) {
		Gson aGson=new Gson();
		String messageJson=aGson.toJson(message);
		List<String> toClean=new ArrayList<String>();
		for (Session sess : allSessions.values()) {
			try {
				sess.getRemote().sendString(messageJson);
			} catch (Exception e) {
				Fonctions.trace("ERR", "Error while sending to session " + sess.getRemoteAddress().getHostName() + " removing session from map considering as vanished", "AdminWebSocket");
				toClean.add(sess.toString());
			}
		}
		for ( String aSession:toClean)
		{
			allSessions.remove(aSession);
		}
	}
	

	public Boolean getClose() {
		return close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}

	public static Map<String, Session> getAllSessions() {
		return allSessions;
	}

	public static void setAllSessions(Map<String, Session> allSessions) {
		AdminWebSocket.allSessions = allSessions;
	}



}

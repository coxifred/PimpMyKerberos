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
	
	private static Map<Integer, Session> allSessions=new HashMap<Integer,Session>();

	Boolean close = false;

	@OnWebSocketClose
	public void onWebSocketClose(int statusCode, String reason) {
		close = true;
		Fonctions.trace("DBG", "onWebSocketClose " + statusCode + " " + reason, "CORE");
	}

	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		Fonctions.trace("DBG", "onWebSocketConnect", "CORE");
		allSessions.put(session.hashCode(), session);
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
		List<Integer> toClean=new ArrayList<Integer>();
		Fonctions.trace("DBG",allSessions.size() + " sessions in cache before sending message","AdminWebSocket");
		for (Session sess : getAllSessions().values()) {
			try {
				sess.getRemote().sendString(messageJson);
			} catch (Exception e) {
				Fonctions.trace("ERR", "Error while sending to session [" + sess.toString() + "] " + sess.getRemoteAddress().getHostName() + " removing session from map considering as vanished", "AdminWebSocket");
				toClean.add(sess.hashCode());
			}
		}
		for ( Integer aSession:toClean)
		{
			allSessions.remove(aSession);
		}
		Fonctions.trace("DBG",allSessions.size() + " sessions in cache after sending message","AdminWebSocket");
	}
	

	public Boolean getClose() {
		return close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}

	public static synchronized Map<Integer, Session> getAllSessions() {
		return allSessions;
	}

	public static void setAllSessions(Map<Integer, Session> allSessions) {
		AdminWebSocket.allSessions = allSessions;
	}

	



}

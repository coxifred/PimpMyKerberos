package pimpmykerberos.server.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pimpmykerberos.beans.Log;
import pimpmykerberos.beans.Memory;
import pimpmykerberos.beans.Space;
import pimpmykerberos.beans.User;
import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class AdminServlet extends AbstractServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8546977083372271300L;

	static Map<String, List<Date>> ip2Connect = new HashMap<String, List<Date>>();

	public AdminServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		perform(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			perform(request, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void perform(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("action") != null) {
			String action = request.getParameter("action");
			switch (action) {
			case "login":
				authenfication(request, response);
				break;
			case "logout":
				logout(request, response);
				break;
			case "isLogged":
				isLogged(request, response);
				break;
			case "getMe":
				getMe(request, response);
				break;
			case "getLogs":
				getLogs(request, response);
				break;
			case "setNightMode":
				setNightMode(request, response);
				break;
			case "getCore":
				getCore(request,response);
				break;
			case "setMuteMode":
				setMuteMode(request, response);
				break;
			case "debug":
				debug(request, response);
				break;
			case "getMemory":
				getMemory(request, response);
				break;
			case "getSpace":
				getSpace(request, response);
				break;
			case "save":
				downloadConfig(request, response);
				break;
			case "getWebSocketPort":
				getWebSocketPort(request, response);
				break;
			}

		}
	}

	private void getCore(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				response.getWriter().write(toGson(Core.getInstance()));
			}
		}
		
	}

	private void setMuteMode(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String muteMode=request.getParameter("muteMode");
				try {
					Integer mute=Integer.decode(muteMode);
					Core.getInstance().setMuteMode(mute);
				} catch (Exception e)
				{
					Fonctions.trace("ERR", "Couldn't parse muteMode to Integer " + muteMode, "AdminServlet");
				}
			}
		}
		
	}

	private void setNightMode(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String nightMode=request.getParameter("nightMode");
				try {
					Integer night=Integer.decode(nightMode);
					Core.getInstance().setNightMode(night);
				} catch (Exception e)
				{
					Fonctions.trace("ERR", "Couldn't parse nightMode to Integer " + nightMode, "AdminServlet");
				}
			}
		}
		
	}

	private void downloadConfig(HttpServletRequest request, HttpServletResponse response) {
		try {
			FileInputStream in;
			OutputStream out = response.getOutputStream();
			in = new FileInputStream(Core.getInstance().getCoreFile());
			response.setContentType("text/xml");
	        // Make sure to show the download dialog
	        response.setHeader("Content-disposition","attachment; filename=" + Fonctions.getDateFormat(new Date(),null) + "_aCore.xml");
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
		} catch (Exception e) {
			Fonctions.trace("ERR", "Couldn't export file " + Core.getInstance().getCoreFile(), "AdminServlet");
		}

	}

	private void getMemory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Fonctions.trace("DBG", "Total memory:" + Fonctions.getTotalMemory() + ",Used memory:"
				+ Fonctions.getUsedMemory() + ",Free memory:" + Fonctions.getFreeMemory(), "CORE");
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				Memory aMemoryBean = new Memory();
				aMemoryBean.setFree(Fonctions.getFreeMemory().replace(",", "."));
				aMemoryBean.setMax(Fonctions.getTotalMemory().replace(",", "."));
				aMemoryBean.setUsed(Fonctions.getUsedMemory().replace(",", "."));
				response.getWriter().write(toGson(aMemoryBean));
			}
		}
		response.getWriter().write("");

	}

	private void getSpace(HttpServletRequest request, HttpServletResponse response) throws IOException {

		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				File aDockerCompose = new File(Core.getInstance().getDockerCompose().getDockerComposeFile());
				if (aDockerCompose != null && aDockerCompose.exists()) {
					File upperFile = aDockerCompose.getParentFile();
					if (upperFile != null) {
						Space aSpace = new Space();
						aSpace.setFree(String.format("%.2f", Float.valueOf(upperFile.getFreeSpace() / 1024 / 1024)));
						aSpace.setMax(String.format("%.2f", Float.valueOf(upperFile.getTotalSpace() / 1024 / 1024)));
						aSpace.setUseable(
								String.format("%.2f", Float.valueOf(upperFile.getUsableSpace() / 1024 / 1024)));
						aSpace.setUsed(String.format("%.2f", Float.valueOf(
								upperFile.getTotalSpace() / 1024 / 1024 - upperFile.getFreeSpace() / 1024 / 1024)));
						response.getWriter().write(toGson(aSpace));
					}
				}

			}
		}
		response.getWriter().write("");

	}

	private void getWebSocketPort(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println(Core.getInstance().getWebSocketPort());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getLogs(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		String include = request.getParameter("include");
		String exclude = request.getParameter("exclude");

		if (requester != null) {
			if (requester.getName().equals("admin")) {
				response.getWriter().write(Log.getSimpleLogs(include, exclude, Core.getInstance().getLogs()));
			} else {

			}
		}
		response.getWriter().write("");

	}

	private void debug(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		String debugMode = request.getParameter("debug");
		if (requester != null && requester.getName().equals("admin")) {
			if ("false".equals(debugMode)) {
				Core.getInstance().setDebug(false);
				Fonctions.trace("INFO", "Farewell to Debug mode", "CORE");
			}
			if ("true".equals(debugMode)) {
				Core.getInstance().setDebug(true);
				Fonctions.trace("INFO", "Respawn Debug mode", "CORE");
			}

		}
		response.getWriter().write(Core.getInstance().getDebug().toString());

	}

	private void getMe(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			response.getWriter().write(toGson(requester));
		}
	}

	private void authenfication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String user = request.getParameter("user");
		String passwd = request.getParameter("passwd");
		manageFail2Ban(request);
		if (User.isAuthentified(user, passwd)) {
			Fonctions.trace("INF", "Connection from ip successfull for user " + user, "CORE");
			request.getSession().setAttribute("USER", Core.getInstance().getUsers().get(user));
			response.getWriter().write("/main.html");
		} else {
			Fonctions.trace("ERR", "Connection from ip failed for user " + user, "CORE");
			response.getWriter().write("/index.html");
		}
	}

	private void manageFail2Ban(HttpServletRequest request) {
		String remoteIp = request.getRemoteAddr();
		List<Date> aDateList = new ArrayList<Date>();
		if (ip2Connect.containsKey(remoteIp)) {
			aDateList = ip2Connect.get(remoteIp);
		}
		Date aDate = new Date();
		aDateList.add(aDate);
		ip2Connect.put(remoteIp, aDateList);
		// Counting acces for last 10 minutes, if > 10, then slowing
		Calendar start = Calendar.getInstance();
		start.add(Calendar.MINUTE, -10);
		Calendar end = Calendar.getInstance();
		// Discovering dates
		List<Date> newDates = new ArrayList<Date>();
		for (Date theDate : aDateList) {
			if (theDate.after(start.getTime()) && theDate.before(end.getTime())) {
				newDates.add(theDate);
			}
		}
		ip2Connect.put(remoteIp, newDates);
		Fonctions.trace("INF", newDates.size() + " connection(s) within 10 minutes from ip " + remoteIp + " @" + aDate,
				"CORE");
		if (newDates.size() > 10) {
			Fonctions.trace("INF", "Slowing connection, by waiting " + newDates.size() + " * 120 secs for ip "
					+ remoteIp + " @" + aDate, "CORE");
			Fonctions.attendre(newDates.size() * 120000);
		}
		Fonctions.trace("INF", "Trying Connection from ip " + remoteIp + " @" + aDate, "CORE");
	}

	private void isLogged(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (request.getSession().getAttribute("USER") != null) {
			response.getWriter().write("true");
		} else {
			response.getWriter().write("false");
		}

	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("USER");
	}

}
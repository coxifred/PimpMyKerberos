package pimpmykerberos.server.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pimpmykerberos.beans.Directory;
import pimpmykerberos.beans.User;
import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class CameraServlet extends AbstractServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8546977083372271300L;

	public CameraServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Fonctions.trace("DBG", "doGet from CameraServlet", "CameraServlet");
		try {
			perform(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		Fonctions.trace("DBG", "doPost from CameraServlet", "CameraServlet");
		try {
			perform(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void perform(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("action") != null) {
			String action = request.getParameter("action");
			switch (action) {
			case "getDir":
				getDir(request, response);
				break;
			case "isDockerCompose":
				isDockerCompose(request, response);
				break;
			case "setPath":
				setPath(request, response);
				break;
			case "getCameras":
				getCameras(request, response);
				break;
			case "getTimeLine":
				getTimeLine(request, response);
				break;
			case "getFileByHour":
				getFileByHour(request, response);
				break;
			case "seek":
				seek(request, response);
				break;
			case "cleanCam":
				cleanCam(request, response);
				break;
			case "cleanAll":
				cleanAll(request, response);
				break;
			case "fromDay":
				fromDay(request,response);
				break;

			}
		}

	}

	private void fromDay(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String date=request.getParameter("date");
				if ( date != null)
				{
				 try{
				 
				  Date aDate=Fonctions.getDateFormat(date+"_23:59:50", "yyyy-MM-dd_hh:mm:ss");
				  int time_offset;
			      Calendar cal = Calendar.getInstance();
			      time_offset = Calendar.DST_OFFSET - Calendar.ZONE_OFFSET;
			      cal.setTime(aDate);
			      cal.add(Calendar.HOUR, -time_offset);
			      cal.getTime();
			      Fonctions.trace("DBG", "Display from  " + cal.getTime(), "CameraServlet");
				  String startCrunch=Long.toString( cal.getTimeInMillis());
				  String size = request.getParameter("size");
   				  response.getWriter().write(toGson(Core.getInstance().getTimeline().extract(startCrunch,size)));
				 }catch (Exception e)
				 {
					 Fonctions.trace("ERR", "Couldn't parse date as Date " + date, "CameraServlet");
				 }
				}
			}
		}
	}

	private void seek(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String idCam=request.getParameter("id");
				if ( idCam != null )
				{
				 try{
				  String startCrunch=Core.getInstance().seekCamera(idCam).toString();
				  Fonctions.trace("DBG", "Seeking camera " + idCam + " to " + startCrunch + " " , "CameraServlet");
				  String size = request.getParameter("size");
   				  response.getWriter().write(toGson(Core.getInstance().getTimeline().extract(startCrunch,size)));
				 }catch (Exception e)
				 {
					 Fonctions.trace("ERR", "Couldn't seek camera " + idCam, "CameraServlet");
				 }
				}
			}
		}
		
	}
	
	private void getFileByHour(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				 try{
				  response.getWriter().write(toGson(Core.getInstance().getTimeline().extractByHour()));
				 }catch (Exception e)
				 {
					 Fonctions.trace("ERR", "Couldn't extract files amout by hour ", "CameraServlet");
				 }
				
			}
		}
		
	}

	private void cleanAll(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				try {
  				    Integer count=Core.getInstance().cleanAll();
					response.getWriter().write(count.toString());
				} catch (IOException e) {
					 Fonctions.trace("ERR", "Couldn't clean all cam", "CameraServlet");
				}
			}
		}
		
	}
	
	private void cleanCam(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String idCam=request.getParameter("id");
				if ( idCam != null )
				{
				 try{
					 Integer count=Core.getInstance().cleanCamera(idCam);
					 response.getWriter().write(count.toString());
				 }catch (Exception e)
				 {
					 Fonctions.trace("ERR", "Couldn't clean idCam " + idCam, "CameraServlet");
				 }
				}
			}
		}
		
	}

	private void setPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String path = request.getParameter("path");
				Core.getInstance().setKerberosioPath(path);
				Core.getInstance().computeKerberosioPath(true);
				Core.getInstance().saveCore();
			}
		}
		
	}
	
	private void getCameras(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				requester=Core.getInstance().getUsers().get("admin");
				response.getWriter().write(toGson(requester.getCameras().values()));
			}
		}
	}
	
	private void getTimeLine(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String startCrunch = request.getParameter("startCrunch");
				String size = request.getParameter("size");
				response.getWriter().write(toGson(Core.getInstance().getTimeline().extract(startCrunch,size)));
			}
		}
	}
	
	
	private void isDockerCompose(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String sub = request.getParameter("dir");
				File aDockerComposeFile=new File(sub + "/" + "docker-compose.yml");
				if ( aDockerComposeFile.exists())
						{
							response.getWriter().write("true");
						}else
						{
							response.getWriter().write("false");
						}
			}
		}
		
	}

	private static void getDir(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String sub = request.getParameter("sub");
				List<Directory> dirs=new ArrayList<Directory>();
				if ( sub != null && new File(sub).isDirectory() )
				{
					extracted(sub, dirs);
				}else
				{
					extracted("/", dirs);
				}
				response.getWriter().write(toGson(dirs));
			}
		}
	}

	private static void extracted(String sub, List<Directory> dirs) {
		File aFile=new File(sub);
		for ( File aSubFile:aFile.listFiles())
		{
			if (aSubFile.isDirectory())
			{
				Directory aDirectory=new Directory();
				aDirectory.setName(aSubFile.getAbsolutePath());
				aDirectory.setParentName(sub);
				dirs.add(aDirectory);
			}
		}
	}
}
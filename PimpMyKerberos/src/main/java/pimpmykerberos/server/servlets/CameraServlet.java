package pimpmykerberos.server.servlets;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import pimpmykerberos.beans.Directory;
import pimpmykerberos.beans.KeepCam;
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
			case "getKeep":
				getKeep(request, response);
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
			case "keepCam":
				keepCam(request, response);
				break;
			case "trainCam":
				trainCam(request, response);
				break;
			case "purgeFile":
				purgeFile(request, response);
				break;
			case "cleanAll":
				cleanAll(request, response);
				break;
			case "purgeAllKeep":
				purgeAllKeep(request, response);
				break;
			case "fromDay":
				fromDay(request, response);
				break;

			}
		}

	}

	private void getKeep(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				
					try {
						// Trying create directory
						List<KeepCam> keepCams=new ArrayList<KeepCam>();
						Fonctions.trace("DBG", "Looking into " + Core.getInstance().getKerberosioPath()
								+ "/pimpMyKerberos/keep", "CameraServlet");
						File aFile = new File(Core.getInstance().getKerberosioPath() + "/pimpMyKerberos/keep");
						if ( aFile.exists() )
						{
							for (File aKeepFile:aFile.listFiles())
							{
								KeepCam aKeepCam=new KeepCam();
								aKeepCam.setPathToMedia(aKeepFile.getAbsolutePath());
								aKeepCam.setTime(new Date(aKeepFile.lastModified()));
								aKeepCam.setId(UUID.randomUUID().toString().replaceAll(" ", ""));
								keepCams.add(aKeepCam);
							}
							
						}
						Fonctions.trace("DBG", keepCams.size() + " keepFiles found", "CameraServlet");
						response.getWriter().write(toGson(keepCams));
					} catch (Exception e) {
						Fonctions.trace("ERR", "Couldn't read " + Core.getInstance().getKerberosioPath()
								+ "/pimpMyKerberos/keep " + e.getMessage(), "CameraServlet");
					}
				
			}
		}
		
	}

	private void keepCam(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String id = request.getParameter("id");
				if (id != null) {
					try {
						// Trying create directory
						Fonctions.trace("DBG", "Creating directory " + Core.getInstance().getKerberosioPath()
								+ "/pimpMyKerberos/keep", "CameraServlet");
						File aFile = new File(Core.getInstance().getKerberosioPath() + "/pimpMyKerberos/keep");
						Boolean success = aFile.mkdirs();
						Fonctions.trace("DBG", "Creation directory status " + success, "CameraServlet");
						// Saving file int File
						Fonctions.trace("DBG", "Copying file " + id + " into " + Core.getInstance().getKerberosioPath()
							+ "/pimpMyKerberos/keep", "CameraServlet");
						FileUtils.copyFileToDirectory(new File(id), aFile,true);

					} catch (Exception e) {
						Fonctions.trace("ERR", "Couldn't save file " + id + " to " + Core.getInstance().getKerberosioPath()
								+ "/pimpMyKerberos/keep", "CameraServlet");
					}
				}
			}
		}
	}
	
	
	private void purgeAllKeep(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
					try {
						Fonctions.trace("DBG", "Cleaning directory " + Core.getInstance().getKerberosioPath() + "/pimpMyKerberos/keep", "CameraServlet");
						File aDir=new File( Core.getInstance().getKerberosioPath() + "/pimpMyKerberos/keep");
						if ( aDir.isDirectory() )
						{
							for ( File aFile:aDir.listFiles())
							{
								FileUtils.forceDelete(aFile);
							}
						}
						else
						{
							Fonctions.trace("ERR", Core.getInstance().getKerberosioPath() + "/pimpMyKerberos/keep doesn't exists", "CameraServlet");
						}

					} catch (Exception e) {
						Fonctions.trace("ERR", "Error while cleaning keep directory " + e.getMessage(), "CameraServlet");
					}
				
			}
		}
	}


	private void purgeFile(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String id = request.getParameter("id");
				if (id != null) {
					try {
						// Trying create directory
						Fonctions.trace("DBG", "Cleaning file " + id, "CameraServlet");
						if (id.startsWith(Core.getInstance().getKerberosioPath())) {
							File aFile = new File(id);
							FileUtils.forceDelete(aFile);
						} else

						{
							Fonctions.trace("ERR", id + " don't start with " + Core.getInstance().getKerberosioPath()
									+ " security bypass", "CameraServlet");
						}

					} catch (Exception e) {
						Fonctions.trace("ERR", "Couldn't remove file " + id, "CameraServlet");
					}
				}
			}
		}
	}

	private void trainCam(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String id = request.getParameter("id");
				String model = request.getParameter("model");
				if (id != null && model != null) {
					try {
						// Trying create directory
						Fonctions.trace("DBG", "Creating directory " + Core.getInstance().getKerberosioPath()
							+ "/pimpMyKerberos/train/" + model, "CameraServlet");
						File aFile = new File(Core.getInstance().getKerberosioPath() + "/pimpMyKerberos/train/"
								 + model);
						Boolean success = aFile.mkdirs();
						Fonctions.trace("DBG", "Creation directory status " + success, "CameraServlet");
						// Saving file int File
						Fonctions.trace("DBG", "Copying file " + id + " into " + Core.getInstance().getKerberosioPath()
								+ "/pimpMyKerberos/train/" + model, "CameraServlet");
						FileUtils.copyFileToDirectory(new File(id), aFile,true);

					} catch (Exception e) {
						Fonctions.trace("ERR", "Couldn't save file " + id + " to " + Core.getInstance().getKerberosioPath()
								 + "/pimpMyKerberos/train/" + model, "CameraServlet");
					}
				}
			}
		}
	}

	private void fromDay(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String date = request.getParameter("date");
				if (date != null) {
					try {

						Date aDate = Fonctions.getDateFormat(date + ":00", "yyyy-MM-dd_hh:mm:ss");
						ZoneId zone = ZoneId.of(Core.getInstance().getTimeZone());
						ZoneId utcZone = ZoneId.of("UTC");
						LocalTime now = LocalTime.now(zone);
						LocalTime utc = LocalTime.now(utcZone);
						long hoursBetween = ChronoUnit.HOURS.between(now,utc);
						Calendar cal = Calendar.getInstance();
						cal.setTime(aDate);
						cal.add(Calendar.HOUR_OF_DAY, Long.valueOf(hoursBetween).intValue());
						Fonctions.trace("DBG", "Display from " + aDate + " nowlocalTime " + now + " hoursBetween " + hoursBetween, "CameraServlet");
						String startCrunch = Long.toString(cal.getTime().getTime());
						String size = request.getParameter("size");
						response.getWriter().write(toGson(Core.getInstance().getTimeline().extract(startCrunch, size)));
					} catch (Exception e) {
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
				String idCam = request.getParameter("id");
				if (idCam != null) {
					try {
						String startCrunch = Core.getInstance().seekCamera(idCam).toString();
						Fonctions.trace("DBG", "Seeking camera " + idCam + " to " + startCrunch + " ", "CameraServlet");
						String size = request.getParameter("size");
						response.getWriter().write(toGson(Core.getInstance().getTimeline().extract(startCrunch, size)));
					} catch (Exception e) {
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
				try {
					response.getWriter().write(toGson(Core.getInstance().getTimeline().extractByHour()));
				} catch (Exception e) {
					Fonctions.trace("ERR", "Couldn't extract files amout by hour " + e.getMessage(), "CameraServlet");
					e.printStackTrace();
				}

			}
		}

	}

	private void cleanAll(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				try {
					Integer count = Core.getInstance().cleanAll();
					response.getWriter().write(count.toString());
				} catch (IOException e) {
					Fonctions.trace("ERR", "Couldn't clean all cam " + e.getMessage(), "CameraServlet");
				}
			}
		}

	}

	private void cleanCam(HttpServletRequest request, HttpServletResponse response) {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String idCam = request.getParameter("id");
				if (idCam != null) {
					try {
						Integer count = Core.getInstance().cleanCamera(idCam);
						response.getWriter().write(count.toString());
					} catch (Exception e) {
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
				requester = Core.getInstance().getUsers().get("admin");
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
				if ( Core.getInstance().getTimeline() != null )
				{
				response.getWriter().write(toGson(Core.getInstance().getTimeline().extract(startCrunch, size)));
				}else
				{
					Fonctions.trace("ERR", "Couldn't read timeLine data", "CameraServlet");
				}
			}
		}
	}

	private void isDockerCompose(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User requester = (User) request.getSession().getAttribute("USER");
		if (requester != null) {
			if (requester.getName().equals("admin")) {
				String sub = request.getParameter("dir");
				File aDockerComposeFile = new File(sub + "/" + "docker-compose.yml");
				if (aDockerComposeFile.exists()) {
					response.getWriter().write("true");
				} else {
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
				List<Directory> dirs = new ArrayList<Directory>();
				if (sub != null && new File(sub).isDirectory()) {
					extracted(sub, dirs);
				} else {
					extracted("/", dirs);
				}
				response.getWriter().write(toGson(dirs));
			}
		}
	}

	private static void extracted(String sub, List<Directory> dirs) {
		File aFile = new File(sub);
		for (File aSubFile : aFile.listFiles()) {
			if (aSubFile.isDirectory()) {
				Directory aDirectory = new Directory();
				aDirectory.setName(aSubFile.getAbsolutePath());
				aDirectory.setParentName(sub);
				dirs.add(aDirectory);
			}
		}
	}
}
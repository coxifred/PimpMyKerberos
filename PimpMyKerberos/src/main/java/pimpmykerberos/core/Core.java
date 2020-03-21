package pimpmykerberos.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.thoughtworks.xstream.XStream;

import pimpmykerberos.beans.Camera;
import pimpmykerberos.beans.Log;
import pimpmykerberos.beans.TimeLine;
import pimpmykerberos.beans.User;
import pimpmykerberos.beans.docker.DockerCompose;
import pimpmykerberos.beans.docker.Service;
import pimpmykerberos.server.Webserver;
import pimpmykerberos.server.messages.Message;
import pimpmykerberos.server.websocket.AdminWebSocket;
import pimpmykerberos.threads.ThreadMemory;
import pimpmykerberos.threads.ThreadSaver;
import pimpmykerberos.threads.ThreadWatch;
import pimpmykerberos.utils.Fonctions;

public class Core {

	/**
	 * Singleton code
	 */
	static Core instance;

	/**
	 * Config File for Core
	 */
	String coreFile;

	/**
	 * webserver port
	 */
	Integer webServerPort = 443;

	/**
	 * websocket port
	 */
	Integer webSocketPort = 4430;

	/**
	 * Complete url access
	 */
	transient String urlAccess = "";

	/**
	 * Ip for binding server
	 */
	String webServerIp;

	/**
	 * Debug mode
	 */
	Boolean debug = false;
	
	/**
	 * Time between camera scanning from kerberos.io
	 */
	Integer timeBetweenScan=60000;
	
	/**
	 * Debug mode
	 */
	Boolean debugJetty = false;

	/**
	 * Webserver
	 */
	transient Webserver ws;

	/**
	 * Admin password
	 */
	String adminPassword;

	/**
	 * uuid generated in case of webserver
	 */
	UUID uuid;

	Integer maxLogEntries = 1000;

	/**
	 * Top Path of kerberosio
	 */
	String kerberosioPath = "";
	
	/**
	 * timeLine 
	 */
	transient TimeLine timeline;
	
	/*
	 * Max lines for camera
	 */
	Integer maxDisplayLineInGUI=5;
	
	/**
	 * Default max keep day for files (all cam)
	 */
	Integer daysBeforePurge;
	
	/**
	 * Max column to display
	 */
	Integer maxDisplayColumnInGUI=6;
	
	/**
	 * A Docker Compose
	 */
	transient DockerCompose dockerCompose;

	/*
	 * for https,http2 and alpn)
	 */
	Boolean http2 = true;

	/**
	 * Session id
	 */
	Long sessionId = 0l;

	/**
	 * List des users
	 * 
	 * @throws Exception
	 */
	Map<String, User> users = new HashMap<String, User>();

	/**
	 * Logs
	 */
	transient List<Log> logs = new ArrayList<Log>();

	/**
	 * Datapath
	 */
	String dataPath = "";

	public void launch() throws Exception {

		Fonctions.trace("INF", "Starting main core", "CORE");
		// Chargement des users
		User.loadUsers();

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		// Creating default Directory (plugins)
		Fonctions.trace("INF", "Checking/Creating plugins directory in " + getDataPath() + "/plugins", "CORE");
		new File(getDataPath() + "/plugins").mkdirs();

		// Loading plugins
		loadPlugin();

		/**
		 * Starting webserver
		 */
		ws = new Webserver();

		Fonctions.trace("INF", "Starting webserver on port " + getWebServerPort(), "CORE");
		ws = new Webserver();
		if (getWebServerIp() == null) {
			ws.setIp(InetAddress.getLocalHost().getHostAddress());
		} else {
			ws.setIp(getWebServerIp());
		}
		ws.setPort(getWebServerPort());
		ws.startWebSocket(http2);
		Fonctions.traceBanner();

		Fonctions.trace("INF", "", "CORE");
		Fonctions.trace("INF", "", "CORE");
		Fonctions.trace("INF",
				"Please open this url in you favorite browser https://" + ws.getIp() + ":" + ws.getPort(), "CORE");
		urlAccess = "https://" + ws.getIp() + ":" + ws.getPort();
		Fonctions.trace("INF", "", "CORE");
		Fonctions.trace("INF", "", "CORE");

		// Starting ThreadMemory
		ThreadMemory tm = new ThreadMemory();
		tm.start();

		// Starting ThreadSaver (save data)
		ThreadSaver ts = new ThreadSaver();
		ts.start();

		// Starting ThreadWatch (looking for new files)
		ThreadWatch tsw = new ThreadWatch();
		tsw.start();

		while (ws.getWebSocketThread().isAlive()) {
			Fonctions.attendre(5000);

			if (!ts.isAlive()) {
				ts = new ThreadSaver();
				ts.start();
			}
			if (!tm.isAlive()) {
				tm = new ThreadMemory();
				tm.start();
			}
			if (!tsw.isAlive()) {
				tsw = new ThreadWatch();
				tsw.start();
			}
		}

		Fonctions.trace("INF", "Ending main core", "CORE");
		System.exit(0);
	}

	private void loadPlugin() throws IOException {

	}

	public void saveCore() {
		XStream aStream = new XStream();
		try {
			File aFile = new File(getCoreFile());
			Fonctions.trace("DBG", "Saving file to " + aFile.getAbsolutePath(), "CORE");
			aStream.toXML(this, new FileWriter(aFile.getAbsolutePath() + ".future"));
			FileUtils.copyFile(new File(aFile.getAbsolutePath() + ".future"), new File(aFile.getAbsolutePath()));
			FileUtils.moveFile(new File(aFile.getAbsolutePath() + ".future"), new File(aFile.getAbsolutePath()));
		} catch (Exception e) {

		}
	}

	public static Core getInstance() {
		if (instance == null) {
			instance = new Core();
		}
		return instance;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public static void setInstance(Core instance) {
		Core.instance = instance;
	}

	public Integer getWebServerPort() {
		return webServerPort;
	}

	public void setWebServerPort(Integer webServerPort) {
		this.webServerPort = webServerPort;
	}

	public String getWebServerIp() {
		return webServerIp;
	}

	public void setWebServerIp(String webServerIp) {
		this.webServerIp = webServerIp;
	}

	public Webserver getWs() {
		return ws;
	}

	public void setWs(Webserver ws) {
		this.ws = ws;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public Boolean getHttp2() {
		return http2;
	}

	public void setHttp2(Boolean http2) {
		this.http2 = http2;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(Vector<Log> logs) {
		this.logs = logs;
	}

	public int getMaxLogEntries() {
		return maxLogEntries;
	}

	public void setMaxLogEntries(Integer maxLogEntries) {
		this.maxLogEntries = maxLogEntries;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public synchronized Long getNextSessionId() {
		if (sessionId == null) {
			sessionId = 0l;
		}
		sessionId++;
		return sessionId;
	}

	public Boolean getDebugJetty() {
		return debugJetty;
	}

	public void setDebugJetty(Boolean debugJetty) {
		this.debugJetty = debugJetty;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public Integer getWebSocketPort() {
		return webSocketPort;
	}

	public void setWebSocketPort(Integer webSocketPort) {
		this.webSocketPort = webSocketPort;
	}

	public String getUrlAccess() {
		return urlAccess;
	}

	public void setUrlAccess(String urlAccess) {
		this.urlAccess = urlAccess;
	}

	public String getKerberosioPath() {
		return kerberosioPath;
	}

	public void setKerberosioPath(String kerberosioPath) {
		this.kerberosioPath = kerberosioPath;
	}

	public String getCoreFile() {
		return coreFile;
	}

	public void setCoreFile(String coreFile) {
		this.coreFile = coreFile;
	}

	public void computeKerberosioPath() {
		// Try to deserialize docker-compose.yml
		File aDockerComposeFile = new File(getKerberosioPath() + "/docker-compose.yml");

		if (aDockerComposeFile.exists()) {
			Yaml yaml = new Yaml(new Constructor(DockerCompose.class));

			try {
				dockerCompose = yaml.load(new FileInputStream(aDockerComposeFile));
				dockerCompose.setDockerComposeFile(aDockerComposeFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				Fonctions.trace("WNG", "Error while parsing docker-compose.yml, continue", "CORE");
			}

		}

		// Explore cameras
		for (File aFile : new File(getKerberosioPath()).listFiles()) {
			if (aFile.isDirectory()) {
				Fonctions.trace("DBG", "Found a camera " + aFile.getName(), "CORE");
				Camera aCamera = new Camera();
				aCamera.setName(aFile.getName());
				if ( Core.getInstance().getUsers().get("admin").getCameras().containsKey(aCamera.getName()))
				{
					aCamera=Core.getInstance().getUsers().get("admin").getCameras().get(aCamera.getName());
					aCamera.clean();
				}else
				{
					aCamera.setDaysBeforePurge(Core.getInstance().getDaysBeforePurge());
					Fonctions.trace("INF", "New Camera detected " + aCamera.getName() + " sending message to GUI", "CORE");
					Message aMessage=new Message();
					aMessage.setAction("RELOAD");
					aMessage.setMessage("New camera detected " + aCamera.getName());
					AdminWebSocket.broadcastMessage(aMessage);
				}

				aCamera.setCapturePath(aFile.getAbsolutePath() + "/capture");
				if (dockerCompose != null) {
					// Looking for exposePort
					Service aService = getServiceFromContainerName(aCamera.getName());
					if (aService != null) {
						for (String port : aService.getPorts()) {
							if (port.endsWith(":8889")) {
								try {
									Integer broadCastPort = Integer.decode(Fonctions.getFieldFromString(port, ":", 0));
									aCamera.setBroadcastPort(broadCastPort);
									Fonctions.trace("INF", "Broadcast port for Camera " + aCamera.getName()
											+ " will be " + broadCastPort, "CORE");
								} catch (Exception e) {
									Fonctions.trace("WNG", "Error while parsing port line " + port + ", continue",
											"CORE");
								}
							}
							if (port.endsWith(":80")) {
								try {
									Integer kerberosPort = Integer.decode(Fonctions.getFieldFromString(port, ":", 0));
									aCamera.setKerberosPort(kerberosPort);
									Fonctions.trace("INF", "Kerberos port for Camera " + aCamera.getName()
											+ " will be " + kerberosPort, "CORE");
								} catch (Exception e) {
									Fonctions.trace("WNG", "Error while parsing port line " + port + ", continue",
											"CORE");
								}

							}
						}
					} else {
						Fonctions.trace("WNG", "Can't find service with container_name:" + aCamera.getName()
								+ " in docker-compose.yaml", "CORE");
					}

				}
				aCamera.populate();
				Core.getInstance().getUsers().get("admin").getCameras().put(aCamera.getName(), aCamera);
			}
		}
		TimeLine aTimeLine=new TimeLine();
		aTimeLine.populate();
		Core.getInstance().setTimeline(aTimeLine);
	}

	private Service getServiceFromContainerName(String containerName) {
		for (Service aService : dockerCompose.getServices().values()) {
			if (aService.getContainer_name().equals(containerName)) {
				return aService;
			}
		}
		return null;
	}

	public DockerCompose getDockerCompose() {
		return dockerCompose;
	}

	public void setDockerCompose(DockerCompose dockerCompose) {
		this.dockerCompose = dockerCompose;
	}

	public Integer getTimeBetweenScan() {
		return timeBetweenScan;
	}

	public void setTimeBetweenScan(Integer timeBetweenScan) {
		this.timeBetweenScan = timeBetweenScan;
	}

	public TimeLine getTimeline() {
		return timeline;
	}

	public void setTimeline(TimeLine timeline) {
		this.timeline = timeline;
	}

	public Integer getMaxDisplayLineInGUI() {
		return maxDisplayLineInGUI;
	}

	public void setMaxDisplayLineInGUI(Integer maxDisplayLineInGUI) {
		this.maxDisplayLineInGUI = maxDisplayLineInGUI;
	}

	public Integer getDaysBeforePurge() {
		return daysBeforePurge;
	}

	public void setDaysBeforePurge(Integer daysBeforePurge) {
		this.daysBeforePurge = daysBeforePurge;
	}

	public Integer getMaxDisplayColumnInGUI() {
		return maxDisplayColumnInGUI;
	}

	public void setMaxDisplayColumnInGUI(Integer maxDisplayColumnInGUI) {
		this.maxDisplayColumnInGUI = maxDisplayColumnInGUI;
	}
	
	public void cleanCamera(Integer cameraId)
	{
		User requester=Core.getInstance().getUsers().get("admin");
		Camera aCamera=requester.getCameras().get(cameraId);
		if ( aCamera != null )
		{
			aCamera.clean();
		}
	}

	public void cleanAll() {
		User requester=Core.getInstance().getUsers().get("admin");
		for ( Camera aCamera:requester.getCameras().values())
		{
			aCamera.clean();
		}
		
	}

	public Long seekCamera(Integer cameraId) {
		User requester=Core.getInstance().getUsers().get("admin");
		Camera aCamera=requester.getCameras().get(cameraId);
		if ( aCamera != null )
		{
			return aCamera.seek();
		}else 
		{
		return Long.MAX_VALUE;	
		}
		
	}

}

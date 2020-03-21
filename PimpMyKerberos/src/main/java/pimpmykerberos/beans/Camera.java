package pimpmykerberos.beans;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import pimpmykerberos.core.Core;
import pimpmykerberos.server.messages.Message;
import pimpmykerberos.server.websocket.AdminWebSocket;
import pimpmykerberos.utils.Fonctions;

public class Camera {
	String name;
	String capturePath;
	String ip;
	Integer kerberosPort;
	Integer daysBeforePurge=30;
	Integer broadcastPort;
	Date freshDate;
	transient TreeMap<Long, String> timeToFile = new TreeMap<Long, String>();

	public void populate() {
		File captureDir = new File(capturePath);
		if (captureDir.exists() && captureDir.isDirectory()) {
			int i = 0;
			for (File aFile : captureDir.listFiles()) {
				timeToFile.put(aFile.lastModified(), aFile.getAbsolutePath());
				i++;
			}
			Date lateDate = new Date(timeToFile.firstKey());
			Date aNewfreshDate = new Date(timeToFile.lastKey());
			if (freshDate != null && aNewfreshDate.getTime() > freshDate.getTime()) {
				Fonctions.trace("DBG", "New detection for camera " + name + " sending message to IHM", "Camera");
				Message aMessage = new Message();
				aMessage.setImagePath(timeToFile.get(timeToFile.lastKey()));
				aMessage.setAction("RELOAD");
				aMessage.setMessage("New detection from " + name);
				AdminWebSocket.broadcastMessage(aMessage);
			}
			freshDate = aNewfreshDate;
			Fonctions.trace("DBG",
					"Camera " + getName() + " has " + i + " entries from " + lateDate + " to " + freshDate, "Camera");
		} else {
			Fonctions.trace("ERR", capturePath + " doesn't exist or not a directory", "Camera");
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapturePath() {
		return capturePath;
	}

	public void setCapturePath(String capturePath) {
		this.capturePath = capturePath;
	}

	public Integer getDaysBeforePurge() {
		return daysBeforePurge;
	}

	public void setDaysBeforePurge(Integer daysBeforePurge) {
		this.daysBeforePurge = daysBeforePurge;
	}

	public Integer getBroadcastPort() {
		return broadcastPort;
	}

	public void setBroadcastPort(Integer broadcastPort) {
		this.broadcastPort = broadcastPort;
	}

	public Map<Long, String> getTimeToFile() {
		return timeToFile;
	}

	public void setTimeToFile(TreeMap<Long, String> timeToFile) {
		this.timeToFile = timeToFile;
	}

	public Integer getKerberosPort() {
		return kerberosPort;
	}

	public void setKerberosPort(Integer kerberosPort) {
		this.kerberosPort = kerberosPort;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public Date getFreshDate() {
		return freshDate;
	}

	public void setFreshDate(Date freshDate) {
		this.freshDate = freshDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Camera other = (Camera) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Integer clean() {
		Calendar calendar = Calendar.getInstance();
		if (getDaysBeforePurge() < Core.getInstance().getDaysBeforePurge()) {
			calendar.add(Calendar.DAY_OF_YEAR, -getDaysBeforePurge());
		} else {
			calendar.add(Calendar.DAY_OF_YEAR, -Core.getInstance().getDaysBeforePurge());
		}
		Integer count=0;
		File captureDir = new File(capturePath);
		if (captureDir.exists() && captureDir.isDirectory()) {
			Fonctions.trace("DBG",
					"Cleaning all files older than " + calendar.getTime() + " in " + captureDir.getAbsolutePath(),
					"Camera");
			for (File aFile : captureDir.listFiles()) {
				if (aFile.lastModified() < calendar.getTimeInMillis()) {
					Fonctions.trace("DBG", "Cleaning file " + aFile.getAbsolutePath(), "Camera");
					try {
						FileUtils.forceDelete(aFile);
						count++;
					} catch (IOException e) {
						Fonctions.trace("ERR", "Can't clean " + aFile.getAbsolutePath() + " " + e.getMessage(),
								"Camera");
					}
				}
			}
		}
		return count;
	}

	public Long seek() {
		if (timeToFile.lastKey() != null) {
			return timeToFile.lastKey();
		} else {
			return Long.MAX_VALUE;
		}
	}

}

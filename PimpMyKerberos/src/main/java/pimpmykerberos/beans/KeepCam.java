package pimpmykerberos.beans;

import java.util.Date;

public class KeepCam {
	
	String id;
	Date time;
	String pathToMedia;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getPathToMedia() {
		return pathToMedia;
	}
	public void setPathToMedia(String pathToMedia) {
		this.pathToMedia = pathToMedia;
	}
	
}

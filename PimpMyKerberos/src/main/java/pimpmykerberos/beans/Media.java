package pimpmykerberos.beans;

import java.util.Date;

public class Media {
	String pathToMedia;
	Long time;
	Date date;
	String camName;

	public Media(String pathToMedia,Long time,String camName,Date aDate) {
		this.pathToMedia=pathToMedia;
		this.time=time;
		this.camName=camName;
		this.date=aDate;
	}

	public String getPathToMedia() {
		return pathToMedia;
	}

	public void setPathToMedia(String pathToMedia) {
		this.pathToMedia = pathToMedia;
		
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getCamName() {
		return camName;
	}

	public void setCamName(String camName) {
		this.camName = camName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
}

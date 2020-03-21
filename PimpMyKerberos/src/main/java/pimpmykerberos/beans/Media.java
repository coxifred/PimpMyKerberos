package pimpmykerberos.beans;

public class Media {
	String pathToMedia;
	Long time;
	String camName;

	public Media(String pathToMedia,Long time,String camName) {
		this.pathToMedia=pathToMedia;
		this.time=time;
		this.camName=camName;
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

	
}

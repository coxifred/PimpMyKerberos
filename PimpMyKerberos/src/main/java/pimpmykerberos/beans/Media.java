package pimpmykerberos.beans;

public class Media {
	String pathToMedia;
	Long time;

	public Media(String pathToMedia,Long time) {
		this.pathToMedia=pathToMedia;
		this.time=time;
	}

	public String getPathToMedia() {
		return pathToMedia;
	}

	public void setPathToMedia(String pathToMedia) {
		this.pathToMedia = pathToMedia;
		
	}

	
}

package pimpmykerberos.beans;

public class Dashboard {
	String backgroundColor="white";
	Boolean homePage=false;
	String content="";
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public Boolean getHomePage() {
		return homePage;
	}
	public void setHomePage(Boolean homePage) {
		this.homePage = homePage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}

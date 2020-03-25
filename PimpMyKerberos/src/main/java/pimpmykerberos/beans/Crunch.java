package pimpmykerberos.beans;

import java.util.List;
import java.util.Map;

public class Crunch {

	String id;
	List<Map<Integer,Media>> cameraToMedia;
	Integer maxLineDisplay=5;
	Integer maxColumnDisplay=6;
	Long startCrunch=0l;
	Long endCrunch=0l;
	Long size=0l;
	
	
	public List<Map<Integer, Media>> getCameraToMedia() {
		return cameraToMedia;
	}
	public void setCameraToMedia(List<Map<Integer, Media>> cameraToMedia) {
		this.cameraToMedia = cameraToMedia;
	}
	public Long getStartCrunch() {
		return startCrunch;
	}
	public void setStartCrunch(Long startCrunch) {
		this.startCrunch = startCrunch;
	}
	public Long getEndCrunch() {
		return endCrunch;
	}
	public void setEndCrunch(Long endCrunch) {
		this.endCrunch = endCrunch;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Integer getMaxLineDisplay() {
		return maxLineDisplay;
	}
	public void setMaxLineDisplay(Integer maxLineDisplay) {
		this.maxLineDisplay = maxLineDisplay;
	}
	public Integer getMaxColumnDisplay() {
		return maxColumnDisplay;
	}
	public void setMaxColumnDisplay(Integer maxColumnDisplay) {
		this.maxColumnDisplay = maxColumnDisplay;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}

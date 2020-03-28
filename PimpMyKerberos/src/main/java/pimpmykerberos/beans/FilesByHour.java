package pimpmykerberos.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FilesByHour {
	
	
	Map<String,TreeMap<Long,Integer>> camMap=new HashMap<String,TreeMap<Long,Integer>>();

	public Map<String, TreeMap<Long, Integer>> getCamMap() {
		return camMap;
	}

	public void setCamMap(Map<String, TreeMap<Long, Integer>> camMap) {
		this.camMap = camMap;
	}
	
	
	
	
	
	

}

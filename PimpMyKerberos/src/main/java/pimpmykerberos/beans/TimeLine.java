package pimpmykerberos.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class TimeLine {
	transient TreeMap<Long, Map<Integer, Media>> timeToMedia = new TreeMap<Long, Map<Integer, Media>>();
	

	public void populate() {
		
		TreeMap<Long, Map<Integer, Media>> cameraToMediaTemp = new TreeMap<Long, Map<Integer, Media>>(Collections.reverseOrder());
		// Retrieve all time entries
		User admin = Core.getInstance().getUsers().get("admin");
		int cameraNumber = 1;
		for (Camera aCamera : admin.getCameras().values()) {
			for (Long aTime : aCamera.getTimeToFile().keySet()) {
				Map<Integer, Media> mediaz = new HashMap<Integer, Media>();
				if (cameraToMediaTemp.containsKey(aTime)) {
					mediaz = cameraToMediaTemp.get(aTime);
				}
				mediaz.put(cameraNumber, new Media(aCamera.getTimeToFile().get(aTime),aTime));
				cameraToMediaTemp.put(aTime, mediaz);
			}
			cameraNumber++;
		}
		timeToMedia = cameraToMediaTemp;
		Fonctions.trace("DBG", timeToMedia.size() + " total entries", "TimeLine");
	}

	public Crunch extract(String startCrunch, String size) {
		Crunch aCrunch = new Crunch();
		aCrunch.setMaxLineDisplay(Core.getInstance().getMaxDisplayLineInGUI());
		aCrunch.setMaxColumnDisplay(Core.getInstance().getMaxDisplayColumnInGUI());
		if (timeToMedia.size() > 0) {
			if ( timeToMedia.size() < Core.getInstance().getMaxDisplayLineInGUI())
			{
				aCrunch.setMaxLineDisplay(timeToMedia.size());
			}
			if (startCrunch == null)
			{
				startCrunch = timeToMedia.firstKey().toString();
			}
			if ( size == null ) 
			{
				size=Core.getInstance().getMaxDisplayLineInGUI().toString();
			}

			Long sCrunchLong = Long.decode(startCrunch);
			Fonctions.trace("DBG", "sCrunchLong=" + sCrunchLong + " => " + new Date(sCrunchLong), "TimeLine");
			Long aSize = Long.decode(size);
			aCrunch.setSize(aSize);
			List<Map<Integer, Media>> cameraToMedia = new ArrayList<Map<Integer, Media>>();

			long currentSize = 0;
			for (Long aLong : timeToMedia.keySet()) {
				
				if (aLong <= sCrunchLong && currentSize < aSize) {
					Fonctions.trace("DBG", "aLong=" + aLong + " => " + new Date(aLong), "TimeLine");
					if (currentSize == 0)
					{
						aCrunch.setStartCrunch(aLong);
					}
					cameraToMedia.add(timeToMedia.get(aLong));
					aCrunch.setEndCrunch(aLong);
					currentSize++;
				}
			}
			aCrunch.setCameraToMedia(cameraToMedia);
		}
		return aCrunch;
	}

	public TreeMap<Long, Map<Integer, Media>> getTimeToMedia() {
		return timeToMedia;
	}

	public void setTimeToMedia(TreeMap<Long, Map<Integer, Media>> timeToMedia) {
		this.timeToMedia = timeToMedia;
	}


	
}

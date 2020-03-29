package pimpmykerberos.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class TimeLine {
	transient TreeMap<Long, Map<Integer, Media>> timeToMedia = new TreeMap<Long, Map<Integer, Media>>();

	public void populate() {

		TreeMap<Long, Map<Integer, Media>> cameraToMediaTemp = new TreeMap<Long, Map<Integer, Media>>(
				Collections.reverseOrder());
		// Retrieve all time entries
		User admin = Core.getInstance().getUsers().get("admin");
		int cameraNumber = 1;
		for (Camera aCamera : admin.getCameras().values()) {
			for (Long aTime : aCamera.getTimeToFile().keySet()) {
				Map<Integer, Media> mediaz = new HashMap<Integer, Media>();
				// Check if file are still presents;
				cleanMissings(cameraToMediaTemp, aTime);
				if (cameraToMediaTemp.containsKey(aTime)) {
					mediaz = cameraToMediaTemp.get(aTime);

				}
				mediaz.put(cameraNumber,
						new Media(aCamera.getTimeToFile().get(aTime), aTime, aCamera.getName(), new Date(aTime)));
				cameraToMediaTemp.put(aTime, mediaz);
			}
			cameraNumber++;
		}
		timeToMedia.clear();
		timeToMedia = cameraToMediaTemp;
		Fonctions.trace("DBG", timeToMedia.size() + " total entries", "TimeLine");
	}

	public Crunch extract(String startCrunch, String size) {
		Crunch aCrunch = new Crunch();
		aCrunch.setId(UUID.randomUUID().toString());
		aCrunch.setMaxLineDisplay(Core.getInstance().getMaxDisplayLineInGUI());
		aCrunch.setMaxColumnDisplay(Core.getInstance().getMaxDisplayColumnInGUI());
		if (timeToMedia.size() > 0) {
			if (timeToMedia.size() < Core.getInstance().getMaxDisplayLineInGUI()) {
				aCrunch.setMaxLineDisplay(timeToMedia.size());
			}
			if (startCrunch == null) {
				startCrunch = timeToMedia.firstKey().toString();
			}
			if (size == null) {
				size = Core.getInstance().getMaxDisplayLineInGUI().toString();
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
					if (currentSize == 0) {
						aCrunch.setStartCrunch(aLong);
					}
					// Check if file still exists
					cleanMissings(timeToMedia, aLong);

					cameraToMedia.add(timeToMedia.get(aLong));
					aCrunch.setEndCrunch(aLong);
					currentSize++;
				}
			}
			aCrunch.setCameraToMedia(cameraToMedia);
		}
		return aCrunch;
	}

	private void cleanMissings(TreeMap<Long, Map<Integer, Media>> checkMap, Long aLong) {
		Map<Integer, Media> check = checkMap.get(aLong);
		List<Integer> toClean = new ArrayList<Integer>();
		if (check != null) {
			for (Integer aMediaInt : check.keySet()) {
				Media aMedia = check.get(aMediaInt);
				File aMediaToCheck = new File(aMedia.pathToMedia);
				if (!aMediaToCheck.exists()) {
					Fonctions.trace("ERR", "File " + aMedia.pathToMedia + " is no more present, clean from memory",
							"TimeLine");
					toClean.add(aMediaInt);
				}
			}

			for (Integer aIntToDelete : toClean) {
				check.remove(aIntToDelete);
			}
		}
	}

	public TreeMap<Long, Map<Integer, Media>> getTimeToMedia() {
		return timeToMedia;
	}

	public void setTimeToMedia(TreeMap<Long, Map<Integer, Media>> timeToMedia) {
		this.timeToMedia = timeToMedia;
	}

	public FilesByHour extractByHour() {
		FilesByHour aFilesByHour = new FilesByHour();
		if (new File(Core.getInstance().getKerberosioPath()).isDirectory()) {
			for (File aFile : new File(Core.getInstance().getKerberosioPath()).listFiles()) {
				if (aFile.isDirectory() && !aFile.getName().equalsIgnoreCase("pimpMyKerberos")) {
					if (new File(aFile.getAbsolutePath() + File.separator + "capture").exists()) {
						TreeMap<Long, Integer> mapCam = new TreeMap<Long, Integer>();
						Calendar aCalendar = Calendar.getInstance();
						for (File subFile : new File(aFile.getAbsolutePath() + File.separator + "capture")
								.listFiles()) {
							if (subFile.isFile()) {
								aCalendar.setTimeInMillis(subFile.lastModified());
								aCalendar.set(Calendar.MINUTE, 0);
								aCalendar.set(Calendar.SECOND, 0);
								aCalendar.set(Calendar.MILLISECOND, 0);
								Integer count = 1;
								if (mapCam.containsKey(aCalendar.getTimeInMillis())) {
									count += mapCam.get(aCalendar.getTimeInMillis());
								}
								Fonctions.trace("DBG",
										"Adding file count " + count + " to timeslot " + aCalendar.getTime()
												+ " fileName is " + subFile.getName() + " file time "
												+ new Date(subFile.lastModified()),
										"TimeLine");
								mapCam.put(aCalendar.getTimeInMillis(), count);
								aCalendar = Calendar.getInstance();
								aCalendar.add(Calendar.HOUR_OF_DAY, 1);
								mapCam.put(aCalendar.getTimeInMillis(), 0);
							}

						}
						Fonctions.trace("DBG", "Fill map for camera " + aFile.getName(), "TimeLine");
						aFilesByHour.getCamMap().put(aFile.getName(), mapCam);
					}
				}
			}
		} else {
			Fonctions.trace("ERR",
					"KerberosioPath directory " + Core.getInstance().getKerberosioPath() + " is no more present",
					"TimeLine");
		}
		return aFilesByHour;
	}

}

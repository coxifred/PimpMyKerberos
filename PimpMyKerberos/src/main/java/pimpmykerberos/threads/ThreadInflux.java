package pimpmykerberos.threads;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class ThreadInflux extends Thread {

	@Override
	public void run() {
		setName("ThreadInflux");
		while (true) {
			Fonctions.trace("DBG", "ThreadInflux analysis", "ThreadInflux");
			try {
				extractData();
			} catch (Exception e) {
				Fonctions.trace("DBG", "Error while uploading info to influxDb " + e.getMessage(), "ThreadInflux");
				e.printStackTrace();
			}
			Fonctions.attendre(1000 * 60 * 60);

		}
	}

	private void extractData() {
		Map<String, List<Long>> camToFile = new HashMap<String, List<Long>>();
		Calendar aCalendarEnd = Calendar.getInstance();
		aCalendarEnd.set(Calendar.MINUTE, 0);
		aCalendarEnd.set(Calendar.SECOND, 0);
		aCalendarEnd.set(Calendar.MILLISECOND, 0);

		Calendar aCalendarStart = Calendar.getInstance();
		aCalendarStart.set(Calendar.MINUTE, 0);
		aCalendarStart.set(Calendar.SECOND, 0);
		aCalendarStart.set(Calendar.MILLISECOND, 0);
		aCalendarStart.add(Calendar.HOUR_OF_DAY, -1);
		if (new File(Core.getInstance().getKerberosioPath()).isDirectory()) {
			for (File aFile : new File(Core.getInstance().getKerberosioPath()).listFiles()) {
				if (aFile.isDirectory()) {

					List<Long> timestamps = new ArrayList<Long>();
					if (new File(aFile.getAbsolutePath() + File.separator + "capture").exists()) {
						for (File subFile : new File(aFile.getAbsolutePath() + File.separator + "capture")
								.listFiles()) {
							if (subFile.isFile() && subFile.lastModified() >= aCalendarStart.getTimeInMillis()
									&& subFile.lastModified() < aCalendarEnd.getTimeInMillis()) {
								timestamps.add(subFile.lastModified());

							}
						}
						camToFile.put(aFile.getName(), timestamps);
					}

				}
			}

			final String serverURL = Core.getInstance().getInfluxDbUrl(),
					databaseName = Core.getInstance().getInfluxDbName(),
					username = Core.getInstance().getInfluxDbUser(), password = Core.getInstance().getInfluxDbPasswd();
			if (serverURL != null && !"".contentEquals(serverURL)) {

				final InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);
				influxDB.setDatabase(databaseName);
				influxDB.query(new Query("CREATE DATABASE " + databaseName));
				for (String aKey : camToFile.keySet()) {
					List<Long> list = camToFile.get(aKey);
					Integer count = list.size();
					Fonctions.trace("DBG", count + " file(s) for camera " + aKey + " between "
							+ aCalendarStart.getTime() + " and " + aCalendarEnd.getTime(), "ThreadInflux");
					for (Long aLong : list) {
						influxDB.write(Point.measurement("file_count").time(aLong, TimeUnit.MILLISECONDS)
								.tag("camera", aKey).addField("file_count", 1d).build());
					}
				}
			} else {
				Fonctions.trace("DBG", "No influxDbUrl & influxDbName declared in aCore.xml, by pass", "ThreadInflux");
			}
		} else {
			Fonctions.trace("ERR",
					"KerberosioPath directory " + Core.getInstance().getKerberosioPath() + " is no more present",
					"ThreadInflux");
		}

	}
}

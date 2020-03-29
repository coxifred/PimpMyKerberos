package pimpmykerberos;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class CoreLauncher {

	public static void main(String[] args) {
		String configCore = null;

		try {

			configCore = args[0];

			if (configCore == null) {
				printUsage(configCore);
			}
			// Try to load Core from file
			XStream xs = new XStream();
			Fonctions.trace("INF", "Loading CORE File " + configCore, "CORE");
			Core aCore = (Core) xs.fromXML(new FileInputStream(new File(configCore)));

			Core.setInstance(aCore);
			aCore.setCoreFile(configCore);
			List<String> confLines=Fonctions.getAllLinesFromFile(configCore);
			for ( String aConfigLine:confLines)
			{
				Fonctions.trace("INF", aConfigLine, "CORE");
			}
			aCore.launch();
			

		} catch (Exception e) {
			e.printStackTrace();
			printUsage(configCore);
			Fonctions.trace("INF", "Exiting in 60s, let you time to investigate", "CORE");
			Fonctions.attendre(60000);
			System.exit(1);
		}

	}

	private static void printUsage(String receivedCoreConfig) {
		Fonctions.trace("WNG", "I received CoreConfigFile=" + receivedCoreConfig, "CORE");
		Fonctions.trace("DEAD", "Usage java -jar pimpMyKerberos.jar <CoreConfigFile>", "CORE");
	}

}

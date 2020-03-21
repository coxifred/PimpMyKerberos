package pimpmykerberos.threads;

import pimpmykerberos.utils.Fonctions;

public class ThreadMemory extends Thread {

	@Override
	public void run() {
		setName("ThreadMemory");
		while (true) {
			Fonctions.trace("DBG", "Total memory:" + Fonctions.getTotalMemory() + ",Used memory:"
					+ Fonctions.getUsedMemory() + ",Free memory:" + Fonctions.getFreeMemory(), "CORE");

			Fonctions.attendre(60000);
		}
	}

}

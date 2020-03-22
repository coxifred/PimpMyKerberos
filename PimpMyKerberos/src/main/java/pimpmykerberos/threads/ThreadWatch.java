package pimpmykerberos.threads;

import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class ThreadWatch extends Thread {

	@Override
	public void run() {
		setName("ThreadWatch");
		while (true) {
			Fonctions.trace("DBG", "Kerberos analysis", "ThreadWatch");
			Core.getInstance().computeKerberosioPath(true);
			Fonctions.attendre(Core.getInstance().getTimeBetweenScan());
	
		}
	}

}

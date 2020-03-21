package pimpmykerberos.threads;

import pimpmykerberos.beans.User;
import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class ThreadSaver extends Thread {

	

	@Override
	public void run() {
		setName("ThreadSaver");
		while (true) {
			Fonctions.trace("DBG", "Saving data now", "CORE");
			for (User aUser : Core.getInstance().getUsers().values()) {
				aUser.saveUser();
			}
			Core.getInstance().saveCore();
			Fonctions.attendre(60000);
		}
	}

	
}

package de.kekshaus.cookieApi.bungee.utils;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseActions;

public class AutoUnmuteChecker implements Runnable {

	public AutoUnmuteChecker() {

	}

	public void run() {
		DataBaseActions.clearOldMuted();
	}

}

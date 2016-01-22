package de.kekshaus.cookieApi.bungee.utils;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseActions;

public class AutoUnbanChecker implements Runnable {

	public AutoUnbanChecker() {

	}

	public void run() {
		DataBaseActions.clearOldBans();
	}

}

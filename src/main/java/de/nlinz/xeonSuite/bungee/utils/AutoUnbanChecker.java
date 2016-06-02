package de.nlinz.xeonSuite.bungee.utils;

import de.nlinz.xeonSuite.bungee.dbase.DataBaseActions;

public class AutoUnbanChecker implements Runnable {

	public AutoUnbanChecker() {

	}

	public void run() {
		DataBaseActions.clearOldBans();
	}

}

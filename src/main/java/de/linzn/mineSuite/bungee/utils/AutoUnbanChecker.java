package de.linzn.mineSuite.bungee.utils;

import de.linzn.mineSuite.bungee.dbase.DataBaseActions;

public class AutoUnbanChecker implements Runnable {

	public void run() {
		DataBaseActions.clearOldBans();
        DataBaseActions.clearOldMuted();
	}

}

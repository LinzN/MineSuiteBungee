package de.linzn.mineSuite.bungee.utils;

import de.linzn.mineSuite.bungee.dbase.DataBaseActions;

public class AutoUnmuteChecker implements Runnable {

	public AutoUnmuteChecker() {

	}

	public void run() {
		DataBaseActions.clearOldMuted();
	}

}

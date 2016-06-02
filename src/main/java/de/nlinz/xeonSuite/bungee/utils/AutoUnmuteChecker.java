package de.nlinz.xeonSuite.bungee.utils;

import de.nlinz.xeonSuite.bungee.dbase.DataBaseActions;

public class AutoUnmuteChecker implements Runnable {

	public AutoUnmuteChecker() {

	}

	public void run() {
		DataBaseActions.clearOldMuted();
	}

}

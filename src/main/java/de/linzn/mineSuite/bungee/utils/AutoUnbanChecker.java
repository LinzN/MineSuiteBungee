package de.linzn.mineSuite.bungee.utils;

import de.linzn.mineSuite.bungee.database.mysql.MySQLTasks;

public class AutoUnbanChecker implements Runnable {

	public void run() {
		MySQLTasks.clearOldBans();
		MySQLTasks.clearOldMuted();
	}

}

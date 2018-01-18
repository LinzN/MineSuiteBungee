/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.bungee.utils;

import de.linzn.mineSuite.bungee.database.mysql.MySQLTasks;

public class AutoUnbanChecker implements Runnable {

    public void run() {
        MySQLTasks.clearOldBans();
        MySQLTasks.clearOldMuted();
    }

}

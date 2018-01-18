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

package de.linzn.jSocket.core;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.ProxyServer;

public class TaskRunnable {

    public void runSingleThreadExecutor(Runnable run) {
        ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), run);
    }
}

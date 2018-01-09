package de.linzn.jSocket.core;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.ProxyServer;

public class TaskRunnable {

    public void runSingleThreadExecutor(Runnable run) {
        ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), run);
    }
}

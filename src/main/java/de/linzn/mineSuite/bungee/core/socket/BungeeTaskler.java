package de.linzn.mineSuite.bungee.core.socket;

import de.linzn.jSocket.core.ThreadTaskler;
import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.ProxyServer;

public class BungeeTaskler implements ThreadTaskler {
    @Override
    public void runThreadPoolExecutor(Runnable runnable) {

    }

    @Override
    public void runThreadExecutor(Thread thread) {

    }

    @Override
    public void runSingleThreadExecutor(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), runnable);
    }
}

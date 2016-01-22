package de.xHyveSoftware.socket.bungee.util;

import java.util.concurrent.TimeUnit;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Scheduler {

	public static ScheduledTask schedule(final Runnable runnable, long millis) {
		return ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, runnable, millis,
				TimeUnit.MILLISECONDS);
	}

	public static ScheduledTask scheduleAtFixedRate(final Runnable runnable, long delay, long interval) {
		return ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, runnable, delay, interval,
				TimeUnit.MILLISECONDS);
	}
}

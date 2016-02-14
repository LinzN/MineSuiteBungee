package de.kekshaus.cookieApi.bungee;

import java.util.concurrent.TimeUnit;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseManager;
import de.kekshaus.cookieApi.bungee.listeners.ProxyServerListener;
import de.kekshaus.cookieApi.bungee.utils.AutoUnbanChecker;
import de.kekshaus.cookieApi.bungee.utils.AutoUnmuteChecker;
import de.kekshaus.cookieApi.bungee.utils.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class CookieApiBungee extends Plugin {

	public static CookieApiBungee instance;
	public static ProxyServer proxy;

	@SuppressWarnings({ "deprecation" })
	public void onEnable() {
		instance = this;
		proxy = ProxyServer.getInstance();
		final Config Filemanager = new Config(this);
		Filemanager.setDefaultConfig();
		if (DataBaseManager.setupDatabase()) {
			ProxyServer.getInstance().getConsole().sendMessage(ChatColor.BLUE + "Loading CookieApi...");
			registerListeners();
			ProxyServer.getInstance().getConsole().sendMessage(ChatColor.BLUE + "Finish!");
			if (autoSystem()) {
				ProxyServer.getInstance().getConsole().sendMessage(ChatColor.BLUE + "AutoSystem aktiviert!");
			}

		}
	}

	private void registerListeners() {
		proxy.getPluginManager().registerListener(this, new ProxyServerListener());
	}

	public void onDisable() {
	}

	private boolean autoSystem() {
		AutoUnbanChecker unban = new AutoUnbanChecker();
		AutoUnmuteChecker unmute = new AutoUnmuteChecker();
		ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, unban, 2, 4, TimeUnit.MINUTES);
		ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, unmute, 1, 4, TimeUnit.MINUTES);
		return true;
	}

}
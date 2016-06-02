package de.nlinz.xeonSuite.bungee;

import java.util.concurrent.TimeUnit;

import de.nlinz.xeonSuite.bungee.dbase.DataBaseManager;
import de.nlinz.xeonSuite.bungee.listeners.ProxyServerListener;
import de.nlinz.xeonSuite.bungee.listeners.bungeeChannel.BungeeSockBanListener;
import de.nlinz.xeonSuite.bungee.listeners.bungeeChannel.BungeeSockChatListener;
import de.nlinz.xeonSuite.bungee.listeners.bungeeChannel.BungeeSockGuildListener;
import de.nlinz.xeonSuite.bungee.listeners.bungeeChannel.BungeeSockHomeListener;
import de.nlinz.xeonSuite.bungee.listeners.bungeeChannel.BungeeSockTeleportListener;
import de.nlinz.xeonSuite.bungee.listeners.bungeeChannel.BungeeSockWarpListener;
import de.nlinz.xeonSuite.bungee.utils.AutoUnbanChecker;
import de.nlinz.xeonSuite.bungee.utils.AutoUnmuteChecker;
import de.nlinz.xeonSuite.bungee.utils.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class XeonSuiteBungee extends Plugin {

	public static XeonSuiteBungee instance;
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
		proxy.getPluginManager().registerListener(this, new BungeeSockBanListener());
		proxy.getPluginManager().registerListener(this, new BungeeSockChatListener());
		proxy.getPluginManager().registerListener(this, new BungeeSockGuildListener());
		proxy.getPluginManager().registerListener(this, new BungeeSockHomeListener());
		proxy.getPluginManager().registerListener(this, new BungeeSockTeleportListener());
		proxy.getPluginManager().registerListener(this, new BungeeSockWarpListener());
	}

	public void onDisable() {
	}

	private boolean autoSystem() {
		AutoUnbanChecker unban = new AutoUnbanChecker();
		AutoUnmuteChecker unmute = new AutoUnmuteChecker();
		ProxyServer.getInstance().getScheduler().schedule(XeonSuiteBungee.instance, unban, 2, 4, TimeUnit.MINUTES);
		ProxyServer.getInstance().getScheduler().schedule(XeonSuiteBungee.instance, unmute, 1, 4, TimeUnit.MINUTES);
		return true;
	}

}
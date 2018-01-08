package de.linzn.mineSuite.bungee;

import de.linzn.mineSuite.bungee.dbase.XeonConnectionSetup;
import de.linzn.mineSuite.bungee.listeners.ProxyServerListener;
import de.linzn.mineSuite.bungee.listeners.xeonSocket.*;
import de.linzn.mineSuite.bungee.utils.AutoUnbanChecker;
import de.linzn.mineSuite.bungee.utils.AutoUnmuteChecker;
import de.linzn.mineSuite.bungee.utils.Config;
import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class MineSuiteBungeePlugin extends Plugin {

	public static MineSuiteBungeePlugin instance;
	public static ProxyServer proxy;

	@Override
	@SuppressWarnings({ "deprecation" })
	public void onEnable() {
		instance = this;
		proxy = ProxyServer.getInstance();
		final Config Filemanager = new Config(this);
		Filemanager.setDefaultConfig();
		if (XeonConnectionSetup.create()) {
			ProxyServer.getInstance().getConsole().sendMessage(ChatColor.BLUE + "Loading XeonSuite...");
			registerListeners();
			ProxyServer.getInstance().getConsole().sendMessage(ChatColor.BLUE + "Finish!");
			if (autoSystem()) {
				ProxyServer.getInstance().getConsole().sendMessage(ChatColor.BLUE + "AutoSystem aktiviert!");
			}

		}
		new Metrics(this);
	}

	private void registerListeners() {
		proxy.getPluginManager().registerListener(this, new ProxyServerListener());

		XeonSocketServerManager.registerDataListener(new XeonBan());
		XeonSocketServerManager.registerDataListener(new XeonChat());
		XeonSocketServerManager.registerDataListener(new XeonGuild());
		XeonSocketServerManager.registerDataListener(new XeonHome());
		XeonSocketServerManager.registerDataListener(new XeonTeleport());
		XeonSocketServerManager.registerDataListener(new XeonWarp());
		XeonSocketServerManager.registerDataListener(new XeonPortal());
	}

	@Override
	public void onDisable() {
	}

	private boolean autoSystem() {
		AutoUnbanChecker unban = new AutoUnbanChecker();
		AutoUnmuteChecker unmute = new AutoUnmuteChecker();
		ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.instance, unban, 2, 4, TimeUnit.MINUTES);
		ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.instance, unmute, 1, 4, TimeUnit.MINUTES);
		return true;
	}

}
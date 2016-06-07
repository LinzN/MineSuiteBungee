package de.nlinz.xeonSuite.bungee;

import java.util.concurrent.TimeUnit;

import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.xeonSuite.bungee.dbase.XeonConnectionSetup;
import de.nlinz.xeonSuite.bungee.listeners.ProxyServerListener;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonBan;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonChat;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonGuild;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonHome;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonTeleport;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonWarp;
import de.nlinz.xeonSuite.bungee.utils.AutoUnbanChecker;
import de.nlinz.xeonSuite.bungee.utils.AutoUnmuteChecker;
import de.nlinz.xeonSuite.bungee.utils.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class XeonSuiteBungee extends Plugin {

	public static XeonSuiteBungee instance;
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
	}

	private void registerListeners() {
		proxy.getPluginManager().registerListener(this, new ProxyServerListener());

		XeonSocketServerManager.registerDataListener(new XeonBan());
		XeonSocketServerManager.registerDataListener(new XeonChat());
		XeonSocketServerManager.registerDataListener(new XeonGuild());
		XeonSocketServerManager.registerDataListener(new XeonHome());
		XeonSocketServerManager.registerDataListener(new XeonTeleport());
		XeonSocketServerManager.registerDataListener(new XeonWarp());
	}

	@Override
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
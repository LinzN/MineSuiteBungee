package de.kekshaus.cookieApi.bungee;

import java.util.concurrent.TimeUnit;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseManager;
import de.kekshaus.cookieApi.bungee.listeners.ProxyServerListener;
import de.kekshaus.cookieApi.bungee.listeners.bungeeChannel.BungeeStreamChatListener;
import de.kekshaus.cookieApi.bungee.listeners.bungeeChannel.BungeeStreamDataListener;
import de.kekshaus.cookieApi.bungee.listeners.bungeeChannel.BungeeStreamOtherListener;
import de.kekshaus.cookieApi.bungee.listeners.bungeeChannel.BungeeStreamTeleportListener;
import de.kekshaus.cookieApi.bungee.listeners.bungeeChannel.PipeLiveStreamListener;
import de.kekshaus.cookieApi.bungee.out.tasks.PipeStreamScheduler;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamChatEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamDataEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamOtherEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamTeleportEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.PipeLiveStreamEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamChatEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamDataEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamOtherEvent;
import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamTeleportEvent;
import de.kekshaus.cookieApi.bungee.utils.AutoUnbanChecker;
import de.kekshaus.cookieApi.bungee.utils.AutoUnmuteChecker;
import de.kekshaus.cookieApi.bungee.utils.Config;
import de.xHyveSoftware.socket.bungee.Starter;
import de.xHyveSoftware.socket.bungee.api.PacketManager;
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

			PacketManager.registerPacket(BungeeStreamTeleportEvent.class);
			PacketManager.registerPacket(BungeeStreamDataEvent.class);
			PacketManager.registerPacket(BungeeStreamOtherEvent.class);
			PacketManager.registerPacket(BungeeStreamChatEvent.class);
			PacketManager.registerPacket(ServerStreamTeleportEvent.class);
			PacketManager.registerPacket(ServerStreamDataEvent.class);
			PacketManager.registerPacket(ServerStreamOtherEvent.class);
			PacketManager.registerPacket(ServerStreamChatEvent.class);
			PacketManager.registerPacket(PipeLiveStreamEvent.class);

			PacketManager.registerListener(new PipeLiveStreamListener());
			PacketManager.registerListener(new BungeeStreamTeleportListener());
			PacketManager.registerListener(new BungeeStreamDataListener());
			PacketManager.registerListener(new BungeeStreamOtherListener());
			PacketManager.registerListener(new BungeeStreamChatListener());
			Starter.start();
			new PipeStreamScheduler();

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
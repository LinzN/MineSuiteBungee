package de.nlinz.xeonSuite.bungee.listeners;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.nlinz.xeonSuite.bungee.XeonSuiteBungee;
import de.nlinz.xeonSuite.bungee.dbase.DataBaseActions;
import de.nlinz.xeonSuite.bungee.dbase.BungeeDataTable;
import de.nlinz.xeonSuite.bungee.managers.BanManager;
import de.nlinz.xeonSuite.bungee.managers.MuteManager;
import de.nlinz.xeonSuite.bungee.managers.PlayerManager;
import de.nlinz.xeonSuite.bungee.utils.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyServerListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void playerLogin(PostLoginEvent event) {
		if (!BungeeDataTable.session.containsKey(event.getPlayer().getUniqueId())) {
			ProxyServer.getInstance()
					.broadcast(ChatColor.GOLD + event.getPlayer().getName() + " ist " + ChatColor.GREEN + "online");
		}
		PlayerManager.initPlayer(event.getPlayer());
		event.getPlayer()
				.sendMessage(ChatColor.GOLD + "*** Willkommen auf MineGaming! Wir wünschen dir viel Spaß! ***");
		event.getPlayer().sendMessage(ChatColor.GREEN + "Webseite: " + ChatColor.GRAY + " www.MineGaming.de   -   "
				+ ChatColor.GREEN + "Votes:  " + ChatColor.GRAY + "vote.minegaming.de");
		event.getPlayer().sendMessage(ChatColor.GOLD + "*********************************************************");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void playerLogout(final PlayerDisconnectEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		ProxyServer.getInstance().getScheduler().schedule(XeonSuiteBungee.instance, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
				if (player == null) {
					PlayerManager.deinitPlayer(event.getPlayer());
					ProxyServer.getInstance().broadcast(
							ChatColor.GOLD + event.getPlayer().getName() + " ist " + ChatColor.DARK_RED + "offline");
				} else {
					ProxyServer.getInstance().getLogger().warning(ChatColor.YELLOW
							+ "Player not log out correctly? Cant deinitialize player, because player is not offline!");
				}
			}
		}, 1, TimeUnit.SECONDS);
	}

	@EventHandler(priority = 64)
	public void onLogin(final LoginEvent e) {
		if (BanManager.isBanned(e.getConnection().getUniqueId())) {
			final Long current = System.currentTimeMillis();
			final Long end = BanManager.getEnd(e.getConnection().getUniqueId());
			if (end < current && end != -1L) {
				e.setCancelled(false);
				BanManager.unBanSystem(e.getConnection().getUniqueId());
			} else {
				e.setCancelled(true);
				e.setCancelReason(BanManager.getBannedMessage(e.getConnection().getUniqueId()));
			}
		}

		long timeStamp = new Date().getTime();
		if (DataBaseActions.updateProfile(e.getConnection().getUniqueId(), e.getConnection().getName(), timeStamp)) {
			XeonSuiteBungee.instance.getLogger()
					.info("UUID-cache updated for incoming connection " + e.getConnection().getName());
		} else {
			XeonSuiteBungee.instance.getLogger().info(
					"FAIL! UUID-cache update for incoming connection " + e.getConnection().getName() + " failed!");
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(final ChatEvent e) {
		final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		if (!MuteManager.isMuted(p.getUniqueId())) {
			return;
		}
		final long current = System.currentTimeMillis();
		final long end = MuteManager.getMuteTime(p.getUniqueId());
		if (end < current && end != -1L) {
			e.setCancelled(false);
			MuteManager.unMuteSystem(p.getUniqueId());
			return;
		}
		if (e.isCommand()) {
			final String command = e.getMessage().replaceAll("/", "").split(" ")[0].toLowerCase();
			boolean contains = false;
			for (final String string : Config.getStringList("cmd.forbidden")) {
				if (command.equals(string.toLowerCase())) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				return;
			}
		}

		e.setCancelled(true);
		e.setMessage("g");
		p.sendMessage(MuteManager.getMutedMessage(p.getUniqueId()));
		return;

	}

}

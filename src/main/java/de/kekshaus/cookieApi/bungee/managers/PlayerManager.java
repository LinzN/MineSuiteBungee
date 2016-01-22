package de.kekshaus.cookieApi.bungee.managers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.List;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.dbase.DataBaseActions;
import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;
import de.kekshaus.cookieApi.bungee.utils.Location;

public class PlayerManager {

	public static void sendMessageToTarget(ProxiedPlayer target, String message) {
		if (target == null) {

			return;
		}
		for (String line : (message).split("\n")) {
			target.sendMessage(TextComponent.fromLegacyText(line));
		}
	}

	public static ProxiedPlayer getPlayer(String player) {
		return ProxyServer.getInstance().getPlayer(player);
	}

	public static void initPlayer(final ProxiedPlayer player) {
		ProxyServer.getInstance().getScheduler().runAsync(CookieApiBungee.instance, new Runnable() {
			@Override
			public void run() {
				if (MuteManager.isMuted(player.getUniqueId())) {
					List<String> list = DataBaseActions.getMuteInfos(player.getUniqueId());
					String reason = list.get(0);
					String mutedby = list.get(1);
					long time = Long.parseLong(list.get(2));
					PlayerHashDB.muteReason.put(player.getUniqueId(), reason);
					PlayerHashDB.mutedBy.put(player.getUniqueId(), mutedby);
					PlayerHashDB.muteTime.put(player.getUniqueId(), time);

				}
				PlayerHashDB.channel.put(player.getUniqueId(), "GLOBAL");
				PlayerHashDB.session.put(player.getUniqueId(), true);

			}
		});
	}

	public static void deinitPlayer(final ProxiedPlayer player) {
		ProxyServer.getInstance().getScheduler().runAsync(CookieApiBungee.instance, new Runnable() {
			@Override
			public void run() {
				PlayerHashDB.isMuted.remove(player.getUniqueId());
				PlayerHashDB.muteTime.remove(player.getUniqueId());
				PlayerHashDB.muteReason.remove(player.getUniqueId());
				PlayerHashDB.mutedBy.remove(player.getUniqueId());
				PlayerHashDB.guildInvites.remove(player.getUniqueId());
				PlayerHashDB.msgreply.remove(player.getUniqueId());
				PlayerHashDB.channel.remove(player.getUniqueId());
				PlayerHashDB.isafk.remove(player.getUniqueId());
				PlayerHashDB.socialspy.remove(player.getUniqueId());
				PlayerHashDB.session.remove(player.getUniqueId());
				removeDeathBackLocation(player);
			}
		});
	}

	public static void setDeathBackLocation(Location loc, ProxiedPlayer player) {
		if (PlayerHashDB.deathBackLocation.containsKey(player.getUniqueId())) {
			PlayerHashDB.deathBackLocation.remove(player.getUniqueId());
			PlayerHashDB.lastBack.remove(player.getUniqueId());
		}
		PlayerHashDB.deathBackLocation.put(player.getUniqueId(), loc);
		PlayerHashDB.lastBack.put(player.getUniqueId(), true);
	}

	public static void removeDeathBackLocation(ProxiedPlayer player) {
		if (PlayerHashDB.deathBackLocation.containsKey(player.getUniqueId())) {
			PlayerHashDB.deathBackLocation.remove(player.getUniqueId());
			PlayerHashDB.lastBack.remove(player.getUniqueId());
		}
	}

	public static boolean hasDeathBackLocation(ProxiedPlayer player) {
		return PlayerHashDB.deathBackLocation.containsKey(player.getUniqueId());
	}

	public static Location getLastBackLocation(ProxiedPlayer player) {
		return PlayerHashDB.deathBackLocation.get(player.getUniqueId());
	}

	public static Location getDeathBackLocation(ProxiedPlayer player) {
		return PlayerHashDB.deathBackLocation.get(player.getUniqueId());
	}

}

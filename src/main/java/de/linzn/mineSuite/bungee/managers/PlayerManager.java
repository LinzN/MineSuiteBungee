package de.linzn.mineSuite.bungee.managers;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.dbase.BungeeDataTable;
import de.linzn.mineSuite.bungee.dbase.DataBaseActions;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

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
		ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), () -> {
			if (MuteManager.isMuted(player.getUniqueId())) {
				List<String> list = DataBaseActions.getMuteInfos(player.getUniqueId());
				String reason = list.get(0);
				String mutedby = list.get(1);
				long time = Long.parseLong(list.get(2));
				BungeeDataTable.muteReason.put(player.getUniqueId(), reason);
				BungeeDataTable.mutedBy.put(player.getUniqueId(), mutedby);
				BungeeDataTable.muteTime.put(player.getUniqueId(), time);

			}
			BungeeDataTable.channel.put(player.getUniqueId(), "GLOBAL");
			BungeeDataTable.session.put(player.getUniqueId(), true);

		});
	}

	public static void deinitPlayer(final ProxiedPlayer player) {
		ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), () -> {
			BungeeDataTable.isMuted.remove(player.getUniqueId());
			BungeeDataTable.muteTime.remove(player.getUniqueId());
			BungeeDataTable.muteReason.remove(player.getUniqueId());
			BungeeDataTable.mutedBy.remove(player.getUniqueId());
			BungeeDataTable.guildInvites.remove(player.getUniqueId());
			BungeeDataTable.msgreply.remove(player.getUniqueId());
			BungeeDataTable.channel.remove(player.getUniqueId());
			BungeeDataTable.isafk.remove(player.getUniqueId());
			BungeeDataTable.socialspy.remove(player.getUniqueId());
			BungeeDataTable.session.remove(player.getUniqueId());
			removeDeathBackLocation(player);
		});
	}

	public static void setDeathBackLocation(Location loc, ProxiedPlayer player) {
		if (BungeeDataTable.deathBackLocation.containsKey(player.getUniqueId())) {
			BungeeDataTable.deathBackLocation.remove(player.getUniqueId());
			BungeeDataTable.lastBack.remove(player.getUniqueId());
		}
		BungeeDataTable.deathBackLocation.put(player.getUniqueId(), loc);
		BungeeDataTable.lastBack.put(player.getUniqueId(), true);
	}

	public static void removeDeathBackLocation(ProxiedPlayer player) {
		if (BungeeDataTable.deathBackLocation.containsKey(player.getUniqueId())) {
			BungeeDataTable.deathBackLocation.remove(player.getUniqueId());
			BungeeDataTable.lastBack.remove(player.getUniqueId());
		}
	}

	public static boolean hasDeathBackLocation(ProxiedPlayer player) {
		return BungeeDataTable.deathBackLocation.containsKey(player.getUniqueId());
	}

	public static Location getLastBackLocation(ProxiedPlayer player) {
		return BungeeDataTable.deathBackLocation.get(player.getUniqueId());
	}

	public static Location getDeathBackLocation(ProxiedPlayer player) {
		return BungeeDataTable.deathBackLocation.get(player.getUniqueId());
	}

}

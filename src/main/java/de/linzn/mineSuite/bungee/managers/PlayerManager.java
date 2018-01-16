package de.linzn.mineSuite.bungee.managers;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.database.mysql.MySQLTasks;
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
				List<String> list = MySQLTasks.getMuteInfos(player.getUniqueId());
				String reason = list.get(0);
				String mutedby = list.get(1);
				long time = Long.parseLong(list.get(2));
				DataHashTable.muteReason.put(player.getUniqueId(), reason);
				DataHashTable.mutedBy.put(player.getUniqueId(), mutedby);
				DataHashTable.muteTime.put(player.getUniqueId(), time);

			}
			DataHashTable.channel.put(player.getUniqueId(), "GLOBAL");
			DataHashTable.session.put(player.getUniqueId(), true);

		});
	}

	public static void deinitPlayer(final ProxiedPlayer player) {
		ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), () -> {
			DataHashTable.isMuted.remove(player.getUniqueId());
			DataHashTable.muteTime.remove(player.getUniqueId());
			DataHashTable.muteReason.remove(player.getUniqueId());
			DataHashTable.mutedBy.remove(player.getUniqueId());
			DataHashTable.guildInvites.remove(player.getUniqueId());
			DataHashTable.msgreply.remove(player.getUniqueId());
			DataHashTable.channel.remove(player.getUniqueId());
			DataHashTable.isafk.remove(player.getUniqueId());
			DataHashTable.socialspy.remove(player.getUniqueId());
			DataHashTable.session.remove(player.getUniqueId());
			removeDeathBackLocation(player);
		});
	}

	public static void setDeathBackLocation(Location loc, ProxiedPlayer player) {
		if (DataHashTable.deathBackLocation.containsKey(player.getUniqueId())) {
			DataHashTable.deathBackLocation.remove(player.getUniqueId());
			DataHashTable.lastBack.remove(player.getUniqueId());
		}
		DataHashTable.deathBackLocation.put(player.getUniqueId(), loc);
		DataHashTable.lastBack.put(player.getUniqueId(), true);
	}

	public static void removeDeathBackLocation(ProxiedPlayer player) {
		if (DataHashTable.deathBackLocation.containsKey(player.getUniqueId())) {
			DataHashTable.deathBackLocation.remove(player.getUniqueId());
			DataHashTable.lastBack.remove(player.getUniqueId());
		}
	}

	public static boolean hasDeathBackLocation(ProxiedPlayer player) {
		return DataHashTable.deathBackLocation.containsKey(player.getUniqueId());
	}

	public static Location getLastBackLocation(ProxiedPlayer player) {
		return DataHashTable.deathBackLocation.get(player.getUniqueId());
	}

	public static Location getDeathBackLocation(ProxiedPlayer player) {
		return DataHashTable.deathBackLocation.get(player.getUniqueId());
	}

}

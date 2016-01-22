package de.kekshaus.cookieApi.bungee;

import java.util.UUID;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseActions;
import de.kekshaus.cookieApi.bungee.managers.BanManager;
import de.kekshaus.cookieApi.bungee.managers.MuteManager;
import de.kekshaus.cookieApi.bungee.out.SendMsg;
import de.kekshaus.cookieApi.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@SuppressWarnings("deprecation")
public class BanApi {

	public static void permBan(final String player, final String reason, final String bannedby) {

		UUID uuid = null;
		if (ProxyServer.getInstance().getPlayer(player) != null) {
			uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
		}
		if (uuid == null) {

			uuid = DataBaseActions.getUUID(player);
		}
		if (uuid != null) {
			BanManager.banPlayer(uuid, reason, bannedby, -1L, player);
		} else {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(bannedby);
			p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
		}

	}

	public static void tempBan(String player, String reason, String bannedby, long seconds) {
		UUID uuid = null;
		if (ProxyServer.getInstance().getPlayer(player) != null) {
			uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
		}
		if (uuid == null) {
			uuid = DataBaseActions.getUUID(player);
		}
		if (uuid != null) {
			BanManager.banPlayer(uuid, reason, bannedby, seconds, player);
		} else {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(bannedby);
			p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
		}

	}

	public static void unBan(String player, String reason, String unbannedby) {
		BanManager.unBan(player, reason, unbannedby);
	}

	public static void permMute(String player, String reason, String mutedby) {
		UUID uuid = null;
		if (ProxyServer.getInstance().getPlayer(player) != null) {
			uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
		}
		if (uuid == null) {
			uuid = DataBaseActions.getUUID(player);
		}
		if (uuid != null) {
			MuteManager.mutePlayer(uuid, reason, mutedby, -1L, player);
		} else {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(mutedby);
			p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
		}
	}

	public static void tempMute(String player, String reason, String mutedby, long seconds) {
		UUID uuid = null;
		if (ProxyServer.getInstance().getPlayer(player) != null) {
			uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
		}
		if (uuid == null) {
			uuid = DataBaseActions.getUUID(player);
		}
		if (uuid != null) {
			MuteManager.mutePlayer(uuid, reason, mutedby, seconds, player);
		} else {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(mutedby);
			p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
		}
	}

	public static void unMute(String player, String reason, String unmutedby) {
		MuteManager.unMute(player, reason, unmutedby);
	}

	public static void kick(String player, String reason, String kickedby) {
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
		if (p != null) {
			String sendmessage = "§6Du wurdest von §a" + kickedby + " §6vom Server geschmissen. \nGrund: §a" + reason;
			p.disconnect(sendmessage);
			SendMsg.kickMSG(player, reason, kickedby);
		}
	}
}

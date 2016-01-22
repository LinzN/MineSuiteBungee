package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

import de.kekshaus.cookieApi.bungee.BanApi;
import de.kekshaus.cookieApi.bungee.managers.PlayerManager;
import de.kekshaus.cookieApi.bungee.managers.TeleportManager;
import de.kekshaus.cookieApi.bungee.out.TeleportToHome;
import de.kekshaus.cookieApi.bungee.out.TeleportToLocation;
import de.kekshaus.cookieApi.bungee.out.TeleportToOther;
import de.kekshaus.cookieApi.bungee.out.TeleportToWarp;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamTeleportEvent;
import de.kekshaus.cookieApi.bungee.utils.Location;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Channel("BungeeStreamTeleport")
public class BungeeStreamTeleportListener extends AbstractPacketListener {
	@PacketHandler
	public void onCookieApiMessageEvent(BungeeStreamTeleportEvent event) throws IOException, SQLException {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String task = in.readUTF();

		if (task.equals("TeleportToHome")) {
			ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
			TeleportToHome.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(),
					in.readDouble(), in.readFloat(), in.readFloat()));
			ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to home!");
			return;
		}
		if (task.equals("TeleportToWarp")) {
			ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
			TeleportToWarp.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(),
					in.readDouble(), in.readFloat(), in.readFloat()));
			ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to warp!");
			return;
		}
		if (task.equals("TeleportToServer")) {
			ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
			TeleportToOther.portalOtherServer(player, in.readUTF());
			ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to server!");
			return;
		}
		if (task.equals("TeleportToLocation")) {
			ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
			TeleportToLocation.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(),
					in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
			ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to spawntype!");
			return;
		}

		if (task.equals("PlayersDeathBackLocation")) {
			ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
			TeleportManager.setPlayersDeathBackLocation(PlayerManager.getPlayer(player.getName()),
					new Location(player.getServer().getInfo().getName(), in.readUTF(), in.readDouble(), in.readDouble(),
							in.readDouble(), in.readFloat(), in.readFloat()));

			return;
		}

		if (task.equals("TeleportToPlayer")) {
			TeleportManager.teleportPlayerToPlayer(in.readUTF(), in.readUTF(), in.readBoolean(), in.readBoolean());

			return;
		}

		if (task.equals("TpaHereRequest")) {
			TeleportManager.requestPlayerTeleportToYou(in.readUTF(), in.readUTF());
			return;
		}

		if (task.equals("TpaRequest")) {
			TeleportManager.requestToTeleportToPlayer(in.readUTF(), in.readUTF());
			return;
		}
		if (task.equals("TpAccept")) {
			TeleportManager.acceptTeleportRequest(PlayerManager.getPlayer(in.readUTF()));
			return;
		}
		if (task.equals("TpDeny")) {
			TeleportManager.denyTeleportRequest(PlayerManager.getPlayer(in.readUTF()));
			return;
		}

		if (task.equals("TpAll")) {
			TeleportManager.tpAll(in.readUTF(), in.readUTF());
			return;
		}

		if (task.equals("SendPlayerBack")) {
			ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
			TeleportManager.sendPlayerToLastBack(player);
			return;
		}

		if (task.equals("PermaBan")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String bannedby = in.readUTF();
			BanApi.permBan(player, reason, bannedby);

			return;
		}
		if (task.equals("TempBan")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String bannedby = in.readUTF();
			Long seconds = in.readLong();
			BanApi.tempBan(player, reason, bannedby, seconds);
			return;
		}
		if (task.equals("PermaMute")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String mutedby = in.readUTF();
			BanApi.permMute(player, reason, mutedby);
			return;
		}
		if (task.equals("TempMute")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String mutedby = in.readUTF();
			Long seconds = in.readLong();
			BanApi.tempMute(player, reason, mutedby, seconds);
			return;
		}
		if (task.equals("kick")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String kickedby = in.readUTF();
			BanApi.kick(player, reason, kickedby);
			return;
		}
		if (task.equals("unban")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String unbannedby = in.readUTF();
			BanApi.unBan(player, reason, unbannedby);
			return;
		}
		if (task.equals("unmute")) {
			String player = in.readUTF();
			String reason = in.readUTF();
			String unmutedby = in.readUTF();
			BanApi.unMute(player, reason, unmutedby);
			return;
		}

	}

}
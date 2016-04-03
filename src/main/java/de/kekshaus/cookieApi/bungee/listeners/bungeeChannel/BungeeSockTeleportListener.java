package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.DataInputStream;
import java.io.IOException;

import de.keks.socket.bungee.events.plugin.BungeeSockTeleportEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.kekshaus.cookieApi.bungee.managers.PlayerManager;
import de.kekshaus.cookieApi.bungee.managers.TeleportManager;
import de.kekshaus.cookieApi.bungee.out.TeleportToLocation;
import de.kekshaus.cookieApi.bungee.out.TeleportToOther;
import de.kekshaus.cookieApi.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeSockTeleportListener implements Listener {
	@EventHandler
	public void onSocketMessage(BungeeSockTeleportEvent e) {
		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equals("TeleportToServer")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportToOther.portalOtherServer(player, in.readUTF());
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to server!");
				return;
			}
			if (task.equals("TeleportToLocation")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportToLocation.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(),
						in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to spawntype!");
				return;
			}

			if (task.equals("PlayersDeathBackLocation")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportManager.setPlayersDeathBackLocation(PlayerManager.getPlayer(player.getName()),
						new Location(player.getServer().getInfo().getName(), in.readUTF(), in.readDouble(),
								in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));

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
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportManager.acceptTeleportRequest(player);
				return;
			}
			if (task.equals("TpDeny")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportManager.denyTeleportRequest(player);
				return;
			}

			if (task.equals("TpAll")) {
				TeleportManager.tpAll(in.readUTF(), in.readUTF());
				return;
			}

			if (task.equals("SendPlayerBack")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportManager.sendPlayerToLastBack(player);
				return;
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

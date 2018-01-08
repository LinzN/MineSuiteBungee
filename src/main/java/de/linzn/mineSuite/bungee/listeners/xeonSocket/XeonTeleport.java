package de.linzn.mineSuite.bungee.listeners.xeonSocket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.managers.TeleportManager;
import de.linzn.mineSuite.bungee.out.TeleportToLocation;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class XeonTeleport implements IncomingDataListener {

	@Override
	public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
		String subChannel = null;
		try {
			subChannel = in.readUTF();

			if (subChannel.equals("TeleportToLocation")) {
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

			if (subChannel.equals("PlayersDeathBackLocation")) {
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

			if (subChannel.equals("TeleportToPlayer")) {
				TeleportManager.teleportPlayerToPlayer(in.readUTF(), in.readUTF(), in.readBoolean(), in.readBoolean());

				return;
			}

			if (subChannel.equals("TpaHereRequest")) {
				TeleportManager.requestPlayerTeleportToYou(in.readUTF(), in.readUTF());
				return;
			}

			if (subChannel.equals("TpaRequest")) {
				TeleportManager.requestToTeleportToPlayer(in.readUTF(), in.readUTF());
				return;
			}
			if (subChannel.equals("TpAccept")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportManager.acceptTeleportRequest(player);
				return;
			}
			if (subChannel.equals("TpDeny")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportManager.denyTeleportRequest(player);
				return;
			}

			if (subChannel.equals("TpAll")) {
				TeleportManager.tpAll(in.readUTF(), in.readUTF());
				return;
			}

			if (subChannel.equals("SendPlayerBack")) {
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

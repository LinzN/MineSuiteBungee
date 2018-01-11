package de.linzn.mineSuite.bungee.socket.listener;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.managers.TeleportManager;
import de.linzn.mineSuite.bungee.socket.output.JServerTeleportOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerTeleportListener implements IncomingDataListener {

	@Override
	public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
		String subChannel;
		try {
			subChannel = in.readUTF();

			if (subChannel.equals("teleport_teleport-location")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " teleport task has been canceled.");
					return;
				}
                Location location = new Location(in.readUTF(), in.readUTF(), in.readDouble(),
                        in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());
                JServerTeleportOutput.teleportToLocation(player, location);
                ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " has been teleported with teleport system.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
				return;
			}

			if (subChannel.equals("teleport_teleport-dead-location")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " teleport task has been canceled.");
					return;
				}
                Location location = new Location(in.readUTF(), in.readUTF(), in.readDouble(),
                        in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());

                JServerTeleportOutput.teleportToLocation(player, location);
                ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " has been teleported to deadpoint with teleport system.");
                TeleportManager.setPlayersDeathBackLocation(PlayerManager.getPlayer(player.getName()), location);
				return;
			}

			if (subChannel.equals("teleport_teleport-to-player")) {
				TeleportManager.teleportPlayerToPlayer(in.readUTF(), in.readUTF(), in.readBoolean(), in.readBoolean());

				return;
			}

			if (subChannel.equals("teleport_tpa-request-here")) {
				TeleportManager.requestPlayerTeleportToYou(in.readUTF(), in.readUTF());
				return;
			}

			if (subChannel.equals("teleport_tpa-to-request")) {
				TeleportManager.requestToTeleportToPlayer(in.readUTF(), in.readUTF());
				return;
			}
			if (subChannel.equals("teleport_tpa-accept")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " tpa task has been canceled.");
					return;
				}
				TeleportManager.acceptTeleportRequest(player);
				return;
			}
			if (subChannel.equals("teleport_tpa-deny")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " tpa task has been canceled.");
					return;
				}
				TeleportManager.denyTeleportRequest(player);
				return;
			}

			if (subChannel.equals("teleport_teleport-all")) {
				TeleportManager.tpAll(in.readUTF(), in.readUTF());
				return;
			}

			if (subChannel.equals("teleport_send-player-back")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " teleport task has been canceled.");
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

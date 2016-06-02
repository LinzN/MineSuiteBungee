package de.nlinz.xeonSuite.bungee.listeners.xeonSocket;

import java.io.DataInputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.JavaSocketServer;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import de.nlinz.xeonSuite.bungee.managers.PlayerManager;
import de.nlinz.xeonSuite.bungee.managers.TeleportManager;
import de.nlinz.xeonSuite.bungee.out.TeleportToLocation;
import de.nlinz.xeonSuite.bungee.out.TeleportToOther;
import de.nlinz.xeonSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class XeonTeleport implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonTeleport";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = JavaSocketServer.createInputStream(event.getStreamBytes());
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

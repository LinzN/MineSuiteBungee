package de.linzn.mineSuite.bungee.socket.listener;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.socket.output.JServerTeleportOutput;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerPortalListener implements IncomingDataListener {


	@Override
	public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
		String subChannel = null;
		try {
			subChannel = in.readUTF();

			if (subChannel.equals("portal_teleport-server")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " portal task has been canceled.");
					return;
				}
				String server = in.readUTF();
				JServerTeleportOutput.portalOtherServer(player, server);
				ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " has been teleported with portal system.");
				ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + server);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

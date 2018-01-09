package de.linzn.mineSuite.bungee.socket.output;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TeleportToLocation {

	public static void execute(ProxiedPlayer player, Location loc) {
		ServerInfo servernew = ProxyServer.getInstance().getServerInfo(loc.getServer());
		if (servernew == null) {
			MineSuiteBungeePlugin.getInstance().getLogger()
					.severe("Location has no Server, this should never happen. Please check");
			new Exception("").printStackTrace();
			return;
		}

		if (player == null) {
			new Exception("").printStackTrace();
			return;
		}

		if (player.getServer() == null || !player.getServer().getInfo().toString().equals(servernew.toString())) {
			player.connect(servernew);
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF(servernew.getName());
			dataOutputStream.writeUTF("TeleportToLocation");
			dataOutputStream.writeUTF(player.getName());
			dataOutputStream.writeUTF(loc.getWorld());
			dataOutputStream.writeDouble(loc.getX());
			dataOutputStream.writeDouble(loc.getY());
			dataOutputStream.writeDouble(loc.getZ());
			dataOutputStream.writeFloat(loc.getYaw());
			dataOutputStream.writeFloat(loc.getPitch());
		} catch (IOException e) {
			e.printStackTrace();
		}
		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
	}
}
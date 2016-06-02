package de.nlinz.xeonSuite.bungee.out;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.xeonSuite.bungee.XeonSuiteBungee;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonTeleport;
import de.nlinz.xeonSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeleportToLocation {

	public static void execute(ProxiedPlayer player, Location loc) {
		ServerInfo servernew = ProxyServer.getInstance().getServerInfo(loc.getServer());
		if (servernew == null) {
			XeonSuiteBungee.instance.getLogger()
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

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonTeleport.channelName);

		try {
			out.writeUTF(servernew.getName());
			out.writeUTF("TeleportToLocation");
			out.writeUTF(player.getName());
			out.writeUTF(loc.getWorld());
			out.writeDouble(loc.getX());
			out.writeDouble(loc.getY());
			out.writeDouble(loc.getZ());
			out.writeFloat(loc.getYaw());
			out.writeFloat(loc.getPitch());
		} catch (IOException e) {
			e.printStackTrace();
		}
		XeonSocketServerManager.sendData(bytes);
	}
}

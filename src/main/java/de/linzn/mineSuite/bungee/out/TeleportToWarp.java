package de.linzn.mineSuite.bungee.out;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.listeners.xeonSocket.XeonWarp;
import de.linzn.mineSuite.bungee.utils.Location;
import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TeleportToWarp {

	public static void execute(ProxiedPlayer player, Location loc) {
		ServerInfo servernew = ProxyServer.getInstance().getServerInfo(loc.getServer());
		if (servernew == null) {
            MineSuiteBungeePlugin.instance.getLogger()
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
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonWarp.channelName);

		try {
			out.writeUTF(servernew.getName());
			out.writeUTF("TeleportToWarp");
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

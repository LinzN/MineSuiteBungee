package de.nlinz.xeonSuite.bungee.out;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.JavaSocketServer;
import de.nlinz.xeonSocketBungee.mask.XeonSocketBungeeMask;
import de.nlinz.xeonSuite.bungee.XeonSuiteBungee;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonWarp;
import de.nlinz.xeonSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeleportToWarp {

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
		DataOutputStream out = JavaSocketServer.createChannel(bytes, XeonWarp.channelName);

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

		XeonSocketBungeeMask.inst().getSocketServer().sendBytesOut(bytes);
	}
}

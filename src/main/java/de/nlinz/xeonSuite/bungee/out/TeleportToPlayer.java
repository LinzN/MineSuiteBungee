package de.nlinz.xeonSuite.bungee.out;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonTeleport;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeleportToPlayer {

	public static void execute(ProxiedPlayer player, ProxiedPlayer target) {
		if (player.getServer().getInfo() != target.getServer().getInfo()) {
			player.connect(target.getServer().getInfo());
		}
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonTeleport.channelName);

		try {
			out.writeUTF(target.getServer().getInfo().getName());
			out.writeUTF("TeleportToPlayer");
			out.writeUTF(player.getName());
			out.writeUTF(target.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);
	}
}

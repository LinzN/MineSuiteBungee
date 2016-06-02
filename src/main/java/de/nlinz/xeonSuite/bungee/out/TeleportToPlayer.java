package de.nlinz.xeonSuite.bungee.out;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.keks.socket.bungee.BungeePlugin;
import de.keks.socket.core.Channel;

public class TeleportToPlayer {

	public static void execute(ProxiedPlayer player, ProxiedPlayer target) {
		if (player.getServer().getInfo() != target.getServer().getInfo()) {
			player.connect(target.getServer().getInfo());
		}
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(bytes);

		try {
			out.writeUTF(target.getServer().getInfo().getName());
			out.writeUTF("TeleportToPlayer");
			out.writeUTF(player.getName());
			out.writeUTF(target.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}
}

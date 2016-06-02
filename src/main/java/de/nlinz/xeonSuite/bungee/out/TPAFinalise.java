package de.nlinz.xeonSuite.bungee.out;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.keks.socket.bungee.BungeePlugin;
import de.keks.socket.core.Channel;

public class TPAFinalise {

	public static void execute(ProxiedPlayer player, ProxiedPlayer target) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(bytes);

		try {
			out.writeUTF(player.getServer().getInfo().getName());
			out.writeUTF("TeleportAccept");
			out.writeUTF(player.getName());
			out.writeUTF(target.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		BungeePlugin.instance().sendBytesOut(bytes);
	}
}

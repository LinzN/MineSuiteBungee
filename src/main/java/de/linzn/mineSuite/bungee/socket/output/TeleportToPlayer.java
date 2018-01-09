package de.linzn.mineSuite.bungee.socket.output;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TeleportToPlayer {

	public static void execute(ProxiedPlayer player, ProxiedPlayer target) {
		if (player.getServer().getInfo() != target.getServer().getInfo()) {
			player.connect(target.getServer().getInfo());
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF(target.getServer().getInfo().getName());
			dataOutputStream.writeUTF("TeleportToPlayer");
			dataOutputStream.writeUTF(player.getName());
			dataOutputStream.writeUTF(target.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
	}
}

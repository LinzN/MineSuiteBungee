package de.linzn.mineSuite.bungee.socket.output;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TPAFinalise {

	public static void execute(ProxiedPlayer player, ProxiedPlayer target) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF(player.getServer().getInfo().getName());
			dataOutputStream.writeUTF("TeleportAccept");
			dataOutputStream.writeUTF(player.getName());
			dataOutputStream.writeUTF(target.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
	}
}

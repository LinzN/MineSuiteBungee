package de.kekshaus.cookieApi.bungee.out;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.out.tasks.SendServerTeleportMessage;

public class TPAFinalise {

	public static void execute(ProxiedPlayer player, ProxiedPlayer target) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bytes);

		try {
			out.writeUTF(player.getServer().getInfo().getName());
			out.writeUTF("TeleportAccept");
			out.writeUTF(player.getName());
			out.writeUTF(target.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		CookieApiBungee.proxy.getScheduler().runAsync(CookieApiBungee.instance, new SendServerTeleportMessage(bytes));
	}
}

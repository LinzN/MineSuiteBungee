package de.kekshaus.cookieApi.bungee.out;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.out.tasks.SendServerTeleportMessage;
import de.kekshaus.cookieApi.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeleportToHome {

	public static void execute(ProxiedPlayer player, Location loc)

	{
		ServerInfo servernew = ProxyServer.getInstance().getServerInfo(loc.getServer());
		if (servernew == null) {
			CookieApiBungee.instance.getLogger()
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
		DataOutputStream out = new DataOutputStream(bytes);

		try {
			out.writeUTF(servernew.getName());
			out.writeUTF("TeleportToHome");
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

		CookieApiBungee.proxy.getScheduler().runAsync(CookieApiBungee.instance, new SendServerTeleportMessage(bytes));
	}
}

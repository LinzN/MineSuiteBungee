package de.kekshaus.cookieApi.bungee.out;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeleportToOther {

	public static void portalOtherServer(ProxiedPlayer player, String server) {
		ServerInfo servernew = ProxyServer.getInstance().getServerInfo(server);
		if (servernew == null) {
			CookieApiBungee.instance.getLogger().severe("Location has no Server, this should never happen. Please check");
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
	}
}

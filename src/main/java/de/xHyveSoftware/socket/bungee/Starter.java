package de.xHyveSoftware.socket.bungee;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.utils.Config;
import de.xHyveSoftware.socket.bungee.discover.DiscoveryTable;
import de.xHyveSoftware.socket.bungee.sockets.P2PServer;
import de.xHyveSoftware.socket.bungee.sockets.P2PServers;

public class Starter {

	@SuppressWarnings("deprecation")
	public static void start() {
		String ip = Config.getString("p2p.ip");
		int port = Config.getInt("p2p.port");
		int multicastport = Config.getInt("p2p.castport");
		;
		String networkInterface = "";
		String multicastip = Config.getString("p2p.castip");

		P2PServers.init(multicastip, multicastport, networkInterface);
		P2PServer server = new P2PServer(new DiscoveryTable(), ip, port);
		// server.start();
		CookieApiBungee.instance.getExecutorService().execute(server);

	}

	public static void stop() {
		for (P2PServer server : P2PServers.getServers()) {
			server.shutdown();
		}
	}
}

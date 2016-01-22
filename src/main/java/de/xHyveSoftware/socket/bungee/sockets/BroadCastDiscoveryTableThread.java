package de.xHyveSoftware.socket.bungee.sockets;

import de.xHyveSoftware.socket.bungee.packet.protocol.DiscoveryTable;
import de.xHyveSoftware.socket.bungee.util.Logger;

public class BroadCastDiscoveryTableThread implements Runnable {
	private P2PServer server;

	public BroadCastDiscoveryTableThread(P2PServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		try {
			DiscoveryTable discoveryTable = new DiscoveryTable();
			discoveryTable.setHosts(server.getDiscoveryTable().dump());

			server.broadcast(discoveryTable);
		} catch (Exception e) {
			Logger.error("Could not broadcast Discovery Table", e);
		}
	}
}

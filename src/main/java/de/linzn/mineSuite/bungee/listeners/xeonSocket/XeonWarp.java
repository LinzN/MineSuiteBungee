package de.linzn.mineSuite.bungee.listeners.xeonSocket;

import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.out.TeleportToWarp;
import de.linzn.mineSuite.bungee.utils.Location;
import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;

public class XeonWarp implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonWarp";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = XeonSocketServerManager.readDataInput(event.getStreamBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equals("TeleportToWarp")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportToWarp.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(),
						in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to warp!");
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
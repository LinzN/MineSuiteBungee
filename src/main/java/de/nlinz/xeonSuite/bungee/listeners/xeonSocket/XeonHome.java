package de.nlinz.xeonSuite.bungee.listeners.xeonSocket;

import java.io.DataInputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.JavaSocketServer;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import de.nlinz.xeonSuite.bungee.managers.PlayerManager;
import de.nlinz.xeonSuite.bungee.out.TeleportToHome;
import de.nlinz.xeonSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class XeonHome implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonHome";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = JavaSocketServer.createInputStream(event.getStreamBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equals("TeleportToHome")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportToHome.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(),
						in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to home!");
				return;
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

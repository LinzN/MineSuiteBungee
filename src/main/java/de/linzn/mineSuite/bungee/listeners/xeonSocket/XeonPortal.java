package de.linzn.mineSuite.bungee.listeners.xeonSocket;

import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.out.TeleportToOther;
import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.DataInputStream;
import java.io.IOException;

public class XeonPortal implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonPortal";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = XeonSocketServerManager.readDataInput(event.getStreamBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equals("TeleportToServer")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				if (player == null) {
					ProxyServer.getInstance().getLogger().info("[" + player + "] <-> task canceled. Is offline!");
					return;
				}
				TeleportToOther.portalOtherServer(player, in.readUTF());
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to server!");
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

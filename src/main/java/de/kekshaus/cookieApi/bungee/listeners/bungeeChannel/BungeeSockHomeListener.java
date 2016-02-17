package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.DataInputStream;
import java.io.IOException;

import de.keks.socket.bungee.events.plugin.BungeeSockHomeEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.kekshaus.cookieApi.bungee.managers.PlayerManager;
import de.kekshaus.cookieApi.bungee.out.TeleportToHome;
import de.kekshaus.cookieApi.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeSockHomeListener implements Listener {
	@EventHandler
	public void onSocketMessage(BungeeSockHomeEvent e) {
		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equals("TeleportToHome")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
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

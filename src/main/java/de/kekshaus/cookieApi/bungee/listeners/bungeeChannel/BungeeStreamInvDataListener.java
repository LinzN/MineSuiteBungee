package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import de.kekshaus.cookieApi.bungee.managers.InventarManager;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamInvDataEvent;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;

@Channel("BungeeStreamInvData")
public class BungeeStreamInvDataListener extends AbstractPacketListener {
	@PacketHandler
	public void onCookieApiMessageEvent(BungeeStreamInvDataEvent event) throws IOException, SQLException {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String task = in.readUTF();

		if (task.equals("SendPlayerData")) {
			InventarManager.setInventory(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(),
					in.readDouble(), in.readInt(), in.readInt(), in.readLong());
			return;
		}
		if (task.equals("SendSuccess")) {
			String playerName = in.readUTF();
			UUID playerUUID = UUID.fromString(in.readUTF());
			return;
		}

	}

}
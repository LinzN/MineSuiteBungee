package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.IOException;
import java.sql.SQLException;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.out.tasks.SendServerDataMessage;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamDataEvent;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;

@Channel("BungeeStreamData")
public class BungeeStreamDataListener extends AbstractPacketListener {
	@PacketHandler
	public void onCookieApiMessageEvent(BungeeStreamDataEvent event) throws IOException, SQLException {

		CookieApiBungee.proxy.getScheduler().runAsync(CookieApiBungee.instance, new SendServerDataMessage(event.getData()));
	}

}
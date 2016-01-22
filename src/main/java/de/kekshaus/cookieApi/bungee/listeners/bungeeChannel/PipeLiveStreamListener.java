package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.kekshaus.cookieApi.bungee.socketEvents.PipeLiveStreamEvent;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;
import net.md_5.bungee.api.ProxyServer;

@Channel("PipeStream")
public class PipeLiveStreamListener extends AbstractPacketListener {
	@PacketHandler
	public void onPipeLiveStreamEvent(PipeLiveStreamEvent event) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

		try {
			String pipeinfo = in.readUTF();
			ProxyServer.getInstance().getLogger().info("SOCKETSTREAM STATE: " + pipeinfo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
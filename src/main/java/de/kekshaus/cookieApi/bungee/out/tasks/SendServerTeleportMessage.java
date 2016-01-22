package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;

import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamTeleportEvent;

public class SendServerTeleportMessage implements Runnable {

	private final ByteArrayOutputStream bytes;

	public SendServerTeleportMessage(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}

	public void run() {
		ServerStreamTeleportEvent serverStreamEvent = new ServerStreamTeleportEvent();
		serverStreamEvent.setBytes(bytes.toByteArray());
		serverStreamEvent.send();
	}

}
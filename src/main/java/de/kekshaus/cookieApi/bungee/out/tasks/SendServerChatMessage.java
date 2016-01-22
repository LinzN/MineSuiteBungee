package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;

import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamChatEvent;

public class SendServerChatMessage implements Runnable {

	private final ByteArrayOutputStream bytes;

	public SendServerChatMessage(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}

	public void run() {
		ServerStreamChatEvent serverStreamEvent = new ServerStreamChatEvent();
		serverStreamEvent.setBytes(bytes.toByteArray());
		serverStreamEvent.send();
	}

}
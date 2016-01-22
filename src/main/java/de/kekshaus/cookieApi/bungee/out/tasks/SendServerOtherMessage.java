package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;

import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamOtherEvent;

public class SendServerOtherMessage implements Runnable {

	private final ByteArrayOutputStream bytes;

	public SendServerOtherMessage(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}

	public void run() {
		ServerStreamOtherEvent serverStreamEvent = new ServerStreamOtherEvent();
		serverStreamEvent.setBytes(bytes.toByteArray());
		serverStreamEvent.send();
	}

}
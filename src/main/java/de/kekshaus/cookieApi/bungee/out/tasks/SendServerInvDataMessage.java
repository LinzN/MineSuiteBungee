package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;

import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamInvDataEvent;

public class SendServerInvDataMessage implements Runnable {

	private final ByteArrayOutputStream bytes;

	public SendServerInvDataMessage(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}

	public void run() {
		ServerStreamInvDataEvent serverStreamEvent = new ServerStreamInvDataEvent();
		serverStreamEvent.setBytes(bytes.toByteArray());
		serverStreamEvent.send();
	}

}
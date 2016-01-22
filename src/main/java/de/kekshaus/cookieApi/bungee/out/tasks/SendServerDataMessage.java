package de.kekshaus.cookieApi.bungee.out.tasks;

import de.kekshaus.cookieApi.bungee.socketEvents.ServerStreamDataEvent;

public class SendServerDataMessage implements Runnable {

	private final byte[] bytes;

	public SendServerDataMessage(byte[] bytes) {
		this.bytes = bytes;
	}

	public void run() {
		ServerStreamDataEvent serverStreamEvent = new ServerStreamDataEvent();
		serverStreamEvent.setBytes(bytes);
		serverStreamEvent.send();
	}

}
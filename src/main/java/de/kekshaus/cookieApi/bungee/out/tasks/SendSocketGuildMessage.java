package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;

import de.kekshaus.cookieApi.bungee.socketEvents.SocketGuildEvent;

public class SendSocketGuildMessage implements Runnable {

	private final ByteArrayOutputStream bytes;

	public SendSocketGuildMessage(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}

	public void run() {
		SocketGuildEvent serverGuildStream = new SocketGuildEvent();
		serverGuildStream.setBytes(bytes.toByteArray());
		serverGuildStream.send();
	}

}
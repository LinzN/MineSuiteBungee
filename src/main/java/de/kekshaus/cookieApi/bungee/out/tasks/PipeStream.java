package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;

import de.kekshaus.cookieApi.bungee.socketEvents.PipeLiveStreamEvent;

public class PipeStream implements Runnable {

	private final ByteArrayOutputStream bytes;

	public PipeStream(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}

	public void run() {
		PipeLiveStreamEvent serverStreamEvent = new PipeLiveStreamEvent();
		serverStreamEvent.setBytes(bytes.toByteArray());
		serverStreamEvent.send();
	}

}
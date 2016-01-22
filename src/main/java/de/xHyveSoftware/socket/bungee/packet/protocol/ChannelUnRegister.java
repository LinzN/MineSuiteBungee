package de.xHyveSoftware.socket.bungee.packet.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;

public class ChannelUnRegister extends DefinedPacket {

	private String channel;

	@Override
	public void handle(DataInputStream input) throws IOException {
		channel = DefinedPacket.readString(input);
	}

	@Override
	public void handle(DataOutputStream output) throws IOException {
		DefinedPacket.writeString(channel, output);
	}
}

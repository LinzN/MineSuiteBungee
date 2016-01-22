package de.xHyveSoftware.socket.bungee.packet.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;

public class Message extends DefinedPacket {

	private long channel;

	private long packet;

	private byte[] message;

	@Override
	public void handle(DataInputStream input) throws IOException {
		channel = DefinedPacket.readLong(input);
		packet = DefinedPacket.readLong(input);
		message = DefinedPacket.readArray(input);
	}

	@Override
	public void handle(DataOutputStream output) throws IOException {
		DefinedPacket.writeLong(channel, output);
		DefinedPacket.writeLong(packet, output);
		DefinedPacket.writeArray(message, output);
	}

	public Long getChannel() {
		// TODO Auto-generated method stub
		return this.channel;
	}

	public Long getPacket() {
		// TODO Auto-generated method stub
		return this.packet;
	}

	public void setChannel(Long channelKey) {
		// TODO Auto-generated method stub
		this.channel = channelKey;

	}

	public void setPacket(Long classKey) {
		// TODO Auto-generated method stub
		this.packet = classKey;
	}

	public void setMessage(byte[] byteArray) {
		// TODO Auto-generated method stub
		this.message = byteArray;
	}

	public byte[] getMessage() {
		// TODO Auto-generated method stub
		return this.message;
	}
}

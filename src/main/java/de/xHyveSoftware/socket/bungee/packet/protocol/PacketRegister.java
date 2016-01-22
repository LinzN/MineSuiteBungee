package de.xHyveSoftware.socket.bungee.packet.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;

public class PacketRegister extends DefinedPacket {

	private Long channel;

	private Long packet;

	private byte mode;

	public void setMode(byte b) {
		this.mode = b;
	}

	public void setChannel(Long c) {
		this.channel = c;
	}

	public void setPacket(Long p) {
		this.packet = p;
	}

	@Override
	public void handle(DataInputStream input) throws IOException {
		channel = DefinedPacket.readLong(input);
		packet = DefinedPacket.readLong(input);
		mode = (byte) input.read();
	}

	@Override
	public void handle(DataOutputStream output) throws IOException {
		DefinedPacket.writeLong(channel, output);
		DefinedPacket.writeLong(packet, output);
		output.write(mode);
	}

	public int getMode() {
		// TODO Auto-generated method stub
		return mode;
	}

	public Long getChannel() {
		// TODO Auto-generated method stub
		return this.channel;
	}

	public Long getPacket() {
		// TODO Auto-generated method stub
		return this.packet;
	}
}

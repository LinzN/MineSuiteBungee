package de.xHyveSoftware.socket.bungee.packet.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;

public class DiscoveryTable extends DefinedPacket {

	private String[] hosts;

	@Override
	public void handle(DataInputStream input) throws IOException {
		hosts = DefinedPacket.readStringArray(input);
	}

	@Override
	public void handle(DataOutputStream output) throws IOException {
		DefinedPacket.writeStringArray(hosts, output);
	}

	public String[] getHosts() {
		// TODO Auto-generated method stub
		return this.hosts;
	}

	public void setHosts(String[] dump) {
		// TODO Auto-generated method stub
		this.hosts = dump;
	}
}

package de.xHyveSoftware.socket.bungee.api.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Message {
	public Message write(DataOutputStream outputStream);

	public Message read(DataInputStream inputStream);

	public Message send();

	public String getChannel();
}

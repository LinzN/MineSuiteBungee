
package de.xHyveSoftware.socket.bungee.sockets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketException;

import de.xHyveSoftware.socket.bungee.packet.ByteHolder;
import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;
import de.xHyveSoftware.socket.bungee.packet.ProtocolHandler;
import de.xHyveSoftware.socket.bungee.util.Logger;

public class ClientRunnable extends Thread {

	private P2PClient client;

	public ClientRunnable(P2PClient client) {

		this.client = client;

		setName("Client-" + client.getSocket().getRemoteSocketAddress());
	}

	@Override
	public void run() {

		while (!client.getSocket().isClosed()) {
			try {
				if (client.getSocket().getInputStream().available() > 0 || client.getQueue().size() > 0
						|| client.getByteQueue().size() > 0) {
					if (client.getSocket().getInputStream().available() > 0) {
						while (client.getSocket().getInputStream().available() > 0) {
							DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(
									DefinedPacket.readArray(client.getSocket().getInputStream())));
							client.getPacketHandler().handle(ProtocolHandler.readPacket(dataInputStream));
							dataInputStream.close();
						}
					}

					if (client.getByteQueue().size() > 0) {
						synchronized (client.getByteQueue()) {
							while (client.getByteQueue().size() > 0) {
								ByteHolder byteHolder = client.getByteQueue().poll();
								try {
									client.getDataOutputStream().write(byteHolder.getBytes());
								} catch (SocketException e) {

									System.out.println("MulticastServer: Verbindung zu " + client.getSocket().toString()
											+ " verloren!");
									client.getServer().removeClient(client);
								}
							}
						}
					}

					if (client.getQueue().size() > 0) {
						synchronized (client.getQueue()) {
							while (client.getQueue().size() > 0) {
								DefinedPacket packet = client.getQueue().poll();

								ByteArrayOutputStream bao = new ByteArrayOutputStream();
								DataOutputStream dataOutputStream1 = new DataOutputStream(bao);
								byte packetID = ProtocolHandler.getPacketID(packet);
								dataOutputStream1.write(packetID);
								packet.handle(dataOutputStream1);
								dataOutputStream1.close();

								DefinedPacket.writeArray(bao.toByteArray(), client.getDataOutputStream());
							}
						}
					}
				}

				Thread.sleep(5);
			} catch (Exception e) {
				Logger.error("Could not tick client", e);
				client.getServer().removeClient(client);
			}
		}

		client.getServer().removeClient(client);
	}
}

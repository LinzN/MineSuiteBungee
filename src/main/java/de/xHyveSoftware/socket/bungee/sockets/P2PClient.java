package de.xHyveSoftware.socket.bungee.sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;

import de.xHyveSoftware.socket.bungee.cache.RegisteredPacketsCache;
import de.xHyveSoftware.socket.bungee.packet.ByteHolder;
import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;
import de.xHyveSoftware.socket.bungee.packet.PacketHandler;

public class P2PClient {
	private Socket socket;
	private P2PServer server;
	private final ArrayDeque<DefinedPacket> queue = new ArrayDeque<>();
	private final ArrayDeque<ByteHolder> byteQueue = new ArrayDeque<>();
	private final PacketHandler packetHandler;
	private final DataOutputStream dataOutputStream;
	private final RegisteredPacketsCache packetsCache = new RegisteredPacketsCache();

	public Socket getSocket() {
		return this.socket;

	}

	public RegisteredPacketsCache getPacketsCache() {
		return this.packetsCache;

	}

	public P2PClient(Socket socket, P2PServer server) throws IOException {
		packetHandler = new PacketHandler(this);
		dataOutputStream = new DataOutputStream(socket.getOutputStream());

		this.socket = socket;
		this.server = server;

		ClientRunnable clientRunnable = new ClientRunnable(this);
		clientRunnable.start();
	}

	public synchronized void byteWrite(byte[] bytes) throws IOException {
		synchronized (byteQueue) {
			byteQueue.add(new ByteHolder(bytes));
		}
	}

	public P2PServer getServer() {
		// TODO Auto-generated method stub
		return this.server;
	}

	public PacketHandler getPacketHandler() {
		// TODO Auto-generated method stub
		return this.packetHandler;
	}

	public ArrayDeque<ByteHolder> getByteQueue() {
		// TODO Auto-generated method stub
		return this.byteQueue;
	}

	public DataOutputStream getDataOutputStream() {
		// TODO Auto-generated method stub
		return this.dataOutputStream;
	}

	public ArrayDeque<DefinedPacket> getQueue() {
		// TODO Auto-generated method stub
		return this.queue;
	}
}

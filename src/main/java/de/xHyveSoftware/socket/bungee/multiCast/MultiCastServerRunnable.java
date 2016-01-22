package de.xHyveSoftware.socket.bungee.multiCast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Arrays;

import de.xHyveSoftware.socket.bungee.sockets.P2PServer;
import de.xHyveSoftware.socket.bungee.sockets.P2PServers;
import de.xHyveSoftware.socket.bungee.util.ArrayUtil;
import de.xHyveSoftware.socket.bungee.util.Logger;

public class MultiCastServerRunnable implements Runnable {
	private MulticastSocket multicastSocket;
	private InetAddress group;
	private int runs = 0;
	private int port;

	public MultiCastServerRunnable(String CastIP, int castport, String network) throws IOException {
		group = InetAddress.getByName(CastIP);
		multicastSocket = new MulticastSocket(castport);
		port = castport;

		if (!network.equals("")) {
			multicastSocket.setNetworkInterface(NetworkInterface.getByName(network));
		}

		multicastSocket.joinGroup(group);
	}

	@Override
	public void run() {
		while (true) {
			if (runs > 30 * 20) {
				try {
					for (P2PServer server : P2PServers.getServers()) {
						String host = server.getServerSocket().getInetAddress().getHostAddress() + ":"
								+ server.getServerSocket().getLocalPort();
						DatagramPacket hi = new DatagramPacket(host.getBytes(), host.length(), group, port);
						multicastSocket.send(hi);
					}
				} catch (IOException e) {
					Logger.error("Could not send MultiCast", e);
				}

				runs = 0;
			} else {
				runs++;
			}

			try {
				byte[] buf = new byte[1000];
				DatagramPacket recv = new DatagramPacket(buf, buf.length);
				multicastSocket.receive(recv);

				byte[] recvBuf = recv.getData();

				switch (recvBuf[0]) {
				case 0:
					String recHost = new String(Arrays.copyOfRange(recvBuf, 1, recv.getLength()));
					for (P2PServer server : P2PServers.getServers()) {
						if (!server.getDiscoveryTable().isKnownHost(recHost)) {
							server.getDiscoveryTable().add(recHost.replaceAll("\\u0000", ""));
						}
					}

					break;
				case 1:
					for (P2PServer server : P2PServers.getServers()) {
						String host = server.getServerSocket().getInetAddress().getHostAddress() + ":"
								+ server.getServerSocket().getLocalPort();
						DatagramPacket hi = new DatagramPacket(ArrayUtil.concatenate(new byte[] { 0 }, host.getBytes()),
								host.length() + 1, group, port);
						multicastSocket.send(hi);
					}

					break;
				}
			} catch (IOException e) {
				Logger.error("Could not get MultiCast data", e);
			}
		}
	}

	public void requestDiscovery() throws IOException {
		DatagramPacket reqDiscovery = new DatagramPacket(new byte[] { 1 }, 1, group, port);
		multicastSocket.send(reqDiscovery);
	}
}

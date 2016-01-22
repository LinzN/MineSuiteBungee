package de.xHyveSoftware.socket.bungee.sockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.xHyveSoftware.socket.bungee.cache.RegisteredPacketsCache;
import de.xHyveSoftware.socket.bungee.multiCast.MultiCastServerRunnable;
import de.xHyveSoftware.socket.bungee.packet.DefinedPacket;
import de.xHyveSoftware.socket.bungee.packet.protocol.Message;
import de.xHyveSoftware.socket.bungee.util.Logger;

public class P2PServers {
	private final static LinkedHashSet<P2PServer> servers = new LinkedHashSet<>();
	private final static RegisteredPacketsCache packetCache = new RegisteredPacketsCache();
	private static MultiCastServerRunnable multiCastServerRunnable;

	@SuppressWarnings("deprecation")
	public static void init(String CastIP, int castport, String network) {
		try {
			multiCastServerRunnable = new MultiCastServerRunnable(CastIP, castport, network);
			CookieApiBungee.instance.getExecutorService().execute(multiCastServerRunnable);
			// multiCastServerRunnable.start();
		} catch (IOException e) {
			Logger.error("Could not setup MultiCast", e);
		}
	}

	public static void requestDiscovery() {
		try {
			multiCastServerRunnable.requestDiscovery();
		} catch (IOException e) {
			Logger.error("Could not request Discovery", e);
		}
	}

	public static void registerPacket(Long channel, Long packet) {
		packetCache.addToCache(channel, packet);
	}

	public static void removePacket(Long channel, Long packet) {
		packetCache.removeFromCache(channel, packet);
	}

	public static LinkedHashMap<Long, ArrayList<Long>> getPacketCacheDump() {
		return packetCache.getDump();
	}

	public static void broadCastToAll(DefinedPacket packet) {
		synchronized (servers) {
			for (P2PServer server : servers) {
				try {
					server.broadcast(packet);
				} catch (IOException e) {
					Logger.warn("Server could not send packet", e);
				}
			}
		}
	}

	public static void broadCastToAll(Message packet) {
		synchronized (servers) {
			for (P2PServer server : servers) {
				try {
					server.broadcast(packet);
				} catch (IOException e) {
					Logger.warn("Server could not send packet", e);
				}
			}
		}
	}

	public static void addServer(P2PServer server) {
		synchronized (servers) {
			servers.add(server);
		}
	}

	public static void removeServer(P2PServer server) {
		synchronized (servers) {
			servers.remove(server);
		}
	}

	public static LinkedHashSet<P2PServer> getServers() {
		synchronized (servers) {
			return new LinkedHashSet<>(servers);
		}
	}
}

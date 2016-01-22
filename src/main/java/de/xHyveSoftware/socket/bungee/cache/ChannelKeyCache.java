package de.xHyveSoftware.socket.bungee.cache;

import java.util.LinkedHashMap;

import de.xHyveSoftware.socket.bungee.util.StringCode;

public class ChannelKeyCache {
	private final static LinkedHashMap<String, Long> cache = new LinkedHashMap<>();

	public static void addToCache(String channel) {
		synchronized (cache) {
			cache.put(channel, StringCode.getStringCode(channel));
		}
	}

	public static Long getKey(String channel) {
		return cache.get(channel);
	}

	public static void removeFromCache(String channel) {
		synchronized (cache) {
			cache.remove(channel);
		}
	}
}

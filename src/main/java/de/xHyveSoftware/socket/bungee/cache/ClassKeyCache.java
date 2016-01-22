package de.xHyveSoftware.socket.bungee.cache;

import java.util.LinkedHashMap;

import de.xHyveSoftware.socket.bungee.util.StringCode;

@SuppressWarnings("rawtypes")
public class ClassKeyCache {
	private final static LinkedHashMap<Class, Long> cache = new LinkedHashMap<>();

	public static void addToCache(Class clazz) {
		synchronized (cache) {
			cache.put(clazz, StringCode.getStringCode(clazz.getName()));
		}
	}

	public static Long getKey(Class clazz) {
		return cache.get(clazz);
	}

	public static void removeFromCache(Class clazz) {
		synchronized (cache) {
			cache.remove(clazz);
		}
	}
}

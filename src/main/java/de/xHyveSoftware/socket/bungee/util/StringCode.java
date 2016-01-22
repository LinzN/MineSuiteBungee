package de.xHyveSoftware.socket.bungee.util;

public class StringCode {
	public static long getStringCode(String string) {
		long hash = 31;

		for (int i = 0; i < string.length(); i++) {
			hash += hash * 7 + string.charAt(i);
		}

		return hash;
	}
}

package de.xHyveSoftware.socket.bungee.util;

import java.lang.reflect.Array;

public class ArrayUtil {
	public static byte[] concatenate(byte[] A, byte[] B) {
		int aLen = A.length;
		int bLen = B.length;

		byte[] C = (byte[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);

		return C;
	}
}

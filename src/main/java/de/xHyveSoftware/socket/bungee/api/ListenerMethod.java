package de.xHyveSoftware.socket.bungee.api;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import de.xHyveSoftware.socket.bungee.api.listener.PacketListener;
import de.xHyveSoftware.socket.bungee.util.Logger;

public class ListenerMethod {
	private MethodHandle method;
	private PacketListener packetListener;

	public ListenerMethod(Method method, PacketListener packetListener) {
		try {
			this.method = MethodHandles.lookup().unreflect(method);
			this.packetListener = packetListener;
		} catch (IllegalAccessException e) {
			Logger.error("Could not setup Listener Method", e);
		}
	}

	public MethodHandle getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	public PacketListener getPacketListener() {
		// TODO Auto-generated method stub
		return this.packetListener;
	}
}

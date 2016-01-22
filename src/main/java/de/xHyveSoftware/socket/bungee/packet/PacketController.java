package de.xHyveSoftware.socket.bungee.packet;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import de.xHyveSoftware.socket.bungee.util.Logger;

public class PacketController {
	@SuppressWarnings("rawtypes")
	private static final LinkedHashMap<Class, MethodHandle> methods = new LinkedHashMap<>();

	static {
		for (Method method : PacketHandler.class.getMethods()) {
			if (method.getName().equals("handle")) {
				@SuppressWarnings("rawtypes")
				Class clazz = method.getParameterTypes()[0];
				try {
					methods.put(clazz, MethodHandles.lookup().unreflect(method));
				} catch (IllegalAccessException e) {
					Logger.warn("Could not get method for reflection", e);
				}
			}
		}
	}

	public void handle(DefinedPacket packet) {
		try {
			methods.get(packet.getClass()).invoke(this, packet);
		} catch (Throwable e) {
			Logger.warn("Could not invoke method for PacketHandler");
		}
	}
}

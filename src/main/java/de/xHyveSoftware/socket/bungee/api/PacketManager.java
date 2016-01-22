package de.xHyveSoftware.socket.bungee.api;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.PacketListener;
import de.xHyveSoftware.socket.bungee.cache.ChannelKeyCache;
import de.xHyveSoftware.socket.bungee.cache.ClassKeyCache;
import de.xHyveSoftware.socket.bungee.packet.protocol.Message;
import de.xHyveSoftware.socket.bungee.packet.protocol.PacketRegister;
import de.xHyveSoftware.socket.bungee.sockets.P2PServers;
import de.xHyveSoftware.socket.bungee.util.Logger;
import de.xHyveSoftware.socket.bungee.util.StringCode;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class PacketManager {
	private final static LinkedHashMap<Long, LinkedHashMap<Long, ArrayList<ListenerMethod>>> listeners = new LinkedHashMap<>();
	private final static LinkedHashMap<Long, LinkedHashMap<Long, Class>> packets = new LinkedHashMap<>();
	private final static BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

	private static class PacketDequeue implements Runnable {
		public PacketDequeue() {
			// setName("PacketDequeue");
		}

		public void run() {
			while (true) {
				try {
					Message message = messageQueue.take();
					callListeners(message);
				} catch (InterruptedException e) {

				}
			}
		}
	}

	static {
		CookieApiBungee.instance.getExecutorService().execute(new PacketDequeue());
		// new PacketDequeue().start();
	}

	public static void callListeners(Message message) {
		synchronized (listeners) {
			if (!listeners.containsKey(message.getChannel())
					|| !listeners.get(message.getChannel()).containsKey(message.getPacket())) {
				return;
			}

			if (!packets.containsKey(message.getChannel())) {
				return;
			}

			// Build up that message
			if (!packets.get(message.getChannel()).containsKey(message.getPacket())) {
				return;
			}

			Class packetClass = packets.get(message.getChannel()).get(message.getPacket());

			try {
				de.xHyveSoftware.socket.bungee.api.message.Message message1 = (de.xHyveSoftware.socket.bungee.api.message.Message) packetClass
						.newInstance();
				message1.read(new DataInputStream(new ByteArrayInputStream(message.getMessage())));

				for (ListenerMethod listenerMethod : listeners.get(message.getChannel()).get(message.getPacket())) {
					listenerMethod.getMethod().invoke(listenerMethod.getPacketListener(), message1);
				}
			} catch (Throwable throwable) {
				Logger.warn("Could not execute Listeners", throwable);
			}
		}
	}

	public static void registerListener(PacketListener listener) {
		// Find the Channel
		String channel;
		if (listener.getClass().isAnnotationPresent(Channel.class)) {
			channel = listener.getClass().getAnnotation(Channel.class).value();
		} else {
			channel = listener.getChannel();
		}

		// If there is no channel for this Packet => Outta here
		if (channel == null) {
			Logger.warn("The listener has no Channel annotation and did gave null back on getChannel() - "
					+ listener.getClass().getName());
			return;
		}

		Long key = StringCode.getStringCode(channel);

		for (Method method : listener.getClass().getMethods()) {
			if (method.isAnnotationPresent(PacketHandler.class)) {
				Class argument = method.getParameterTypes()[0];

				if (!de.xHyveSoftware.socket.bungee.api.message.Message.class.isAssignableFrom(argument)) {
					continue;
				}

				ListenerMethod listenerMethod = new ListenerMethod(method, listener);

				Long argumentKey = StringCode.getStringCode(argument.getName());

				synchronized (packets) {
					if (!packets.containsKey(key) || !packets.get(key).containsKey(argumentKey)) {
						continue;
					}
				}

				synchronized (listeners) {
					// Check if Channel exists
					if (listeners.containsKey(key)) {
						if (listeners.get(key).containsKey(argumentKey)) {
							listeners.get(key).get(argumentKey).add(listenerMethod);
						} else {
							ArrayList<ListenerMethod> listeners1 = new ArrayList<>();
							listeners1.add(listenerMethod);

							listeners.get(key).put(argumentKey, listeners1);

							PacketRegister packetRegister = new PacketRegister();
							packetRegister.setMode((byte) 0);
							packetRegister.setChannel(key);
							packetRegister.setPacket(argumentKey);

							P2PServers.broadCastToAll(packetRegister);
							P2PServers.registerPacket(key, argumentKey);
						}
					} else {
						ArrayList<ListenerMethod> listeners1 = new ArrayList<>();
						listeners1.add(listenerMethod);

						LinkedHashMap<Long, ArrayList<ListenerMethod>> newHashMap = new LinkedHashMap<>();
						newHashMap.put(argumentKey, listeners1);

						listeners.put(key, newHashMap);

						PacketRegister packetRegister = new PacketRegister();
						packetRegister.setMode((byte) 0);
						packetRegister.setChannel(key);
						packetRegister.setPacket(argumentKey);

						P2PServers.broadCastToAll(packetRegister);
						P2PServers.registerPacket(key, argumentKey);
					}
				}
			}
		}
	}

	public static void removeListener(PacketListener listener) {
		// Find the Channel
		String channel;
		if (listener.getClass().isAnnotationPresent(Channel.class)) {
			channel = listener.getClass().getAnnotation(Channel.class).value();
		} else {
			channel = listener.getChannel();
		}

		// If there is no channel for this Packet => Outta here
		if (channel == null) {
			Logger.warn("The listener has no Channel annotation and did gave null back on getChannel() - "
					+ listener.getClass().getName());
			return;
		}

		Long key = StringCode.getStringCode(channel);

		for (Method method : listener.getClass().getMethods()) {
			if (method.isAnnotationPresent(PacketHandler.class)) {
				Class argument = method.getParameterTypes()[0];

				if (!de.xHyveSoftware.socket.bungee.api.message.Message.class.isAssignableFrom(argument)) {
					continue;
				}

				Long argumentKey = StringCode.getStringCode(argument.getName());

				synchronized (listeners) {
					// Check if Channel exists
					if (listeners.containsKey(key) && listeners.get(key).containsKey(argumentKey)) {
						Iterator<ListenerMethod> iterator = listeners.get(key).get(argumentKey).iterator();

						while (iterator.hasNext()) {
							ListenerMethod method1 = iterator.next();

							if (method1.getPacketListener().equals(listener)) {
								iterator.remove();
							}
						}

						if (listeners.get(key).get(argumentKey).size() == 0) {
							PacketRegister packetRegister = new PacketRegister();
							packetRegister.setMode((byte) 1);
							packetRegister.setChannel(key);
							packetRegister.setPacket(argumentKey);

							P2PServers.broadCastToAll(packetRegister);
							P2PServers.removePacket(key, argumentKey);

							listeners.get(key).remove(argumentKey);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void registerPacket(Class packet) {
		if (!de.xHyveSoftware.socket.bungee.api.message.Message.class.isAssignableFrom(packet)) {
			Logger.warn("This packet does not implement the Message Interface - " + packet.getName());
			return;
		}

		// Create the Packet once
		try {
			de.xHyveSoftware.socket.bungee.api.message.Message message = (de.xHyveSoftware.socket.bungee.api.message.Message) packet
					.newInstance();

			// Find the Channel to which this Packet belongs
			String channel;
			if (packet.isAnnotationPresent(Channel.class)) {
				channel = ((Channel) packet.getAnnotation(Channel.class)).value();
			} else {
				channel = message.getChannel();
			}

			// If there is no channel for this Packet => Outta here
			if (channel == null) {
				Logger.warn("The packet has no Channel annotation and did gave null back on getChannel() - "
						+ packet.getName());
				return;
			}

			Long key = StringCode.getStringCode(channel);
			Long classKey = StringCode.getStringCode(packet.getName());

			synchronized (packets) {
				// Check if Channel exists
				if (packets.containsKey(key)) {
					packets.get(key).put(classKey, packet);
				} else {
					LinkedHashMap<Long, Class> classes = new LinkedHashMap<>();
					classes.put(classKey, packet);

					packets.put(key, classes);
					ChannelKeyCache.addToCache(channel);
				}

				ClassKeyCache.addToCache(packet);
			}
		} catch (Exception e) {
			Logger.warn("The packet could not be build. Make sure it has a default Constructor - " + packet.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public static void removePacket(Class packet) {
		if (!de.xHyveSoftware.socket.bungee.api.message.Message.class.isAssignableFrom(packet)) {
			Logger.warn("This packet does not implement the Message Interface - " + packet.getName());
			return;
		}

		// Create the Packet once
		try {
			de.xHyveSoftware.socket.bungee.api.message.Message message = (de.xHyveSoftware.socket.bungee.api.message.Message) packet
					.newInstance();

			// Find the Channel to which this Packet belongs
			String channel;
			if (packet.isAnnotationPresent(Channel.class)) {
				channel = ((Channel) packet.getAnnotation(Channel.class)).value();
			} else {
				channel = message.getChannel();
			}

			// If there is no channel for this Packet => Outta here
			if (channel == null) {
				Logger.warn("The packet has no Channel annotation and did gave null back on getChannel() - "
						+ packet.getName());
				return;
			}

			Long key = StringCode.getStringCode(channel);
			Long classKey = StringCode.getStringCode(packet.getName());

			synchronized (packets) {
				// Check if Channel exists
				if (packets.containsKey(key)) {
					packets.get(key).remove(classKey);
					ClassKeyCache.removeFromCache(packet);
				}

				if (packets.get(key).size() == 0) {
					ChannelKeyCache.removeFromCache(channel);
				}
			}

			synchronized (listeners) {
				if (listeners.containsKey(key)) {
					listeners.get(key).remove(classKey);

					PacketRegister packetRegister = new PacketRegister();
					packetRegister.setMode((byte) 1);
					packetRegister.setChannel(key);
					packetRegister.setPacket(classKey);

					P2PServers.broadCastToAll(packetRegister);
					P2PServers.removePacket(key, classKey);
				}
			}
		} catch (Exception e) {
			Logger.warn("The packet could not be build. Make sure it has a default Constructor - " + packet.getName());
		}
	}

	public static void addMessage(Message message) {
		messageQueue.add(message);
	}
}

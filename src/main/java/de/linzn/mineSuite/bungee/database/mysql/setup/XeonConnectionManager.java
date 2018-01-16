package de.linzn.mineSuite.bungee.database.mysql.setup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class XeonConnectionManager {

	public final static XeonConnectionManager DEFAULT = new XeonConnectionManager();

	private final Map<String, XeonConnectionHandler> map;

	public XeonConnectionHandler getHandler(String key, XeonConnectionFactory f) {
		XeonConnectionHandler handler = new XeonConnectionHandler(key, f);
		map.put(key, handler);
		return handler;
	}

	public Connection getConnection(String handle) throws SQLException {
		return map.get(handle).getConnection();
	}

	public void release(String handle, Connection c) {
		map.get(handle).release(c);
	}

	public XeonConnectionHandler getHandler(String key) {
		XeonConnectionHandler handler = map.get(key);
		if (handler == null) {
			throw new NoSuchElementException();
		}
		return handler;
	}

	public XeonConnectionManager() {
		this.map = new ConcurrentHashMap<>();
	}

	public void shutdown() {
		for (XeonConnectionHandler handler : map.values()) {
			handler.shutdown();
		}
		map.clear();
	}
}
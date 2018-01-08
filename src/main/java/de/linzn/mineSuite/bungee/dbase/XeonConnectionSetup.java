package de.linzn.mineSuite.bungee.dbase;

import de.linzn.mineSuite.bungee.utils.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.Statement;

public class XeonConnectionSetup {
	public static boolean create() {
		return mysql();

	}

	public static boolean mysql() {
		String dbHost = Config.ConfigConfiguration.getString("sql.host");
		int dbPort = Config.ConfigConfiguration.getInt("sql.port");
		String database = Config.ConfigConfiguration.getString("sql.database");
		String dbUser = Config.ConfigConfiguration.getString("sql.username");
		String dbPassword = Config.ConfigConfiguration.getString("sql.password");
		String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database;
		XeonConnectionFactory factory = new XeonConnectionFactory(url, dbUser, dbPassword);
		XeonConnectionManager manager = XeonConnectionManager.DEFAULT;
		XeonConnectionHandler handler = manager.getHandler("XeonSuite", factory);

		try {
			Connection connection = handler.getConnection();
			String sql = "CREATE TABLE IF NOT EXISTS mutes (Id int NOT NULL AUTO_INCREMENT, UUID text, Muted text, Reason text, MutedBy text, MutedAt bigint, ExpireTime bigint, UnMutedBy text, UnMutedReason text, PRIMARY KEY (Id));";
			String sql2 = "CREATE TABLE IF NOT EXISTS bans (Id int NOT NULL AUTO_INCREMENT, UUID text, Banned text, Reason text, BannedBy text, BannedAt bigint, ExpireTime bigint, UnBannedBy text, UnBannedReason text, PRIMARY KEY (Id));";
			String sql3 = "CREATE TABLE IF NOT EXISTS uuidcache (Id int NOT NULL AUTO_INCREMENT, UUID text, NAME text, TIMESTAMP bigint, PRIMARY KEY (id));";
			Statement action = connection.createStatement();
			action.executeUpdate(sql);
			action.executeUpdate(sql2);
			action.executeUpdate(sql3);
			action.close();
			handler.release(connection);

			return true;

		} catch (Exception e) {
			ProxyServer.getInstance().getConsole()
					.sendMessage(ChatColor.RED + "=================XeonSuite-ERROR================");
			ProxyServer.getInstance().getConsole().sendMessage(ChatColor.RED + "Unable to connect to database.");
			ProxyServer.getInstance().getConsole()
					.sendMessage(ChatColor.RED + "Pls check you mysql connection in config.yml.");
			ProxyServer.getInstance().getConsole()
					.sendMessage(ChatColor.RED + "=================XeonSuite-ERROR================");
			e.printStackTrace();
			return false;
		}

	}

}

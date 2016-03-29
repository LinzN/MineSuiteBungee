package de.kekshaus.cookieApi.bungee.dbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import de.kekshaus.cookieApi.bungee.utils.Config;

public class DataBaseManager {

	public static boolean setupDatabase() {
		String dbHost = Config.ConfigConfiguration.getString("sql.host");
		int dbPort = Config.ConfigConfiguration.getInt("sql.port");
		String database = Config.ConfigConfiguration.getString("sql.database");
		String dbUser = Config.ConfigConfiguration.getString("sql.username");
		String dbPassword = Config.ConfigConfiguration.getString("sql.password");
		String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database;

		ConnectionFactory factory = new ConnectionFactory(url, dbUser, dbPassword);
		ConnectionManager manager = ConnectionManager.DEFAULT;
		ConnectionHandler handler = manager.getHandler("bungeeapi", factory);

		try {
			Connection connection = handler.getConnection();
			String sql = "CREATE TABLE IF NOT EXISTS mutes (Id int NOT NULL AUTO_INCREMENT, UUID text, Muted text, Reason text, MutedBy text, MutedAt bigint, ExpireTime bigint, UnMutedBy text, UnMutedReason text, PRIMARY KEY (Id));";
			String sql2 = "CREATE TABLE IF NOT EXISTS bans (Id int NOT NULL AUTO_INCREMENT, UUID text, Banned text, Reason text, BannedBy text, BannedAt bigint, ExpireTime bigint, UnBannedBy text, UnBannedReason text, PRIMARY KEY (Id));";
			String sql3 = "CREATE TABLE IF NOT EXISTS uuidcache (Id int NOT NULL AUTO_INCREMENT, UUID text, NAME text, PRIMARY KEY (id));";
			Statement action = connection.createStatement();
			action.executeUpdate(sql);
			action.executeUpdate(sql2);
			action.executeUpdate(sql3);
			action.close();
			handler.release(connection);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}

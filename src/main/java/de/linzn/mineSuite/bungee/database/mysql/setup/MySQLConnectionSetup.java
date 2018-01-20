/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.bungee.database.mysql.setup;

import de.linzn.mineSuite.bungee.utils.Config;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.Statement;

public class MySQLConnectionSetup {
    public static boolean create() {
        return mysql();

    }

    public static boolean mysql() {
        String dbHost = Config.ConfigConfiguration.getString("mysql.host");
        int dbPort = Config.ConfigConfiguration.getInt("mysql.port");
        String database = Config.ConfigConfiguration.getString("mysql.database");
        String dbUser = Config.ConfigConfiguration.getString("mysql.username");
        String dbPassword = Config.ConfigConfiguration.getString("mysql.password");
        String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database;
        MySQLConnectionFactory factory = new MySQLConnectionFactory(url, dbUser, dbPassword);
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        MySQLConnectionHandler handler = manager.getHandler("MineSuiteBungee", factory);
        manager.getHandler("MineSuiteBan", factory);
        manager.getHandler("MineSuiteWarp", factory);
        manager.getHandler("MineSuiteHome", factory);
        manager.getHandler("MineSuiteTeleport", factory);
        manager.getHandler("MineSuitePortal", factory);

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
                    .sendMessage(ChatColor.RED + "=================MineSuiteBungee-ERROR================");
            ProxyServer.getInstance().getConsole().sendMessage(ChatColor.RED + "Unable to connect to database.");
            ProxyServer.getInstance().getConsole()
                    .sendMessage(ChatColor.RED + "Pls check you mysql connection in config.yml.");
            ProxyServer.getInstance().getConsole()
                    .sendMessage(ChatColor.RED + "=================MineSuiteBungee-ERROR================");
            e.printStackTrace();
            return false;
        }

    }

}
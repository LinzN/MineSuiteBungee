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

import de.linzn.mineSuite.bungee.core.Config;
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
        manager.getHandler("MineSuiteGuild", factory);
        manager.getHandler("MineSuiteChat", factory);
        manager.getHandler("mineSuiteEconomy", factory);

        try {
            Connection connection = handler.getConnection();
            String core = "CREATE TABLE IF NOT EXISTS core_uuidcache (Id int NOT NULL AUTO_INCREMENT, UUID text, NAME text, TIMESTAMP bigint, PRIMARY KEY (id));";

            String ban_module_1 = "CREATE TABLE IF NOT EXISTS module_ban_mutes (Id int NOT NULL AUTO_INCREMENT, UUID text, Muted text, Reason text, MutedBy text, MutedAt bigint, ExpireTime bigint, UnMutedBy text, UnMutedReason text, PRIMARY KEY (Id));";
            String ban_module_2 = "CREATE TABLE IF NOT EXISTS module_ban_bans (Id int NOT NULL AUTO_INCREMENT, UUID text, Banned text, Reason text, BannedBy text, BannedAt bigint, ExpireTime bigint, UnBannedBy text, UnBannedReason text, PRIMARY KEY (Id));";

            String warp_module_1 = "CREATE TABLE IF NOT EXISTS module_warp_warps (player VARCHAR(100), warp_name VARCHAR(100), server VARCHAR(100), world text, x double, y double, z double, yaw float, pitch float, visible int, PRIMARY KEY (`warp_name`));";
            String teleport_module_1 = "CREATE TABLE IF NOT EXISTS module_teleport_spawns (Id int NOT NULL AUTO_INCREMENT, spawntype VARCHAR(100), server VARCHAR(100), world text, x double, y double, z double, yaw float, pitch float, visible int, PRIMARY KEY (Id));";
            String portal_module_1 = "CREATE TABLE IF NOT EXISTS module_portal_portals (portalname VARCHAR(100), server VARCHAR(100), type VARCHAR(20), destination VARCHAR(100), world VARCHAR(100), filltype VARCHAR(100) DEFAULT 'AIR', xmax INT(11), xmin INT(11), ymax INT(11), ymin INT(11), zmax INT(11), zmin INT(11), CONSTRAINT pk_portalname PRIMARY KEY (portalname));";
            String home_module_1 = "CREATE TABLE IF NOT EXISTS module_home_homes (player VARCHAR(100), home_name VARCHAR(100), server VARCHAR(100), world text, x double, y double, z double, yaw float, pitch float, PRIMARY KEY (`player`,`home_name`,`server`));";
            String economy_module_1 = "CREATE TABLE IF NOT EXISTS module_economy_settings (id int(11) NOT NULL AUTO_INCREMENT, setting varchar(255) NOT NULL, value varchar(255) NOT NULL, PRIMARY KEY (`id`));";
            String economy_module_2 = "CREATE TABLE IF NOT EXISTS module_economy_profiles ( id int(11) NOT NULL AUTO_INCREMENT, uuid varchar(255) NOT NULL, type varchar(255) NOT NULL, balance double DEFAULT NULL, PRIMARY KEY (`id`));";

            Statement action = connection.createStatement();
            action.executeUpdate(core);
            action.executeUpdate(ban_module_1);
            action.executeUpdate(ban_module_2);
            action.executeUpdate(warp_module_1);
            action.executeUpdate(teleport_module_1);
            action.executeUpdate(portal_module_1);
            action.executeUpdate(home_module_1);
            action.executeUpdate(economy_module_1);
            action.executeUpdate(economy_module_2);
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

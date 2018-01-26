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

package de.linzn.mineSuite.bungee.database.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BungeeQuery {

    public static UUID getUUID(String player) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        UUID uuid = null;

        try {
            Connection conn = manager.getConnection("MineSuiteBungee");
            PreparedStatement sql = conn.prepareStatement("SELECT UUID FROM core_uuidcache WHERE NAME = '" + player + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                uuid = UUID.fromString(result.getString(1));
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBungee", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    public static boolean updateProfile(UUID uuid, String player, long time) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteBungee");
            PreparedStatement sql = conn.prepareStatement("SELECT NAME FROM core_uuidcache WHERE UUID = '" + uuid + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {

                PreparedStatement sql2 = conn.prepareStatement("UPDATE core_uuidcache SET NAME = '" + player
                        + "', TIMESTAMP = '" + time + "' WHERE UUID = '" + uuid.toString() + "';");
                sql2.executeUpdate();
                sql2.close();
            } else {
                PreparedStatement sql2 = conn.prepareStatement("INSERT INTO core_uuidcache (UUID, NAME, TIMESTAMP) VALUES ('"
                        + uuid.toString() + "', '" + player + "', '" + time + "');");
                sql2.executeUpdate();
                sql2.close();
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBungee", conn);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

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

package de.linzn.mineSuite.bungee.module.teleport.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;
import de.linzn.mineSuite.bungee.utils.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeleportQuery {
    public static void setSpawn(String spawnType, Location location) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteTeleport");
            PreparedStatement sql = conn.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawnType
                    + "' AND server = '" + location.getServer() + "';");
            if (spawnType.equalsIgnoreCase("lobby")) {
                sql = conn.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawnType + "';");
            }
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                PreparedStatement update = conn
                        .prepareStatement("UPDATE spawns SET server = '" + location.getServer() + "', world = '" + location.getWorld() + "', x = '"
                                + location.getX() + "', y = '" + location.getY() + "', z = '" + location.getZ() + "', yaw = '" + location.getYaw() + "', pitch = '" + location.getPitch()
                                + "' WHERE spawntype = '" + spawnType + "' AND server = '" + location.getServer() + "';");
                if (spawnType.equalsIgnoreCase("lobby")) {
                    update = conn.prepareStatement("UPDATE spawns SET server = '" + location.getServer() + "', world = '" + location.getWorld() + "', x = '"
                            + location.getX() + "', y = '" + location.getY() + "', z = '" + location.getZ() + "', yaw = '" + location.getYaw() + "', pitch = '" + location.getPitch()
                            + "' WHERE spawntype = '" + spawnType + "';");
                }

                update.executeUpdate();
                update.close();
            } else {
                PreparedStatement insert = conn
                        .prepareStatement("INSERT INTO spawns (spawntype, server, world, x, y, z, yaw, pitch) VALUES ('"
                                + spawnType + "', '" + location.getServer() + "', '" + location.getWorld() + "', '" + location.getX() + "', '" + location.getY() + "', '" + location.getZ()
                                + "', '" + location.getYaw() + "', '" + location.getPitch() + "');");
                insert.executeUpdate();
                insert.close();
            }
            result.close();
            sql.close();

            manager.release("MineSuiteTeleport", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void unsetSpawn(String spawnType, String serverName, String worldName) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineSuiteTeleport");
            PreparedStatement remove;
            if (spawnType.equalsIgnoreCase("lobby")) {
                remove = conn.prepareStatement("DELETE FROM spawns WHERE spawntype = '" + spawnType + "';");
            } else {
                remove = conn.prepareStatement("DELETE FROM spawns WHERE spawntype = '" + spawnType + "' AND server = '" + serverName + "';");
            }
            remove.executeUpdate();
            remove.close();
            manager.release("MineSuiteTeleport", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSpawn(String spawnType, String serverName) {
        final List<String> rlist = new ArrayList<>();

        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteTeleport");
            if (spawnType.equalsIgnoreCase("lobby")) {
                PreparedStatement sql = conn
                        .prepareStatement("SELECT world, server, x, y, z, yaw, pitch FROM spawns WHERE spawntype = '"
                                + spawnType + "';");
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    rlist.add(0, "empty");
                    rlist.add(1, result.getString(1));
                    rlist.add(2, result.getString(2));
                    rlist.add(3, result.getString(3));
                    rlist.add(4, result.getString(4));
                    rlist.add(5, result.getString(5));
                    rlist.add(6, result.getString(6));
                    rlist.add(7, result.getString(7));

                }
                result.close();
                sql.close();
            } else {
                PreparedStatement sql = conn
                        .prepareStatement("SELECT world, server, x, y, z, yaw, pitch FROM spawns WHERE spawntype = '"
                                + spawnType + "' AND server = '" + serverName + "';");
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    rlist.add(0, "empty");
                    rlist.add(1, result.getString(1));
                    rlist.add(2, result.getString(2));
                    rlist.add(3, result.getString(3));
                    rlist.add(4, result.getString(4));
                    rlist.add(5, result.getString(5));
                    rlist.add(6, result.getString(6));
                    rlist.add(7, result.getString(7));

                }
                result.close();
                sql.close();
            }
            manager.release("MineSuiteTeleport", conn);

            return rlist;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isSpawn(String spawnType, String serverName, String worldName) {
        boolean isspawn = false;
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteTeleport");
            PreparedStatement sql;
            if (spawnType.equalsIgnoreCase("lobby")) {
                sql = conn.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawnType + "';");
            } else {
                sql = conn.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawnType + "' AND server = '" + serverName + "';");
            }
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                isspawn = true;
            } else {
                isspawn = false;
            }
            result.close();
            sql.close();
            manager.release("MineSuiteTeleport", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isspawn;
    }

}

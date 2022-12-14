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

package de.linzn.mineSuite.bungee.module.warp.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpQuery {


    public static void setWarp(UUID uuid, String warp, String server, String world, double x, double y, double z,
                               float yaw, float pitch, int visible) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteWarp");
            PreparedStatement sql = conn
                    .prepareStatement("SELECT warp_name FROM module_warp_warps WHERE warp_name = '" + warp + "';");
            ResultSet result = sql.executeQuery();
            if (!result.next()) {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO module_warp_warps (player, warp_name, server, world, x, y, z, yaw, pitch, visible) VALUES ('"
                                + uuid.toString() + "', '" + warp + "', '" + server + "', '" + world + "', '" + x
                                + "', '" + y + "', '" + z + "', '" + yaw + "', '" + pitch + "', '" + visible + "');");
                insert.executeUpdate();
                insert.close();
            }
            result.close();
            sql.close();
            manager.release("MineSuiteWarp", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void updateWarp(String warp, String server, String world, double x, double y, double z,
                                  float yaw, float pitch, int visible) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteWarp");
            PreparedStatement sql = conn
                    .prepareStatement("SELECT warp_name FROM module_warp_warps WHERE warp_name = '" + warp + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                PreparedStatement update = conn.prepareStatement("UPDATE module_warp_warps SET server = '" + server + "', world = '"
                        + world + "', x = '" + x + "', y = '" + y + "', z = '" + z + "', yaw = '" + yaw + "', pitch = '"
                        + pitch + "', visible = " + visible + " WHERE warp_name = '" + warp + "';");
                update.executeUpdate();
                update.close();
            }
            result.close();
            sql.close();
            manager.release("MineSuiteWarp", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void removeWarp(String warp) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteWarp");
            PreparedStatement sql = conn
                    .prepareStatement("SELECT warp_name FROM module_warp_warps WHERE warp_name = '" + warp + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                PreparedStatement update = conn.prepareStatement("DELETE FROM module_warp_warps WHERE warp_name = '" + warp + "';");
                update.executeUpdate();
                update.close();
            }
            result.close();
            sql.close();
            manager.release("MineSuiteWarp", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getWarp(String warp) {
        final List<String> rlist = new ArrayList<>();

        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteWarp");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT world, server, x, y, z, yaw, pitch FROM module_warp_warps WHERE warp_name = '" + warp + "';");
            final ResultSet result = sql.executeQuery();
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
            manager.release("MineSuiteWarp", conn);

            return rlist;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isWarp(String warp) {
        boolean isWarp = false;
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteWarp");
            PreparedStatement sql = conn
                    .prepareStatement("SELECT warp_name FROM module_warp_warps WHERE warp_name = '" + warp + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                isWarp = true;
            } else {
                isWarp = false;
            }
            result.close();
            sql.close();
            manager.release("MineSuiteWarp", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isWarp;
    }

    public static ArrayList<String[]> getWarps(int visible) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        ArrayList<String[]> list = new ArrayList<>();
        try {
            Connection conn = manager.getConnection("MineSuiteWarp");
            PreparedStatement sel;
            if (visible == 0) {
                sel = conn.prepareStatement("SELECT * FROM module_warp_warps;");
            } else {
                sel = conn.prepareStatement("SELECT * FROM module_warp_warps WHERE visible = '" + visible + "';");
            }
            ResultSet result = sel.executeQuery();
            if (result != null) {
                while (result.next()) {
                    String[] warp = new String[3];
                    warp[0] = result.getString("warp_name");
                    warp[1] = result.getString("player");
                    list.add(warp);
                }
                result.close();
            }
            sel.close();
            manager.release("MineSuiteWarp", conn);

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

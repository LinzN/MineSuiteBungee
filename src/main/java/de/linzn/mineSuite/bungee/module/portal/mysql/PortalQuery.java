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

package de.linzn.mineSuite.bungee.module.portal.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PortalQuery {
    public static boolean setPortal(Portal portal) {
        String portalName = portal.portalName;
        String serverName = portal.serverName;
        String portalType = portal.portalType;
        String portalDestination = portal.portalDestination;
        String material = portal.fillType;
        String worldName = portal.worldName;
        double maxX = portal.maxX;
        double maxY = portal.maxY;
        double maxZ = portal.maxZ;
        double minX = portal.minX;
        double minY = portal.minY;
        double minZ = portal.minZ;
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuitePortal");
            PreparedStatement sql = conn
                    .prepareStatement("SELECT portalname FROM module_portal_portals WHERE portalname = '" + portalName + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                PreparedStatement update = conn.prepareStatement(
                        "UPDATE module_portal_portals SET server = '" + serverName + "', type = '" + portalType + "', destination = '" + portalDestination
                                + "', world = '" + worldName + "', filltype = '" + material + "', xmax = '" + maxX
                                + "', xmin = '" + minX + "', ymax = '" + maxY + "', ymin = '" + minY + "', zmax = '"
                                + maxZ + "', zmin = '" + minZ + "' WHERE portalname = '" + portalName + "';");
                update.executeUpdate();
                update.close();
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO module_portal_portals (portalname, server, type, destination, world, filltype, xmax, xmin, ymax, ymin, zmax, zmin) VALUES('"
                                + portalName + "', '" + serverName + "', '" + portalType + "', '" + portalDestination + "', '" + worldName
                                + "', '" + material + "', '" + maxX + "', '" + minX + "', '" + maxY + "', '" + minY
                                + "', '" + maxZ + "', '" + minZ + "');");

                insert.executeUpdate();
                insert.close();
            }
            result.close();
            sql.close();
            manager.release("MineSuitePortal", conn);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean unsetPortal(String portalName) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuitePortal");
            PreparedStatement sql = conn
                    .prepareStatement("SELECT portalname FROM module_portal_portals WHERE portalname = '" + portalName + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                PreparedStatement update = conn
                        .prepareStatement("DELETE FROM module_portal_portals WHERE portalname = '" + portalName + "';");
                update.executeUpdate();
                update.close();
            }
            result.close();
            sql.close();
            manager.release("MineSuitePortal", conn);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static HashMap<String, Portal> getPortalsFromServer(String serverName) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        HashMap<String, Portal> portalList = new HashMap<>();
        try {

            Connection conn = manager.getConnection("MineSuitePortal");
            PreparedStatement sel = conn.prepareStatement("SELECT * FROM module_portal_portals WHERE server = '" + serverName + "';");

            ResultSet result = sel.executeQuery();
            if (result != null) {
                while (result.next()) {
                    String portalName = result.getString("portalname");
                    String portalType = result.getString("type");
                    String destination = result.getString("destination");

                    //Portal Datas
                    String server = result.getString("server");
                    String fillType = result.getString("filltype");
                    String worldName = result.getString("world");
                    int minX = result.getInt("xmin");
                    int minY = result.getInt("ymin");
                    int minZ = result.getInt("zmin");

                    int maxX = result.getInt("xmax");
                    int maxY = result.getInt("ymax");
                    int maxZ = result.getInt("zmax");

                    Portal portal = new Portal(portalName, server, portalType, destination, fillType, worldName, minX, minY, minZ, maxX, maxY, maxZ);
                    // Send portal files
                    portalList.put(portal.portalName, portal);
                }
                result.close();
            }
            sel.close();
            manager.release("MineSuitePortal", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portalList;
    }
}

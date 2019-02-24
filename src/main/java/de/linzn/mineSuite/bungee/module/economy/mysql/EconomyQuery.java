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

package de.linzn.mineSuite.bungee.module.economy.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;
import de.linzn.mineSuite.bungee.module.economy.EconomyManager;
import de.linzn.mineSuite.bungee.module.economy.EconomyType;
import de.linzn.openJL.pairs.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyQuery {
    public static boolean createProfile(UUID entityUUID, EconomyType economyType, double defaultValue) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement sql = conn.prepareStatement("SELECT uuid FROM module_economy_profiles WHERE uuid = '" + entityUUID.toString() + "' AND type = '" + economyType.name() + "';");
            ResultSet result = sql.executeQuery();
            boolean hasAlready = result.next();
            result.close();
            sql.close();
            if (!hasAlready) {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO module_economy_profiles (uuid, type, balance) VALUES ('" + entityUUID.toString() + "', '" + economyType.name() + "', '" + defaultValue + "');");
                insert.executeUpdate();
                insert.close();
                manager.release("mineSuiteEconomy", conn);
            }
            manager.release("mineSuiteEconomy", conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteProfile(UUID entityUUID, EconomyType economyType) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement update = conn.prepareStatement("DELETE FROM module_economy_profiles WHERE uuid = '" + entityUUID.toString() + "' AND type = '" + economyType.name() + "';");
            update.executeUpdate();
            update.close();
            manager.release("mineSuiteEconomy", conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateProfile(UUID entityUUID, double balance, EconomyType economyType) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement sql = conn.prepareStatement("SELECT uuid FROM module_economy_profiles WHERE uuid = '" + entityUUID.toString() + "' AND type = '" + economyType.name() + "';");
            ResultSet result = sql.executeQuery();
            boolean hasAlready = result.next();
            result.close();
            sql.close();
            if (hasAlready) {
                PreparedStatement update = conn.prepareStatement("UPDATE module_economy_profiles SET balance = '" + balance + "' WHERE uuid = '" + entityUUID.toString() + "' AND type = '" + economyType.name() + "';");
                update.executeUpdate();
                update.close();
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO module_economy_profiles (uuid, type, balance) VALUES ('" + entityUUID.toString() + "', '" + economyType.name() + "', '" + balance + "');");
                insert.executeUpdate();
                insert.close();
            }
            manager.release("mineSuiteEconomy", conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static double getProfileBalance(UUID entityUUID, EconomyType economyType) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        double value = -1.0;

        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement sql = conn.prepareStatement("SELECT balance FROM module_economy_profiles WHERE uuid = '" + entityUUID.toString() + "' AND type = '" + economyType.name() + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                value = result.getDouble("balance");
            }
            result.close();
            sql.close();
            manager.release("mineSuiteEconomy", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean hasProfile(UUID entityUUID, EconomyType economyType) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        boolean hasProfile = false;

        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement sql = conn.prepareStatement("SELECT uuid FROM module_economy_profiles WHERE uuid = '" + entityUUID.toString() + "' AND type = '" + economyType.name() + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                hasProfile = true;
            }
            result.close();
            sql.close();
            manager.release("mineSuiteEconomy", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasProfile;
    }

    public static List<Pair<UUID, Double>> getProfiles(EconomyType economyType, int amountValue, int offsetValue) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        List<Pair<UUID, Double>> profiles = new ArrayList<>();
        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement sql = conn.prepareStatement("SELECT balance, uuid FROM module_economy_profiles WHERE type = '" + economyType.name() + "' ORDER BY balance DESC LIMIT " + amountValue + " OFFSET " + offsetValue + ";");
            ResultSet result = sql.executeQuery();
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("uuid"));
                double value = result.getDouble("balance");
                profiles.add(new Pair<>(uuid, value));
            }
            result.close();
            sql.close();
            manager.release("mineSuiteEconomy", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }


    public static boolean loadSettings() {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineSuiteEconomy");
            PreparedStatement sql = conn.prepareStatement("SELECT setting, value FROM module_economy_settings;");
            ResultSet result = sql.executeQuery();
            while (result.next()) {
                String setting = result.getString("setting");
                String value = result.getString("value");
                EconomyManager.addSetting(setting, value);
            }
            result.close();
            sql.close();
            manager.release("mineSuiteEconomy", conn);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

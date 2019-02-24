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

package de.linzn.mineSuite.bungee.module.economy;

import de.linzn.mineSuite.bungee.database.CacheManager;
import de.linzn.mineSuite.bungee.module.economy.mysql.EconomyQuery;
import de.linzn.openJL.math.FloatingPoint;

import java.util.HashMap;
import java.util.UUID;

public class EconomyManager {
    private static HashMap<String, String> settings = new HashMap<>();


    public static boolean hasProfile(String playerName) {
        UUID playerUUID = CacheManager.getPlayerUUID(playerName);
        if (playerUUID == null) {
            return false;
        }
        return hasProfile(playerUUID, EconomyType.PLAYER);
    }

    public static boolean hasProfile(UUID entityUUID, EconomyType economyType) {
        return EconomyQuery.hasProfile(entityUUID, economyType);
    }

    public static double getBalance(String playerName) {
        UUID playerUUID = CacheManager.getPlayerUUID(playerName);
        if (playerUUID == null) {
            return 0.0D;
        }
        return getBalance(playerUUID, EconomyType.PLAYER);
    }

    public static double getBalance(UUID entityUUID, EconomyType economyType) {
        double balance = EconomyQuery.getProfileBalance(entityUUID, economyType);
        if (balance == -1.0) {
            balance = Double.parseDouble(EconomyManager.getSetting("currency.defaultValue"));
        }
        return balance;
    }

    public static String formatValue(double value) {
        if (value > 0.99D && value < 1.01) {
            return "" + round(value) + " " + EconomyManager.getSetting("currency.name.singular");
        }
        return "" + round(value) + " " + EconomyManager.getSetting("currency.name.plural");
    }

    public static void reloadSettings() {
        settings.clear();
        EconomyQuery.loadSettings();
    }

    public static String getSetting(String setting) {
        return settings.get(setting);
    }

    public static void addSetting(String setting, String value) {
        settings.put(setting, value);
    }


    private static double round(double value) {
        return FloatingPoint.round(value, 2);
    }
}

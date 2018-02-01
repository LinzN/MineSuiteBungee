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

package de.linzn.mineSuite.bungee.module.home;

import de.linzn.mineSuite.bungee.core.BungeeManager;
import de.linzn.mineSuite.bungee.module.home.mysql.HomeQuery;
import de.linzn.mineSuite.bungee.module.home.socket.JServerHomeOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class HomeManager {

    public static void sendPlayerToHome(UUID playerUUID, String homeName) {
        ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
        if (player == null) {
            ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " home task has been canceled.");
            return;
        }
        BungeeManager.sendMessageToTarget(player, MessageDB.default_TRY_TO_TELEPORT);
        if (HomeQuery.isHome(playerUUID, homeName)) {
            List<String> homeData = HomeQuery.getHome(playerUUID, homeName);
            String world = homeData.get(1);
            String server = homeData.get(2);
            double x = Double.parseDouble(homeData.get(3));
            double y = Double.parseDouble(homeData.get(4));
            double z = Double.parseDouble(homeData.get(5));
            float yaw = Float.parseFloat(homeData.get(6));
            float pitch = Float.parseFloat(homeData.get(7));
            Location location = new Location(server, world, x, y, z, yaw, pitch);
            JServerHomeOutput.teleportToHome(player, location);
            ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has been teleported with home system.");
            ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.home_NO_HOME);
        }

    }

    public static void createHome(String homeName, UUID homeOwner, Location location, int limit) {
        ProxiedPlayer player = BungeeManager.getPlayer(homeOwner);
        if (HomeQuery.isHome(homeOwner, homeName)) {
            HomeQuery.setHome(homeOwner, homeName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            ProxyServer.getInstance().getLogger().info("[MineSuite] Update Home N: " + homeName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            BungeeManager.sendMessageToTarget(player, MessageDB.home_REFRESH_HOME.replace("{home}", homeName));
        } else {
            if (HomeQuery.getHomes(homeOwner).size() >= limit) {
                BungeeManager.sendMessageToTarget(player, MessageDB.home_HOME_LIMIT);
            } else {
                HomeQuery.setHome(homeOwner, homeName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                ProxyServer.getInstance().getLogger().info("[MineSuite] Create Home N: " + homeName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
                BungeeManager.sendMessageToTarget(player, MessageDB.home_NEW_HOME.replace("{home}", homeName));
            }
        }

    }


    public static void removeHome(String homeName, UUID homeOwner) {
        ProxiedPlayer player = BungeeManager.getPlayer(homeOwner);
        if (HomeQuery.isHome(homeOwner, homeName)) {
            HomeQuery.delHome(homeOwner, homeName);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Remove Home N: " + homeName + " from " + player.getName());
            BungeeManager.sendMessageToTarget(player, MessageDB.home_DELETE_HOME.replace("{home}", homeName));
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.home_NO_HOME);
        }

    }

    public static void getHomeList(UUID playerUUID, int page) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        HashMap<String, String> list = HomeQuery.getHomes(playerUUID);
        List<String> homename = new ArrayList<>();
        List<String> servername = new ArrayList<>();


        for (Map.Entry<String, String> s : list.entrySet()) {
            homename.add(s.getKey());
            servername.add(s.getValue());
        }
        int counter = 1;
        int rgCount = list.size();
        if ((page * 6 + 1) > rgCount) {
            player.sendMessage("So viele Homeseiten besitzt du nicht!");
            return;
        }
        Collections.sort(homename);
        player.sendMessage("§aAuflistung all deiner Homes: ");
        player.sendMessage("§aServername?   §9Homename? ");
        List<String> homeList = homename.subList(page * 6,
                page * 6 + 6 > rgCount ? rgCount : page * 6 + 6);
        for (String s : homeList) {
            player.sendMessage("§a" + list.get(s) + ": §9" + s);
            counter++;
        }
        if (counter >= 7) {

            int pageSeite;
            if (page == 0) {
                pageSeite = 2;
            } else {
                pageSeite = (page + 2);
            }
            player.sendMessage("§aMehr auf Seite §e" + pageSeite + " §amit §e/homes " + pageSeite);
        }

        if (counter <= 6 && page != 0) {
            player.sendMessage("§aZurück auf Seite §e" + (page) + "§a mit §e/homes " + (page));
        }
    }

}

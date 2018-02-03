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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        ArrayList<String[]> list = HomeQuery.getHomes(playerUUID);

        int counter = 0;
        int homeCount = list.size();
        if ((page * 6 + 1) > homeCount) {
            player.sendMessage(MessageDB.home_HOME_PAGE_NO_HOMES);
            return;
        }
        player.sendMessage(MessageDB.home_HOME_PAGE_HOMES);
        List<String[]> homeList = list.subList(page * 6, page * 6 + 6 > homeCount ? homeCount : page * 6 + 6);
        for (String[] home : homeList) {
            player.sendMessage(MessageDB.home_HOME_PAGE_ENTRY.replace("{home}", home[0]).replace("{server}", home[1]));
            counter++;
        }
        if ((page * 6 + counter) < homeCount) {
            int newPage;
            if (page == 0) {
                newPage = 2;
            } else {
                newPage = (page + 2);
            }
            player.sendMessage(MessageDB.default_LIST_MOREPAGE.replace("{page}", "" + newPage).replace("{command}", "/homes"));
        }

        if (counter <= 6 && page != 0) {
            player.sendMessage(MessageDB.default_LIST_BACKPAGE.replace("{page}", "" + page).replace("{command}", "/homes"));
        }
    }

}

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

import de.linzn.mineSuite.bungee.managers.BungeeManager;
import de.linzn.mineSuite.bungee.module.home.mysql.HomeQuery;
import de.linzn.mineSuite.bungee.module.home.socket.JServerHomeOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class HomeManager {

    public static void sendPlayerToHome(UUID playerUUID, String homeName) {
        ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
        if (player == null) {
            ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " home task has been canceled.");
            return;
        }
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
            //todo Player msg
            player.sendMessage("Dieses Home gibt es leider nicht!");
        }

    }

    public static void createHome(String homeName, UUID homeOwner, Location location, int limit) {
        ProxiedPlayer player = BungeeManager.getPlayer(homeOwner);
        if (HomeQuery.isHome(homeOwner, homeName)) {
            HomeQuery.setHome(homeOwner, homeName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            ProxyServer.getInstance().getLogger().info("[MineSuite] Update Home N: " + homeName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            //todo Player msg
            player.sendMessage("Du hast dein Home " + homeName + " aktualisiert!");
        } else {
            if (HomeQuery.getHomes(homeOwner).size() >= limit) {
                //todo Player msg
                player.sendMessage("Du hast leider schon dein Homelimit erreicht!");
            } else {
                HomeQuery.setHome(homeOwner, homeName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                ProxyServer.getInstance().getLogger().info("[MineSuite] Create Home N: " + homeName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
                //todo Player msg
                player.sendMessage("Du hast dein Home " + homeName + " gesetzt!");
            }
        }

    }


    public static void removeHome(String homeName, UUID homeOwner) {
        ProxiedPlayer player = BungeeManager.getPlayer(homeOwner);

        if (HomeQuery.isHome(homeOwner, homeName)) {
            HomeQuery.delHome(homeOwner, homeName);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Remove Home N: " + homeName + " from " + player.getName());
            //todo Player msg
            player.sendMessage("Du hast das Home " + homeName + " entfernt!");
        } else {
            //todo Player msg
            player.sendMessage("Dieses Home gibt es leider nicht!");
        }

    }

}

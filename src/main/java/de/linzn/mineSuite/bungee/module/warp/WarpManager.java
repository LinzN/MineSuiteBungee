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

package de.linzn.mineSuite.bungee.module.warp;

import de.linzn.mineSuite.bungee.managers.BungeeManager;
import de.linzn.mineSuite.bungee.module.warp.mysql.WarpQuery;
import de.linzn.mineSuite.bungee.module.warp.socket.JServerWarpOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

public class WarpManager {

    public static void sendPlayerToWarp(UUID playerUUID, String warpName) {
        ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
        if (player == null) {
            ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " warp task has been canceled.");
            return;
        }

        if (WarpQuery.isWarp(warpName)) {
            List<String> warpData = WarpQuery.getWarp(warpName);
            String world = warpData.get(1);
            String server = warpData.get(2);
            double x = Double.parseDouble(warpData.get(3));
            double y = Double.parseDouble(warpData.get(4));
            double z = Double.parseDouble(warpData.get(5));
            float yaw = Float.parseFloat(warpData.get(6));
            float pitch = Float.parseFloat(warpData.get(7));
            Location location = new Location(server, world, x, y, z, yaw, pitch);
            JServerWarpOutput.teleportToWarp(player, location);
            ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has been teleported with warp system.");
            ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
        } else {
            //todo Player msg
            player.sendMessage("Diesen Warp gibt es leider nicht!");
        }

    }

    public static void createWarp(String warpName, UUID creator, Location location, int publicWarp) {
        ProxiedPlayer player = BungeeManager.getPlayer(creator);
        if (WarpQuery.isWarp(warpName)) {
            WarpQuery.updateWarp(warpName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), publicWarp);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Update Warp N: " + warpName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            //todo Player msg
            player.sendMessage("Du hast den Warp " + warpName + " aktualisiert!");
        } else {
            WarpQuery.setWarp(creator, warpName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), publicWarp);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Create Warp N: " + warpName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            //todo Player msg
            player.sendMessage("Du hast den Warp " + warpName + " gesetzt!");
        }
    }

    public static void removeWarp(String warpName, UUID remover) {
        ProxiedPlayer player = BungeeManager.getPlayer(remover);
        if (WarpQuery.isWarp(warpName)) {
            WarpQuery.removeWarp(warpName);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Remove Warp N: " + warpName + " by " + player.getName());
            //todo Player msg
            player.sendMessage("Du hast den Warp " + warpName + " entfernt!");
        } else {
            //todo Player msg
            player.sendMessage("Diesen Warp gibt es leider nicht!");
        }
    }
}
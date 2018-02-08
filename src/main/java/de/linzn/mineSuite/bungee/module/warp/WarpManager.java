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

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.database.mysql.BungeeQuery;
import de.linzn.mineSuite.bungee.module.core.BungeeManager;
import de.linzn.mineSuite.bungee.module.teleport.socket.JServerTeleportOutput;
import de.linzn.mineSuite.bungee.module.warp.mysql.WarpQuery;
import de.linzn.mineSuite.bungee.utils.Location;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpManager {

    public static void sendPlayerToWarp(UUID playerUUID, String warpName, boolean portal) {
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

            if (ProxyServer.getInstance().getServerInfo(server) == null) {
                MineSuiteBungeePlugin.getInstance().getLogger().severe("Server is invalid");
                player.sendMessage(MessageDB.teleport_SERVER_ERROR);
                return;
            }
            if (!BungeeManager.waitForReady(player.getServer().getInfo().getName(), playerUUID, server)) {
                ProxyServer.getInstance().getLogger().severe("[MineSuite] " + player.getName() + " teleport break?");
                return;
            }

            if (!portal) {
                BungeeManager.sendMessageToTarget(player, MessageDB.default_TRY_TO_TELEPORT);
            }
            //JServerWarpOutput.teleportToWarp(player, location);
            JServerTeleportOutput.teleportToLocation(player, location);
            ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has been teleported with warp system.");
            ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());

        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.warp_NO_WARP);
        }

    }

    public static void createWarp(String warpName, UUID creator, Location location, int publicWarp) {
        ProxiedPlayer player = BungeeManager.getPlayer(creator);
        if (WarpQuery.isWarp(warpName)) {
            WarpQuery.updateWarp(warpName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), publicWarp);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Update Warp N: " + warpName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            BungeeManager.sendMessageToTarget(player, MessageDB.warp_REFRESH_WARP.replace("{warp}", warpName));
        } else {
            WarpQuery.setWarp(creator, warpName, location.getServer(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), publicWarp);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Create Warp N: " + warpName + " S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            BungeeManager.sendMessageToTarget(player, MessageDB.warp_NEW_WARP.replace("{warp}", warpName));
        }
    }

    public static void removeWarp(String warpName, UUID remover) {
        ProxiedPlayer player = BungeeManager.getPlayer(remover);
        if (WarpQuery.isWarp(warpName)) {
            WarpQuery.removeWarp(warpName);
            ProxyServer.getInstance().getLogger().info("[MineSuite] Remove Warp N: " + warpName + " by " + player.getName());
            BungeeManager.sendMessageToTarget(player, MessageDB.warp_DELETE_WARP.replace("{warp}", warpName));
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.warp_NO_WARP);
        }
    }

    public static void getWarpList(UUID playerUUID, int page, int visible) {
        ArrayList<String[]> list = WarpQuery.getWarps(visible);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);

        int warpCount = list.size();
        if ((page * 6 + 1) > warpCount) {
            player.sendMessage(MessageDB.warp_WARP_PAGE_NO_WARPS);
            return;
        }

        player.sendMessage(MessageDB.warp_WARP_PAGE_WARPS);
        int counter = 1;
        List<String[]> warplist = list.subList(page * 6,
                page * 6 + 6 > warpCount ? warpCount : page * 6 + 6);
        for (String[] wl : warplist) {
            String warpName = wl[0];
            UUID ownerUUID = UUID.fromString(wl[1]);
            player.sendMessage(MessageDB.warp_WARP_PAGE_ENTRY.replace("{warp}", warpName).replace("{player}", BungeeQuery.getPlayerName(ownerUUID)));
            counter++;
        }

        if ((page * 6 + counter) < warpCount) {

            int newPage;
            if (page == 0) {
                newPage = 2;
            } else {
                newPage = (page + 2);
            }
            player.sendMessage(MessageDB.default_LIST_MOREPAGE.replace("{page}", "" + newPage).replace("{command}", "/warps"));
        }

        if (counter <= 6 && page != 0) {
            player.sendMessage(MessageDB.default_LIST_BACKPAGE.replace("{page}", "" + page).replace("{command}", "/warps"));
        }

    }
}

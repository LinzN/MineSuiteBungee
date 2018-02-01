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

import de.linzn.mineSuite.bungee.core.BungeeManager;
import de.linzn.mineSuite.bungee.database.mysql.BungeeQuery;
import de.linzn.mineSuite.bungee.module.warp.mysql.WarpQuery;
import de.linzn.mineSuite.bungee.module.warp.socket.JServerWarpOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class WarpManager {

    public static void sendPlayerToWarp(UUID playerUUID, String warpName, boolean portal) {
        ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
        if (player == null) {
            ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " warp task has been canceled.");
            return;
        }
        if (!portal) {
            BungeeManager.sendMessageToTarget(player, MessageDB.default_TRY_TO_TELEPORT);
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
        HashMap<String, UUID> list = WarpQuery.getWarps(visible);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        List<String> warpName = new ArrayList<>();

        for (Map.Entry<String, UUID> s : list.entrySet()) {
            warpName.add(s.getKey());
        }

        int rgCount = list.size();
        if ((page * 6 + 1) > rgCount) {
            player.sendMessage("So viele Seiten für Warps gibt es nicht!");
            return;
        }
        Collections.sort(warpName);
        player.sendMessage("§aDie Warpliste von MineGaming");
        player.sendMessage("§9Warpname?   §dBesitzer? ");
        int counter = 1;
        List<String> warplist = warpName.subList(page * 6,
                page * 6 + 6 > rgCount ? rgCount : page * 6 + 6);
        for (String wl : warplist) {
            player.sendMessage("§aWName: §9" + wl + " §agehört §d"
                    + BungeeQuery.getPlayerName(list.get(wl)));
            counter++;
        }

        if (counter >= 7) {

            int pageSeite;
            if (page == 0) {
                pageSeite = 2;
            } else {
                pageSeite = (page + 2);
            }
            player.sendMessage("§aMehr auf Seite §e" + pageSeite + " §amit §e/warps " + pageSeite);
        }

        if (counter <= 6 && page != 0) {
            player.sendMessage("§aZurück auf Seite §e" + (page) + "§a mit §e/warps " + (page));
        }

    }
}

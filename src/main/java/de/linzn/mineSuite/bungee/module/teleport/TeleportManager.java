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

package de.linzn.mineSuite.bungee.module.teleport;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.core.BungeeManager;
import de.linzn.mineSuite.bungee.module.teleport.mysql.TeleportQuery;
import de.linzn.mineSuite.bungee.module.teleport.socket.JServerTeleportOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class TeleportManager {
    private static HashMap<ProxiedPlayer, ProxiedPlayer> pendingTeleportsTPA = new HashMap<>();
    private static HashMap<ProxiedPlayer, ProxiedPlayer> pendingTeleportsTPAHere = new HashMap<>();
    private static int expireTime = 10;


    public static void teleportToSpawnType(UUID playerUUID, String spawnType, String serverName, String worldName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (player == null) {
            ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " teleport task has been canceled.");
            return;
        }
        BungeeManager.sendMessageToTarget(player, MessageDB.default_TRY_TO_TELEPORT);
        if (TeleportQuery.isSpawn(spawnType, serverName, worldName)) {
            List<String> spawnData = TeleportQuery.getSpawn(spawnType, serverName);
            String world = spawnData.get(1);
            String server = spawnData.get(2);
            double x = Double.parseDouble(spawnData.get(3));
            double y = Double.parseDouble(spawnData.get(4));
            double z = Double.parseDouble(spawnData.get(5));
            float yaw = Float.parseFloat(spawnData.get(6));
            float pitch = Float.parseFloat(spawnData.get(7));
            Location location = new Location(server, world, x, y, z, yaw, pitch);
            JServerTeleportOutput.teleportToLocation(player, location);
            ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has been teleported to spawnType with teleport system.");
            ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NOT_SET_SPAWNTYPE);
        }

    }

    public static void setSpawnType(UUID playerUUID, String spawnType, Location location) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (spawnType.equalsIgnoreCase("lobby") || spawnType.equalsIgnoreCase("serverspawn")) {
            if (TeleportQuery.isSpawn(spawnType, location.getServer(), location.getWorld())) {
                TeleportQuery.setSpawn(spawnType, location);
                BungeeManager.sendMessageToTarget(player, MessageDB.teleport_REFRESH_SPAWNTYPE.replace("{spawn}", spawnType));

                ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has update spawnType.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            } else {
                TeleportQuery.setSpawn(spawnType, location);
                BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NEW_SPAWNTYPE.replace("{spawn}", spawnType));
                ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has set spawnType.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            }
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NO_VALID_SPAWNTYPE);
        }
    }

    public static void unsetSpawnType(UUID playerUUID, String spawnType, String serverName, String worldName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (spawnType.equalsIgnoreCase("lobby") || spawnType.equalsIgnoreCase("serverspawn")) {
            if (TeleportQuery.isSpawn(spawnType, serverName, worldName)) {
                TeleportQuery.unsetSpawn(spawnType, serverName, worldName);
                BungeeManager.sendMessageToTarget(player, MessageDB.teleport_DELETE_SPAWNTYPE.replace("{spawn}", spawnType));
            } else {
                BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NOT_SET_SPAWNTYPE);
            }
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NO_VALID_SPAWNTYPE);
        }
    }


    public static void requestToTeleportToPlayer(String player, String target) {
        final ProxiedPlayer bp = BungeeManager.getPlayer(player);
        final ProxiedPlayer bt = BungeeManager.getPlayer(target);
        if (playerHasPendingTeleport(bp)) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_PLAYER_TELEPORT_PENDING);
            return;
        }
        if (bt == null) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.default_PLAYER_NOT_ONLINE.replace("{player}", bp.getName()));
            return;
        }
        if (bp.getName().equals(bt.getName())) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_TELEPORT_UNABLE.replace("{player}", bp.getName()));
            return;
        }
        if (playerHasPendingTeleport(bt)) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_PLAYER_TELEPORT_PENDING_OTHER);
            return;
        }

        pendingTeleportsTPA.put(bt, bp);
        BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_TELEPORT_REQUEST_SENT.replace("{player}", bt.getName()));
        BungeeManager.sendMessageToTarget(bt, MessageDB.teleport_PLAYER_REQUESTS_TO_TELEPORT_TO_YOU.replace("{player}", bp.getName()));
        ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.getInstance(), () -> {
            if (pendingTeleportsTPA.containsKey(bt)) {
                if (!pendingTeleportsTPA.get(bt).equals(bp)) {
                    return;
                }
                BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_TPA_REQUEST_TIMED_OUT.replace("{player}", bt.getName()));
                pendingTeleportsTPA.remove(bt);
                BungeeManager.sendMessageToTarget(bt, MessageDB.teleport_TP_REQUEST_OTHER_TIMED_OUT.replace("{player}", bp.getName()));
            }
        }, expireTime, TimeUnit.SECONDS);
    }

    public static void requestPlayerTeleportToYou(String player, String target) {
        final ProxiedPlayer bp = BungeeManager.getPlayer(player);
        final ProxiedPlayer bt = BungeeManager.getPlayer(target);
        if (playerHasPendingTeleport(bp)) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_PLAYER_TELEPORT_PENDING);
            return;
        }
        if (bt == null) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.default_PLAYER_NOT_ONLINE.replace("{player}", bp.getName()));
            return;
        }

        if (bp.getName().equals(bt.getName())) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_TELEPORT_UNABLE.replace("{player}", bp.getName()));
            return;
        }

        if (playerHasPendingTeleport(bt)) {
            BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_PLAYER_TELEPORT_PENDING_OTHER.replace("{player}", bt.getName()));
            return;
        }
        pendingTeleportsTPAHere.put(bt, bp);
        BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_TELEPORT_REQUEST_SENT.replace("{player}", bp.getName()));
        BungeeManager.sendMessageToTarget(bt, MessageDB.teleport_PLAYER_REQUESTS_YOU_TELEPORT_TO_THEM.replace("{player}", bp.getName()));

        ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.getInstance(), () -> {
            if (pendingTeleportsTPAHere.containsKey(bt)) {
                if (!pendingTeleportsTPAHere.get(bt).equals(bp)) {
                    return;
                }
                BungeeManager.sendMessageToTarget(bp, MessageDB.teleport_TPAHERE_REQUEST_TIMED_OUT.replace("{player}", bt.getName()));
                pendingTeleportsTPAHere.remove(bt);
                BungeeManager.sendMessageToTarget(bt, MessageDB.teleport_TP_REQUEST_OTHER_TIMED_OUT.replace("{player}", bp.getName()));
            }
        }, expireTime, TimeUnit.SECONDS);
    }

    public static void acceptTeleportRequest(ProxiedPlayer player) {
        if (pendingTeleportsTPA.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPA.get(player);
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_TELEPORT_ACCEPTED.replace("{player}", target.getName()));
            BungeeManager.sendMessageToTarget(target, MessageDB.teleport_TELEPORT_REQUEST_ACCEPTED.replace("{player}", player.getName()));
            JServerTeleportOutput.teleportAccept(target, player);
            pendingTeleportsTPA.remove(player);
        } else if (pendingTeleportsTPAHere.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPAHere.get(player);
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_TELEPORT_ACCEPTED.replace("{player}", target.getName()));
            BungeeManager.sendMessageToTarget(target, MessageDB.teleport_TELEPORT_REQUEST_ACCEPTED.replace("{player}", player.getName()));
            JServerTeleportOutput.teleportAccept(player, target);
            pendingTeleportsTPAHere.remove(player);
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NO_TELEPORTS);
        }
    }

    public static void denyTeleportRequest(ProxiedPlayer player) {
        if (pendingTeleportsTPA.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPA.get(player);
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_TELEPORT_DENIED.replace("{player}", target.getName()));
            BungeeManager.sendMessageToTarget(target, MessageDB.teleport_TELEPORT_REQUEST_DENIED.replace("{player}", player.getName()));
            pendingTeleportsTPA.remove(player);
        } else if (pendingTeleportsTPAHere.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPAHere.get(player);
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_TELEPORT_DENIED.replace("{player}", target.getName()));
            BungeeManager.sendMessageToTarget(target, MessageDB.teleport_TELEPORT_REQUEST_DENIED.replace("{player}", player.getName()));
            pendingTeleportsTPAHere.remove(player);
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NO_TELEPORTS);
        }
    }

    public static boolean playerHasPendingTeleport(ProxiedPlayer player) {
        return pendingTeleportsTPA.containsKey(player) || pendingTeleportsTPAHere.containsKey(player);
    }

    public static void setPlayersDeathBackLocation(ProxiedPlayer player, Location loc) {
        BungeeManager.setDeathBackLocation(loc, player);
    }

    public static void sendPlayerToLastBack(ProxiedPlayer player) {
        BungeeManager.sendMessageToTarget(player, MessageDB.default_TRY_TO_TELEPORT);
        if (BungeeManager.hasDeathBackLocation(player)) {
            JServerTeleportOutput.teleportToLocation(player, BungeeManager.getLastBackLocation(player));
            ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to deathpoint!");
            BungeeManager.removeDeathBackLocation(player);
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.teleport_NO_BACK_TP);
        }

    }

    public static void tpAll(String sender, String target) {
        ProxiedPlayer p = BungeeManager.getPlayer(sender);
        ProxiedPlayer t = BungeeManager.getPlayer(target);

        if (t == null) {
            BungeeManager.sendMessageToTarget(p, MessageDB.default_PLAYER_NOT_ONLINE);
            return;
        }

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (!player.equals(p)) {
                JServerTeleportOutput.teleportToPlayer(p, t);
            }

            BungeeManager.sendMessageToTarget(player,
                    MessageDB.teleport_ALL_PLAYERS_TELEPORTED.replace("{player}", t.getName()));
        }
    }

    public static void teleportPlayerToPlayerUUID(UUID playerUUID, UUID targetUUID, boolean silent, boolean bypass) {
        ProxiedPlayer p = BungeeManager.getPlayer(playerUUID);
        ProxiedPlayer t = BungeeManager.getPlayer(targetUUID);

        BungeeManager.sendMessageToTarget(p, MessageDB.default_TRY_TO_TELEPORT);
        if (p == null || t == null) {
            BungeeManager.sendMessageToTarget(p, MessageDB.default_PLAYER_NOT_ONLINE);
            return;
        }
        if (p.getName().equals(t.getName())) {
            BungeeManager.sendMessageToTarget(p, MessageDB.teleport_TELEPORT_UNABLE.replace("{player}", p.getName()));
            return;
        }
        JServerTeleportOutput.teleportToPlayer(p, t);
        if (!silent) {
            BungeeManager.sendMessageToTarget(t, MessageDB.teleport_PLAYER_TELEPORTED_TO_YOU.replace("{player}", p.getName()));
        }

        BungeeManager.sendMessageToTarget(p, MessageDB.teleport_TELEPORTED_TO_PLAYER.replace("{player}", t.getName()));
    }

    public static void teleportPlayerToPlayer(String player, String target, boolean silent, boolean bypass) {
        ProxiedPlayer p = BungeeManager.getPlayer(player);
        ProxiedPlayer t = BungeeManager.getPlayer(target);

        if (p == null || t == null) {
            BungeeManager.sendMessageToTarget(p, MessageDB.default_PLAYER_NOT_ONLINE);
            return;
        }
        if (p.getName().equals(t.getName())) {
            BungeeManager.sendMessageToTarget(p, MessageDB.teleport_TELEPORT_UNABLE.replace("{player}", p.getName()));
            return;
        }
        teleportPlayerToPlayerUUID(p.getUniqueId(), t.getUniqueId(), silent, bypass);

    }

}

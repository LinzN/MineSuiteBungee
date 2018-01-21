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
import de.linzn.mineSuite.bungee.managers.BungeeManager;
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
    public static HashMap<ProxiedPlayer, ProxiedPlayer> pendingTeleportsTPA = new HashMap<>();
    public static HashMap<ProxiedPlayer, ProxiedPlayer> pendingTeleportsTPAHere = new HashMap<>();
    private static int expireTime = 10;


    public static void teleportToSpawnType(UUID playerUUID, String spawnType, String serverName, String worldName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
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
            //todo
            player.sendMessage("Dieser Spawn ist leider nicht gesetzt.");
        }

    }

    public static void setSpawnType(UUID playerUUID, String spawnType, Location location) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (spawnType.equalsIgnoreCase("lobby") || spawnType.equalsIgnoreCase("serverspawn")) {
            if (TeleportQuery.isSpawn(spawnType, location.getServer(), location.getWorld())) {
                TeleportQuery.setSpawn(spawnType, location);
                //todo
                player.sendMessage("Der Spawn wurde aktualisiert");
                ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has update spawnType.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            } else {
                TeleportQuery.setSpawn(spawnType, location);
                //todo
                player.sendMessage("Der Spawn wurde gesetzt.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] " + player.getName() + " has set spawnType.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
            }
        } else {
            //todo
            player.sendMessage("Kein gültiger Spawntype");
        }
    }

    public static void unsetSpawnType(UUID playerUUID, String spawnType, String serverName, String worldName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (spawnType.equalsIgnoreCase("lobby") || spawnType.equalsIgnoreCase("serverspawn")) {
            if (TeleportQuery.isSpawn(spawnType, serverName, worldName)) {
                TeleportQuery.unsetSpawn(spawnType, serverName, worldName);
                //todo
                player.sendMessage("Der Spawn wurde entfernt.");
            } else {
                //todo
                player.sendMessage("Dieser Spawn ist leider nicht gesetzt.");
            }
        } else {
            //todo
            player.sendMessage("Kein gültiger Spawntype");
        }
    }




    public static void requestToTeleportToPlayer(String player, String target) {
        final ProxiedPlayer bp = BungeeManager.getPlayer(player);
        final ProxiedPlayer bt = BungeeManager.getPlayer(target);
        if (playerHasPendingTeleport(bp)) {
            bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING);
            return;
        }
        if (bt == null) {
            bp.sendMessage(MessageDB.PLAYER_NOT_ONLINE.replace("{player}", bp.getName()));
            return;
        }
        if (bp.getName().equals(bt.getName())) {
            bp.sendMessage(MessageDB.TELEPORT_UNABLE.replace("{player}", bp.getName()));
            return;
        }
        if (playerHasPendingTeleport(bt)) {
            bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING_OTHER);
            return;
        }

        pendingTeleportsTPA.put(bt, bp);
        bp.sendMessage(MessageDB.TELEPORT_REQUEST_SENT.replace("{player}", bt.getName()));
        bt.sendMessage(MessageDB.PLAYER_REQUESTS_TO_TELEPORT_TO_YOU.replace("{player}", bp.getName()));
        ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.getInstance(), () -> {
            if (pendingTeleportsTPA.containsKey(bt)) {
                if (!pendingTeleportsTPA.get(bt).equals(bp)) {
                    return;
                }
                if (bp != null) {
                    bp.sendMessage(MessageDB.TPA_REQUEST_TIMED_OUT.replace("{player}", bt.getName()));
                }
                pendingTeleportsTPA.remove(bt);
                if (bt != null) {
                    bt.sendMessage(MessageDB.TP_REQUEST_OTHER_TIMED_OUT.replace("{player}", bp.getName()));
                }
            }
        }, expireTime, TimeUnit.SECONDS);
    }

    public static void requestPlayerTeleportToYou(String player, String target) {
        final ProxiedPlayer bp = BungeeManager.getPlayer(player);
        final ProxiedPlayer bt = BungeeManager.getPlayer(target);
        if (playerHasPendingTeleport(bp)) {
            bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING);
            return;
        }
        if (bt == null) {
            bp.sendMessage(MessageDB.PLAYER_NOT_ONLINE.replace("{player}", bp.getName()));
            return;
        }
        if (playerHasPendingTeleport(bt)) {
            bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING_OTHER.replace("{player}", bt.getName()));
            return;
        }
        pendingTeleportsTPAHere.put(bt, bp);
        bp.sendMessage(MessageDB.TELEPORT_REQUEST_SENT.replace("{player}", bp.getName()));
        bt.sendMessage(MessageDB.PLAYER_REQUESTS_YOU_TELEPORT_TO_THEM.replace("{player}", bp.getName()));

        ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.getInstance(), () -> {
            if (pendingTeleportsTPAHere.containsKey(bt)) {
                if (!pendingTeleportsTPAHere.get(bt).equals(bp)) {
                    return;
                }
                if (bp != null) {
                    bp.sendMessage(MessageDB.TPAHERE_REQUEST_TIMED_OUT.replace("{player}", bt.getName()));
                }
                pendingTeleportsTPAHere.remove(bt);
                if (bt != null) {
                    bt.sendMessage(MessageDB.TP_REQUEST_OTHER_TIMED_OUT.replace("{player}", bp.getName()));
                }
            }
        }, expireTime, TimeUnit.SECONDS);
    }

    public static void acceptTeleportRequest(ProxiedPlayer player) {
        if (pendingTeleportsTPA.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPA.get(player);
            player.sendMessage(MessageDB.TELEPORT_ACCEPTED.replace("{player}", target.getName()));
            target.sendMessage(MessageDB.TELEPORT_REQUEST_ACCEPTED.replace("{player}", player.getName()));
            JServerTeleportOutput.teleportAccept(target, player);
            pendingTeleportsTPA.remove(player);
        } else if (pendingTeleportsTPAHere.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPAHere.get(player);
            player.sendMessage(MessageDB.TELEPORT_ACCEPTED.replace("{player}", target.getName()));
            target.sendMessage(MessageDB.TELEPORT_REQUEST_ACCEPTED.replace("{player}", player.getName()));
            JServerTeleportOutput.teleportAccept(player, target);
            pendingTeleportsTPAHere.remove(player);
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.NO_TELEPORTS);
        }
    }

    public static void denyTeleportRequest(ProxiedPlayer player) {
        if (pendingTeleportsTPA.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPA.get(player);
            BungeeManager.sendMessageToTarget(player, MessageDB.TELEPORT_DENIED.replace("{player}", target.getName()));
            target.sendMessage(MessageDB.TELEPORT_REQUEST_DENIED.replace("{player}", player.getName()));
            pendingTeleportsTPA.remove(player);
        } else if (pendingTeleportsTPAHere.containsKey(player)) {
            ProxiedPlayer target = pendingTeleportsTPAHere.get(player);
            BungeeManager.sendMessageToTarget(player, MessageDB.TELEPORT_DENIED.replace("{player}", target.getName()));
            target.sendMessage(MessageDB.TELEPORT_REQUEST_DENIED.replace("{player}", player.getName()));
            pendingTeleportsTPAHere.remove(player);
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.NO_TELEPORTS);
        }
    }

    public static boolean playerHasPendingTeleport(ProxiedPlayer player) {
        return pendingTeleportsTPA.containsKey(player) || pendingTeleportsTPAHere.containsKey(player);
    }

    public static void setPlayersDeathBackLocation(ProxiedPlayer player, Location loc) {
        BungeeManager.setDeathBackLocation(loc, player);
    }

    public static void sendPlayerToLastBack(ProxiedPlayer player) {
        if (BungeeManager.hasDeathBackLocation(player)) {
            JServerTeleportOutput.teleportToLocation(player, BungeeManager.getLastBackLocation(player));
            ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to deathpoint!");
            BungeeManager.removeDeathBackLocation(player);
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.NO_BACK_TP);
        }

    }

    public static void tpAll(String sender, String target) {
        ProxiedPlayer p = BungeeManager.getPlayer(sender);
        ProxiedPlayer t = BungeeManager.getPlayer(target);

        if (t == null) {
            p.sendMessage(MessageDB.PLAYER_NOT_ONLINE);
            return;
        }

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (!player.equals(p)) {
                JServerTeleportOutput.teleportToPlayer(p, t);
            }

            BungeeManager.sendMessageToTarget(player,
                    MessageDB.ALL_PLAYERS_TELEPORTED.replace("{player}", t.getName()));
        }
    }

    public static void teleportPlayerToPlayer(String player, String target, boolean silent, boolean bypass) {
        ProxiedPlayer p = BungeeManager.getPlayer(player);
        ProxiedPlayer t = BungeeManager.getPlayer(target);
        if (p == null || t == null) {
            p.sendMessage(MessageDB.PLAYER_NOT_ONLINE);
            return;
        }

        JServerTeleportOutput.teleportToPlayer(p, t);

        if (!silent) {
            t.sendMessage(MessageDB.PLAYER_TELEPORTED_TO_YOU.replace("{player}", p.getName()));
        }

        p.sendMessage(MessageDB.TELEPORTED_TO_PLAYER.replace("{player}", t.getName()));
    }
}

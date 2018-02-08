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

package de.linzn.mineSuite.bungee.core;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.listeners.JServerBungeeOutput;
import de.linzn.mineSuite.bungee.module.ban.BanManager;
import de.linzn.mineSuite.bungee.module.ban.mysql.BanQuery;
import de.linzn.mineSuite.bungee.utils.Location;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BungeeManager {

    public static void sendMessageToTarget(ProxiedPlayer target, String message) {
        if (target == null) {

            return;
        }
        for (String line : (message).split("\n")) {
            target.sendMessage(TextComponent.fromLegacyText(line));
        }
    }

    public static ProxiedPlayer getPlayer(String player) {
        return ProxyServer.getInstance().getPlayer(player);
    }

    public static ProxiedPlayer getPlayer(UUID uuid) {
        return ProxyServer.getInstance().getPlayer(uuid);
    }

    public static void initPlayer(final ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), () -> {
            if (BanManager.isMuted(player.getUniqueId())) {
                List<String> list = BanQuery.getMuteInfos(player.getUniqueId());
                String reason = list.get(0);
                String mutedby = list.get(1);
                long time = Long.parseLong(list.get(2));
                DataHashTable.muteReason.put(player.getUniqueId(), reason);
                DataHashTable.mutedBy.put(player.getUniqueId(), mutedby);
                DataHashTable.muteTime.put(player.getUniqueId(), time);

            }
            DataHashTable.channel.put(player.getUniqueId(), "GLOBAL");
            DataHashTable.session.put(player.getUniqueId(), true);

        });
    }

    public static void deinitPlayer(final ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().runAsync(MineSuiteBungeePlugin.getInstance(), () -> {
            DataHashTable.isMuted.remove(player.getUniqueId());
            DataHashTable.muteTime.remove(player.getUniqueId());
            DataHashTable.muteReason.remove(player.getUniqueId());
            DataHashTable.mutedBy.remove(player.getUniqueId());
            DataHashTable.guildInvites.remove(player.getUniqueId());
            DataHashTable.msgreply.remove(player.getUniqueId());
            DataHashTable.channel.remove(player.getUniqueId());
            DataHashTable.isafk.remove(player.getUniqueId());
            DataHashTable.socialspy.remove(player.getUniqueId());
            DataHashTable.session.remove(player.getUniqueId());
            DataHashTable.readyToTeleport.remove(player.getUniqueId());
            removeDeathBackLocation(player);
        });
    }

    public static void setDeathBackLocation(Location loc, ProxiedPlayer player) {
        if (DataHashTable.deathBackLocation.containsKey(player.getUniqueId())) {
            DataHashTable.deathBackLocation.remove(player.getUniqueId());
            DataHashTable.lastBack.remove(player.getUniqueId());
        }
        DataHashTable.deathBackLocation.put(player.getUniqueId(), loc);
        DataHashTable.lastBack.put(player.getUniqueId(), true);
    }

    public static void removeDeathBackLocation(ProxiedPlayer player) {
        if (DataHashTable.deathBackLocation.containsKey(player.getUniqueId())) {
            DataHashTable.deathBackLocation.remove(player.getUniqueId());
            DataHashTable.lastBack.remove(player.getUniqueId());
        }
    }

    public static boolean hasDeathBackLocation(ProxiedPlayer player) {
        return DataHashTable.deathBackLocation.containsKey(player.getUniqueId());
    }

    public static Location getLastBackLocation(ProxiedPlayer player) {
        return DataHashTable.deathBackLocation.get(player.getUniqueId());
    }

    public static Location getDeathBackLocation(ProxiedPlayer player) {
        return DataHashTable.deathBackLocation.get(player.getUniqueId());
    }

    public static boolean waitForReady(String server, UUID playerUUID, String locationServer) {
        MineSuiteBungeePlugin.getInstance().getLogger().info("Request for confirm task");
        DataHashTable.readyToTeleport.put(playerUUID, new AtomicBoolean(false));
        JServerBungeeOutput.requestTeleportConfirm(server, playerUUID, locationServer);
        int counter = 0;
        while (!DataHashTable.readyToTeleport.get(playerUUID).get()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            if (counter >= 100) { /* 5000 ms cancel task */
                DataHashTable.readyToTeleport.remove(playerUUID);
                JServerBungeeOutput.cancelTeleport(server, playerUUID, locationServer);
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(playerUUID);
                if (p != null) {
                    p.sendMessage(MessageDB.teleport_CONFIRM_ERROR);
                }
                MineSuiteBungeePlugin.getInstance().getLogger().info("Confirm error time");
                return false;
            }
            counter++;
        }
        MineSuiteBungeePlugin.getInstance().getLogger().info("Confirm success");
        DataHashTable.readyToTeleport.remove(playerUUID);
        return true;
    }

}

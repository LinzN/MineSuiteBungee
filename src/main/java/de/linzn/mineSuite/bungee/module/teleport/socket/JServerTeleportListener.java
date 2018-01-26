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

package de.linzn.mineSuite.bungee.module.teleport.socket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.core.BungeeManager;
import de.linzn.mineSuite.bungee.module.teleport.TeleportManager;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerTeleportListener implements IncomingDataListener {

    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            subChannel = in.readUTF();

            if (subChannel.equals("client_teleport-set-spawntype")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                String spawnType = in.readUTF();
                String serverName = in.readUTF();
                String worldName = in.readUTF();
                double x = in.readDouble();
                double y = in.readDouble();
                double z = in.readDouble();
                float yaw = in.readFloat();
                float pitch = in.readFloat();
                Location location = new Location(serverName, worldName, x, y, z, yaw, pitch);
                TeleportManager.setSpawnType(playerUUID, spawnType, location);
                return;
            }

            if (subChannel.equals("client_teleport-unset-spawntype")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                String spawnType = in.readUTF();
                String serverName = in.readUTF();
                String worldName = in.readUTF();
                TeleportManager.unsetSpawnType(playerUUID, spawnType, serverName, worldName);
                return;
            }

            if (subChannel.equals("client_teleport-teleport-to-spawntype")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                String spawnType = in.readUTF();
                String serverName = in.readUTF();
                String worldName = in.readUTF();
                TeleportManager.teleportToSpawnType(playerUUID, spawnType, serverName, worldName);
                return;
            }


            if (subChannel.equals("client_teleport_teleport-location")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
                if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " teleport task has been canceled.");
                    return;
                }
                Location location = new Location(in.readUTF(), in.readUTF(), in.readDouble(),
                        in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());
                JServerTeleportOutput.teleportToLocation(player, location);
                ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " has been teleported with teleport system.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
                return;
            }

            if (subChannel.equals("client_teleport_set-dead-location")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
                if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " task has been canceled.");
                    return;
                }
                Location location = new Location(in.readUTF(), in.readUTF(), in.readDouble(),
                        in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());

                TeleportManager.setPlayersDeathBackLocation(BungeeManager.getPlayer(player.getName()), location);
                return;
            }

            if (subChannel.equals("client_teleport_teleport-to-player")) {
                TeleportManager.teleportPlayerToPlayer(in.readUTF(), in.readUTF(), in.readBoolean(), in.readBoolean());

                return;
            }
            if (subChannel.equals("client_teleport_teleport-to-player-uuid")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                UUID targetUUID = UUID.fromString(in.readUTF());
                TeleportManager.teleportPlayerToPlayerUUID(playerUUID, targetUUID, in.readBoolean(), in.readBoolean());

                return;
            }

            if (subChannel.equals("client_teleport_tpa-request-here")) {
                TeleportManager.requestPlayerTeleportToYou(in.readUTF(), in.readUTF());
                return;
            }

            if (subChannel.equals("client_teleport_tpa-to-request")) {
                TeleportManager.requestToTeleportToPlayer(in.readUTF(), in.readUTF());
                return;
            }
            if (subChannel.equals("client_teleport_tpa-accept")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
                if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " tpa task has been canceled.");
                    return;
                }
                TeleportManager.acceptTeleportRequest(player);
                return;
            }
            if (subChannel.equals("client_teleport_tpa-deny")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
                if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " tpa task has been canceled.");
                    return;
                }
                TeleportManager.denyTeleportRequest(player);
                return;
            }

            if (subChannel.equals("client_teleport_teleport-all")) {
                TeleportManager.tpAll(in.readUTF(), in.readUTF());
                return;
            }

            if (subChannel.equals("client_teleport_teleport-player-back")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                ProxiedPlayer player = BungeeManager.getPlayer(playerUUID);
                if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " teleport task has been canceled.");
                    return;
                }
                TeleportManager.sendPlayerToLastBack(player);
                return;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

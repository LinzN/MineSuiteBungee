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

package de.linzn.mineSuite.bungee.module.portal;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.core.BungeeManager;
import de.linzn.mineSuite.bungee.module.portal.mysql.Portal;
import de.linzn.mineSuite.bungee.module.portal.mysql.PortalQuery;
import de.linzn.mineSuite.bungee.module.portal.socket.JServerPortalOutput;
import de.linzn.mineSuite.bungee.module.teleport.socket.JServerTeleportOutput;
import de.linzn.mineSuite.bungee.module.warp.WarpManager;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class PortalManager {

    private static HashMap<String, Portal> portalList = new HashMap<>();

    public static void playerUsePortal(String portalName, UUID playerUUID) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (player == null) {

            return;
        }
        Portal portal = portalList.get(portalName);
        if (portal == null) {

            return;
        }
        if (portal.portalType.equalsIgnoreCase("warp")) {
            WarpManager.sendPlayerToWarp(playerUUID, portal.portalDestination, true);
        } else if (portal.portalType.equalsIgnoreCase("server")) {
            serverPortal(playerUUID, portal.portalDestination);
        }
    }

    public static void setPortal(UUID playerUUID, Portal portal) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (!portal.portalType.equalsIgnoreCase("warp") && !portal.portalType.equalsIgnoreCase("server")) {
            BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_NO_TYPE);
            return;
        }
        if (getPortal(portal.portalName) != null) {
            String oldServer = getPortal(portal.portalName).serverName;
            portalList.remove(portal.portalName);
            if (PortalQuery.setPortal(portal)) {
                portalList.put(portal.portalName, portal);
                JServerPortalOutput.disablePortal(oldServer, portal.portalName);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                JServerPortalOutput.enablePortal(portal.serverName, portal);
                ProxyServer.getInstance().getLogger().info("[MineSuite] Update portal " + portal.portalName);
                BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_REFRESH);
            } else {
                BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_ERROR);
            }
        } else {
            if (PortalQuery.setPortal(portal)) {
                portalList.put(portal.portalName, portal);
                JServerPortalOutput.enablePortal(portal.serverName, portal);
                ProxyServer.getInstance().getLogger().info("[MineSuite] Register new portal " + portal.portalName);
                BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_CREATED);
            } else {
                BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_ERROR);
            }
        }
    }

    public static void unsetPortal(UUID playerUUID, String portalName, String serverName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (getPortal(portalName) != null) {
            if (PortalQuery.unsetPortal(portalName)) {
                portalList.remove(portalName);
                JServerPortalOutput.disablePortal(serverName, portalName);
                ProxyServer.getInstance().getLogger().info("[MineSuite] Unregister portal " + portalName);
                BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_DELETED);
            } else {
                BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_ERROR);
            }
        } else {
            BungeeManager.sendMessageToTarget(player, MessageDB.portal_PORTAL_NO_PORTAL);
        }
    }


    public static Portal getPortal(String portalName) {
        return portalList.get(portalName);
    }

    public static void processingPortalRequest(String serverName) {
        ProxyServer.getInstance().getLogger().info("[MineSuite] Request for portals from server " + serverName);
        //todo Player msg
        for (Portal portal : portalList.values()) {
            if (portal.serverName.equalsIgnoreCase(serverName)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ProxyServer.getInstance().getLogger().info("[MineSuite] Sending portal " + portal.portalName + " to server " + serverName);
                JServerPortalOutput.enablePortal(portal.serverName, portal);
            }
        }

    }


    public static void loadPortalsToHash() {
        ProxyServer.getInstance().getLogger().info("[MineSuite] Loading portals ");
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            HashMap<String, Portal> serverPortals = PortalQuery.getPortalsFromServer(server.getName());
            for (Portal portal : serverPortals.values()) {
                portalList.put(portal.portalName, portal);
                ProxyServer.getInstance().getLogger().info("[MineSuite] Found portal " + portal.portalName);
            }
        }
    }

    private static void serverPortal(UUID playerUUID, String server) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        ServerInfo serverNew = ProxyServer.getInstance().getServerInfo(server);

        if (ProxyServer.getInstance().getServerInfo(server) == null) {
            MineSuiteBungeePlugin.getInstance().getLogger().severe("Server is invalid");
            player.sendMessage(MessageDB.teleport_SERVER_ERROR);
            return;
        }
        if (!BungeeManager.waitForReady(player.getServer().getInfo().getName(), playerUUID, server)) {
            ProxyServer.getInstance().getLogger().severe("[MineSuite] " + player.getName() + " teleport break?");
            return;
        }
        BungeeManager.sendMessageToTarget(player, MessageDB.default_TRY_TO_TELEPORT);
        JServerTeleportOutput.teleportToServer(player, server);


    }
}

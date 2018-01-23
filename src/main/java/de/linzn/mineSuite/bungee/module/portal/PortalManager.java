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

import de.linzn.mineSuite.bungee.module.portal.mysql.Portal;
import de.linzn.mineSuite.bungee.module.portal.mysql.PortalQuery;
import de.linzn.mineSuite.bungee.module.portal.socket.JServerPortalOutput;
import de.linzn.mineSuite.bungee.module.teleport.TeleportManager;
import de.linzn.mineSuite.bungee.module.warp.WarpManager;
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
            WarpManager.sendPlayerToWarp(playerUUID, portal.portalDestination);
        } else if (portal.portalType.equalsIgnoreCase("server")) {
            TeleportManager.teleportToServer(playerUUID, portal.portalDestination);
        }

    }

    public static void setPortal(UUID playerUUID, Portal portal) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (getPortal(portal.portalName) != null) {
            portalList.remove(portal.portalName);
        }
        if (PortalQuery.setPortal(portal)) {
            portalList.put(portal.portalName, portal);
            JServerPortalOutput.enablePortal(portal.serverName, portal);
            //todo Player msg
            ProxyServer.getInstance().getLogger().info("[MineSuite] Register new portal " + portal.portalName);
            player.sendMessage("Das Portal wurde erstellt");
        } else {
            //todo Player msg
            player.sendMessage("Es ist ein Fehler beim Erstellen des Portals aufgetreten.");
        }
    }

    public static void unsetPortal(UUID playerUUID, String portalName, String serverName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (PortalQuery.unsetPortal(portalName)) {
            portalList.remove(portalName);
            JServerPortalOutput.disablePortal(serverName, portalName);
            //todo Player msg
            ProxyServer.getInstance().getLogger().info("[MineSuite] Unregister portal " + portalName);
            player.sendMessage("Das Portal wurde entfernt");
        } else {
            //todo Player msg
            player.sendMessage("Es ist ein Fehler beim Entfernen des Portals aufgetreten.");
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
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ProxyServer.getInstance().getLogger().info("[MineSuite] Sending portal " + portal.portalName + " to server " + serverName);
                JServerPortalOutput.enablePortal(portal.serverName, portal);
            }
        }

    }


    public static void loadPortalsToHash() {
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            HashMap<String, Portal> serverPortals = PortalQuery.getPortalsFromServer(server.getName());
            for (Portal portal : serverPortals.values()) {
                portalList.put(portal.portalName, portal);
            }
        }
    }
}

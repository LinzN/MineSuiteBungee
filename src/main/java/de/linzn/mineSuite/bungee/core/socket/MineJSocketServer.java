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

package de.linzn.mineSuite.bungee.core.socket;

import de.linzn.jSocket.server.JServer;
import de.linzn.jSocket.server.JServerConnection;
import de.linzn.mineSuite.bungee.core.Config;
import de.linzn.mineSuite.bungee.listeners.JServerBroadcastListener;
import de.linzn.mineSuite.bungee.listeners.JServerBungeeListener;
import de.linzn.mineSuite.bungee.module.ban.socket.JServerBanListener;
import de.linzn.mineSuite.bungee.module.chat.socket.JServerChatListener;
import de.linzn.mineSuite.bungee.module.guild.JServerGuildListener;
import de.linzn.mineSuite.bungee.module.home.socket.JServerHomeListener;
import de.linzn.mineSuite.bungee.module.portal.socket.JServerPortalListener;
import de.linzn.mineSuite.bungee.module.teleport.socket.JServerTeleportListener;
import de.linzn.mineSuite.bungee.module.warp.socket.JServerWarpListener;

public class MineJSocketServer {

    public JServer jServer;

    public MineJSocketServer() {
        String hostname = Config.ConfigConfiguration.getString("jSocket.host");
        int port = Config.ConfigConfiguration.getInt("jSocket.port");
        jServer = new JServer(hostname, port);
        setupListener();
    }

    private void setupListener() {
        jServer.registerIncomingDataListener("mineSuiteBungee", new JServerBungeeListener());
        jServer.registerIncomingDataListener("mineSuiteBroadcast", new JServerBroadcastListener());
        jServer.registerIncomingDataListener("mineSuiteBan", new JServerBanListener());
        jServer.registerIncomingDataListener("mineSuiteChat", new JServerChatListener());
        jServer.registerIncomingDataListener("mineSuiteGuild", new JServerGuildListener());
        jServer.registerIncomingDataListener("mineSuiteHome", new JServerHomeListener());
        jServer.registerIncomingDataListener("mineSuitePortal", new JServerPortalListener());
        jServer.registerIncomingDataListener("mineSuiteTeleport", new JServerTeleportListener());
        jServer.registerIncomingDataListener("mineSuiteWarp", new JServerWarpListener());
    }

    public void openServer() {
        this.jServer.openServer();
    }

    public void closeServer() {
        this.jServer.closeServer();
    }

    public void broadcastClients(String header, byte[] bytes) {
        for (JServerConnection jServerConnection : this.jServer.getClients().values()) {
            jServerConnection.writeOutput(header, bytes);
        }
    }
}

package de.linzn.mineSuite.bungee.socket;

import de.linzn.jSocket.server.JServer;
import de.linzn.jSocket.server.JServerConnection;
import de.linzn.mineSuite.bungee.socket.listener.*;
import de.linzn.mineSuite.bungee.utils.Config;

public class MineJSocketServer {

    public JServer jServer;

    public MineJSocketServer() {
        String hostname = Config.ConfigConfiguration.getString("jSocket.host");
        int port = Config.ConfigConfiguration.getInt("jSocket.port");
        jServer = new JServer(hostname, port);
        setupListener();
    }

    private void setupListener() {
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

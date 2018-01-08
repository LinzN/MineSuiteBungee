package de.linzn.mineSuite.bungee.socket;

import de.linzn.jSocket.server.JServer;
import de.linzn.jSocket.server.JServerConnection;
import de.linzn.mineSuite.bungee.listeners.xeonSocket.*;
import de.linzn.mineSuite.bungee.utils.Config;

public class MineJSocketServer {

    public JServer jServer;

    public MineJSocketServer() {
        String hostname = Config.ConfigConfiguration.getString("jSocket.host");
        int port = Config.ConfigConfiguration.getInt("jSocket.port");
        jServer = new JServer(hostname, port);
    }

    private void setupListener() {
        jServer.registerIncomingDataListener("mineSuiteBan", new XeonBan());
        jServer.registerIncomingDataListener("mineSuiteChat", new XeonChat());
        jServer.registerIncomingDataListener("mineSuiteGuild", new XeonGuild());
        jServer.registerIncomingDataListener("mineSuiteHome", new XeonHome());
        jServer.registerIncomingDataListener("mineSuitePortal", new XeonPortal());
        jServer.registerIncomingDataListener("mineSuiteTeleport", new XeonTeleport());
        jServer.registerIncomingDataListener("mineSuiteWarp", new XeonWarp());
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

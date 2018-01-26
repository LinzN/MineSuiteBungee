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

package de.linzn.jSocket.server;

import de.linzn.jSocket.core.ChannelDataEventPacket;
import de.linzn.jSocket.core.ConnectionListener;
import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.jSocket.core.TaskRunnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class JServer implements Runnable {
    public ServerSocket server;
    ArrayList<ChannelDataEventPacket> dataInputListener;
    ArrayList<ConnectionListener> connectionListeners;
    HashMap<UUID, JServerConnection> jServerConnections;
    private String host;
    private int port;

    public JServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.jServerConnections = new HashMap<>();
        this.dataInputListener = new ArrayList<>();
        this.connectionListeners = new ArrayList<>();
        System.out.println("[" + Thread.currentThread().getName() + "] " + "Create JServer");
    }

    public void openServer() {
        try {
            this.server = new ServerSocket();
            this.server.bind(new InetSocketAddress(this.host, this.port));
            new TaskRunnable().runSingleThreadExecutor(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            this.server.close();
            ArrayList<UUID> uuidList = new ArrayList<>(this.jServerConnections.keySet());
            for (UUID uuid : uuidList) {
                this.jServerConnections.get(uuid).setDisable();
            }
            this.jServerConnections.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName("jServer");
        do {
            try {
                Socket socket = this.server.accept();
                socket.setTcpNoDelay(true);
                JServerConnection jServerConnection = new JServerConnection(socket, this);
                jServerConnection.setEnable();
                this.jServerConnections.put(jServerConnection.getUUID(), jServerConnection);
            } catch (IOException e) {
                System.out.println("[" + Thread.currentThread().getName() + "] " + "Connection already closed!");
            }
        } while (!this.server.isClosed());
    }

    public void registerIncomingDataListener(String channel, IncomingDataListener dataInputListener) {
        this.dataInputListener.add(new ChannelDataEventPacket(channel, dataInputListener));
    }

    public void registerConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.add(connectionListener);
    }

    public JServerConnection getClient(UUID uuid) {
        return this.jServerConnections.get(uuid);
    }

    public HashMap<UUID, JServerConnection> getClients() {
        return this.jServerConnections;
    }
}

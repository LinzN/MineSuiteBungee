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

package de.linzn.mineSuite.bungee.socket.listener;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.socket.output.JServerWarpOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerWarpListener implements IncomingDataListener {

    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            subChannel = in.readUTF();
            if (subChannel.equals("client_warp_teleport-warp")) {
                ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
                if (player == null) {
                    ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " warp task has been canceled.");
                    return;
                }
                Location location = new Location(in.readUTF(), in.readUTF(), in.readDouble(),
                        in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());
                JServerWarpOutput.teleportToWarp(player, location);
                ProxyServer.getInstance().getLogger().info("[MineSuite]" + player.getName() + " has been teleported with warp system.");
                ProxyServer.getInstance().getLogger().info("[MineSuite] S: " + location.getServer() + " W:" + location.getWorld() + " X:" + location.getX() + " Y:" + location.getY() + " Z:" + location.getZ());
                return;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

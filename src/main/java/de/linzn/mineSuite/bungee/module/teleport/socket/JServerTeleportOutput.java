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

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.module.core.socket.JServerBungeeOutput;
import de.linzn.mineSuite.bungee.utils.Location;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JServerTeleportOutput {

    public static void teleportToLocation(ProxiedPlayer player, Location loc) {
        ServerInfo servernew = ProxyServer.getInstance().getServerInfo(loc.getServer());

        Callback<Boolean> callBack = (aBoolean, throwable) -> {
            if (!aBoolean) {
                player.sendMessage(MessageDB.teleport_SERVER_ERROR);
                JServerBungeeOutput.cancelTeleport(player.getServer().getInfo().getName(), player.getUniqueId(), loc.getServer());
            }
        };

        if (player.getServer() == null || !player.getServer().getInfo().toString().equals(servernew.toString())) {
            player.connect(servernew, callBack);
        }


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(servernew.getName());
            dataOutputStream.writeUTF("server_teleport_teleport-location");
            dataOutputStream.writeUTF(player.getUniqueId().toString());
            dataOutputStream.writeUTF(loc.getWorld());
            dataOutputStream.writeDouble(loc.getX());
            dataOutputStream.writeDouble(loc.getY());
            dataOutputStream.writeDouble(loc.getZ());
            dataOutputStream.writeFloat(loc.getYaw());
            dataOutputStream.writeFloat(loc.getPitch());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void teleportToServer(ProxiedPlayer player, String server) {
        ServerInfo serverNew = ProxyServer.getInstance().getServerInfo(server);

        Callback<Boolean> callBack = (aBoolean, throwable) -> {
            if (!aBoolean) {
                player.sendMessage(MessageDB.teleport_SERVER_ERROR);
                JServerBungeeOutput.cancelTeleport(player.getServer().getInfo().getName(), player.getUniqueId(), server);
            }
        };

        if (player.getServer() == null || !player.getServer().getInfo().toString().equals(serverNew.toString())) {
            player.connect(serverNew, callBack);
        } else {
            //todo testmsg
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Leider Fehlgeschlagen!");
        }
    }

    public static void teleportToPlayer(ProxiedPlayer player, ProxiedPlayer target) {
        Callback<Boolean> callBack = (aBoolean, throwable) -> {
            if (!aBoolean) {
                player.sendMessage(MessageDB.teleport_SERVER_ERROR);
                JServerBungeeOutput.cancelTeleport(player.getServer().getInfo().getName(), player.getUniqueId(), player.getServer().getInfo().getName());
            }
        };
        if (player.getServer().getInfo() != target.getServer().getInfo()) {
            player.connect(target.getServer().getInfo(), callBack);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(target.getServer().getInfo().getName());
            dataOutputStream.writeUTF("server_teleport_teleport-to-player");
            dataOutputStream.writeUTF(player.getUniqueId().toString());
            dataOutputStream.writeUTF(target.getUniqueId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void teleportAccept(ProxiedPlayer player, ProxiedPlayer target) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(player.getServer().getInfo().getName());
            dataOutputStream.writeUTF("server_teleport_tpa-accept");
            dataOutputStream.writeUTF(player.getUniqueId().toString());
            dataOutputStream.writeUTF(target.getUniqueId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

}

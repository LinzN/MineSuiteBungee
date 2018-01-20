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

package de.linzn.mineSuite.bungee.module.ban.socket;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.ProxyServer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class JServerBanOutput {

    public static void permBanMSG(String banned, String reason, String bannedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + banned + " §6wurde Permanent von §a" + bannedby
                + " §6vom Server gesperrt.";
        String sendreason = "§6Grund: §a" + reason;
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        ProxyServer.getInstance().getConsole().sendMessage(sendreason);
        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF(sendreason);

        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }

    public static void tempBanMSG(String banned, String time, String reason, String bannedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + banned + " §6wurde für §a" + time + " §6von §a" + bannedby
                + " §6vom Server gesperrt.";
        String sendreason = "§6Grund: §a" + reason;
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        ProxyServer.getInstance().getConsole().sendMessage(sendreason);

        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF(sendreason);

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }

    public static void permMuteMSG(String muted, String reason, String mutedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + muted + " §6wurde §aPermanent §6von §a" + mutedby
                + " §6vom Chat ausgeschlossen.";
        String sendreason = "§6Grund: §a" + reason;
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        ProxyServer.getInstance().getConsole().sendMessage(sendreason);

        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF(sendreason);

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }

    public static void tempMuteMSG(String muted, String time, String reason, String mutedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + muted + " §6wurde für §a" + time + " §6von §a" + mutedby
                + " §6vom Chat ausgeschlossen.";
        String sendreason = "§6Grund: §a" + reason;
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        ProxyServer.getInstance().getConsole().sendMessage(sendreason);

        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF(sendreason);

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }

    public static void kickMSG(String kicked, String reason, String kickedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + kicked + " §6wurde von §a" + kickedby
                + " §6vom Server geschmissen. \nGrund: §a" + reason;
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF("none");

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }

    public static void unMute(String unmuted, String reason, String unmutedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + unmuted + " §6wurde von §a" + unmutedby + " §6 zum Chat hinzugefügt.";
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF("none");

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }

    public static void unBan(String unbanned, String reason, String unbannedby) {
        ProxyServer.getInstance().getServers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        String sendmessage = "§6Spieler §a" + unbanned + " §6wurde von §a" + unbannedby + " §6vom Server entsperrt.";
        ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
        try {
            dataOutputStream.writeUTF("server_ban_notify");
            dataOutputStream.writeUTF(sendmessage);
            dataOutputStream.writeUTF("none");

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBan", byteArrayOutputStream.toByteArray());

    }
}

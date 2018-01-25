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

package de.linzn.mineSuite.bungee.module.chat;

import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.managers.BungeeManager;
import de.linzn.mineSuite.bungee.module.chat.socket.JServerChatOutput;
import de.linzn.mineSuite.bungee.utils.ChatFormate;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class ChatManager {

    private static void globalChat(String sender, String text, String prefix, String suffix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);

        if (player == null) {
            return;
        }
        String formattedText = ChatFormate.genGlobalChat(sender, text, prefix, suffix);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            BungeeManager.sendMessageToTarget(p, formattedText);
        }
        ProxyServer.getInstance().getLogger().info(formattedText);

    }

    private static void tradeChat(String sender, String text, String prefix, String suffix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);

        if (player == null) {
            return;
        }
        String formattedText = ChatFormate.genTradeChat(sender, text, prefix, suffix);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            BungeeManager.sendMessageToTarget(p, formattedText);
        }
        ProxyServer.getInstance().getLogger().info(formattedText);

    }

    private static void broadcastChat(String text) {
        String formattedText = ChatFormate.genBroadcastChat(text);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            BungeeManager.sendMessageToTarget(p, formattedText);
        }
        ProxyServer.getInstance().getLogger().info(formattedText);
    }

    public static void privateMsgChat(String sender, String receiver, String text, String prefix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        ProxiedPlayer receivedPlayer = ProxyServer.getInstance().getPlayer(receiver);

        if (player == null) {
            return;
        }

        if (receivedPlayer == null) {
            BungeeManager.sendMessageToTarget(player, MessageDB.default_PLAYER_NOT_ONLINE);
            return;
        }
        if (DataHashTable.isafk.containsKey(receivedPlayer.getUniqueId())) {
            BungeeManager.sendMessageToTarget(player, MessageDB.chat_AFK_MARKED);
        }
        DataHashTable.msgreply.put(receivedPlayer.getUniqueId(), player.getUniqueId());

        String formattedTextSender = ChatFormate.genPrivateSender(sender, receiver, text, prefix);
        String formattedTextReceiver = ChatFormate.genPrivateReceiver(sender, receiver, text, prefix);
        String formattedText = ChatFormate.genPrivate(sender, receiver, text);

        BungeeManager.sendMessageToTarget(player, formattedTextSender);
        BungeeManager.sendMessageToTarget(receivedPlayer, formattedTextReceiver);

        ProxyServer.getInstance().getLogger().info("[PM]" + formattedText);

        for (UUID uuid : DataHashTable.socialspy.keySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
            BungeeManager.sendMessageToTarget(p, "§4[SP]§r" + formattedText);
        }

    }

    public static void privateReplyChat(String sender, String text, String prefix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        UUID uuidReceiver = DataHashTable.msgreply.get(player.getUniqueId());
        if (uuidReceiver == null) {
            BungeeManager.sendMessageToTarget(player, MessageDB.chat_NO_REPLY);
            return;
        }
        ProxiedPlayer receivedPlayer = ProxyServer.getInstance().getPlayer(uuidReceiver);
        if (receivedPlayer == null) {
            BungeeManager.sendMessageToTarget(player, MessageDB.default_PLAYER_NOT_ONLINE);
            return;
        }
        if (DataHashTable.isafk.containsKey(receivedPlayer.getUniqueId())) {
            BungeeManager.sendMessageToTarget(player, MessageDB.chat_AFK_MARKED);
        }
        DataHashTable.msgreply.put(receivedPlayer.getUniqueId(), player.getUniqueId());

        String formattedTextSender = ChatFormate.genPrivateSender(sender, receivedPlayer.getName(), text,
                prefix);
        String formattedTextReceiver = ChatFormate.genPrivateReceiver(sender, receivedPlayer.getName(), text,
                prefix);
        String formattedText = ChatFormate.genPrivate(sender, receivedPlayer.getName(), text);

        BungeeManager.sendMessageToTarget(player, formattedTextSender);
        BungeeManager.sendMessageToTarget(receivedPlayer, formattedTextReceiver);

        ProxyServer.getInstance().getLogger().info("[Reply]" + formattedText);
        for (UUID uuid : DataHashTable.socialspy.keySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
            BungeeManager.sendMessageToTarget(p, "§4[SP]§r" + formattedText);
        }
    }

    @SuppressWarnings("deprecation")
    public static void channelSend(String sender, String text, String prefix, String suffix, String channel) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        if (player == null) {
            return;
        }
        if (channel.equalsIgnoreCase("GLOBAL")) {
            globalChat(sender, text, prefix, suffix);
        } else if (channel.equalsIgnoreCase("STAFF")) {
            staffChat(sender, text, prefix);

        } else if (channel.equalsIgnoreCase("TRADE")) {
            tradeChat(sender, text, prefix, suffix);

        } else if (channel.equalsIgnoreCase("BROADCAST")) {
            broadcastChat(text);

        } else if (channel.equalsIgnoreCase("GUILD")) {
            //sendGuildChat(guild, sender, text);

        } else if (channel.equalsIgnoreCase("NONE")) {
            String ch = DataHashTable.channel.get(player.getUniqueId());
            if (ch == null) {
                ProxyServer.getInstance().getLogger().info("Channel for player " + sender + " == null ????");
                return;
            }
            if (ch.equalsIgnoreCase("GLOBAL")) {
                globalChat(sender, text, prefix, suffix);
            } else if (ch.equalsIgnoreCase("STAFF")) {
                staffChat(sender, text, prefix);

            } else if (ch.equalsIgnoreCase("TRADE")) {
                tradeChat(sender, text, prefix, suffix);

            } else if (ch.equalsIgnoreCase("GUILD")) {
                /*
                if (guild.equalsIgnoreCase("NONE")) {
                    player.sendMessage("Du bist in keiner Gilde!");
                    DataHashTable.channel.put(player.getUniqueId(), "GLOBAL");
                } else {
                    sendGuildChat(guild, sender, text);
                }
                */
            }
        } else {
            globalChat(player.getName(), text, prefix, suffix);
        }

    }

    private static void staffChat(String sender, String text, String prefix) {
        String formattedText = ChatFormate.genStaffChat(sender, text, prefix);
        JServerChatOutput.staffChat(formattedText);
    }

    public static void sendGuildChat(String guild, String sender, String text) {
        String formattedText = ChatFormate.genGuildChat(sender, text);
        ProxyServer.getInstance().getLogger().info(guild + "-> " + formattedText);
        for (UUID uuid : DataHashTable.socialspy.keySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
            BungeeManager.sendMessageToTarget(p, "§4[SP]§r" + guild + "-> " + formattedText);
        }
        JServerChatOutput.sendGuildChat(guild, formattedText);
    }

}

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
import de.linzn.mineSuite.bungee.module.chat.socket.JServerChatOutput;
import de.linzn.mineSuite.bungee.utils.ChatFormate;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class ChatManager {

    public static void globalChat(String sender, String text, String prefix, String suffix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);

        if (player == null) {
            return;
        }
        String formatedText = ChatFormate.genGlobalChat(sender, text, prefix, suffix);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(formatedText);
        }
        ProxyServer.getInstance().getLogger().info(formatedText);

    }

    public static void tradeChat(String sender, String text, String prefix, String suffix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);

        if (player == null) {
            return;
        }
        String formatedText = ChatFormate.genTradeChat(sender, text, prefix, suffix);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(formatedText);
        }
        ProxyServer.getInstance().getLogger().info(formatedText);

    }

    public static void broadcastChat(String text) {

        String formatedText = ChatFormate.genBroadcastChat(text);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(formatedText);
        }
        ProxyServer.getInstance().getLogger().info(formatedText);

    }

    public static void privateMsgChat(String sender, String reciever, String text, String prefix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        ProxiedPlayer recievedPlayer = ProxyServer.getInstance().getPlayer(reciever);

        if (player == null) {
            return;
        }

        if (recievedPlayer == null) {
            player.sendMessage("Dieser Spieler ist nicht online!");
            return;
        }
        if (DataHashTable.isafk.containsKey(recievedPlayer.getUniqueId())) {
            player.sendMessage("§eDer Spieler ist als abwesend makiert!");
        }
        DataHashTable.msgreply.put(recievedPlayer.getUniqueId(), player.getUniqueId());

        String formatedTextSender = ChatFormate.genPrivateSender(sender, reciever, text, prefix);
        String formatedTextReciever = ChatFormate.genPrivateReceiver(sender, reciever, text, prefix);
        String formatedText = ChatFormate.genPrivate(sender, reciever, text);

        player.sendMessage(formatedTextSender);
        recievedPlayer.sendMessage(formatedTextReciever);

        ProxyServer.getInstance().getLogger().info("[PM]" + formatedText);

        for (UUID uuid : DataHashTable.socialspy.keySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
            p.sendMessage("§4[SP]§r" + formatedText);
        }

    }

    public static void privateReplyChat(String sender, String text, String prefix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        UUID uuidreciever = DataHashTable.msgreply.get(player.getUniqueId());
        if (uuidreciever == null) {
            player.sendMessage("Du hast niemand zum Antworten!");
            return;
        }
        ProxiedPlayer recievedPlayer = ProxyServer.getInstance().getPlayer(uuidreciever);
        if (recievedPlayer == null) {
            player.sendMessage("Dieser Spieler ist offline!");
            return;
        }
        if (DataHashTable.isafk.containsKey(recievedPlayer.getUniqueId())) {
            player.sendMessage("§eDer Spieler ist als abwesend makiert!");
        }
        DataHashTable.msgreply.put(recievedPlayer.getUniqueId(), player.getUniqueId());

        String formatedTextSender = ChatFormate.genPrivateSender(sender, recievedPlayer.getName(), text,
                prefix);
        String formatedTextReciever = ChatFormate.genPrivateReceiver(sender, recievedPlayer.getName(), text,
                prefix);
        String formatedText = ChatFormate.genPrivate(sender, recievedPlayer.getName(), text);

        player.sendMessage(formatedTextSender);
        recievedPlayer.sendMessage(formatedTextReciever);

        ProxyServer.getInstance().getLogger().info("[Reply]" + formatedText);
        for (UUID uuid : DataHashTable.socialspy.keySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
            p.sendMessage("§4[SP]§r" + formatedText);
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
            globalChat(player.getDisplayName(), text, prefix, suffix);
        }

    }

    public static void staffChat(String sender, String text, String prefix) {
        String formattedText = ChatFormate.genStaffChat(sender, text, prefix);
        JServerChatOutput.staffChat(formattedText);
    }

    public static void sendGuildChat(String guild, String sender, String text) {
        String formattedText = ChatFormate.genGuildChat(sender, text);
        ProxyServer.getInstance().getLogger().info(guild + "-> " + formattedText);
        for (UUID uuid : DataHashTable.socialspy.keySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
            p.sendMessage("§4[SP]§r" + guild + "-> " + formattedText);
        }
        JServerChatOutput.sendGuildChat(guild, formattedText);
    }

}

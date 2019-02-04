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

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.module.chat.socket.JServerChatOutput;
import de.linzn.mineSuite.bungee.module.core.BungeeManager;
import de.linzn.mineSuite.bungee.utils.ChatFormate;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class ChatManager {

    private static HashMap<String, IChatChannel> chatChannels = new HashMap();


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


    public static void channelSend(String sender, String text, String prefix, String suffix, String channel) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        if (player == null) {
            return;
        }
        if (channel.equalsIgnoreCase("GLOBAL")) {
            if (chatChannels.containsKey("GLOBAL")) {
                IChatChannel globalChannel = getChat("GLOBAL");
                globalChannel.sendChat(sender, text, prefix, suffix);
            }
        } else if (channel.equalsIgnoreCase("STAFF")) {
            if (chatChannels.containsKey("STAFF")) {
                IChatChannel staffChannel = getChat("STAFF");
                staffChannel.sendChat(sender, text, prefix, null);
            }
        } else if (channel.equalsIgnoreCase("TRADE")) {
            if (chatChannels.containsKey("TRADE")) {
                IChatChannel tradeChannel = getChat("TRADE");
                tradeChannel.sendChat(sender, text, prefix, suffix);
            }

        } else if (channel.equalsIgnoreCase("BROADCAST")) {
            if (chatChannels.containsKey("BROADCAST")) {
                IChatChannel broadcastChannel = getChat("BROADCAST");
                broadcastChannel.sendChat(null, text, null, null);
            }


        } else if (channel.equalsIgnoreCase("GUILD")) {
            if (chatChannels.containsKey("GUILD")) {
                IChatChannel guildChannel = getChat("GUILD");
                guildChannel.sendChat(sender, text, prefix, suffix);
            }

        } else if (channel.equalsIgnoreCase("NONE")) {
            String ch = DataHashTable.channel.get(player.getUniqueId());
            if (ch == null) {
                ProxyServer.getInstance().getLogger().info("Channel for player " + sender + " == null ????");
                return;
            }
            if (ch.equalsIgnoreCase("GLOBAL")) {
                if (chatChannels.containsKey("GLOBAL")) {
                    IChatChannel globalChannel = getChat("GLOBAL");
                    globalChannel.sendChat(sender, text, prefix, suffix);
                }
            } else if (ch.equalsIgnoreCase("STAFF")) {
                if (chatChannels.containsKey("STAFF")) {
                    IChatChannel staffChannel = getChat("STAFF");
                    staffChannel.sendChat(sender, text, prefix, null);
                }
            } else if (ch.equalsIgnoreCase("TRADE")) {
                if (chatChannels.containsKey("TRADE")) {
                    IChatChannel tradeChannel = getChat("TRADE");
                    tradeChannel.sendChat(sender, text, prefix, suffix);
                }
            } else if (ch.equalsIgnoreCase("GUILD")) {
                if (chatChannels.containsKey("GUILD")) {
                    IChatChannel guildChannel = getChat("GUILD");
                    guildChannel.sendChat(sender, text, prefix, suffix);
                }
            }
        } else {
            if (chatChannels.containsKey("GLOBAL")) {
                IChatChannel globalChannel = getChat("GLOBAL");
                globalChannel.sendChat(sender, text, prefix, suffix);
            }
        }
    }

    public static void sendTitle(String title, String subTitel, int time) {
        JServerChatOutput.titleBroadcast(title, subTitel, time);
    }

    public static void registerChat(IChatChannel iChatChannel) {
        MineSuiteBungeePlugin.getInstance().getLogger().info("Register new Chat: " + iChatChannel.getChannelName());
        chatChannels.put(iChatChannel.getChannelName(), iChatChannel);
    }

    public static IChatChannel getChat(String channelName) {
        return chatChannels.get(channelName);
    }

    public static void unregisterChat(String channelName) {
        MineSuiteBungeePlugin.getInstance().getLogger().info("Unregister new Chat: " + channelName);
        chatChannels.remove(channelName);
    }

}

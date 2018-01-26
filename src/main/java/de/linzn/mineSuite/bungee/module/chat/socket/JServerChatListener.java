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

package de.linzn.mineSuite.bungee.module.chat.socket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.module.chat.ChatManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerChatListener implements IncomingDataListener {


    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        // TODO Auto-generated method stub
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            subChannel = in.readUTF();
            if (subChannel.equals("client_chat_default-chat")) {
                String sender = in.readUTF();
                String text = in.readUTF();
                String prefix = in.readUTF();
                String suffix = in.readUTF();
                String chatChannel = in.readUTF();
                ChatManager.channelSend(sender, text, prefix, suffix, chatChannel);
                return;
            }

            if (subChannel.equals("client_chat_channel-switch")) {
                String sender = in.readUTF();
                String chatChannel = in.readUTF();
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
                if (player == null) {
                    return;
                }
                DataHashTable.channel.put(player.getUniqueId(), chatChannel);
                return;
            }

            if (subChannel.equals("client_chat_afk-switch")) {
                String sender = in.readUTF();
                boolean value = in.readBoolean();
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
                if (player == null) {
                    return;
                }
                if (value) {
                    DataHashTable.isafk.put(player.getUniqueId(), value);
                } else {
                    DataHashTable.isafk.remove(player.getUniqueId());
                }
                return;
            }

            if (subChannel.equals("client_chat_spy-switch")) {
                String sender = in.readUTF();
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
                if (player == null) {
                    return;
                }
                if (!DataHashTable.socialspy.containsKey(player.getUniqueId())) {
                    DataHashTable.socialspy.put(player.getUniqueId(), true);
                    player.sendMessage("§aChannel: ALLE (SocialSpy on)");

                } else {
                    DataHashTable.socialspy.remove(player.getUniqueId());
                    player.sendMessage("§aChannel: EIGENE (SocialSpy off)");
                }
                return;
            }

            if (subChannel.equals("client_chat_guild-chat")) {
                String guild = in.readUTF();
                String sender = in.readUTF();
                String text = in.readUTF();
                ChatManager.sendGuildChat(guild, sender, text);
                return;
            }

            if (subChannel.equals("client_chat_private-msg")) {
                String sender = in.readUTF();
                String reciever = in.readUTF();
                String text = in.readUTF();
                String prefix = in.readUTF();
                ChatManager.privateMsgChat(sender, reciever, text, prefix);
                return;
            }

            if (subChannel.equals("client_chat_private-reply")) {
                String sender = in.readUTF();
                String text = in.readUTF();
                String prefix = in.readUTF();
                ChatManager.privateReplyChat(sender, text, prefix);
                return;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

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

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.database.mysql.BungeeQuery;
import de.linzn.mineSuite.bungee.module.ban.BanManager;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerBanListener implements IncomingDataListener {

    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            subChannel = in.readUTF();

            switch (subChannel) {
                case "client_ban_perma-ban": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String bannedby = in.readUTF();
                    UUID uuid = null;
                    if (ProxyServer.getInstance().getPlayer(player) != null) {
                        uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                    }
                    if (uuid == null) {

                        uuid = BungeeQuery.getUUID(player);
                    }
                    if (uuid != null) {
                        BanManager.banPlayer(uuid, reason, bannedby, -1L, player);
                    } else {
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(bannedby);
                        p.sendMessage(MessageDB.default_PLAYER_NOT_EXIST);
                    }
                    break;
                }
                case "client_ban_temp-ban": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String bannedby = in.readUTF();
                    Long seconds = in.readLong();
                    UUID uuid = null;
                    if (ProxyServer.getInstance().getPlayer(player) != null) {
                        uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                    }
                    if (uuid == null) {
                        uuid = BungeeQuery.getUUID(player);
                    }
                    if (uuid != null) {
                        BanManager.banPlayer(uuid, reason, bannedby, seconds, player);
                    } else {
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(bannedby);
                        p.sendMessage(MessageDB.default_PLAYER_NOT_EXIST);
                    }
                    break;
                }
                case "client_ban_perma-mute": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String mutedby = in.readUTF();
                    UUID uuid = null;
                    if (ProxyServer.getInstance().getPlayer(player) != null) {
                        uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                    }
                    if (uuid == null) {
                        uuid = BungeeQuery.getUUID(player);
                    }
                    if (uuid != null) {
                        BanManager.mutePlayer(uuid, reason, mutedby, -1L, player);
                    } else {
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(mutedby);
                        p.sendMessage(MessageDB.default_PLAYER_NOT_EXIST);
                    }
                    break;
                }
                case "client_ban_temp-mute": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String mutedby = in.readUTF();
                    Long seconds = in.readLong();
                    UUID uuid = null;
                    if (ProxyServer.getInstance().getPlayer(player) != null) {
                        uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                    }
                    if (uuid == null) {
                        uuid = BungeeQuery.getUUID(player);
                    }
                    if (uuid != null) {
                        BanManager.mutePlayer(uuid, reason, mutedby, seconds, player);
                    } else {
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(mutedby);
                        p.sendMessage(MessageDB.default_PLAYER_NOT_EXIST);
                    }
                    break;
                }
                case "client_ban_kick": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String kickedby = in.readUTF();
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
                    if (p != null) {
                        String sendmessage = "§6Du wurdest von §a" + kickedby + " §6vom Server geschmissen. \nGrund: §a" + reason;
                        p.disconnect(sendmessage);
                        JServerBanOutput.kickMSG(player, reason, kickedby);
                    }
                    break;
                }
                case "client_ban_unban": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String unbannedby = in.readUTF();
                    BanManager.unBan(player, reason, unbannedby);
                    break;
                }
                case "client_ban_unmute": {
                    String player = in.readUTF();
                    String reason = in.readUTF();
                    String unmutedby = in.readUTF();
                    BanManager.unMute(player, reason, unmutedby);
                    break;
                }
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}

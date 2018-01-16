package de.linzn.mineSuite.bungee.socket.listener;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.database.mysql.MySQLTasks;
import de.linzn.mineSuite.bungee.managers.MuteManager;
import de.linzn.mineSuite.bungee.socket.output.JServerBanOutput;
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

            if (subChannel.equals("client_ban_perma-ban")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String bannedby = in.readUTF();
                UUID uuid = null;
                if (ProxyServer.getInstance().getPlayer(player) != null) {
                    uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                }
                if (uuid == null) {

                    uuid = MySQLTasks.getUUID(player);
                }
                if (uuid != null) {
                    de.linzn.mineSuite.bungee.managers.BanManager.banPlayer(uuid, reason, bannedby, -1L, player);
                } else {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(bannedby);
                    p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
                }
                return;
            }
            if (subChannel.equals("client_ban_temp-ban")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String bannedby = in.readUTF();
                Long seconds = in.readLong();
                UUID uuid = null;
                if (ProxyServer.getInstance().getPlayer(player) != null) {
                    uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                }
                if (uuid == null) {
                    uuid = MySQLTasks.getUUID(player);
                }
                if (uuid != null) {
                    de.linzn.mineSuite.bungee.managers.BanManager.banPlayer(uuid, reason, bannedby, seconds, player);
                } else {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(bannedby);
                    p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
                }

                return;
            }
            if (subChannel.equals("client_ban_perma-mute")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String mutedby = in.readUTF();
                UUID uuid = null;
                if (ProxyServer.getInstance().getPlayer(player) != null) {
                    uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                }
                if (uuid == null) {
                    uuid = MySQLTasks.getUUID(player);
                }
                if (uuid != null) {
                    MuteManager.mutePlayer(uuid, reason, mutedby, -1L, player);
                } else {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(mutedby);
                    p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
                }
                return;
            }
            if (subChannel.equals("client_ban_temp-mute")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String mutedby = in.readUTF();
                Long seconds = in.readLong();
                UUID uuid = null;
                if (ProxyServer.getInstance().getPlayer(player) != null) {
                    uuid = ProxyServer.getInstance().getPlayer(player).getUniqueId();
                }
                if (uuid == null) {
                    uuid = MySQLTasks.getUUID(player);
                }
                if (uuid != null) {
                    MuteManager.mutePlayer(uuid, reason, mutedby, seconds, player);
                } else {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(mutedby);
                    p.sendMessage(MessageDB.PLAYER_NOT_EXIST);
                }
                return;
            }
            if (subChannel.equals("client_ban_kick")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String kickedby = in.readUTF();
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
                if (p != null) {
                    String sendmessage = "§6Du wurdest von §a" + kickedby + " §6vom Server geschmissen. \nGrund: §a" + reason;
                    p.disconnect(sendmessage);
                    JServerBanOutput.kickMSG(player, reason, kickedby);
                }
                return;
            }
            if (subChannel.equals("client_ban_unban")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String unbannedby = in.readUTF();
                de.linzn.mineSuite.bungee.managers.BanManager.unBan(player, reason, unbannedby);
                return;
            }
            if (subChannel.equals("client_ban_unmute")) {
                String player = in.readUTF();
                String reason = in.readUTF();
                String unmutedby = in.readUTF();
                MuteManager.unMute(player, reason, unmutedby);
                return;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}

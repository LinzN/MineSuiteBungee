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

package de.linzn.mineSuite.bungee.module.core.socket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.module.core.BungeeManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerBungeeListener implements IncomingDataListener {

    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            subChannel = in.readUTF();

            if (subChannel.equals("client_confirm-teleport")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                String locationServer = in.readUTF();
                if (DataHashTable.readyToTeleport.containsKey(playerUUID)) {
                    DataHashTable.readyToTeleport.get(playerUUID).set(true);
                } else {
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
                    if (player != null) {
                        JServerBungeeOutput.cancelTeleport(player.getServer().getInfo().getName(), playerUUID, locationServer);
                    }
                }
            }

            if (subChannel.equals("client_compare-ip")) {
                UUID actorUUID = UUID.fromString(in.readUTF());
                String firstPlayer = in.readUTF();
                String secondPlayer = in.readUTF();
                BungeeManager.compareIPAddresses(actorUUID, firstPlayer, secondPlayer);
            }
            if (subChannel.equals("client_get-last-seen")) {
                UUID actorUUID = UUID.fromString(in.readUTF());
                String targetPlayer = in.readUTF();
                BungeeManager.lastPlayerSeen(actorUUID, targetPlayer);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

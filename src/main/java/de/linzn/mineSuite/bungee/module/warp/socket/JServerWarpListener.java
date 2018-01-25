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

package de.linzn.mineSuite.bungee.module.warp.socket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.module.warp.WarpManager;
import de.linzn.mineSuite.bungee.utils.Location;

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
                String warpName = in.readUTF();
                UUID playerUUID = UUID.fromString(in.readUTF());
                WarpManager.sendPlayerToWarp(playerUUID, warpName, false);
                return;

            } else if (subChannel.equals("client_warp_create-warp")) {
                String warpName = in.readUTF();
                UUID playerUUID = UUID.fromString(in.readUTF());
                Location location = new Location(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());
                int publicWarp = in.readInt();
                WarpManager.createWarp(warpName, playerUUID, location, publicWarp);
                return;
            } else if (subChannel.equals("client_warp_remove-warp")) {
                String warpName = in.readUTF();
                UUID playerUUID = UUID.fromString(in.readUTF());
                WarpManager.removeWarp(warpName, playerUUID);
                return;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

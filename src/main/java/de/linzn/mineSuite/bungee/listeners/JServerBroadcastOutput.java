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

package de.linzn.mineSuite.bungee.listeners;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.utils.Location;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JServerBroadcastOutput {

    public static void broadcastTest(Location loc) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("servername");
            dataOutputStream.writeUTF("server_warp_teleport-warp");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBroadcast", byteArrayOutputStream.toByteArray());
    }
}

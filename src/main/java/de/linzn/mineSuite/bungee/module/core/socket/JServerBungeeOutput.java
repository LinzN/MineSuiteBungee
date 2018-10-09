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

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerBungeeOutput {


    public static void requestTeleportConfirm(String server, UUID playerUUID, String targetServer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF("server_confirm-teleport");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(targetServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBungee", byteArrayOutputStream.toByteArray());
    }

    public static void cancelTeleport(String server, UUID playerUUID, String targetServer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF("server_cancel-teleport");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(targetServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBungee", byteArrayOutputStream.toByteArray());
    }

    public static void request_economy_balance(String server, String accountName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF("server_request-balance");
            dataOutputStream.writeUTF(accountName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteBungee", byteArrayOutputStream.toByteArray());
    }

}

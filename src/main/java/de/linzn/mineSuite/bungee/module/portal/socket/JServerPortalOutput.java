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

package de.linzn.mineSuite.bungee.module.portal.socket;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.module.portal.mysql.Portal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JServerPortalOutput {

    public static void enablePortal(String server, Portal portal)

    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF("server_portal_enable-portal");

            dataOutputStream.writeUTF(portal.portalName);
            dataOutputStream.writeUTF(portal.fillType);
            dataOutputStream.writeUTF(portal.worldName);

            dataOutputStream.writeDouble(portal.minX);
            dataOutputStream.writeDouble(portal.minY);
            dataOutputStream.writeDouble(portal.minZ);

            dataOutputStream.writeDouble(portal.maxX);
            dataOutputStream.writeDouble(portal.maxY);
            dataOutputStream.writeDouble(portal.maxZ);

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuitePortal", byteArrayOutputStream.toByteArray());
    }

    public static void disablePortal(String server, String portalName)

    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF("server_portal_disable-portal");
            dataOutputStream.writeUTF(portalName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuitePortal", byteArrayOutputStream.toByteArray());
    }
}

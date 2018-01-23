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

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.module.portal.PortalManager;
import de.linzn.mineSuite.bungee.module.portal.mysql.Portal;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerPortalListener implements IncomingDataListener {


    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            subChannel = in.readUTF();

            if (subChannel.equals("client_portal_create-portal")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                /* Portal target destination and type */
                String portalName = in.readUTF();
                String portalType = in.readUTF();
                String portalDestination = in.readUTF();
                String fillType = in.readUTF();
                /* Location of the portal */
                String serverName = in.readUTF();
                String worldName = in.readUTF();
                /* Cords of min side minX, minY, minZ */
                double minX = in.readDouble();
                double minY = in.readDouble();
                double minZ = in.readDouble();
                /* Cords of min side maxX, maxY, maxZ */
                double maxX = in.readDouble();
                double maxY = in.readDouble();
                double maxZ = in.readDouble();
                Portal portal = new Portal(portalName, serverName, portalType, portalDestination, fillType, worldName, minX, minY, minZ, maxX, maxY, maxZ);
                PortalManager.setPortal(playerUUID, portal);
            }

            if (subChannel.equals("client_portal_remove-portal")) {
                UUID playerUUID = UUID.fromString(in.readUTF());
                String portalName = in.readUTF();
                String serverName = in.readUTF();
                PortalManager.unsetPortal(playerUUID, portalName, serverName);
            }

            if (subChannel.equals("client_portal_request-portals")) {
                String serverName = in.readUTF();
                PortalManager.processingPortalRequest(serverName);
            }

            if (subChannel.equals("client_portal_use-portal")) {
                String portalName = in.readUTF();
                UUID playerUUID = UUID.fromString(in.readUTF());
                PortalManager.playerUsePortal(portalName, playerUUID);
                //todo some stuff
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

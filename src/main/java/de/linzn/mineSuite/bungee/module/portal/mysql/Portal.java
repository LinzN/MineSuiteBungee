
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

package de.linzn.mineSuite.bungee.module.portal.mysql;


public class Portal {
    public String portalName;
    public String serverName;
    public String portalType;
    public String portalDestination;
    public String worldName;
    public String fillType;

    public double minX;
    public double minY;
    public double minZ;

    public double maxX;
    public double maxY;
    public double maxZ;


    public Portal(String portalName, String serverName, String portalType, String portalDestination, String fillType, String worldName, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.portalName = portalName;
        this.serverName = serverName;
        this.portalType = portalType;
        this.portalDestination = portalDestination;
        this.fillType = fillType;
        this.worldName = worldName;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }


}
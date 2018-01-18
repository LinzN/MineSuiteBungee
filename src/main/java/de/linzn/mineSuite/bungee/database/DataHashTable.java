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

package de.linzn.mineSuite.bungee.database;

import de.linzn.mineSuite.bungee.utils.Location;

import java.util.HashMap;
import java.util.UUID;

public class DataHashTable {

    public static HashMap<UUID, Boolean> acceptingTeleports = new HashMap<>();
    public static HashMap<UUID, Location> deathBackLocation = new HashMap<>();
    public static HashMap<UUID, Location> teleportBackLocation = new HashMap<>();
    public static HashMap<UUID, Boolean> lastBack = new HashMap<>();
    public static HashMap<UUID, Boolean> isMuted = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Long> muteTime = new HashMap<UUID, Long>();
    public static HashMap<UUID, String> mutedBy = new HashMap<UUID, String>();
    public static HashMap<UUID, String> muteReason = new HashMap<UUID, String>();
    public static HashMap<UUID, UUID> guildInvites = new HashMap<UUID, UUID>();
    public static HashMap<UUID, UUID> msgreply = new HashMap<UUID, UUID>();
    public static HashMap<UUID, String> channel = new HashMap<UUID, String>();
    public static HashMap<UUID, Boolean> isafk = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Boolean> socialspy = new HashMap<UUID, Boolean>();
    public static HashMap<UUID, Boolean> session = new HashMap<UUID, Boolean>();

}

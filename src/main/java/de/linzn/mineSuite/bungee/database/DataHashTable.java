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

import de.linzn.mineSuite.bungee.utils.FakePair;
import de.linzn.mineSuite.bungee.utils.Location;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataHashTable {

    public static HashMap<UUID, Location> deathBackLocation = new HashMap<>();
    public static HashMap<UUID, Boolean> lastBack = new HashMap<>();
    public static HashMap<UUID, Boolean> isMuted = new HashMap<>();
    public static HashMap<UUID, Long> muteTime = new HashMap<>();
    public static HashMap<UUID, String> mutedBy = new HashMap<>();
    public static HashMap<UUID, String> muteReason = new HashMap<>();
    public static HashMap<UUID, UUID> msgreply = new HashMap<>();
    public static HashMap<UUID, String> channel = new HashMap<>();
    public static HashMap<UUID, Boolean> isafk = new HashMap<>();
    public static HashMap<UUID, Boolean> socialspy = new HashMap<>();
    public static HashMap<UUID, Boolean> session = new HashMap<>();
    public static HashMap<UUID, AtomicBoolean> readyToTeleport = new HashMap<>();
    public static HashMap<String, FakePair> economyRequest = new HashMap<>();

}

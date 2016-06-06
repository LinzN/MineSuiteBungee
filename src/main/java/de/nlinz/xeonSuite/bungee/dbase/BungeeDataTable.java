package de.nlinz.xeonSuite.bungee.dbase;

import java.util.HashMap;
import java.util.UUID;

import de.nlinz.xeonSuite.bungee.utils.Location;

public class BungeeDataTable {

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
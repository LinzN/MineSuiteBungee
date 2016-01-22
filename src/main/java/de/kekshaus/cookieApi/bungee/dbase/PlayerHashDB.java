package de.kekshaus.cookieApi.bungee.dbase;

import java.util.HashMap;
import java.util.UUID;

import de.kekshaus.cookieApi.bungee.utils.Location;

public class PlayerHashDB {

	public static HashMap<UUID, Boolean> acceptingTeleports = new HashMap<>();
	public static HashMap<UUID, Location> deathBackLocation = new HashMap<>();
	public static HashMap<UUID, Location> teleportBackLocation = new HashMap<>();
	public static HashMap<UUID, Boolean> lastBack = new HashMap<>();
	public static HashMap<UUID, Boolean> isMuted = new HashMap<UUID, Boolean>();
	public static HashMap<UUID, Long> muteTime = new HashMap<UUID, Long>();
	public static HashMap<UUID, String> mutedBy = new HashMap<UUID, String>();
	public static HashMap<UUID, String> muteReason = new HashMap<UUID, String>();
	public static HashMap<UUID, String> guildInvites = new HashMap<UUID, String>();
	public static HashMap<UUID, UUID> msgreply = new HashMap<UUID, UUID>();
	public static HashMap<UUID, String> channel = new HashMap<UUID, String>();
	public static HashMap<UUID, Boolean> isafk = new HashMap<UUID, Boolean>();
	public static HashMap<UUID, Boolean> socialspy = new HashMap<UUID, Boolean>();
	public static HashMap<UUID, Boolean> session = new HashMap<UUID, Boolean>();

}

package de.linzn.mineSuite.bungee.database;


import de.linzn.mineSuite.bungee.database.mysql.BungeeQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheManager {
    private static Map<UUID, String> playerCache = new HashMap<>();

    public static UUID getPlayerUUID(String playerName) {
        if (playerCache.containsValue(playerName)) {
            for (UUID uuid : playerCache.keySet()) {
                if (playerCache.get(uuid).equalsIgnoreCase(playerName)) {
                    return uuid;
                }
            }
        }

        UUID playerUUID = BungeeQuery.getUUID(playerName);

        if (playerUUID != null) {
            playerCache.put(playerUUID, playerName);
            return playerUUID;
        }
        return null;
    }

    public static String getPlayerName(UUID playerUUID) {
        if (playerCache.containsKey(playerUUID)) {
            return playerCache.get(playerUUID);
        }
        String playerName = BungeeQuery.getPlayerName(playerUUID);

        if (playerName != null) {
            playerCache.put(playerUUID, playerName);
            return playerName;
        }
        return null;
    }

    public UUID[] getPlayerUUIDs(String[] playerNames) {
        UUID[] playerUUIDS = new UUID[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = getPlayerName(playerUUIDS[i]);
        }
        return playerUUIDS;
    }

    public String[] getPlayerNames(UUID[] playerUUIDs) {
        String[] playerNames = new String[playerUUIDs.length];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = getPlayerName(playerUUIDs[i]);
        }
        return playerNames;
    }
}

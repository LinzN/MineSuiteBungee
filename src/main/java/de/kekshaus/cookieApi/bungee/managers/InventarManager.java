package de.kekshaus.cookieApi.bungee.managers;

import java.util.UUID;

import de.kekshaus.cookieApi.bungee.dbase.CookieInventory;
import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;

public class InventarManager {
	public static void setInventory(String playerName, String playerUUID, String armorData, String inventoryData,
			String enderchestData, double heathData, int foodData, int experienceData, long inventoryTime) {

		CookieInventory cInv = new CookieInventory(playerName, playerUUID);
		cInv.setArmorData(armorData);
		cInv.setInventoryData(inventoryData);
		cInv.setEnderchestData(enderchestData);
		cInv.setHeathData(heathData);
		cInv.setFoodData(foodData);
		cInv.setExperienceData(experienceData);
		cInv.setInventoryTime(inventoryTime);
		PlayerHashDB.inventory.put(cInv.getPlayerUUID(), cInv);
	}

	public static void removeInventory(UUID playerUUID) {
		if (PlayerHashDB.inventory.containsKey(playerUUID)) {
			PlayerHashDB.inventory.remove(playerUUID);
		}

	}

	public static CookieInventory getInventory(UUID playerUUID) {
		if (PlayerHashDB.inventory.containsKey(playerUUID)) {
			return PlayerHashDB.inventory.get(playerUUID);
		}
		return null;

	}
}

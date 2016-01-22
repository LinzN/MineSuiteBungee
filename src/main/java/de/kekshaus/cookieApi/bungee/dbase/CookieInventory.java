package de.kekshaus.cookieApi.bungee.dbase;

import java.util.UUID;

public class CookieInventory {
	private String playername;
	private UUID playeruuid;
	private long inventoryTimeData;
	private String armorData;
	private String inventoryData;
	private String endChestData;
	private double healthData;
	private int footData;
	private int experienceData;

	public CookieInventory(String playername, String playerUUID) {
		this.playername = playername;
		this.playeruuid = UUID.fromString(playerUUID);
	}

	// GetData
	public String getArmorData() {
		return this.armorData;
	}

	public String getInventoryData() {
		return this.inventoryData;
	}

	public String getEnderchestData() {
		return this.endChestData;
	}

	public long getInventoryTime() {
		return this.inventoryTimeData;
	}

	public double getHeathData() {
		return this.healthData;
	}

	public int getFoodData() {
		return this.footData;
	}

	public int getExperienceData() {
		return this.experienceData;
	}

	public String getPlayername() {
		return this.playername;
	}

	public UUID getPlayerUUID() {
		return this.playeruuid;
	}

	public String getPlayerUUIDString() {
		return this.playeruuid.toString();
	}

	// SetData
	public void setArmorData(String data) {
		this.armorData = data;
	}

	public void setInventoryData(String data) {
		this.inventoryData = data;
	}

	public void setEnderchestData(String data) {
		this.endChestData = data;
	}

	public void setInventoryTime(long data) {
		this.inventoryTimeData = data;
	}

	public void setHeathData(double data) {
		this.healthData = data;
	}

	public void setFoodData(int data) {
		this.footData = data;
	}

	public void setExperienceData(int data) {
		this.experienceData = data;
	}

}

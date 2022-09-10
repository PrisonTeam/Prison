package tech.mcprison.prison.backpacks;

import java.util.Set;
import java.util.TreeMap;

import tech.mcprison.prison.backpacks.BackpackEnums.BackpackFeatures;
import tech.mcprison.prison.backpacks.BackpackEnums.BackpackType;
import tech.mcprison.prison.internal.inventory.PlayerInventory;

public class PlayerBackpack {
	
	private String playerName;
	private String playerUuid;
	
	private String name;
	private String itemType; // XMaterial
	
	private Set<BackpackFeatures> features;
	
	// either inventory or silos... not both... not neither:
	private BackpackType backpackType;
	
	private PlayerInventory inventory;
	private int inventorySize;
	
	
	// if max of 54 silos, then can use a double-chest to view as GUI and 
	// show the itemStacks with quantity being added lore?
	private TreeMap<String, PlayerBackpackSiloData> silos;
	private int maxSiloSlots;
	private int maxSiloSize;
	private int maxTotalSize; // all silos combined

	public PlayerBackpack() {
		super();
		
		this.silos = new TreeMap<>();
		
	}
	
	public PlayerBackpack( String playerName, String playerUuid, 
			BackpackType backpackType, 
			String backpackName, String itemType ) {
		this();

		this.playerName = playerName;
		this.playerUuid = playerUuid;
		
		this.backpackType = backpackType;
		this.name = backpackName;
		this.itemType = itemType;
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerUuid() {
		return playerUuid;
	}
	public void setPlayerUuid(String playerUuid) {
		this.playerUuid = playerUuid;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Set<BackpackFeatures> getFeatures() {
		return features;
	}
	public void setFeatures(Set<BackpackFeatures> features) {
		this.features = features;
	}

	public BackpackType getBackpackType() {
		return backpackType;
	}
	public void setBackpackType(BackpackType backpackType) {
		this.backpackType = backpackType;
	}

	public PlayerInventory getInventory() {
		return inventory;
	}
	public void setInventory(PlayerInventory inventory) {
		this.inventory = inventory;
	}

	public int getInventorySize() {
		return inventorySize;
	}
	public void setInventorySize(int inventorySize) {
		this.inventorySize = inventorySize;
	}

	public TreeMap<String, PlayerBackpackSiloData> getSilos() {
		return silos;
	}
	public void setSilos(TreeMap<String, PlayerBackpackSiloData> silos) {
		this.silos = silos;
	}

	public int getMaxSiloSlots() {
		return maxSiloSlots;
	}
	public void setMaxSiloSlots(int maxSiloSlots) {
		this.maxSiloSlots = maxSiloSlots;
	}

	public int getMaxSiloSize() {
		return maxSiloSize;
	}
	public void setMaxSiloSize(int maxSiloSize) {
		this.maxSiloSize = maxSiloSize;
	}

	public int getMaxTotalSize() {
		return maxTotalSize;
	}
	public void setMaxTotalSize(int maxTotalSize) {
		this.maxTotalSize = maxTotalSize;
	}

}

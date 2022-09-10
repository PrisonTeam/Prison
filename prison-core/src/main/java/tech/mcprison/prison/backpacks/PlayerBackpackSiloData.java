package tech.mcprison.prison.backpacks;

import tech.mcprison.prison.internal.ItemStack;

public class PlayerBackpackSiloData {
	
	private String itemType; // key
	private ItemStack itemSample; // quantity of 1
	private int count;
	private boolean locked; // locks the itemType in to this silo even when count == 0
	
	public PlayerBackpackSiloData() {
		super();
		
	}

	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public ItemStack getItemSample() {
		return itemSample;
	}
	public void setItemSample(ItemStack itemSample) {
		this.itemSample = itemSample;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
}

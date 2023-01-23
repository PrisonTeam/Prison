package tech.mcprison.prison.backpacks;

public class BackpackEnums {

	public enum BackpackType {
		inventory, // Uses standard inventory object, up to 6 rows, 9 stacks each
		silo; // Up to 54 silos per backpack, which will use a chest to display
	}
	
	public enum BackpackFeatures {
		soulboundBackpack, // If player dies, they keep the backpack
		soulboundItems, // if soulboundBackpack is enabled, this will preserve items within
		virtual, // if not virtual, then the itemType must exist in their inventory
		tradable, // Able to trade/sell whole backpack with contents
		droppable, // able to remove the backpack item from inventory
		enablePickup, // normally picked up items go in to backpack
		enableSellall, // sellall will sell from backpack
		enablePlayerToggle, // player pickup toggleable  
		enableSellallToggle, // player sellall toggleable  
		enchanted, // show enchant glow on item 
		placeable, // Able to place backpack as a chest - may need to supply empty chests
		
		allowMultiples, // If this feature is not set, then a player can only have one backpack of a given type
		loreStats, // Update lore on backpack item with stats: blocks and counts
		restrictView, // Prevent viewing through open inventory. Use with loreStats.
		allowRename, // Allow players to rename their backpacks
	}
}

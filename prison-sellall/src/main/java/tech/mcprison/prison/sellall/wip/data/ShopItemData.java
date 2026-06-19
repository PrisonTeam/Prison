package tech.mcprison.prison.sellall.wip.data;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.sellall.wip.data.ShopData.ShopItemAction;

public class ShopItemData {
	
	private String name;
	private String description;
	
	
	// ItemId is the text representation of the block or item.  Generally it is 
	// an xseries name, but could also be a custom block name too, following
	// prison's qualified naming convention.
	private String itemId;
	private double cost;
	
	private ShopItemAction action;
	
	private transient PrisonBlock prisonBlock;
	
	public ShopItemData() {
		super();
	}
	
	public ShopItemData( String name, String description, String itemId, double cost ) {
		super();
		
		this.name = name;
		this.description = description;
		this.itemId = itemId;
		this.cost = cost;
	}
		
		
}

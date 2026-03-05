package tech.mcprison.prison.sellall.wip.data;

import java.util.List;

public class ShopData {
	
	private String name;
	
	private int priority;
	
	// Connection identifies how this shop is tied to something, such
	// as a rank, mine, or a perm.
	private String connection;
	private ConnectionType connectionType;
	
	private String parentName;
	private transient ShopData parent;
	private double parentMultiplier;
//	private boolean includeParentItems; // if false, then makes no sense to have a parent
	
	
	/**
	 * Added items is the "raw" list of items that should be added to this shop.
	 * Removed items are applied only to inherited items from the parents.
	 */
	private List<String> itemsAdd;
	private List<String> itemsRemove;

	/*
	 * Items is the "calculated" set of items, which includes items from the parents,
	 * with the adjustments made to the prices.
	 */
	private transient List<String> items;

	public enum ConnectionType {
		RANK,
		MINE,
		PERM;
	}
	
	/**
	 * <p>The default value will be ADD, which indicates that the specified 
	 * ShopItemData should be "added" to the shop.  The REMOVE value indicates
	 * that this item should be removed from the current shop, as all of the
	 * items are pulled from the parents. It's a way to remove items from a shop
	 * when inheriting from parents.
	 * </p>
	 *
	 */
	public enum ShopItemAction {
		ADD,
		REMOVE,
		
		;
	}
	
	public ShopData() {
		super();
		
		boolean a = !!true;
		boolean b = !!!!!!a;
	}
}

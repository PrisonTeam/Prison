package tech.mcprison.prison.spigot.bombs;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;

public class MineBombData {

	private String name;
	
	
	/**
	 * <p>The String name of an XMaterial item to use as the "bomb".
	 * </p>
	 */
	private String itemType;
	
	/*
	 * <p>A transient reference to the actual XMaterial item that is used
	 * for the bomb.  This is converted from the String itemType.
	 * </p>
	 */
	private transient XMaterial item;
	
	
	private List<String> lore;
	
	/**
	 * <p>The radius identifies how large the blast should be.
	 * </p>
	 */
	private int radius = 1;
	
	/**
	 * <p>The chance of complete removal.  So if the radius includes
	 * 100 blocks, but the chance is only 50%, each block will be given
	 * a chance if it is to be included, or excluded.
	 * </p>
	 */
	// private double chance;
	
	
	private String explosionShape;
	
	public MineBombData() {
		super();
		
	}

	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}

	public String getItemType() {
		return itemType;
	}
	public void setItemType( String itemType ) {
		this.itemType = itemType;
	}

	public XMaterial getItem() {
		return item;
	}
	public void setItem( XMaterial item ) {
		this.item = item;
	}

	public List<String> getLore() {
		return lore;
	}
	public void setLore( List<String> lore ) {
		this.lore = lore;
	}

	public int getRadius() {
		return radius;
	}
	public void setRadius( int radius ) {
		this.radius = radius;
	}

	public String getExplosionShape() {
		return explosionShape;
	}
	public void setExplosionShape( String explosionShape ) {
		this.explosionShape = explosionShape;
	}
	
}

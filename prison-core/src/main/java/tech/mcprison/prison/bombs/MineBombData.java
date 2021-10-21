package tech.mcprison.prison.bombs;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.internal.block.PrisonBlock;

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
	private transient PrisonBlock item;
	
	
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
	
	
	private String description;
	
	
	private boolean glowing = false;
	
	private boolean activated = false;
	
	
	public MineBombData() {
		super();
		
	}
	
	
	public MineBombData( String name, String itemType, String explosionShape, 
				int radius, String... lores ) {
		this();
		
		this.name = name;
		this.itemType = itemType;
		
		this.explosionShape = explosionShape;
		this.radius = radius;
		
		this.lore = new ArrayList<>();
		
		if ( lores != null ) {
			for ( String l : lores ) {
				this.lore.add( l );
			}
		}
		
	}
	
	
	public MineBombData clone() {
		MineBombData cloned = new MineBombData( getName(), getItemType(), getExplosionShape(),
				getRadius() );
		
		cloned.setDescription( getDescription() );
		cloned.setGlowing( isGlowing() );
		cloned.setActivated( isActivated() );
		
		for ( String l : lore ) {
			cloned.getLore().add( l );
		}
		
		return cloned;
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

	public PrisonBlock getItem() {
		return item;
	}
	public void setItem( PrisonBlock item ) {
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

	public String getDescription() {
		return description;
	}
	public void setDescription( String description ) {
		this.description = description;
	}

	public boolean isGlowing() {
		return glowing;
	}
	public void setGlowing( boolean glowing ) {
		this.glowing = glowing;
	}

	public boolean isActivated() {
		return activated;
	}
	public void setActivated( boolean activated ) {
		this.activated = activated;
	}
	
}

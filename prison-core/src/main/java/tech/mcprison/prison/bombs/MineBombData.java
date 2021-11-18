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
	 * <p>The radius identifies how large the blast should be as 
	 * expressed in a radius.  Generally this applies to a sphere or 
	 * a circle or disk, but can also apply to other shapes such as
	 * cubes.
	 * </p>
	 * 
	 * <p>The radius is always based upon a center block, plus the radius.
	 * Therefore a radius of 1 will result in a sphere with a diameter of
	 * three (1 block for the center, and then 1 additional block on each
	 * side).  The calculated diameter can never be even, and the actual 
	 * blast size will appear to be larger than this specified radius due
	 * to the starting inner block.
	 * </p>
	 * 
	 */
	private int radius = 1;
	
	
	/**
	 * <p>Some shapes may require an inner radius, such as a hollow sphere or
	 * a torus.  This is ignored for all other shapes.
	 * </p>
	 */
	private int radiusInner = 0;
	
	
	/**
	 * <p>Some shapes may require a height, such as a cylinder or a cone.
	 * Even if the shape is laying on it's side, the height will still 
	 * apply in other planes and not just up and down.  
	 * This is ignored for all other shapes.
	 * </p>
	 */
	private int height = 0;
	
	/**
	 * <p>The chance of complete removal.  So if the explosion includes
	 * 100 blocks, but the chance is only 50%, each block will be given
	 * a 50% chance to be included from the explosion block list.
	 * </p>
	 */
	private double removalChance;
	
	
	private String explosionShape;
	
	
	private String toolInHandName;
	
	private int toolInHandFortuneLevel = 0;
	
	
	private String description;
	
	
	/**
	 * <p>On spigot versions that support it, the bomb, when placed, will glow.
	 * Was introduced with Minecraft 1.9.  Applies only to Entities, of which
	 * dropped items may be considered an entity?
	 * </p>
	 */
	private boolean glowing = false;
	
	
	/**
	 * <p>This autosell will force the results of an explosion to autosell even when autosell
	 * is disabled on the server's auto features.  This is almost a requirement for large 
	 * explosions, especially if an unsafe fortune enchantment is being used.
	 * </p>
	 */
	private boolean autosell = false;
	
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
		
		this.toolInHandName = null;
		this.toolInHandFortuneLevel = 0;
		
		this.removalChance = 100.0d;
		
		this.glowing = false;
	}
	
	
	public MineBombData clone() {
		MineBombData cloned = new MineBombData( getName(), getItemType(), getExplosionShape(),
				getRadius() );
		
		cloned.setDescription( getDescription() );
		
		cloned.setToolInHandName( getToolInHandName() );
		cloned.setToolInHandFortuneLevel( getToolInHandFortuneLevel() );
		
		cloned.setRadiusInner( getRadiusInner() );
		cloned.setHeight( getHeight() );
		
		cloned.setRemovalChance( getRemovalChance() );
		
		
		cloned.setGlowing( isGlowing() );
		cloned.setAutosell( isAutosell() );
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

	public int getRadiusInner() {
		return radiusInner;
	}
	public void setRadiusInner( int radiusInner ) {
		this.radiusInner = radiusInner;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight( int height ) {
		this.height = height;
	}

	public double getRemovalChance() {
		return removalChance;
	}
	public void setRemovalChance( double removalChance ) {
		this.removalChance = removalChance;
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

	public String getToolInHandName() {
		return toolInHandName;
	}
	public void setToolInHandName( String toolInHandName ) {
		this.toolInHandName = toolInHandName;
	}

	public int getToolInHandFortuneLevel() {
		return toolInHandFortuneLevel;
	}
	public void setToolInHandFortuneLevel( int toolInHandFortuneLevel ) {
		this.toolInHandFortuneLevel = toolInHandFortuneLevel;
	}

	public boolean isGlowing() {
		return glowing;
	}
	public void setGlowing( boolean glowing ) {
		this.glowing = glowing;
	}

	public boolean isAutosell() {
		return autosell;
	}
	public void setAutosell( boolean autosell ) {
		this.autosell = autosell;
	}

	public boolean isActivated() {
		return activated;
	}
	public void setActivated( boolean activated ) {
		this.activated = activated;
	}
	
}

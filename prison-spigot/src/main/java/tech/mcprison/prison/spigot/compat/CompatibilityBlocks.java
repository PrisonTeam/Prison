package tech.mcprison.prison.spigot.compat;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.util.Location;

public interface CompatibilityBlocks
			extends CompatibilityPlayer {

	public SpigotBlock getSpigotBlock(Block spigotBlock);
	
	public XMaterial getXMaterial( Block spigotBlock );
	
	public XMaterial getXMaterial( PrisonBlock prisonBlock );
	

	
	
	public void updateSpigotBlock( PrisonBlock prisonBlock, Block spigotBlock );
	
	public void updateSpigotBlock( XMaterial xMat, Block spigotBlock );
	

	
	public void updateSpigotBlockAsync( PrisonBlock prisonBlock, Location location );
	

	
	public BlockTestStats testCountAllBlockTypes();
	

    public int getDurabilityMax( SpigotItemStack itemStack );
	
    public boolean hasDurability( SpigotItemStack itemStack );
    
    public int getDurability( SpigotItemStack itemStack );
    
    public boolean setDurability( SpigotItemStack itemStack, int newDurability );
    
	public void setBlockFace( Block bBlock, BlockFace blockFace );
	
    public ItemStack getLapisItemStack();
    
    
    public int getMinY();
    
    public int getMaxY();
	
    

    /**
     * Not compatible with Spigot 1.8 through 1.13 so return a value of 0.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    public int getCustomModelData( SpigotItemStack itemStack );
    /**
     * Not compatible with Spigot 1.8 through 1.13 so return a value of 0.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
    public int getCustomModelData( ItemStack itemStack );
    
    /**
     * Not compatible with Spigot 1.8 through 1.13 so do nothing.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
	public void setCustomModelData( SpigotItemStack itemStack, int customModelData );
    /**
     * Not compatible with Spigot 1.8 through 1.13 so do nothing.
     * Only available with 1.14 and higher.
     * @param itemStack
     * @return
     */
	public void setCustomModelData( ItemStack itemStack, int customModelData );

	
	/**
	 * <p>With spigot 1.14 and newer, there is a function on a block that
	 * identifies if a block is passable.  The description in the api docs are:
	 * </p>
	 * 
	 * <pre>
	 * Checks if this block is passable.

A block is passable if it has no colliding parts that would prevent 
players from moving through it.

Examples: Tall grass, flowers, signs, etc. are passable, but open doors, 
fence gates, trap doors, etc. are not because they still have parts that 
can be collided with.
	 * </p>
	 * 
	 * @param bBlock
	 * @return
	 */
	public boolean isPassable( Block bBlock );

}

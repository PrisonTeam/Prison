package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * <p>This is an example of an explosive event that should be used for 
 * enchantments that will break more than one block at a time.
 * </p>
 * 
 * <p>The main block that is provided should be "the block" that the 
 * player mined and broke.  That should be the same block as included
 *  in the standard bukkit BlockBreakEvent.
 * </p>
 * 
 * <p>The player object is the same as found in the standard bukkit
 * BlockBreakEvent.
 * </p>
 * 
 * <p>The blocks in explodedBlocks List should be all of the blocks that
 * are included within the explosion, or the enchantment effects, but 
 * must exclude the block that was broke.  These blocks do not have to be
 * constrained to a physical mine, since prison will validate each block 
 * that is included, and will only process the blocks that are within the
 * actual mines.  All blocks outside of the mine will be ignored.
 * </p>
 * 
 * <p>The field triggeredBy is optional, but really should identify 
 * what enchantment was used that triggered this event. The value of 
 * this field will serve as a filter on blockBreak events so users can
 * setup filters to take action when specific enchantments are used.
 * </p>
 * 
 * 
 * @author RoyalBlueRanger
 *
 */
public class ExplosiveBlockBreakEvent
	extends BlockBreakEvent
{
	private static final HandlerList handlers = new HandlerList();
	
	private List<Block> explodedBlocks;
	
	private String triggeredBy;
	
	public ExplosiveBlockBreakEvent( Block theBlock, Player player,
			List<Block> explodedBlocks, String triggeredBy ) {
		super( theBlock, player );
		
		this.explodedBlocks = explodedBlocks;
		this.triggeredBy = triggeredBy;
	}
	public ExplosiveBlockBreakEvent( Block theBlock, Player player,
			List<Block> explodedBlocks ) {
		this( theBlock, player, explodedBlocks, null );
	}
	public ExplosiveBlockBreakEvent( Block theBlock, Player player ) {
		this( theBlock, player, new ArrayList<>(), null );
		
	}
	
	public List<Block> getExplodedBlocks() {
		return explodedBlocks;
	}
	public void setExplodedBlocks( List<Block> explodedBlocks ) {
		this.explodedBlocks = explodedBlocks;
	}
	
	public String getTriggeredBy() {
		return triggeredBy;
	}
	public void setTriggeredBy( String triggeredBy ) {
		this.triggeredBy = triggeredBy;
	}
	
	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
	public static HandlerList getHandlersList(){
		return handlers;
	}
}

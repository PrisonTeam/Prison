package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
	
	/**
	 * This is strictly a non-function example of how to fire this event.  Make sure you load up the
	 * parameters correctly, of which this example is passing nulls for block and player, and an 
	 * empty explodedBlocks list.  The only purpose for this dummy function is to show you how
	 * to use it in your plugin.
	 */
	public void sampleUsage() {
		
		Block block = null;
		Player player = null;
		List<Block> explodedBlocks = new ArrayList<>();
		String triggeredBy = "sampleUsage";
		
		ExplosiveBlockBreakEvent ebbe = new ExplosiveBlockBreakEvent( block, player, explodedBlocks, triggeredBy );
	
		Bukkit.getServer().getPluginManager().callEvent( ebbe );
		
		if ( !ebbe.isCancelled() ) {
			// Go ahead and break the blocks in your plugin.  The fact that it's not canceled implies
			// that no other plugin was able to process this event.
		}
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
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}

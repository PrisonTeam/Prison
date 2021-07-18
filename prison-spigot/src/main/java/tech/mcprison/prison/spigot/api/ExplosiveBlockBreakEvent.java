package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

/**
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

package me.pulsi_.prisonenchants.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Support for Prison Enchant's v1.0 and v2.0 of the API...
 * 
 * 
 * 			
	{
		// PrisonEnchants-API-v1.0.0:
		//me.pulsi_.prisonenchants.events.PEExplosionEvent

		PEExplosionEvent peEE = new PEExplosionEvent();
		Block block = peEE.getBlockBroken();
		String eventName = peEE.getEventName();
		List<Block> explodedBlocks = peEE.getExplodedBlocks();
		HandlerList handlers = peEE.getHandlers();
		HandlerList handlerList = peEE.getHandlerList();
		Player player = peEE.getPlayer();
		boolean async = peEE.isAsynchronous();
		boolean canceled = peEE.isCancelled();
		peEE.setCancelled(false);
		
	}
	{
		// PrisonEnchants-API-v2.2.0:
		//me.pulsi_.prisonenchants.events.PEExplosionEvent

		PEExplosionEvent peEE = new PEExplosionEvent();
		List<Block> blocks = peEE.getBlocks();
		PEEnchant enchantSource = peEE.getEnchantSource(); // not needed
		String eventName = peEE.getEventName();
		HandlerList handlers = peEE.getHandlers();
		HandlerList handlerList = peEE.getHandlerList();
		Player player = peEE.getPlayer();
		boolean async = peEE.isAsynchronous();
		boolean canceled = peEE.isCancelled();
		peEE.setBlocks( blocks );
		peEE.setCancelled(false);
	}
	{
		// PrisonEnchants-API-v2.2.1:
		//me.pulsi_.prisonenchants.events.PEExplosionEvent
		NOTE: v2.2.1 adds the function getOrigin();
		
		Location locationOfOriginalBlock = peEE.getOrigin();
	}
 * 
 */
public class PEExplosionEvent
	extends Event {
	
	public String getEventName() {
		return "";
	}

	/**
	 * PrisonEnchants v1.0:
	 * @return
	 */
	public Block getBlockBroken() {
		Block results = null;
		return results;
	}
	
	/**
	 * PrisonEnchants v1.0:
	 * @return
	 */
	public List<Block> getExplodedBlocks() {
		List<Block> results = new ArrayList<>();
		return results;
	}
	
	/**
	 * PrisonEnchants v2.2:
	 * @return
	 */
	public List<Block> getBlocks() {
		List<Block> results = new ArrayList<>();
		return results;
	}
	
	public HandlerList getHandlers() {
		return null;
	}
	public static HandlerList getHandlerList() {
		return null;
	}
	
	public Player getPlayer() {
		return null;
	}
	
	public Location getOrigin() {
		return null;
	}
	
//	public boolean isAsynchronous() {
//		return true;
//	}
	
	public boolean isCancelled() {
		return true;
	}
	
	public void setCancelled( boolean cancel ) {
	}
	
}

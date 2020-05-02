package tech.mcprison.prison.spigot.block;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.modules.Module;

public class OnBlockBreakEventListener 
	implements Listener {

	public OnBlockBreakEventListener() {
		super();
	}
	
	
    /**
     * <p>The EventPriorty.MONITOR means that the state of the event is OVER AND DONE,
     * so this function CANNOT do anything with the block, other than "monitor" what
     * happened.  That is all we need to do, is to just count the number of blocks within
     * a mine that have been broken.
     * </p>
     * 
     * <p>One interesting fact about this monitoring is that we know that a block was broken,
     * not because of what is left (should be air), but because this function was called.
     * There is a chance that the event was canceled and the block remains unbroken, which
     * is what WorldGuard would do.  But the event will also be canceled when auto pickup is
     * enabled, and at that point the BlockType will be air.
     * </p>
     * 
     * <p>So it's important to check to see that the BlockType is Air. If it is canceled we 
     * need to count it if it is AIR since something already broke the block and took the drop.  
     * If it is not 
     * canceled we still need to count it since it will be a normal drop.  Basically if it is
     * air, then count it since there is no way to break AIR.
     * </p>
     * 
     * @param e
     */
    @EventHandler(priority=EventPriority.MONITOR) 
    public void onBlockBreak(BlockBreakEvent e) {
    	
    	boolean isAir = e.getBlock().getType() != null && e.getBlock().getType() == Material.AIR;
    	   	
    	// If canceled it must be AIR, otherwise if it is not canceled then 
    	// count it since it will be a normal drop
    	if ( e.isCancelled() && isAir || !e.isCancelled() ) {

    		
    		// Get the player objects: Spigot and the Prison player:
    		// Player p = e.getPlayer();
    		// SpigotPlayer player = new SpigotPlayer( p );
    		
    		// Validate that the event is happening within a mine so we can count the block break event:
    		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
    		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
    			PrisonMines mineManager = (PrisonMines) mmOptional.get();
    			
    			
    			// Use a timed priority queue where most recent mines go to the front of the
    			// list so they are hit first?  Inactive mines go the end.
    			
    			for ( Mine mine : mineManager.getMines() ) {
    				SpigotBlock block = new SpigotBlock(e.getBlock());
    				if ( mine.isInMine( block.getLocation() ) ) {
    					
    					mine.addBlockBreakCount();
    					
    					// Possible processing if mined percentage gets too high? Submit reset? 
    					
    					// Reset if the mine runs out of blocks:
    					if ( mine.getRemainingBlockCount() == 0 ) {
    						// submit a manual reset since the mine is empty:
    						mine.manualReset();
    					}
    					
    					break;
    				}
    			}
    		}
    	}
    }

}

package tech.mcprison.prison.spigot.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.block.SpigotBlock;

public class PrisonUtilsListeners
		implements Listener
{
	
	public PrisonUtilsListeners() {
		super();
		
		// Force it to be setup and ready:
		BlockUtils.getInstance();
		
	}
	

    /**
     * <p>This event prevents any block that is within the unbreakableBlockList 
     * from being broken since those blocks are apart of another "process" 
     * such as a decay event.
     * </p>
     * 
     * @param e
     */
    @EventHandler( priority=EventPriority.LOWEST )
    public void unbreakableBlock( BlockBreakEvent e ) {
    	
	    	if ( !e.isCancelled() ) {
	    		PrisonBlock block = SpigotBlock.getSpigotBlock( e.getBlock() ).getPrisonBlock();
	    		
	    		if ( BlockUtils.getInstance().isUnbreakable( block ) ) {
	    			e.setCancelled( true );
	    		}
	    	}
    }

}

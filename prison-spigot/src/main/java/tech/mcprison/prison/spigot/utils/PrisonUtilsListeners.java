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
//	private HashMap<MineTargetBlockKey, SpigotBlock> unbreakableBlockList;
	
	
	public PrisonUtilsListeners() {
		super();
		
		// Force it to be setup and ready:
		BlockUtils.getInstance();
		
//		this.unbreakableBlockList = new HashMap<>();
	}
	
	// Runtime exception for some reasons here.
//    @EventHandler( priority=EventPriority.LOWEST )
//    public void rainbowDecayBlockEvent(PrisonMinesBlockEventEvent e) {
    	
//    	if ( !e.isCancelled() && e.getParameter() != null &&
//    			e.getParameter().startsWith("rainbow-decay" ) ) {
//    		
//    		PrisonUtilsRainbowDecay rainbow = new PrisonUtilsRainbowDecay( 
//    						e, getUnbreakableBlockList());
//    		
//    		PrisonUtilsTask runableTask = new PrisonUtilsTask( rainbow.getTasks() );
//    		
//    		// submit:
//    		runableTask.submit();
//    	}

//    }

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


//	public HashMap<MineTargetBlockKey, SpigotBlock> getUnbreakableBlockList() {
//		return unbreakableBlockList;
//	}

}

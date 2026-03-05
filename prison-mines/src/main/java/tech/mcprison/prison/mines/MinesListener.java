package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerSuffocationEvent;
import tech.mcprison.prison.internal.events.world.PrisonWorldLoadEvent;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.tasks.MineTeleportWarmUpTask;
import tech.mcprison.prison.selection.SelectionCompletedEvent;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Bounds;

/**
 * @author Faizaan A. Datoo, Dylan M. Perks
 */
public class MinesListener {

    @Subscribe
    public void onSelectionComplete(SelectionCompletedEvent e) {
        Bounds bounds = e.getSelection().asBounds();
        String dimensions = bounds.getWidth() + "x" + bounds.getHeight() + "x" + bounds.getLength();
        e.getPlayer().sendMessage("&3Ready. &7Your mine will be &8" + dimensions
            + "&7 blocks. Type /mines create to create it.");
    }

    
    @Subscribe
    public void onWorldLoadListener( PrisonWorldLoadEvent e ) {
	    	
	    	String worldName = e.getWorldName();
	    	PrisonMines.getInstance().getMineManager().assignAvailableWorld( worldName );
	    	
    }
    
    
    /**
     * <p>If a player is suffocating, and if they are within a mine, then based upon the config
     * settings, the play may not experience suffocation, and they may be teleported to
     * the mine's spawn location, or the center of the mine.
     * </p>
     * @param e
     */
    @Subscribe
    public void onPlayerSuffocationListener( PlayerSuffocationEvent e ) {
    	
	    	Player player = e.getPlayer();
	    	Mine mine = PrisonMines.getInstance().findMineLocation( player );
	
	    	if ( mine != null ) {
	    		
	    		// If players can't be suffocated in mines, then cancel the suffocation event:
	    		if ( !Prison.get().getPlatform().getConfigBooleanFalse( "prison-mines.enable-suffocation-in-mines" ) ) {
	    		
	    			e.setCanceled( true );
	    		}    		
	    		
		    		
		    	if ( Prison.get().getPlatform().getConfigBooleanTrue( "prison-mines.tp-to-spawn-on-mine-resets" ) ) {
		
		    		// Submit the teleport task to run in 3 ticks.  This will allow the suffocation
		    		// event to be canceled.  If the player moves then they don't need to be teleported
		    		// so it will be canceled.
		    		MineTeleportWarmUpTask mineTeleportWarmUp = new MineTeleportWarmUpTask( 
		    				player, mine, "spawn", 0.5 );
		    		mineTeleportWarmUp.setMessageSuccess( 
		    				"&7You have been teleported out of the mine to prevent suffocating." );
		    		mineTeleportWarmUp.setMessageFailed( null );
		    		
		    		PrisonTaskSubmitter.runTaskLater( mineTeleportWarmUp, 3 );
		    	}
	
	    	}
	    	else {
	    		player.sendMessage( "&7You cannot be teleported to safety.  Good luck." );
	    		
	    	}
    }
    

}

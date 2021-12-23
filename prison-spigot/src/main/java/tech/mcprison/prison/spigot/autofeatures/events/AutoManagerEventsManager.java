package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;

public abstract class AutoManagerEventsManager
	extends AutoManagerFeatures
	implements PrisonEventManager
{

	public AutoManagerEventsManager() {
		super();
		
	}
	
	public boolean isDisabled( String worldName ) {
		return Prison.get().getPlatform().isWorldExcluded( worldName );
	}
	
    /**
     * <p>If one BlockBreak related event needs to be unregistered, then this function will
     * unregisters all of them that has been registered through the auto features.  If 
     * this function is called by different functions, the results will be the same. If
     * they are ran back-to-back, then only the first call will remove all the Listeners
     * and the other calls will do nothing since the source ArrayList will be emptied 
     * and there would be nothing to remove.
     * </p>
     * 
     */
    @Override
    public void unregisterListeners() {
    	
    	SpigotPrison prison = SpigotPrison.getInstance();
    	
    	int count = 0;
    	while ( prison.getRegisteredBlockListeners().size() > 0 ) {
    		Listener listener = prison.getRegisteredBlockListeners().remove( 0 );
    		
    		if ( listener != null ) {
    			
    			HandlerList.unregisterAll( listener );
    			count++;
    		}
    	}
    	
    	Output.get().logInfo( "AutoManagerEventsManager: unregistered a total of %d event listeners.",
    			count );
	}
    
}

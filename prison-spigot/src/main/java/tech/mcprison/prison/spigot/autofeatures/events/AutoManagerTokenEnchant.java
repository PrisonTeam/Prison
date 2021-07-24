package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerTokenEnchant 
	extends AutoManagerFeatures
	implements PrisonEventLManager {

	public AutoManagerTokenEnchant() {
        super();
        
    }
	
	
	@Override
	public void registerEvents(SpigotPrison spigotPrison ) {
	
		new AutoManagerTokenEnchantEventListener().initialize();
		
	}

	
    public void onTEBlockExplode(TEBlockExplodeEvent e) {
    	if ( !e.isCancelled() ) {
    	    
    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
    public class AutoManagerTokenEnchantEventListener
		extends AutoManagerTokenEnchant
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOW) 
        public void onTEBlockExplode(TEBlockExplodeEvent e) {
        	super.onTEBlockExplode( e );
        }
        
		public void initialize() {
	    	boolean isEventEnabled = isBoolean( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
	    	
	    	if ( !isEventEnabled ) {
	    		return;
	    	}
			
			// Check to see if the class TEBlockExplodeEvent even exists:
			try {
				Output.get().logInfo( "AutoManager: checking if loaded: TokenEnchant" );
				
				Class.forName( "com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent", false, 
								this.getClass().getClassLoader() );
				
				Output.get().logInfo( "AutoManager: Trying to register TokenEnchant" );

				
				String eP = getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
				BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );

				if ( eventPriority != BlockBreakPriority.DISABLED ) {
					
					EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
					
					PluginManager pm = Bukkit.getServer().getPluginManager();

					pm.registerEvent(BlastUseEvent.class, this, ePriority,
							new EventExecutor() {
								public void execute(Listener l, Event e) { 
									((AutoManagerTokenEnchantEventListener)l)
													.onTEBlockExplode((TEBlockExplodeEvent)e);
								}
							},
							SpigotPrison.getInstance());
					
				}
				
			}
			catch ( ClassNotFoundException e ) {
				// TokenEnchant is not loaded... so ignore.
				Output.get().logInfo( "AutoManager: TokenEnchant is not loaded" );
			}
			catch ( Exception e ) {
				Output.get().logInfo( "AutoManager: TokenEnchant failed to load. [%s]", e.getMessage() );
			}
		}

    }
    
	
    @Override
    public void unregisterListeners() {
    	
    	AutoManagerTokenEnchantEventListener listener = null;
    	for ( RegisteredListener lstnr : TEBlockExplodeEvent.getHandlerList().getRegisteredListeners() )
		{
			if ( lstnr.getListener() instanceof AutoManagerTokenEnchantEventListener ) {
				listener = (AutoManagerTokenEnchantEventListener) lstnr.getListener();
				break;
			}
		}

    	if ( listener != null ) {
    		
			HandlerList.unregisterAll( listener );
    	}
    	
    }
    
    @Override
	public void dumpEventListeners() {
    	boolean isEventEnabled = isBoolean( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			
			Class.forName( "com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent", false, 
							this.getClass().getClassLoader() );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"TEBlockExplodeEvent", 
					new SpigotHandlerList( TEBlockExplodeEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				Output.get().logInfo( "" );
				eventDisplay.toLog( LogLevel.DEBUG );
			}
		}
		catch ( ClassNotFoundException e ) {
			// TokenEnchant is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: TokenEnchant failed to load. [%s]", e.getMessage() );
		}
	}
    
	
}

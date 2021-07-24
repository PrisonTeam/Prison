package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.events.PrisonEventLManager;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerPrisonEnchants
	extends AutoManagerFeatures 
	implements PrisonEventLManager {

	public AutoManagerPrisonEnchants() {
		super();
	}


	@Override
	public void registerEvents( SpigotPrison spigotPrison ) {
	
		new AutoManagerExplosiveEventListener().initialize();
		
	}

	
	public class AutoManagerExplosiveEventListener 
		extends AutoManager
		implements Listener {
		
		@EventHandler(priority=EventPriority.LOW) 
		public void onPrisonEnchantsExplosiveEvent(ExplosiveEvent e) {
			super.onPrisonEnchantsExplosiveEvent( e );
		}
		
		public void initialize() {
	    	boolean isEventEnabled = isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents );
	    	
	    	if ( !isEventEnabled ) {
	    		return;
	    	}
			
			// Check to see if the class ExplosiveEvent even exists:
			try {
				Output.get().logInfo( "AutoManager: checking if loaded: PrisonEnchants" );
				
				Class.forName( "me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent", false, 
								this.getClass().getClassLoader() );
				
				Output.get().logInfo( "AutoManager: Trying to register PrisonEnchants" );

				
				String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
				BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );

				if ( eventPriority != BlockBreakPriority.DISABLED ) {
					
					EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
					
					PluginManager pm = Bukkit.getServer().getPluginManager();

					pm.registerEvent(ExplosiveEvent.class, this, ePriority,
							new EventExecutor() {
								public void execute(Listener l, Event e) { 
									((AutoManagerExplosiveEventListener)l)
													.onPrisonEnchantsExplosiveEvent((ExplosiveEvent)e);
								}
							},
							SpigotPrison.getInstance());
					
				}
				
			}
			catch ( ClassNotFoundException e ) {
				// PrisonEnchants is not loaded... so ignore.
				Output.get().logInfo( "AutoManager: PrisonEnchants is not loaded" );
			}
			catch ( Exception e ) {
				Output.get().logInfo( "AutoManager: PrisonEnchants failed to load. [%s]", e.getMessage() );
			}
		}

	}

   
	public class AutoManagerExplosiveEventListenerMonitor 
		extends AutoManager
		implements Listener {
		
	    @EventHandler(priority=EventPriority.MONITOR) 
	    public void onPrisonEnchantsExplosiveEventMonitor(ExplosiveEvent e) {
	    	super.onPrisonEnchantsExplosiveEvent( e );
	    }
	}

    @Override
    public void unregisterListeners() {
    	
    	AutoManagerExplosiveEventListener listener = null;
    	for ( RegisteredListener lstnr : ExplosiveEvent.getHandlerList().getRegisteredListeners() )
		{
			if ( lstnr.getListener() instanceof AutoManagerExplosiveEventListener ) {
				listener = (AutoManagerExplosiveEventListener) lstnr.getListener();
				break;
			}
		}

    	if ( listener != null ) {
    		
			HandlerList.unregisterAll( listener );
    	}
    	
    }
	
	@Override
	public void dumpEventListeners() {
    	boolean isEventEnabled = isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			
			Class.forName( "me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent", false, 
							this.getClass().getClassLoader() );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"ExplosiveEvent", 
					new SpigotHandlerList( ExplosiveEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				Output.get().logInfo( "" );
				eventDisplay.toLog( LogLevel.DEBUG );
			}
		}
		catch ( ClassNotFoundException e ) {
			// PrisonEnchants is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: PrisonEnchants failed to load. [%s]", e.getMessage() );
		}
	}
    

}

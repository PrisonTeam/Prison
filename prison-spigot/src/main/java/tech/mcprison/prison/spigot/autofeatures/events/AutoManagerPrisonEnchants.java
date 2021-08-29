package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import me.pulsi_.prisonenchants.events.PEExplosionEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerPrisonEnchants
	extends AutoManagerEventsManager {

	public AutoManagerPrisonEnchants() {
		super();
	}


	@Override
	public void registerEvents() {
	
		initialize();
		
	}

	
	public class AutoManagerPEExplosiveEventListener 
		extends AutoManagerBlockBreakEvents
		implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEvent(PEExplosionEvent e) {
			
//			me.pulsi_.prisonenchants.events.PEExplosionEvent
			
			genericBlockExplodeEventAutoManager( e );
		}
	}
	
	public class OnBlockBreakPEExplosiveEventListener 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEvent(PEExplosionEvent e) {
			genericBlockExplodeEvent( e );
		}
	}
	
	public class OnBlockBreakPEExplosiveEventListenerMonitor
		extends OnBlockBreakEventListener
		implements Listener {
		
		@EventHandler(priority=EventPriority.MONITOR) 
		public void onPrisonEnchantsExplosiveEvent(PEExplosionEvent e) {
			genericBlockExplodeEventMonitor( e );
		}
	}

	@Override
	public void initialize() {
		boolean isEventEnabled = isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents );
		
		if ( !isEventEnabled ) {
			return;
		}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			Output.get().logInfo( "AutoManager: checking if loaded: Pulsi_'s PrisonEnchants" );
			
			Class.forName( "me.pulsi_.prisonenchants.events.PEExplosionEvent", false, 
					this.getClass().getClassLoader() );
			
			Output.get().logInfo( "AutoManager: Trying to register Pulsi_'s PrisonEnchants" );
			
			
			String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
			BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
			
			if ( eventPriority != BlockBreakPriority.DISABLED ) {
				
				EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
				
				
				OnBlockBreakPEExplosiveEventListenerMonitor normalListenerMonitor = 
												new OnBlockBreakPEExplosiveEventListenerMonitor();
				
				
				SpigotPrison prison = SpigotPrison.getInstance();
				
				
				PluginManager pm = Bukkit.getServer().getPluginManager();
				
				if ( eventPriority != BlockBreakPriority.MONITOR ) {
					
					if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
						
						AutoManagerPEExplosiveEventListener autoManagerlListener = 
								new AutoManagerPEExplosiveEventListener();
						
						pm.registerEvent(PEExplosionEvent.class, autoManagerlListener, ePriority,
								new EventExecutor() {
							public void execute(Listener l, Event e) { 
								((AutoManagerPEExplosiveEventListener)l)
								.onPrisonEnchantsExplosiveEvent((PEExplosionEvent)e);
							}
						},
								prison);
						prison.getRegisteredBlockListeners().add( autoManagerlListener );
					}
					
					OnBlockBreakPEExplosiveEventListener normalListener = 
							new OnBlockBreakPEExplosiveEventListener();
					
					pm.registerEvent(PEExplosionEvent.class, normalListener, ePriority,
							new EventExecutor() {
						public void execute(Listener l, Event e) { 
							((OnBlockBreakPEExplosiveEventListener)l)
							.onPrisonEnchantsExplosiveEvent((PEExplosionEvent)e);
						}
					},
							prison);
					prison.getRegisteredBlockListeners().add( normalListener );
				}
				
				pm.registerEvent(PEExplosionEvent.class, normalListenerMonitor, EventPriority.MONITOR,
						new EventExecutor() {
					public void execute(Listener l, Event e) { 
						((OnBlockBreakPEExplosiveEventListenerMonitor)l)
										.onPrisonEnchantsExplosiveEvent((PEExplosionEvent)e);
					}
				},
				prison);
				prison.getRegisteredBlockListeners().add( normalListenerMonitor );

				
			}
			
		}
		catch ( ClassNotFoundException e ) {
			// PrisonEnchants is not loaded... so ignore.
			Output.get().logInfo( "AutoManager: Pulsi_'s PrisonEnchants is not loaded" );
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: Pulsi_'s PrisonEnchants failed to load. [%s]", e.getMessage() );
		}
	}

   

    @Override
    public void unregisterListeners() {
    	
    	super.unregisterListeners();
    	
    }
	
	@Override
	public void dumpEventListeners() {
		
		StringBuilder sb = new StringBuilder();
		
		dumpEventListeners( sb );
		
		if ( sb.length() > 0 ) {

			
			for ( String line : sb.toString().split( "\n" ) ) {
				
				Output.get().logInfo( line );
			}
		}
		
	}
	
	
	@Override
	public void dumpEventListeners( StringBuilder sb ) {
    	boolean isEventEnabled = isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			
			Class.forName( "me.pulsi_.prisonenchants.events.PEExplosionEvent", false, 
							this.getClass().getClassLoader() );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"Pulsi_'s PEExplosionEvent", 
					new SpigotHandlerList( PEExplosionEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
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

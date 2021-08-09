package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import me.pulsi_.prisonenchants.events.PrisonExplosionEvent;
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

	
	public class AutoManagerExplosiveEventListener 
		extends AutoManagerBlockBreakEvents
		implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEvent(PrisonExplosionEvent e) {
			genericBlockExplodeEventAutoManager( e );
		}
	}
	
	public class OnBlockBreakExplosiveEventListener 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEvent(PrisonExplosionEvent e) {
			genericBlockExplodeEvent( e );
		}
	}
	
	public class OnBlockBreakExplosiveEventListenerMonitor
		extends OnBlockBreakEventListener
		implements Listener {
		
		@EventHandler(priority=EventPriority.MONITOR) 
		public void onPrisonEnchantsExplosiveEvent(PrisonExplosionEvent e) {
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
			Output.get().logInfo( "AutoManager: checking if loaded: PrisonEnchants" );
			
			Class.forName( "me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent", false, 
					this.getClass().getClassLoader() );
			
			Output.get().logInfo( "AutoManager: Trying to register PrisonEnchants" );
			
			
			String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
			BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
			
			if ( eventPriority != BlockBreakPriority.DISABLED ) {
				
				EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
				
				
				OnBlockBreakExplosiveEventListener normalListener = 
												new OnBlockBreakExplosiveEventListener();
				OnBlockBreakExplosiveEventListenerMonitor normalListenerMonitor = 
												new OnBlockBreakExplosiveEventListenerMonitor();
				
				
				SpigotPrison prison = SpigotPrison.getInstance();
				
				
				PluginManager pm = Bukkit.getServer().getPluginManager();
				
				if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
					
					AutoManagerExplosiveEventListener autoManagerlListener = 
							new AutoManagerExplosiveEventListener();

					pm.registerEvent(PrisonExplosionEvent.class, autoManagerlListener, ePriority,
							new EventExecutor() {
						public void execute(Listener l, Event e) { 
							((AutoManagerExplosiveEventListener)l)
										.onPrisonEnchantsExplosiveEvent((PrisonExplosionEvent)e);
						}
					},
					prison);
					prison.getRegisteredBlockListeners().add( autoManagerlListener );
				}

				
				pm.registerEvent(PrisonExplosionEvent.class, normalListener, ePriority,
						new EventExecutor() {
					public void execute(Listener l, Event e) { 
						((OnBlockBreakExplosiveEventListener)l)
										.onPrisonEnchantsExplosiveEvent((PrisonExplosionEvent)e);
					}
				},
				prison);
				prison.getRegisteredBlockListeners().add( normalListener );
				
				pm.registerEvent(PrisonExplosionEvent.class, normalListenerMonitor, EventPriority.MONITOR,
						new EventExecutor() {
					public void execute(Listener l, Event e) { 
						((OnBlockBreakExplosiveEventListenerMonitor)l)
										.onPrisonEnchantsExplosiveEvent((PrisonExplosionEvent)e);
					}
				},
				prison);
				prison.getRegisteredBlockListeners().add( normalListenerMonitor );

				
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
			
			Class.forName( "me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent", false, 
							this.getClass().getClassLoader() );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"ExplosiveEvent", 
					new SpigotHandlerList( PrisonExplosionEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
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

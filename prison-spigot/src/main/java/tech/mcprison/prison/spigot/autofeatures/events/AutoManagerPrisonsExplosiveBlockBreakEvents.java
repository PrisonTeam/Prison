package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerPrisonsExplosiveBlockBreakEvents
	extends AutoManagerEventsManager {


	public AutoManagerPrisonsExplosiveBlockBreakEvents() {
		super();
	}


	@Override
	public void registerEvents() {
	
		initialize();
		
	}

	
	public class AutoManagerExplosiveBlockBreakEventListener 
		extends AutoManagerBlockBreakEvents
		implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonsExplosiveBlockBreakEvent(ExplosiveBlockBreakEvent e) {
			
//			me.pulsi_.prisonenchants.events.PEExplosionEvent
			
			genericBlockExplodeEventAutoManager( e );
		}
	}
	
	public class OnBlockBreakExplosiveBlockBreakEventListener 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonExplosiveBlockBreakEvent(ExplosiveBlockBreakEvent e) {
			genericBlockExplodeEvent( e );
		}
	}
	
	public class OnBlockBreakExplosiveBlockBreakEventListenerMonitor
		extends OnBlockBreakEventListener
		implements Listener {
		
		@EventHandler(priority=EventPriority.MONITOR) 
		public void onPrisonExplosiveBlockBreakEventMonitor(ExplosiveBlockBreakEvent e) {
			genericBlockExplodeEventMonitor( e );
		}
	}

	@Override
	public void initialize() {
		
		String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

		if ( !isEventEnabled ) {
			return;
		}
		
		try {
			
			Output.get().logInfo( "AutoManager: Trying to register ExplosiveBlockBreakEvent Listener" );
			
			
			BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
			
			if ( eventPriority != BlockBreakPriority.DISABLED ) {
				
				EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
				
				
				OnBlockBreakExplosiveBlockBreakEventListenerMonitor normalListenerMonitor = 
												new OnBlockBreakExplosiveBlockBreakEventListenerMonitor();
				
				
				SpigotPrison prison = SpigotPrison.getInstance();
				
				
				PluginManager pm = Bukkit.getServer().getPluginManager();
				
				if ( eventPriority != BlockBreakPriority.MONITOR ) {
					
					if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
						
						AutoManagerExplosiveBlockBreakEventListener autoManagerlListener = 
								new AutoManagerExplosiveBlockBreakEventListener();
						
						pm.registerEvent(ExplosiveBlockBreakEvent.class, autoManagerlListener, ePriority,
								new EventExecutor() {
							public void execute(Listener l, Event e) { 
								((AutoManagerExplosiveBlockBreakEventListener)l)
								.onPrisonsExplosiveBlockBreakEvent((ExplosiveBlockBreakEvent)e);
							}
						},
								prison);
						prison.getRegisteredBlockListeners().add( autoManagerlListener );
					}
					else if ( isBoolean( AutoFeatures.normalDrop ) ) {
						
						OnBlockBreakExplosiveBlockBreakEventListener normalListener = 
								new OnBlockBreakExplosiveBlockBreakEventListener();
						
						pm.registerEvent(ExplosiveBlockBreakEvent.class, normalListener, ePriority,
								new EventExecutor() {
							public void execute(Listener l, Event e) { 
								((OnBlockBreakExplosiveBlockBreakEventListener)l)
								.onPrisonExplosiveBlockBreakEvent((ExplosiveBlockBreakEvent)e);
							}
						},
								prison);
						prison.getRegisteredBlockListeners().add( normalListener );
					}
					
				}
				
				pm.registerEvent(ExplosiveBlockBreakEvent.class, normalListenerMonitor, EventPriority.MONITOR,
						new EventExecutor() {
					public void execute(Listener l, Event e) { 
						((OnBlockBreakExplosiveBlockBreakEventListenerMonitor)l)
										.onPrisonExplosiveBlockBreakEventMonitor((ExplosiveBlockBreakEvent)e);
					}
				},
				prison);
				prison.getRegisteredBlockListeners().add( normalListenerMonitor );

				
			}
			
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: Prison's own ExplosiveBlockBreakEvent failed to load. [%s]", e.getMessage() );
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

		String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			
			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"ExplosiveBlockBreakEvent", 
					new SpigotHandlerList( ExplosiveBlockBreakEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
			}
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: PrisonEnchants failed to load. [%s]", e.getMessage() );
		}
	}
    
}

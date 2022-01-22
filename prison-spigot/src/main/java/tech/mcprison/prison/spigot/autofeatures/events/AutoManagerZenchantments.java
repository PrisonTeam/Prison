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
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;
import zedly.zenchantments.BlockShredEvent;

public class AutoManagerZenchantments
	extends AutoManagerEventsManager {

	public AutoManagerZenchantments() {
		super();
	}
	
	
	@Override
	public void registerEvents() {
	
		initialize();

	}

	  
    public class AutoManagerBlockShredEventListener
	    extends AutoManagerBlockBreakEvents
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockShredBreak(BlockShredEvent e) {
			if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ) {
				return;
			}
    		genericBlockEventAutoManager( e );
    	}
    }
 
    public class OnBlockBreakBlockShredEventListener 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onBlockShredBreak(BlockShredEvent e) {
			if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ) {
				return;
			}
			genericBlockEvent( e );
		}
	}
    
    public class OnBlockBreakBlockShredEventListenerMonitor 
    extends OnBlockBreakEventListener
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.MONITOR) 
    	public void onBlockShredBreakMonitor(BlockShredEvent e) {
    		if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ) {
    			return;
    		}
    		genericBlockEventMonitor( e );
    	}
    }
    
    
    @Override
    public void initialize() {
    	
    	String eP = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( !isEventEnabled ) {
    		return;
    	}
    	
    	// Check to see if the class BlastUseEvent even exists:
    	try {
    		Output.get().logInfo( "AutoManager: checking if loaded: Zenchantments" );
    		
    		Class.forName( "zedly.zenchantments.BlockShredEvent", false, 
    				this.getClass().getClassLoader() );
    		
    		Output.get().logInfo( "AutoManager: Trying to register Zenchantments" );
    		
    		
    		BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
    		
    		if ( eventPriority != BlockBreakPriority.DISABLED ) {
    			
    			EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
    			
    			
    			OnBlockBreakBlockShredEventListenerMonitor normalListenerMonitor = 
												new OnBlockBreakBlockShredEventListenerMonitor();
    			
    			
    			SpigotPrison prison = SpigotPrison.getInstance();
    			
    			PluginManager pm = Bukkit.getServer().getPluginManager();
    			
    			if ( eventPriority != BlockBreakPriority.MONITOR ) {
    				
    				if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
    					
    					AutoManagerBlockShredEventListener autoManagerlListener = 
    							new AutoManagerBlockShredEventListener();
    					
    					pm.registerEvent(BlockShredEvent.class, autoManagerlListener, ePriority,
    							new EventExecutor() {
    						public void execute(Listener l, Event e) { 
    							if ( l instanceof OnBlockBreakBlockShredEventListenerMonitor && 
    									e instanceof BlockShredEvent ) {
    								OnBlockBreakBlockShredEventListenerMonitor lmon = 
    										(OnBlockBreakBlockShredEventListenerMonitor) l;
    								BlockShredEvent event = (BlockShredEvent) e;
    								lmon.onBlockShredBreakMonitor( event );
    							}
    						}
    					},
    							prison);
    					prison.getRegisteredBlockListeners().add( autoManagerlListener );
    				}
    				else if ( isBoolean( AutoFeatures.normalDrop ) ) {
    					
    					OnBlockBreakBlockShredEventListener normalListener = 
    							new OnBlockBreakBlockShredEventListener();
    					
    					pm.registerEvent(BlockShredEvent.class, normalListener, ePriority,
    							new EventExecutor() {
    						public void execute(Listener l, Event e) { 
    							if ( l instanceof OnBlockBreakBlockShredEventListenerMonitor && 
    									e instanceof BlockShredEvent ) {
    								OnBlockBreakBlockShredEventListenerMonitor lmon = 
    										(OnBlockBreakBlockShredEventListenerMonitor) l;
    								BlockShredEvent event = (BlockShredEvent) e;
    								lmon.onBlockShredBreakMonitor( event );
    							}
    						}
    					},
    							prison);
    					prison.getRegisteredBlockListeners().add( normalListener );
    				}
    				
    			}
    			else {
    				
    				pm.registerEvent(BlockShredEvent.class, normalListenerMonitor, EventPriority.MONITOR,
    						new EventExecutor() {
    					public void execute(Listener l, Event e) { 
    						if ( l instanceof OnBlockBreakBlockShredEventListenerMonitor && 
    								e instanceof BlockShredEvent ) {
    							OnBlockBreakBlockShredEventListenerMonitor lmon = 
    									(OnBlockBreakBlockShredEventListenerMonitor) l;
    							BlockShredEvent event = (BlockShredEvent) e;
    							lmon.onBlockShredBreakMonitor( event );
    						}
    					}
    				},
    						prison);
    				prison.getRegisteredBlockListeners().add( normalListenerMonitor );
    			}
    			
    		}
    		
    	}
    	catch ( ClassNotFoundException e ) {
    		// Zenchantments is not loaded... so ignore.
    		Output.get().logInfo( "AutoManager: Zenchantments is not loaded" );
    	}
    	catch ( Exception e ) {
    		Output.get().logInfo( "AutoManager: Zenchantments failed to load. [%s]", e.getMessage() );
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
 
    	String eP = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
    	
    	// Check to see if the class BlastUseEvent even exists:
    	try {
    		
    		Class.forName( "zedly.zenchantments.BlockShredEvent", false, 
    				this.getClass().getClassLoader() );
    		
    		
    		ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
    				"BlockShredEvent", 
    				new SpigotHandlerList( BlockShredEvent.getHandlerList()) );
    		
    		if ( eventDisplay != null ) {
    			sb.append( eventDisplay.toStringBuilder() );
    			sb.append( "NOTE: Zenchantments uses the same HandlerList as BlockBreakEvent so " +
    					"listeners are combined due to this bug.\n" );
    			sb.append( "\n" );
    		}
    	}
    	catch ( ClassNotFoundException e ) {
    		// Zenchantments is not loaded... so ignore.
    	}
    	catch ( Exception e ) {
    		Output.get().logInfo( "AutoManager: zenchantments failed to load. [%s]", e.getMessage() );
    	}
    }

}

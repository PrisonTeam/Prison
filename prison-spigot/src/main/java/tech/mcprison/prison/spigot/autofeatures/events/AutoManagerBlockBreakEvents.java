package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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


public class AutoManagerBlockBreakEvents 
	extends AutoManagerEventsManager
	{
	
	public AutoManagerBlockBreakEvents() {
        super();
   
    }

	/**
	 * <p>When a listener is registered within prison's auto manager, the events
	 * are tracked internally and can be unregistered later.  This function does not 
	 * make it obvious the instantiated object is "stored", but it is.
	 * </p>
	 * 
	 * <p>For more info on the storage of these registered events, please see:
	 * </p>
	 * 
	 * <pre>SpigotPrison.getRegisteredBlockListeners()</pre>
	 * 
	 */
	@Override
	public void registerEvents() {
		
		initialize();
		
		// Prison's own internal event and listener:
		new AutoManagerPrisonsExplosiveBlockBreakEvents().registerEvents();
		
		new AutoManagerCrazyEnchants().registerEvents();
		new AutoManagerPrisonEnchants().registerEvents();
		new AutoManagerTokenEnchant().registerEvents();
		new AutoManagerZenchantments().registerEvents();
		
	}
	
	
    public class AutoManagerBlockBreakEventListener 
	    extends AutoManagerBlockBreakEvents
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onBlockBreak(BlockBreakEvent e) {
			genericBlockEventAutoManager( e );
		}
	}
    
    public class OnBlockBreakEventListenerNormal
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		genericBlockEvent( e );
    	}
    }
    
	public class OnBlockBreakEventListenerNormalMonitor
		extends OnBlockBreakEventListener
		implements Listener {
			
		@EventHandler(priority=EventPriority.MONITOR) 
		public void onBlockBreak(BlockBreakEvent e) {
			genericBlockEventMonitor( e );
		}
	}

    public void initialize() {
    	
    	// Check to see if the class BlockBreakEvent even exists:
    	try {
    		
    		Output.get().logInfo( "AutoManager: Trying to register BlockBreakEvent" );
    		
    		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
    		
    		if ( eventPriority != BlockBreakPriority.DISABLED ) {
    			
    			EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
    			
    			
				
    			OnBlockBreakEventListenerNormalMonitor normalListenerMonitor = 
												new OnBlockBreakEventListenerNormalMonitor();
				

    			SpigotPrison prison = SpigotPrison.getInstance();
    			
    			PluginManager pm = Bukkit.getServer().getPluginManager();
    			
    			if ( eventPriority != BlockBreakPriority.MONITOR ) {
    				
    				if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
    					
    					AutoManagerBlockBreakEventListener autoManagerlListener = 
    							new AutoManagerBlockBreakEventListener();
    					
    					pm.registerEvent(BlockBreakEvent.class, autoManagerlListener, ePriority,
    							new EventExecutor() {
    						public void execute(Listener l, Event e) {
    							if ( l instanceof AutoManagerBlockBreakEventListener && 
    									e instanceof BlockBreakEvent ) {
    								((AutoManagerBlockBreakEventListener)l)
    								.onBlockBreak((BlockBreakEvent)e);
    							}
    						}
    					},
    							prison);
    					prison.getRegisteredBlockListeners().add( autoManagerlListener );
    				}
    				
    				OnBlockBreakEventListenerNormal normalListener = 
    						new OnBlockBreakEventListenerNormal();
    				
    				pm.registerEvent(BlockBreakEvent.class, normalListener, ePriority,
    						new EventExecutor() {
    					public void execute(Listener l, Event e) {
    						if ( l instanceof OnBlockBreakEventListenerNormal && 
    								e instanceof BlockBreakEvent ) {
    							((OnBlockBreakEventListenerNormal)l)
    							.onBlockBreak((BlockBreakEvent)e);
    						}
    					}
    				},
    						prison);
    				prison.getRegisteredBlockListeners().add( normalListener );
    				
    			}
    			
    			
    			pm.registerEvent(BlockBreakEvent.class, normalListenerMonitor, EventPriority.MONITOR,
    					new EventExecutor() {
    				public void execute(Listener l, Event e) { 
    					if ( l instanceof OnBlockBreakEventListenerNormalMonitor && 
        						 e instanceof BlockBreakEvent ) {
    						((OnBlockBreakEventListenerNormalMonitor)l)
    										.onBlockBreak((BlockBreakEvent)e);
    					}
    				}
    			},
    			prison);
    			prison.getRegisteredBlockListeners().add( normalListenerMonitor );
    			
    		}
    		
    	}
    	catch ( Exception e ) {
    		Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
    	}
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
		
		// Check to see if the class BlockBreakEvent even exists:
		try {

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"BlockBreakEvent", 
					new SpigotHandlerList( BlockBreakEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
			}
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
		}
	}
    
}

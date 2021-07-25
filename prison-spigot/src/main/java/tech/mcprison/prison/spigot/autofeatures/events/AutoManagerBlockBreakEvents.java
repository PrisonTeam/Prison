package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;


public class AutoManagerBlockBreakEvents 
	extends AutoManagerFeatures
	implements PrisonEventManager
	{
	
	public AutoManagerBlockBreakEvents() {
        super();
   
    }

	
	@Override
	public void registerEvents() {
	
		
		initialize();
		
		
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
    			
    			
				
				
    			OnBlockBreakEventListenerNormal normalListener = 
												new OnBlockBreakEventListenerNormal();
    			OnBlockBreakEventListenerNormalMonitor normalListenerMonitor = 
												new OnBlockBreakEventListenerNormalMonitor();
				

    			SpigotPrison prison = SpigotPrison.getInstance();
    			
    			PluginManager pm = Bukkit.getServer().getPluginManager();
    			
    			if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
    				
    				AutoManagerBlockBreakEventListener autoManagerlListener = 
    											new AutoManagerBlockBreakEventListener();
    				
    				pm.registerEvent(BlockBreakEvent.class, autoManagerlListener, ePriority,
    						new EventExecutor() {
    					public void execute(Listener l, Event e) { 
    						((AutoManagerBlockBreakEventListener)l)
    									.onBlockBreak((BlockBreakEvent)e);
    					}
    				},
					prison);
    				prison.getRegisteredBlockListeners().add( autoManagerlListener );
    			}
    			
    			pm.registerEvent(BlockBreakEvent.class, normalListener, ePriority,
    					new EventExecutor() {
    				public void execute(Listener l, Event e) { 
    					((OnBlockBreakEventListenerNormal)l)
    								.onBlockBreak((BlockBreakEvent)e);
    				}
    			},
    			prison);
    			prison.getRegisteredBlockListeners().add( normalListener );

    			
    			pm.registerEvent(BlockBreakEvent.class, normalListenerMonitor, EventPriority.MONITOR,
    					new EventExecutor() {
    				public void execute(Listener l, Event e) { 
    					((OnBlockBreakEventListenerNormalMonitor)l)
    								.onBlockBreak((BlockBreakEvent)e);
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
    	
    	SpigotPrison prison = SpigotPrison.getInstance();
    	
    	while ( prison.getRegisteredBlockListeners().size() > 0 ) {
    		Listener listener = prison.getRegisteredBlockListeners().remove( 0 );
    		
    		if ( listener != null ) {
    			
    			HandlerList.unregisterAll( listener );
    		}
    	}
    	
    	
//    	AutoManagerBlockBreakEventListener listener = null;
//    	for ( RegisteredListener lstnr : BlockBreakEvent.getHandlerList().getRegisteredListeners() )
//		{
//			if ( lstnr.getListener() instanceof AutoManagerBlockBreakEventListener ) {
//				listener = (AutoManagerBlockBreakEventListener) lstnr.getListener();
//				break;
//			}
//		}
//
//    	if ( listener != null ) {
//    		
//			HandlerList.unregisterAll( listener );
//    	}
//    	
//		
//		new AutoManagerCrazyEnchants().unregisterListeners();
//		new AutoManagerPrisonEnchants().unregisterListeners();
//		new AutoManagerTokenEnchant().unregisterListeners();
//		new AutoManagerZenchantments().unregisterListeners();
    }

	
    @Override
	public void dumpEventListeners() {
		
		// Check to see if the class BlockBreakEvent even exists:
		try {

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					"BlockBreakEvent", 
					new SpigotHandlerList( BlockBreakEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				Output.get().logInfo( "" );
				eventDisplay.toLog( LogLevel.DEBUG );
			}
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
		}
	}
    
//    /**
//     * <p>Had to set to a EventPriorty.LOW so other plugins can work with the blocks.
//     * The other plugins were EZBlock & SellAll. This function was canceling the
//     * event after it auto picked it up, so the other plugins were not registering
//     * the blocks as being broken.
//     * </p>
//     * 
//     * @param e
//     */
//    @EventHandler(priority=EventPriority.LOW) 
//    public void onBlockBreak(BlockBreakEvent e) {
//    	
//    	if ( !e.isCancelled() && e.getBlock().getType() != null) {
//    		
//    		// Get the player objects: Spigot and the Prison player:
//    		Player p = e.getPlayer();
//    		// SpigotPlayer player = new SpigotPlayer( p );
//    		
//    		// Validate that the event is happening within a mine since the
//    		// onBlockBreak events here are only valid within the mines:
//    		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
//    		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
//    			PrisonMines mineManager = (PrisonMines) mmOptional.get();
//    			
//    			for ( Mine mine : mineManager.getMines() ) {
//    				SpigotBlock block = new SpigotBlock(e.getBlock());
//    				if ( mine.isInMine( block.getLocation() ) ) {
//    					
//    					applyAutoEvents( e, mine, p );
//    					break;
//    				}
//    			}
//    		}
//    	}
//    }

//    /**
//     * <p>The optimized logic on how an BlockBreakEvent is handled is within the OnBlockBreakEventListener
//     * class and optimizes mine reuse.
//     * </p>
//     *
//     */
//    public void onBlockBreak(BlockBreakEvent e) {
//
//    	// NOTE: If autoManager is turned off, then process only the blockEvents:
//    	genericBlockEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
//    }
//    
//    /**
//     * <p>The use of e.getBlock != null is added to "use" the BlockShredEvent to prevent
//     * the complier from falsely triggering a Not Used warning.
//     * </p>
//     */
//    public void onBlockShredBreak(BlockShredEvent e) {
//
//    	// NOTE: If autoManager is turned off, then process only the blockEvents:
//    	genericBlockEventAutoManager( e, !( isBoolean(AutoFeatures.isAutoManagerEnabled) && e.getBlock() != null ) );
//    }
    
//    @EventHandler(priority=EventPriority.LOW) 
//    public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
//    	if ( !e.isCancelled() ) {
//    	    
//    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
//    	}
//    }
//    
    
//    
//    public void onCrazyEnchantsBlockExplode( Object obj ) {
//    	BlastUseEvent e = (BlastUseEvent) obj;
//    	if ( !e.isCancelled() ) {
//
//    		// NOTE: If autoManager is turned off, then process only the blockEvents:
//    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
//    	}
//    }
//    
//    
//    
//    public void onPrisonEnchantsExplosiveEvent( Object obj ) {
//    	ExplosiveEvent e = (ExplosiveEvent) obj;
//    	if ( !e.isCancelled() ) {
//    		
//    		// NOTE: If autoManager is turned off, then process only the blockEvents:
//    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
//    	}
//    }
//    
    
}

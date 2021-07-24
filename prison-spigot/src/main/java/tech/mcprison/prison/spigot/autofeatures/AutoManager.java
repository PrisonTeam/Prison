package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
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
import zedly.zenchantments.BlockShredEvent;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends AutoManagerFeatures
	implements PrisonEventLManager
	{
	
	public AutoManager() {
        super();
   
        // NOTE: Set in SpigotPrison.
//        // Save this instance within the SpigotPrison instance so it can be accessed
//        // from non-event listeners:
//        SpigotPrison.getInstance().setAutoFeatures( this );
    }

	
	@Override
	public void registerEvents(SpigotPrison spigotPrison ) {
	
		
		new AutoManagerBlockBreakEventListener().initialize();
		
		new AutoManagerCrazyEnchants().registerEvents( spigotPrison );
		new AutoManagerPrisonEnchants().registerEvents( spigotPrison );
		new AutoManagerTokenEnchant().registerEvents( spigotPrison );
		new AutoManagerZenchantments().registerEvents( spigotPrison );
		
		
	}
	
	
    public class AutoManagerBlockBreakEventListener 
	    extends AutoManager
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onBlockBreak(BlockBreakEvent e) {
			super.onBlockBreak( e );
		}
		
		public void initialize() {
			
			// Check to see if the class BlockBreakEvent even exists:
			try {
				
				Output.get().logInfo( "AutoManager: Trying to register BlockBreakEvent" );
				
				String eP = getMessage( AutoFeatures.blockBreakEventPriority );
				BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );

				if ( eventPriority != BlockBreakPriority.DISABLED ) {
					
					EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
					
					PluginManager pm = Bukkit.getServer().getPluginManager();

					pm.registerEvent(BlockBreakEvent.class, this, ePriority,
							new EventExecutor() {
								public void execute(Listener l, Event e) { 
									((AutoManagerBlockBreakEventListener)l)
													.onBlockBreak((BlockBreakEvent)e);
								}
							},
							SpigotPrison.getInstance());
					
				}
				
			}
			catch ( Exception e ) {
				Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
			}
		}

	}

    @Override
    public void unregisterListeners() {
    	
    	AutoManagerBlockBreakEventListener listener = null;
    	for ( RegisteredListener lstnr : BlockBreakEvent.getHandlerList().getRegisteredListeners() )
		{
			if ( lstnr.getListener() instanceof AutoManagerBlockBreakEventListener ) {
				listener = (AutoManagerBlockBreakEventListener) lstnr.getListener();
				break;
			}
		}

    	if ( listener != null ) {
    		
			HandlerList.unregisterAll( listener );
    	}
    	
		
		new AutoManagerCrazyEnchants().unregisterListeners();
		new AutoManagerPrisonEnchants().unregisterListeners();
		new AutoManagerTokenEnchant().unregisterListeners();
		new AutoManagerZenchantments().unregisterListeners();
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

    /**
     * <p>The optimized logic on how an BlockBreakEvent is handled is within the OnBlockBreakEventListener
     * class and optimizes mine reuse.
     * </p>
     *
     */
    public void onBlockBreak(BlockBreakEvent e) {

    	// NOTE: If autoManager is turned off, then process only the blockEvents:
    	genericBlockEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    }
    
    /**
     * <p>The use of e.getBlock != null is added to "use" the BlockShredEvent to prevent
     * the complier from falsely triggering a Not Used warning.
     * </p>
     */
    public void onBlockShredBreak(BlockShredEvent e) {

    	// NOTE: If autoManager is turned off, then process only the blockEvents:
    	genericBlockEventAutoManager( e, !( isBoolean(AutoFeatures.isAutoManagerEnabled) && e.getBlock() != null ) );
    }
    
//    @EventHandler(priority=EventPriority.LOW) 
//    public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
//    	if ( !e.isCancelled() ) {
//    	    
//    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
//    	}
//    }
//    
    
    
    public void onCrazyEnchantsBlockExplode( Object obj ) {
    	BlastUseEvent e = (BlastUseEvent) obj;
    	if ( !e.isCancelled() ) {

    		// NOTE: If autoManager is turned off, then process only the blockEvents:
    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
    
    public void onPrisonEnchantsExplosiveEvent( Object obj ) {
    	ExplosiveEvent e = (ExplosiveEvent) obj;
    	if ( !e.isCancelled() ) {
    		
    		// NOTE: If autoManager is turned off, then process only the blockEvents:
    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
}

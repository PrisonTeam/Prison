package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import zedly.zenchantments.BlockShredEvent;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends AutoManagerFeatures
	{
	
	public AutoManager() {
        super();
        
        // Save this instance within the SpigotPrison instance so it can be accessed
        // from non-event listeners:
        SpigotPrison.getInstance().setAutoFeatures( this );
    }

	
	public void registerBlockBreakEvents(SpigotPrison spigotPrison ) {
	
		
		String bbePriority = getMessage( AutoFeatures.blockBreakEventPriority );
		BlockBreakPriority blockBreakPriority = BlockBreakPriority.fromString( bbePriority );
		
		switch ( blockBreakPriority )
		{
			case LOWEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerEventListenerLowest(), spigotPrison);
				break;
				
			case LOW:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerEventListenerLow(), spigotPrison);
				break;
				
			case NORMAL:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerEventListenerNormal(), spigotPrison);
				break;
				
			case HIGH:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerEventListenerHigh(), spigotPrison);
				break;
				
			case HIGHEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerEventListenerHighest(), spigotPrison);
				break;

			case DISABLED:
				Output.get().logInfo( "AutoManager BlockBreakEvent handling has been DISABLED." );
				break;

			default:
				break;
		}
		
		
    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( isCEBlockExplodeEnabled ) {
    		AutoManagerCrazyEnchants amCE = new AutoManagerCrazyEnchants();
    		amCE.registerBlastUseEvents( spigotPrison );
    	}

		
		
		
		String zbsPriority = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
		BlockBreakPriority blockShredPriority = BlockBreakPriority.fromString( zbsPriority );
		
		if ( blockShredPriority != BlockBreakPriority.DISABLED ) {
			
			// Always register the monitor event:
			Bukkit.getPluginManager().registerEvents( 
					new AutoManagerBlockShredEventListenerMonitor(), spigotPrison);
		}

		
		switch ( blockShredPriority )
		{
			case LOWEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerLowest(), spigotPrison);
				break;
				
			case LOW:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerLow(), spigotPrison);
				break;
				
			case NORMAL:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerNormal(), spigotPrison);
				break;
				
			case HIGH:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerHigh(), spigotPrison);
				break;
				
			case HIGHEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerHighest(), spigotPrison);
				break;

			case DISABLED:
				Output.get().logInfo( "AutoManager Zenchantments BlockShredEvent handling has been DISABLED." );
				break;

			default:
				break;
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
     * <p>Had to set to a EventPriorty.LOW so other plugins can work with the blocks.
     * The other plugins were EZBlock & SellAll. This function was canceling the
     * event after it auto picked it up, so the other plugins were not registering
     * the blocks as being broken.
     * </p>
     * 
     * 
     */
    public void onBlockBreak(BlockBreakEvent e) {

    	genericBlockEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    }
    
    /**
     * <p>The use of e.getBlock != null is added to "use" the BlockShredEvent to prevent
     * the complier from falsely triggering a Not Used warning.
     * </p>
     */
    public void onBlockShredBreak(BlockShredEvent e) {

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
    
    
    public void onCrazyEnchantsBlockExplodeLow( Object obj ) {
    	BlastUseEvent e = (BlastUseEvent) obj;
    	if ( !e.isCancelled() ) {
    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
    
    public class AutoManagerEventListenerLowest 
		extends AutoManager
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onBlockBreak(BlockBreakEvent e) {
        	super.onBlockBreak( e );
        }
        
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onBlockShredBreak(BlockShredEvent e) {
        	super.onBlockShredBreak( e );
        }
    }
    
    public class AutoManagerEventListenerLow 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerEventListenerNormal 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerEventListenerHigh 
	    extends AutoManager
	    implements Listener {
	    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerEventListenerHighest 
    extends AutoManager
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    
    
    
    
    public class AutoManagerBlockShredEventListenerMonitor 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.MONITOR) 
    	public void onBlockShredBreakMonitor(BlockShredEvent e) {
    		super.genericBlockEventMonitor( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerLowest 
		extends AutoManager
		implements Listener {
        
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onBlockShredBreak(BlockShredEvent e) {
        	super.onBlockShredBreak( e );
        }
    }
    
    public class AutoManagerBlockShredEventListenerLow 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerNormal 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerHigh 
	    extends AutoManager
	    implements Listener {
	    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerHighest 
    extends AutoManager
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    
    
}

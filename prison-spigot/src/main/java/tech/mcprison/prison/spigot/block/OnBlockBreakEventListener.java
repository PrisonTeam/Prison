package tech.mcprison.prison.spigot.block;

import org.bukkit.event.block.BlockBreakEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.AutoManager;
import zedly.zenchantments.BlockShredEvent;

/**
 * <p>This is a pivotal class that "monitors" onBlockBreak events so it can
 * help keep accurate counts within all mines of how many blocks have been
 * mined.  
 * </p>
 * 
 * <p><b>Note:</b> Because this is a MONITOR event, we cannot do anything with the 
 * target block here. Mostly because everything has already been done with it, and 
 * this is only intended to MONITOR the final results. 
 * </p>
 * 
 * <p>This is a very critical class too, in that every single block break on
 * server will hit this class, even if the block is not within a mine. 
 * Therefore it is paramount that the computational cost, and temporal consumption,
 * is as minimal as possible.  Many of the behaviors, or goals, that this class
 * tries to accomplish can be done through simpler way, but performance is
 * a high goal so some of the code may be convoluted, but it has its purpose.
 * <p>
 * 
 * <p>Performance considerations: Unfortunately, this class can only be optimized 
 * for mines.  If a player is within a mine and breaking blocks, then we can
 * help ensure the next block they break within the mine will have optimal performance
 * in getting the reference to that mine.  If they are outside of all mines, like 
 * in a free world, then all mines will have to be checked each time.  There may
 * be room for optimizations in the future.
 * </p>
 * 
 * <p><b>Goals and Purposes:</b> These are needs of this class, or more specifically, the 
 * goals and purposes that it is trying to solve.</p>
 * 
 * <ul>
 *   <li>Record onBlockBreak events within a mine - Primary goal and purpose</li>
 *   <li>If a mine becomes empty, submit a manual reset - Secondary purpose</li>
 * </ul>
 *
 * <p><b>Resources:</b> These are the resources that are needed and their interdependencies.
 * </p>
 * 
 * <ul>
 *   <li>Ranks Module: Not needed & not used.
 *     <ul>
 *       <li>Would have added a level of complexity that is not needed</li>
 *       <li>Could have added a last mine used field, but the overhead of 
 *       		getting a Prison player object would have been too high.</ul>
 *       <li>Using internal player lookup and caching instead for optimal performance</li>
 *      </ul>
 *   </li>
 *   <li>MineModule: Required
 *     <ul>
 *       <li>Used to find the correct mine</li>
 *       <li>Actions performed on the mine: increment block break count & submit a manual 
 *           mine reset job if block count hits zero.</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * 
 * <p>General performance results:  Enable the commented out code to test the milliseconds it
 * spends on monitoring:
 * </p>
 * 
 * <ul>
 *   <li>Spigot 1.8.8, drop, single block mine: 
 *   		0.03371 0.033003 0.036944 0.033234 0.032782 0.030926 0.036374 0.036431</li> 
 *   <li>Spigot 1.8.8, drop, large block mine:
 *   		0.01759 0.017882 0.00455 .003232 0.002935 0.00344 0.00344 0.003248 0.002831 0.002898</li>
 *   <li>Spigot 1.8.8, drop, wilderness: 
 *   		0.024488 0.016086 0.010138 0.007265 0.009756 0.008951 0.009319 0.008664 0.007657 0.007013</li>
 *   <li>Spigot 1.8.8, drop, wilderness:
 *   		0.014462 0.009286 0.011038 0.011808 0.014291 0.011617 0.007516 0.008267 0.005324 0.004896</li>
 *   <li>Spigot 1.8.8, drop, wilderness, disabled:
 *          0.001549 0.001138 0.001162 0.001487 0.00181 0.000084</li>
 *   		</li>
 * </ul>
 *
 */
public class OnBlockBreakEventListener 
	extends OnBlockBreakEventCore {

	
	public OnBlockBreakEventListener() {
		super();
		
	}
	
	
	public enum BlockBreakPriority {
		
		DISABLED,
		
		LOWEST,
		LOW,
		NORMAL,
		HIGH,
		HIGHEST;
		
		public static BlockBreakPriority fromString( String value ) {
			BlockBreakPriority results = BlockBreakPriority.LOW;
			
			if ( value != null ) {
				
				for ( BlockBreakPriority bbPriority : values() ) {
					if ( bbPriority.name().equalsIgnoreCase( value )) {
						results = bbPriority;
						break;
					}
				}
			}
			
			return results;
		}
	}
	
	public void registerAllBlockBreakEvents(SpigotPrison spigotPrison ) {
		
		// Only register these event listeners if these are enabled.
		// In order to be enabled, the prison mines module must be enabled.
		
		if ( isEnabled() ) {
			
			// AutoManager should be registered first:
			// Only register Auto Manager if it is enabled:
			if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
				
				AutoManager autoManager = new AutoManager();
				autoManager.registerBlockBreakEvents( spigotPrison );
			}
				
				
			
			// Registers all of the non-AutoManager block events:
			OnBlockBreakEventListeners listeners = new OnBlockBreakEventListeners();
			listeners.registerBlockBreakEvents( spigotPrison );


			
//	    Bukkit.getPluginManager().registerEvents(new OnBlockBreakEventListener(), spigotPrison);
		}
		
		else {
			Output.get().logWarn( "BlockBreak event listeners cannot be registered " +
											"since the mine module is disabled." );
		}
		
	}

//	
//    /**
//     * <p>The EventPriorty.MONITOR means that the state of the event is OVER AND DONE,
//     * so this function CANNOT do anything with the block, other than "monitor" what
//     * happened.  That is all we need to do, is to just count the number of blocks within
//     * a mine that have been broken.
//     * </p>
//     * 
//     * <p><b>Note:</b> Because this is a MONITOR event, we cannot do anything with the 
//     * target block here. Mostly because everything has already been done with it, and 
//     * this is only intended to MONITOR the final results. 
//     * </p>
//     * 
//     * <p>One interesting fact about this monitoring is that we know that a block was broken,
//     * not because of what is left (should be air), but because this function was called.
//     * There is a chance that the event was canceled and the block remains unbroken, which
//     * is what WorldGuard would do.  But the event will also be canceled when auto pickup is
//     * enabled, and at that point the BlockType will be air.
//     * </p>
//     * 
//     * <p>If the event is canceled it's important to check to see that the BlockType is Air,
//     * since something already broke the block and took the drop.  
//     * If it is not canceled we still need to count it since it will be a normal drop.  
//     * </p>
//     * 
//     * @param e
//     */
//    @EventHandler(priority=EventPriority.MONITOR) 
//    public void onBlockBreakMonitor(BlockBreakEvent e) {
//
//    	genericBlockEventMonitor( e );
//    }
//    
//    @EventHandler(priority=EventPriority.MONITOR) 
//    public void onBlockShredBreakMonitor(BlockShredEvent e) {
//    	genericBlockEventMonitor( e );
//    }
//    
////    @EventHandler(priority=EventPriority.MONITOR) 
////    public void onTEBlockExplodeMonitor(TEBlockExplodeEvent e) {
////    
////    	genericBlockExplodeEventMonitor( e );
////    }
//
//    @EventHandler(priority=EventPriority.MONITOR) 
//	public void onCrazyEnchantsBlockExplodeMonitor( BlastUseEvent e ) {
//		
//    	genericBlockExplodeEventMonitor( e );
//	}
//    
    
    
    public void onBlockBreak(BlockBreakEvent e) {

    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
    		genericBlockEvent( e );
    	}
    }
    
    public void onBlockShredBreak(BlockShredEvent e) {

    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
    		genericBlockEvent( e, false, false, false );
    	}
    	else {
    		genericBlockEvent( e, false, true, false );
    	}
    }
    
    
    public void onCrazyEnchantsBlockExplode( Object obj ) {
    	
    	boolean isCEBlockExplodeEnabled = isBoolean( 
    				AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( isCEBlockExplodeEnabled ) {
    		BlastUseEvent e = (BlastUseEvent) obj;
    		
    		// if autoManager is turned off, then only process the blockEvents
    		genericBlockExplodeEvent( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    public void onPrisonEnchantsExplosiveEvent(ExplosiveEvent e) {
    	
    	// if autoManager is turned off, then only process the blockEvents
    	genericBlockExplodeEvent( e, false, isBoolean(AutoFeatures.isAutoManagerEnabled), false );
    }
    
////    @EventHandler(priority=EventPriority.LOW) 
////    public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
////
////    	boolean isTEExplosiveEnabled = isBoolean( 
////							    AutoFeatures.isProcessTokensEnchantExplosiveEvents );
////    	
////    	if ( isTEExplosiveEnabled ) {
////    		
////    		genericBlockExplodeEvent( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
////    	}
////    }




}

package tech.mcprison.prison.spigot.block;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.events.AutoManagerBlockBreakEvents;

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
	
	public void registerAllBlockBreakEvents(SpigotPrison spigotPrison ) {
		
		// Only register these event listeners if these are enabled.
		// In order to be enabled, the prison mines module must be enabled.
		
		if ( !isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
			
			Output.get().logWarn( "AutoMager: AutoFeatures is dsabled. " +
					"No block break listeners are registered. " +
					"The setting 'autoManager.isAutoManagerEnabled' is set to 'false' " +
					"in autoFeaturesConfig.yml." );
			
			return;
		}
		
		if ( isEnabled() ) {
			
			
			Output.get().logInfo( "AutoManager: AutoFeatures and the Mine module are enabled. Prison " +
					"will register the selected block break listeners." );
			
			
			// This will register all events that should be enabled, for both
			// auto manager and the normal events too.
			new AutoManagerBlockBreakEvents().registerEvents();
			
			
		}
		
		else {
			Output.get().logWarn( "AutoManager: AutoFeaturs are enabled, but the Mines module is disabled." +
					"Prison will not register any block break listeners." );
		}
		
	}


}

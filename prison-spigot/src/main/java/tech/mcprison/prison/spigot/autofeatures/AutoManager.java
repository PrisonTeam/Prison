package tech.mcprison.prison.spigot.autofeatures;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.cryptomorin.xseries.XMaterial;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import zedly.zenchantments.BlockShredEvent;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends AutoManagerFeatures
	implements Listener {
	
//	private boolean teExplosionTriggerEnabled;
	
	public AutoManager() {
        super();
        
        // Save this instance within the SpigotPrison instance so it can be accessed
        // from non-event listeners:
        SpigotPrison.getInstance().setAutoFeatures( this );
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
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onBlockBreak(BlockBreakEvent e) {
    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {

    		genericBlockEvent( e, false );
    	}
    }
    
    /**
     * <p>The use of e.getBlock != null is added to "use" the BlockShredEvent to prevent
     * the complier from falsely triggering a Not Used warning.
     * </p>
     */
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onBlockShredBreak(BlockShredEvent e) {
    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) && e.getBlock() != null ) {
    		 
    		genericBlockEvent( e, false );
    	}
    }
    
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onTEBlockExplodeMonitor(TEBlockExplodeEvent e) {
    	if ( !e.isCancelled() && isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
    	    
    		genericBlockExplodeEvent( e );
    	}
    }
    
    
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onCrazyEnchantsBlockExplodeMonitor(BlastUseEvent e) {
    	if ( !e.isCancelled() && isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
    		genericBlockExplodeEvent( e );
    	}
    }
    
    
////    @Override
//    @EventHandler(priority=EventPriority.LOW) 
//    public void onRevEnchantsMineEvent(RevGiveBlocksEvent e) {
//    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
//    		 
//    		e.get
//    		genericBlockEvent( e, false );
//    	}
//    }
    
    
    @Override
	public boolean doAction( SpigotBlock block, Mine mine, Player player ) {
    	
    	return applyAutoEvents( block, player, mine );
	}
    
    
    /**
     * <p>This function overrides the doAction in OnBlockBreakEventListener and
     * this is only enabled when auto manager is enabled.
     * </p>
     * 
     */
    @Override
    public boolean doAction( Mine mine, Player player, List<SpigotBlock> explodedBlocks, 
    								BlockEventType blockEventType, String triggered ) {
    	return applyAutoEvents( player, mine, explodedBlocks, blockEventType, triggered );
    }
    
    
//    /**
//     * <p>This function overrides the doAction in OnBlockBreakEventListener and
//     * this is only enabled when auto manager is enabled.
//     * </p>
//     * 
//     */
//    @Override
//    public void doAction( Mine mine, BlastUseEvent e, 
//    		List<SpigotBlock> teExplosiveBlocks ) {
//    	applyAutoEvents( e, mine, teExplosiveBlocks );
//    }


	private boolean applyAutoEvents( SpigotBlock spigotBlock, Player player, Mine mine) {
		boolean cancel = false;
		
		if (isBoolean(AutoFeatures.isAutoManagerEnabled) && 
			!spigotBlock.isEmpty() ) {
			
//			Output.get().logInfo( "#### AutoManager.applyAutoEvents: BlockBreakEvent: :: " + mine.getName() + "  " + 
//					"  blocks remaining= " + 
//					mine.getRemainingBlockCount() + " [" + block.toString() + "]"
//					);

			SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

			int count = applyAutoEvents( player, spigotBlock, mine );
			
			if ( count > 0 ) {
				processBlockBreakage( spigotBlock, mine, player, count, BlockEventType.blockBreak,
										null, itemInHand );

    			cancel = true;
			}
			
			if ( mine != null ) {
				checkZeroBlockReset( mine );
			}
	
		}
		
		return cancel;
	}


	
	
	private int applyAutoEvents( Player player, SpigotBlock block, Mine mine ) {
		int count = 0;
		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

		
		boolean isLoreEnabled = isBoolean( AutoFeatures.isLoreEnabled );
		
		boolean lorePickup = isLoreEnabled && checkLore( itemInHand, getMessage( AutoFeatures.lorePickupValue ) );
		boolean loreSmelt = isLoreEnabled && checkLore( itemInHand, getMessage( AutoFeatures.loreSmeltValue) );
		boolean loreBlock = isLoreEnabled && checkLore( itemInHand, getMessage( AutoFeatures.loreBlockValue ) );
		
		
		boolean isAutoPickup = lorePickup || isBoolean( AutoFeatures.autoPickupEnabled ) ||
										player.isPermissionSet( getMessage( AutoFeatures.permissionAutoPickup ));
		
		boolean isAutoSmelt = loreSmelt || isBoolean( AutoFeatures.autoSmeltEnabled ) ||
										player.isPermissionSet( getMessage( AutoFeatures.permissionAutoPickup ));
		
		boolean isAutoBlock = loreBlock || isBoolean( AutoFeatures.autoBlockEnabled ) ||
										player.isPermissionSet( getMessage( AutoFeatures.permissionAutoBlock ));
		
		// NOTE: Using isPermissionSet so players that are op'd to not auto enable everything.
		//       Ops will have to have the perms set to actually use them.
				
		// AutoPickup
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoPickupLimitToMines )) &&
				isAutoPickup ) {
			
			count = autoFeaturePickup( block, player, itemInHand );

			// Cannot set to air yet, or auto smelt and auto block will only get AIR:
//			autoPickupCleanup( block, count );
		}
		
		XMaterial source = SpigotUtil.getXMaterial( block.getPrisonBlock() );
		
		// AutoSmelt
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoSmeltLimitToMines )) &&
				isAutoSmelt ){
			
			// Smelting needs to change the source to the smelted target so auto block will work:
			source = autoFeatureSmelt( player, source );
		}
		
		// AutoBlock
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoBlockLimitToMines )) &&
				isAutoBlock ) {
			
			autoFeatureBlock( player, source );
		}
		
		
		// AutoPickup - Clean up (set block to air)
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoPickupLimitToMines )) &&
				isAutoPickup ) {
			
			autoPickupCleanup( block, count );
			
		}

//			
//			// A block was broke... so record that event on the tool:	
//			if ( isBoolean( AutoFeatures.loreTrackBlockBreakCount ) && e.isCancelled()) {
//
//				// The event was canceled, so the block was successfully broke, so increment the name counter:
//				
////				ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
//				itemLoreCounter( itemInHand, getMessage( AutoFeatures.loreBlockBreakCountName ), 1 );
//			}
		
		return count;
	}


	/**
	 * <p>This function gets called for EACH block that is impacted by the
	 * explosion event.  The event may have have a list of blocks, but not all
	 * blocks may be included in the mine. This function is called ONLY when
	 * a block is within a mine,
	 * </p>
	 * 
	 * <p>The event TEBlockExplodeEvent has already taken place and it handles all
	 * actions such as auto pickup, auto smelt, and auto block.  The only thing we
	 * need to do is to record the number of blocks that were removed during 
	 * the explosion event. The blocks should have been replaced by air.
	 * </p>
	 * 
	 * <p>Normally, prison based auto features, and or the perms need to be enabled 
	 * for the auto features to be enabled, but since this is originating from another
	 * plugin and another external configuration, we cannot test for any real perms
	 * or configs.  The fact that this event happens within one of the mines is 
	 * good enough, and must be counted.
	 * </p>
	 * 
	 * @param e
	 * @param mine
	 */
	private boolean applyAutoEvents( Player player, Mine mine, 
									List<SpigotBlock> explodedBlocks, BlockEventType blockEventType, 
										String triggered ) {
		boolean cancel = false;
		
		int totalCount = 0;

		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

		
		// The explodedBlocks list have already been validated as being within the mine:
		for ( SpigotBlock spigotBlock : explodedBlocks ) {
			
			int drop = applyAutoEvents( player, spigotBlock, mine );
			totalCount += drop;
			
			if ( drop > 0 ) {
				
				processBlockBreakage( spigotBlock, mine, player, drop, 
											blockEventType, triggered, itemInHand );
			}
		}
		
		if ( mine != null ) {
			checkZeroBlockReset( mine );
		}
		
		if ( totalCount > 0 ) {
			cancel = true;
		}
		
		return cancel;
	}
	
	
//	private void applyAutoEvents( BlastUseEvent e, Mine mine, 
//										List<SpigotBlock> explosiveBlocks ) {
//		
//		Player player = e.getPlayer();
//		
//		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );
//		
//		// Auto manager is enabled if it's going to hit this function so no need to double check:
//		boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
//		
//		if ( isCEBlockExplodeEnabled ) {
//			
//			
//			// The teExplosiveBlocks list have already been validated as being within the mine:
//			for ( SpigotBlock spigotBlock : explosiveBlocks ) {
////				
//				int count = applyAutoEvents( player, spigotBlock, mine );
//
//				if ( count > 0 ) {
//					
//					processBlockBreakage( spigotBlock, mine, player, count, BlockEventType.CEXplosion,
//											null, itemInHand );
//					
//				}
//			}
//			
//			if ( isBoolean( AutoFeatures.isDebugSupressOnBlockBreakEventCancels )) {
//				e.setCancelled( true );
//			}
//			
//			if ( mine != null ) {
//				checkZeroBlockReset( mine );
//			}
//			
//			
////			Output.get().logInfo( "#### AutoManager: TEBlockExplodeEvent:: " + mine.getName() + "  e.blocks= " + 
////					e.blockList().size() + "  processed : " +
////					teExplosiveBlocks.size() + "  " + totalCount + 
////					"  blocks remaining= " + mine.getRemainingBlockCount() + 
////					" (" + (teExplosiveBlocks.size() + mine.getRemainingBlockCount()) + ")");
//			
//		}
//		
//	}
	
//	private boolean isTeExplosionTriggerEnabled() {
//		return teExplosionTriggerEnabled;
//	}
//
//	private void setTeExplosionTriggerEnabled( boolean teExplosionTriggerEnabled ) {
//		this.teExplosionTriggerEnabled = teExplosionTriggerEnabled;
//	}

}

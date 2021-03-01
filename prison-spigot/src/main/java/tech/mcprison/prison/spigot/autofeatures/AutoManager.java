package tech.mcprison.prison.spigot.autofeatures;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends AutoManagerFeatures
	implements Listener {
	
	private boolean teExplosionTriggerEnabled;
	
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
    
    
    
    @Override
	public void doAction( SpigotBlock block, Mine mine, BlockBreakEvent e ) {
    	
    	applyAutoEvents( block, e, mine );
	}
    
    
    /**
     * <p>This function overrides the doAction in OnBlockBreakEventListener and
     * this is only enabled when auto manager is enabled.
     * </p>
     * 
     */
    @Override
    public void doAction( Mine mine, TEBlockExplodeEvent e, 
    				List<SpigotBlock> teExplosiveBlocks ) {
    	applyAutoEvents( e, mine, teExplosiveBlocks );
    }
    
    
    /**
     * <p>This function overrides the doAction in OnBlockBreakEventListener and
     * this is only enabled when auto manager is enabled.
     * </p>
     * 
     */
    @Override
    public void doAction( Mine mine, BlastUseEvent e, 
    		List<SpigotBlock> teExplosiveBlocks ) {
    	applyAutoEvents( e, mine, teExplosiveBlocks );
    }


	//TODO Use the SpigotBlock within these functions so it can use the new block model and the custom blocks if they exist
	private void applyAutoEvents( SpigotBlock spigotBlock, BlockBreakEvent e, Mine mine) {

		if (isBoolean(AutoFeatures.isAutoManagerEnabled) && !e.isCancelled() && 
			!spigotBlock.isEmpty() ) {
			
			
//			Output.get().logInfo( "#### AutoManager.applyAutoEvents: BlockBreakEvent: :: " + mine.getName() + "  " + 
//					"  blocks remaining= " + 
//					mine.getRemainingBlockCount() + " [" + block.toString() + "]"
//					);
			
			Player player = e.getPlayer();

			SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

			
			String targetBlockName = mine.getTargetPrisonBlockName( spigotBlock );


			int count = applyAutoEvents( player, spigotBlock, mine );
			
			
			if ( count > 0 ) {
				
				// Record the block break before it is changed to AIR:
				mine.incrementBlockMiningCount( targetBlockName );
				
				// Process mine block break events:
				SpigotPlayer sPlayer = new SpigotPlayer( player );
				
				
				// move in to the loop when blocks are tracked?... ??? 
//				String blockName = spigotBlock.getPrisonBlock().getBlockName();
				mine.processBlockBreakEventCommands( targetBlockName, sPlayer, BlockEventType.blockBreak, null );


				// Set the broken block to AIR and cancel the event
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);

				// Maybe needed to prevent drop side effects:
				e.getBlock().getDrops().clear();

			}
			
			// A block was broke... so record that event on the tool:	
			if ( isBoolean( AutoFeatures.loreTrackBlockBreakCount ) && e.isCancelled()) {

				// The event was canceled, so the block was successfully broke, so increment the name counter:
				
//				ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
				itemLoreCounter( itemInHand, getMessage( AutoFeatures.loreBlockBreakCountName ), 1 );
			}
		}
		
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
			autoPickupCleanup( block, player, itemInHand, count );
			
		}
		
		// AutoSmelt
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoSmeltLimitToMines )) &&
				isAutoSmelt ){
			
			autoFeatureSmelt( block, player, itemInHand );
		}
		
		// AutoBlock
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoBlockLimitToMines )) &&
				isAutoBlock ) {
			
			autoFeatureBlock( block, player, itemInHand );
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


	private boolean checkLore( SpigotItemStack itemInHand, String loreValue ) {
		boolean results = false;
		
		double lorePercent = getLoreValue( itemInHand, loreValue );

		results = lorePercent == 100.0 ||
					lorePercent > 0 && 
					lorePercent <= getRandom().nextDouble() * 100;
		
		return results;
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
	private void applyAutoEvents( TEBlockExplodeEvent e, Mine mine, 
						List<SpigotBlock> teExplosiveBlocks ) {
		
		Player player = e.getPlayer();
		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

		// Auto manager is enabled if it's going to hit this function so no need to double check:
//		boolean isAutoManagerEnabled = isBoolean( AutoFeatures.isAutoManagerEnabled );
		boolean isTEExplosiveEnabled = isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents );
		
		if ( isTEExplosiveEnabled ) {

			int totalCount = 0;
			
			// The teExplosiveBlocks list have already been validated as being within the mine:
			for ( SpigotBlock spigotBlock : teExplosiveBlocks ) {
				

				String targetBlockName = mine.getTargetPrisonBlockName( spigotBlock );

				
				int count = applyAutoEvents( player, spigotBlock, mine );
				totalCount += count;

				
				if ( count > 0 ) {
					
					
					// Record the block break before it is changed to AIR:
					mine.incrementBlockMiningCount( targetBlockName );
					
					
					String triggered = null;
					
					// Please be aware:  This function is named the same as the auto features setting, but this is 
					// not related.  This is only trying to get the name of the enchantment that triggered the event.
					if ( isTeExplosionTriggerEnabled() ) {
						
						try {
							triggered = e.getTrigger();
						}
						catch ( Exception | NoSuchMethodError ex ) {
							// Only print the error the first time, then suppress the error:
							String error = ex.getMessage();
							
							Output.get().logError( "Error: Trying to access the TEBlockExplodeEvent.getTrigger() " +
									"function.  Make sure you are using TokenEnchant v18.11.0 or newer. The new " +
									"getTrigger() function returns the TE Plugin that is firing the TEBlockExplodeEvent. " +
									"The Prison BlockEvents can be filtered by this triggered value. " +
									error );
							
							// Disable collecting the trigger.
							setTeExplosionTriggerEnabled( false );
							
						}
					}
					
					
					// Process mine block break events:
					SpigotPlayer sPlayer = new SpigotPlayer( e.getPlayer() );
					
					
					// move in to the loop when blocks are tracked?... ??? 
//					String blockName = spigotBlock.getPrisonBlock().getBlockName();
					mine.processBlockBreakEventCommands( targetBlockName, sPlayer, BlockEventType.TEXplosion,
							triggered );

					e.setCancelled( true );
				}


				
			}
			
			
//			Output.get().logInfo( "#### AutoManager: TEBlockExplodeEvent:: " + mine.getName() + "  e.blocks= " + 
//					e.blockList().size() + "  processed : " +
//					teExplosiveBlocks.size() + "  " + totalCount + 
//					"  blocks remaining= " + mine.getRemainingBlockCount() + 
//					" (" + (teExplosiveBlocks.size() + mine.getRemainingBlockCount()) + ")");
			
			
			
			if (e.isCancelled()) {
				// The event was canceled, so the block was successfully broke, so increment the name counter:
				itemLoreCounter(itemInHand, getMessage(AutoFeatures.loreBlockExplosionCountName), totalCount);
			}
		}
		
	}
	
	
	private void applyAutoEvents( BlastUseEvent e, Mine mine, 
										List<SpigotBlock> teExplosiveBlocks ) {
		
		Player player = e.getPlayer();
		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );
		
		// Auto manager is enabled if it's going to hit this function so no need to double check:
//		boolean isAutoManagerEnabled = isBoolean( AutoFeatures.isAutoManagerEnabled );
		boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
		
		if ( isCEBlockExplodeEnabled ) {
			
			int totalCount = 0;
			
			// The teExplosiveBlocks list have already been validated as being within the mine:
			for ( SpigotBlock spigotBlock : teExplosiveBlocks ) {
				
				String targetBlockName = mine.getTargetPrisonBlockName( spigotBlock );

				
				int count = applyAutoEvents( player, spigotBlock, mine );
				totalCount += count;

				
				if ( count > 0 ) {
					
					// Record the block break before it is changed to AIR:
					mine.incrementBlockMiningCount( targetBlockName );
					
					
					// Process mine block break events:
					SpigotPlayer sPlayer = new SpigotPlayer( player );
					
					
					// move in to the loop when blocks are tracked?... ??? 
//					String blockName = spigotBlock.getPrisonBlock().getBlockName();
					mine.processBlockBreakEventCommands( targetBlockName, sPlayer, BlockEventType.CEXplosion, null );

				}

				
			}
			
			
//			Output.get().logInfo( "#### AutoManager: TEBlockExplodeEvent:: " + mine.getName() + "  e.blocks= " + 
//					e.blockList().size() + "  processed : " +
//					teExplosiveBlocks.size() + "  " + totalCount + 
//					"  blocks remaining= " + mine.getRemainingBlockCount() + 
//					" (" + (teExplosiveBlocks.size() + mine.getRemainingBlockCount()) + ")");
			
			
//			if ( totalCount > 0 ) {
//				
//				
//				// Override blockCount to be exactly the blocks within the mine:
//				int blockCount = teExplosiveBlocks.size();
//				
//				mine.addBlockBreakCount( blockCount );
//				mine.addTotalBlocksMined( blockCount );
//				
//				
//				// Set the broken block to AIR and cancel the event
//				e.setCancelled(true);
//				// e.getBlock().setType(Material.AIR);
//				
//				// Maybe needed to prevent drop side effects:
//				//e.getBlock().getDrops().clear();
//				
//			}
			
			if (e.isCancelled()) {
				// The event was canceled, so the block was successfully broke, so increment the name counter:
				itemLoreCounter(itemInHand, getMessage(AutoFeatures.loreBlockExplosionCountName), totalCount);
			}
		}
		
	}
	
	private boolean isTeExplosionTriggerEnabled() {
		return teExplosionTriggerEnabled;
	}

	private void setTeExplosionTriggerEnabled( boolean teExplosionTriggerEnabled ) {
		this.teExplosionTriggerEnabled = teExplosionTriggerEnabled;
	}

}

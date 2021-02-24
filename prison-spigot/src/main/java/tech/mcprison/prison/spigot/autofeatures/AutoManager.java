package tech.mcprison.prison.spigot.autofeatures;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends AutoManagerFeatures
	implements Listener {
	
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
    		
        	
        	Location bLoc = e.getBlock().getLocation();
        	String blockId = bLoc.getBlockX() + "." + bLoc.getBlockY() + "." + bLoc.getBlockZ();
        	
        	Output.get().logInfo( "#### AutoManager.OnBlockBreak :  %s   blocks= 1   isCanceled=%b ",
        			blockId, e.isCancelled() );

    		genericBlockEvent( e );
    	}
    }
    
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onTEBlockExplodeMonitor(TEBlockExplodeEvent e) {
    	if ( !e.isCancelled() && isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
    	    
        	Location bLoc = e.getBlock().getLocation();
        	String blockId = bLoc.getBlockX() + "." + bLoc.getBlockY() + "." + bLoc.getBlockZ();

        	Output.get().logInfo( "#### AutoManager.onTEBlockExplode :  %s blocks= %s   isCanceled=%b", 
        			blockId, Integer.toString( e.blockList().size() ), e.isCancelled() );

    		genericBlockExplodeEvent( e );
    	}
    }
    
    
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onCrazyEnchantsBlockExplode(BlastUseEvent e) {
    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
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
    
    // Prevents players from picking up armorStands (used for holograms), only if they're invisible
	@EventHandler
	public void manipulate(PlayerArmorStandManipulateEvent e) {
		if(!e.getRightClicked().isVisible()) {
			e.setCancelled(true);
		}
	}

	//TODO Use the SpigotBlock within these functions so it can use the new block model and the custom blocks if they exist
	private void applyAutoEvents( SpigotBlock block, BlockBreakEvent e, Mine mine) {

		if (isBoolean(AutoFeatures.isAutoManagerEnabled) && !e.isCancelled()) {
			
			Player player = e.getPlayer();

			SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );


			int count = applyAutoEvents( player, block, mine );
			
			
			if ( count > 0 ) {
				
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
		int totalCount = 0;
		
		Player player = e.getPlayer();
		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

		// Auto manager is enabled if it's going to hit this function so no need to double check:
//		boolean isAutoManagerEnabled = isBoolean( AutoFeatures.isAutoManagerEnabled );
		boolean isTEExplosiveEnabled = isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents );
		
		if ( isTEExplosiveEnabled ) {
			
			Output.get().logInfo( "#### AutoManager.applyAutoEvents :  isTEExplosiveEnabled = true" );
			
			// The teExplosiveBlocks list have already been validated as being within the mine:
			for ( SpigotBlock spigotBlock : teExplosiveBlocks ) {
				
//				if ( isAutoManagerEnabled ) {
//					
					totalCount += applyAutoEvents( player, spigotBlock, mine );
//				}
//				else {
					// This will never be called since this function will be called
					// from auto complete listeners:
//					totalCount += calculateNormalDrop( itemInHand, spigotBlock );
//				}
				 
			}
		}
		
		
		if ( totalCount > 0 ) {
			
			// Set the broken block to AIR and cancel the event
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);

			// Maybe needed to prevent drop side effects:
			e.getBlock().getDrops().clear();

		}

		if (e.isCancelled()) {
			// The event was canceled, so the block was successfully broke, so increment the name counter:
			itemLoreCounter(itemInHand, getMessage(AutoFeatures.loreBlockExplosionCountName), totalCount);
		}
	}
}

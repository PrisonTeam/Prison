package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.data.Mine;
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
    		
//    		Output.get().logInfo( "####  AutoManager.OnBlockBreak: " + 
//    				" x: " + e.getBlock().getX() + " y: " + e.getBlock().getY() +
//    				" z: " + e.getBlock().getZ() + " cancelled: " + e.isCancelled());
    		
    		genericBlockEvent( e );
    	}
    }
    
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onTEBlockExplode(TEBlockExplodeEvent e) {
    	if ( isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
    		genericBlockExplodeEvent( e );
    	}
    }
    
    
    
    @Override
	public void doAction( SpigotBlock block, Mine mine, BlockBreakEvent e ) {
    	applyAutoEvents( block, e, mine );
	}
    
    
    @Override
    public void doAction( Mine mine, TEBlockExplodeEvent e, int blockCount ) {
    	applyAutoEvents( e, mine, blockCount );
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

			
			
			double lorePickup = 0.0; // doesItemHaveAutoFeatureLore( ItemLoreEnablers.Pickup, player );
			double loreSmelt = 0.0; // doesItemHaveAutoFeatureLore( ItemLoreEnablers.Smelt, player );
			double loreBlock = 0.0; // doesItemHaveAutoFeatureLore( ItemLoreEnablers.Block, player );
			
			boolean permPickup = 
					lorePickup == 100.0 ||
					lorePickup > 0 && lorePickup <= getRandom().nextDouble() * 100;
			boolean permSmelt = 
					loreSmelt == 100.0 ||
					loreSmelt > 0 && loreSmelt <= getRandom().nextDouble() * 100;
			boolean permBlock = 
					loreBlock == 100.0 ||
					loreBlock > 0 && loreBlock <= getRandom().nextDouble() * 100;
			
			// AutoPickup
			if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoPickupLimitToMines )) &&
					(permPickup || isBoolean( AutoFeatures.autoPickupEnabled ) ||
							player.hasPermission( getMessage( AutoFeatures.permissionAutoPickup ) )) ) {
				
				int count = autoFeaturePickup( block, player, itemInHand );
				autoPickupCleanup( player, itemInHand, count, e );
			}
			
			// AutoSmelt
			if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoSmeltLimitToMines )) &&
					(permSmelt || isBoolean( AutoFeatures.autoSmeltEnabled ) ||
							player.hasPermission( getMessage( AutoFeatures.permissionAutoSmelt ) )) ){
				
				autoFeatureSmelt( block, player, itemInHand );
			}
			
			// AutoBlock
			if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoBlockLimitToMines )) &&
					(permBlock || isBoolean( AutoFeatures.autoBlockEnabled ) ||
							player.hasPermission(getMessage( AutoFeatures.permissionAutoBlock ) ) ) ) {
				
//				Output.get().logInfo( "AutoManager.applyAutoEnvents: AutoBlock  enabled = %b   " +
//						"%s hasPerm = %b  isSet = %b   has lore = %b ",
//						isBoolean( AutoFeatures.autoBlockEnabled ),
//						getMessage( AutoFeatures.permissionAutoBlock ), 
//						player.hasPermission(getMessage( AutoFeatures.permissionAutoBlock ) ),
//						player.isPermissionSet(getMessage( AutoFeatures.permissionAutoBlock ) ),
//						loreBlock
//						);
				autoFeatureBlock( block, player, itemInHand );
			}
			
			// NOTE: This may be in duplication... durability is calculated in auto pickup:
			// Calculate durability if enabled:
			// isCalculateDurabilityEnabled must be enabled before loreDurabiltyResistance will
			// even be checked. 
//			if ( isBoolean( AutoFeatures.isCalculateDurabilityEnabled ) && 
//					e.isCancelled()) {
//				
//				ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
//
//				// value of 0 = normal durability. Value 100 = never calculate durability.
//				int durabilityResistance = 0;
//				if ( isBoolean( AutoFeatures.loreDurabiltyResistance ) ) {
//					durabilityResistance = getDurabilityResistance( itemInHand, 
//							getMessage( AutoFeatures.loreDurabiltyResistanceName ) );
//				}
//				
//				calculateDurability( player, itemInHand, durabilityResistance );
//			}
			
			
			// A block was broke... so record that event on the tool:	
			if ( isBoolean( AutoFeatures.loreTrackBlockBreakCount ) && e.isCancelled()) {

				// The event was canceled, so the block was successfully broke, so increment the name counter:
				
//				ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
				itemLoreCounter( itemInHand, getMessage( AutoFeatures.loreBlockBreakCountName ), 1 );
			}
		}
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
	private void applyAutoEvents( TEBlockExplodeEvent e, Mine mine, int blockCount ) {
		
		Player player = e.getPlayer();
		
//		double lorePickup = doesItemHaveAutoFeatureLore( ItemLoreEnablers.Pickup, p );
//		double loreSmelt = doesItemHaveAutoFeatureLore( ItemLoreEnablers.Smelt, p );
//		double loreBlock = doesItemHaveAutoFeatureLore( ItemLoreEnablers.Block, p );
//		
//		boolean permPickup = p.hasPermission( "prison.autofeatures.pickup" ) ||
//				lorePickup == 100.0 ||
//				lorePickup > 0 && lorePickup <= getRandom().nextDouble() * 100;
//		boolean permSmelt = p.hasPermission( "prison.autofeatures.smelt" ) ||
//				loreSmelt == 100.0 ||
//				loreSmelt > 0 && loreSmelt <= getRandom().nextDouble() * 100;
//		boolean permBlock = p.hasPermission( "prison.autofeatures.block" ) ||
//				loreBlock == 100.0 ||
//				loreBlock > 0 && loreBlock <= getRandom().nextDouble() * 100;

//		if ( permPickup || permSmelt || permBlock ||
//				isAreEnabledFeatures())

		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );

		if (e.isCancelled()) {
			// The event was canceled, so the block was successfully broke, so increment the name counter:
			itemLoreCounter(itemInHand, getMessage(AutoFeatures.loreBlockExplosionCountName), blockCount);
		}
	}
}

package tech.mcprison.prison.spigot.autofeatures;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventCore;
import tech.mcprison.prison.spigot.block.OnBlockBreakExternalEvents;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;
import tech.mcprison.prison.spigot.utils.tasks.PlayerAutoRankupTask;
import tech.mcprison.prison.util.Text;

/**
 * <p>This class controls the data and the basic functions for auto features.
 * It loads the settings only once, and uses them on each event that is raised
 * in order to reduce overhead in reloading all of them on each block break
 * event.  Then, if an external process should happen to save changes to
 * these settings, then it will reload the settings so they are always
 * current.
 * </p>
 *
 *
 */
public abstract class AutoManagerFeatures
		extends OnBlockBreakEventCore {

	private Random random = new Random();

	
	public AutoManagerFeatures() {
		super();

		setup();
	}

	public enum EventListenerCancelBy {
		none,
		event,
		drops;
	}

	private void setup() {

		
		// Register all external events such as mcMMO, EZBlocks, and Quests:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();

	}


    /**
     * <p> NOTE: Check for the ACCESS priority and if someone does not have access, then return 
     * with a cancel on the event.  Both ACCESSBLOCKEVENTS and ACCESSMONITOR will be 
     * converted to just ACCESS at this point, and the other part will run under either 
     * BLOCKEVENTS or MONITOR.
     * </p>
     * 
     * @param pmEvent
     * @param start
     * @return
     */
    protected boolean checkIfNoAccess( PrisonMinesBlockBreakEvent pmEvent, double start ) {
    	boolean results = false;
    	
    	// NOTE: Check for the ACCESS priority and if someone does not have access, then return 
    	//       with a cancel on the event.  Both ACCESSBLOCKEVENTS and ACCESSMONITOR will be
    	//       converted to just ACCESS at this point, and the other part will run under either
    	//       BLOCKEVENTS or MONITOR.
    	if ( pmEvent.getBbPriority() == BlockBreakPriority.ACCESS && pmEvent.getMine() != null && 
    			!pmEvent.getMine().hasMiningAccess( pmEvent.getSpigotPlayer() )) {
    		
    		String message = String.format( "(&cACCESS fail: player %s does not have access to "
    												+ "mine %s&3. Event canceled) ",
    						pmEvent.getSpigotPlayer().getName(),
    						pmEvent.getMine().getTag() );
    		pmEvent.getDebugInfo().append( message );

    		printDebugInfo( pmEvent, start );

    		results = true;
    	}
    	
    	return results;
    }
    
	/**
	 * <p>Prints out the debugInfo if it has anything to print.
	 * </p>
	 * 
	 * @param pmEvent
	 * @param start
	 */
    protected void printDebugInfo(  PrisonMinesBlockBreakEvent pmEvent, double start ) {
		if ( pmEvent != null && pmEvent.getDebugInfo().length() > 0 ) {
			
			long stop = System.nanoTime();
			pmEvent.getDebugInfo().append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, pmEvent.getDebugInfo().toString() );
		}
    }
    
    
    /**
     * <p>This provides for the basic dump of the event listeners.
     * </p>
     * 
     * @param handlers
     * @param bbPriority
     * @param sb
     */
    protected void dumpEventListenersCore( String title, HandlerList handlers, BlockBreakPriority bbPriority,
    			StringBuilder sb ) {
    	
		String cdTitle = String.format( 
				"%s (&7%s&2)", 
				title,
				( bbPriority == null ? "--none--" : bbPriority.name()) );
		
		ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
				cdTitle, 
				new SpigotHandlerList( handlers ) );

		if ( eventDisplay != null ) {
			sb.append( eventDisplay.toStringBuilder() );
			sb.append( "\n" );
		}

		
		if ( bbPriority.isComponentCompound() ) {
			StringBuilder sbCP = new StringBuilder();
			for ( BlockBreakPriority bbp : bbPriority.getComponentPriorities() ) {
				if ( sbCP.length() > 0 ) {
					sbCP.append( ", " );
				}
				sbCP.append( "'" ).append( bbp.name() ).append( "'" );
			}
			
			String msg = String.format( "&2Note '&7%s&2' is a compound of: [&7%s&2]",
					bbPriority.name(),
					sbCP );
			
			sb.append( msg ).append( "\n" );
		}


    }
	
	/**
	 * <p>For the event handlers that implement the BlockBreakEvent, this allows 
	 * the other plugins to also process the event if forced.
	 * </p>
	 * 
	 * @param pmEvent
	 * @param debugInfo
	 * @param e
	 */
	protected void processPMBBExternalEvents( PrisonMinesBlockBreakEvent pmEvent, 
			BlockBreakEvent e ) {
		
		if ( pmEvent.getMine() != null || pmEvent.getMine() == null && 
				!isBoolean( AutoFeatures.pickupLimitToMines ) ) {
			
			// check all external events such as mcMMO and EZBlocks:
			pmEvent.getDebugInfo().append( 
					OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e ) );
			
		}

	}
	

	protected EventListenerCancelBy processPMBBEvent(PrisonMinesBlockBreakEvent pmEvent ) {
		
		EventListenerCancelBy cancelBy = EventListenerCancelBy.none;
		
		// This is where the processing actually happens:
		if ( pmEvent.getMine() != null || pmEvent.getMine() == null && 
				!isBoolean( AutoFeatures.pickupLimitToMines ) ) {
			
			pmEvent.getDebugInfo().append( "(normal processing initiating) " );
			
			// Set the mine's PrisonBlockTypes for the block. Used to identify custom blocks.
			// Needed since processing of the block will lose track of which mine it came from.
			if ( pmEvent.getMine() != null ) {
				pmEvent.getSpigotBlock().setPrisonBlockTypes( pmEvent.getMine().getPrisonBlockTypes() );
			}
			
			// check all external events such as mcMMO and EZBlocks:
//			debugInfo.append( 
//					OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e ) );
			
			List<SpigotBlock> explodedBlocks = new ArrayList<>();
			pmEvent.setExplodedBlocks( explodedBlocks );
//    			String triggered = null;
			
//    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
//    							pmEvent.getMine(), sBlock, explodedBlocks, BlockEventType.blockBreak, triggered );
			Bukkit.getServer().getPluginManager().callEvent( pmEvent );
			if ( pmEvent.isCancelled() ) {
				pmEvent.getDebugInfo().append( 
						"(normal processing: PrisonMinesBlockBreakEvent was canceled by another plugin!) " );
			}
			else {
				
				// Cancel drops if so configured:
				if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
					
					cancelBy = EventListenerCancelBy.drops;
					
				}
				
				// doAction returns a boolean that indicates if the event should be canceled or not:
				if ( doAction( pmEvent ) ) {
//                	if ( doAction( sBlock, pmEvent.getMine(), pmEvent.getPlayer(), debugInfo ) ) {
					
					if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
						cancelBy = EventListenerCancelBy.event;
					}
					else {
						
						pmEvent.getDebugInfo().append( "(event not canceled) " );
					}
					
					finalizeBreakTheBlocks( pmEvent );
					
					doBlockEvents( pmEvent );
					
				}
				else {
					
					pmEvent.getDebugInfo().append( "(doAction failed without details) " );
				}
				
			}
			
			
			pmEvent.getDebugInfo().append( "(normal processing completed) " );
		}
		else {
			
			pmEvent.getDebugInfo().append( "(logic bypass) " );
		}
		return cancelBy;
	}



	
	/**
	 * <p>If the fortune level is zero, then this function will always return a value of one.
	 * </p>
	 *
	 * <p>If it is non-zero, then this function will return a value of one, plus an
	 * equally distributed chance of returning an additional 0 to fortuneLevel
	 * bonuses of that material.
	 * </p>
	 *
	 * <p>This applies to "all" materials within the mine, including chests, shulkers,
	 * signs etc...
	 * </p>
	 *
	 * @param fortuneLevel
	 * @return
	 */
	public int getDropCount(int fortuneLevel) {
		int j =
				fortuneLevel == 0 ?
						1 :
						1 + getRandom().nextInt(fortuneLevel + 1);

		return j;
	}


	protected boolean hasSilkTouch (SpigotItemStack itemInHand){
		boolean results = false;
		try {
			if ( itemInHand != null && itemInHand.getBukkitStack() != null && itemInHand.getBukkitStack().getEnchantments() != null ) {
				results = itemInHand.getBukkitStack().getEnchantments().containsKey(Enchantment.SILK_TOUCH);
			}
		}
		catch ( NullPointerException e ) {
			// Ignore. This happens when a TokeEnchanted tool is used when TE is not installed anymore.
			// It throws this exception:  Caused by: java.lang.NullPointerException: null key in entry: null=5
		}
		return results;
	}

	protected boolean hasFortune(SpigotItemStack itemInHand){
		boolean results = false;
		try {
			if ( itemInHand != null && itemInHand.getBukkitStack() != null && itemInHand.getBukkitStack().getEnchantments() != null ) {
				results = itemInHand.getBukkitStack().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS);
			}
		}
		catch ( NullPointerException e ) {
			// Ignore. This happens when a TokeEnchanted tool is used when TE is not installed anymore.
			// It throws this exception:  Caused by: java.lang.NullPointerException: null key in entry: null=5
		}
		return results;
	}
	
	protected short getFortune(SpigotItemStack itemInHand){
		short results = (short) 0;
		
		try {
			if ( itemInHand != null && 
					itemInHand.getBukkitStack() != null && 
					itemInHand.getBukkitStack().containsEnchantment( Enchantment.LOOT_BONUS_BLOCKS ) &&
					itemInHand.getBukkitStack().getEnchantments() != null ) {
				results = (short) itemInHand.getBukkitStack().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			}
		}
		catch ( NullPointerException e ) {
			// Ignore. This happens when a TokeEnchanted tool is used when TE is not installed anymore.
			// It throws this exception:  Caused by: java.lang.NullPointerException: null key in entry: null=5
		}
		
		int maxFortuneLevel = getInteger( AutoFeatures.fortuneMultiplierMax );
		if ( maxFortuneLevel > 0 && results > maxFortuneLevel ) {
			results = (short) maxFortuneLevel;
		}
		
		return results;
	}

	
    
    
//    @Override
//	public boolean doAction( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo ) {
//    	
//    	return processAutoEvents( pmEvent, debugInfo );
//	}
    
    
    /**
     * <p>This function overrides the doAction in OnBlockBreakEventListener and
     * this is only enabled when auto manager is enabled.
     * </p>
     * 
     */
    @Override
    public boolean doAction( PrisonMinesBlockBreakEvent pmEvent ) {
    	return applyAutoEvents( pmEvent );
    }
	

//	private boolean processAutoEvents( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo ) {
//		boolean cancel = false;
//		
//		if (isBoolean(AutoFeatures.isAutoManagerEnabled) && !pmEvent.getSpigotBlock().isEmpty() ) {
//			
//			
//			debugInfo.append( "(doAction autoManager processAutoEvent single-block) ");
//
//			
////			Output.get().logInfo( "#### AutoManager.applyAutoEvents: BlockBreakEvent: :: " + mine.getName() + "  " + 
////					"  blocks remaining= " + 
////					mine.getRemainingBlockCount() + " [" + block.toString() + "]"
////					);
//
//
//			int count = applyAutoEventsDetails( pmEvent, debugInfo );
//			
//			if ( count > 0 ) {
//				processBlockBreakage( pmEvent, count, true, debugInfo );
//
//    			cancel = true;
//			}
//			
//			checkZeroBlockReset( pmEvent.getMine() );
//	
//		}
//		
//		return cancel;
//	}


	
	
	private int applyAutoEventsDetails( PrisonMinesBlockBreakEvent pmEvent ) {
		int totalDrops = 0;
		
		Player player = pmEvent.getPlayer();
		Mine mine = pmEvent.getMine();
		
		SpigotItemStack itemInHand = SpigotCompatibility.getInstance().getPrisonItemInMainHand( player );

		
		boolean isLoreEnabled = isBoolean( AutoFeatures.isLoreEnabled );
		
		boolean lorePickup = isLoreEnabled && checkLore( itemInHand, getMessage( AutoFeatures.lorePickupValue ) );
		boolean loreSmelt = isLoreEnabled && checkLore( itemInHand, getMessage( AutoFeatures.loreSmeltValue) );
		boolean loreBlock = isLoreEnabled && checkLore( itemInHand, getMessage( AutoFeatures.loreBlockValue ) );
		
		boolean permPickup = player.isPermissionSet( getMessage( AutoFeatures.permissionAutoPickup ));
		boolean permSmelt = player.isPermissionSet( getMessage( AutoFeatures.permissionAutoSmelt ));
		boolean permBlock = player.isPermissionSet( getMessage( AutoFeatures.permissionAutoBlock ));
		
		boolean isAutoFeaturesEnabled = isBoolean( AutoFeatures.isAutoFeaturesEnabled );
		
		boolean configPickup = isAutoFeaturesEnabled && isBoolean( AutoFeatures.autoPickupEnabled );
		boolean configSmelt = isAutoFeaturesEnabled && isBoolean( AutoFeatures.autoSmeltEnabled );
		boolean configBlock = isAutoFeaturesEnabled && isBoolean( AutoFeatures.autoBlockEnabled );
		
		
		boolean configNormalDrop = isBoolean( AutoFeatures.handleNormalDropsEvents );
		boolean configNormalDropSmelt = isBoolean( AutoFeatures.normalDropSmelt );
		boolean configNormalDropBlock = isBoolean( AutoFeatures.normalDropBlock );
		
		
		boolean limit2minesPickup = isBoolean( AutoFeatures.pickupLimitToMines );
		boolean limit2minesSmelt = isBoolean( AutoFeatures.smeltLimitToMines );
		boolean limit2minesBlock = isBoolean( AutoFeatures.blockLimitToMines );
		
		boolean isAutoPickup = lorePickup || configPickup || permPickup;
		
		isAutoPickup = (mine != null || mine == null && !limit2minesPickup) && isAutoPickup;
		
		boolean isAutoSmelt = loreSmelt || configSmelt || permSmelt;
		
		isAutoSmelt = (mine != null || mine == null && !limit2minesSmelt) && isAutoSmelt;
		
		boolean isAutoBlock = loreBlock || configBlock || permBlock;
		
		isAutoBlock = (mine != null || mine == null && !limit2minesBlock) && isAutoBlock;
		
		if ( Output.get().isDebug( DebugTarget.blockBreak ) ) {
			
			pmEvent.getDebugInfo().append( "(applyAutoEvents: " )
				.append( pmEvent.getSpigotBlock().getBlockName() );
			
			if ( !isAutoFeaturesEnabled ) {
				pmEvent.getDebugInfo().append("isAutoFeaturesEnabled=false (disabled)");
			}
			else {
				
				pmEvent.getDebugInfo()
				.append( " Pickup [")
				.append( isAutoPickup ? "enabled: " : "disabled:" )
				.append( lorePickup ? "lore " : "" )
				.append( permPickup ? "perm " : "" )
				.append( configPickup ? "config " : "" )
				.append( limit2minesPickup ? "limit2mines" : "noLimit" )
				.append( "] ")
				
				.append( " Smelt [")
				.append( isAutoSmelt ? "enabled: " : "disabled:" )
				.append( loreSmelt ? "lore " : "" )
				.append( permSmelt ? "perm " : "" )
				.append( configSmelt ? "config " : "" )
				.append( limit2minesSmelt ? "limit2mines" : "noLimit" )
				.append( "] ")
				
				.append( " Block [")
				.append( isAutoBlock ? "enabled: " : "disabled:" )
				.append( loreBlock ? "lore " : "" )
				.append( permBlock ? "perm " : "" )
				.append( configBlock ? "config " : "" )
				.append( limit2minesBlock ? "limit2mines" : "noLimit" )
				.append( "] ");
				
			}
			
			pmEvent.getDebugInfo()
				.append( ")" );
		}
		
		// NOTE: Using isPermissionSet so players that are op'd to not auto enable everything.
		//       Ops will have to have the perms set to actually use them.
				
		// AutoPickup
		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.pickupLimitToMines )) ) {
			
			if ( isAutoPickup ) {
				
				// processing auto pickup
				totalDrops = autoFeaturePickup( pmEvent, isAutoSmelt, isAutoBlock, pmEvent.getDebugInfo() );
//			count = autoFeaturePickup( pmEvent.getSpigotBlock(), player, itemInHand, isAutoSmelt, isAutoBlock, debugInfo );
				
				// Cannot set to air yet, or auto smelt and auto block will only get AIR:
//			autoPickupCleanup( block, count );
			}
			else {
				// Need to check to see if normal drops should be processed:
				
				if ( configNormalDrop ) {
					pmEvent.getDebugInfo()
						.append( "(NormalDrop handling enabled: " )
						.append( "normalDropSmelt[" )
						.append( configNormalDropSmelt ? "enabled" : "disabled" )
						.append( "] " )
						.append( "normalDropBlock[" )
						.append( configNormalDropBlock ? "enabled" : "disabled" )
						.append( "] " )
						.append( ")" );
					
					// process normal drops here:
					
					totalDrops = calculateNormalDrop( pmEvent );

				}
				else {
					pmEvent.getDebugInfo().append(" [Warning: normalDrop handling is disabled] " );
				}
				
			}
			
		}
		
//		else {
//			
//			// Warning!!  The following should NOT happen if auto pickup is disabled.  That's what 
//			// 
//			
//			// Because auto pickup is not enabled, auto smelt and auto block cannot be applied through 
//			// the picked up item stacks.  Therefore, if auto smelt or auto block is enabled with 
//			// auto pickup being disabled, they will act on the player's full inventory.
//			
//			// So breaking the block could cause a drop but the drop may not enter the players inventory 
//			// to be included in the smelt or block processing.  Therefore odd behaviors could be seen.  
//			// But having those enabled without auto pickup is beyond the scope of support for prison... 
//			// you get what you got. 
//			
//			XMaterial source = SpigotUtil.getXMaterial( block.getPrisonBlock() );
//			
//			// AutoSmelt
//			if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoSmeltLimitToMines )) &&
//					isAutoSmelt ){
//				
//				// Smelting needs to change the source to the smelted target so auto block will work:
//				source = autoFeatureSmelt( player, source );
//			}
//			
//			// AutoBlock
//			if ( (mine != null || mine == null && !isBoolean( AutoFeatures.autoBlockLimitToMines )) &&
//					isAutoBlock ) {
//				
//				autoFeatureBlock( player, source );
//			}
//			
//		}
		
		
		
//		// AutoPickup - Clean up (set block to air)
//		if ( (mine != null || mine == null && !isBoolean( AutoFeatures.pickupLimitToMines )) &&
//				isAutoPickup ) {
//			
//			autoPickupCleanup( pmEvent.getSpigotBlock(), count );
//			
//		}

		
		return totalDrops;
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
	private boolean applyAutoEvents( PrisonMinesBlockBreakEvent pmEvent ) {
//		boolean success = false;
		
		int totalDrops = applyAutoEventsDetails( pmEvent );

		pmEvent.getDebugInfo().append( "(autoEvents totalDrops: " + totalDrops + ") ");

		return applyDropsBlockBreakage( pmEvent, totalDrops );
		
//		debugInfo.append( "(doAction autoManager applyAutoEvents multi-blocks: " + pmEvent.getExplodedBlocks().size() + ") ");
//		
//		
//		// The explodedBlocks list have already been validated as being within the mine:
//		boolean applyExhaustion = true;
//		
//		// First process the block that was hit by the player:
//		if ( pmEvent.getTargetBlock() != null ) {
//			
//			processBlockBreakage( pmEvent, pmEvent.getTargetBlock(), 1, applyExhaustion, debugInfo );
//			applyExhaustion = false;
//			
//		}
//
//		// Then process all of the other blocks that were included in the explosion event, if there was one:
//		for ( MineTargetPrisonBlock targetBlock : pmEvent.getTargetExplodedBlocks() ) 
//		{
//			
//			processBlockBreakage( pmEvent, targetBlock, 1, applyExhaustion, debugInfo );
//		}
//		
//		autosellPerBlockBreak( pmEvent.getPlayer() );
//		
//		if ( pmEvent.getMine() != null ) {
//			checkZeroBlockReset( pmEvent.getMine() );
//		}
//		
//		if ( totalDrops > 0 ) {
//			success = true;
//		}
//		
//		return success;
	}
	
	
	
	/*
	 * Drops are wrong with old 1.14.4 releases of spigot
	 * but got fixed in newer versions.
	 *
	 * For older versions, a good way to get the right drops would be to use 
	 * BlockDropItemEvent.getItems(), but it's deprecated
	 * */
	protected int autoPickup( PrisonMinesBlockBreakEvent pmEvent,
							boolean isAutoSmelt, boolean isAutoBlock, StringBuilder debugInfo ) {
		//, BlockBreakEvent e ) {
		int count = 0;

		Player player = pmEvent.getPlayer();
		SpigotItemStack itemInHand = pmEvent.getItemInHand();
//		SpigotBlock block = pmEvent.getSpigotBlock();
		List<SpigotItemStack> drops = pmEvent.getBukkitDrops();
				
		// The following may not be the correct drops for all versions of spigot,
		// plus there are some extra items, such as flint, that will never be dropped.
//		List<SpigotItemStack> drops = new ArrayList<>( SpigotUtil.getDrops(block, itemInHand) );
		
		
		// This clears the drops for the given block, so if the event is not canceled, it will
		// not result in duplicate drops.
//		if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//			block.clearDrops();
//		}
		
		
		if (drops != null && drops.size() > 0 ) {
			
			debugInfo.append( "[autoPickupDrops]" );
			
			// Need better drop calculation that is not using the getDrops function.
			
			calculateSilkTouch( itemInHand, drops );
			
			// Adds in additional drop items: Add Flint with gravel drops:
			calculateDropAdditions( itemInHand, drops );
			
			
			// Add fortune to the items in the inventory
			if ( isBoolean( AutoFeatures.isCalculateFortuneEnabled ) ) {
				short fortuneLevel = getFortune(itemInHand);

				debugInfo.append( "(calculateFortune: fort " + fortuneLevel + ")" );
				
				for ( SpigotItemStack itemStack : drops ) {
					
					// calculateFortune directly modifies the quantity on the blocks ItemStack:
					calculateFortune( itemStack, fortuneLevel );
				}
			}
			
			
			// NOTE: This should be done after applying fortune, otherwise it will get misreadings.
			// Merge drops so each item is only represented once before adding to the player's inventory
			drops = mergeDrops( drops );
			
			
			// Smelt
			if ( isAutoSmelt ) {
				debugInfo.append( "(autoSmelting: itemStacks)" );
				normalDropSmelt( drops );
			}
			
			
			// Block
			if ( isAutoBlock ) {
				debugInfo.append( "(autoBlocking: itemStacks)" );
				normalDropBlock( drops );
			}
			
			String mineName = pmEvent.getMine() == null ? null : pmEvent.getMine().getName();
			
			// PlayerCache log block breaks:
			TreeMap<String, Integer> targetBlockCounts = pmEvent.getTargetBlockCounts();
			for ( Entry<String, Integer> targetBlockCount : targetBlockCounts.entrySet() )
			{
				
				PlayerCache.getInstance().addPlayerBlocks( pmEvent.getSpigotPlayer(), mineName, 
						targetBlockCount.getKey(), targetBlockCount.getValue().intValue() );
			}
			
			
			DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.0000");
			DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
			
			double autosellTotal = 0;
			double autosellUnsellableCount = 0;
			
			long nanoTime = 0L;
			
			boolean isSellallEnabled = SpigotPrison.getInstance().isSellAllEnabled();
			
			// This is true if the player cannot toggle the autosell, and it's
			// true if they can, and the have it enabled:
			boolean isPlayerAutosellEnabled = isSellallEnabled &&
					SellAllUtil.get() != null && 
					SellAllUtil.get().checkIfPlayerAutosellIsActive( 
							pmEvent.getSpigotPlayer().getWrapper() ) 
					;
			
			
			for ( SpigotItemStack itemStack : drops ) {
				
				count += itemStack.getAmount();
				
						
				// Try to autosell if enabled:
				if ( isSellallEnabled &&
						
						(isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled) &&
								isPlayerAutosellEnabled || 
						pmEvent.isForceAutoSell() || 
						!player.isOp() && !"disable".equalsIgnoreCase( getMessage( AutoFeatures.permissionAutoSellPerBlockBreakEnabled ) ) &&
						player.hasPermission( getMessage( AutoFeatures.permissionAutoSellPerBlockBreakEnabled ) )) && 
						isPlayerAutosellEnabled ) {
					
					final long nanoStart = System.nanoTime();
					double amount = SellAllUtil.get().sellAllSell( player, itemStack, false, false, true );
					final long nanoStop = System.nanoTime();
					nanoTime += nanoStop - nanoStart;
					
					autosellTotal += amount;
					
					PlayerCache.getInstance().addPlayerEarnings( pmEvent.getSpigotPlayer(), 
							amount, mineName );

					if ( amount != 0 ) {
						debugInfo.append( "(sold: " + itemStack.getName() + " qty: " + itemStack.getAmount() + 
								" value: " + dFmt.format( amount ) + ") ");
						
						// Set to zero quantity since they have all been sold.
						itemStack.setAmount( 0 );
					}
					else {
						
						// Unable to sell since amount was zero.  Not configured to be sold.
						debugInfo.append( "(unsellable: " + itemStack.getName() + " qty: " + itemStack.getAmount() + ") ");
						autosellUnsellableCount += itemStack.getAmount();
					}
					
				}
				
				
				
				// Add blocks to player's inventory IF autosell was unable to sell the item stack, hence
				// it will have an amount of more than 0.
				if ( itemStack.getAmount() > 0 ) {
					
					if ( Output.get().isDebug() && isSellallEnabled ) {
						
						// Just get the calculated value for the drops... do not sell:
						double amount = SellAllUtil.get().getSellMoney( player, itemStack );
						autosellTotal += amount;
						
						debugInfo.append( "(Debug-unsold-value-check: " + itemStack.getName() + 
								" qty: " + itemStack.getAmount() + " value: " + dFmt.format( amount ) + ") ");
					}
					
					HashMap<Integer, SpigotItemStack> extras = SpigotUtil.addItemToPlayerInventory( player, itemStack );
				
					
					// Warning: The following is now obsolete since there is now a sellall function that will sell on a 
					//          per SpigotItemStack so it eliminates a ton of overhead.  It also supports thousands of 
					//          items per stack.

//					if ( extras.size() > 0 && autosellPerBlockBreak( player ) ) {
//						
//						// Try to add the extras back to the player's inventory if they had autosellPerBlockBrean enabled:
//						for ( SpigotItemStack extraItemStack : extras.values() ) {
//							
//							HashMap<Integer, SpigotItemStack> extras2 = SpigotUtil.addItemToPlayerInventory( player, extraItemStack );
//							
//							autosellPerBlockBreak( player );
//							
//							// If the remainder of the extras still as too much, then just drop them and move on:
//							dropExtra( extras2, player );
//						}
//						extras.clear();
//					}
					
					
					dropExtra( extras, player, debugInfo );
//					dropExtra( player.getInventory().addItem(itemStack), player, block );
				}
				
				
			} 
			
			if ( count > 0 || autosellTotal > 0 ) {
				
				debugInfo.append( "[autoPickupDrops total: qty: " + count + " value: " + dFmt.format( autosellTotal ) + 
						"  unsellableCount: " + autosellUnsellableCount );
				
				if ( nanoTime > 0 ) {
					final double autoSellTimeMs = ( nanoTime / 1000000.0d );
					debugInfo.append( " autosellTiming: " )
						.append( fFmt.format( autoSellTimeMs ) )
						.append( " ms" );
				}
				
				debugInfo.append( " ] " );
			}
			
			
//			if ( !isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled) && 
//					!pmEvent.isForceAutoSell() ) {
//				
//				autosellPerBlockBreak( player );
//			}
			
//				autoPickupCleanup( player, itemInHand, count );
		}
		return count;
	}


	



	public int calculateNormalDrop( PrisonMinesBlockBreakEvent pmEvent ) {
		
		// Count should be the total number of items that are to be "dropped".
		// So effectively it will be the sum of all bukkitDrops counts.
		int count = 0;

//		// The following may not be the correct drops for all versions of spigot,
//		// plus there are some extra items, such as flint, that will never be dropped.
//		List<SpigotItemStack> drops = new ArrayList<>( SpigotUtil.getDrops(block, itemInHand) );
//
//		
//		// This clears the drops for the given block, so if the event is not canceled, it will
//		// not result in duplicate drops.
//		if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//			block.clearDrops();
//		}
		
		List<SpigotItemStack> drops = pmEvent.getBukkitDrops();
		
		
		if (drops != null && drops.size() > 0 ) {
			
			pmEvent.getDebugInfo().append( "[normalDrops]" );

			// Need better drop calculation that is not using the getDrops function.
			short fortuneLevel = getFortune( pmEvent.getItemInHand() );

			calculateSilkTouch( pmEvent.getItemInHand(), drops );
			
			// Adds in additional drop items: Add Flint with gravel drops:
			calculateDropAdditions( pmEvent.getItemInHand(), drops );

			
			if ( isBoolean( AutoFeatures.isCalculateFortuneEnabled ) ) {

				// calculate fortune before smelting and blocking:
				for ( SpigotItemStack itemStack : drops ) {
					
					// calculateFortune directly modifies the quantity on the blocks ItemStack:
					calculateFortune( itemStack, fortuneLevel );
				}
			}
			
			
			// Merge drops so each item is only represented once, but has counts.
			drops = mergeDrops( drops );
			
			
			if ( isBoolean( AutoFeatures.normalDropSmelt ) ) {
				pmEvent.getDebugInfo().append( "(normSmelting: itemStacks)" );
				normalDropSmelt( drops );
			}
			
			
			if ( isBoolean( AutoFeatures.normalDropBlock ) ) {
				pmEvent.getDebugInfo().append( "(normBlocking: itemStacks)" );
				normalDropBlock( drops );
			}
			
			
			String mineName = pmEvent.getMine() == null ? null : pmEvent.getMine().getName();
			
			// PlayerCache log block breaks:
			TreeMap<String, Integer> targetBlockCounts = pmEvent.getTargetBlockCounts();
			for ( Entry<String, Integer> targetBlockCount : targetBlockCounts.entrySet() )
			{
				
				PlayerCache.getInstance().addPlayerBlocks( pmEvent.getSpigotPlayer(), mineName, 
						targetBlockCount.getKey(), targetBlockCount.getValue().intValue() );
			}
			
			
			
			double autosellTotal = 0;
			
			// Drop the items where the original block was located:
			for ( SpigotItemStack itemStack : drops ) {

				count += itemStack.getAmount();
				
				// Since this is not auto pickup, then only autosell if set in the pmEvent:
				if ( pmEvent.isForceAutoSell() ) {
					
					Player player = pmEvent.getPlayer();

					double amount = SellAllUtil.get().sellAllSell( player, itemStack, false, false, true );
					autosellTotal += amount;
					
					if ( amount != 0 ) {
						pmEvent.getDebugInfo().append( "(sold: " + itemStack.getName() + " qty: " + itemStack.getAmount() + " value: " + amount + ") ");
						
						// Set to zero quantity since they have all been sold.
						itemStack.setAmount( 0 );
					}
					
				}
				
				if ( itemStack.getAmount() != 0 ) {
					
					if ( Output.get().isDebug() ) {
						
						// Just get the calculated value for the drops... do not sell:
						Player player = pmEvent.getPlayer();
					
						double amount = SellAllUtil.get().sellAllSell( player, itemStack, true, false, false );
						autosellTotal += amount;
						
						pmEvent.getDebugInfo().append( "(adding: " + itemStack.getName() + " qty: " + itemStack.getAmount() + " value: " + amount + ") ");
					}
					
					dropAtBlock( itemStack, pmEvent.getSpigotBlock() );
				}
				
				
			}

			
			if ( count > 0 || autosellTotal > 0 ) {
				
				pmEvent.getDebugInfo().append( "[normalDrops total: qty: " + count + " value: " + autosellTotal + ") ");
				
			}
			
			
//			// Break the block and change it to air:
//			if ( !pmEvent.getSpigotBlock().isEmpty() ) {
//				
//				if ( isBoolean( AutoFeatures.applyBlockBreaksThroughSyncTask ) ) {
//					
//					AutoManagerBreakBlockTask.submitTask( pmEvent.getSpigotBlock() );
//				}
//				else {
//					
//					pmEvent.getSpigotBlock().setPrisonBlock( PrisonBlock.AIR );
//				}
//				
//			}
		}
		
		return count;
	}

	

	
//	public void playerSmelt( SpigotPlayer player ) {
//		
//		List<XMaterial> smelts = new ArrayList<>();
//		
//		smelts.add( XMaterial.COBBLESTONE );
//		smelts.add( XMaterial.GOLD_ORE );
//		smelts.add( XMaterial.NETHER_GOLD_ORE );
//		smelts.add( XMaterial.DEEPSLATE_GOLD_ORE );
//		smelts.add( XMaterial.RAW_GOLD );
//		
//		smelts.add( XMaterial.IRON_ORE );
//		smelts.add( XMaterial.DEEPSLATE_IRON_ORE );
//		smelts.add( XMaterial.RAW_IRON );
//		
//		smelts.add( XMaterial.COAL_ORE );
//		smelts.add( XMaterial.DEEPSLATE_COAL_ORE );
//		
//		smelts.add( XMaterial.DIAMOND_ORE );
//		smelts.add( XMaterial.DEEPSLATE_DIAMOND_ORE );
//		
//		smelts.add( XMaterial.EMERALD_ORE );
//		smelts.add( XMaterial.DEEPSLATE_EMERALD_ORE );
//		
//		smelts.add( XMaterial.LAPIS_ORE );
//		smelts.add( XMaterial.DEEPSLATE_LAPIS_ORE );
//		
//		smelts.add( XMaterial.REDSTONE_ORE );
//		smelts.add( XMaterial.DEEPSLATE_REDSTONE_ORE );
//		
//		smelts.add( XMaterial.NETHER_QUARTZ_ORE );
//		smelts.add( XMaterial.ANCIENT_DEBRIS );
//		
//		smelts.add( XMaterial.COPPER_ORE );
//		smelts.add( XMaterial.DEEPSLATE_COPPER_ORE );
//		smelts.add( XMaterial.RAW_COPPER );
//		
//		
//		for ( XMaterial xMat : smelts ) {
//			autoFeatureSmelt( player.getWrapper(), xMat );
//		}
//
//	}
	
//	public void playerBlock( SpigotPlayer player ) {
//		
//		List<XMaterial> blocks = new ArrayList<>();
//		
//		blocks.add( XMaterial.GOLD_INGOT );
//		blocks.add( XMaterial.IRON_INGOT );
//		blocks.add( XMaterial.COAL );
//		blocks.add( XMaterial.DIAMOND );
//		blocks.add( XMaterial.REDSTONE );
//		blocks.add( XMaterial.EMERALD );
//		blocks.add( XMaterial.QUARTZ );
//		blocks.add( XMaterial.PRISMARINE_SHARD );
//		blocks.add( XMaterial.SNOW_BLOCK );
//		blocks.add( XMaterial.GLOWSTONE_DUST );
//		blocks.add( XMaterial.LAPIS_LAZULI );
//		
//		
//		for ( XMaterial xMat : blocks ) {
//			autoFeatureBlock( player.getWrapper(), xMat );
//		}
//		
//	}
	
//	/**
//	 * <p>The List of drops must have only one ItemStack per block type (name).
//	 * This function combines multiple occurrences together and adds up their 
//	 * counts to properly represent the total quantity in the original drops collection
//	 * that had duplicate entries.
//	 * </p>
//	 * 
//	 * @param List of SpigotItemStack drops with duplicate entries
//	 * @return List of SpigotItemStack drops without duplicates
//	 */
//	private List<SpigotItemStack> mergeDrops( List<SpigotItemStack> drops )
//	{
//		TreeMap<String,SpigotItemStack> results = new TreeMap<>();
//
//		for ( SpigotItemStack drop : drops ) {
//			String key = drop.getName();
//			if ( !results.containsKey( key ) ) {
//				results.put( key, drop );
//			}
//			else {
//				SpigotItemStack sItemStack = results.get( key );
//				
//				sItemStack.setAmount( sItemStack.getAmount() + drop.getAmount() );
//			}
//		}
//		
//		return new ArrayList<>( results.values() );
//	}


//	protected void autoPickupCleanupX( SpigotBlock block, int count )
//	{
//		// Auto pickup has been successful. Now clean up.
//		if ( count > 0 ) {
//			
//
////			// Set the broken block to AIR and cancel the event
//			if ( !block.isEmpty() ) {
//				
//				if ( isBoolean( AutoFeatures.applyBlockBreaksThroughSyncTask ) ) {
//					
//					// submit a task to change the block to air:
//					AutoManagerBreakBlockTask.submitTask( block );
//				}
//				
//				else {
//					
//					block.setPrisonBlock( PrisonBlock.AIR );
//				}
//			}
//			
//		}
//	}
	
	


	


	protected boolean checkLore( SpigotItemStack itemInHand, String loreValue ) {
		boolean results = false;
		
		double lorePercent = getLoreValue( itemInHand, loreValue );

		results = lorePercent == 100.0 ||
					lorePercent > 0 && 
					lorePercent <= getRandom().nextDouble() * 100;
		
		return results;
	}

	
//
//	protected void autoSmelt( boolean autoSmelt, XMaterial source, XMaterial target, Player p ) {
//
//		if ( autoSmelt && source != null && target != null ) {
//			
//			HashMap<Integer, SpigotItemStack> overflow = SpigotUtil.itemStackReplaceItems( p, source, target, 1 );
//			dropExtra( overflow, p );
//
//		}
//	}
//	
//	
//	protected void autoBlock( boolean autoBlock, XMaterial source, XMaterial target, Player p  ) {
//		autoBlock(autoBlock, source, target, 9, p );
//	}
//
//	
//	protected void autoBlock( boolean autoBlock, XMaterial source, XMaterial target, int ratio, Player p  ) {
//
//		if ( autoBlock && source != null && target != null ) {
//			HashMap<Integer, SpigotItemStack> overflow = SpigotUtil.itemStackReplaceItems( p, source, target, ratio );
//			dropExtra( overflow, p );
//			
//		}
//	}



	/**
	 * <p>If the player does not have any more inventory room for the current items, then
	 * it will drop the extra.
	 * </p>
	 * @param extra
	 * @param player
	 * @param block
	 */
	protected void dropExtra( HashMap<Integer, SpigotItemStack> extra, Player player, StringBuilder debugInfo ) {

		if ( SpigotPrison.getInstance().isSellAllEnabled() && (
				 extra != null && extra.size() > 0 ||
				 player.getInventory().firstEmpty() == -1
				)) {
			
//			if ( SpigotPrison.getInstance().isSellAllEnabled() ) 
			{
				
				
				SellAllUtil sellAllUtil = SellAllUtil.get();
				
				// On inventory is full, will auto sell if auto sell is enabled in either
				// the sellall configs, or the auto feature configs.
				if (sellAllUtil != null && (
						sellAllUtil.isAutoSellEnabled ||
						isBoolean(AutoFeatures.isAutoSellIfInventoryIsFull) )) {
					
					
					if ( sellAllUtil.checkIfPlayerAutosellIsActive(player) ) {
						
						boolean saNote = sellAllUtil.isAutoSellNotificationEnabled;

						List<Double> amounts = new ArrayList<>();
						
						final long nanoStart = System.nanoTime();
						
						// bypass delay (cooldown), no sound
						SellAllUtil.get().sellAllSell(player, false, !saNote, saNote, false, false, false, amounts );
						final long nanoStop = System.nanoTime();
						long nanoTime = nanoStop - nanoStart;
						
						double amount = 0;
						for ( Double amt : amounts ) {
							amount += amt;
						}
						
						// Since sellall on inventory full, sell the extras too...
						if ( extra.size() > 0 ) {
							for ( Entry<Integer, SpigotItemStack> drop : extra.entrySet() )
							{
								SpigotItemStack itemStack = drop.getValue();
								final long nanoStart2 = System.nanoTime();
								double amount2 = SellAllUtil.get().sellAllSell( player, itemStack, false, false, true );
								final long nanoStop2 = System.nanoTime();
								
								nanoTime += nanoStop2 - nanoStart2;
								
								amount += amount2;
								
							}
							
						}
						
						if ( amount > 0d ) {
							
							DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.0000");
							DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
							
							debugInfo.append( "[dropExtra sellall: value: " + dFmt.format( amount )  );
							
							if ( nanoTime > 0 ) {
								final double autoSellTimeMs = ( nanoTime / 1000000.0d );
								debugInfo.append( " sellallTiming: " )
									.append( fFmt.format( autoSellTimeMs ) )
									.append( " ms" );
							}
							
							debugInfo.append( " ] " );
							
							SpigotPlayer sPlayer = new SpigotPlayer( player );
							PlayerAutoRankupTask.autoSubmitPlayerRankupTask( sPlayer, debugInfo );
						}
						
					}
					
					
//				if (sellAllUtil.isAutoSellPerUserToggleable) {
//					if (sellAllUtil.isPlayerAutoSellEnabled(player)) {
//						if (sellAllUtil.isAutoSellNotificationEnabled) {
//							SellAllUtil.get().sellAllSell(player, false, false, true, true, false, true);
//						} else {
//							SellAllUtil.get().sellAllSell(player, false, true, false, false, false, true);
//						}
//					}
//				} else {
//					if (sellAllUtil.isAutoSellNotificationEnabled) {
//						SellAllUtil.get().sellAllSell(player, false, false, true, true, false, true);
//					} else {
//						SellAllUtil.get().sellAllSell(player, false, true, false, false, false, true);
//					}
//				}
					
					// Now that something might have been sold, try to add all the extra inventory items back to the
					// player's inventory so it is not lost then pass the moreExtras along to be handled as the
					// configurations require.
					HashMap<Integer, SpigotItemStack> moreExtras = new HashMap<>();
					for (SpigotItemStack itemStack : extra.values()) {
						moreExtras.putAll(SpigotUtil.addItemToPlayerInventory(player, itemStack));
					}
					extra = moreExtras;
				}
			}
			
			

			// drop the player's items if they should be dropped. Also check to see if 
			// the player should be notified.  Ignore zero count itemStacks.
			boolean needToNotify = false;
			for (SpigotItemStack itemStack : extra.values()) {
				
				if ( itemStack.getAmount() > 0 ) {

					if (isBoolean(AutoFeatures.dropItemsIfInventoryIsFull)) {
						
						SpigotUtil.dropPlayerItems(player, itemStack);
					}
					
					needToNotify = true;
				}
			}
			

			// Only send one notification, not matter how many stacks need to be dropped or lost:
			if ( needToNotify ) {
				if (isBoolean(AutoFeatures.dropItemsIfInventoryIsFull)) {
					
					notifyPlayerThatInventoryIsFull(player);
				} else {
					notifyPlayerThatInventoryIsFullLosingItems(player);
				}
			}
			
		}
		
	}
	
//	private boolean isBoolean( Configuration sellAllConfig, String config ) {
//		String configValue = sellAllConfig.getString( config );
//		return configValue != null && configValue.equalsIgnoreCase( "true" );
//	}
	
	private void dropAtBlock( SpigotItemStack itemStack, SpigotBlock block ) {
		
		SpigotUtil.dropItems( block, itemStack );
	}

	private void notifyPlayerThatInventoryIsFull( Player player ) {
		String message = inventoryIsFullMsg();

		// AutoFeatures.inventoryIsFull
		notifyPlayerWithSound( player, message );
	}

//	@SuppressWarnings( "unused" )
//	private void notifyPlayerThatInventoryIsFullDroppingItems( Player player ) {
//		notifyPlayerWithSound( player, AutoFeatures.inventoryIsFullDroppingItems );
//	}

	private void notifyPlayerThatInventoryIsFullLosingItems( Player player ) {
		
		String message = inventoryIsFullLosingItemsMsg();
		
		// AutoFeatures.inventoryIsFullLosingItems
		notifyPlayerWithSound( player, message );
	}

	private void notifyPlayerWithSound( Player player, String message ) {
		
//		String message = getMessage( AutoFeatures messageId );

		// Play sound when full
		if (isBoolean(AutoFeatures.playSoundIfInventoryIsFull)) {
//	not used:		Prison.get().getMinecraftVersion() ;

			// This hard coding the Sound enum causes failures in spigot 1.8.8 since it does not exist:
			Sound sound = null;
			
			String soundName = getMessage(AutoFeatures.playSoundIfInventoryIsFullSound);
			if ( soundName != null && soundName.trim().length() > 0 ) {
				
				sound = getSound( soundName );
			}
			
			
			if ( sound == null ) {
				
				if ( new BluesSpigetSemVerComparator().compareMCVersionTo( "1.9.0" ) < 0 ) {
					
					// 1.8.x
					sound = getSound("NOTE_PLING");
				}
				else if ( new BluesSpigetSemVerComparator().compareMCVersionTo( "1.13.0" ) < 0 ) {
					
					// 1.9.x through 1.12.x
					sound = getSound("BLOCK_NOTE_PLING");
				}
				
				else {
					// 1.13.x and up:
					
					sound = getSound("BLOCK_NOTE_BLOCK_PLING");
				}
			}
			
//			try {
//				sound = Sound.valueOf("ANVIL_USE"); // pre 1.9 sound
//			} catch(IllegalArgumentException e) {
//				sound = Sound.valueOf("BLOCK_NOTE_PLING "); // post 1.9 sound
//			}

			float volume = (float) getDouble(AutoFeatures.playSoundIfInventoryIsFullSoundVolume);
			float pitch = (float) getDouble(AutoFeatures.playSoundIfInventoryIsFullSoundPitch);
//			player.playSound(player.getLocation(), sound, volume, pitch);
//			player.playSound(player.getLocation(), sound, 4F, 1F);
			
			
			player.getWorld().playSound( player.getLocation(), sound, volume, pitch );
			
		}

		
		if ( isBoolean( AutoFeatures.actionBarMessageIfInventoryIsFull ) ) {
			
			(new SpigotPlayer( player )).setActionBar( message );
		}
		else {
			
			player.sendMessage( message );
		}

		// holographic display for showing full inventory does not work well.
//		if ( isBoolean( AutoFeatures.hologramIfInventoryIsFull ) ) {
//			displayMessageHologram( block, message , player);
//		}
//		else {
//		actionBarVersion(player, message);
//		}
	}

	private Sound getSound( String soundName ) {
		Sound results = null;
		Sound altSound = null;
		
		for ( Sound s : Sound.values() ) {
			if ( altSound == null && s.name().toLowerCase().contains( "plink" ) ) {
				altSound = s;
			}
			if ( s.name().equalsIgnoreCase( soundName ) ) {
				results = s;
				break;
			}
		}
		
		if ( results == null && altSound != null ) {
			results = altSound;
		}
		
		return results;
	}
	
//	private void actionBarVersion(Player player, String message) {
//		
//		PlayerMessagingTask.submitTask( player, MessageType.actionBar, message );
//		
////		SpigotCompatibility.getInstance().sendActionBar( player, message );
//		
////		if (new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0) {
////			displayActionBarMessage(player, message);
////		}
////		else {
////			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(SpigotPrison.format(message)));
////		}
//	}

	/**
	 * This is not usable since it not only prevents the player from mining when it is
	 * displayed, but it cannot be seen since it is shown above the player's head.
	 *
	 * The player can continue to mine other materials even though one kind of resource
	 * be full.
	 *
	 * @param block
	 * @param message
	 * @param p
	 */
	@SuppressWarnings( "unused" )
	private void displayMessageHologram(Block block, String message, Player p){
		ArmorStand as = (ArmorStand) block.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		as.setCustomName(SpigotPrison.format(message));
		Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), as::remove, (7L * 20L));
	}

//	private void displayActionBarMessage(Player player, String message) {
//		SpigotPlayer prisonPlayer = new SpigotPlayer(player);
//		Prison.get().getPlatform().showActionBar(prisonPlayer, message, 80);
//	}


	/**
	 * <p>Using the ItemLoreEnables, check to see if the item in the hand has the
	 * specified Lore.  If so, then return 100.0 if just the lore, otherwise
	 * if it has a number to the right of the Lore, convert it to a double and
	 * return it.
	 * </p>
	 *
	 * <p>The only valid value to follow the lore, can only be a double number,
	 * or an integer.  For example 1.234, 50, 75.567, 99.0, or 100.0.
	 * Any value less than 0 will be zero, which is the same as no lore (disabled).
	 * Any value greater than 100 will be 100.0. No values following the lore will
	 * be treated as 100.0 percent.  Do not include percent sign or unit of measure.
	 * </p>
	 *
	 * @param loreEnabler
	 * @param player
	 * @return Percent chance of Lore enablement.
	 */
	protected double doesItemHaveAutoFeatureLore( ItemLoreEnablers loreEnabler, Player player ) {
		double results = 0.0;

		ItemStack itemInHand = SpigotCompatibility.getInstance().getItemInMainHand( player );
		if ( itemInHand != null && itemInHand.getType() != Material.AIR &&
				itemInHand.getItemMeta() != null ) { // (itemInHand.hasItemMeta()) { NOTE: hasItemMeta() always returns nulls
			
			ItemMeta meta = itemInHand.getItemMeta();
			
			if ( meta != null && meta.hasLore()) { 
				for (String lore : meta.getLore()) {
					if (lore.startsWith( loreEnabler.name())) {

						// Lore detected so set default to 100%:
						results = 100.0;
						
						String value = lore.replace( loreEnabler.name(), "" ).trim();

						if (value.length() > 0) {
							
							// Content has been found after the lore's name. If it is a number, then
							// use that to set the lore's percentage.  If it fails at parsing then use 100%.

							try {
								results = Double.parseDouble( value );
							}
							catch (NumberFormatException e) {

								// Error: Default to 100%
								// Do not generate log messages since there will be 1000's...
								results = 100.0;
							}

							// Clean up the parsed number.  Less than zero is zero (disabled).
							if ( results < 0.0 ) {
								results = 0.0;
							}

							// Cannot exceed 100%
							if ( results > 100.0 ) {
								results = 100.0;
							}
						}
					}
				}
			}
		}

		return results;
	}

	protected double getLoreValue( SpigotItemStack itemInHand, String loreValue ) {
		double results = 0.0;

		if ( itemInHand != null && !itemInHand.isAir() && loreValue != null && !loreValue.trim().isEmpty() ) {
			List<String> lores = itemInHand.getLore();
			
			// Clean the loreValue we need to compare everything to.  It must have all color codes removed:
			String loreValueCleaned = Text.stripColor( loreValue );
			
			for ( String lore : lores ) {
				
				// Remove the color codes so it can be cleanly compared with the loreValue:
				String loreCleaned = Text.stripColor( lore );

				if (loreCleaned.startsWith( loreValueCleaned )) {
					
					// Lore detected so set default to 100%:
					results = 100.0;
					
					String value = loreCleaned.replace( loreValueCleaned, "" ).trim();
					
					if (value.length() > 0) {
						
						// Content has been found after the lore's name. If it is a number, then
						// use that to set the lore's percentage.  If it fails at parsing then use 100%.
						
						try {
							results = Double.parseDouble( value );
						}
						catch (NumberFormatException e) {
							
							// Error: Default to 100%
							// Do not generate log messages since there will be 1000's...
							results = 100.0;
						}
						
						// Clean up the parsed number.  Less than zero is zero (disabled).
						if ( results < 0.0 ) {
							results = 0.0;
						}
						
						// Cannot exceed 100%
						if ( results > 100.0 ) {
							results = 100.0;
						}
					}
				}
				
			}
		}

		return results;
	}

	
	/**
	 * <p>If using autoPickupBlockNameList then must use XMaterial's name.
	 * If block type is CustomItems, then it must be prefixed with CustomIems:BlockName. Minecraft
	 * blocks can be prefixed with minecraft:BlockName but they don't have to be.
	 * </p>
	 * 
	 * @param block
	 * @param p
	 * @param itemInHand
	 * @param debugInfo 
	 * @return
	 */
	protected int autoFeaturePickup( PrisonMinesBlockBreakEvent pmEvent,
			// SpigotBlock block, Player p, SpigotItemStack itemInHand,
							boolean isAutoSmelt, boolean isAutoBlock, StringBuilder debugInfo ) {

		int count = 0;

		boolean isAll = isBoolean( AutoFeatures.pickupAllBlocks );
		PrisonBlock prisonBlock = pmEvent.getSpigotBlock().getPrisonBlock();
		
		// Use this is a block name list based upon the following:  blockType:blockName if not minecraft, or blockName
		List<String> pickupBlockNameList =
				isBoolean( AutoFeatures.pickupBlockNameListEnabled ) ? 
						getListString( AutoFeatures.pickupBlockNameList ) : null;

		if ( isAll ) {
			count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );

		}
		
		else if ( isBoolean( AutoFeatures.pickupBlockNameListEnabled ) && pickupBlockNameList.size() > 0 && 
							pickupBlockNameList.contains( prisonBlock.getBlockName() ) ) {
			count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
		}
			
		else {

			switch ( prisonBlock.getBlockName() ) {

				case "cobblestone":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "stone":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "gold_ore":
				case "nether_gold_ore":
				case "deepslate_gold_ore":
				case "raw_gold":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "iron_ore":
				case "deepslate_iron_ore":
				case "raw_iron":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "coal_ore":
				case "deepslate_coal_ore":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "diamond_ore":
				case "deepslate_diamond_ore":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "redstone_ore":
				case "deepslate_redstone_ore":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "emerald_ore":
				case "deepslate_emerald_ore":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "quartz_ore":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "lapis_ore":
				case "deepslate_lapis_ore":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "snow_ball":
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "glowstone_dust": // works 1.15.2
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;

				case "copper_ore": 
				case "deepslate_copper_ore": 
				case "raw_copper": 
					count += autoPickup( pmEvent, isAutoSmelt, isAutoBlock, debugInfo );
					break;
					
				default:
					break;
			}
		}

		// Moved out of this function since it shouldn't have been placed here.
		// Calculate XP on block break if enabled:
//		calculateXP( p, block, count );

//				Output.get().logInfo( "In mine: %s  blockName= [%s] %s  drops= %s  count= %s  dropNumber= %s ",
//						mine.getName(), blockName, Integer.toString( dropNumber ),
//						(e.getBlock().getDrops(itemInHand) != null ? e.getBlock().getDrops(itemInHand).size() : "-=null=-"),
//						Integer.toString( count ), Integer.toString( dropNumber )
//						);

		return count;
	}

//	
//	protected XMaterial autoFeatureSmelt( Player p, XMaterial source )
//	{
//		XMaterial results = source;
//		
//		boolean isAll = isBoolean( AutoFeatures.smeltAllBlocks );
//		
////		XMaterial source = SpigotUtil.getXMaterial( block.getPrisonBlock() );
//		if ( source != null ) {
//			
//			switch ( source )
//			{
//				case COBBLESTONE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltCobblestone ), source, XMaterial.STONE, p );
//					results = XMaterial.STONE;
//					break;
//					
//				case GOLD_ORE:
//				case NETHER_GOLD_ORE:
//				case DEEPSLATE_GOLD_ORE:
//				case RAW_GOLD:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltGoldOre ), source, XMaterial.GOLD_INGOT, p );
//					results = XMaterial.GOLD_INGOT;
//					break;
//					
//				case IRON_ORE:
//				case DEEPSLATE_IRON_ORE:
//				case RAW_IRON:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltIronOre ), source, XMaterial.IRON_INGOT, p );
//					results = XMaterial.IRON_INGOT;
//					break;
//					
//				case COAL_ORE:
//				case DEEPSLATE_COAL_ORE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltCoalOre ), source, XMaterial.COAL, p );
//					results = XMaterial.COAL;
//					break;
//					
//				case DIAMOND_ORE:
//				case DEEPSLATE_DIAMOND_ORE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltDiamondlOre ), source, XMaterial.DIAMOND, p );
//					results = XMaterial.DIAMOND;
//					break;
//					
//				case EMERALD_ORE:
//				case DEEPSLATE_EMERALD_ORE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltEmeraldOre ), source, XMaterial.EMERALD, p );
//					results = XMaterial.EMERALD;
//					break;
//					
//				case LAPIS_ORE:
//				case DEEPSLATE_LAPIS_ORE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltLapisOre ), source, XMaterial.LAPIS_LAZULI, p );
//					results = XMaterial.LAPIS_LAZULI;
//					break;
//					
//				case REDSTONE_ORE:
//				case DEEPSLATE_REDSTONE_ORE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltRedstoneOre ), source, XMaterial.REDSTONE, p );
//					results = XMaterial.REDSTONE;
//					break;
//					
//				case NETHER_QUARTZ_ORE:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltNetherQuartzOre ), source, XMaterial.QUARTZ, p );
//					results = XMaterial.QUARTZ;
//					break;
//					
//				case ANCIENT_DEBRIS:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltAncientDebris ), source, XMaterial.NETHERITE_SCRAP, p );
//					results = XMaterial.NETHERITE_SCRAP;
//					break;
//
//				// v1.17 !!
//				case COPPER_ORE:
//				case DEEPSLATE_COPPER_ORE:
//				case RAW_COPPER:
//					autoSmelt( isAll || isBoolean( AutoFeatures.smeltCopperOre ), source, XMaterial.COPPER_INGOT, p );
//					results = XMaterial.COPPER_INGOT;
//					break;
//					
//				default:
//					break;
//			}
//		}
//		
//		
//		return results;
//	}
//
//	protected void autoFeatureBlock( Player p, XMaterial source  ) {
//
//		boolean isAll = isBoolean( AutoFeatures.smeltAllBlocks );
//
//		if ( source != null ) {
//			
//			// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
//			// checks within each block type's function call.  So in one pass, could hit on more
//			// than one of these for multiple times too.
//			switch ( source )
//			{
//				case GOLD_INGOT:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockGoldBlock ), source, XMaterial.GOLD_BLOCK, p );
//					
//					break;
//					
//				case IRON_INGOT:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockIronBlock ), source, XMaterial.IRON_BLOCK, p );
//					
//					break;
//
//				case COAL:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockCoalBlock ), source, XMaterial.COAL_BLOCK, p );
//					
//					break;
//					
//				case DIAMOND:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockDiamondBlock ), source, XMaterial.DIAMOND_BLOCK, p );
//					
//					break;
//					
//				case REDSTONE:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockRedstoneBlock ), source,XMaterial.REDSTONE_BLOCK, p );
//					
//					break;
//					
//				case EMERALD:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockEmeraldBlock ), source, XMaterial.EMERALD_BLOCK, p );
//					
//					break;
//					
//				case QUARTZ:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockQuartzBlock ), source, XMaterial.QUARTZ_BLOCK, 4, p );
//					
//					break;
//					
//				case PRISMARINE_SHARD:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockPrismarineBlock ), source, XMaterial.PRISMARINE, 4, p );
//					
//					break;
//					
//				case SNOWBALL:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockSnowBlock ), source, XMaterial.SNOW_BLOCK, 4, p );
//					
//					break;
//					
//				case GLOWSTONE_DUST:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockGlowstone ), source, XMaterial.GLOWSTONE, 4, p );
//					
//					break;
//					
//				case LAPIS_LAZULI:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockLapisBlock ), source, XMaterial.LAPIS_BLOCK, p );
//					
//					break;
//					
//				case COPPER_INGOT:
//					autoBlock( isAll || isBoolean( AutoFeatures.blockCopperBlock ), source, XMaterial.COPPER_BLOCK, p );
//					
//					break;
//					
//				default:
//					break;
//			}
//		}
//
//	}
//
	
	/**
	 * <p>This processes the normal drop smelting if it's enabled.  Only the 
	 * List of SpigotItemStacks are needed.
	 * </p>
	 * 
	 * @param drops
	 */
	protected void normalDropSmelt( List<SpigotItemStack> drops ) {
		
		boolean isAll = isBoolean( AutoFeatures.smeltAllBlocks );
		
		Set<XMaterial> xMats = new HashSet<>();
		for ( SpigotItemStack sItemStack : drops ) {
			
			if ( sItemStack.getMaterial().getBlockType() == PrisonBlockType.CustomItems ) {
				// cannot smelt custom blocks so skip XMaterial:
				continue;
			}
			
			XMaterial xMat = null;
			
			if ( sItemStack.getBukkitStack() != null ) {
				
				xMat = XMaterial.matchXMaterial( sItemStack.getBukkitStack() );
			}
			else if ( sItemStack.getMaterial() != null ) {
				
				xMat = SpigotCompatibility.getInstance().getXMaterial( sItemStack.getMaterial() );
			}
			
			if ( xMat != null && !xMats.contains( xMat ) ) {
				xMats.add( xMat );
			}
			
		}
		
		
		for ( XMaterial source : xMats ) {
			
			
			switch ( source )
			{
				case COBBLESTONE:
					if ( isAll || isBoolean( AutoFeatures.smeltCobblestone ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.STONE, 1 );
					}
					break;
					
				case GOLD_ORE:
				case NETHER_GOLD_ORE:
				case DEEPSLATE_GOLD_ORE:
				case RAW_GOLD:
					
					if ( isAll || isBoolean( AutoFeatures.smeltGoldOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.GOLD_INGOT, 1 );
					}
					break;
					
				case IRON_ORE:
				case DEEPSLATE_IRON_ORE:
				case RAW_IRON:
					if ( isAll || isBoolean( AutoFeatures.smeltIronOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.IRON_INGOT, 1 );
					}
					break;
					
				case COAL_ORE:
				case DEEPSLATE_COAL_ORE:
					if ( isAll || isBoolean( AutoFeatures.smeltCoalOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.COAL, 1 );
					}
					break;
					
				case DIAMOND_ORE:
				case DEEPSLATE_DIAMOND_ORE:
					if ( isAll || isBoolean( AutoFeatures.smeltDiamondlOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.DIAMOND, 1 );
					}
					break;
					
				case EMERALD_ORE:
				case DEEPSLATE_EMERALD_ORE:
					if ( isAll || isBoolean( AutoFeatures.smeltEmeraldOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.EMERALD, 1 );
					}
					break;
					
				case LAPIS_ORE:
				case DEEPSLATE_LAPIS_ORE:
					if ( isAll || isBoolean( AutoFeatures.smeltLapisOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.LAPIS_LAZULI, 1 );
					}
					break;
					
				case REDSTONE_ORE:
				case DEEPSLATE_REDSTONE_ORE:
					if ( isAll || isBoolean( AutoFeatures.smeltRedstoneOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.REDSTONE, 1 );
					}
					break;
					
				case NETHER_QUARTZ_ORE:
					if ( isAll || isBoolean( AutoFeatures.smeltNetherQuartzOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.QUARTZ, 1 );
					}
					break;
					
				case ANCIENT_DEBRIS:
					if ( isAll || isBoolean( AutoFeatures.smeltAncientDebris ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.NETHERITE_SCRAP, 1 );
					}
					break;

				// v1.17 !!
				case COPPER_ORE:
				case DEEPSLATE_COPPER_ORE:
				case RAW_COPPER:
					if ( isAll || isBoolean( AutoFeatures.smeltCopperOre ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.COPPER_INGOT, 1);
					}
					break;
					
				default:
					break;
			}
		}
		
	}


	/**
	 * <p>This processed the normal drops for blocking.  Only the List of 
	 * SpigotItemStacks are needed since everything else is self contained.
	 * </p>
	 * 
	 * @param drops
	 */
	protected void normalDropBlock( List<SpigotItemStack> drops ) {
		
		boolean isAll = isBoolean( AutoFeatures.smeltAllBlocks );
		
		Set<XMaterial> xMats = new HashSet<>();
		for ( SpigotItemStack sItemStack : drops ) {
			
			if ( sItemStack.getMaterial().getBlockType() == PrisonBlockType.CustomItems ) {
				// cannot block custom blocks so skip XMaterial:
				continue;
			}
			
			if ( sItemStack.getBukkitStack() != null ) {
				
				XMaterial source = XMaterial.matchXMaterial( sItemStack.getBukkitStack() );
				
				if ( !xMats.contains( source  ) ) {
					xMats.add( source );
				}
			}
		}
		
		
		for ( XMaterial source : xMats ) {
			
			switch ( source )
			{
				case GOLD_INGOT:
					if ( isAll || isBoolean( AutoFeatures.blockGoldBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.GOLD_BLOCK, 9 );
					}
					break;
					
				case IRON_INGOT:
					if ( isAll || isBoolean( AutoFeatures.blockIronBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.IRON_BLOCK, 9 );
					}
					break;

				case COAL:
					if ( isAll || isBoolean( AutoFeatures.blockCoalBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.COAL_BLOCK, 9 );
					}
					break;
					
				case DIAMOND:
					if ( isAll || isBoolean( AutoFeatures.blockDiamondBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.DIAMOND_BLOCK, 9 );
					}
					break;
					
				case REDSTONE:
					if ( isAll || isBoolean( AutoFeatures.blockRedstoneBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source,XMaterial.REDSTONE_BLOCK, 9 );
					}
					break;
					
				case EMERALD:
					if ( isAll || isBoolean( AutoFeatures.blockEmeraldBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.EMERALD_BLOCK, 9 );
					}
					break;
					
				case QUARTZ:
					if ( isAll || isBoolean( AutoFeatures.blockQuartzBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.QUARTZ_BLOCK, 4 );
					}
					break;
					
				case PRISMARINE_SHARD:
					if ( isAll || isBoolean( AutoFeatures.blockPrismarineBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.PRISMARINE, 4 );
					}
					break;
					
				case SNOWBALL:
					if ( isAll || isBoolean( AutoFeatures.blockSnowBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.SNOW_BLOCK, 4 );
					}
					break;
					
				case GLOWSTONE_DUST:
					if ( isAll || isBoolean( AutoFeatures.blockGlowstone ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.GLOWSTONE, 4 );
					}
					break;
					
				case LAPIS_LAZULI:
					if ( isAll || isBoolean( AutoFeatures.blockLapisBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.LAPIS_BLOCK, 9 );
					}
					break;
					
				case COPPER_INGOT:
					if ( isAll || isBoolean( AutoFeatures.blockCopperBlock ) ) {
						
						SpigotUtil.itemStackReplaceItems( drops, source, XMaterial.COPPER_BLOCK, 9 );
					}
					break;
					
				default:
					break;
			}
		}
		
	}



	protected void incrementCounterInName( ItemStack itemInHand, ItemMeta meta ) {
		String name = meta.getDisplayName();
		if (!meta.hasDisplayName() || name == null || name.trim().length() == 0) {
			name = itemInHand.getType().name().toLowerCase().replace("_", " ").trim();
			name += " [1]";
		} else {

			int j = name.lastIndexOf(']');
			if (j == -1) {
				name += " [1]";
			} else {
				if (j != -1) {
					int i = name.lastIndexOf('[', j);

					if (i != -1) {
						String numStr = name.substring(i + 1, j);

//									Output.get().logInfo( String.format( "AutoManager: name:  %s : %s ",
//											name, numStr) );

						try {
							int blocksMined = Integer.parseInt( numStr );
							name = name.substring( 0, i ).trim() + " [" + ++blocksMined + "]";
						} catch (NumberFormatException e1) {
							Output.get().logError("AutoManager: tool counter failure. tool name= [" + name + "] error: " + e1.getMessage());
						}
					}
				}
			}
		}
		if (name != null) {
			meta.setDisplayName( name );
			itemInHand.setItemMeta( meta );
		}
	}

	/**
	 * <p>NOTE that the calculations of calculateBukkitExtendedFortuneBlockCount()
	 * will override the fortune calculations below.
	 * </p>
	 * 
	 * 
	 * <p>This function is based upon the following wiki page.
	 * </p>
	 * 
	 * https://minecraft.gamepedia.com/Fortune
	 *
	 * <p>"<b>Ore:</b> For coal ore, diamond ore, emerald ore, lapis lazuli ore,
	 * nether gold ore,‌[upcoming: JE 1.16] and nether quartz ore,
	 * Fortune I gives a 33% chance to multiply drops by 2 (averaging 33% increase),
	 * Fortune II gives a chance to multiply drops by 2 or 3 (25% chance each,
	 * averaging 75% increase), and Fortune III gives a chance to multiply drops
	 * by 2, 3, or 4 (20% chance each, averaging 120% increase).
	 * </p>
	 * <p>"Generally speaking, Fortune gives a weight of 2 to a normal drop chance
	 * and adds a weight of 1 for each extra drop multiplier. The drop multipliers
	 * are the integers between 2 and Fortune Level + 1, inclusive.
	 * </p>
	 * <p>"The formula to calculate the average drops multiplier is
	 * 1/(Fortune Level+2) + (Fortune Level+1)/2, which means
	 * Fortune IV gives 2.67x drops on average, Fortune V gives
	 * 3.14x drops on average, etc. "
	 * </p>
	 *
	 * <p><b>Discrete random</b> Glowstone, melons, nether wart, redstone ore,
	 * sea lanterns, and sweet berries use a discrete uniform distribution,
	 * meaning each possible drop amount is equally likely to be dropped.
	 * Fortune increases the maximum number of drops by 1 per level. However,
	 * maximum drop limitations may apply: glowstone has a cap of 4 glowstone
	 * dust, sea lanterns have a cap of 5 prismarine crystals, and melons have
	 * a cap of 9 melon slices. If a drop higher than these maximums is rolled,
	 * it is rounded down to the cap.
	 * </p>
	 *
	 *
	 * @param blocks
	 * @param fortuneLevel
	 */
	protected void calculateFortune(SpigotItemStack blocks, int fortuneLevel) {

		
		if (fortuneLevel > 0) {
			
			int count = blocks.getAmount();
			
			if ( isBoolean( AutoFeatures.isExtendBukkitFortuneCalculationsEnabled ) ) {
				
				// If the bukkit fortune was applied (amount > 1) then extend it to the full
				// range of the fortune enchantment.  Otherwise this value will be zero.
				// This value represents the final number of block amount.
				int bukkitExtendedFortuneBlockCount = 
						calculateBukkitExtendedFortuneBlockCount( blocks, fortuneLevel );
				
				if ( bukkitExtendedFortuneBlockCount > 0 ) {
					count = bukkitExtendedFortuneBlockCount;
					
					// The count has the final value so set it as the amount:
					blocks.setAmount( count );
				}
				return;
			}
			
			if ( isBoolean( AutoFeatures.isCalculateAltFortuneEnabled ) ) {

				int multiplier = 1;
				
				// Due to variations with gold and wood PickAxe need to use a dynamic
				// Material name selection which will fit for the version of MC that is
				// being ran.

				XMaterial xMat = XMaterial.matchXMaterial( blocks.getBukkitStack() );
				
				// These need to be processed first due to special drop amounts for these items, and 
				// in the next group there is the isCalculateAltFortuneOnAllBlocksEnabled setting that
				// would override these values if it were to be processed first.
				if ( xMat == XMaterial.GLOWSTONE ||
						xMat == XMaterial.GLOWSTONE_DUST ||
						xMat == XMaterial.REDSTONE ||
						xMat == XMaterial.SEA_LANTERN ||
						xMat == XMaterial.REDSTONE_ORE ||
						xMat == XMaterial.PRISMARINE ||
						
						xMat == XMaterial.BEETROOT_SEEDS ||
						xMat == XMaterial.CARROT ||
						xMat == XMaterial.MELON ||
						xMat == XMaterial.MELON_SEEDS ||
						xMat == XMaterial.NETHER_WART ||
						xMat == XMaterial.POTATO ||
						xMat == XMaterial.GRASS ||
						xMat == XMaterial.WHEAT ) {
					multiplier = getRandom().nextInt( fortuneLevel );
					
					// limits slightly greater than standard:
					if ( xMat == XMaterial.GLOWSTONE) {
						// standard: 4
						if (multiplier > 5) {
							multiplier = 5;
						}
					} else if ( xMat == XMaterial.SEA_LANTERN) {
						// standard: 5
						if (multiplier > 6) {
							multiplier = 6;
						}
					} else if ( xMat == XMaterial.MELON) {
						// standard: 9
						if (multiplier > 11) {
							multiplier = 11;
						}
					}
					
					
					// If the adjustedfortuneMultipler is greater than the permitted max value then use the max value.
					// A zero value for fortuneMultiplierMax indicates no max should be used.
					int fortuneMultiplierMax = getInteger( AutoFeatures.fortuneMultiplierMax );
					if ( fortuneMultiplierMax != 0d && multiplier > fortuneMultiplierMax ) {
						multiplier = fortuneMultiplierMax;
					}
					
					// add the multiplier to the count:
					count += multiplier;

				}
				
				else if ( 
						isBoolean( AutoFeatures.isCalculateAltFortuneOnAllBlocksEnabled ) ||
						
						
						xMat == XMaterial.GOLD_ORE ||
						xMat == XMaterial.NETHER_GOLD_ORE ||
						xMat == XMaterial.DEEPSLATE_GOLD_ORE ||
						xMat == XMaterial.RAW_GOLD ||
						xMat == XMaterial.GOLD_BLOCK ||

						xMat == XMaterial.IRON_ORE ||
						xMat == XMaterial.DEEPSLATE_IRON_ORE ||
						xMat == XMaterial.RAW_IRON ||
						xMat == XMaterial.IRON_BLOCK ||

						xMat == XMaterial.COAL_ORE ||
						xMat == XMaterial.DEEPSLATE_COAL_ORE ||
						xMat == XMaterial.COAL ||
						xMat == XMaterial.COAL_BLOCK ||
						
						xMat == XMaterial.DIAMOND_ORE ||
						xMat == XMaterial.DEEPSLATE_DIAMOND_ORE ||
						xMat == XMaterial.DIAMOND ||
						xMat == XMaterial.DIAMOND_BLOCK ||
						
						xMat == XMaterial.EMERALD_ORE ||
						xMat == XMaterial.DEEPSLATE_EMERALD_ORE ||
						xMat == XMaterial.EMERALD ||
						xMat == XMaterial.EMERALD_BLOCK ||

						xMat == XMaterial.LAPIS_ORE ||
						xMat == XMaterial.DEEPSLATE_LAPIS_ORE ||
						xMat == XMaterial.LAPIS_BLOCK ||
						
						xMat == XMaterial.NETHER_QUARTZ_ORE ||
						xMat == XMaterial.QUARTZ_BLOCK ||
						
						xMat == XMaterial.REDSTONE_ORE ||
						xMat == XMaterial.REDSTONE_BLOCK ||
						xMat == XMaterial.DEEPSLATE_REDSTONE_ORE ||
						
						xMat == XMaterial.COPPER_ORE ||
						xMat == XMaterial.DEEPSLATE_COPPER_ORE ||
						xMat == XMaterial.RAW_COPPER ||
						

						xMat == XMaterial.NETHER_WART_BLOCK ||
						xMat == XMaterial.NETHERITE_BLOCK ||
						xMat == XMaterial.PURPUR_BLOCK ||
						xMat == XMaterial.SLIME_BLOCK ||
						xMat == XMaterial.SNOW_BLOCK
						) {
					
					multiplier = calculateFortuneMultiplier( fortuneLevel, multiplier );
					
					// multiply the multiplier:
					count *= multiplier;
					
				}
				
			}

			// The count has the final value so set it as the amount:
			blocks.setAmount( count );
		}

	}


	/**
	 * <p>This function detects if bukkit applied a fortune to the drop that it supplied by
	 * checking to see if the block amount is grater than 1.
	 * If it is, and the fortune level of the tool is greater than the vanilla max fortune 
	 * level of 3, then it calculates a fortune multiplier based upon the how far above the
	 * max level of 3.
	 * </p>
	 * 
	 * <p>To help prevent excessive number of results, the calculated fortune multiplier is 
	 * then randomly adjusted between 0.70 (reduction) to 1.10 (increase) in the fortune
	 * multiplier.
	 * </p>
	 * 
	 * <p>A returned value of zero indicates no fortune was able to be calculated and 
	 * this should be ignored.
	 * </p>
	 * 
	 * @param blocks
	 * @param fortuneLevel
	 * @return
	 */
	private int calculateBukkitExtendedFortuneBlockCount( SpigotItemStack blocks, int fortuneLevel )
	{
		int bukkitExtendedFortuneBlockCount = 0;

		
		if ( isBoolean( AutoFeatures.isExtendBukkitFortuneCalculationsEnabled ) && 
				fortuneLevel > 0 && blocks.getAmount() > 1 ) {
			
			// Note: fortune has already been applied by bukkit, but it may have been
			//       capped at fortune 3.
			
			if ( fortuneLevel > 3 ) {
				
				// First calculate the fortune multiplier, which is simply the fortunelevel
				// divided by the max normal fortune level which is always three.
				double fortuneMultiplier = fortuneLevel / 3;
				
				// Next calculate the random factor.  It will be applied to the multiplier to 
				// adjust the fortune results slightly.  The default range will be 0.7 to 1.1 so the 
				// the average result would be to reduce the generated quantity.
				double randomFactorRangeLow = getInteger( AutoFeatures.extendBukkitFortuneFactorPercentRangeLow ) / 100d;
				double randomFactorRangeHigh = getInteger( AutoFeatures.extendBukkitFortuneFactorPercentRangeHigh ) / 100d;
				
				double randomFactor = ( (randomFactorRangeHigh - randomFactorRangeLow) * getRandom().nextDouble() ) + 
								randomFactorRangeLow;
				
				
				// The adjusted fortune multiplier is to be applied to the number of blocks
				// and represents a close approximation to what bukkit's fortune may produce
				// if it were to be extended to enchantment levels greater than 3 for fortune.
				double adjustedFortuneMultiplier = Math.round(fortuneMultiplier * randomFactor);
				double adjFortuneMultiplierCapped = adjustedFortuneMultiplier;
				
				
				
				
				// If the adjustedfortuneMultipler is greater than the permitted max value then use the max value.
				// A zero value for fortuneMultiplierMax indicates no max should be used.
				int fortuneMultiplierMax = getInteger( AutoFeatures.fortuneMultiplierMax );
				if ( fortuneMultiplierMax != 0d && adjustedFortuneMultiplier > fortuneMultiplierMax ) {
					adjFortuneMultiplierCapped = fortuneMultiplierMax;
				}
				
				
				bukkitExtendedFortuneBlockCount = (int) (blocks.getAmount() * adjFortuneMultiplierCapped);
				
					
				if ( Output.get().isDebug( DebugTarget.blockBreakFortune ) ) {
					
					String message = "### calculateBukkitExtendedFortuneBlockCount ### " +
							"fortuneLevel: %d  defaultBlocks: %d  fortMult: %f  " +
							"rndRngLow: %f  rndRngHigh: %f  rndFactor: %f  adjFortMult: %f  " +
							"maxMultiplier: %f   extendedFortuneBlockCount= %d";
					message = String.format(  message, 
							fortuneLevel, blocks.getAmount(),
							fortuneMultiplier, randomFactorRangeLow, randomFactorRangeHigh, 
							randomFactor, adjustedFortuneMultiplier, 
							adjFortuneMultiplierCapped, bukkitExtendedFortuneBlockCount );
					
					Output.get().logDebug( DebugTarget.blockBreakFortune, message );
				}
			}
			
		}
		
		return bukkitExtendedFortuneBlockCount;
	}


	/**
	 * <p>Fortune is calculated using the standard calculations used by vanilla
	 * minecraft when the tool's fortuneLevel is three or lower.
	 * </p>
	 * 
	 * <p>This function also supports non-standard higher fortune levels. Fortune 
	 * levels of 4 and 5 are fixed at lower levels.  Levels 6 and higher uses a
	 * formula with an initial threshold of 80%, but the threshold increases 
	 * as the fortuneLevel increases.  There is no upper limit on the calculations, 
	 * but anything greater than a fortune level of 200 has a 100% chance of 
	 * the calculated multiplier being applied.
	 * </p>
	 * 
	 * <p>Fortune Levels and the resulting multipliers are based upon random chance
	 * between 0 and 100:
	 * 
	 * </p>
	 * <ul>
	 *   <li>Fortune 1: <ul>
	 *   	<li><b>2</b> :: rnd <= 33</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 2: <ul>
	 *   	<li><b>2</b> :: rnd <= 25</li>
	 *   	<li><b>3</b> :: rnd <= 50</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 3: <ul>
	 *   	<li><b>2</b> :: rnd <= 20</li>
	 *   	<li><b>3</b> :: rnd <= 40</li>
	 *   	<li><b>4</b> :: rnd <= 60</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 4: <ul>
	 *   	<li><b>2</b> :: rnd <= 16</li>
	 *   	<li><b>3</b> :: rnd <= 32</li>
	 *   	<li><b>4</b> :: rnd <= 48</li>
	 *   	<li><b>5</b> :: rnd <= 64</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 5: <ul>
	 *   	<li><b>2</b> :: rnd <= 14</li>
	 *   	<li><b>3</b> :: rnd <= 28</li>
	 *   	<li><b>4</b> :: rnd <= 42</li>
	 *   	<li><b>5</b> :: rnd <= 56</li>
	 *   	<li><b>6</b> :: rnd <= 70</li>
	 *   	</ul></li>
	 *   </li>
	 *
	 *   <li>Fortune 6 & Higher: <ul>
	 *     <li>Threshold is set to 80%</li>
	 *     <li>For every fortuneLevel of 10, threshold is increased by 1%</li>
	 *     <li>Threshold can only reach a max value of 100</li>
	 *     <li>If rnd is greater than threshold, then multiplier is only 1</li>
	 *     <li>chancePerUnit = threshold / fortuneLevel</li>
	 *     <li>multiplier = the floor value of chancePerUnit * rnd</li>
	 *     <li>If modified threshold hit 100, then chancePerUnit is always calculated.</li>
	 *     <li>No upper limit on fortuneLevel.</li>
	 *     </ul></li>
	 *   </li>
	 * </ul
	 * 
	 * @param fortuneLevel
	 * @param multiplier
	 * @return
	 */
	private int calculateFortuneMultiplier(int fortuneLevel, int multiplier) {
		int rnd = getRandom().nextInt( 100 );

		switch (fortuneLevel) {
			case 0:
				break;
			case 1:
				if (rnd <= 33) {
					multiplier = 2;
				}
				break;

			case 2:
				if (rnd <= 25) {
					multiplier = 2;
				}
				else if (rnd <= 50) {
					multiplier = 3;
				}
				break;

			case 3:
				if (rnd <= 20) {
					multiplier = 2;
				}
				else if (rnd <= 40) {
					multiplier = 3;
				}
				else if (rnd <= 60) {
					multiplier = 4;
				}
				break;


			case 4:
				if (rnd <= 16) {
					multiplier = 2;
				}
				else if (rnd <= 32) {
					multiplier = 3;
				}
				else if (rnd <= 48) {
					multiplier = 4;
				}
				else if (rnd <= 64) {
					multiplier = 5;
				}
				break;

			case 5:
				// values of 5 or higher
				if (rnd <= 14) {
					multiplier = 2;
				}
				else if (rnd <= 28) {
					multiplier = 3;
				}
				else if (rnd <= 42) {
					multiplier = 4;
				}
				else if (rnd <= 56) {
					multiplier = 5;
				}
				else if (rnd <= 70) {
					// Only 8% not 16% chance
					multiplier = 6;
				}
				break;
				
			default:
				
				// Fortune is over 5, so apply a special formula:
				
				// Take the fortune level and divide by 10 and then take the 
				// floor value of that and use it as the threshold modifier.
				
				int thresholdModifier = Math.floorDiv( fortuneLevel, 10 );
				
				// Max thresholdModifier can only be 100.
				if ( thresholdModifier > 100 ) {
					thresholdModifier = 100;
				}
				
				// Calculate the threshold to apply the multiplier, starting 
				// with 80%, then add the thresholdModifier so the higher the
				// fortune, then the greater the odds of engaging the the 
				// multiplier, where a fortune of 200 will guarantee the 
				// the multiplier will always be enabled.
				
				double threshold = 80.0d + thresholdModifier;
				
				// Use a random number that is a double:
				double rndD = getRandom().nextDouble() * 100d;
				
				if ( rndD <= threshold ) {
					// Passed the threshold, so calculate the multiplier.
					
					// The chancesPerUnit represents how to subdivide the 
					// threshold in to number of fortuneLevels.
					double chancesPerUnit = threshold / fortuneLevel;
					
					// Calculate how many units are in the rndD number:
					double units = rndD / chancesPerUnit;
					
					// The multiplier is the floor of units. Do not round up.
					multiplier = 1 + (int) Math.floor( units );
					
				}
				
		}
		
		
		// If the adjustedfortuneMultipler is greater than the permitted max value then use the max value.
		// A zero value for fortuneMultiplierMax indicates no max should be used.
		int fortuneMultiplierMax = getInteger( AutoFeatures.fortuneMultiplierMax );
		if ( fortuneMultiplierMax != 0d && multiplier > fortuneMultiplierMax ) {
			multiplier = fortuneMultiplierMax;
		}
		
		
		return multiplier;
	}

	/**
	 * <p>This function has yet to be implemented, but it should implement behavior if
	 * silk touch is enabled for the tool.
	 * </p>
	 *
	 * @param itemInHand
	 * @param drops
	 */
	@SuppressWarnings( "unused" )
	private void calculateSilkTouch(SpigotItemStack itemInHand, List<SpigotItemStack> drops) {
		
		if ( isBoolean( AutoFeatures.isCalculateSilkEnabled ) && hasSilkTouch( itemInHand )) {
			
			for (SpigotItemStack itemStack : drops) {
				
				// If stack is gravel, then there is a 10% chance of dropping flint.
				
			}
		}
	}

	/**
	 * <p>Because of the use of getDrops() function, not all of the correct drops are actually
	 * dropped.  This function tries to restore some of those special drops.
	 * </p>
	 *
	 * <p>Example of what this does, is to provide a random drop of flint when mining
	 * coal ore.
	 * </p>
	 *
	 * <p>"When a block of gravel is mined, there is a 10% chance for a single piece of flint
	 * to drop instead of the gravel block. When mined with a Fortune-enchanted tool, this chance
	 * increases to 14% at Fortune I, 25% at Fortune II, and 100% at Fortune III. Gravel mined
	 * using a tool with Silk Touch or gravel that fell on a non-solid block never produces flint."
	 * <a href="https://minecraft.gamepedia.com/Flint">wiki</a>
	 * </p>
	 *
	 * <p>For example gravel having random flint drops.
	 * </p>
	 *
	 * @param itemInHand
	 * @param drops
	 */
	private void calculateDropAdditions(SpigotItemStack itemInHand, List<SpigotItemStack> drops) {
		
		if ( isBoolean( AutoFeatures.isCalculateDropAdditionsEnabled ) ) {
			
			List<SpigotItemStack> adds = new ArrayList<SpigotItemStack>();
			
			for (SpigotItemStack itemStack : drops) {
				
				// If gravel and has the 10% chance whereas rnd is zero, which is 1 out of 10.
				// But if has silk touch, then never drop flint.
				adds.addAll( 
						calculateDropAdditionsGravelFlint( itemInHand, itemStack, drops ) );
			}
			
			if ( adds.size() > 0 ) {
				drops.addAll( adds );
			}
		}
	}


	/**
	 * <p>For gravel flint drops, this function adds special processing to increase the quantity
	 * of flint drops from the standard 1, to be influence by fortune enchants.  If fortune
	 * is >= 3, then add one to the quantity drop, plus a random chance to add floor(fortune / 5).
	 * So if fortune is 3, then the drop will always be 2.  If fortune is 5, then drop will
	 * be 2, plus a random chance of one additional drop.  If fortune is 20, then drop will be
	 * 2, plus an equal random chance to add 0 to 4 additional flints to the quantity of
	 * the flint drop. The other thing that is different from vanilla, is that if the player
	 * will get a flint drop, they will still get the normal gravel drop.
	 * </p>
	 *
	 * @param itemInHand
	 * @param itemStack
	 * @param drops
	 */
	private List<SpigotItemStack> calculateDropAdditionsGravelFlint(SpigotItemStack itemInHand, 
											SpigotItemStack itemStack,
												   List<SpigotItemStack> drops ) {
		List<SpigotItemStack> adds = new ArrayList<SpigotItemStack>();
		
		PrisonBlock gravel = SpigotUtil.getPrisonBlock( XMaterial.GRAVEL );
		
		if (itemStack.getMaterial().compareTo( gravel ) == 0 && !hasSilkTouch(itemInHand)) {

			int quantity = 1;
			int threshold = 10;

			// If fortune is enabled on the tool, then increase drop odds by:
			//  1 = 14%, 2 = 25%, 3+ = 100%
			int fortune = getFortune(itemInHand);
			switch (fortune) {
				case 0:
					// No additional threshold when fortune is zero:
					break;
					
				case 1:
					threshold = 14;
					break;
					
				case 2:
					threshold = 25;
					break;
					
				case 3:
				default:
					// if Fortune 3 or higher, default to 100% drop:
					threshold = 100;
					break;
			}

			// If zero, then 10% chance of 1 out of 10.
			if (getRandom().nextInt(100) <= threshold) {

				// If fortune is >= 3, then add one to the quantity drop, plus a
				// random chance to add floor(fortune / 5).
				if (fortune >= 3) {
					quantity += 1 + getRandom().nextInt(Math.floorDiv( fortune, 5));
				}

//				ItemStack flintStack = new ItemStack(Material.FLINT, quantity);
				PrisonBlock flint = SpigotUtil.getPrisonBlock( XMaterial.FLINT );
				SpigotItemStack flintStack = new SpigotItemStack( quantity, flint );
				adds.add(flintStack);
			}
		}
		return adds;
	}

	public Random getRandom() {
		return random;
	}

}
package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import me.pulsi_.prisonenchants.events.PEExplosionEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerBreakBlockTask;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.integrations.IntegrationCrazyEnchantmentsPickaxes;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.util.Text;

public class OnBlockBreakEventCore
	extends OnBlockBreakMines
{

	
	private int uses = 0;
	private long usesElapsedTimeNano = 0L;
	
	private boolean teExplosionTriggerEnabled;
	
	private AutoFeaturesWrapper autoFeatureWrapper = null;
	
	
	private Boolean crazyEnchantEnabled;
	
	private Random random = new Random();
	
	
	public OnBlockBreakEventCore() {
		super();
		
		this.autoFeatureWrapper = AutoFeaturesWrapper.getInstance();
		
	
		
		this.teExplosionTriggerEnabled = true;
		
		this.crazyEnchantEnabled = null;
	}
	

	public enum EventDetails {
		monitor,
		auto_manager,
		block_events_only
		;
	}
	
	
	/**
	 * <p>The Prison Mines module must be enabled, or these BlockBreakEvents should 
	 * not be enabled since they are geared to work with the prison mines.
	 * </p>
	 * 
	 * <p>At this time, prison's block handling is not supported outside of the mines.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		boolean results = false;

		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
			PrisonMines prisonMines = (PrisonMines) mmOptional.get();
			
			results = prisonMines != null;
		}
		
		return results;
	}

	public AutoFeaturesFileConfig getAutoFeaturesConfig() {
		return autoFeatureWrapper.getAutoFeaturesConfig();
	}

	public boolean isBoolean( AutoFeatures feature ) {
		return autoFeatureWrapper.isBoolean( feature );
	}

	public String getMessage( AutoFeatures feature ) {
		return autoFeatureWrapper.getMessage( feature );
	}

	public int getInteger( AutoFeatures feature ) {
		return autoFeatureWrapper.getInteger( feature );
	}
	
	public double getDouble( AutoFeatures feature ) {
		return autoFeatureWrapper.getDouble( feature );
	}
	
	protected List<String> getListString( AutoFeatures feature ) {
		return autoFeatureWrapper.getListString( feature );
	}
	
	
	
	public enum ItemLoreCounters {

		// NOTE: the String value must include a trailing space!

		itemLoreBlockBreakCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Mined:" +
				ChatColor.GRAY + " "),

		itemLoreBlockExplodeCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Exploded:" +
				ChatColor.GRAY + " ");


		private final String lore;
		ItemLoreCounters( String lore ) {
			this.lore = lore;
		}
		public String getLore() {
			return lore;
		}

	}

	public enum ItemLoreEnablers {
		Pickup,
		Smelt,
		Block
		;
	}
	
	
    
    
    protected void genericBlockEventMonitor( BlockBreakEvent e ) {
    	genericBlockEvent( e, true, false, false );
    }
    
    protected void genericBlockEvent( BlockBreakEvent e ) {
    	genericBlockEvent( e, false, false, false );
    }

    protected void genericBlockEventAutoManager( BlockBreakEvent e ) {
    	// NOTE: If autoManager is turned off, then process only the blockEvents:
    	boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
    	genericBlockEvent( e, false, blockEventsOnly, true );
    }
    
    
	protected void genericBlockExplodeEventMonitor( TEBlockExplodeEvent e ) {
		genericBlockExplodeEvent( e, true, false, false );
	}
	
	protected void genericBlockExplodeEvent( TEBlockExplodeEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericBlockExplodeEvent( e, false, blockEventsOnly, false );
	}
	
	protected void genericBlockExplodeEventAutoManager( TEBlockExplodeEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericBlockExplodeEvent( e, false, blockEventsOnly, true );
	}
	

	protected void genericBlockExplodeEventMonitor( BlastUseEvent e ) {
		genericBlastUseEvent( e, true, false, false );
	}
	
	protected void genericBlockExplodeEvent( BlastUseEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericBlastUseEvent( e, false, blockEventsOnly, false );
	}

	protected void genericBlockExplodeEventAutoManager( BlastUseEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericBlastUseEvent( e, false, blockEventsOnly, true );
	}
	
	
	/**
	 * For Pulsi_'s PrisonEnchants plugin:
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEventMonitor( PEExplosionEvent e ) {
		genericExplosiveEvent( e, true, false, false );
	}
	
	/**
	 * For Pulsi_'s PrisonEnchants plugin:
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEvent( PEExplosionEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericExplosiveEvent( e, false, blockEventsOnly, false );
	}
	
	/**
	 * For Pulsi_'s PrisonEnchants plugin:
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEventAutoManager( PEExplosionEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericExplosiveEvent( e, false, blockEventsOnly, true );
	}
	
	
	
	
	/**
	 * For Prison's very own ExplosiveBlockBreakEvent:
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEventMonitor( ExplosiveBlockBreakEvent e ) {
		genericExplosiveEvent( e, true, false, false );
	}
	
	/**
	 * For Prison's very own ExplosiveBlockBreakEvent:
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEvent( ExplosiveBlockBreakEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericExplosiveEvent( e, false, blockEventsOnly, false );
	}
	
	/**
	 * For Prison's very own ExplosiveBlockBreakEvent:
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEventAutoManager( ExplosiveBlockBreakEvent e ) {
		// NOTE: If autoManager is turned off, then process only the blockEvents:
		boolean blockEventsOnly = !isBoolean(AutoFeatures.isAutoManagerEnabled);
		genericExplosiveEvent( e, false, blockEventsOnly, true );
	}
	
//	protected boolean processMinesBlockBreakEvent( Cancellable event, Player player, Block block ) {
//		boolean processEvent = true;
//		
//		SpigotBlock sBlock = new SpigotBlock( block );
//		if ( BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
//			event.setCancelled( true );
//			processEvent = false;
//		}
//		
//		Mine mine = findMine( player, sBlock,  null, null ); 
//		
//		if ( mine == null  ) {
//			// Prison is unable to process blocks outside of mines right now, so exit:
//			processEvent = false;
//		}
//		
//		// If not minable, then display message and exit.
//		if ( !mine.getMineStateMutex().isMinable() ) {
//			
//			SpigotPlayer sPlayer = new SpigotPlayer( player );
//			sPlayer.setActionBar( "Mine " + mine.getTag() + " is being reset... please wait." );
//			event.setCancelled( true );
//			processEvent = false;
//		}
//		MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( sBlock );
//		
//		// If ignore all block events, then exit this function without logging anything:
//		if ( targetBlock.isIgnoreAllBlockEvents() ) {
//			event.setCancelled( true );
//			processEvent = false;
//		}
//
//		
//		return processEvent;
//	}
	
    /**
     * <p>This genericBlockEvent handles the basics of a BlockBreakEvent to see if it has happened
     * within a mine or not.  If it is happening within a mine, then we process it with the doAction()
     * function.
     * </p>
     * 
     * @param e
     * @param montior Identifies that a monitor event called this function.  A monitor should only record
     * 					block break counts.
     */
	protected void genericBlockEvent( BlockBreakEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {
		
		if ( e instanceof PrisonMinesBlockBreakEvent ) {
			return;
		}
		
    	long start = System.nanoTime();

    	if ( e.isCancelled() ||  ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlock()) ) {
    		return;
    	}

    	
		
		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** genericBlockEvent ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" )) );
		
		
		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( !monitor && !e.isCancelled() || monitor ) 
    	{

    		// Need to wrap in a Prison block so it can be used with the mines:
    		SpigotBlock sBlock = new SpigotBlock(e.getBlock());
    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.blockBreak;
    		String triggered = null;
    		
    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
    					sBlock, sPlayer, monitor, blockEventsOnly, eventType, triggered );
    		
    		if ( !validateEvent( pmEvent, debugInfo ) ) {
    			
    			// The event has not passed validation. All logging and Errors have been recorded
    			// so do nothing more. This is to just prevent normal processing from occurring.
    			
    			if ( pmEvent.isCancelOriginalEvent() ) {
    				
    				e.setCancelled( true );
    			}
    		}
    		
    		else if ( pmEvent.isMonitor() ) {
    			// Stop here, and prevent additional processing. Monitors should never process the event beyond this.
    		}
    		
    		// This is where the processing actually happens:
    		else if ( pmEvent.getMine() != null || pmEvent.getMine() == null && 
    									!isBoolean( AutoFeatures.pickupLimitToMines ) ) {
    			debugInfo.append( "(normal processing initiating) " );
    			
    			// Set the mine's PrisonBlockTypes for the block. Used to identify custom blocks.
    			// Needed since processing of the block will lose track of which mine it came from.
    			if ( pmEvent.getMine() != null ) {
    				sBlock.setPrisonBlockTypes( pmEvent.getMine().getPrisonBlockTypes() );
    			}
    			
    			// check all external events such as mcMMO and EZBlocks:
    			OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e );
    			
    			List<SpigotBlock> explodedBlocks = new ArrayList<>();
    			pmEvent.setExplodedBlocks( explodedBlocks );
//    			String triggered = null;
    			
//    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
//    							pmEvent.getMine(), sBlock, explodedBlocks, BlockEventType.blockBreak, triggered );
                Bukkit.getServer().getPluginManager().callEvent( pmEvent );
                if ( pmEvent.isCancelled() ) {
                	debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
                }
                else {
                	
                	// doAction returns a boolean that indicates if the event should be canceled or not:
                	if ( doAction( pmEvent, debugInfo ) ) {
//                	if ( doAction( sBlock, pmEvent.getMine(), pmEvent.getPlayer(), debugInfo ) ) {

                		if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
                			e.setCancelled( true );
                		}
                		else {
                			
                			debugInfo.append( "(event was not canceled) " );
                		}
                		
                		finalizeBreakTheBlocks( pmEvent );
                		
                		doBlockEvents( pmEvent );
                		
                	}
                	else {
                		
                		debugInfo.append( "(doAction failed without details) " );
                	}

                }
    			
    			
                debugInfo.append( "(normal processing completed) " );
    		}
    		else {
    			
    			debugInfo.append( "(logic bypass) " );
    		}
    		
    	}
    	
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
	}

	protected boolean ignoreMinesBlockBreakEvent( Cancellable event, Player player, Block block ) {
	
	MinesEventResults eventResults = ignoreMinesBlockBreakEvent( player, block );
	
	if ( eventResults.isCancelEvent() ) {
		event.setCancelled( eventResults.isCancelEvent() );
	}
	return eventResults.isIgnoreEvent();
}

protected boolean processMinesBlockBreakEvent( PEExplosionEvent event, Player player, Block block ) {
	
	MinesEventResults eventResults = ignoreMinesBlockBreakEvent( player, block );
	
	if ( eventResults.isCancelEvent() ) {
		event.setCancelled( eventResults.isCancelEvent() );
	}
	return eventResults.isIgnoreEvent();
}


	/**
	 * <p>This processes the block finalizations, which are counting the block breakage,
	 * and also processes the blockEvents.  It's important to process the block events
	 * after the blocks are broken (set to AIR) to ensure that when the blockEvents are 
	 * ran, then the block would already be set to AIR if it is being broke inline.
	 * </p>
	 * 
	 * @param pmEvent
	 */
	private void doBlockEvents( PrisonMinesBlockBreakEvent pmEvent )
	{

		if ( pmEvent.getMine() != null ) {
			
			// apply to ALL blocks including exploded:
			applyBlockFinalizations( pmEvent, pmEvent.getTargetBlock() );

			
			for ( MineTargetPrisonBlock teBlock : pmEvent.getTargetExplodedBlocks() ) {
				
				applyBlockFinalizations( pmEvent, teBlock );
			}
			
			checkZeroBlockReset( pmEvent.getMine() );
		}
	}

	private void finalizeBreakTheBlocks( PrisonMinesBlockBreakEvent pmEvent )
	{
		List<SpigotBlock> blocks = finalizeBreakTheBlocksCollectEm( pmEvent );
		
		if ( isBoolean( AutoFeatures.applyBlockBreaksThroughSyncTask ) ) {
			
			AutoManagerBreakBlockTask.submitTask( blocks, pmEvent.getMine() );
		}
		else {
			
			int count = 0;
			for ( SpigotBlock spigotBlock : blocks ) {
				
				if ( count++ % 10 == 0 && pmEvent.getMine() != null && 
						!pmEvent.getMine().getMineStateMutex().isMinable() ) {
					
					SpigotPlayer sPlayer = pmEvent.getSpigotPlayer();
					sPlayer.setActionBar( "Mine " + pmEvent.getMine().getTag() + " is being reset... please wait." );
					break;
				}
				
				spigotBlock.setPrisonBlock( PrisonBlock.AIR );
			}
		}
				
	}
	private List<SpigotBlock> finalizeBreakTheBlocksCollectEm( PrisonMinesBlockBreakEvent pmEvent ) {
		List<SpigotBlock> blocks = new ArrayList<>();
		
		if ( pmEvent.getTargetBlock() != null && pmEvent.getTargetBlock().getMinedBlock() != null ) {
			
			SpigotBlock minedBlock = ((SpigotBlock) pmEvent.getTargetBlock().getMinedBlock());
			
			// Only add the minedBlock to the blocks list if it matches the expected targetBlock name, which
			// indicates it has not been replaced by something else, such as the result of a block event.
			if ( pmEvent.getTargetBlock().getPrisonBlock().getBlockName().equalsIgnoreCase( minedBlock.getBlockName() )) {
				
				blocks.add( minedBlock );
				pmEvent.getTargetBlock().setAirBroke( true );
//				pmEvent.getTargetBlock().setMinedBlock( null );
			}
			
		}
		
		for ( MineTargetPrisonBlock targetBlock : pmEvent.getTargetExplodedBlocks() ) {
			
			if ( targetBlock != null && targetBlock.getMinedBlock() != null ) {

				SpigotBlock minedBlock = ((SpigotBlock) targetBlock.getMinedBlock());

				// Only add the minedBlock to the blocks list if it matches the expected targetBlock name, which
				// indicates it has not been replaced by something else, such as the result of a block event.
				if ( targetBlock.getPrisonBlock().getBlockName().equalsIgnoreCase( minedBlock.getBlockName() )) {
					
					blocks.add( minedBlock );
					targetBlock.setAirBroke( true );
//					targetBlock.setMinedBlock( null );
				}
				
			}
		}
		
		return blocks;
	}


	
	/**
	 * <p>This function an attempt to provide a uniform procedure to validate if the event should 
	 * be processed.  This will eliminate a lot of duplicate code, and will make supporting other
	 * plugin events easier to implement.
	 * </p>
	 * 
	 * <p>This is using the PrisonMinesBlockBreakEvent object to act as the vehicle for carrying
	 * all of the possible parameters that are needed for appropriate processing.
	 * </p>
	 * 
	 * @param pmEvent
	 * @param debugInfo
	 * @return
	 */
	private boolean validateEvent( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo )
	{
		boolean results = true;
		

		SpigotBlock sBlockHit = pmEvent.getSpigotBlock();
		
		Mine mine = findMine( pmEvent.getPlayer(), sBlockHit, 
				pmEvent.getUnprocessedRawBlocks(), pmEvent );
		
		pmEvent.setMine( mine );
		
		debugInfo.append( "mine=" + (mine == null ? "none" : mine.getName()) + " " );
		
		debugInfo.append( sBlockHit.getLocation().toWorldCoordinates() ).append( " " );
		
		// Since BlastUseEvent (crazy enchant) does not identify the block that is initially 
		// broke, an explosion for them is greater than 1.
		boolean isExplosionEvent = pmEvent.getUnprocessedRawBlocks().size() > 
					(pmEvent.getBlockEventType() == BlockEventType.CEXplosion ? 0 : 1);
		
		// validate the blocks, if there are some.  Add them to the exploded blocks list
		if ( mine != null ) {
			int unbreakable = 0;
			int outsideOfMine = 0;
			int alreadyMined = 0;
			int noTargetBlock = 0;
			int blockTypeNotExpected = 0;
			
			boolean targetBlockAlreadyMined = false;
			
			// Get the mine's targetBlock:
			MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( sBlockHit );
			pmEvent.setTargetBlock( targetBlock );
			
			// If ignore all block events has been set on this target block, then shutdown.
			// Same if this block was already included in an explosion... prevent it from spawning
			// more explosions, which could result in a chain reaction.
			if ( targetBlock != null && 
					(targetBlock.isIgnoreAllBlockEvents() || 
					 targetBlock.isExploded()) ) {
				debugInfo.setLength( 0 );
				
				pmEvent.setForceIfAirBlock( false );
				
				// do not cancel event so some other listener can process it
				pmEvent.setCancelOriginalEvent( false );
				
				return false;
			}
			
			// NOTE: for the primary block pmEvent.getSpigotBlock() the unbreakable will be checked later:
			if ( targetBlock != null && sBlockHit != null ) {
				
				if ( !targetBlock.isMined() || !targetBlock.isAirBroke() ) {
				
		    		// The field isMined() is used to "reserve" a block to indicate that it is in 
		    		// the stages of being processed, since much later in the processing will the
		    		// block be set to setAirBreak() or even setCounted().  This prevents 
		    		// high-speed or concurrent operations from multiple players from trying to 
		    		// process the same block. 

					if ( targetBlock.getPrisonBlock().equals( sBlockHit.getPrisonBlock() ) &&
							collectBukkitDrops( pmEvent.getBukkitDrops(), targetBlock, pmEvent.getItemInHand(), sBlockHit )) {
						
						
						// If a chain reaction on explosions, this will prevent the same block from
						// being processed more than once:
						targetBlock.setMined( true );
						
						targetBlock.setMinedBlock( sBlockHit );
						
						
						// Mark the block as being part of an explosion, if it was:
						targetBlock.setExploded( isExplosionEvent );

					}
					else {
						// The block is not the correct type. It has been changed since the mine was reset
						// so it cannot be processed.
						
						
						if ( Output.get().isDebug( DebugTarget.targetBlockMismatch ) ) {
							
							String message = String.format( 
									"TargetBlock mismatch error - primaryBlock:  targetBlock: %s  blockBroke: %s",
									targetBlock.getPrisonBlock().getBlockName() , 
									sBlockHit.getPrisonBlock().getBlockName()
									);
							
							Output.get().logWarn( message );
							
						}
						
						// Prevent this block from being processed again, or attempted to be processed:
						
						targetBlockAlreadyMined = true;

						targetBlock.setMined( true );
						targetBlock.setAirBroke( true );
						targetBlock.setIgnoreAllBlockEvents( true );
						
						// do not cancel event so some other listener can process it
						pmEvent.setCancelOriginalEvent( false );
						
						blockTypeNotExpected++;
						
						if ( !pmEvent.isForceIfAirBlock() ) {
							
							results = false;
						}
						
					}

					
				}
				else {
					alreadyMined++;
					targetBlockAlreadyMined = true;
					
					if ( !targetBlock.isMined() ) {
						targetBlock.setMined( true );
					}
					
					// A mine bomb will be "set" above a valid mine block, so it would generally be air and
					// it probably was already mined if it's not on top of the top layer of the mine.
					if ( !pmEvent.isForceIfAirBlock() ) {
						
						// This block has already been mined and is not a mine bomb, so fail the validation
						// and cancel the event since if it's not an air block, it may be another effect that
						// is placing a block within the mine, such as a prison util's decay function.
						debugInfo.append( "VALIDATION_FAILED_BLOCK_ALREADY_MINED " );
						
						results = false;
						
						pmEvent.setCancelOriginalEvent( true );
					}
					
				}
			}
			else {
				noTargetBlock++;
				
				debugInfo.append( "VALIDATION_FAILED_NO_TARGETBLOCK " );
				
				results = false;
			}
			
			
			// Don't start processing the unprocessedRawBlocks unless results is true, otherwise some 
			// of the blocks could be marked as being mined but then never processed.
			if ( results ) {
				
				for ( Block bukkitBlock : pmEvent.getUnprocessedRawBlocks() ) 
				{
					SpigotBlock sBlock = new SpigotBlock( bukkitBlock );
					
					// Thanks to CrazyEnchant, there is no telling which block was actually hit, so 
					// if using CrazyEnchant one of the unprocessedRawBlocks may be the same as the
					// pmEvent.getSpigotBlock(), so ignore if both are the same.
					if ( !sBlock.equals( sBlockHit ) ) {
						
						if ( !mine.isInMineExact( sBlock.getLocation() ) ) {
							outsideOfMine++;
						}
						else if ( BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
							
							unbreakable++;
						}
						
						else if ( sBlock.isEmpty() ) {
							alreadyMined++;
						}
						else {
							
							// Get the mine's targetBlock:
							MineTargetPrisonBlock targetExplodedBlock = mine.getTargetPrisonBlock( sBlock );
							
							if ( targetExplodedBlock == null || targetExplodedBlock.getPrisonBlock() == null ) {
								
								// No targetBlock so add it anyway:
								pmEvent.getExplodedBlocks().add( sBlock );
								
								noTargetBlock++;
							}
							
							else if ( targetExplodedBlock.isMined() ) {
								
								alreadyMined++;
							}
							else {
								
								if ( !targetExplodedBlock.isMined() ) {
									
									// Check to make sure the block is the same block that was placed there.
									// If not, then do not process it.
									SpigotBlock sBlockMined = new SpigotBlock( bukkitBlock );
									PrisonBlock pBlockMined = sBlockMined.getPrisonBlock();
									
									if ( targetExplodedBlock.getPrisonBlock().equals( pBlockMined ) &&
											collectBukkitDrops( pmEvent.getBukkitDrops(), targetExplodedBlock, pmEvent.getItemInHand(), sBlockMined ) ) {
										
										// If a chain reaction on explosions, this will prevent the same block from
										// being processed more than once:
										targetExplodedBlock.setMined( true );
										
										targetExplodedBlock.setMinedBlock( sBlock );
										
										
										// Mark the block as being part of an explosion, if it was:
										targetExplodedBlock.setExploded( isExplosionEvent );

										
										pmEvent.getExplodedBlocks().add( sBlock );
										pmEvent.getTargetExplodedBlocks().add( targetExplodedBlock );
										
										
									}
									else {
										// The block is not the correct type. It has been changed since the mine was reset
										// so it cannot be processed.
										
										
										if ( Output.get().isDebug( DebugTarget.targetBlockMismatch ) ) {
											
											String message = String.format( 
													"TargetBlock mismatch error - multiBLock:  targetBlock: %s  blockBroke: %s",
													targetBlock.getPrisonBlock().getBlockName() , 
													sBlockHit.getPrisonBlock().getBlockName()
													);
											
											Output.get().logWarn( message );
											
										}
										
										
										// Prevent this block from being processed again, or attempted to be processed:
										
										targetExplodedBlock.setMined( true );
										targetExplodedBlock.setAirBroke( true );
										targetExplodedBlock.setIgnoreAllBlockEvents( true );
										
										blockTypeNotExpected++;
									}
									
									
								}
							}
						}
					}
				}
			}
			
			
			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
				
				debugInfo.append( "VALIDATED_BLOCKS_IN_EXPLOSION (" + 
							pmEvent.getExplodedBlocks().size() + 
						" blocks) " );
			}
			if ( unbreakable > 0 ) {
				
				debugInfo.append( "UNBREAKABLE_BLOCK_UTILS (" + unbreakable + 
						" blocks, event not canceled) " );
			}
			if ( outsideOfMine > 0 ) {
				
				debugInfo.append( "BLOCKS_OUTSIDE_OF_MINE (" + outsideOfMine + 
						" blocks, event not canceled) " );
			}
			if ( alreadyMined > 0 ) {
				
				debugInfo.append( "BLOCKS_ALREADY_MINED (" + alreadyMined + 
						" ) " );
			}
			if ( noTargetBlock > 0 ) {
				
				debugInfo.append( "NO_TARGET_BLOCKS (" + noTargetBlock + 
						" ) " );
			}
			if ( blockTypeNotExpected > 0 ) {
				
				debugInfo.append( "BLOCK_TYPE_NOT_EXPECTED__CANNOT_PROCESS (" + blockTypeNotExpected + 
						" ) " );
			}
			
			
			// Need to compress the drops to eliminate duplicates:
			pmEvent.setBukkitDrops( mergeDrops( pmEvent.getBukkitDrops() ) );
						
			
			// If target block already was mined and there are no exploded blocks, then this whole event 
			// needs to be canceled since it sounds like a blockevent fired a prison util explosion that
			// has zero blocks tied to it.
			if ( targetBlockAlreadyMined && pmEvent.isForceIfAirBlock() && pmEvent.getExplodedBlocks().size() == 0 ) {
				
				// Since this was a dud event, we must set the flag to ignore all 
				// future block events that include this block as the primary block.
				// This code block cancels the current event, but we must ensure that
				// the monitor event is also canceled.
				pmEvent.getTargetBlock().setIgnoreAllBlockEvents( true );
				
				pmEvent.setForceIfAirBlock( false );
				
				results = false;
				
				pmEvent.setCancelOriginalEvent( true );

				// Ignore event and clear debugInfo:
				debugInfo.setLength( 0 );
				
				return results;
			}
			
		}
		
		
		debugInfo.append( "blocks(" )
			.append( pmEvent.getBlock() == null ? "0" : "1" )
			.append( "+" )
			.append( pmEvent.getExplodedBlocks().size() )
			.append( ") " );

		if ( isToolDisabled( pmEvent.getPlayer() ) ) {
			
			// This will prevent sending too many messages since it is using PlayerMessagingTask:
			pmEvent.getSpigotPlayer().setActionBar( "&cYour tool is worn-out and cannot be used." );
			
//			PrisonUtilsTitles uTitles = new PrisonUtilsTitles();
//			uTitles.utilsTitlesActionBarForce( pmEvent.getSpigotPlayer(), null, 
//					"&cYour tool is worn-out and cannot be used." );
			
			pmEvent.setCancelOriginalEvent( true );
			debugInfo.append( "UNUSABLE_TOOL__WORN_OUT (event canceled) " );
			results = false;
		}
		if ( mine != null && BlockUtils.getInstance().isUnbreakable( sBlockHit ) ) {
			// The block is unbreakable because a utility has it locked:
			
			pmEvent.setCancelOriginalEvent( true );
			debugInfo.append( "UNBREAKABLE_BLOCK_UTILS (event canceled) " );
			results = false;
		}
		if ( mine != null && (mine.isMineAccessByRank() || mine.isAccessPermissionEnabled()) && 
					!mine.hasMiningAccess( pmEvent.getSpigotPlayer() ) ) {
			// The player does not have permission to access this mine, so do not process 
			// 
			
			pmEvent.setCancelOriginalEvent( true );
			debugInfo.append( "ACCESS_DENIED (event canceled - Access by rank/perm/perms) " );
			results = false;
		}
		
		
		
		if ( results && pmEvent.isBlockEventsOnly() ) {
			
			// NOTE: This "should" never be activated since blockEventsOnly used to be enabled
			//       when AutoManager is disabled.  As of 2021-11-23 if AutoManager is disabled,
			//       then everything related to auto manager is disabled.
			String triggered = null;
			
			doActionBlockEventOnly( sBlockHit, mine, pmEvent.getPlayer(), 
					BlockEventType.blockBreak, triggered );

			debugInfo.append( "(actionBlockEventOnly singluar) " );
			
			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
				
				for ( SpigotBlock sBlock : pmEvent.getExplodedBlocks() ) {
					
					doActionBlockEventOnly( sBlock, mine, pmEvent.getPlayer(), 
							pmEvent.getBlockEventType(), pmEvent.getTriggered() );
				}
				
				debugInfo.append( "(actionBlockEventOnly - " + 
						pmEvent.getExplodedBlocks().size() +
							" Exploded Blocks - finalized) " );
			}
			
			results = false;
		}
		else if ( results && pmEvent.isMonitor() && mine == null ) {
			// bypass all processing since the block break is outside any mine:
			
			debugInfo.append( "(bypassed monitor no mine) " );
			results = false;
		}
		else if ( results && pmEvent.isMonitor() && mine != null ) {
			
			doActionMonitor( sBlockHit, mine );
			
			debugInfo.append( "(monitor - singular) " );
			
			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
				
				for ( SpigotBlock sBlock : pmEvent.getExplodedBlocks() ) {
    				
					doActionMonitor( sBlock, mine );
    			}

    			debugInfo.append( "(monitor - " + 
						pmEvent.getExplodedBlocks().size() +
							" Exploded Blocks - finalized) " );
			}

			// should be true at this point:
			//results = true;
		}
		
		
		if ( results && isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {

			clearBukkitDrops( pmEvent.getBukkitDrops(), pmEvent.getTargetBlock() );
			
			for ( MineTargetPrisonBlock targetBlock : pmEvent.getTargetExplodedBlocks() )
			{
				clearBukkitDrops( pmEvent.getBukkitDrops(), targetBlock );
				
			}
		
		}
		
//		if ( results ) {
//			// Collect the bukkit drops && cancel the drops if needed
//			
//			collectBukkitDrops( pmEvent.getBukkitDrops(), pmEvent.getTargetBlock(), 
//									pmEvent.getItemInHand() );
//			
//			for ( MineTargetPrisonBlock targetBlock : pmEvent.getTargetExplodedBlocks() )
//			{
//				collectBukkitDrops( pmEvent.getBukkitDrops(), targetBlock, 
//									pmEvent.getItemInHand() );
//				
//			}
//			
//			// Need to compress the drops to eliminate duplicates:
//			pmEvent.setBukkitDrops( mergeDrops( pmEvent.getBukkitDrops() ) );
//		}
		
		
		if ( results ) {
			debugInfo.append( "(PassedValidation) " );
		}
		else {
			debugInfo.append( "(ValidationFailed) " );
		}

		
		return results;
	}

	private boolean collectBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock, 
										SpigotItemStack itemInHand, SpigotBlock sBlockMined )
	{
		boolean results = false;
		
//		if ( sBlockMined == null && targetBlock.getMinedBlock() != null ) {
//			sBlockMined = (SpigotBlock) targetBlock.getMinedBlock();
//		}
		//SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();
		
		if ( sBlockMined != null && targetBlock.getPrisonBlock().equals( sBlockMined.getPrisonBlock() ) ) {
			
			List<SpigotItemStack> drops = SpigotUtil.getDrops(sBlockMined, itemInHand);
			
			bukkitDrops.addAll( drops );
			
//			// This clears the drops for the given block, so if the event is not canceled, it will
//			// not result in duplicate drops.
//			if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//				sBlock.clearDrops();
//			}
			
			results = true;
			
		}
		else if ( sBlockMined != null) {
			Output.get().logWarn( "collectBukkitDrops: block was changed and not what was expected.  " +
					"Block: " + sBlockMined.getBlockName() + "  expecting: " + targetBlock.getPrisonBlock().getBlockName() );
		}
		
		return results;
	}
	
	
	private void clearBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock )
	{

		SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();
		sBlock.clearDrops();

	}

	/**
	 * <p>The List of drops must have only one ItemStack per block type (name).
	 * This function combines multiple occurrences together and adds up their 
	 * counts to properly represent the total quantity in the original drops collection
	 * that had duplicate entries.
	 * </p>
	 * 
	 * @param List of SpigotItemStack drops with duplicate entries
	 * @return List of SpigotItemStack drops without duplicates
	 */
	private List<SpigotItemStack> mergeDrops( List<SpigotItemStack> drops )
	{
		TreeMap<String,SpigotItemStack> results = new TreeMap<>();

		for ( SpigotItemStack drop : drops ) {
			String key = drop.getName();
			if ( !results.containsKey( key ) ) {
				results.put( key, drop );
			}
			else {
				SpigotItemStack sItemStack = results.get( key );
				
				sItemStack.setAmount( sItemStack.getAmount() + drop.getAmount() );
			}
		}
		
		return new ArrayList<>( results.values() );
	}

	/**
	 * <p>Since there are multiple blocks associated with this event, pull out the player first and
	 * get the mine, then loop through those blocks to make sure they are within the mine.
	 * </p>
	 * 
	 * <p>The logic in this function is slightly different compared to genericBlockEvent() because this
	 * event contains multiple blocks so it's far more efficient to process the player data once. 
	 * So that basically needed a slight refactoring.
	 * </p>
	 * 
	 * @param e
	 */
	private void genericBlockExplodeEvent( TEBlockExplodeEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {

		long start = System.nanoTime();
		
    	if ( e.isCancelled() ||  ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlock()) ) {
    		return;
    	}
    	

		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** genericBlockExplodeEvent(TEBlockExplodeEvent) ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" )) );
		


		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( !monitor && !e.isCancelled() || monitor ) {
    		
    		
	    	String eP = getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
			boolean isTEExplosiveEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );


    		// Need to wrap in a Prison block so it can be used with the mines:
    		SpigotBlock sBlock = new SpigotBlock(e.getBlock());
    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.TEXplosion;
    		String triggered = checkCEExplosionTriggered( e );
    		
    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
    					sBlock, sPlayer, monitor, blockEventsOnly, eventType, triggered );
    		
    		// NOTE: Token Enchant will pass the event's block to prison, but that block may 
    		//       have already been processed by prison.  Therefore the PrisonMinesBlockBreakEvent
    		//       must enable the feature setForceIfAirBlock( true ).  That block will not be used a 
    		//       second time, but it will allow the explosion event to be processed.
    		pmEvent.setForceIfAirBlock( true );
    		
    		pmEvent.setUnprocessedRawBlocks( e.blockList() );
    		
    		if ( !validateEvent( pmEvent, debugInfo ) ) {
    			
    			// The event has not passed validation. All logging and Errors have been recorded
    			// so do nothing more. This is to just prevent normal processing from occurring.
    			
    			if ( pmEvent.isCancelOriginalEvent() ) {
    				
    				e.setCancelled( true );
    			}
    		}
    		
    		else if ( pmEvent.isMonitor() ) {
    			// Stop here, and prevent additional processing. Monitors should never process the event beyond this.
    		}
    		

    		
    		// now process all blocks (non-monitor):
    		else if ( isTEExplosiveEnabled && 
    				( pmEvent.getMine() != null || pmEvent.getMine() == null && 
    									!isBoolean( AutoFeatures.pickupLimitToMines )) ) {

    			
    			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
    				
//					String triggered = checkCEExplosionTriggered( e );
//					
//
//					PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
//	    												mine, block, explodedBlocks, BlockEventType.TEXplosion, triggered );
	                Bukkit.getServer().getPluginManager().callEvent( pmEvent );
	                if ( pmEvent.isCancelled() ) {
	                	debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
	                }
	                else {
	                	
	                	// This is where the processing actually happens:
	                	if ( doAction( pmEvent, debugInfo ) ) {
	                		
	                		if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
	                			
	                			e.setCancelled( true );
	                		}
	                		else {
	                			
	                			debugInfo.append( "(event was not canceled) " );
	                		}
	                		
	                		finalizeBreakTheBlocks( pmEvent );
	                		
	                		doBlockEvents( pmEvent );

	                	}
	                	
	                	else {
	                		
	                		debugInfo.append( "(doAction failed without details) " );
	                	}
	                	
	                }
    				
    			}
    			
    			
    			debugInfo.append( "(normal processing) " );
   			}
    		else {
    			
    			debugInfo.append( "(logic bypass) " );
    		}
    			
    	}
    	
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
	}



	private String checkCEExplosionTriggered( TEBlockExplodeEvent e )
	{
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
		
		return triggered;
	}

	
	
	/**
	 * <p>Since there are multiple blocks associated with this event, pull out the player first and
	 * get the mine, then loop through those blocks to make sure they are within the mine.
	 * </p>
	 * 
	 * <p>The logic in this function is slightly different compared to genericBlockEvent() because this
	 * event contains multiple blocks so it's far more efficient to process the player data once. 
	 * So that basically needed a slight refactoring.
	 * </p>
	 * 
	 * @param e
	 */
	protected void genericBlastUseEvent( BlastUseEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {

		long start = System.nanoTime();
		
    	if ( e.isCancelled() ||  ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlockList().get( 0 )) ) {
    		return;
    	}
    	
		
		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
				
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** genericBlastUseEvent(BlastUseEvent) ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" )) );
		


		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( (!monitor && !e.isCancelled() || monitor) && 
				e.getBlockList().size() > 0 ) {

			
	    	String eP = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
			boolean isCEBlockExplodeEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

			
    		Block bukkitBlock = e.getBlockList().get( 0 );
    		
    		// Need to wrap in a Prison block so it can be used with the mines:
    		SpigotBlock sBlock = new SpigotBlock( bukkitBlock );
    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.CEXplosion;
    		String triggered = null;
    		

    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( bukkitBlock, e.getPlayer(),
					sBlock, sPlayer, monitor, blockEventsOnly, eventType, triggered );
		
    		
    		for ( int i = 1; i < e.getBlockList().size(); i++ ) {
    			pmEvent.getUnprocessedRawBlocks().add( e.getBlockList().get( i ) );
    		}
    		
    		
    		if ( !validateEvent( pmEvent, debugInfo ) ) {
    			
    			// The event has not passed validation. All logging and Errors have been recorded
    			// so do nothing more. This is to just prevent normal processing from occurring.
    			
    			if ( pmEvent.isCancelOriginalEvent() ) {
    				
    				e.setCancelled( true );
    			}
    		}

    		
    		else if ( pmEvent.isMonitor() ) {
    			// Stop here, and prevent additional processing. Monitors should never process the event beyond this.
    		}
    		


    		// now process all blocks (non-monitor):
    		else if ( isCEBlockExplodeEnabled && 
    				( pmEvent.getMine() != null || pmEvent.getMine() == null && !isBoolean( AutoFeatures.pickupLimitToMines )) ) {


    			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
    				
//					String triggered = null;
    				
					
					// Warning: BlastUseEvent does not identify the block the player actually hit, so the dummyBlock
					//          is just a random first block from the explodedBlocks list and may not be the block
					//          that initiated the explosion event.
//					SpigotBlock dummyBlock = explodedBlocks.get( 0 );
					
//	    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( dummyBlock.getWrapper(), e.getPlayer(),
//	    												mine, dummyBlock, explodedBlocks, BlockEventType.CEXplosion, triggered );
	                Bukkit.getServer().getPluginManager().callEvent(pmEvent);
	                if ( pmEvent.isCancelled() ) {
	                	debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
	                }
	                else {
	                	
	                	if ( doAction( pmEvent, debugInfo ) ) {
	                		
	                		if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
	                			
	                			e.setCancelled( true );
	                		}
	                		else {
	                			
	                			debugInfo.append( "(event was not canceled) " );
	                		}
	                		
	                		finalizeBreakTheBlocks( pmEvent );
	                		
	                		doBlockEvents( pmEvent );

	                	}
	                	
	                	else {
	                		
	                		debugInfo.append( "(doAction failed without details) " );
	                	}
	                	
	                }
    			}
    			

    			debugInfo.append( "(normal processing) " );
    		}
    		else {
    			
    			debugInfo.append( "(logic bypass) " );
    		}

		}
    	
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
	}
	

	
	/**
	 * <p>Since there are multiple blocks associated with this event, pull out the player first and
	 * get the mine, then loop through those blocks to make sure they are within the mine.
	 * </p>
	 * 
	 * <p>The logic in this function is slightly different compared to genericBlockEvent() because this
	 * event contains multiple blocks so it's far more efficient to process the player data once. 
	 * So that basically needed a slight refactoring.
	 * </p>
	 * 
	 * @param e
	 */
	protected void genericExplosiveEvent( PEExplosionEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {

		long start = System.nanoTime();
		
    	if ( e.isCancelled() ||  processMinesBlockBreakEvent( e, e.getPlayer(), e.getBlockBroken()) ) {
    		return;
    	}
    	

		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
				
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** genericExplosiveEvent(PrisonEnchants - ExplosiveEvent) ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" )) );
		


		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( !monitor && !e.isCancelled() || monitor ) {
		
    		
	    	String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
			boolean isPEExplosiveEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );


    		// Need to wrap in a Prison block so it can be used with the mines:
    		SpigotBlock sBlock = new SpigotBlock(e.getBlockBroken());
    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.PEExplosive;
    		String triggered = null; // e.getTriggeredBy();
    		
    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( e.getBlockBroken(), e.getPlayer(),
    					sBlock, sPlayer, monitor, blockEventsOnly, eventType, triggered );
    		
    		pmEvent.setUnprocessedRawBlocks( e.getExplodedBlocks() );
    		
    		
    		if ( !validateEvent( pmEvent, debugInfo ) ) {
    			
    			// The event has not passed validation. All logging and Errors have been recorded
    			// so do nothing more. This is to just prevent normal processing from occurring.
    			
    			if ( pmEvent.isCancelOriginalEvent() ) {
    				
    				e.setCancelled( true );
    			}
    		}

    		
    		else if ( pmEvent.isMonitor() ) {
    			// Stop here, and prevent additional processing. Monitors should never process the event beyond this.
    		}
    		


    		// now process all blocks (non-monitor):
    		else if ( isPEExplosiveEnabled && 
    				( pmEvent.getMine() != null || pmEvent.getMine() == null && !isBoolean( AutoFeatures.pickupLimitToMines )) ) {
    			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
    				
//					String triggered = null;
    				
					
//	    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( dummyBlock.getWrapper(), e.getPlayer(),
//	    												mine, dummyBlock, explodedBlocks, BlockEventType.PEExplosive, triggered );
	                Bukkit.getServer().getPluginManager().callEvent(pmEvent);
	                if ( pmEvent.isCancelled() ) {
	                	debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
	                }
	                else {
	                	
	                	if ( doAction( pmEvent, debugInfo ) ) {
	                		
	                		if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
	                			
	                			e.setCancelled( true );
	                		}
	                		else {
	                			
	                			debugInfo.append( "(event was not canceled) " );
	                		}
	                		
	                		finalizeBreakTheBlocks( pmEvent );
	                		
	                		doBlockEvents( pmEvent );

	                	}
	                	
	                	else {
	                		
	                		debugInfo.append( "(doAction failed without details) " );
	                	}
	                	
	                }
    			}
    			
    			
    			debugInfo.append( "(normal processing) " );
    		}
    		else {
    			
    			debugInfo.append( "(logic bypass) " );
    		}

		}
    	
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}

	}
	
	protected void genericExplosiveEvent( ExplosiveBlockBreakEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {
		
		long start = System.nanoTime();
		
    	if ( e.isCancelled() ||  ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlock()) ) {
    		return;
    	}
    	
		
		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** genericExplosiveEvent(Prison's - ExplosiveBlockBreakEvent) ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" )) );
		
		// NOTE that check for auto manager has happened prior to accessing this function.
		if ( !monitor && !e.isCancelled() || monitor ) {
			
			
	    	String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
			boolean isPPrisonExplosiveBlockBreakEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

			
			// Need to wrap in a Prison block so it can be used with the mines:
			SpigotBlock sBlock = new SpigotBlock(e.getBlock());
			SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
			
			BlockEventType eventType = BlockEventType.PrisonExplosion;
			String triggered = e.getTriggeredBy();
			
			PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
					sBlock, sPlayer, monitor, blockEventsOnly, eventType, triggered );

			
			// If this event is fired, but yet there are no exploded blocks, then do not set 
			// forceIfAirBlock to true so this event is skipped.
			if ( e.getExplodedBlocks() != null && e.getExplodedBlocks().size() > 0 ) {
				
				pmEvent.setUnprocessedRawBlocks( e.getExplodedBlocks() );
				pmEvent.setForceIfAirBlock( e.isForceIfAirBlock() );
			}
			
			
			
			// Warning: toolInHand really needs to be defined in the event if the source is a
			//          Mine Bomb, otherwise auto features will detect the player is holding 
			//          a mine bomb which is not a pickaxe so the drops will be ZERO.  If they
			//          used their last mine bomb, then auto features will detect only AIR 
			//          in their hand.
			if ( e.getToolInHand() != null ) {
				pmEvent.setItemInHand( (SpigotItemStack) e.getToolInHand() );
			}
			
			if ( !validateEvent( pmEvent, debugInfo ) ) {
				
				// The event has not passed validation. All logging and Errors have been recorded
				// so do nothing more. This is to just prevent normal processing from occurring.
				
				if ( pmEvent.isCancelOriginalEvent() ) {
					
					e.setCancelled( true );
				}
			}
			
			
    		else if ( pmEvent.isMonitor() ) {
    			// Stop here, and prevent additional processing. Monitors should never process the event beyond this.
    		}
    		

			
			// now process all blocks (non-monitor):
			else if ( isPPrisonExplosiveBlockBreakEnabled && 
					( pmEvent.getMine() != null || pmEvent.getMine() == null && 
					!isBoolean( AutoFeatures.pickupLimitToMines )) ) {
				
				if ( pmEvent.getExplodedBlocks().size() > 0 ) {
					
//					String triggered = null;
					
					
//	    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( dummyBlock.getWrapper(), e.getPlayer(),
//	    												mine, dummyBlock, explodedBlocks, BlockEventType.PEExplosive, triggered );
					Bukkit.getServer().getPluginManager().callEvent(pmEvent);
					if ( pmEvent.isCancelled() ) {
						debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
					}
					else {
						
						if ( doAction( pmEvent, debugInfo ) ) {
							
							if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
								
								e.setCancelled( true );
							}
							else {
								
								debugInfo.append( "(event was not canceled) " );
							}
							
							finalizeBreakTheBlocks( pmEvent );
	                		
	                		doBlockEvents( pmEvent );

						}
						
						else {
							
							debugInfo.append( "(doAction failed without details) " );
						}
						
					}
				}
				
				
				debugInfo.append( "(normal processing) " );
			}
			else {
				
				debugInfo.append( "(logic bypass) " );
			}
			
		}
		
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
		
	}
	

	
	public void doActionMonitor( SpigotBlock block, Mine mine ) {
		if ( mine != null ) {
			
			// Good chance the block was already counted, but just in case it wasn't:
			MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( block );
			
			// Record the block break:
			if ( mine.incrementBlockMiningCount( targetBlock ) ) {
				
				// submit a mine sweeper task.  It will only run if it is enabled and another 
				// mine sweeper task has not been submitted.
				mine.submitMineSweeperTask();
				
				// Checks to see if the mine ran out of blocks, and if it did, then
				// it will reset the mine:
				mine.checkZeroBlockReset();
			}

		}
	}
	
	public void doActionBlockEventOnly( SpigotBlock spigotBlock, Mine mine, Player player, 
													BlockEventType blockEventType, String triggered ) {
		if ( mine != null && spigotBlock != null ) {
			
			MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( spigotBlock );
			
			if ( targetBlock != null && targetBlock.getPrisonBlock() != null && !targetBlock.isCounted() ) {
				
				
//				String targetBlockName =  mine == null ? 
//						spigotBlock.getPrisonBlock().getBlockName()
//						: targetBlock.getPrisonBlock().getBlockName();
						
				// Process mine block break events:
				
				if ( mine.incrementBlockMiningCount( targetBlock ) ) {
					
					SpigotPlayer sPlayer = new SpigotPlayer( player );
					PlayerCache.getInstance().addPlayerBlocks( sPlayer, mine.getName(), targetBlock.getPrisonBlock(), 1 );
					
					
					PrisonBlock prisonBlock = spigotBlock.getPrisonBlock();
					mine.processBlockBreakEventCommands( prisonBlock, targetBlock, sPlayer, blockEventType, triggered );
				}

				
			}
		}
	}
	
	
//	public boolean doActionX( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo ) {
//		boolean cancel = false;
//		debugInfo.append( "(doAction: starting EventCore) " );
//		
//		
//		AutoManagerFeatures aMan = SpigotPrison.getInstance().getAutoFeatures();
//		
//		
//		// Do not have to check if auto manager is enabled because it isn't if it's calling this function:
////			boolean isAutoManagerEnabled = aMan.isBoolean( AutoFeatures.isAutoManagerEnabled );
//		boolean isProcessNormalDropsEnabled = isBoolean( AutoFeatures.handleNormalDropsEvents );
//		
//		int drop = 1;
//		
//		if ( isProcessNormalDropsEnabled ) {
//			
//			debugInfo.append( "(doAction calculateNormalDrop) " );
//			
//			// Drop the contents of the individual block breaks
//			drop = aMan.calculateNormalDrop( pmEvent );
//			
//		}
//
//		if ( drop > 0 ) {
//			debugInfo.append( "(doAction processBlockBreakage) " );
//			
//			aMan.processBlockBreakage( pmEvent, drop, true, debugInfo );
//			
//			cancel = true;
//			
//			aMan.autosellPerBlockBreak( pmEvent.getPlayer() );
//		}
//		
//		if ( pmEvent.getMine() != null ) {
//			aMan.checkZeroBlockReset( pmEvent.getMine() );
//		}
//		
//		return cancel;
//	}
	
	
	/**
	 * <p>This function is processed when auto manager is disabled and process token enchant explosions
	 * is enabled.  This function is overridden in AutoManager when auto manager is enabled.
	 * </p>
	 * 
	 * 
	 * @param mine
	 * @param e
	 * @param teExplosiveBlocks
	 */
	public boolean doAction( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo ) {
		
		AutoManagerFeatures aMan = SpigotPrison.getInstance().getAutoFeatures();
		int totalDrops = aMan.calculateNormalDrop( pmEvent, debugInfo );
		
		debugInfo.append( "(normalDrops totalDrops: " + totalDrops + ") ");
		
		return applyDropsBlockBreakage( pmEvent, totalDrops, debugInfo );
	}
	
	
	public boolean applyDropsBlockBreakage( PrisonMinesBlockBreakEvent pmEvent, int totalDrops, StringBuilder debugInfo ) {
		boolean success = false;
	
		// The explodedBlocks list have already been validated as being within the mine:
		debugInfo.append( "(applyDropsBlockBreakage multi-blocks: " + pmEvent.getTargetExplodedBlocks().size() + ") ");
		

		// Process the blockBreakage which actually breaks the block, calculates and gives the player xp, 
		// calculates the durability, applies food exhaustion:
		processBlockBreakage( pmEvent, debugInfo );


//		autosellPerBlockBreak( pmEvent.getPlayer() );
		
//		if ( pmEvent.getMine() != null ) {
//			checkZeroBlockReset( pmEvent.getMine() );
//		}
		
		if ( totalDrops > 0 ) {
			success = true;
		}
		
		return success;
	}
	

	
//	/**
//	 * <p>This function is processed when auto manager is disabled and process crazy enchant explosions
//	 * is enabled.  This function is overridden in AutoManager when auto manager is enabled.
//	 * </p>
//	 * 
//	 * 
//	 * @param mine
//	 * @param e
//	 * @param teExplosiveBlocks
//	 */
//	public void doAction( Mine mine, BlastUseEvent e, List<SpigotBlock> explodedBlocks ) {
//	
//		if ( mine == null || mine != null && !e.isCancelled() ) {
//			
//			int totalCount = 0;
//			
//			
//			SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( e.getPlayer() );
//			
//			AutoManagerFeatures aMan = SpigotPrison.getInstance().getAutoFeatures();
//			
//			// Do not have to check if auto manager is enabled because it isn't if it's calling this function:
////			boolean isAutoManagerEnabled = aMan.isBoolean( AutoFeatures.isAutoManagerEnabled );
//			boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
//			
//			
//			if ( isCEBlockExplodeEnabled ) {
//				
////				StringBuilder sb = new StringBuilder();
////				for ( SpigotBlock spigotBlock : explodedBlocks )
////				{
////					sb.append( spigotBlock.toString() ).append( " " );
////				}
//				
////				Output.get().logInfo( "#### OnBlockBreakEventListener.doAction: BlastUseEvent: :: " + mine.getName() + "  e.blocks= " + 
////						e.getBlockList().size() + "  blockSize : " + explodedBlocks.size() + 
////						"  blocks remaining= " + 
////						mine.getRemainingBlockCount() + " [" + sb.toString() + "]"
////						);
//				
//				// The CrazyEnchants block list have already been validated as being within the mine:
//				for ( SpigotBlock spigotBlock : explodedBlocks ) {
//					
//					// Drop the contents of the individual block breaks
//					int drop = aMan.calculateNormalDrop( itemInHand, spigotBlock );
//					totalCount += drop;
//					
//					if ( drop > 0 ) {
//						
//						aMan.processBlockBreakage( spigotBlock, mine, e.getPlayer(), drop, BlockEventType.CEXplosion, null,
//												itemInHand );
//						
//					}
//				}
//				
//				if ( mine != null ) {
//					aMan.checkZeroBlockReset( mine );
//				}
//				
//				if ( totalCount > 0 ) {
//					
//					// Set the broken block to AIR and cancel the event
//					e.setCancelled(true);
//					
//				}
//				
//			}
//			
//		}
//	}
	
//	private void doActionMonitor( Mine mine, BlastUseEvent e, List<SpigotBlock> explodedBlocks ) {
//		if ( mine != null ) {
//			
//			// Checks to see if the mine ran out of blocks, and if it did, then
//			// it will reset the mine:
//			mine.checkZeroBlockReset();
//		}
//	}
	

	
	public void processBlockBreakage( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo )
	{
		
		// If this block is not in the mine (if null) and it has not been broke before
		// and wasn't originally air, then process the breakage:
		
		if ( pmEvent.getMine() != null ) {
		

			
			// Calculate XP for all blocks if enabled:
			int totalXp = xpCalculateXP( pmEvent, debugInfo );
			xpGivePlayerXp( pmEvent.getSpigotPlayer(), totalXp, debugInfo );

			
			int blocksMined = (pmEvent.getTargetBlock() == null ? 0 : 1 ) + pmEvent.getTargetExplodedBlocks().size();
			
			// A block was broke... so record that event on the tool:	
			itemLoreCounter( pmEvent.getItemInHand(), getMessage( AutoFeatures.loreBlockBreakCountName ), blocksMined );
			
			
			// calculate durability impact: Include item durability resistance.
			if ( isBoolean( AutoFeatures.isCalculateDurabilityEnabled ) && 
					pmEvent.isCalculateDurability() ) {
				
				// value of 0 = normal durability. Value 100 = never calculate durability.
				int durabilityResistance = 0;
				if ( isBoolean( AutoFeatures.loreDurabiltyResistance ) ) {
					durabilityResistance = getDurabilityResistance( pmEvent.getItemInHand(),
							getMessage( AutoFeatures.loreDurabiltyResistanceName ) );
				}
				
				calculateAndApplyDurability( pmEvent.getPlayer(), pmEvent.getItemInHand(), 
															blocksMined, durabilityResistance, debugInfo );
			}
			
			
			
			
			// Only calculate once, no matter how many blocks are included in the explosion blocks:
			if ( isBoolean( AutoFeatures.isCalculateFoodExhustion ) ) {
				pmEvent.getSpigotPlayer().incrementFoodExhaustionBlockBreak();
			}
			
			
//			if ( pmEvent.getMine() != null ) {
//				// Record the block break:
//				
//				// apply to ALL blocks including exploded:
//				applyBlockFinalizations( pmEvent, pmEvent.getTargetBlock() );
//
//				
//				for ( MineTargetPrisonBlock teBlock : pmEvent.getTargetExplodedBlocks() ) {
//					
//					applyBlockFinalizations( pmEvent, teBlock );
//				}
//				
//				checkZeroBlockReset( pmEvent.getMine() );
//			}
			
		}
	}
	
	
	
	// Warning: The following is now obsolete since there is now a sellall function that will sell on a 
	//          per SpigotItemStack so it eliminates a ton of overhead.  It also supports thousands of 
	//          items per stack.
//	public boolean autosellPerBlockBreak( Player player ) {
//		boolean enabled = false;
//		
//		
////		if (isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled) || 
////				pmEvent.isForceAutoSell() ) {
////			
////			SellAllUtil.get().sellAllSell( player, itemStack, true, false, false );
////		}
//
//		
//		// This won't try to sell on every item stack, but assuming that sellall will hit on very block 
//		// break, then the odds of inventory being overflowed on one explosion would be more rare than anything
//		if ( isBoolean( AutoFeatures.isAutoSellPerBlockBreakEnabled ) ) {
//			
//			enabled = true;
//			
//			// Run sell all
//			if ( isBoolean( AutoFeatures.isAutoSellPerBlockBreakInlinedEnabled ) ) {
//				// run sellall inline with the block break event:
//				if (PrisonSpigotSellAllCommands.get() != null) {
//					PrisonSpigotSellAllCommands.get().sellAllSellWithDelayCommand(new SpigotPlayer(player));
//				}
//			}
//			else {
//				// Submit sellall to run in the future (0 ticks in the future):
//				String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall sell silent" );
//				Bukkit.dispatchCommand(player, registeredCmd);
//			}
//		}
//		
//		return enabled;
//	}
	
//	
//	public void checkZeroBlockReset( Mine mine ) {
//		if ( mine != null ) {
//			
//			// submit a mine sweeper task.  It will only run if it is enabled and another 
//			// mine sweeper task has not been submitted.
//			mine.submitMineSweeperTask();
//			
//			// Checks to see if the mine ran out of blocks, and if it did, then
//			// it will reset the mine:
//			mine.checkZeroBlockReset();
//		}
//	}
	
	private void applyBlockFinalizations( PrisonMinesBlockBreakEvent pmEvent, MineTargetPrisonBlock targetBlock ) {

		if ( targetBlock != null ) {
			
			Mine mine = pmEvent.getMine();

			// Increment the block break counts if they have not been processed before.
			// Since the function return true if it can count the block, then we can 
			// then have the player counts be incremented.
			if ( mine.incrementBlockMiningCount( targetBlock ) ) {
				
				// Now in AutoManagerFeatures.autoPickup and calculateNormalDrop:
//				PlayerCache.getInstance().addPlayerBlocks( pmEvent.getSpigotPlayer(), 
//						mine.getName(), targetBlock.getPrisonBlock(), 1 );
				
			}
			
			
			SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();
			PrisonBlock pBlock = sBlock == null ? null : sBlock.getPrisonBlock();
			
			mine.processBlockBreakEventCommands( pBlock,
					targetBlock, pmEvent.getSpigotPlayer(), pmEvent.getBlockEventType(), pmEvent.getTriggered() );
			
		}
	}
	
	protected int xpCalculateXP( PrisonMinesBlockBreakEvent pmEvent, StringBuilder debugInfo ) {
		int xp = 0;
		
		if (isBoolean(AutoFeatures.isCalculateXPEnabled) ) {
			
			int totalXp = 0;
			int totalBonusXp = 0;
			int totalBlocks = 0;
			
			SpigotPlayer player = pmEvent.getSpigotPlayer();
			ItemStack itemInHand = pmEvent.getItemInHand() == null ? null : pmEvent.getItemInHand().getBukkitStack();

			// Calculate the XP for the primary block:
			MineTargetPrisonBlock targetBlock = pmEvent.getTargetBlock();
			totalXp = xpCalculateBlockXP( targetBlock, player );
			totalBlocks += (totalXp == 0 ? 0 : 1 );
			
			// Calculate the XP for any exploded block:
			for ( MineTargetPrisonBlock targetExplodedBlock : pmEvent.getTargetExplodedBlocks() ) {
				
				totalXp += xpCalculateBlockXP( targetExplodedBlock, player );
				totalBonusXp += xpCalculateBonusXP( targetExplodedBlock, player, itemInHand );
				
				totalBlocks++;
			}
			
			xp = totalXp + totalBonusXp;
			
			if ( totalXp > 0 || totalBonusXp > 0 ) {
				
				String message = String.format( "(XP calcs:  blocks: %d  xp: %d  bonusXp: %d) ",
						totalBlocks, totalXp, totalBonusXp );
				
				debugInfo.append( message );

			}
		}
		
		
		return xp;
	}

	private int xpCalculateBlockXP( MineTargetPrisonBlock targetBlock, SpigotPlayer player )
	{
		int xp = 0;
		
		if ( targetBlock != null && targetBlock.getMinedBlock() != null ) {
			
			SpigotBlock sBlock = ((SpigotBlock) targetBlock.getMinedBlock());
			
			xp += calculateXP( sBlock.getPrisonBlock().getBlockName() );
			
		}
		return xp;
	}
	private int xpCalculateBonusXP( MineTargetPrisonBlock targetBlock, SpigotPlayer player, ItemStack itemInHand )
	{
		int xp = 0;
		
		if ( targetBlock != null && targetBlock.getMinedBlock() != null ) {
			
			SpigotBlock sBlock = ((SpigotBlock) targetBlock.getMinedBlock());
			
			// Bonus XP:
			xp = checkCrazyEnchant( player.getWrapper(), sBlock.getWrapper(), itemInHand );
			
		}
		return xp;
	}
	
	
	/** 
	 * <p>Gives XP to the player, either as Orbs or directly.
	 * </p>
	 * 
	 * @param player
	 * @param totalXp
	 * @param debugInfo
	 */
	protected void xpGivePlayerXp(SpigotPlayer player, int totalXp, StringBuilder debugInfo ) {
		
		if ( totalXp > 0 ) {
			
			boolean giveXpOrbs = isBoolean( AutoFeatures.givePlayerXPAsOrbDrops );
			
			if ( giveXpOrbs ) {
				
				player.dropXPOrbs( totalXp );
//						tech.mcprison.prison.util.Location dropPoint = player.getLocation().add( player.getLocation().getDirection());
//						((ExperienceOrb) player.getWorld().spawn(dropPoint, ExperienceOrb.class)).setExperience(xp);
			}
			else {
				player.giveExp( totalXp );
			}
			
			debugInfo.append( "(xp " + totalXp + ( giveXpOrbs ? "Orbs" : "direct") + ") " );
		}
		
	}
	
	

	
	/**
	 * <p>This calculate xp based upon the block that is broken.
	 * Fortune does not increase XP that a block drops.
	 * </p>
	 *
	 * <ul>
	 *   <li>Coal Ore: 0 - 2</li>
	 *   <li>Nether Gold Ore: 0 - 1</li>
	 *   <li>Diamond Ore, Emerald Ore: 3 - 7</li>
	 *   <li>Lapis Luzuli Ore, Nether Quartz Ore: 2 - 5</li>
	 *   <li>Redstone Ore: 1 - 5</li>
	 *   <li>Monster Spawner: 15 - 43</li>
	 * </ul>
	 *
	 * @param Block
	 * @return
	 */
	private int calculateXP( String blockName ) {
		int xp = 0;

		switch (blockName.toLowerCase()) {

			case "gold_ore":
			case "nether_gold_ore":
			case "deepslate_gold_ore":
			case "raw_gold":
				
			case "iron_ore":
			case "deepslate_iron_ore":
			case "raw_iron":
				
			case "copper_ore": 
			case "deepslate_copper_ore": 
			case "raw_copper": 	
				xp = getRandom().nextInt( 1 );
				break;
			
			case "coal_ore":
			case "deepslate_coal_ore":
			case "coal":
				xp = getRandom().nextInt( 2 );
				break;


			case "diamond_ore":
			case "deepslate_diamond_ore":
			case "emerald_ore":
			case "deepslate_emerald_ore":
				xp = getRandom().nextInt( 4 ) + 3;
				break;

			case "lapis_ore":
			case "nether_quartz_ore":
			case "deepslate_lapis_ore":
				xp = getRandom().nextInt( 3 ) + 2;
				break;

			case "redstone_ore":
			case "deepslate_redstone_ore":
				xp = getRandom().nextInt( 4 ) + 1;
				break;

			case "spawn":
				xp = getRandom().nextInt( 28 ) + 15;
				break;

			default:
				break;
		}

		return xp;
	}

	/**
	 * <p>This function will search for the loreDurabiltyResistanceName within the
	 * item in the hand, if found it will return the number if it exists.  If not
	 * found, then it will return a value of zero, indicating that no special resistance
	 * exists, and that durability should be applied as normal.
	 * </p>
	 *
	 * <p>If there is no value after the lore name, then the default is 100 %.
	 * If a value follows the lore name, then it must be an integer.
	 * If it is less than 0, then 0. If it is greater than 100, then 100.
	 * </p>
	 *
	 * @param itemInHand
	 * @param itemLore
	 * @return
	 */
	protected int getDurabilityResistance(SpigotItemStack itemInHand, String itemLore) {
		int results = 0;

		if ( itemInHand != null && itemInHand.getBukkitStack().hasItemMeta() ) {

			List<String> lore = new ArrayList<>();

			itemLore = itemLore.trim() + " ";

			itemLore = Text.translateAmpColorCodes( itemLore.trim() + " ");

//			String prisonBlockBroken = itemLore.getLore();

			ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();

			if (meta.hasLore()) {
				lore = meta.getLore();

				for (String s : lore) {
					if (s.startsWith(itemLore)) {

						// It has the durability resistance lore, so set the results to 100.
						// If a value is set, then it will be replaced.
						results = 100;

						String val = s.replace(itemLore, "").trim();

						try {
							results += Integer.parseInt(val);
						} catch (NumberFormatException e1) {
							Output.get().logError("AutoManager: tool durability failure. lore= [" + s + "] val= [" + val + "] error: " + e1.getMessage());
						}

						break;
					}
				}
			}

			if ( results > 100d ) {
				results = 100;
			}
			else if ( results < 0 ) {
				results = 0;
			}

		}

		return results;
	}

	/**
	 * <p>This function will check to see the feature 
	 * <pre>isDisableToolWhenWornOutPreventBreakage</pre> is enabled, and if so,
	 * then it this will return a value of true if the tool in the hand of the player
	 * is has a maxDurability greater than zero and the current durability level is
	 * equal to or greater than the maxDurability.
	 * </p>
	 * 
	 * <p>This function actually does not apply to just tools, but could apply to anything
	 * that may be used for breaking a block and has durability.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	private boolean isToolDisabled( Player player ) {
		boolean results = false;

		if ( isBoolean( AutoFeatures.isPreventToolBreakage ) ) {
			
			SpigotItemStack itemInHand =
					SpigotCompatibility.getInstance().getPrisonItemInMainHand( player );
			
			if ( itemInHand != null && !itemInHand.isAir() ) {
				int breakageThreshold = getInteger( AutoFeatures.preventToolBreakageThreshold );
				
				Compatibility compat = SpigotCompatibility.getInstance();
				int maxDurability = compat.getDurabilityMax( itemInHand );
				int durability = compat.getDurability( itemInHand );
				
				results = ( maxDurability > 0 && 
						(durability + breakageThreshold) >= maxDurability );
			}
		}
			
		return results;
	}

	/**
	 * <p>This is calculated only once per block break event, no matter how many blocks
	 * are involved.
	 * </p>
	 * 
	 * <p>This should calculate and apply the durability consumption on the tool.
	 * </p>
	 * 
	 * <p>This only applies to to the use of tools. If the player's hand is empty (AIR),
	 * or the item in hand has no max durability, then it's not a tool so it skips 
	 * this processing.
	 * </p>
	 *
	 * <p>The damage is calculated as a value of one durability, but all damage can be
	 * skipped if the tool has a durability enchantment.  If it does, then there is a
	 * percent chance of 1 in (1 + durabilityLevel).  So if a tool has a durability level
	 * of 1, then there is a 50% chance. Level of 2, then a 66.6% chance. Level of 3 has
	 * a 75% chance. And a level of 9 has a 90% chance. There are no upper limits on
	 * durability enchantment levels.
	 * </p>
	 *
	 * <p>Some blocks may have a damage of greater than 1, but for now, this
	 * does not take that in to consideration. If hooking up in the future, just
	 * set the initial damage to the correct value based upon block type that was mined.
	 * </p>
	 *
	 * <p>The parameter durabilityResistance is optional, to disable use a value of ZERO.
	 * This is a percentage and is calculated first.  If random value is equal to the parameter
	 * or less, then it will skip the durability calculations for the current event.
	 * </p>
	 *
	 * <p>Based upon the following URL.  See Tool Durability.
	 * </p>
	 * https://minecraft.gamepedia.com/Item_durability
	 *
	 * @param player
	 * @param itemInHand - Should be the tool they used to mine or dig the block
	 * @param durabilityResistance - Chance to prevent durability wear being applied.
	 * 			Zero always disables this calculation and allows normal durability calculations
	 * 			to be performed. 100 always prevents wear.
	 */
	protected void calculateAndApplyDurability(Player player, SpigotItemStack itemInHand, int blocksMined, 
						int durabilityResistance, StringBuilder debugInfo ) {

		// If no tool, or durabilityResistance is less than 100:
		if ( itemInHand != null && !itemInHand.isAir() && durabilityResistance < 100 ) {
			
			Compatibility compat = SpigotCompatibility.getInstance();
			int maxDurability = compat.getDurabilityMax( itemInHand );
			int durability = compat.getDurability( itemInHand );
			
			short totalDamage = 1;  // Generally 1 unless instant break block then zero.
			
			int durabilityLevel = 0;
			boolean toolBreak = false;
			
			// Need to skip processing on empty item stacks and items that have no max durability
			if ( maxDurability > 0 ) {
				short damage = 1;
				
				if ( itemInHand.getBukkitStack().containsEnchantment( Enchantment.DURABILITY)) {
					durabilityLevel = itemInHand.getBukkitStack().getEnchantmentLevel( Enchantment.DURABILITY );
				}
				
				for ( int y = 0; y < blocksMined; y++ ) {
					if ( durabilityResistance >= 100 ) {
						damage = 0;
					} 
					else if ( durabilityResistance > 0 ) {
						if ( getRandom().nextInt( 100 ) <= durabilityResistance ) {
							damage = 0;
						}
					}
					
					if ( damage > 0 && durabilityLevel > 0 ) {
						
						// the chance of losing durability is 1 in (1+level)
						// So if the random int == 0, then take damage, otherwise none.
						if (getRandom().nextInt( 1 + durabilityLevel ) > 0) {
							damage = 0;
						}
					}
					
					totalDamage += damage;
				}
				
				int newDurability = durability + totalDamage;
				int remainingDurability = maxDurability - newDurability;

				if (totalDamage > 0 && 
						(!isBoolean( AutoFeatures.isPreventToolBreakage ) || 
						  isBoolean( AutoFeatures.isPreventToolBreakage ) && 
						  remainingDurability >= getInteger( AutoFeatures.preventToolBreakageThreshold ) ) ) {
					
//				Compatibility compat = SpigotPrison.getInstance().getCompatibility();
//				int maxDurability = compat.getDurabilityMax( itemInHand );
//				int durability = compat.getDurability( itemInHand );
					
					if (newDurability > maxDurability) {
						// Item breaks! ;(
						compat.breakItemInMainHand( player );
						itemInHand = null;
						toolBreak = true;
					} else {
						compat.setDurability( itemInHand, newDurability );
					}
					player.updateInventory();
				}
			}
			
			String message = String.format( "(calcDurability: %s:  maxDurability= %d  " + 
					"durability: %d  damage: %d  durResistance: %d  toolDurabilityLvl: %d  %s) ", 
					(itemInHand == null ? "(empty hand)" : itemInHand.getName() ), 
					maxDurability, durability, totalDamage, 
					durabilityResistance, durabilityLevel, 
					(toolBreak ? "[Broke]" : "") );
			debugInfo.append( message );
			
		}
	}



	/**
	 * <p>This adds a lore counter to the tool if it is enabled.
	 * </p>
	 * 
	 * @param itemInHand
	 * @param itemLore
	 * @param blocks
	 */
	protected void itemLoreCounter( SpigotItemStack itemInHand, String itemLore, int blocks) {

		// A block was broke... so record that event on the tool:	
		if ( itemInHand != null && isBoolean( AutoFeatures.loreTrackBlockBreakCount ) ) {
			
			if (itemInHand.getBukkitStack().hasItemMeta()) {
				
				List<String> lore = new ArrayList<>();
				itemLore = itemLore.trim() + " ";
				itemLore = Text.translateAmpColorCodes( itemLore.trim() + " ");
				ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();
				
//			String prisonBlockBroken = itemLore.getLore();
				
				
				if (meta.hasLore()) {
					lore = meta.getLore();
					boolean found = false;
					
					for( int i = 0; i < lore.size(); i++ ) {
						if ( lore.get( i ).startsWith( itemLore ) ) {
							String val = lore.get( i ).replace( itemLore, "" ).trim();
							int count = blocks;
							
							try {
								count += Integer.parseInt(val);
							} catch (NumberFormatException e1) {
								Output.get().logError("AutoManager: tool counter failure. lore= [" + lore.get(i) + "] val= [" + val + "] error: " + e1.getMessage());								}
							
							lore.set(i, itemLore + count);
							found = true;
							
							break;
						}
					}
					
					if ( !found ) {
						lore.add(itemLore + 1);
					}
					
				} else {
					lore.add(itemLore + 1);
				}
				
				meta.setLore(lore);
				itemInHand.getBukkitStack().setItemMeta(meta);
				
				// incrementCounterInName( itemInHand, meta );
				
			}
		}
		
	}

//
//
//	/**
//	 * <p>Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered
//	 * function that will do it's processing before prison will process the blocks.
//	 * </p>
//	 * 
//	 * <p>This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.
//	 * </p>
//	 * 
//	 * @param e
//	 */
//	private void registerMCMMO() {
//		
//		if ( !isMCMMOChecked ) {
//			
//	    	boolean isProcessMcMMOBlockBreakEvents = isBoolean( AutoFeatures.isProcessMcMMOBlockBreakEvents );
//
//			if ( isProcessMcMMOBlockBreakEvents ) {
//				
//				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
//					
//					if ( rListener.getPlugin().isEnabled() && 
//							rListener.getPlugin().getName().equalsIgnoreCase( "mcMMO" ) ) {
//						
//						registeredListenerMCMMO = rListener;
//					}
//				}
//				
//			}
//			
//			isMCMMOChecked = true;
//		}
//	}
//	
//	private void checkMCMMO( Player player, Block block ) {
//		if ( registeredListenerMCMMO != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkMCMMO( bEvent );
//		}
//	}
//	
//	private void checkMCMMO( BlockBreakEvent e ) {
//		if ( registeredListenerMCMMO != null ) {
//			
//			try {
//				registeredListenerMCMMO.callEvent( e );
//			}
//			catch ( EventException e1 ) {
//				e1.printStackTrace();
//			}
//		}
//	}
//	
//	
//	
//	/**
//	 * <p>Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered
//	 * function that will do it's processing before prison will process the blocks.
//	 * </p>
//	 * 
//	 * <p>This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.
//	 * </p>
//	 * 
//	 * @param e
//	 */
//	private void registerEZBlock() {
//		
//		if ( !isEZBlockChecked ) {
//			
//	    	boolean isProcessMcMMOBlockBreakEvents = isBoolean( AutoFeatures.isProcessEZBlocksBlockBreakEvents );
//
//			if ( isProcessMcMMOBlockBreakEvents ) {
//				
//				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
//					
//					if ( rListener.getPlugin().isEnabled() && 
//							rListener.getPlugin().getName().equalsIgnoreCase( "EZBlocks" ) ) {
//						
//						registeredListenerEZBlock = rListener;
//					}
//				}
//				
//			}
//			
//			isEZBlockChecked = true;
//		}
//	}
//	
//	private void checkEZBlock( Player player, Block block ) {
//		if ( registeredListenerEZBlock != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkEZBlock( bEvent );
//		}
//	}
//	
//	private void checkEZBlock( BlockBreakEvent e ) {
//		if ( registeredListenerEZBlock != null ) {
//			
//			try {
//				registeredListenerEZBlock.callEvent( e );
//			}
//			catch ( EventException e1 ) {
//				e1.printStackTrace();
//			}
//		}
//	}
//	

	
	
	private int checkCrazyEnchant( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		try {
			if ( isCrazyEnchantEnabled() == null ) {
				Class.forName( 
						"tech.mcprison.prison.spigot.integrations.IntegrationCrazyEnchantmentsPickaxes", false, 
						this.getClass().getClassLoader() );
				setCrazyEnchantEnabled( Boolean.TRUE );
			}
			
			if ( isCrazyEnchantEnabled() != null && isCrazyEnchantEnabled().booleanValue() && 
					item != null && IntegrationCrazyEnchantmentsPickaxes.getInstance().isEnabled() ) {
				
				bonusXp = IntegrationCrazyEnchantmentsPickaxes.getInstance()
						.getPickaxeEnchantmentExperienceBonus( player, block, item );
			}
		}
		catch ( NoClassDefFoundError | Exception e ) {
			setCrazyEnchantEnabled( Boolean.FALSE );
		}
		
		return bonusXp;
	}
	




	
	@SuppressWarnings( "unused" )
	private synchronized String incrementUses(Long elapsedNano) {
		String message = null;
		usesElapsedTimeNano += elapsedNano;
		
		if ( ++uses >= 100 ) {
			double avgNano = usesElapsedTimeNano / uses;
			double avgMs = avgNano / 1000000;
			message = String.format( "OnBlockBreak: count= %s avgNano= %s avgMs= %s ", 
					Integer.toString(uses), Double.toString(avgNano), Double.toString(avgMs) );
			
			uses = 0;
			usesElapsedTimeNano = 0L;
		}
		return message;
	}

	private boolean isTeExplosionTriggerEnabled() {
		return teExplosionTriggerEnabled;
	}

	private void setTeExplosionTriggerEnabled( boolean teExplosionTriggerEnabled ) {
		this.teExplosionTriggerEnabled = teExplosionTriggerEnabled;
	}

	public Random getRandom() {
		return random;
	}

	public Boolean isCrazyEnchantEnabled() {
		return crazyEnchantEnabled;
	}
	public void setCrazyEnchantEnabled( Boolean crazyEnchantEnabled ) {
		this.crazyEnchantEnabled = crazyEnchantEnabled;
	}


}

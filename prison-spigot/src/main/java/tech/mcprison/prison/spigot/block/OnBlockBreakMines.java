package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.game.SpigotLocation;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.tasks.PrisonCommandTaskData;
import tech.mcprison.prison.tasks.PrisonCommandTaskData.TaskMode;
import tech.mcprison.prison.tasks.PrisonCommandTasks;

public class OnBlockBreakMines
	extends OnBlockBreakEventCoreMessages
{
	private PrisonMines prisonMineManager;
	private boolean mineModuleDisabled = false;

	enum EventResultsReasons {
		result_reason_not_yet_set,
		results_passed,
		cancel_event__block_is_null,
		cancel_event__block_is_locked, 
		ignore_event__block_is_not_in_a_mine, 
		cancel_event__mine_mutex__mine_resetting, 
		ignore_event__target_block_ignore_all_events, 
		ignore_event__block_already_counted, 
		cancel_event__block_already_counted, 
		ignore_event__monitor_priority_but_not_AIR, 
		cancel_event__player_has_no_access, 
		cancel_event__block_is_not_mappable_to_target_block, 
		
		results_passed__access_priority__player_has_access,
		cancel_event__access_priority__block_is_not_in_a_mine,
		cancel_event__access_priority__player_has_no_access
		;
		
	}
	
	public OnBlockBreakMines() {
		super();
		
		this.prisonMineManager = null;
		
	}
	
	public class MinesEventResults {
		
		private EventResultsReasons resultsReason = EventResultsReasons.result_reason_not_yet_set;
		
		private boolean cancelEvent = false;
		private boolean ignoreEvent = false;
		
		private String eventName;
		
		private BlockBreakPriority bbPriority;
		private Mine mine = null;
		private SpigotPlayer sPlayer;
		
		private Block block;
		private SpigotBlock spigotBlock;
		
		
		public MinesEventResults( BlockBreakPriority bbPriority, SpigotPlayer sPlayer, Block block ) {
			super();
			
			this.bbPriority = bbPriority;
			this.sPlayer = sPlayer;
			this.block = block;
		}
		
		public void logDebugInfo() {
			if ( (isIgnoreEvent() || isCancelEvent()) && 
					Output.get().isDebug() ) {
				
				String eventName = getEventName() != null && getEventName().trim().length() > 0 ?
						"[event: " + getEventName() + "] " : "";
						
				String blockName = getSpigotBlock() == null ? 
						"noPrisonBlock" : 
						getSpigotBlock().getBlockName();
				String blockLocation = getSpigotBlock() == null ? 
						"" : 
						getSpigotBlock().getLocation().toWorldCoordinates();
				
				Output.get().logInfo( "Prison AutoFeatures Fast-Fail: %s%s %s %s %s%s",
						eventName,
						getResultsReason().name(),
						//getBbPriority().name(),
						getSpigotPlayer().getName(),
						
						getMine() == null ? "noMine" : getMine().getName(),
						blockName,
						blockLocation
						);
			}
		}
		

		public String getDebugInfo() {
			
			String blockName = getSpigotBlock() == null ? 
					"noPrisonBlock" : 
						getSpigotBlock().getBlockName();
			String blockLocation = getSpigotBlock() == null ? 
					"" : 
						getSpigotBlock().getLocation().toWorldCoordinates();
			
			return String.format( 
					"  EventInfo: %s %s Mine: %s %s %s ", 
					getResultsReason().name(),
//					getBbPriority().name(),
					getSpigotPlayer().getName(),
					
					getMine() == null ? "noMine" : getMine().getName(),
					blockName,
					blockLocation );
		}
		
		public EventResultsReasons getResultsReason() {
			return resultsReason;
		}
		public void setResultsReason(EventResultsReasons resultsReason) {
			this.resultsReason = resultsReason;
		}
		
		public String getEventName() {
			return eventName;
		}
		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public BlockBreakPriority getBbPriority() {
			return bbPriority;
		}
		public void setBbPriority(BlockBreakPriority bbPriority) {
			this.bbPriority = bbPriority;
		}

		public SpigotPlayer getSpigotPlayer() {
			return sPlayer;
		}
		public void setSpigotPlayer(SpigotPlayer sPlayer) {
			this.sPlayer = sPlayer;
		}

		public Mine getMine() {
			return mine;
		}
		public void setMine(Mine mine) {
			this.mine = mine;
		}

		public boolean isCancelEvent() {
			return cancelEvent;
		}
		public void setCancelEvent( boolean cancelEvent ) {
			this.cancelEvent = cancelEvent;
		}

		public boolean isIgnoreEvent() {
			return ignoreEvent;
		}
		public void setIgnoreEvent( boolean ignoreEvent ) {
			this.ignoreEvent = ignoreEvent;
		}

		public Block getBlock() {
			return block;
		}
		public void setBlock(Block block) {
			this.block = block;
		}

		public SpigotBlock getSpigotBlock() {
			return spigotBlock;
		}
		public void setSpigotBlock(SpigotBlock spigotBlock) {
			this.spigotBlock = spigotBlock;
		}

	}
	
	public Mine findMine( SpigotPlayer player, SpigotBlock sBlock, List<Block> altBlocksSource, PrisonMinesBlockBreakEvent pmEvent )
	{
		return findMine( player.getUniqueId(), sBlock, altBlocksSource, pmEvent );
	}
	
	public Mine findMine( Player player, SpigotBlock sBlock, List<Block> altBlocksSource, PrisonMinesBlockBreakEvent pmEvent )
	{
		return findMine( player.getUniqueId(), sBlock, altBlocksSource, pmEvent );
	}
	
	public Mine findMine( UUID playerUUID, SpigotBlock sBlock, List<Block> altBlocksSource, PrisonMinesBlockBreakEvent pmEvent )
	{
//		Long playerUUIDLSB = Long.valueOf( playerUUID.getLeastSignificantBits() );

		// Get the cached mine, if it exists:
		Mine mine = getPlayerCache().get( playerUUID.toString() );
		
		if ( mine == null || sBlock != null && !mine.isInMineExact( sBlock.getLocation() ) )
		{
			// Look for the correct mine to use.
			// Set mine to null so if cannot find the right one it will return a
			// null:
			mine = findMineLocation( sBlock );

			// Thanks to CrazyEnchant, where they do not identify the block the
			// player breaks, we
			// have to go through all of the unprecessedRawBlocks to see if any
			// are within a mine.
			// If we find a block that's in a mine, then use that block as the
			// primary block.
			if ( mine == null && altBlocksSource != null )
			{

				for ( Block bBlock : altBlocksSource )
				{
					SpigotBlock sBlockAltBlock = SpigotBlock.getSpigotBlock( bBlock );
					mine = findMineLocation( sBlockAltBlock );
					if ( mine != null )
					{

						if ( pmEvent != null )
						{
							pmEvent.setSpigotBlock( sBlockAltBlock );
						}

						break;
					}
				}
			}

			// Store the mine in the player cache if not null:
			if ( mine != null )
			{
				getPlayerCache().put( playerUUID.toString(), mine );
//				getPlayerCache().put( playerUUIDLSB, mine );
			}
		}

		return mine;
	}
	
	
	// NOTE: The following two commented out functions need to be copied and actived in 
	//       in the class that is using these functions for these two events:
	
//	protected boolean ignoreMinesBlockBreakEvent( Cancellable event, Player player, Block block ) {
//		
//		MinesEventResults eventResults = ignoreMinesBlockBreakEvent( player, block );
//		
//		if ( eventResults.isCancelEvent() ) {
//			event.setCancelled( eventResults.isCancelEvent() );
//		}
//		return eventResults.isIgnoreEvent();
//	}
//
//	protected boolean processMinesBlockBreakEvent( PEExplosionEvent event, Player player, Block block ) {
//		
//		MinesEventResults eventResults = ignoreMinesBlockBreakEvent( player, block );
//		
//		if ( eventResults.isCancelEvent() ) {
//			event.setCancelled( eventResults.isCancelEvent() );
//		}
//		return eventResults.isIgnoreEvent();
//	}

	
	public List<Block> removeAllInvalidBlocks( Player player, 
			List<Block> blocks, 
			BlockBreakPriority bbPriority, 
			boolean ignoreBlockReuse ) {

		List<Block> goodBlocks = new ArrayList<>();
		
		if ( blocks.size() == 0 || getPrisonMineManager() == null ) {
			// Mines are not enabled, so exit with request to ignore event:
			return goodBlocks;
		}

		
		
		// searching for mines is expensive because it's has to check each and every block
		// against all possible mines.  
		
		// So first find the "center" of the block list by taking the averages of all blocks's x, y, and z coordinates:
		Location locAvgBukkit = getCenterLocationOfBlocks(blocks, player);
		
		
		// next we need to find the greatest distance from the average:
		double locAvgRadius = getGreatestDistanceFromLocation(blocks, locAvgBukkit);
		
		
		SpigotLocation locAvg = new SpigotLocation( locAvgBukkit );
		
		
		// Next we need to find all mines that are within the max distance of the mine, 
		// to ensure we don't miss the outer corners, take the mine's distance to a corner 
		// and mult by 1.3 which will potentially include mines that are outside of the
		// list of blocks:
		List<Mine> minesShortList = getAllMinesWithinTheRadialDistance( locAvg, locAvgRadius );
		
		
		
		// If no mines are found, then obviously no blocks will be within the mines:
		if ( minesShortList.size() == 0 ) {
			return goodBlocks;
		}
			
		
//		SpigotPlayer sPlayer = new SpigotPlayer( player );
		

		// All blocks must be not be null, and the blocks cannot be identified as an
		// unbreakable block, which is usually part of an explosion event such as a 
		// block decay.
		Mine lastMine = null;
		
		for (Block block : blocks) {
			
			SpigotBlock sBlock = SpigotBlock.getSpigotBlock( block );
			
			if ( block != null && !BlockUtils.getInstance().isUnbreakable( sBlock ) ) {

				// If a block is in the same mine as the 'lastMine', then add it to the
				// goodBlocks list.
				if ( lastMine != null && lastMine.isInMine(sBlock) ) {
					goodBlocks.add(block);
				}
				
				// else if lastMine is null or the block is not in the lastMine, then
				// need to check all mines in the short list to see if the block is in
				// mine.  If it is, then capture it in the goodBlocks list.
				else {
					
					for (Mine mine : minesShortList) {
						if ( mine.isInMineExact(locAvg) ) {
							lastMine = mine;
							
							goodBlocks.add(block);
							break;
						}
					}
				}
				
			}
		}
		
//		// Purge all blocks in the parameter List:
//		blocks.clear();
//		
//		// Add only the goodBlocks back to the parameter's variable:
//		blocks.addAll( goodBlocks );
		
		return goodBlocks;
	}


	/**
	 * <p>This function will take a list of blocks, and calculate the average location, 
	 * which will be the center of the group of blocks.  The group of blocks can be
	 * any shape, and it will still find the center.
	 * </p>
	 * 
	 * <p>The way this function works, is that it adds all x's, all y's, and all z's.
	 * Then divides all these values by the number of blocks that were summed together.
	 * Basically finding the common vector or everything.
	 * </p>
	 * 
	 * @param blocks
	 * @param player
	 * @return
	 */
	private Location getCenterLocationOfBlocks(List<Block> blocks, Player player) {
		long xT = 0;
		long yT = 0;
		long zT = 0;
		int count = 0;
		for (Block block : blocks) {
			xT += block.getX();
			yT += block.getY();
			zT += block.getZ();
			count++;
		}
		
		double xAvg = (double) xT / count;
		double yAvg = (double) yT / count;
		double zAvg = (double) zT / count;
		Location locAvgBukkit = new Location( player.getWorld(), xAvg, yAvg, zAvg );
		return locAvgBukkit;
	}
	
	
	/**
	 * <p>This function, given the a location, which should be the center of all blocks 
	 * (See function `getCenterLocationOfBlocks()`), will return the greatest distance 
	 * of all of the included blocks.  This will be the outer radius of all the blocks, 
	 * based upon the 'centerLocation'.
	 * </p>
	 * 
	 * @param blocks
	 * @param locAvgBukkit
	 * @return
	 */
	private double getGreatestDistanceFromLocation(List<Block> blocks, Location centerLocation ) {
		double locAvgRadius = 0;
		for (Block block : blocks) {
			double d = centerLocation.distance( block.getLocation() );
			if ( d > locAvgRadius ) {
				locAvgRadius = d;
			}
		}
		return locAvgRadius;
	}

	/**
	 * <p>This function, given a location, and the radius of a sphere around that location, find all 
	 * mines that that has at least one block that will fall within the range of this the location's
	 * sphere.  To ensure a mine is not missed by a block or two, the 'maxPossibleDistance' has 
	 * four added to it.  This will help ensure that more mines are included in this list, even if it 
	 * may miss the farthest block by a couple of blocks.  It's far better to be more inclusive, than 
	 * to exclude too many.
	 * </p>
	 * 
	 * <p>This function basically behaves as if we have two points and spheres around each point.  The
	 * spheres would be the radius from each location, to form a sphere for each location. Between the
	 * two spheres, we only need to use in our calculations, the closest point on each sphere, such that
	 * the two chosen points (one of each sphere) are the closest to each other, out of the whole surface
	 * of those two spheres.  Basically, these two points, also happen to be on the line that is formed
	 * from one location to the other location.  And the two points on that line, which represents the 
	 * spheres surface, would be consider intersecting with each other if the distance between the points
	 * is less than zero.  If they don't intersect then the distance will be greater than zero.
	 * </p>
	 * 
	 * <p>So basically, the total distance should be less than the two radiuses added together (plus 4.0)
	 * which indicates there is a probable chance a block may be within that mine.  All mines that 
	 * meet this requirement is returned in a List<Mine>.
	 * </p>
	 * 
	 * @param locAvg
	 * @param locAvgRadius
	 * @return
	 */
	private List<Mine> getAllMinesWithinTheRadialDistance( SpigotLocation location, double locationRadius ) {
		List<Mine> minesShortList = new ArrayList<>();
		for (Mine m : getPrisonMineManager().getMines() ) {
			if ( !m.isVirtual() ) {
				
				double mineRadius = m.getBounds().getRadius();
				double maxPossibleDistance = mineRadius + locationRadius + 4;

				double distanceFromMine = m.getBounds().getDistance3d( location );
				
				
				if ( distanceFromMine <= maxPossibleDistance ) {
					minesShortList.add( m );
				}
			}
		}
		return minesShortList;
	}

	
	
		
	/**
	 * <p> If the event is canceled, it still needs to be processed because of the MONITOR events: 
	 * An event will be "canceled" and "ignored" if the block 
	 * BlockUtils.isUnbreakable(), or if the mine is activly resetting.
	 * The event will also be ignored if the block is outside of a mine
	 * or if the targetBlock has been set to ignore all block events which 
	 * means the block has already been processed.
	 * </p>
	 * 
	 * @param player
	 * @param block
	 * @return
	 */
	protected MinesEventResults ignoreMinesBlockBreakEvent( Player player, 
				Block block, 
				BlockBreakPriority bbPriority,
				boolean ignoreBlockReuse ) {
		
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		MinesEventResults results = new MinesEventResults( bbPriority, sPlayer, block );
		
		SpigotBlock sBlock = SpigotBlock.getSpigotBlock( block );
		results.setSpigotBlock( sBlock );
		
		if ( block == null ) {
			
			results.setResultsReason( EventResultsReasons.cancel_event__block_is_null);
			
			results.setCancelEvent( true );
			results.setIgnoreEvent( true );
		}
		
		else if ( BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
			results.setResultsReason( EventResultsReasons.cancel_event__block_is_locked );
			
			results.setCancelEvent( true );
			results.setIgnoreEvent( true );
		}
		else if ( bbPriority.isAccess() ) {
			
			Mine mine = findMine( player, sBlock,  null, null ); 
			results.setMine( mine );
			
			if ( mine == null ) {
				// Prison is unable to process blocks outside of mines right now, so exit:
				results.setResultsReason( EventResultsReasons
								.cancel_event__access_priority__block_is_not_in_a_mine );
				
				results.setCancelEvent( true );
				results.setIgnoreEvent( true );
			}
			else if ( !mine.hasMiningAccess(sPlayer) ) {
				
				results.setResultsReason( EventResultsReasons
								.cancel_event__access_priority__player_has_no_access );
				results.setIgnoreEvent( true );
				results.setCancelEvent( true );
				
	    		if ( sPlayer != null &&
	    				AutoFeaturesWrapper.getInstance()
	    					.isBoolean( AutoFeatures.eventPriorityACCESSFailureTPToCurrentMine ) ) {
	    			// run the `/mines tp` command for the player which will TP them to a 
	    			// mine they can access:
	    			
					String debugInfo = String.format(
									"ACCESS failed: teleport %s to valid mine.", 
									sPlayer.getName() );
					
					PrisonCommandTaskData cmdTask = new PrisonCommandTaskData( debugInfo, 
									"mines tp", 0 );
					cmdTask.setTaskMode( TaskMode.syncPlayer );

	    			PrisonCommandTasks.submitTasks( sPlayer, cmdTask );
	    			
	    		}

			}
			else {
				results.setResultsReason( EventResultsReasons
								.results_passed__access_priority__player_has_access );
				
				// Not sure why that was here?  This looks like they should have access?]
				results.setIgnoreEvent( true );
			}
		
		}
		else if ( bbPriority.isMonitor() && !sBlock.isEmpty() && 
				AutoFeaturesWrapper.getInstance().isBoolean( 
						AutoFeatures.processMonitorEventsOnlyIfPrimaryBlockIsAIR ) ) {

			results.setResultsReason( EventResultsReasons.ignore_event__monitor_priority_but_not_AIR );
			
			results.setIgnoreEvent( true );
		}
		else {
			
			Mine mine = findMine( player, sBlock,  null, null ); 
			results.setMine( mine );
			
			if ( mine == null ) {
				// Prison is unable to process blocks outside of mines right now, so exit:
				results.setResultsReason( EventResultsReasons.ignore_event__block_is_not_in_a_mine );
				
				results.setIgnoreEvent( true );
			}
			else {
				
				// If not minable, then display message and exit.
				if ( !mine.getMineStateMutex().isMinable() ) {
					results.setResultsReason( EventResultsReasons.cancel_event__mine_mutex__mine_resetting );
					
					sPlayer.setActionBar( mineIsBeingResetMsg( mine.getTag() ) );
					results.setIgnoreEvent( true );
					results.setCancelEvent( true );
					
				}
//				else if ( bbPriority.isAccess() && !mine.hasMiningAccess(sPlayer) ) {
//					
//					results.setResultsReason( EventResultsReasons.cancel_event__player_has_no_access );
//					results.setIgnoreEvent( true );
//					results.setCancelEvent( true );
//					
//		    		if ( sPlayer != null &&
//		    				AutoFeaturesWrapper.getInstance()
//		    					.isBoolean( AutoFeatures.eventPriorityACCESSFailureTPToCurrentMine ) ) {
//		    			// run the `/mines tp` command for the player which will TP them to a 
//		    			// mine they can access:
//		    			
//						String debugInfo = String.format(
//										"ACCESS failed: teleport %s to valid mine.", 
//										sPlayer.getName() );
//						
//						PrisonCommandTaskData cmdTask = new PrisonCommandTaskData( debugInfo, 
//										"mines tp", 0 );
//						cmdTask.setTaskMode( TaskMode.syncPlayer );
//
//		    			PrisonCommandTasks.submitTasks( sPlayer, cmdTask );
//		    			
//		    		}
//
//				}
				else {
					
					MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( sBlock );
					
					if ( targetBlock != null ) {
						
						// If ignore all block events, then exit this function without logging anything:
						if ( targetBlock.isIgnoreAllBlockEvents() ) {
							
							// Do not cancel the event... let other plugins deal with it... prison does not care about this block.
							results.setResultsReason( EventResultsReasons.ignore_event__target_block_ignore_all_events );
							
							//event.setCancelled( true );
							results.setIgnoreEvent( true );
						}
						
						// If the block's already been counted, then can ignore the event:
						else if ( !ignoreBlockReuse && targetBlock.isCounted() ) {
							
							results.setResultsReason( EventResultsReasons.ignore_event__block_already_counted );
							
							results.setIgnoreEvent( true );
							
							// Cancel the event if the setting is enabled:
							if ( AutoFeaturesWrapper.getInstance().isBoolean( 
									AutoFeatures.ifBlockIsAlreadyCountedThenCancelEvent ) ) {
								
								results.setResultsReason( EventResultsReasons.cancel_event__block_already_counted );
								
								results.setCancelEvent( true );
							}
						}
					}
					else {
						// A targetBlock could not be found for the current block.  
						// So it must be invalid:
						
						// Do not cancel the event... 
						results.setResultsReason( EventResultsReasons.cancel_event__block_is_not_mappable_to_target_block );
						
						results.setCancelEvent( true );
					}
				}
				
				
			}
		}
		
		if ( results.getResultsReason() == EventResultsReasons.result_reason_not_yet_set ) {
			results.setResultsReason( EventResultsReasons.results_passed );
		}
		
		//results.logDebugInfo();
		
		return results;
	}
	
	
//	/**
//	 * <p>Warning... this is a temp copy of the real function and will be removed
//	 * if PEExplosionEvent adds the interface Cancellable.
//	 * </p>
//	 * 
//	 * @param event
//	 * @param player
//	 * @param block
//	 * @return
//	 */
//	protected boolean processMinesBlockBreakEvent( PEExplosionEvent event, Player player, Block block ) {
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
	
//	/**
//	 * <p>If mine is not null, then it will check for a zero-block reset (reset-threshold).
//	 * </p>
//	 * 
//	 * @param mine
//	 */
//	public void checkZeroBlockReset( Mine mine ) {
//		if ( mine != null ) {
//			
//			// Checks to see if the mine ran out of blocks, and if it did, then
//			// it will reset the mine:
//			mine.checkZeroBlockReset();
//		}
//	}
	
	
//	/**
//	 * <p>If mine is not null, then it will perform a mine sweeper 
//	 * for the mine, if it is enabled.
//	 * </p>
//	 * 
//	 * @param mine
//	 */
//	public void checkMineSweeper( Mine mine ) {
//		if ( mine != null ) {
//			
//			// submit a mine sweeper task.  It will only run if it is enabled and another 
//			// mine sweeper task has not been submitted.
//			mine.submitMineSweeperTask();
//		}
//	}
	
	
	
	/**
	 * <p>Checks only if the names match.  Does not check locations within any worlds.
	 * </p>
	 * @param pbTargetBlock
	 * @param pbBlockHit
	 * @return
	 */
	public boolean isBlockAMatch( MineTargetPrisonBlock targetBlock, PrisonBlock pbBlockHit )
	{
		boolean results = false;
		if ( targetBlock != null ) {
			PrisonBlockStatusData pbTargetBlock = targetBlock.getPrisonBlock();
			
			if ( pbTargetBlock != null && pbBlockHit != null ) {
				
				if ( pbTargetBlock.getBlockType() == PrisonBlockType.CustomItems ) {
					// The pbBlockHit will never match the pbTargetBlock.. must check the actual block:
					
					List<CustomBlockIntegration> cbIntegrations = 
							PrisonAPI.getIntegrationManager().getCustomBlockIntegrations();
					
					for ( CustomBlockIntegration customBlock : cbIntegrations )
					{
						String cbId = customBlock.getCustomBlockId( pbBlockHit );
						
						if ( cbId != null ) {
							
							if ( pbTargetBlock.getBlockName().equalsIgnoreCase( cbId ) ) {
								
								pbBlockHit.setBlockType( customBlock.getBlockType() );
								pbBlockHit.setBlockName( cbId );
								
								results = true;
								break;
							}
						}
					}
					
				}
				else {
					
					results = pbTargetBlock.equals( pbBlockHit );
				}
			}
		}
		
		return results;
	}
	

	/**
	 * <p>This function is only called once it has been confirmed that the block is the correct one, that
	 * it matches the the targetBlock.  All validation on if the block is correct, or not, has been 
	 * removed since this is not the place for the checks.
	 * </p>
	 * 
	 * <p>The function isBlockAMatch() should be used prior to calling this function.
	 * </p>
	 * 
	 * @param bukkitDrops
	 * @param targetBlock
	 * @param itemInHand
	 * @param sBlockMined
	 * @return
	 */
	public boolean collectBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock,
			SpigotItemStack itemInHand, SpigotBlock sBlockMined, SpigotPlayer player )
	{
		boolean results = false;

		if ( targetBlock != null && 
				targetBlock.getPrisonBlock().getBlockType().isCustomBlockType() ) {
			
			List<CustomBlockIntegration> cbIntegrations = 
					PrisonAPI.getIntegrationManager().getCustomBlockIntegrations();
			
			for ( CustomBlockIntegration customBlock : cbIntegrations )
			{
				List<? extends ItemStack> drops = customBlock.getDrops( player, sBlockMined, itemInHand );
				
				for ( ItemStack drop : drops )
				{
					bukkitDrops.add( (SpigotItemStack) drop );
					results = true;
				}
			}
			
		}
		
		
		
		// if ( sBlockMined == null && targetBlock.getMinedBlock() != null ) {
		// sBlockMined = (SpigotBlock) targetBlock.getMinedBlock();
		// }
		// SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();

		// If in the mine, then need a targetBlock, otherwise if it's null then get drops anyway:
		if ( !results && sBlockMined != null 
//				&& ( targetBlock == null ||
//					targetBlock.getPrisonBlock().equals( sBlockMined.getPrisonBlock() )
//				   ) 
				)
		{
			

			getSpigotDrops( bukkitDrops, sBlockMined, itemInHand );
			
			//// This clears the drops for the given block, so if the event is
			//// not canceled, it will
			//// not result in duplicate drops.
			// if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
			// sBlock.clearDrops();
			// }
			
			results = true;

		}
//		else if ( !results && sBlockMined != null )
//		{
//			Output.get().logWarn( "collectBukkitDrops: block was changed and not what was expected.  " + "Block: " +
//					sBlockMined.getBlockName() + "  expecting: " + 
//					(targetBlock == null ? "(nothing)" : targetBlock.getPrisonBlock().getBlockName()) );
//		}

		return results;
	}

	/**
	 * <p>This gets the bukkit drops...
	 * </p>
	 * 
	 * @param bukkitDrops
	 * @param sBlockMined
	 * @param itemInHand
	 */
	private void getSpigotDrops( List<SpigotItemStack> bukkitDrops, SpigotBlock sBlockMined, SpigotItemStack itemInHand )
	{
		List<SpigotItemStack> drops = SpigotUtil.getDrops( sBlockMined, itemInHand );
		
		bukkitDrops.addAll( drops );
	}
	
	
	public void clearBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock )
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
	public List<SpigotItemStack> mergeDrops( List<SpigotItemStack> drops )
	{
		if ( drops.size() <= 1 ) {
			return drops;
		}
		TreeMap<String,SpigotItemStack> results = new TreeMap<>();

		boolean changed = false;
		for ( SpigotItemStack drop : drops ) {
			String key = drop.getName();
			if ( !results.containsKey( key ) ) {
				results.put( key, drop );
			}
			else {
				SpigotItemStack sItemStack = results.get( key );
				
				sItemStack.setAmount( sItemStack.getAmount() + drop.getAmount() );
				changed = true;
			}
		}
		
		return changed ?  new ArrayList<>( results.values() ) : drops;
	}

	
	private Mine findMineLocation( SpigotBlock block ) {
		return getPrisonMineManager() == null || block == null || block.getLocation() == null ? 
				null : getPrisonMineManager().findMineLocationExact( block.getLocation() );
	}
	

	private TreeMap<String, Mine> getPlayerCache() {
		return getPrisonMineManager() == null ? 
				new TreeMap<String, Mine>() :
				getPrisonMineManager().getPlayerCache();
	}

	private PrisonMines getPrisonMineManager() {
		if ( prisonMineManager == null && !isMineModuleDisabled() ) {
			
			Module module = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
			
			if ( module != null && module.isEnabled() ) {
				PrisonMines prisonMines = (PrisonMines) module;
				this.prisonMineManager = prisonMines;
			} else {
				setMineModuleDisabled( true );
			}
		}
		return prisonMineManager;
	}
	
	private boolean isMineModuleDisabled() {
		return mineModuleDisabled;
	}
	private void setMineModuleDisabled( boolean mineModuleDisabled ) {
		this.mineModuleDisabled = mineModuleDisabled;
	}
}

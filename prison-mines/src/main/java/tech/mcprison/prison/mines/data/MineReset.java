package tech.mcprison.prison.mines.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetBlockKey;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.MineScheduler.MineJob;
import tech.mcprison.prison.mines.data.MineScheduler.MineResetActions;
import tech.mcprison.prison.mines.events.MineResetEvent;
import tech.mcprison.prison.mines.features.MineLinerBuilder;
import tech.mcprison.prison.mines.features.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.mines.features.MineMover;
import tech.mcprison.prison.mines.features.MineTracerBuilder;
import tech.mcprison.prison.mines.tasks.MinePagedResetAsyncTask;
import tech.mcprison.prison.mines.tasks.MineTeleportTask;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.tasks.PrisonCommandTaskData;
import tech.mcprison.prison.tasks.PrisonCommandTasks;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Bounds.Edges;
import tech.mcprison.prison.util.Location;

public abstract class MineReset
	extends MineData
{
	/**
	 * <p>Minecraft ticks have 20 per seconds, which is 50 MS per tick. 
	 * The value for MINE_RESET__MAX_PAGE_ELASPSED_TIME_MS is intended
	 * to be used as a threshold to determine when to stop placing blocks
	 * and resubmit in the sync job queue to allow other processes to run.
	 * </p>
	 * 
	 * <p>This value, in milliseconds, is not hard-and-fast guaranteed to be 
	 * caught exactly at that time. It is instead used to check to see if
	 * the current process has exceeded this value, which may be many times
	 * greater than this value.
	 * </p>
	 * 
	 * <p>This value is subject to refinement and tuning to ensure better and
	 * more accurate responses.
	 * </p>
	 */
	public static final long MINE_RESET__MAX_PAGE_ELASPSED_TIME_MS = 75;

	
	/**
	 * <p>This is the time in ticks that is used when submitting 
	 * the reset jobs. A value of 0 could cause the server to lock up since
	 * it does not have enough time to deal with other tasks.  
	 * </p>
	 * 
	 * <p>One tick is 50 milliseconds, 2 is 100 milliseconds or 1/10 of 
	 * a second.
	 * </p>
	 */
	public static final long MINE_RESET__PAGE_SUBMIT_DELAY_TICKS = 1;
	
	/**
	 * <p>When placing blocks, this is the block count that is used to check for
	 * the elapsed time.
	 * </p>
	 * 
	 * <p>It does not matter if some of those blocks were placed in a prior 
	 * page, for it is the elapsed time that is important.  It's also important
	 * that there are not too many checks since it will just contribute to lag.
	 * The value needs to be moderately large.
	 * </p>
	 * 
	 * <p>This value is subject to refinement and tuning to ensure better and
	 * more accurate responses.
	 * </p>
	 */
	public static final long MINE_RESET__PAGE_TIMEOUT_CHECK__BLOCK_COUNT = 250;
	
	
	// NOTE: Longer delay on air counts will not prevent the "server is running behind" messages.
	public static final long MINE_RESET__AIR_COUNT_BASE_DELAY_TICKS = 10 * 20L; // 10 seconds
	public static final long MINE_RESET__AIR_COUNT_SUBMIT_GAP_TICKS = 10; // 10 ticks == 0.5 second
	

	private transient List<MineTargetPrisonBlock> mineTargetPrisonBlocks = null;
	private transient TreeMap<MineTargetBlockKey, MineTargetPrisonBlock> mineTargetPrisonBlocksMap = null;
	
	private transient MineJob currentJob;
	
	private transient int resetPage = 0;
	private transient int resetPosition = 0;
	
	private transient long resetPageMaxPageElapsedTimeMs = -1;
	private transient long resetPagePageSubmitDelayTicks = -1;
	private transient long resetPageTimeoutCheckBlockCount = -1;
	
	private transient int airCountOriginal = 0;
	private transient int airCount = 0;
	private transient long airCountTimestamp = 0L;
	private transient long airCountElapsedTimeMs = 0L;
	
	
	
	private transient long statsResetTimeMS = 0;
	private transient long statsBlockGenTimeMS = 0;
	private transient long statsBlockUpdateTimeMS = 0;
	private transient long statsBlockUpdateTimeNanos = 0;
	
	
	private transient int statsResetPages = 0;
	private transient long statsResetPageBlocks = 0;
	private transient long statsResetPageMs = 0;
	
	
	private transient List<Long> statsMineSweeperTaskMs;
	private transient boolean mineSweeperSubmitted = false;
	
	public MineReset() {
		super();
		
		this.statsMineSweeperTaskMs = new ArrayList<>();

		this.currentJob = null;
		
	}

    /**
     * <p>This initialize function gets called after the classes are
     * instantiated, and is initiated from Mine class and propagates
     * to the MineData class.  Good for kicking off the scheduler.
     * </p>
     * 
     * <p>Once the mine has been loaded in to memory, the number of 
     * air blocks must be counted to properly set the blockBreakCount.
     * </p>
     * 
     */
	@Override
	protected void initialize() {
	    	super.initialize();
	    	
	    	if ( !isVirtual() ) {
	    		
	    		// Once the mine has been loaded, MUST get a count of all air blocks.
	    		refreshBlockBreakCountUponStartup( 0 );
	    	}
    }
    
    
    public String statsMessage() {
	    	StringBuilder sb = new StringBuilder();
	    	DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
	    	DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
	    	
	    	sb.append( "&3 ResetTime: &7" );
	    	sb.append( dFmt.format(getStatsResetTimeMS() / 1000.0d )).append( " s " );
	    	
	    	sb.append( "&3 BlockGenTime: &7" );
	    	sb.append( dFmt.format(getStatsBlockGenTimeMS() / 1000.0d )).append( " s " );
	    	
	
	    	sb.append( "&3 BlockUpdateTime: &7" );
	    	sb.append( dFmt.format(getStatsBlockUpdateTimeMS() / 1000.0d )).append( " s " );
	    	sb.append( dFmt.format(getStatsBlockUpdateTimeNanos() / 1000000.0d )).append( " ms(nanos) " );
	    	
	    	
	    	sb.append( "&3 ResetPages: &7" );
	    	sb.append( iFmt.format(getStatsResetPages() ));
	    	
	    	double avgBlocks = getStatsResetPages() == 0 ? 0 : 
	    			getStatsResetPageBlocks() / getStatsResetPages();
	    	double avgMs = getStatsResetPages() == 0 ? 0 :
	    			getStatsResetPageMs() / getStatsResetPages();
	    	
	    	sb.append( "&3 avgBlocksPerPage: &7" );
	    	sb.append( dFmt.format(avgBlocks));
	    	
	    	sb.append( "&3 avgMsPerPage: &7" );
	    	sb.append( dFmt.format(avgMs));
	    	
	    	sb.append( statsMessageMineSweeper() );
	    	
	//    	sb.append(  "  TPS: " )
	//    		.append( Prison.get().getPrisonTPS().getAverageTPSFormatted() );
	    	
	    	return sb.toString();
    }

    public String statsMessageMineSweeper() {
	    	StringBuilder sb = new StringBuilder();
	    	
	    	if ( getStatsMineSweeperTaskMs().size() > 0 ) {
	    		sb.append( " &3 MineSweeper ms: " );
	    		
	    		for ( Long sweepMs : getStatsMineSweeperTaskMs() ) {
	    			sb.append( sweepMs ).append( " " );
				}
	    	}
	    	
	    	return sb.toString();
    }
    
    private void resetStats() {
	    	setResetPage( 0 ); 
	    	
	    	setBlockBreakCount( 0 );
	    	
			// The reset position is critical in ensuring that all blocks within the mine are reset 
			// and that when a reset process pages (allows another process to run) then it will be
			// used to pick up where it left off.
	    	setResetPosition( 0 );
	    	
	    	setSkipResetBypassCount( 0 );
	    	
	    	// NOTE: DO NOT reset blockBreakCount here!  Players can break many blocks between
	    	//       here and when the mine actually starts to reset.
	    	
	    	setAirCountOriginal( 9 );
	    	setAirCount( 0 );
	
	    	setStatsResetTimeMS( 0 );
	    	setStatsBlockGenTimeMS( 0 );
	    	setStatsBlockUpdateTimeMS( 0 );
	    	setStatsBlockUpdateTimeNanos( 0 );
	    	
	    	setStatsResetPages( 0 );
	    	setStatsResetPageBlocks( 0 );
		setStatsResetPageMs( 0 );
		
		
		// Save the if there are unsaved blocks:
		saveIfUnsavedBlockCounts();
		
		
		// Mine Sweeper counts:
		getStatsMineSweeperTaskMs().clear();
		
    }
    
    public void saveIfUnsavedBlockCounts() {
		if ( hasUnsavedBlockCounts() ) {
			PrisonMines.getInstance().getMineManager().saveMine( (Mine) this );

			resetUnsavedBlockCounts();
		}
    }
	

    protected abstract long teleportAllPlayersOut();
    
    
    public abstract void teleportPlayerOut(Player player);
    

    public abstract Location teleportPlayerOut(Player player, String targetLocation);


	public abstract Location alternativeTpLocation();
	
	
	public abstract void submitTeleportGlassBlockRemoval();
	
	
    /**
     * This should be used to submit async tasks.
     * 
     * @param callbackAsync
     */
    public abstract int submitAsyncTask( PrisonRunnable callbackAsync, long delay );
    
    public abstract int submitSyncTask( PrisonRunnable callbackSync, long delay );


	
	
    protected abstract void broadcastResetMessageToAllPlayersWithRadius();
    
    protected abstract void broadcastPendingResetMessageToAllPlayersWithRadius(MineJob mineJob);
    
    protected abstract void broadcastSkipResetMessageToAllPlayersWithRadius();

    
	
	public int getPlayerCount() {
		int count = 0;
		
		if ( !isVirtual() && isEnabled() ) {

			World world = getWorld().get();
			
			List<Player> players = (world.getPlayers() != null ? world.getPlayers() : 
				Prison.get().getPlatform().getOnlinePlayers());
			for (Player player : players) {
				if ( getBounds().withinIncludeTopBottomOfMine(player.getLocation()) ) {
					count++;
				}
			}
		}
		
		return count;
	}
    


    /**
     * <p>This should now be ran before any mine reset. This generates the list prior to placing
     * any blocks so as to ensure the constraints are honored.  It's easier to update the
     * list that this generates, than to update the blocks in the wild after the fact.  Not 
     * to mention this will be far faster.
     * </p>
     * 
     * <p>This generation of a new block list for the mines is designed to run asynchronously. 
     * It not only generates what each block should be, it also records what the block location 
     * is.  This allows actual block updates to be performed linearly using the mineTargetBlock
     * List, or to randomly access each block using the mineTargetBlockMap; two keys in to the 
     * same collection of target blocks.
     * </p>
     * 
     * <p>The major use of the mineTargetBlock List is to allow paging of the updates: where a mine
     * can be updated in smaller segments.  The actual update must be ran synchronously and in small
     * segments.  
     * </p>
     * 
     * <p>Set the Y plane to refresh at the top and work its way down.  That way if the play is 
     * teleported to the top, it will appear like the whole mine has reset instantly and they will 
     * not see the delay from the bottom of the mine working up to the top.  This will also reduce
     * the likelihood of the player falling back in to the mine if there is no spawn set.
     * </p>
     * 
     */
    public void generateBlockListAsync() {
		
	    	if ( isVirtual() || isDeleted() ) {
	    		// ignore and generate no error messages:
	    		return;
	    	}
	    	
		if ( !isEnabled() ) {
			Output.get().logError(
					String.format( "MineReset: Block count failure: Mine is not enabled. " +
							"Ensure world exists. mine= %s ", 
							getName()  ));
			return;
		}
			
	    	long start = System.currentTimeMillis();
		
		// Reset stats:
		resetStats();
		
		Random random = new Random();
		
		// Clear the mineTargetBlocks List and Map:
		clearMineTargetPrisonBlocks();

		
		// Reset the resetCounts on all blocks within the mine:
		resetResetBlockCounts();
		
		
		Optional<World> worldOptional = getWorld();
		World world = worldOptional.get();
		
		int airCount = 0;
		
		
		int yMin = getBounds().getyBlockMin();
		int yMax = getBounds().getyBlockMax();
		
		int xMin = getBounds().getxBlockMin();
		int xMax = getBounds().getxBlockMax();
		
		int zMin = getBounds().getzBlockMin();
		int zMax = getBounds().getzBlockMax();

		
		int currentLevel = 0;
		int maxLevels = yMax - yMin + 1;
		
		
		// The reset takes place first with the top-most layer since most mines may have
		// the player enter from the top, and the reset will appear to be more "instant".
		for (int y = yMax; y >= yMin; y--) {
			currentLevel++; // One based: First layer is currentLevel == 1
			
			
			// This is used to select the correct block list for the given mine level:
			MineLevelBlockListData mineLevelBlockList = 
							new MineLevelBlockListData( currentLevel, maxLevels, (Mine) this, random );
			
			
			for (int x = xMin; x <= xMax; x++) {
				for (int z = zMin; z <= zMax; z++) {
					
					// updates selected block's exclude from bottom layer max value settings:
					mineLevelBlockList.checkSelectedBlockExcludeFromBottomLayers();
					
					boolean xEdge = x == xMin || x == xMax;
					boolean yEdge = y == yMin || y == yMax;
					boolean zEdge = z == zMin || z == zMax;
					
					boolean isEdge = xEdge && yEdge || xEdge && zEdge ||
									 yEdge && zEdge;
					
					boolean isCorner = xEdge && yEdge && zEdge;
					
					Location targetLocation = new Location(world, x, y, z);
					targetLocation.setEdge( isEdge );
					targetLocation.setCorner( isCorner );
					
					
					PrisonBlock prisonBlock = mineLevelBlockList.randomlySelectPrisonBlock();
					
					if ( prisonBlock == null ) {
						prisonBlock = PrisonBlock.AIR.clone();
					}
					
					
					// Increment the mine's block count. This block is one of the control blocks:
					incrementResetBlockCount( prisonBlock );
					
					// TODO AIR block fix - allow AIR to be part of the regular block list?
					addMineTargetPrisonBlock( prisonBlock, targetLocation );
					
					if ( prisonBlock.isAir() ) {

						airCount++;
					}
					
				}
			}
		}
		

		
		setAirCountOriginal( airCount );
		setAirCount( airCount );

		
		// Apply the constraints
		constraintsApplyMin();
		
		
		if ( Output.get().isDebug() && Output.get().isSelectiveTarget( DebugTarget.blockConstraints ) ) {
			
	    	DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
			
			for ( PrisonBlockStatusData b : getPrisonBlocks() ) {
				
				int rangeLow = b.getRangeBlockCountLowLimit();
				int rangeHigh = b.getRangeBlockCountHighLimit();
				int rangeCount = b.getRangeBlockCountHighLimit() - b.getRangeBlockCountLowLimit() + 1;
				double rangePercent = rangeCount / (double) getBounds().getTotalBlockCount();
				
				String msg = String.format(
						"  Block: %-14s : placed: %-5d  PlacementRange: (%d - %d) "
						+ "%d out of %d  %s  "
						+ "min: %d  max: %d  ExcldTop: %d  ExcldBottom: %d ",
						b.getBlockName(), b.getBlockPlacedCount(),
						rangeLow, rangeHigh,
						rangeCount,
						getBounds().getTotalBlockCount(),
						dFmt.format( rangePercent ),
						
						b.getConstraintMin(), b.getConstraintMax(),
						b.getConstraintExcludeTopLayers(),
						b.getConstraintExcludeBottomLayers()
						);
				
				Output.get().logInfo( msg );
			}
		}
		
		// The reset position is critical in ensuring that all blocks within the mine are reset 
		// and that when a reset process pages (allows another process to run) then it will be
		// used to pick up where it left off.
		setResetPosition( 0 );
		
		long stop = System.currentTimeMillis();
		setStatsBlockGenTimeMS( stop - start );
		
    }
    
    
    
    public List<String> getTargetBlockStatsPerLevel() {
	    	List<String> layers = new ArrayList<>();
	    	
	    	
	    	// Scan all blocks to see if they are the same or now air
	    	//  Use isCheckAir() and isCheckSamme();
	    	scanAllBlocksForUpdates();
	    	
	    	
	    	// Add AIR to make sure it is there:
	    	PrisonBlock air = PrisonBlock.AIR.clone();
	    	boolean hasAir = getPrisonBlock( air.getBlockName() ) != null;
	    	
	    	if ( !hasAir ) {
	    		getPrisonBlocks().add( air );
	    		getBlockStats( air );
	    	}
	    	
	    	int j = 0;
	    	TreeSet<String> keys = new TreeSet<>( getBlockStats().keySet() );
	    	for (String key : keys) {
			PrisonBlockStatusData blk = getBlockStats().get( key );
			
			blk.setAltValues( j++ );
		}
    	
	    	
	    	int layer = 0;
	    	int blockCount = 0;
	    	
	    	for ( int i = 0; i < getMineTargetPrisonBlocks().size(); i++ ) {
	    		
	    		MineTargetPrisonBlock tBlock = getMineTargetPrisonBlocks().get(i);
	    		int y = getMineTargetPrisonBlocks().get(i).getLocation().getBlockY();
	    		blockCount++;
	    		
	    		
	    		{
	    			PrisonBlockStatusData statsBlock = null;
	    			
	    			// the getPrisonblock() should not return a null value:
	    			PrisonBlockStatusData sBlock = tBlock.getPrisonBlock();
	    			
	    			if ( sBlock == null ) {
	    				sBlock = PrisonBlock.AIR.clone();
	    			}
	    			
	    			String blockName = sBlock.getBlockName();
	    			statsBlock = getBlockStats().get( blockName );
	    			
	    			
	    			// TODO Not sure if air.getBlockName() is correct!  It was just 'air' which was wrong.
	    			//      Did not have time to review this code to see if blockName is correct.
	    			if ( statsBlock == null ) {
	    				statsBlock = getBlockStats().get( air.getBlockName() );
	    			}
	    			
	    			
	    			statsBlock.setAltCountVirtual( statsBlock.getAltCountVirtual() + 1);
	    			if ( tBlock.isCheckSame() ) {
	    				statsBlock.setAltCountPhysical( statsBlock.getAltCountPhysical() + 1);
	    			}
	    			else if ( tBlock.isCheckAir() ) {
	    				
	    				PrisonBlockStatusData airStats = getBlockStats( air );
	    				airStats.setAltCountPhysical( airStats.getAltCountPhysical() + 1);
	    				
	    			}
	    			
	    		}
    		
    		
    		
	    		if ( (i + 1) >= getMineTargetPrisonBlocks().size() ||
	    				getMineTargetPrisonBlocks().get(i + 1).getLocation().getBlockY() != y ) {
	    			
	    			StringBuilder sb = new StringBuilder();
	    			
	    			sb.append( "Layer " ).append( layer++ ) 
	    				.append( " (" ).append( blockCount ).append(")")
	    				.append( " : " );
	    			
	    			for ( String k : keys ) {
	    				
	    				PrisonBlockStatusData statsBlock = getBlockStats( k );
	    				
	    				sb.append( statsBlock.getAltColorCode() )
	    				  .append( statsBlock.getAltAlias() )
	    				  .append( Output.get().getColorCodeInfo() )
	    				  .append( ":" )
	    				  .append( statsBlock.getAltCountVirtual() );
	    				
	    				if ( statsBlock.getAltCountVirtual() != statsBlock.getAltCountPhysical() ) {
	    					sb.append( ":" )
	    					  .append( statsBlock.getAltCountPhysical() );
	    				}
	    				
	    				sb.append( " " );
	    				
	    				statsBlock.resetAltValues();
	    				
	    			}
	    			
	    			layers.add( sb.toString() );
	
	    			blockCount = 0;
	    		}
    		

	    	}
	    	
	    	
	    	// Print the legend:
	    	{
	    		// print the legend:
	    		StringBuilder sb = new StringBuilder();
	    		
	    		sb.append( "Legend: " );
	    		
	    		for ( String k : keys ) {
					
	    			PrisonBlockStatusData statsBlock = getBlockStats( k );
	
	   				sb.append( statsBlock.getAltColorCode() )
					  .append( statsBlock.getAltAlias() )
					  .append( Output.get().getColorCodeInfo() )
					  .append( "=" )
					  .append( statsBlock.getBlockName() )
	   				  .append( "  " );
	
					
	    		}
    		
    		
	    		layers.add( sb.toString() );
	    		
	    	}
	    	
	    	
	    	// If there is no air in the layers, then remove it from the stats:
	    	if ( !hasAir ) {
	    		removePrisonBlock( air );
	    		getBlockStats().remove( air.getBlockName() );
	    	}
	
	    	
	    	return layers;
    }
    
    
    public void asynchronouslyResetSetup() {
    	
		// Reset the block break count before resetting the blocks:
		// Set it to the original air count, if subtracted from total block count
		// in the mine, then the result will be blocks remaining.
 		setBlockBreakCount( getAirCountOriginal() );
 		
 		
 		if ( !getCurrentJob().getResetActions().contains( MineResetActions.NO_COMMANDS )) {
 			
 			// Before reset commands:
 			if ( getResetCommands() != null && getResetCommands().size() > 0 ) {
 				
 				List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
 				
 				int row = 0;
 				for (String command : getResetCommands() ) {
 					row++;
 					
 					if ( command.startsWith( "before: " )) {
 						String cmd = command.replace( "before: ", "" );
 						
 						String debugInfo = "MineReset: " + getName() + " Before:";
 						PrisonCommandTaskData cmdTask = new PrisonCommandTaskData( 
 								debugInfo, cmd, row );
 						
 						cmdTasks.add( cmdTask );
 					}
 				}
 				
 				PrisonCommandTasks.submitTasks( cmdTasks );
 			}
 		}
    }

    public void asynchronouslyResetFinalize( List<MineResetActions> jobResetActions ) {
		// If a player falls back in to the mine before it is fully done being reset, 
		// such as could happen if there is lag or a lot going on within the server, 
		// this will TP anyone out who would otherwise suffocate.  I hope! lol
    	
    		
	    	if ( Prison.get().getPlatform().getConfigBooleanTrue( "prison-mines.tp-to-spawn-on-mine-resets" ) ) {
	    		MineTeleportTask teleportTask = new MineTeleportTask( (Mine) this );
	    		teleportTask.submitTaskSync();
	    	}
    	
		
		
		// Reset the paging for the next reset:
		setResetPage( 0 );
		
		incrementResetCount();
		
		try {
			((MineScheduler) this).setMineResetStartTimestamp( -1 );
		} catch (Exception e) {
			// ignore...
		}
		
		if ( !getCurrentJob().getResetActions().contains( MineResetActions.NO_COMMANDS )) {
			
			// After reset commands:
			if ( getResetCommands() != null && getResetCommands().size() > 0 ) {
				
 				
 				List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
 				
 				
 				int row = 0;
				for (String command : getResetCommands() ) {
					row++;

					if ( command.startsWith( "after: " )) {
						String cmd = command.replace( "after: ", "" );
						
						String debugInfo = "MineReset: " + getName() + " After:";
 						PrisonCommandTaskData cmdTask = new PrisonCommandTaskData( 
 								debugInfo, cmd, row );

 						cmdTasks.add( cmdTask );
					}
				}
				
				PrisonCommandTasks.submitTasks( cmdTasks );
			}
		}
        
		
		// Broadcast message to all players within a certain radius of this mine:
		broadcastResetMessageToAllPlayersWithRadius();
		
		
		submitTeleportGlassBlockRemoval();
		
        
        // Tie to the command stats mode so it logs it if stats are enabled:
        if ( PrisonMines.getInstance().getMineManager().isMineStats() || 
        			getCurrentJob().getResetActions().contains( MineResetActions.DETAILS ) ) {
        	
	        	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
	        	Output.get().logInfo("&cMine reset: &7" + getTag() + 
	        			"&c  Blocks: &7" + dFmt.format( getBounds().getTotalBlockCount() ) + 
	        			statsMessage() );
        }
        
		// If part of a chained_resets, then kick off the next reset:
		if ( jobResetActions != null && jobResetActions.contains( MineResetActions.CHAINED_RESETS ) ||
				getCurrentJob().getResetActions().contains( MineResetActions.CHAINED_RESETS )) {
			
			PrisonMines pMines = PrisonMines.getInstance();
			pMines.resetAllMinesNext();
		}

    }
    
	public boolean resetAsynchonouslyInitiate( MineResetType resetType ) {
	    	boolean canceled = false;
			
	    	if ( isVirtual()) {
	    		canceled = true;
	    	}
	    	else 
			if ( !isEnabled() ) {
				Output.get().logError(
						String.format( "MineReset: resetAsynchonouslyInitiate failure: Mine is not enabled. " +
								"Ensure world exists. mine= %s ", 
								getName()  ));
				canceled = true;
			}
			else {
				
				// The all-important event
				MineResetEvent event = new MineResetEvent(this, resetType);
				Prison.get().getEventBus().post(event);
				
				canceled = event.isCanceled();
				if (!canceled) {
					
		    		
				    	if ( Prison.get().getPlatform().getConfigBooleanTrue( "prison-mines.tp-to-spawn-on-mine-resets" ) ) {
				    		MineTeleportTask teleportTask = new MineTeleportTask( (Mine) this );
				    		teleportTask.submitTaskSync();
				    	}
	
				}
				
			}
	
	    	return canceled;
    }
    



    /**
     * This task should be ran upon loading of the mines upon server start.  
     * This will calculate how many air blocks there are within the mine, which
     * will be what the blockBreakCount should be set to initially. 
     * 
     * But as a warning, this may trigger stack traces if there are active
     * entities in the unloaded chunks.  May have run this synchronously. :(
     */
    public void refreshBlockBreakCountUponStartup( long delay) {
    	
    	// not used
    }
    
    
    /**
     * <p>This function performs the air count and should be ran as an async task.
     * </p>
     * 
     * <p>This also should be used to generate the targetBlockList to help identify
     * unknown blocks when being processed.
     * </p>
     * 
     * <p>WARNING: This generates a ton of async failures upon startup or during other
     * times.  This MUST be ran synchronously...
     * </p>
     * 
     */
	protected boolean refreshAirCountSyncTaskCheckBeforeSubmit()
	{
		boolean results = false;
    	
	    	if ( isVirtual() ) {
	    		// ignore:
	    	}
	    	else 
	    	if ( !isEnabled() ) {
				Output.get().logError(
						String.format( "MineReset: refreshAirCountAsyncTask failure: Mine is not enabled. " +
								"Ensure world exists. mine= %s ", 
								getName()  ));
			}
			else if ( getPrisonBlocks().size() == 1 && 
					getPrisonBlocks().get( 0 ).equals( PrisonBlock.IGNORE ) ) {
			
				// This mine is set to ignore all blocks when trying to do a reset, 
				// so for now ignore the types and just set air count to zero.
				// Basically, this mine, if using natural spawned landscape, may contain blocks that are
				// not registered and tracked within prison, and hence will report incorrect errors.
				setAirCount( 0 );
			}
			else {
				Optional<World> worldOptional = getWorld();
				World world = worldOptional.get();
				
				if ( world == null ) {
					Output.get().logError(
							String.format( "MineReset: refreshAirCountAsyncTask failure: The world is invalid and " +
									"cannot be located. mine= %s  worldName=%s ", 
									getName(), getWorldName() ));
	
					
				}
				else {
					
					// This means we can actually go ahead and perform the air-counts and so the 
					// actual job can be submitted:
					return true;
				}
			
			}
			
	    	return results;
	}
	
	
	/**
	 * <p>This can be ran Asynchronously because it's just creating the Locations that will be used
	 * to generate the target blocks.  This does not update anything in the world so can be
	 * async.
	 * </p>
	 * 
	 * @return
	 */
	public List<Location> refreshAirCountSyncTaskBuildLocations() {
		List<Location> locations = new ArrayList<>();
		
		Optional<World> worldOptional = getWorld();
		World world = worldOptional.get();
		
		{
			
			// Reset the target block lists:
			clearMineTargetPrisonBlocks();
			
			
			int yMin = getBounds().getyBlockMin();
			int yMax = getBounds().getyBlockMax();
			
			int xMin = getBounds().getxBlockMin();
			int xMax = getBounds().getxBlockMax();
			
			int zMin = getBounds().getzBlockMin();
			int zMax = getBounds().getzBlockMax();
			
			
			for (int y = yMax; y >= yMin; y--) {
				for (int x = xMin; x <= xMax; x++) {
					for (int z = zMin; z <= zMax; z++) {
						
						boolean xEdge = x == xMin || x == xMax;
						boolean yEdge = y == yMin || y == yMax;
						boolean zEdge = z == zMin || z == zMax;
						
						boolean isEdge = xEdge && yEdge || xEdge && zEdge ||
								yEdge && zEdge;
						
						boolean isCorner = xEdge && yEdge && zEdge;
						
						Location targetLocation = new Location(world, x, y, z);
						targetLocation.setEdge( isEdge );
						targetLocation.setCorner( isCorner );
						
						locations.add( targetLocation );
						
					}
				}
			}
			
		}
		
		return locations;
	}
	
	
	public void refreshAirCountSyncTaskSetLocation( Location targetLocation, 
					OnStartupRefreshBlockBreakCountSyncTask stats ) {
		
		try {
			
			boolean containsCustomBlocks = 
					getPrisonBlockTypes().contains( PrisonBlockType.CustomItems ) ||
					getPrisonBlockTypes().contains( PrisonBlockType.ItemsAdder );

			Block tBlock = targetLocation.getBlockAt( containsCustomBlocks );
			
			
			
			PrisonBlock pBlock = tBlock.getPrisonBlock();
			
			if ( pBlock == null ) {
				// TODO AIR block fix - allow AIR to be part of the regular block list?
				pBlock = PrisonBlock.AIR.clone();
			}
			
				
			// Increment the mine's block count. This block is one of the control blocks:
			addMineTargetPrisonBlock( incrementResetBlockCount( pBlock ), targetLocation );
			
			
			if ( pBlock.isAir() ) {
				stats.incrementAirCount();
			}
		}
		catch ( Exception e ) {
			String msg = e.getMessage();
			
			stats.incrementErrorCount();
			
			// Updates to the "world" should never be ran async.  Upon review of the above 
			// that gets the location and block, causes the chunk to load, if it is not loaded,
			// and if there is an entity in that loaded chunk it will throw an exception:
			//     java.lang.IllegalStateException: Asynchronous entity world add!
			// If there are no entities, it will be fine, but they could cause issues with async 
			// access of unloaded chunks.
			
			String coords = targetLocation.toBlockCoordinates();
			
			if ( stats.getExceptionError() == null ) {
				
				stats.setExceptionError( msg );
				
				StackTraceElement[] stackTrace = e.getStackTrace();
				
				// print only the first 6 lines of the stack trace:
				for (int i = 0; i < stackTrace.length && i <= 9; i++) {
					StackTraceElement stEle = stackTrace[i];
					Output.get().logWarn( "*#* " + stEle.toString());
				}
			}
			
			if ( stats.getErrorCount() == 0 ) {
				// Error count is 0, so setup the messages:
				String message = String.format( 
						"MineReset.refreshAirCountAsyncTask: Error counting air blocks: " +
								"Mine=%s coords=%s  Error: [%s] ", getName(), coords, msg );
				
				if ( msg != null && msg.contains( "Asynchronous entity world add" )) {
					Output.get().logWarn( message, e );
				} 
//				else {
//					Output.get().logWarn( message, e );
//				}
				
			} 
			else if ( stats.getErrorCount() <= 10 ) {
				stats.getSbErrors().append( coords );
			}
		}
		
	}
	
	
	private void scanAllBlocksForUpdates() {
		
		World world = getBounds().getCenter().getWorld();
		if ( world != null ) {

			long start = System.currentTimeMillis();
			
			int i = 0;
			for ( MineTargetPrisonBlock targetBlock : getMineTargetPrisonBlocks() ) {
				
				if ( targetBlock != null ) {
					
					i++;
					
					targetBlock.setCheckAir( false );
					targetBlock.setCheckSame( false );
					
					try {
						
						boolean containsCustomBlocks = 
								getPrisonBlockTypes().contains( PrisonBlockType.CustomItems ) ||
								getPrisonBlockTypes().contains( PrisonBlockType.ItemsAdder );

						Block tBlock = targetBlock.getLocation().getBlockAt( containsCustomBlocks );
						PrisonBlock pBlock = tBlock == null ? null : tBlock.getPrisonBlock();
						
						PrisonBlockStatusData tpBlock = targetBlock.getPrisonBlock();
						
						if ( tBlock == null || pBlock == null || tpBlock == null ) {
							targetBlock.setCheckAir( true );
						}
						else {
							
							
							String targetBlockName = tpBlock.getBlockName();
							
							
							if ( pBlock.getBlockName().equalsIgnoreCase( targetBlockName) ) {
								targetBlock.setCheckSame( true );
							}
							else if ( pBlock.isAir() ) {
								targetBlock.setCheckAir( true );
							}
							else if ( pBlock.getBlockName().equalsIgnoreCase( targetBlockName) ) {
								targetBlock.setCheckSame( true );
							}
							else {
								targetBlock.setCheckSame( false );
								
							}
							
						}
						
					}
					catch ( Exception e ) {
					
						Output.get().logInfo( "MineReset.scanAllBlocksForUpdates: error:"
								+ "  count=%d  %s", (i - 1), e.getMessage());
						
					}
				}
			
			}
			
			if ( Output.get().isDebug() ) {

				long stop = System.currentTimeMillis();
				long elapsed = stop - start;
				
				double ms = elapsed / 1_000_000_000d;
				String msStr = Prison.getDecimalFormatStaticDouble().format(ms);
				
				Output.get().logInfo( 
						String.format( 
								"MineReset.scanAllBlocksForUpdates runtime%s=", 
								msStr
							)
						);
				
			}
		}
	}
	
	/**
	 * <p>This function should ONLY be used if an enchantment plugin is being used that cannot
	 * provide a working explosion event to monitor, and the plugin is breaking more blocks than what
	 * is being reported with the BlockBreakEvent.  
	 * </p>
	 * 
	 * <p>This function ignores previously counted blocks, which is indicated with isAirBroke().
	 * Since it only processes blocks that have not been identified as being broke, then whenever
	 * it encounters an air block (block.isEmpty()) then it is understood that block was just 
	 * broke and needs to be counted then the related targetBlock needs to be set as <b>air</b> and as
	 * have been <b>broke</b>.
	 * </p>
	 */
	protected void runMineSweeperTask() {
		
		World world = getBounds().getCenter().getWorld();
		if ( world != null ) {

			long start = System.currentTimeMillis();
			int blocksChanged = 0;
			
			for ( MineTargetPrisonBlock targetBlock : getMineTargetPrisonBlocks() ) {
				
				if ( targetBlock != null && !targetBlock.isAirBroke() &&
						!targetBlock.isCounted()) {
					
					MineTargetBlockKey key = targetBlock.getBlockKey();
					Location blockLocation = new Location( world, key.getX(), key.getY(), key.getZ() );
					
					Block block = world.getBlockAt( blockLocation );
					if ( block.isEmpty() ) {
						
						incrementBlockMiningCount( targetBlock );
						
						blocksChanged++;
					}
				}
			}
			

			long stop = System.currentTimeMillis();
			long elapsed = stop - start;
			
			// This ensures that the getRemainingBlockCount() is updated and is correct:
			addBlockBreakCount( blocksChanged );
			
			getStatsMineSweeperTaskMs().add( elapsed );
			
			if ( getStatsMineSweeperTaskMs().size() > 10 ) {
				getStatsMineSweeperTaskMs().remove( 0 );
			}
			
			setMineSweeperTotalMs( elapsed + getMineSweeperTotalMs() );
			setMineSweeperCount( 1 + getMineSweeperCount() );
			setMineSweeperBlocksChanged( blocksChanged + getMineSweeperBlocksChanged() );
			
			// Unlock this task so more can be submitted:
			synchronized ( MineSweeperTask.class ) {
				setMineSweeperSubmitted( false );
			}
			
			
			// Check to see if a mine reset is needed:
			checkZeroBlockReset();
		}
		
	}
	
	
	/**
	 * <p>This function, submitMineSweeperTask(), will only submit itself if the boolean 
	 * value isMineSweeperSubmitted() is not set.  This function will calculate a submission
	 * delay that will range from 2 seconds to 10 seconds.  The variation of 8 seconds is 
	 * based upon the percent remaining blocks in the mine, the less that remains, then
	 * the shorter the delay.
	 * </p>
	 * 
	 * @return if true, then the task was just submitted, if false another task is running.
	 */
	public boolean submitMineSweeperTask() {
		boolean results = false;
		
		if ( isMineSweeperEnabled() && !isMineSweeperSubmitted() ) {
			
			synchronized ( MineSweeperTask.class ) {
				
				if ( !isMineSweeperSubmitted() ) {
					
					setMineSweeperSubmitted( true );
					
					
					// calculate the refresh interval, which is actually the submission delay for 
					// running the task.  The ranks of the interval should be between 2 seconds 
					// and 15 seconds.
					int blockCount = getBounds().getTotalBlockCount();
					int blockCurrent = getRemainingBlockCount();
					double blockPercent = blockCurrent / ((double) blockCount);
					
					double taskDelaySec = 2.0d + (13.0d * blockPercent );
					
					long taskDelayTicks = (long) (taskDelaySec * 20.0d);
					
					MineSweeperTask mineSweeperTask = new MineSweeperTask(this, null);
					
					
					// Must run synchronously!!
					PrisonTaskSubmitter.runTaskLater( mineSweeperTask, taskDelayTicks );
					
					
					results = true;
					
				}
			}
		}
		
		return results;
	}
    
	public int getRemainingBlockCount() {
		int remainingBlocks = getBounds().getTotalBlockCount() - getBlockBreakCount();
//		int remainingBlocks = getBounds().getTotalBlockCount() - getAirCount();
		return remainingBlocks;
	}
	
	public double getPercentRemainingBlockCount() {
		if ( isVirtual() ) {
			return 0;
		}
		int totalCount = getBounds().getTotalBlockCount();
		int remainingCount = getRemainingBlockCount();
		double percentRemaining = (totalCount == 0d ? 0d : (remainingCount * 100d) / (double) totalCount);
		return percentRemaining;
	}
	
	/**
	 * <p>This assumes the air block count was just updated.
	 * </p>
	 * 
	 */
	public void refreshMineAsyncTask() {
		if ( isSkipResetEnabled() ) {
			
			if ( getAirCountOriginal() == getAirCount() ) {
				// No blocks mined. Skip and do not increment bypass count since mine is pristine.
				// Skip Reset!!
				return;
			}
			else if ( getPercentRemainingBlockCount() > getSkipResetPercent() ) {
				// Blocks have been mined, but not enough to reset the mine yet.
				// Increment skip bypass count.
				setSkipResetBypassCount( getSkipResetBypassCount() + 1 );
				
				if ( getSkipResetBypassCount() < getSkipResetBypassLimit() ) {
					// Skip Reset!!
					
					broadcastSkipResetMessageToAllPlayersWithRadius();
					
					return;
				}
				
				// Reached bypass limit. Must reset!
				setSkipResetBypassCount( 0 );
			}
			
			// Must reset. Not bypassing.
		}
		
		// Mine reset here:
		// Async if possible...
		
		MinePagedResetAsyncTask resetTask = new MinePagedResetAsyncTask( (Mine) this, MineResetType.normal );
		resetTask.submitTaskAsync();
		
	}
	
	
	private void constraintsApplyMin() {
		
		for ( PrisonBlockStatusData block : getPrisonBlocks() ) {
			constraintsApplyMin( block );
		}
	}
    
  
    /**
     * <p>This function attempts to add enough blocks to the target in order to meet the
     * minimum constraint requirement. There are chances that a placement will not be 
     * successful, so this only allows it to be attempted 3 times the number of blocks 
     * to be placed to prevent an endless loop.
     * </p>
     * 
     * <p>The randomly selected block to be changed can only be changed if it does not
     * have a minimum or maximum constraint value enabled (has a value of zero for both).
     * When other constraints are added, it will also have to honor the other constraints.
     * </p>
     * 
     * @param block
     * @param useNewBlockModel
     */
    private void constraintsApplyMin( PrisonBlockStatusData block )
	{
    	
	    	if ( block.getConstraintMin() > 0 && block.getBlockPlacedCount() < block.getConstraintMin() ) {
	    		
	    		int maxAttempts = (block.getConstraintMin() - block.getBlockPlacedCount()) + 3;
	    		
	    		for ( int i = 0; i < maxAttempts && block.getBlockPlacedCount() < block.getConstraintMin(); i++ ) {
	    			
	    			// Get an unmatched block in the block's range (not the same block):
	    			int blockPos = block.getRandomBlockPositionInRangeUnmatched( getMineTargetPrisonBlocks() );
	    			
	    			// Each block has a valid range in which it can spawn in the mine.  This range
	    			// is honored by using the rangeHigh and rangeLow values.
	//    			int rndPos = ((int) Math.round( Math.random() * (rangeHigh - rangeLow) )) + rangeLow;
	    			
	    			if ( blockPos > -1 && blockPos < getMineTargetPrisonBlocks().size() ) {
	    				
	    				MineTargetPrisonBlock targetBlock = getMineTargetPrisonBlocks().get( blockPos );
	    				
	    				if ( targetBlock != null && 
	    						targetBlock.getPrisonBlock().getConstraintMin() == 0 &&
	    						targetBlock.getPrisonBlock().getConstraintMax() == 0 &&
	    						!targetBlock.getPrisonBlock().getBlockName().equalsIgnoreCase( 
	    								block.getBlockName() ) ) {
	    					
	    					// decrement the block count on the block being removed:
	    					if ( targetBlock.getPrisonBlock().isAir() ) {
	    						
	    						// Need to remove one from the air count fields:
	    						setAirCountOriginal( getAirCountOriginal() - 1 );
	    						setAirCount( getAirCount() - 1 );
	    						
	    					}
	    					else {
	    						targetBlock.getPrisonBlock().decrementResetBlockCount();
	    					}
	    					
	    					
	    					// Add the new block and increment it's count:
	    					targetBlock.setPrisonBlock( block );
	    					
	    					// If the reset is placing an AiR block, then mark the block as
	    					// setAirBroke() and setMined() to ensure they are not counted or 
	    					// processed during normal mining operations, or through 
	    					// MineSweeper checks.
	    					if ( block.isAir() ) {
	    						targetBlock.setAirBroke( true );
	    						targetBlock.setMined( true );
	    					}
	    					block.incrementResetBlockCount();
	    				}
	    			}
	    		}
	    	}
	}

	/**
     * This clears the mine, then provides particle tracers around the outer corners.
	 * @param resetType 
     */
    public void enableTracer(MineResetType resetType) {
    	
	    	// First clear the mine:
	    	clearMine( resetType );

//    	Prison.get().getPlatform().enableMineTracer( 
//    				getWorldName(),
//    				getBounds().getMin(), 
//    				getBounds().getMax());

    	
    	
    }
    
    /**
     * <p>This will adjust the size of the existing mine's area.
     * Any voids left due to reductions in size will not be auto
     * filled.
     * </p>
     * 
     * @param edge
     * @param amount
     */
    public void adjustSize( Edges edge, int amount ) {
    	
	    	// First clear the mine:
	    	clearMine( MineResetType.clear );
			
	    	// if amount is zero, then just refresh the liner:
	    	
	    	if ( amount < 0 ) {
	    		while ( amount++ < 0 ) {
	    			
	    			new MineLinerBuilder( (Mine) this, edge, LinerPatterns.repair, false );
	
	    			Bounds newBounds = new Bounds( getBounds(), edge, -1 );
	    			setBounds( newBounds );
	    			
	    			new MineLinerBuilder( (Mine) this, edge, LinerPatterns.repair, false );
	    		}
	    	}
	    	else if ( amount > 0 ) {
	    		new MineLinerBuilder( (Mine) this, edge, LinerPatterns.repair, false );
	    		
	    		Bounds newBounds = new Bounds( getBounds(), edge, amount );
	    		setBounds( newBounds );
	    	}
	
	    	// Rebuild the liner if it exists:
	    	for ( Edges targtEdge : Edges.values() ) {
				
	    		if ( getLinerData().hasEdge( targtEdge ) ) {
	    			
	    			LinerPatterns pattern = LinerPatterns.fromString( getLinerData().getEdge( targtEdge ) );
	    			boolean force = getLinerData().getForce( targtEdge );
	    			
	    			new MineLinerBuilder( (Mine) this, targtEdge, pattern, force );
	    		}
		}
		
		// Finally trace the mine:
		clearMine( MineResetType.tracer );
    }
    
    
    public void moveMine( Edges edge, int amount ) {
    	
	    	MineMover moveMine = new MineMover();
	    	moveMine.moveMine( (Mine) this, edge, amount );
    }
    
    
    public void clearMine( MineResetType resetType ) {
		
	    	MineTracerBuilder tracerBuilder = new MineTracerBuilder();
	    	tracerBuilder.clearMine( (Mine) this, resetType );
    }
    

	public MineJob getCurrentJob()
	{
		return currentJob;
	}
	public void setCurrentJob( MineJob currentJob )
	{
		this.currentJob = currentJob;
	}
	
	
	
	private void addMineTargetPrisonBlock( PrisonBlockStatusData block, Location targetLocation ) {
		
		MineTargetPrisonBlock mtpb = new MineTargetPrisonBlock( block, targetLocation );
		
		synchronized ( getMineStateMutex() ) {
			
			getMineTargetPrisonBlocks().add( mtpb );
			getMineTargetPrisonBlocksMap().put( mtpb.getBlockKey(), mtpb );
		}
	}
    
    
    private void clearMineTargetPrisonBlocks() {
	    	
	    	// Instead of clearing the collections, set them to null so that way if 
	    	// other reference exist, they will be able to continue to use them
	    	// until the release the references.
	    	
	    	synchronized ( getMineStateMutex() ) {
	    		
	    		mineTargetPrisonBlocks = null;
	    		mineTargetPrisonBlocksMap = null;
	    	}
    	
    }
    
    
	public List<MineTargetPrisonBlock> getMineTargetPrisonBlocks()
	{
		if ( mineTargetPrisonBlocks == null ) {
			mineTargetPrisonBlocks = new ArrayList<>();
		}
		return mineTargetPrisonBlocks;
	}

	public TreeMap<MineTargetBlockKey, MineTargetPrisonBlock> getMineTargetPrisonBlocksMap()
	{
		if ( mineTargetPrisonBlocksMap == null ) {
			mineTargetPrisonBlocksMap = new TreeMap<>();
		}
		return mineTargetPrisonBlocksMap;
	}
	
	public MineTargetPrisonBlock getTargetPrisonBlock( PrisonBlock block ) {
		MineTargetPrisonBlock results = null;
		
		if ( block != null && block.getLocation() != null ) {
			
			Location loc = block.getLocation();
			MineTargetBlockKey key = new MineTargetBlockKey( loc );
			
			results = getMineTargetPrisonBlocksMap().get( key );
		}
		
		return results;
	}
	
	

	public int getResetPage()
	{
		return resetPage;
	}
	public void setResetPage( int resetPage )
	{
		this.resetPage = resetPage;
	}
	
	public int getResetPosition()
	{
		return resetPosition;
	}
	public void setResetPosition( int resetPosition )
	{
		this.resetPosition = resetPosition;
	}

	public long getResetPageMaxPageElapsedTimeMs() {
		if ( resetPageMaxPageElapsedTimeMs == -1 ) {
			this.resetPageMaxPageElapsedTimeMs = Prison.get().getPlatform()
										.getConfigLong( "prison-mines.reset-paging.max-page-elapsed-time-ms", 
													MINE_RESET__MAX_PAGE_ELASPSED_TIME_MS );
		}
		return resetPageMaxPageElapsedTimeMs;
	}
	public void setResetPageMaxPageElapsedTimeMs( long resetPageMaxPageElapsedTimeMs ) {
		this.resetPageMaxPageElapsedTimeMs = resetPageMaxPageElapsedTimeMs;
	}

	public long getResetPagePageSubmitDelayTicks() {
		if ( resetPagePageSubmitDelayTicks == -1 ) {
			this.resetPagePageSubmitDelayTicks = Prison.get().getPlatform()
										.getConfigLong( "prison-mines.reset-paging.page-submit-delay-ticks", 
													MINE_RESET__PAGE_SUBMIT_DELAY_TICKS );
		}
		return resetPagePageSubmitDelayTicks;
	}
	public void setResetPagePageSubmitDelayTicks( long resetPagePageSubmitDelayTicks ) {
		this.resetPagePageSubmitDelayTicks = resetPagePageSubmitDelayTicks;
	}

	public long getResetPageTimeoutCheckBlockCount() {
		if ( resetPageTimeoutCheckBlockCount == -1 ) {
			this.resetPageTimeoutCheckBlockCount = Prison.get().getPlatform()
										.getConfigLong( "prison-mines.reset-paging.page-timeout-check-block-count", 
												MINE_RESET__PAGE_TIMEOUT_CHECK__BLOCK_COUNT );
		}
		return resetPageTimeoutCheckBlockCount;
	}
	public void setResetPageTimeoutCheckBlockCount( long resetPageTimeoutCheckBlockCount ) {
		this.resetPageTimeoutCheckBlockCount = resetPageTimeoutCheckBlockCount;
	}

	public int getAirCountOriginal()
	{
		return airCountOriginal;
	}
	public void setAirCountOriginal( int airCountOriginal )
	{
		this.airCountOriginal = airCountOriginal;
	}

	public int getAirCount()
	{
		return airCount;
	}
	public void setAirCount( int airCount )
	{
		this.airCount = airCount;
	}

	public long getAirCountTimestamp()
	{
		return airCountTimestamp;
	}
	public void setAirCountTimestamp( long airCountTimestamp )
	{
		this.airCountTimestamp = airCountTimestamp;
	}

	public long getAirCountElapsedTimeMs()
	{
		return airCountElapsedTimeMs;
	}
	public void setAirCountElapsedTimeMs( long airCountElapsedTimeMs )
	{
		this.airCountElapsedTimeMs = airCountElapsedTimeMs;
	}

	public long getStatsResetTimeMS()
	{
		return statsResetTimeMS;
	}
	public void setStatsResetTimeMS( long statsResetTimeMS )
	{
		this.statsResetTimeMS = statsResetTimeMS;
	}

	public long getStatsBlockGenTimeMS()
	{
		return statsBlockGenTimeMS;
	}
	public void setStatsBlockGenTimeMS( long statsBlockGenTimeMS )
	{
		this.statsBlockGenTimeMS = statsBlockGenTimeMS;
	}

	public long getStatsBlockUpdateTimeMS()
	{
		return statsBlockUpdateTimeMS;
	}
	public void setStatsBlockUpdateTimeMS( long statsBlockUpdateTimeMS )
	{
		this.statsBlockUpdateTimeMS = statsBlockUpdateTimeMS;
	}

	public long getStatsBlockUpdateTimeNanos() {
		return statsBlockUpdateTimeNanos;
	}
	public void setStatsBlockUpdateTimeNanos( long statsBlockUpdateTimeNanos ) {
		this.statsBlockUpdateTimeNanos = statsBlockUpdateTimeNanos;
	}

	public int getStatsResetPages() {
		return statsResetPages;
	}
	public void setStatsResetPages( int statsResetPages ) {
		this.statsResetPages = statsResetPages;
	}

	public long getStatsResetPageBlocks() {
		return statsResetPageBlocks;
	}
	public void setStatsResetPageBlocks( long statsResetPageBlocks ) {
		this.statsResetPageBlocks = statsResetPageBlocks;
	}

	public long getStatsResetPageMs() {
		return statsResetPageMs;
	}
	public void setStatsResetPageMs( long statsResetPageMs ) {
		this.statsResetPageMs = statsResetPageMs;
	}

	public List<Long> getStatsMineSweeperTaskMs() {
		return statsMineSweeperTaskMs;
	}

	public boolean isMineSweeperSubmitted() {
		return mineSweeperSubmitted;
	}
	public void setMineSweeperSubmitted( boolean mineSweeperSubmitted ) {
		this.mineSweeperSubmitted = mineSweeperSubmitted;
	}
    
}

package tech.mcprison.prison.mines.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.MineScheduler.MineJob;
import tech.mcprison.prison.mines.events.MineResetEvent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

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
	public static final long MINE_RESET__MAX_PAGE_ELASPSED_TIME_MS = 100;
	
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
	public static final long MINE_RESET__PAGE_TIMEOUT_CHECK__BLOCK_COUNT = 500;
	
	
	public static final long MINE_RESET__AIR_COUNT_BASE_DELAY = 30000L; // 30 seconds
	
	@Deprecated
	private List<BlockType> randomizedBlocks;
	
	// to replace randomizedBlocks....
	private List<MineTargetBlock> mineTargetBlocks;
	private TreeMap<MineTargetBlockKey, MineTargetBlock> mineTargetBlocksMap;
	
	private int resetPage = 0;
	private int resetPosition = 0;
	
	private int airCountOriginal = 0;
	private int airCount = 0;
	private long airCountTimestamp = 0L;
	private long airCountElapsedTimeMs = 0L;
	
	
	private int blockBreakCount = 0;
	
	
//	private boolean[] mineAirBlocksOriginal;
//	private boolean[] mineAirBlocksCurrent;

	private long statsResetTimeMS = 0;
	private long statsBlockGenTimeMS = 0;
	private long statsBlockUpdateTimeMS = 0;
	private long statsTeleport1TimeMS = 0;
	private long statsTeleport2TimeMS = 0;
	private long statsMessageBroadcastTimeMS = 0;
	
	
	public MineReset() {
		super();
		
		this.randomizedBlocks = new ArrayList<>();
		
		this.mineTargetBlocks = new ArrayList<>();
		this.mineTargetBlocksMap = new TreeMap<>();
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
    	
    	// Once the mine has been loaded, MUST get a count of all air blocks.
    	refreshBlockBreakCountUponStartup();
    }
    
    /**
     * <p>Optimized the mine reset to focus on itself.  Also set the Y plane to refresh at the top and work its
     * way down.  That way if the play is teleported to the top, it will appear like the whole mine has reset
     * instantly and they will not see the delay from the bottom of the mine working up to the top.  This will
     * also reduce the likelihood of the player falling back in to the mine if there is no spawn set.
     * </p>
     * 
     * <p>The ONLY code that could be asynchronous ran is the random generation of the blocks.  The other  
     * lines of code is using bukkit and/or spigot api calls which MUST be ran synchronously.
     * </p>
     */
    protected void resetSynchonously() {

//    	if ( !isSkipResetEnabled() || 
//    		  isSkipResetEnabled() && 
//    		  getPercentRemainingBlockCount() < getSkipResetPercent() || 
//    		  isSkipResetEnabled() && 
//    		  incrementSkipResetBypassCount() > getSkipResetBypassLimit() ) {
//    		setSkipResetBypassCount(0);
    		
    		long start = System.currentTimeMillis();
    		
    		// The all-important event
    		MineResetEvent event = new MineResetEvent(this);
    		Prison.get().getEventBus().post(event);
    		if (!event.isCanceled()) {
    			resetSynchonouslyInternal();
    		}
    		
    		long stop = System.currentTimeMillis();
    		setStatsResetTimeMS( stop - start );
    		
    		// Tie to the command stats mode so it logs it if stats are enabled:
    		if ( PrisonMines.getInstance().getMineManager().isMineStats() ) {
    			DecimalFormat dFmt = new DecimalFormat("#,##0");
    			Output.get().logInfo("&cMine reset: &7" + getName() + 
    					"&c  Blocks: &7" + dFmt.format( getBounds().getTotalBlockCount() ) + 
    					statsMessage() );
    		}
//    	} else {
//    		// Reset has been skipped.  Do not log.  skipResetBypassCount was incremented.
//    	}
    	
    }

	private void resetSynchonouslyInternal() {
		try {
			// Output.get().logInfo( "MineRest.resetSynchonouslyInternal() " + getName() );

			Optional<World> worldOptional = getWorld();
			if (!worldOptional.isPresent()) {
				Output.get().logError("Could not reset mine " + getName() +
							" because the world it was created in does not exist.");
				return;
			}
			World world = worldOptional.get();
			
			// Generate new set of randomized blocks each time:  This is the ONLY thing that can be async!! ;(
			generateBlockList();
			
			setStatsTeleport1TimeMS(
					teleportAllPlayersOut( getBounds().getyBlockMax() ) );
			
			long time2 = System.currentTimeMillis();
			
			boolean isFillMode = PrisonMines.getInstance().getConfig().fillMode;

			Location altTp = alternativeTpLocation();
			altTp.setY( altTp.getBlockY() - 1 ); // Set Y one lower to 
			//boolean replaceGlassBlock = ( isFillMode && altTp.getBlockAt().getType() == BlockType.GLASS );
				
			// Reset the block break count before resetting the blocks:
			setBlockBreakCount( 0 );
			
			int i = 0;
			for (int y = getBounds().getyBlockMax(); y >= getBounds().getyBlockMin(); y--) {
//    			for (int y = getBounds().getyBlockMin(); y <= getBounds().getyBlockMax(); y++) {
				for (int x = getBounds().getxBlockMin(); x <= getBounds().getxBlockMax(); x++) {
					for (int z = getBounds().getzBlockMin(); z <= getBounds().getzBlockMax(); z++) {
						Location targetBlock = new Location(world, x, y, z);
						if (!isFillMode || 
								isFillMode && targetBlock.getBlockAt().isEmpty() ||
								isFillMode && targetBlock.equals(altTp) && altTp.getBlockAt().getType() == BlockType.GLASS ) {
							targetBlock.getBlockAt().setType(getRandomizedBlocks().get(i++));
						}
						
						if ( targetBlock.getBlockAt().getType() == BlockType.AIR ) {
							incrementBlockBreakCount();
						}
					}
				}
			}
			time2 = System.currentTimeMillis() - time2;
			setStatsBlockUpdateTimeMS( time2 );
			
			incrementResetCount();
			
			
			setSkipResetBypassCount(0);
			
			
			// If a player falls back in to the mine before it is fully done being reset, 
			// such as could happen if there is lag or a lot going on within the server, 
			// this will TP anyone out who would otherwise suffocate.  I hope! lol
			setStatsTeleport2TimeMS(
					teleportAllPlayersOut( getBounds().getyBlockMax() ) );
			
			// free up memory:
			getRandomizedBlocks().clear();
			
			// Broadcast message to all players within a certain radius of this mine:
			broadcastResetMessageToAllPlayersWithRadius( MINE_RESET__BROADCAST_RADIUS_BLOCKS );
			
		} catch (Exception e) {
			Output.get().logError("&cFailed to reset mine " + getName(), e);
		}
	}

    
    public String statsMessage() {
    	StringBuilder sb = new StringBuilder();
    	DecimalFormat dFmt = new DecimalFormat("#,##0.000");
    	
    	sb.append( "&3 Reset: &7" );
    	sb.append( dFmt.format(getStatsResetTimeMS() / 1000.0d ));
    	
    	sb.append( "&3 BlockGen: &7" );
    	sb.append( dFmt.format(getStatsBlockGenTimeMS() / 1000.0d ));
    	
    	sb.append( "&3 TP1: &7" );
    	sb.append( dFmt.format(getStatsTeleport1TimeMS() / 1000.0d ));

    	sb.append( "&3 BlockUpdate: &7" );
    	sb.append( dFmt.format(getStatsBlockUpdateTimeMS() / 1000.0d ));
    	
    	sb.append( "&3 TP2: &7" );
    	sb.append( dFmt.format(getStatsTeleport2TimeMS() / 1000.0d ));
    	
    	sb.append( "&3 MsgBroadcast: &7" );
    	sb.append( dFmt.format(getStatsMessageBroadcastTimeMS() / 1000.0d ));
    	
    	return sb.toString();
    }

    private void resetStats() {
    	setResetPage( 0 ); 
    	
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
    	setStatsTeleport1TimeMS( 0 );
    	setStatsTeleport2TimeMS( 0 );
    	setStatsMessageBroadcastTimeMS( 0 );
    }
	
    /**
     * <p>This function teleports players out of existing mines if they are within 
     * their boundaries within the world where the Mine exists.</p>
     * 
     * <p>Using only players within the existing world of the current mine, each
     * player is checked to see if they are within the mine, and if they are they
     * are teleported either to the mine's spawn location, or straight up from the
     * center of the mine, to the top of the mine (assumes air space will exist there).</p>
     * 
     * <p>This function eliminates possible bug of players being teleported from other
     * worlds, and also eliminates the possibility that the destination could
     * ever be null.</p>
     * 
     * @param world - world 
     * @param targetY
     */
    private long teleportAllPlayersOut(int targetY) {
    	long start = System.currentTimeMillis();
    	
    	World world = getBounds().getCenter().getWorld();

    	List<Player> players = (world.getPlayers() != null ? world.getPlayers() : 
    							Prison.get().getPlatform().getOnlinePlayers());
    	for (Player player : players) {
            if ( getBounds().within(player.getLocation()) ) {
            	
            	teleportPlayerOut(player);
            }
        }
    	
    	return System.currentTimeMillis() - start;
    }
    
    /**
     * <p>This function will teleport the player out of a given mine, or to the given
     * mine. It will not confirm if the player is within the mine before trying to 
     * teleport.
     * </p>
     * 
     * <p>This function will teleport the player to the defined spawn location, or it
     * will teleport the player to the center of the mine, but on top of the
     * mine's surface.</p>
     * 
     * <p>If the player target location has an empty block under its feet, it will 
     * then spawn in a single glass block so the player will not take fall damage.
     * If that block is within the mine, it will be reset at a later time when the
     * mine resets and resets that block.  If it is part of spawn for the mine, then
     * the glass block will become part of the landscape.
     * <p>
     * 
     * <p>Do not show any TP notifications.  It will be obvious that the mine
     * just reset and that they were teleported out of the mine.  Since there is no
     * control over this message, like enabling or disabling, then I'm just 
     * removing it since it just clutters chat and provides no real additional 
     * value.
     * </p>
     * 
     * @param player
     */
    public void teleportPlayerOut(Player player) {
    	Location altTp = alternativeTpLocation();
    	Location target = isHasSpawn() ? getSpawn() : altTp;
    	
    	// Player needs to stand on something.  If block below feet is air, change it to a 
    	// glass block:
    	Location targetGround = new Location( target );
    	targetGround.setY( target.getBlockY() - 1 );
    	if ( targetGround.getBlockAt().isEmpty() ) {
    		targetGround.getBlockAt().setType( BlockType.GLASS );
    	}
    	
    	player.teleport( target );
//    	PrisonMines.getInstance().getMinesMessages().getLocalizable("teleported")
//    			.withReplacements(this.getName()).sendTo(player);
    }


	private Location alternativeTpLocation()
	{
		Location altTp = new Location( getBounds().getCenter() );
    	altTp.setY( getBounds().getyBlockMax() + 1 );
		return altTp;
	}
	
	public int getPlayerCount() {
		int count = 0;
		
    	World world = getBounds().getCenter().getWorld();

    	List<Player> players = (world.getPlayers() != null ? world.getPlayers() : 
    							Prison.get().getPlatform().getOnlinePlayers());
    	for (Player player : players) {
            if ( getBounds().within(player.getLocation()) ) {
            	count++;
            }
        }
		
		return count;
	}
    
//    /**
//     * <p>This is a temporary fix until the Bounds.within() checks for the
//     * same world.  For now, it is assumed that Bounds.min and Bounds.max are 
//     * the same world, but that may not always be the case.</p>
//     * 
//     * @param w1 First world to compare to
//     * @param w2 Second world to compare to
//     * @return true if they are the same world
//     */
//    private boolean isSameWorld(World w1, World w2) {
//    	// TODO Need to fix Bounds.within() to test for same worlds:
//    	return w1 == null && w2 == null ||
//    			w1 != null && w2 != null &&
//    			w1.getName().equalsIgnoreCase(w2.getName());
//    }

    /**
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
    protected void generateBlockListAsync() {
    	
		long start = System.currentTimeMillis();
		
		// Reset stats:
		resetStats();
		
		Random random = new Random();
		
		// Clear the mineTargetBlocks list:
		getMineTargetBlocks().clear();
		
//		// Reset the mineAirBlocks to all false values:
//		boolean[] mAirBlocks = new boolean[ getBounds().getTotalBlockCount() ];
//		// Arrays.fill(  mAirBlocks, false ); // redundant but prevents nulls if were Boolean
//		setMineAirBlocksOriginal( mAirBlocks );
		
		int airCount = 0;
		
		for (int y = getBounds().getyBlockMax(); y >= getBounds().getyBlockMin(); y--) {
			for (int x = getBounds().getxBlockMin(); x <= getBounds().getxBlockMax(); x++) {
				for (int z = getBounds().getzBlockMin(); z <= getBounds().getzBlockMax(); z++) {
					
					BlockType blockType = randomlySelectBlock( random );
					
					MineTargetBlock mtb = new MineTargetBlock( blockType, x, y, z);
					
					getMineTargetBlocks().add( mtb );
					getMineTargetBlocksMap().put( mtb.getBlockKey(), mtb );
					
					if ( blockType == BlockType.AIR ) {
//						mAirBlocks[i++] = true;
						airCount++;
					}
				}
			}
		}
		
		setAirCountOriginal( airCount );
		setAirCount( airCount );
		
		// The reset position is critical in ensuring that all blocks within the mine are reset 
		// and that when a reset process pages (allows another process to run) then it will be
		// used to pick up where it left off.
		setResetPosition( 0 );
		
		long stop = System.currentTimeMillis();
		setStatsBlockGenTimeMS( stop - start );
		
    }
    
    /**
     * <p>Yeah I know, it has async in the name of the function, but it still can only
     * be ran synchronously.  The async part implies this is the reset "part" for the
     * async workflow.
     * </p>
     * 
     * <p>Before this part is ran, the resetSynchonously() function must be ran
     * to regenerate the new block list.
     * </p>
     *  
     */
    protected void resetAsynchonously() {
    	boolean canceled = false;
    	
//		Output.get().logInfo( "MineRest.resetAsynchonously() " + getName() );

    	
    	if ( getResetPage() == 0 ) {
    		canceled = resetAsynchonouslyInitiate();
    	}
    	
    	if ( !canceled ) {
    		
    		 if ( getResetPosition() == 0 ) {
    			// Reset the block break count before resetting the blocks:
    			// Set it to the original air count, if subtracted from total block count
    			// in the mine, then the result will be blocks remaining.
         		setBlockBreakCount( getAirCountOriginal() );
    		 }
    		
    		resetAsynchonouslyUpdate();
    		
    		if ( getResetPosition() == getMineTargetBlocks().size() ) {
    			// Done resetting the mine... wrap up:
    			
           		
        		// If a player falls back in to the mine before it is fully done being reset, 
        		// such as could happen if there is lag or a lot going on within the server, 
        		// this will TP anyone out who would otherwise suffocate.  I hope! lol
        		setStatsTeleport2TimeMS(
        				teleportAllPlayersOut( getBounds().getyBlockMax() ) );
        		
        		incrementResetCount();
        		
        		// Broadcast message to all players within a certain radius of this mine:
        		broadcastResetMessageToAllPlayersWithRadius( MINE_RESET__BROADCAST_RADIUS_BLOCKS );

                
                // Tie to the command stats mode so it logs it if stats are enabled:
                if ( PrisonMines.getInstance().getMineManager().isMineStats() ) {
                	DecimalFormat dFmt = new DecimalFormat("#,##0");
                	Output.get().logInfo("&cMine reset: &7" + getName() + 
                			"&c  Blocks: &7" + dFmt.format( getBounds().getTotalBlockCount() ) + 
                			statsMessage() );
                }
    		} else {
    			//TODO resubmit... 
    			
    			
//    			submitSyncTask( callbackSync );
    			
    		}
    	}
    	
    	// NOTE: blocks already generated in generateBlockListAsync():
    	
//    	long start = System.currentTimeMillis();
//    	
//        // The all-important event
//        MineResetEvent event = new MineResetEvent(this);
//        Prison.get().getEventBus().post(event);
//        if (!event.isCanceled()) {
//        	
//        	try {
//        		Optional<World> worldOptional = getWorld();
//        		if (!worldOptional.isPresent()) {
//        			Output.get().logError("Could not reset mine " + getName() +
//        						" because the world it was created in does not exist.");
//        			return;
//        		}
//        		//World world = worldOptional.get();
//        		
//        		setStatsTeleport1TimeMS(
//        				teleportAllPlayersOut( getBounds().getyBlockMax() ) );
//        		
//        		
//        		resetAsynchonouslyUpdate();
//        		
//        		
//        		// If a player falls back in to the mine before it is fully done being reset, 
//        		// such as could happen if there is lag or a lot going on within the server, 
//        		// this will TP anyone out who would otherwise suffocate.  I hope! lol
//        		setStatsTeleport2TimeMS(
//        				teleportAllPlayersOut( getBounds().getyBlockMax() ) );
//        		
//
//        		
//        		// Broadcast message to all players within a certain radius of this mine:
//        		broadcastResetMessageToAllPlayersWithRadius( MINE_RESET_BROADCAST_RADIUS_BLOCKS );
//        		
//        	} catch (Exception e) {
//        		Output.get().logError("&cFailed to reset mine " + getName(), e);
//        	}
//        }
//        
//        long stop = System.currentTimeMillis();
//        setStatsResetTimeMS( stop - start );
        
//        // Tie to the command stats mode so it logs it if stats are enabled:
//        if ( PrisonMines.getInstance().getMineManager().isMineStats() ) {
//        	DecimalFormat dFmt = new DecimalFormat("#,##0");
//        	Output.get().logInfo("&cMine reset: &7" + getName() + 
//        			"&c  Blocks: &7" + dFmt.format( getBounds().getTotalBlockCount() ) + 
//        			statsMessage() );
//        }
    }

    private boolean resetAsynchonouslyInitiate() {
    	boolean canceled = false;
    	
    	long start = System.currentTimeMillis();
    	
        // The all-important event
        MineResetEvent event = new MineResetEvent(this);
        Prison.get().getEventBus().post(event);
        
        canceled = event.isCanceled();
        if (!canceled) {
        	
        	try {
        		Optional<World> worldOptional = getWorld();
        		if (!worldOptional.isPresent()) {
        			Output.get().logError("Could not reset mine " + getName() +
        						" because the world it was created in does not exist.");
        			canceled = true;
        		} else {
        			
        			setStatsTeleport1TimeMS(
        					teleportAllPlayersOut( getBounds().getyBlockMax() ) );
        		}
        		
        	} catch (Exception e) {
        		Output.get().logError("&cFailed to reset mine " + getName(), e);
        		canceled = true;
        	}
        }
        
        long stop = System.currentTimeMillis();
        setStatsResetTimeMS( stop - start );
        
    	return canceled;
    }
    

    /**
     * <p>This is the synchronous part of the job that actually updates the blocks.
     * It will only replace what it can within the given allocated milliseconds,
     * then it will terminate and allow this process to re-run, picking up where it
     * left off.
     * </p>
     * 
     * <p>Paging is what this is doing. Running, and doing what it can within it's 
     * limited amount of time, then yielding to any other task, then resuming later.
     * This is a way of running a massive synchronous task, without hogging all the
     * resources and killing the TPS.
     * </p>
     * 
     * <p>NOTE: The values for MINE_RESET__PAGE_TIMEOUT_CHECK__BLOCK_COUNT and for
     * MINE_RESET__MAX_PAGE_ELASPSED_TIME_MS are set to arbitrary values and may not
     * be the correct values.  They may be too large and may have to be adjusted to 
     * smaller values to better tune the process.
     * </p>
     *  
     */
    private void resetAsynchonouslyUpdate() {
		World world = getBounds().getCenter().getWorld();
		
//		Output.get().logInfo( "MineRest.resetAsynchonouslyUpdate() " + getName() + " resetPage= " + getResetPage() );
		
//		setStatsTeleport1TimeMS(
//				teleportAllPlayersOut( getBounds().getyBlockMax() ) );
		
		long start = System.currentTimeMillis();
		
		boolean isFillMode = PrisonMines.getInstance().getConfig().fillMode;
		
		int i = getResetPosition();
		for ( ; i < getMineTargetBlocks().size(); i++ )
		{
			MineTargetBlock target = getMineTargetBlocks().get(i);
			
			Location targetBlock = new Location(world, 
					target.getBlockKey().getX(), target.getBlockKey().getY(), 
					target.getBlockKey().getZ());
			
			if (!isFillMode || isFillMode && targetBlock.getBlockAt().isEmpty()) {
				targetBlock.getBlockAt().setType(target.getBlockType());
			} 
			
			/**
			 * About every 500 blocks, check to see if the current wall time spent is greater than
			 * the threshold.  If it is greater, then end the update and let it resubmit.  
			 * It does not matter how many blocks were actually updated during this "page", 
			 * but it is more important the actual elapsed time.  This is to allow other
			 * processes to get processing time and to eliminate possible lagging.
			 */
			if ( i % MINE_RESET__PAGE_TIMEOUT_CHECK__BLOCK_COUNT == 0 ) {
				long elapsed = start = System.currentTimeMillis();
				if ( elapsed > MINE_RESET__MAX_PAGE_ELASPSED_TIME_MS ) {
					break;
				}
			}
		}
		setResetPosition( i );
		
		setResetPage( getResetPage() + 1 ); 
		
		long time = System.currentTimeMillis() - start;
		setStatsBlockUpdateTimeMS( time + getStatsBlockUpdateTimeMS() );
		setStatsResetTimeMS( time + getStatsResetTimeMS() );

    }
    
    /**
     * This should be used to submit async tasks.
     * 
     * @param callbackAsync
     */
    public void submitAsyncTask( PrisonRunnable callbackAsync ) {
    	Prison.get().getPlatform().getScheduler().runTaskLaterAsync( callbackAsync, 0L );
    }
    
    public void submitSyncTask( PrisonRunnable callbackSync ) {
    	Prison.get().getPlatform().getScheduler().runTaskLater( callbackSync, 0L );
    }

//    /**
//     * <p>This function will identify how many air blocks are within a mine.
//     * </p>
//     */
//    private void refreshAirCount() {
//    	refreshAirCount(null);
//    }
    
//    private void refreshAirCount(PrisonRunnable callback) {
//    	
//    	long elapsedTarget = getAirCountTimestamp() + MINE_RESET__AIR_COUNT_BASE_DELAY + 
//    			(getAirCountElapsedTimeMs() * MINE_RESET__AIR_COUNT_BASE_DELAY / 100);
//    	
//    	if ( getAirCountTimestamp() == 0L ||
//    			(elapsedTarget <= System.currentTimeMillis() )) {
//    		
//    		MineCountAirBlocksAsyncTask cabAsyncTask = new MineCountAirBlocksAsyncTask(this, callback);
//    		
//    		submitSyncTask( cabAsyncTask );
//    		
//    		// Cannot run this async
//    		//submitAsyncTask( cabAsyncTask );
//    		
////    		Prison.get().getPlatform().getScheduler().runTaskLaterAsync( cabAsyncTask, 0L );
//    		
//    		// Do not run this here, it must be ran as an async task... 
////    		refreshAirCountAsyncTask();
//    	}
//    }

    /**
     * This task should be ran upon loading of the mines upon server start.  
     * This will calculate how many air blocks there are within the mine, which
     * will be what the blockBreakCount should be set to initially. 
     * 
     * But as a warning, this may trigger stack traces if there are active
     * entities in the unloaded chunks.  May have run this synchronously. :(
     */
    public void refreshBlockBreakCountUponStartup() {
    	
    	OnStartupRefreshBlockBreakCountAsyncTask cabAsyncTask = new OnStartupRefreshBlockBreakCountAsyncTask(this);
    	
    	// Must run synchronously!!
    	submitSyncTask( cabAsyncTask );
    	//submitAsyncTask( cabAsyncTask );
    }
    
    /**
     * <p>This function performs the air count and should be ran as an async task.
     * </p>
     * 
     * <p>WARNING: This generates a ton of async failures upon startup or during other
     * times.  This MUST be ran synchronously...
     * </p>
     * 
     */
	protected void refreshAirCountAsyncTask()
	{
		long start = System.currentTimeMillis();
		Optional<World> worldOptional = getWorld();
		if (!worldOptional.isPresent()) {
			Output.get().logError("Could not count mine " + getName() +
					" air blocks because the world it was created in does not exist.");
		} else {
			
			World world = worldOptional.get();
			
			
			int airCount = 0;
			int errorCount = 0;
			StringBuilder sb = new StringBuilder();
			
			for (int y = getBounds().getyBlockMax(); y >= getBounds().getyBlockMin(); y--) {
				for (int x = getBounds().getxBlockMin(); x <= getBounds().getxBlockMax(); x++) {
					for (int z = getBounds().getzBlockMin(); z <= getBounds().getzBlockMax(); z++) {
						
						try {
							Location targetBlock = new Location(world, x, y, z);
							if ( targetBlock.getBlockAt().getType() == BlockType.AIR ) {
								airCount++;
							}
						}
						catch ( Exception e ) {
							// Updates to the "world" should never be ran async.  Upon review of the above 
							// that gets the location and block, causes the chunk to load, if it is not loaded,
							// and if there is an entity in that loaded chunk it will throw an exception:
							//     java.lang.IllegalStateException: Asynchronous entity world add!
							// If there are no entities, it will be fine, but they could cause issues with async 
							// access of unloaded chunks.
							String coords = String.format( "%d.%d.%d ", x, y, z );
							if ( errorCount ++ == 0 ) {
								String message = String.format( 
										"MineReset.refreshAirCountAsyncTask: Error counting air blocks: " +
										"Mine=%s coords=%s  Error: %s ", getName(), coords, e.getMessage() );
								if ( e.getMessage().contains( "Asynchronous entity world add" )) {
									Output.get().logWarn( message );
								} else {
									Output.get().logWarn( message, e );
								}
								
							} 
							else if ( errorCount <= 20 ) {
								sb.append( coords );
							}
						}
					}
				}
			}
			
			if ( errorCount > 0 ) {
				String message = String.format( 
						"MineReset.refreshAirCountAsyncTask: Error counting air blocks: Mine=%s: " +
								"errorCount=%d  blocks%s : %s", getName(), errorCount,
								(errorCount > 20 ? "(first 20)" : ""),
								sb.toString() );
				Output.get().logWarn( message );
			}
			
			
			setAirCount( airCount );
			
			long stop = System.currentTimeMillis();
			long elapsed = stop - start;
			setAirCountElapsedTimeMs( elapsed );
			setAirCountTimestamp( stop );
		}
	}
    
	public int getRemainingBlockCount() {
		int remainingBlocks = getBounds().getTotalBlockCount() - getBlockBreakCount();
//		int remainingBlocks = getBounds().getTotalBlockCount() - getAirCount();
		return remainingBlocks;
	}
	
	public double getPercentRemainingBlockCount() {
		int totalCount = getBounds().getTotalBlockCount();
		int remainingCount = getRemainingBlockCount();
		double percentRemaining = (totalCount == 0d ? 0d : (remainingCount * 100d) / (double) totalCount);
//		double remainingBlocksP = (totalCount - getAirCount()) * 100d;
//		double originalCount = totalCount - getAirCountOriginal();
//		double percentRemaining = (originalCount == 0d ? 0d : remainingBlocksP / originalCount);
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
					return;
				}
				
				// Reached bypass limit. Must reset!
				setSkipResetBypassCount( 0 );
			}
			
			// Must reset. Not bypassing.
		}
		
		// Mine reset here:
		// Async if possible...
	}
	
    
    /**
     * Generates blocks for the specified mine and caches the result.
     * 
     * The random chance is now calculated upon a double instead of integer.
     *
     * @param mine the mine to randomize
     */
    private void generateBlockList() {
    	long start = System.currentTimeMillis();
    	
        Random random = new Random();
        
        getRandomizedBlocks().clear();
        
        for (int i = 0; i < getBounds().getTotalBlockCount(); i++) {
        	BlockType blockType = randomlySelectBlock( random );
            getRandomizedBlocks().add(blockType);
        }
        long stop = System.currentTimeMillis();
        
        setStatsBlockGenTimeMS( stop - start );
        
//        Output.get().logInfo("&cMine reset: " + getName() + " generated " + getBounds().getTotalBlockCount() + 
//        		" blocks in " + getStatsBlockGenTimeMS() + " ms");
    }


	private BlockType randomlySelectBlock( Random random )
	{
		double chance = random.nextDouble() * 100.0d;
		
		BlockType value = BlockType.AIR;
		for (Block block : getBlocks()) {
		    if (chance <= block.getChance()) {
		        value = block.getType();
		        break;
		    } else {
		        chance -= block.getChance();
		    }
		}
		return value;
	}
    
    
    private void broadcastResetMessageToAllPlayersWithRadius(long radius) {
    	long start = System.currentTimeMillis();
    	
    	if ( getNotificationMode() != MineNotificationMode.disabled ) {
    		World world = getBounds().getCenter().getWorld();
    		
    		List<Player> players = (world.getPlayers() != null ? world.getPlayers() : 
    			Prison.get().getPlatform().getOnlinePlayers());
    		for (Player player : players) {
    			
    			// Check for either mode: Within the mine, or by radius from mines center:
    			if ( getNotificationMode() == MineNotificationMode.within && 
						getBounds().within(player.getLocation() ) ||
					getNotificationMode() == MineNotificationMode.radius && 
    					getBounds().within(player.getLocation(), radius) ) {
    				
            		PrisonMines.getInstance().getMinesMessages()
		                .getLocalizable("reset_message").withReplacements( getName() )
		                .sendTo(player);
    				
//    				player.sendMessage( "The mine " + getName() + " has just reset." );
    			}
    		}
    	}
    	
        long stop = System.currentTimeMillis();
        
        setStatsMessageBroadcastTimeMS( stop - start );
    }
    
    protected void broadcastPendingResetMessageToAllPlayersWithRadius(MineJob mineJob, long radius) {
    	if ( getNotificationMode() != MineNotificationMode.disabled ) {
    		World world = getBounds().getCenter().getWorld();
    		List<Player> players = (world.getPlayers() != null ? world.getPlayers() : 
    			Prison.get().getPlatform().getOnlinePlayers());
    		for (Player player : players) {
    			// Check for either mode: Within the mine, or by radius from mines center:
    			if ( getNotificationMode() == MineNotificationMode.within && 
						getBounds().within(player.getLocation() ) ||
					getNotificationMode() == MineNotificationMode.radius && 
    					getBounds().within(player.getLocation(), radius) ) {
    				
            		PrisonMines.getInstance().getMinesMessages()
		                .getLocalizable("reset_warning")
		                .withReplacements( getName(), 
		                		Text.getTimeUntilString(Math.round(mineJob.getResetInSec() * 1000.0d)) )
		                .sendTo(player);
            	
//    				player.sendMessage( "The mine " + getName() + " will reset in " + 
//    						Text.getTimeUntilString(mineJob.getResetInSec() * 1000) );
    				
    			}
    		}
    	}
    }

	@Deprecated
	public List<BlockType> getRandomizedBlocks()
	{
		return randomizedBlocks;
	}
    @Deprecated
	public void setRandomizedBlocks( List<BlockType> randomizedBlocks )
	{
		this.randomizedBlocks = randomizedBlocks;
	}

	public List<MineTargetBlock> getMineTargetBlocks()
	{
		return mineTargetBlocks;
	}
	public void setMineTargetBlocks( List<MineTargetBlock> mineTargetBlocks )
	{
		this.mineTargetBlocks = mineTargetBlocks;
	}

	public TreeMap<MineTargetBlockKey, MineTargetBlock> getMineTargetBlocksMap()
	{
		return mineTargetBlocksMap;
	}
	public void setMineTargetBlocksMap( TreeMap<MineTargetBlockKey, MineTargetBlock> mineTargetBlocksMap )
	{
		this.mineTargetBlocksMap = mineTargetBlocksMap;
	}
	

//	public boolean[] getMineAirBlocksOriginal()
//	{
//		return mineAirBlocksOriginal;
//	}
//	public void setMineAirBlocksOriginal( boolean[] mineAirBlocksOriginal )
//	{
//		this.mineAirBlocksOriginal = mineAirBlocksOriginal;
//	}
//
//	public boolean[] getMineAirBlocksCurrent()
//	{
//		return mineAirBlocksCurrent;
//	}
//	public void setMineAirBlocksCurrent( boolean[] mineAirBlocksCurrent )
//	{
//		this.mineAirBlocksCurrent = mineAirBlocksCurrent;
//	}

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

	public int incrementBlockBreakCount() {
		return ++blockBreakCount;
	}
	public int getBlockBreakCount() {
		return blockBreakCount;
	}
	public void setBlockBreakCount( int blockBreakCount ) {
		this.blockBreakCount = blockBreakCount;
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

	public long getStatsTeleport1TimeMS()
	{
		return statsTeleport1TimeMS;
	}
	public void setStatsTeleport1TimeMS( long statsTeleport1TimeMS )
	{
		this.statsTeleport1TimeMS = statsTeleport1TimeMS;
	}

	public long getStatsTeleport2TimeMS()
	{
		return statsTeleport2TimeMS;
	}
	public void setStatsTeleport2TimeMS( long statsTeleport2TimeMS )
	{
		this.statsTeleport2TimeMS = statsTeleport2TimeMS;
	}

	public long getStatsMessageBroadcastTimeMS()
	{
		return statsMessageBroadcastTimeMS;
	}
	public void setStatsMessageBroadcastTimeMS( long statsMessageBroadcastTimeMS )
	{
		this.statsMessageBroadcastTimeMS = statsMessageBroadcastTimeMS;
	}
    
}

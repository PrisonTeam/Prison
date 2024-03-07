package tech.mcprison.prison.mines.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.features.MineBlockEvent;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.mines.tasks.MinePagedResetAsyncTask;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonCommandTaskData;
import tech.mcprison.prison.tasks.PrisonCommandTaskData.CustomPlaceholders;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.tasks.PrisonCommandTasks;
import tech.mcprison.prison.util.Location;

public abstract class MineScheduler
		extends MineTasks
		implements PrisonRunnable
{
	
	/**
	 * <p>The jobWorkflow defines the various steps to the workflow that 
	 * is related to the mine reset process.  There will always be a RESET
	 * action and it will always be the last item in the workflow.  If there
	 * are other steps in the workflow they will be MESSAGEs that are to
	 * be sent out a different intervals.
	 * </p>
	 * 
	 * <p>The jobWorkflow is built only one time and it regenerates the jobStack
	 * once a workflow cycle has been completed.
	 * </p>
	 */
	private List<MineJob> jobWorkflow;
	private Stack<MineJob> jobStack;
//	private MineJob currentJob;
	private Integer taskId = null;
	
	private transient long mineResetStartTimestamp;
	
	
	public MineScheduler() {
		super();
		
		this.jobWorkflow = new ArrayList<>();
		this.jobStack = new Stack<>();

		this.mineResetStartTimestamp = -1;
	}

    /**
     * <p>This initialize function gets called after the classes are
     * instantiated, and is initiated from Mine class and propagates
     * to the MineData class.  Good for kicking off the scheduler.
     * </p>
     */
	@Override
	protected void initialize() {
    	super.initialize();
    	
    	// need to rebuild JobWorkflow if reset time ever changes:
    	setJobWorkflow( initializeJobWorkflow() );
    	resetJobStack();
    }
	
	public enum JobType {
		SYNC,
		ASYNC
		;
	}
	
	public enum MineJobAction {
		MESSAGE_GATHER( JobType.ASYNC ),
		MESSAGE( JobType.SYNC ),
		
		RESET_BUILD_BLOCKS_ASYNC( JobType.ASYNC ),
		RESET_ASYNC( JobType.SYNC ),
		RESET_SYNC( JobType.SYNC );
		
		private final JobType jobType;
		private MineJobAction(JobType jobType) {
			this.jobType = jobType;
		}

		public JobType getJobType()
		{
			return jobType;
		}
	}
	
	public enum MineResetScheduleType {
		NORMAL,
		ZERO_BLOCK_RESET,
		FORCED;
	}
	
	public enum MineResetActions {
		NO_COMMANDS,
		CHAINED_RESETS,
		DETAILS
		;
	}
	
	/**
	 * <p>This class represents a workflow action.  The action can be one of either MESSAGE, or
	 * RESET.  The delayActionSec is how many seconds the job must wait until taking action.
	 * When it does take action, such as sending of a message, the resetInSec should be how
	 * many seconds in to the future will be the reset.
	 * </p>
	 *
	 * <p>Please note that the value of resetInSec is the time from when the job starts to run
	 * until the mine should reset.  To find out when the mine should reset when submitting
	 * this job, add both resetInSec and delayActionSec to get the estimate reset time.
	 * </p>
	 * 
	 */
	public class MineJob 
	{
		private MineJobAction action;
		private double delayActionSec;
		private double resetInSec;
		private MineResetScheduleType resetType;
		private List<MineResetActions> resetActions;
		
		public MineJob( MineJobAction action, double delayActionSec, double resetInSec )
		{
			super();
			
			this.action = action;
			this.delayActionSec = delayActionSec;
			this.resetInSec = resetInSec;
			
			this.resetType = MineResetScheduleType.NORMAL;
			
			this.resetActions = new ArrayList<>();
		}
		
		public MineJob( MineJobAction action, double delayActionSec, double resetInSec, MineResetScheduleType resetType )
		{
			this( action, delayActionSec, resetInSec );
			
			this.resetType = resetType;
		}
		
		public double getJobSubmitResetInSec() {
			return getResetInSec() + getDelayActionSec();
		}
		
		@Override
		public String toString() {
			StringBuilder ra = new StringBuilder();
			for ( MineResetActions resetAction : getResetActions() ) {
				ra.append( resetAction.name() ).append( " " );
			}
			
			return "Action: " + getAction().name() + 
					"  Reset at submit: " + getJobSubmitResetInSec() +
					"  Delay before running: " + getDelayActionSec() + 
					"  Reset at run: " + getResetInSec() +
					"  ResetType: " + getResetType().name() + 
					"  ResetActions: " + ra.toString();
		}

		public MineJobAction getAction() {
			return action;
		}
		public void setAction( MineJobAction action ) {
			this.action = action;
		}

		public double getDelayActionSec() {
			return delayActionSec;
		}
		public void setDelayActionSec( double delayActionSec ) {
			this.delayActionSec = delayActionSec;
		}

		public double getResetInSec() {
			return resetInSec;
		}
		public void setResetInSec( double resetInSec ) {
			this.resetInSec = resetInSec;
		}

		public MineResetScheduleType getResetType() {
			return resetType;
		}
		public void setResetType( MineResetScheduleType resetType ) {
			this.resetType = resetType;
		}

		public List<MineResetActions> getResetActions() {
			return resetActions;
		}
		public void setResetActions( List<MineResetActions> resetActions ) {
			this.resetActions = resetActions;
		}

	}
	


	
	private List<MineJob> initializeJobWorkflow()
	{
		if ( PrisonMines.getInstance() != null ) {
			MinesConfig config = PrisonMines.getInstance().getConfig();
			return initializeJobWorkflow(getResetTime(), config.resetMessages, config.resetWarningTimes );
		}
		
		// The following is used within jUnit tests:
		return new ArrayList<>();
	}
	
	/**
	 * <p>Added parameters so this can be unit tested to ensure it's working correctly.
	 * </p>
	 * 
	 * <p>With reset warning times of: 59, 121, and 313.  And a resetTime of 10 minutes, 
	 * then the following is expected with all of them adding up to 15 minutes: 
	 * </p>
	 * <ul>
	 *   <li>MESSAGE, 287 (600 until reset)</li>
	 *   <li>MESSAGE, 192 (313 until reset)</li>
	 *   <li>MESSAGE, 62 (121 until reset)</li>
	 *   <li>RESET, 59 (59 until reset)</li>
	 * </ul>
	 * 
	 * @param resetTime
	 * @param includeMessages
	 * @param rwTimes
	 * @return
	 */
	protected List<MineJob> initializeJobWorkflow( double resetTime, boolean includeMessages, ArrayList<Integer> rwTimes )
	{
		double targetResetTime = resetTime;
		
		
		List<MineJob> workflow = new ArrayList<>();
		ArrayList<Integer> resetWarningTimes = new ArrayList<>();
		
		// The resetWarningTimes can have a null value at the end if the json is setup with something like this:
		//  "resetWarningTimes": [ 600, 300, 60, ]
		// notice the comma after the 60 and no other number following that.  That happened to be a mistake
		// that was made by a user, and resulted in all mines filing to load.
		for ( Integer time : rwTimes ) {
			if ( time != null && time.intValue() > 0 ) {
				resetWarningTimes.add( time );
			}
		}
		if ( resetWarningTimes.size() == 0 ) {
			// they probably assume they never want a reset warning message so set the resetWarningTimes to one year:
			int oneYear = 360 * 24 * 60 * 60;
			resetWarningTimes.add( Integer.valueOf( oneYear ) );
		}
		
		
		// if the mine is virtual, set the resetTime to four hours.  It won't reset, but it will stay active
		// in the workflow:
		if ( isVirtual() || resetTime <= 0 ) {
			targetResetTime = 60 * 60 * 4; // one hour * 4
		}
		
		// Should always NOW use async reset action for this workflow.
		// Determine if the sync or async reset action should be used for this workflow.
		MineJobAction resetAction = MineJobAction.RESET_ASYNC; // : MineJobAction.RESET_SYNC;
		
		if ( includeMessages ) {
			// Need to ensure that the reset warning times are sorted in ascending order:
//			ArrayList<Integer> rwTimes = PrisonMines.getInstance().getConfig().resetWarningTimes;
			Collections.sort( resetWarningTimes );
			
			double total = 0;
			for ( Integer time : resetWarningTimes ) {
				if ( time < targetResetTime ) {
					// if reset time is less than warning time, then skip warning:
					double elapsed = time - total;
					workflow.add( 
							new MineJob( workflow.size() == 0 ? resetAction : MineJobAction.MESSAGE, 
									elapsed, total) );
					total += elapsed;
				}
			}
			workflow.add( 
					new MineJob( workflow.size() == 0 ? resetAction : MineJobAction.MESSAGE, 
							(targetResetTime - total), total) );
			
		} else {
			// Exclude all messages. Only reset mine:
			workflow.add( new MineJob( resetAction, targetResetTime, 0) );
		}
		
		return workflow;
	}
	

	/**
	 * <p>Reset the job stack, and if the reset time has changed, then rebuild the
	 * whole workflow to account for the new reset time.
	 * </p>
	 */
	private void resetJobStack() {
		getJobStack().clear();
		
		double oldResetTime = getJobWorkflow() == null || getJobWorkflow().size() == 0 ? 0.0d :
								getJobWorkflow().get( 0 ).getJobSubmitResetInSec();
		
		if ( oldResetTime == 0.0d || oldResetTime != getResetTime() ) {
			// need to rebuild JobWorkflow if reset time ever changes:
			setJobWorkflow( initializeJobWorkflow() );
		}
		
		getJobStack().addAll( getJobWorkflow() );
	}
	
	/**
	 * Calculate if the reset should be skipped. If it should, increment the skip count
 	 * at the end, and skip the generation of the next block list and the actual resets.
	 */
	@Override
	public void run()
	{
		// TODO track how many times the world fails to load? Then terminate the mine job if it
		// appears like it will never load?
		//checkWorld();
		
		MineResetScheduleType resetScheduleType = getCurrentJob() == null ? 
						MineResetScheduleType.NORMAL : 
						getCurrentJob().getResetType();
		
		boolean forced = resetScheduleType == MineResetScheduleType.FORCED;
		
		// If not a manual reset, and if the resetTime is -1, which means never reset 
		// based upon time, then exit and trigger the next action.  This should never
		// happen, since the task will never advance to this state, but just in case.
		if ( getResetTime() <= 0 && !forced ) {
			
			submitNextAction();
			return;
		}
		
    	boolean skip = !forced && 
    			isSkipResetEnabled() && 
    				getPercentRemainingBlockCount() >= getSkipResetPercent() &&
    				getSkipResetBypassCount() < getSkipResetBypassLimit();
    	
//    	Output.get().logInfo( "Mine Reset: Run: Mine= %s action= %s skip= %s forced= %s ", 
//    			this.getName(), getCurrentJob().getAction().name(),
//    			Boolean.valueOf( skip ).toString(), Boolean.valueOf( forced ).toString() );
		
		switch ( getCurrentJob().getAction() )
		{
			case MESSAGE_GATHER:
				// Not yet implemented: let MESSAGE process this request:

				// The idea here is to check how far all players are from the mine.  If they are
				// candidates to get a message, then collect a list of players async.  This 
				// should be safe to run, since the active players would already have the 
				// chunks where they are already loaded so as to prevent corruption if 
				// this process "forces" an unloaded chunk to load.
				
			
			case MESSAGE:
				// Send reset message:
				broadcastPendingResetMessageToAllPlayersWithRadius(getCurrentJob() );

				break;
				
			case RESET_BUILD_BLOCKS_ASYNC:
				if ( !skip ) {
					generateBlockListAsync();
				}

				break;

			case RESET_SYNC:
			case RESET_ASYNC:
				if ( !skip ) {
					
					List<MineResetActions> resetActions = getCurrentJob().getResetActions();

					MinePagedResetAsyncTask resetTask = 
								new MinePagedResetAsyncTask( (Mine) this, MineResetType.normal, resetActions, resetScheduleType );
					
		    		resetTask.submitTaskAsync();
		    		
//					resetAsynchonously();
				} 
				else {
					incrementSkipResetBypassCount();
					broadcastSkipResetMessageToAllPlayersWithRadius();
				}
				
				break;
				
//			case RESET_SYNC:
//				// synchronous reset.  Will be phased out in the future?
//				if ( !skip ) {
//
//					List<MineResetActions> resetActions = getCurrentJob().getResetActions();
//					
//					MinePagedResetAsyncTask resetTask = 
//							new MinePagedResetAsyncTask( (Mine) this, MineResetType.normal, resetActions );
//					
//					resetTask.submitTaskAsync();
//					
////					resetSynchonously();
//				} else {
//					incrementSkipResetBypassCount();
//				}
//				
//				break;
				
			default:
				break;
		}
		
//		if ( getCurrentJob().getAction() == MineJobAction.RESET ) {
//			resetSynchonously();
//		} else {
//			// Send reset message:
//			broadcastPendingResetMessageToAllPlayersWithRadius(getCurrentJob(), MINE_RESET_BROADCAST_RADIUS_BLOCKS );
//		}
//		
		
		// this may be an issue for disabled resetTimes... may still need to be submitted?
		// disabled resets may not need to be submitted at all.
		if ( getResetTime() > 0 ) {
			
			submitNextAction();
		}
	}

	/**
	 * <p>This checks to see if the world stored for the mine is null, if it is, it
	 * tries to reload it the platform (spigot?) and then it updates all world instances
	 * within the Bound object for the mine, including spawn.
	 * </p>
	 * 
	 */
	@SuppressWarnings( "unused" )
	private void checkWorld()
	{
		if ( isVirtual() ) {
			// ignore:
		}
		else
		if ( !isEnabled() ) {
			// Must try to load world again:
			Optional<World> worldOptional = Prison.get().getPlatform().getWorld( getWorldName() );
	        if (!worldOptional.isPresent()) {
	            Output.get().logWarn( "&7MineScheduler.checkWorld: World STILL doesn't exist. " +
	            		"This is serious. &aworldName= " + getWorldName() );
	        }
	        else {
	        	World world = worldOptional.get();
	        	
	        	setWorld( world );
	        }
		}
	}

	/**
	 * <p>This task performs the job submission.  If the currentJob is null, then it will generate an 
	 * exception in the console. Manually resetting the mine will resubmit the workflow. 
	 * </p>
	 * 
	 * <p>At the beginning of every submission, it will update the targetResetTime associated with
	 * this mine.  This is important since heavy work loads could result in delays that will 
	 * push the actual reset back.  This is a way to update the estimated target time.
	 * </p>
	 */
	private void submitTask() {
		if ( getCurrentJob() != null ) {
			// Need to set the targetRestTime when the job is first submitted since that is the ideal time:
			long targetResetTime = System.currentTimeMillis() + 
									Math.round(getCurrentJob().getJobSubmitResetInSec() * 1000.0d);
			setTargetResetTime( targetResetTime );
			
			long ticksToWait = Math.round( getCurrentJob().getDelayActionSec() * 20.0d);
			
			
			// Submit currentJob using delay in the job. Must be a one time run, no repeats.
			int taskId = PrisonTaskSubmitter.runTaskLater(this, ticksToWait);
			setTaskId( taskId );
		} 
		else {
			Output.get().logError("Mine " + getName() +
					" failed to resubmit itself so it will not auto reset. Manually reset " +
					"this mine to re-enable the auto reset.");
		}
	}
	

	/**
	 * <p>Terminate this job to remove it from running, which will also allow the mine to be 
	 * garbage collected if removing the mine.
	 * </p>
	 * 
	 */
	public void terminateJob() {
		
		setDeleted( true );
		
		getJobStack().clear();
	
		int taskId = getTaskId();
		
		PrisonTaskSubmitter.cancelTask( taskId );
	}
	
	public void submit( double offsetSeconds ) {
		submitNextAction(offsetSeconds);
	}
	private void submitNextAction() {
		submitNextAction(0);
	}
	private void submitNextAction(double offsetSeconds) {
		if ( getJobStack().size() == 0 ) {
			resetJobStack();
		}
		
		setCurrentJob( getJobStack().pop() );
		
		// Offset tries to stagger the mine resets, assuming most will have the same delays:
		if ( offsetSeconds > 0 ) {
			getCurrentJob().setDelayActionSec( getCurrentJob().getDelayActionSec() + offsetSeconds );
		}
		
		// Submit currentJob using delay in the job. Must be a one time run, no repeats.
		submitTask();
	}

	
	
	/**
	 * <p>This function checks if the block break event should execute a 
	 * given command or not. If it needs to, then it will submit them to run as 
	 * a task instead of running them in this thread.
	 * </p>
	 * 
	 * @param blockCount
	 * @param player 
	 */
	public void processBlockBreakEventCommands( PrisonBlock prisonBlock,
						MineTargetPrisonBlock targetBlock,
						Player player, 
							BlockEventType eventType, String triggered ) {
		
		// Only one block is processed here:
		if ( getBlockEvents().size() > 0 ) {
			
			List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
			
			Random random = new Random();
			
			int row = 0;
			for ( MineBlockEvent blockEvent : getBlockEvents() ) {
				double chance = random.nextDouble() * 100;
				
				processBlockEventDetails( player, prisonBlock,
						targetBlock, eventType, chance, blockEvent, triggered,
						cmdTasks, ++row );
			}
			
			
			PrisonCommandTasks.submitTasks( player, cmdTasks );
		}
	}

	
//	/**
//	 * <p>This function checks if the block break event should execute a 
//	 * given command or not. If it needs to, then it will submit them to run as 
//	 * a task instead of running them in this thread.
//	 * </p>
//	 * 
//	 * @param blockCount
//	 * @param player 
//	 */
//	@Deprecated
//	public void processBlockBreakEventCommands( int blockCount, Player player, 
//							BlockEventType eventType, String triggered ) {
//		
//		if ( getBlockEvents().size() > 0 ) {
//			Random random = new Random();
//			
//			for ( int i = 0; i < blockCount; i ++ ) {
//				
//				for ( MineBlockEvent blockEvent : getBlockEvents() ) {
//					double chance = random.nextDouble() * 100;
//					
//					processBlockEventDetails( player, null, eventType, chance, blockEvent, triggered );
//				}
//				
//			}
//		}
//	}

	private void processBlockEventDetails( Player player, PrisonBlock prisonBlock,
							MineTargetPrisonBlock targetBlock, BlockEventType eventType, 
				double chance, 
					MineBlockEvent blockEvent, String triggered, 
					List<PrisonCommandTaskData> cmdTasks, int row )
	{

		boolean fireEvent = blockEvent.isFireEvent( chance, eventType, 
							targetBlock, triggered );
		
		
		if ( fireEvent ) {
			
			// If perms are set, check them, otherwise ignore perm check:
			String perms = blockEvent.getPermission();
			if ( perms != null && perms.trim().length() > 0 && player.hasPermission( perms ) ||
					perms == null || 
					perms.trim().length() == 0
					) {
				
				DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0000" );
				
				PrisonBlockStatusData originalBlock = targetBlock.getPrisonBlock();
				
				String debugInfo = "BlockEvent: " + getName();
				PrisonCommandTaskData cmdTask = new PrisonCommandTaskData( debugInfo, blockEvent.getCommand(), row );
				cmdTask.setTaskMode( blockEvent.getTaskMode() );
				
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.blockName, originalBlock.getBlockName() );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.mineName, getName() );
				
				if ( targetBlock.getLocation() != null ) {
					Location location = targetBlock.getLocation();
					
					cmdTask.addCustomPlaceholder( CustomPlaceholders.locationWorld, location.getWorld().getName() );
					cmdTask.addCustomPlaceholder( CustomPlaceholders.locationX, Integer.toString( location.getBlockX() ));
					cmdTask.addCustomPlaceholder( CustomPlaceholders.locationY, Integer.toString( location.getBlockY() ));
					cmdTask.addCustomPlaceholder( CustomPlaceholders.locationZ, Integer.toString( location.getBlockZ() ));
					
					cmdTask.addCustomPlaceholder( CustomPlaceholders.coordinates, location.toCoordinates() );
					cmdTask.addCustomPlaceholder( CustomPlaceholders.worldCoordinates, location.toWorldCoordinates() );
					
					cmdTask.addCustomPlaceholder( CustomPlaceholders.blockCoordinates, targetBlock.getBlockCoordinates() );
					
				}
//				cmdTask.addCustomPlaceholder( CustomPlaceholders.blockCoordinates, prisonBlock.getBlockCoordinates() );


				cmdTask.addCustomPlaceholder( CustomPlaceholders.blockChance, dFmt.format( originalBlock.getChance() ) );
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.blocksPlaced, Integer.toString( originalBlock.getBlockPlacedCount() ));
				cmdTask.addCustomPlaceholder( CustomPlaceholders.blockRemaining, Long.toString( originalBlock.getBlockCountUnsaved() ));
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.blocksMinedTotal, Long.toString( originalBlock.getBlockCountSession() ) );
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.mineBlocksRemaining, Integer.toString( getRemainingBlockCount() ));
				cmdTask.addCustomPlaceholder( CustomPlaceholders.mineBlocksRemainingPercent, Double.toString( getPercentRemainingBlockCount() ) );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.mineBlocksTotalMined, Long.toString( getTotalBlocksMined() ));
				cmdTask.addCustomPlaceholder( CustomPlaceholders.mineBlocksSize, Integer.toString( getBounds().getTotalBlockCount() ));

				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.blockIsAir, Boolean.toString( targetBlock.getPrisonBlock().isAir() ));
				
				if ( prisonBlock != null ) {
					
					cmdTask.addCustomPlaceholder( CustomPlaceholders.blockMinedName, prisonBlock.getBlockName() );
					cmdTask.addCustomPlaceholder( CustomPlaceholders.blockMinedNameFormal, prisonBlock.getBlockNameFormal() );
					cmdTask.addCustomPlaceholder( CustomPlaceholders.blockMinedBlockType, prisonBlock.getBlockType().name() );
				}
				
				cmdTask.addCustomPlaceholder( CustomPlaceholders.eventType, eventType.name() );
				cmdTask.addCustomPlaceholder( CustomPlaceholders.eventTriggered, triggered );
				
				
				cmdTasks.add( cmdTask );
				
				
//				cmdTask.submitCommandTask( player, blockEvent.getCommand(), blockEvent.getTaskMode() );
					
//				{
//					
//					String formatted = blockEvent.getCommand()
//							.replace( "{msg}", "prison utils msg {player} " )
//							.replace( "{broadcast}", "prison utils broadcast " )
//							.replace("{player}", player.getName())
//							.replace("{player_uid}", player.getUUID().toString());
//					
//					// Split multiple commands in to a List of individual tasks:
//					List<String> tasks = new ArrayList<>( 
//							Arrays.asList( formatted.split( ";" ) ));
//					
//					
//					if ( tasks.size() > 0 ) {
//						
//						String errorMessage = "BlockEvent: Player: " + player.getName();
//						
//						boolean playerTask = blockEvent.getTaskMode() == TaskMode.inlinePlayer || 
//											 blockEvent.getTaskMode() == TaskMode.syncPlayer;
//						
//						PrisonDispatchCommandTask task = 
//								new PrisonDispatchCommandTask( tasks, errorMessage, 
//												player, playerTask );
//						
//						
//						switch ( blockEvent.getTaskMode() )
//						{
//							case inline:
//							case inlinePlayer:
//								// Don't submit, but run it here within this thread:
//								task.run();
//								break;
//								
//							case sync:
//							case syncPlayer:
//							//case "async": // async will cause failures so run as sync:
//								
//								// submit task: 
//								@SuppressWarnings( "unused" ) 
//								int taskId = PrisonTaskSubmitter.runTaskLater(task, 0);
//								break;
//								
//							default:
//								break;
//						}
//						
//					}
//					
//					
////					PrisonAPI.dispatchCommand(formatted);
//				}
			}
		}
	}
	
	@Override
	public boolean checkZeroBlockReset() {
		boolean reset = false;
		
		// Reset if the mine runs out of blocks:
		
		if ( !isVirtual() && getMineStateMutex().isMinable() ) {
			
			int totalBlocks = getBounds().getTotalBlockCount();
			int remaining = getRemainingBlockCount();
			double threshold = getResetThresholdPercent() == 0 ? 0 :
							totalBlocks * getResetThresholdPercent() / 100.0d;
			
			if (
					remaining <= 0 && !isZeroBlockResetDisabled() || 
					remaining <= threshold
					) {
				
				// submit a manual reset since the mine is empty:
				manualReset( MineResetScheduleType.ZERO_BLOCK_RESET, getZeroBlockResetDelaySec() );
				reset = true;
			}
			
		}
		
		return reset;
	}
	
	/**
	 * This is called by the MineCommand.resetCommand() function, which is 
	 * triggered by a player.
	 * 
	 */
//	public void manualReset() {
//		manualReset( MineResetScheduleType.FORCED );
//	}
	public void manualReset( MineResetScheduleType resetType ) {
		
		if ( !isVirtual() ) {
			manualReset( resetType, 0 );
		}
	}
	
	private void manualReset( MineResetScheduleType resetType, double delayActionSec ) {
		List<MineResetActions> resetActions = new ArrayList<>();
		
		manualReset( resetType, delayActionSec, resetActions );
	}
	public void manualReset( MineResetScheduleType resetType, List<MineResetActions> resetActions ) {
		
		manualReset( resetType, 0, resetActions );
	}
	
	
	/**
	 * <p>This function should only be called from the commands to manually force a mine to reset.
	 * How this should work, is it should cancel (remove) the scheduled reset for this mine, then 
	 * run the actual reset, with the intentions of resubmitting the whole workflow from the
	 * beginning.  A manual reset not only resets the mine, but it resets the workflow schedule 
	 * from the beginning.
	 * </p>
	 * @param resetType
	 * @param delayActionSec Delay in seconds before resetting mine. 
	 * 
	 */
	private void manualReset( MineResetScheduleType resetType, double delayActionSec, 
			List<MineResetActions> resetActions ) {
		
		if ( isVirtual() ) {
			// Nope... nothing to reset...
			return;
		}
		
		if ( !getMineStateMutex().isMinable() && 
				getMineResetStartTimestamp() != -1 && 
				System.currentTimeMillis() - getMineResetStartTimestamp() > 3 * 60000 ) {
			
			// Mine reset was trying to run for more than 3 minutes... so it's locked out and failed?
			
			// reset mutex and allow the rest to be forced:
			getMineStateMutex().setMineStateResetFinishedForced();
			
			setMineResetStartTimestamp( -1 );
			
		}
		
		
		
		synchronized ( getMineStateMutex() ) {

			// The synchronized block will halt threads and will wait.  
			// So there is a chance that more than one reset will make it in to this
			// block, so need to check the mutex state again and exist if the mine
			// is not minable, since that indicates an earlier thread won the 
			// reset:
			
			if ( !getMineStateMutex().isMinable() ) {
				// exit since another reset has been started. 
				return;
			}

			
			getMineStateMutex().setMineStateResetStart();
			
			setMineResetStartTimestamp( System.currentTimeMillis() );

			
//			// Lock the mine's mutex if it's still minable.  Otherwise skip it since the
//			// state has been incremented by one already.
//			if ( getMineStateMutex().isMinable() ) {
//				
//				getMineStateMutex().setMineStateResetStart();
//			}
//			else if ( getMineStateMutex().getMineStateSn() > 1 ) {
//				
//				// synchronizing on the mutex this will allow only one thread to be
//				// processed at a time, which will weed out extra threads from being 
//				// wrongfully shutdown. Based upon this "technique" the last thread to
//				// be paused by this synchronized block will be the one that will actually
//				// initiate the reset.
//				if ( getMineStateMutex().getMineStateSn() > 1 ) {
//					
//					// This may be a double submission sinc the mineStateSn should only be 1 at this 
//					// point.  So release this lock and shutdown this duplicate submission.
//					getMineStateMutex().setMineStateResetFinished();
//					
//					// duplicate reset request, so exit...
//					return;
//				}
//				
//			}
			
			
			
			// cancel existing job:
			if ( getTaskId() != null ) {
				PrisonTaskSubmitter.cancelTask( getTaskId() );
			}
			
			// Clear jobStack and set currentJob to run the RESET with zero delay:
			getJobStack().clear();
			
			MineJobAction action = MineJobAction.RESET_ASYNC; // : MineJobAction.RESET_SYNC;
			
			MineJob mineJob = new MineJob( action, delayActionSec, 0, resetType );
			mineJob.setResetType( resetType );
			mineJob.setResetActions( resetActions );
			
			setCurrentJob( mineJob );
			
			// Force reset even if skip is enabled:
			
			// Submit to run:
			submitTask();
		}
		
	}

	public List<MineJob> getJobWorkflow()
	{
		return jobWorkflow;
	}
	public void setJobWorkflow( List<MineJob> jobWorkflow )
	{
		this.jobWorkflow = jobWorkflow;
	}

	public Stack<MineJob> getJobStack()
	{
		return jobStack;
	}
	public void setJobStack( Stack<MineJob> jobStack )
	{
		this.jobStack = jobStack;
	}

	public Integer getTaskId()
	{
		return taskId;
	}
	public void setTaskId( Integer taskId )
	{
		this.taskId = taskId;
	}

	public long getMineResetStartTimestamp() {
		return mineResetStartTimestamp;
	}
	public void setMineResetStartTimestamp(long mineResetStartTimestamp) {
		this.mineResetStartTimestamp = mineResetStartTimestamp;
	}
	
}

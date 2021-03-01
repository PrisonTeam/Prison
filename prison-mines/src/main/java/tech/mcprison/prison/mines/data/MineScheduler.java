package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.features.MineBlockEvent;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.mines.features.MineBlockEvent.TaskMode;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

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
	
	public MineScheduler() {
		super();
		
		this.jobWorkflow = new ArrayList<>();
		this.jobStack = new Stack<>();

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
	
	public enum MineResetType {
		NORMAL,
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
		private MineResetType resetType;
		private List<MineResetActions> resetActions;
		
		public MineJob( MineJobAction action, double delayActionSec, double resetInSec )
		{
			super();
			
			this.action = action;
			this.delayActionSec = delayActionSec;
			this.resetInSec = resetInSec;
			
			this.resetType = MineResetType.NORMAL;
			
			this.resetActions = new ArrayList<>();
		}
		
		public MineJob( MineJobAction action, double delayActionSec, double resetInSec, MineResetType resetType )
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

		public MineResetType getResetType() {
			return resetType;
		}
		public void setResetType( MineResetType resetType ) {
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
		List<MineJob> workflow = new ArrayList<>();
		
		// if the mine is virtual, set the resetTime to four hours.  It won't reset, but it will stay active
		// in the workflow:
		if ( isVirtual() ) {
			resetTime = 60 * 60 * 4; // one hour * 4
		}
		
		// Determine if the sync or async reset action should be used for this workflow.
		MineJobAction resetAction = isUsePagingOnReset() ? MineJobAction.RESET_ASYNC : MineJobAction.RESET_SYNC;
		
		if ( includeMessages ) {
			// Need to ensure that the reset warning times are sorted in ascending order:
//			ArrayList<Integer> rwTimes = PrisonMines.getInstance().getConfig().resetWarningTimes;
			Collections.sort( rwTimes );
			
			double total = 0;
			for ( Integer time : rwTimes ) {
				if ( time < resetTime ) {
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
							(resetTime - total), total) );
			
		} else {
			// Exclude all messages. Only reset mine:
			workflow.add( new MineJob( resetAction, resetTime, 0) );
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
		
		boolean forced = getCurrentJob() != null && 
							getCurrentJob().getResetType() == MineResetType.FORCED;
		
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

			case RESET_ASYNC:
				if ( !skip ) {
					resetAsynchonously();
				} else {
					incrementSkipResetBypassCount();
				}
				
				break;
				
			case RESET_SYNC:
				// synchronous reset.  Will be phased out in the future?
				if ( !skip ) {
					resetSynchonously();
				} else {
					incrementSkipResetBypassCount();
				}
				
				break;
				
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
		
		submitNextAction();
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
		} else {
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
	public void processBlockBreakEventCommands( String blockName, Player player, 
							BlockEventType eventType, String triggered ) {
		
		// Only one block is processed here:
		if ( getBlockEvents().size() > 0 ) {
			Random random = new Random();
			
			for ( MineBlockEvent blockEvent : getBlockEvents() ) {
				double chance = random.nextDouble() * 100;
				
				processBlockEventDetails( player, blockName, eventType, chance, blockEvent, triggered );
			}
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

	private void processBlockEventDetails( Player player, String blockName, BlockEventType eventType, 
				double chance, 
					MineBlockEvent blockEvent, String triggered )
	{

		boolean fireEvent = blockEvent.isFireEvent( chance, eventType, blockName, triggered );
		
		if ( fireEvent ) {
			
			// If perms are set, check them, otherwise ignore perm check:
			String perms = blockEvent.getPermission();
			if ( perms != null && perms.trim().length() > 0 && player.hasPermission( perms ) ||
					perms == null || 
					perms.trim().length() == 0
					) {
				
				 {
					
					String formatted = blockEvent.getCommand()
							.replace( "{msg}", "prison utils msg {player} " )
							.replace( "{broadcast}", "prison utils broadcast " )
							.replace("{player}", player.getName())
							.replace("{player_uid}", player.getUUID().toString());
					
					// Split multiple commands in to a List of individual tasks:
					List<String> tasks = new ArrayList<>( 
							Arrays.asList( formatted.split( ";" ) ));
					
					
					if ( tasks.size() > 0 ) {
						
						String errorMessage = "BlockEvent: Player: " + player.getName();
						
						boolean playerTask = blockEvent.getTaskMode() == TaskMode.inlinePlayer || 
											 blockEvent.getTaskMode() == TaskMode.syncPlayer;
						
						PrisonDispatchCommandTask task = 
								new PrisonDispatchCommandTask( tasks, errorMessage, 
												player, playerTask );
						
						
						switch ( blockEvent.getTaskMode() )
						{
							case inline:
							case inlinePlayer:
								// Don't submit, but run it here within this thread:
								task.run();
								break;
								
							case sync:
							case syncPlayer:
							//case "async": // async will cause failures so run as sync:
								
								// submit task: 
								@SuppressWarnings( "unused" ) 
								int taskId = PrisonTaskSubmitter.runTaskLater(task, 0);
								break;
								
							default:
								break;
						}
						
					}
					
					
//							PrisonAPI.dispatchCommand(formatted);
				}
			}
		}
	}
	
	public boolean checkZeroBlockReset() {
		boolean reset = false;
		
		// Reset if the mine runs out of blocks:
		
		
		if ( !isVirtual() && (
				getRemainingBlockCount() <= 0 && !isZeroBlockResetDisabled() || 
				getResetThresholdPercent() > 0 && 
				getRemainingBlockCount() < (getBounds().getTotalBlockCount() * 
												getResetThresholdPercent() / 100.0d)
				)) {
			
			// submit a manual reset since the mine is empty:
			manualReset( MineResetType.NORMAL, getZeroBlockResetDelaySec() );
			reset = true;
		}
		return reset;
	}
	
	/**
	 * This is called by the MineCommand.resetCommand() function, which is 
	 * triggered by a player.
	 * 
	 */
	public void manualReset() {
		manualReset( MineResetType.FORCED );
	}
	public void manualReset( MineResetType resetType ) {
		
		if ( !isVirtual() ) {
			manualReset( resetType, 0 );
		}
	}
	
	private void manualReset( MineResetType resetType, double delayActionSec ) {
		List<MineResetActions> resetActions = new ArrayList<>();
		
		manualReset( resetType, delayActionSec, resetActions );
	}
	public void manualReset( MineResetType resetType, List<MineResetActions> resetActions ) {
		
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
	private void manualReset( MineResetType resetType, double delayActionSec, 
			List<MineResetActions> resetActions ) {
		
		if ( isVirtual() ) {
			// Nope... nothing to reset... 
			return;
		}
		
		// cancel existing job:
		if ( getTaskId() != null ) {
			PrisonTaskSubmitter.cancelTask( getTaskId() );
		}
		
		// Clear jobStack and set currentJob to run the RESET with zero delay:
		getJobStack().clear();
		
		MineJobAction action = isUsePagingOnReset() ? 
				MineJobAction.RESET_ASYNC : MineJobAction.RESET_SYNC;
		
		MineJob mineJob = new MineJob( action, delayActionSec, 0, resetType );
		mineJob.setResetType( resetType );
		mineJob.setResetActions( resetActions );
		
		setCurrentJob( mineJob );
    	
		// Force reset even if skip is enabled:
		
		// Submit to run:
		submitTask();
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

}

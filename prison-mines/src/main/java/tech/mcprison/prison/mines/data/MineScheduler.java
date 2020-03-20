package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.output.Output;

public abstract class MineScheduler
		extends MineReset
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
	private MineJob currentJob;
	private Integer taskId = null;
	
	public MineScheduler() {
		super();
		
		this.jobWorkflow = new ArrayList<>();
		this.jobStack = new Stack<>();
		this.currentJob = null;
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
		RESET( JobType.SYNC );
		
		private final JobType jobType;
		private MineJobAction(JobType jobType) {
			this.jobType = jobType;
		}

		public JobType getJobType()
		{
			return jobType;
		}
	}
	
	/**
	 * <p>This class represents a workflow action.  The action can be one of either MESSAGE, or
	 * RESET.  The delayActionSec is how many seconds the job must wait until taking action.
	 * When it does take action, such as sending of a message, the resetInSec should be how
	 * many seconds in to the future will be the reset.
	 * </p>
	 *
	 */
	public class MineJob 
	{
		private MineJobAction action;
		private int delayActionSec;
		private int resetInSec;
		
		public MineJob( MineJobAction action, int delayActionSec, int resetInSec )
		{
			super();
			
			this.action = action;
			this.delayActionSec = delayActionSec;
			this.resetInSec = resetInSec;
		}
		
		@Override
		public String toString() {
			return "Action: " + getAction().name() + " Delay: " + getDelayActionSec() + " Reset: " + getResetInSec();
		}

		public MineJobAction getAction()
		{
			return action;
		}
		public void setAction( MineJobAction action )
		{
			this.action = action;
		}

		public int getDelayActionSec()
		{
			return delayActionSec;
		}
		public void setDelayActionSec( int delayActionSec )
		{
			this.delayActionSec = delayActionSec;
		}

		public int getResetInSec()
		{
			return resetInSec;
		}
		public void setResetInSec( int resetInSec )
		{
			this.resetInSec = resetInSec;
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
	protected List<MineJob> initializeJobWorkflow( int resetTime, boolean includeMessages, ArrayList<Integer> rwTimes )
	{
		List<MineJob> workflow = new ArrayList<>();
		
		if ( includeMessages ) {
			// Need to ensure that the reset warning times are sorted in ascending order:
//			ArrayList<Integer> rwTimes = PrisonMines.getInstance().getConfig().resetWarningTimes;
			Collections.sort( rwTimes );
			
			int total = 0;
			for ( Integer time : rwTimes )
			{
				if ( time < resetTime ) {
					// if reset time is less than warning time, then skip warning:
					int elapsed = time - total;
					workflow.add( 
							new MineJob( workflow.size() == 0 ? MineJobAction.RESET : MineJobAction.MESSAGE, 
									elapsed, total) );
					total += elapsed;
				}
			}
			workflow.add( 
					new MineJob( workflow.size() == 0 ? MineJobAction.RESET : MineJobAction.MESSAGE, 
							(resetTime - total), total) );
			
		} else {
			// Exclude all messages. Only reset mine:
			workflow.add( new MineJob( MineJobAction.RESET, resetTime, 0) );
		}
		
		return workflow;
	}
	
	
	private void resetJobStack() {
		getJobStack().clear();
		getJobStack().addAll( getJobWorkflow() );
	}
	
	
	@Override
	public void run()
	{
		switch ( getCurrentJob().getAction() )
		{
			case MESSAGE_GATHER:
				// Not yet implemented: let MESSAGE process this request:
			
			case MESSAGE:
				// Send reset message:
				broadcastPendingResetMessageToAllPlayersWithRadius(getCurrentJob(), 
										MINE_RESET__BROADCAST_RADIUS_BLOCKS );

				break;
				
			case RESET_BUILD_BLOCKS_ASYNC:
				generateBlockListAsync();

				break;

			case RESET_ASYNC:
				// Not yet implemented:
				
				break;
				
			case RESET:
				// synchronous reset.  Will be phased out in the future?
				resetSynchonously();
				
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

	
	
	private void submitTask() {
		if ( getCurrentJob() != null ) {
			long ticksToWait = getCurrentJob().getDelayActionSec() * 20;
			// Submit currentJob using delay in the job. Must be a one time run, no repeats.
			int taskId = Prison.get().getPlatform().getScheduler().runTaskLater(this, ticksToWait);
			setTaskId( taskId );
		} else {
			Output.get().logError("Mine " + getName() +
					" failed to resubmit itself so it will not auto reset. Manually reset " +
					"this mine to re-enable the auto reset.");
		}
	}
	
	public void submit( int offset ) {
		submitNextAction(offset);
	}
	private void submitNextAction() {
		submitNextAction(0);
	}
	private void submitNextAction(int offset) {
		if ( getJobStack().size() == 0 ) {
			resetJobStack();
		}
		
		setCurrentJob( getJobStack().pop() );
		
		// Offset tries to stagger the mine resets, assuming most will have the same delays:
		if ( offset > 0 ) {
			getCurrentJob().setDelayActionSec( getCurrentJob().getDelayActionSec() + offset );
		}
		
		// Submit currentJob using delay in the job. Must be a one time run, no repeats.
		submitTask();
	}

	
	/**
	 * <p>This function should only be called from the commands to manually force a mine to reset.
	 * How this should work, is it should cancel (remove) the scheduled reset for this mine, then 
	 * run the actual reset, with the intentions of resubmitting the whole workflow from the
	 * beginning.  A manual reset not only resets the mine, but it resets the workflow schedule 
	 * from the beginning.
	 * </p>
	 * 
	 */
	public void manualReset() {
		// cancel existing job:
		if ( getTaskId() != null ) {
			Prison.get().getPlatform().getScheduler().cancelTask( getTaskId() );
		}
		
		// Clear jobStack and set currentJob to run the RESET with zero delay:
		getJobStack().clear();
		setCurrentJob( new MineJob( MineJobAction.RESET, 0, 0) );
		
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

	public MineJob getCurrentJob()
	{
		return currentJob;
	}
	public void setCurrentJob( MineJob currentJob )
	{
		this.currentJob = currentJob;
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

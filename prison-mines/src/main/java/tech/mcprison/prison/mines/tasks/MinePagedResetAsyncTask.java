package tech.mcprison.prison.mines.tasks;

import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.PrisonStatsElapsedTimeNanos;
import tech.mcprison.prison.internal.block.MineResetType;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.MineScheduler.MineResetActions;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class MinePagedResetAsyncTask
		implements PrisonRunnable
{
	private Mine mine;
	private final MineResetType resetType;
	
	private int position = 0;
	
	private int page = 0;
	
	private int totalPages = 0;
	private int pagesPerReport = 20;
	private int pagePosition = 0;
	

	private long timeStart = 0;
	private long timePage = 0;
	
	private PrisonStatsElapsedTimeNanos nanos;
	
	
	// Config Settings:
	private int configAsyncResetPageSize = -1;
	private int configSyncSubPageSlice = -1;
	
	
	private List<MineResetActions> resetActions;
	
	
	
	public MinePagedResetAsyncTask( Mine mine, MineResetType resetType, List<MineResetActions> resetActions ) {
		super();
		
		this.mine = mine;
		this.resetType = resetType;
		
		
		this.timeStart = System.currentTimeMillis();
		this.timePage = timeStart;
		
		this.nanos = new PrisonStatsElapsedTimeNanos();
		
		this.totalPages = (mine.getMineTargetPrisonBlocks().size() / 
												getConfigAsyncResetPageSize()) + 1;
		
		this.resetActions = resetActions;
	}
	
	
	public MinePagedResetAsyncTask( Mine mine, MineResetType resetType ) {
		this( mine, resetType, null );
	}
	
	
	public void submitTaskSync() {
		submitTaskAsync();
	}
	public void submitTaskAsync() {
		
		// Prevent the task from being submitted if it is a virtual mine:
		if ( mine.isVirtual() ) {
			return;
		}
		
		
		if ( position > 0 && page++ % pagesPerReport == 0 ) {
			
			if ( PrisonMines.getInstance().getMineManager().isMineStats() ) {
				
				logStats();
			}
		}
		
		long delay = 0;
		PrisonTaskSubmitter.runTaskLaterAsync( this, delay );
	}


	private void logStats()
	{
		long timeCurrent = System.currentTimeMillis();
		long timeElapsedTotal = timeCurrent - timeStart;
		long timeElapsedPage = timeCurrent - timePage;
		timePage = timeCurrent;
		
		
		int blocksPlaced = position - pagePosition;
		pagePosition = position;
		
		
		mine.setResetPosition( position );
		
		mine.setResetPage( page );
		
		mine.setStatsBlockUpdateTimeNanos( nanos.getElapsedTimeNanos() );

		//		long time = System.currentTimeMillis() - start;
		mine.setStatsBlockUpdateTimeMS( timeElapsedPage + mine.getStatsBlockUpdateTimeMS() );
		mine.setStatsResetTimeMS( timeElapsedPage + mine.getStatsResetTimeMS() );
		
		
		mine.setStatsResetPages( page );
		mine.setStatsResetPageBlocks( mine.getStatsResetPageBlocks() + blocksPlaced );
		mine.setStatsResetPageMs( mine.getStatsResetPageMs() + timeElapsedPage  );
		
		
		// Only print these details if stats are enabled:
		if ( PrisonMines.getInstance().getMineManager().isMineStats() ) {
			
			Output.get().logInfo( "MinePagedResetAsyncTask : " +
					mine.getName() + " " +
					resetType.name() + 
					" : page " + page + " of " + totalPages + " : " +
					"  blocks = " + blocksPlaced + "  elapsed = " + timeElapsedPage + 
					" ms  TotalElapsed = " + timeElapsedTotal + " ms   " +
							"block update elapsed = " + 
					( getNanos().getElapsedTimeNanos() / 1000000d ) + " ms(nanos)"
//							"  TPS " +
//					Prison.get().getPrisonTPS().getAverageTPSFormatted()
					);
		}
	}
	
	@Override
	public void run() {
		
		// The first time running this, need to setup the block list if a reset:
		if ( position == 0 ) {
			if ( runSetupCancelReset() ) {
				// If the reset should be canceled then just return, and that will 
				// terminate the reset.  There is nothing else that needs to be done.
				return;
			}
		}
		
		List<MineTargetPrisonBlock> targetBlocks = mine.getMineTargetPrisonBlocks();

		int pageEndPosition = position + getConfigAsyncResetPageSize();
				
		
		while ( position < pageEndPosition ) {
			
			int endIndex = position + getConfigSyncSubPageSlice();
			if ( endIndex > targetBlocks.size() ) {
				endIndex = targetBlocks.size();
				pageEndPosition = endIndex;
			}
			
			List<MineTargetPrisonBlock> tBlocks = targetBlocks.subList( position, endIndex );
			
			mine.getWorld().get().setBlocksSynchronously( tBlocks, resetType, getNanos() );
			
			position += tBlocks.size();
		}
		
		
		// Keep resubmitting this task until it is completed:
		if ( position < targetBlocks.size() ) {
			submitTaskSync();
		}
		else {
			
			// Finished running the task and let it end:
			runShutdown();
		}
		
	}

	
	/**
	 * <p>This is ran before the initial actual processing is performed.  
	 * This calls the functions to 
	 */
	private boolean runSetupCancelReset() {
		
    	// Set the MineStateMutex to a state of starting a mine reset:
    	mine.getMineStateMutex().setMineStateResetStart();
 
		
		if ( resetType == MineResetType.normal || resetType == MineResetType.paged ) {
			mine.generateBlockListAsync();
			
			// resetAsynchonouslyInitiate() will confirm if the reset should happened 
			// and will raise Prison's mine reset event. 
			// A return value of true means cancel the reset:
			return mine.resetAsynchonouslyInitiate();
			
		}
		
		mine.asynchronouslyResetSetup();
		return false;
	}
	
	private void runShutdown() {

		logStats();
		
		
		// Set the MineStateMutex to a state of Finishing a mine reset:
		// It is now safe to allow mining in the mine.
		mine.getMineStateMutex().setMineStateResetFinished();

		
		// Run items such as post-mine-reset commands:
		mine.asynchronouslyResetFinalize( getResetActions() );
 

	}

	
	public PrisonStatsElapsedTimeNanos getNanos() {
		return nanos;
	}
	public void setNanos( PrisonStatsElapsedTimeNanos nanos ) {
		this.nanos = nanos;
	}

	public List<MineResetActions> getResetActions() {
		return resetActions;
	}
	public void setResetActions( List<MineResetActions> resetActions ) {
		this.resetActions = resetActions;
	}


	public int getConfigAsyncResetPageSize() {
		if ( configAsyncResetPageSize == -1 ) {
			this.configAsyncResetPageSize = 
					Long.valueOf( Prison.get().getPlatform()
							.getConfigLong( "prison-mines.reset-async-paging.async-page-size", 
									4000 )).intValue();
		}
		return configAsyncResetPageSize;
	}
	
	public int getConfigSyncSubPageSlice() {
		if ( configSyncSubPageSlice == -1 ) {
			this.configSyncSubPageSlice = 
					Long.valueOf( Prison.get().getPlatform()
							.getConfigLong( "prison-mines.reset-async-paging.sync-sub-page-slice", 
									200 )).intValue();
		}
		return configSyncSubPageSlice;
	}
}

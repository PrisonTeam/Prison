package tech.mcprison.prison.mines.tasks;

import java.util.List;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineTargetPrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Location;

public class MinePagedResetAsyncTask
		implements PrisonRunnable
{
	private Mine mine;
	private final MineResetType resetType;
	
	private int position = 0;
	
	
	private int pageSize = 2000;
	private int page = 0;
	
	private int totalPages = 0;
//	private int subPage = 0;
	private int pagesPerReport = 20;
	private int pagePosition = 0;
	

	private long timeStart = 0;
	private long timePage = 0;
	
	
	
	public enum MineResetType {
		normal,
		paged,
		clear,
		tracer;
	}
	
	
	public MinePagedResetAsyncTask( Mine mine, MineResetType resetType ) {
		super();
		
		this.mine = mine;
		this.resetType = resetType;
		
		this.timeStart = System.currentTimeMillis();
		this.timePage = timeStart;
		
		this.totalPages = (mine.getMineTargetPrisonBlocks().size() / pageSize) + 1;
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
					" ms  TotalElapsed = " + timeElapsedTotal + " ms"// +
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

		int pageEndPosition = position + pageSize;
		
		for ( int i = position; i < pageEndPosition && i < targetBlocks.size(); i++ ) {
			MineTargetPrisonBlock tBlock = targetBlocks.get( i );
			
			final PrisonBlock pBlock = getPrisonBlock( tBlock );
					
			if ( pBlock != null ) {
				
				Location location = tBlock.getLocation();
				
				location.setBlockAsync( pBlock );
			}
			
			position++;
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
		mine.asynchronouslyResetFinalize();
	}

	private PrisonBlock getPrisonBlock( MineTargetPrisonBlock tBlock ) {
		final PrisonBlock pBlock;

		if ( resetType == MineResetType.tracer && tBlock.isEdge() )
		{
			pBlock = PrisonBlock.PINK_STAINED_GLASS;
		}
		else if ( resetType == MineResetType.clear || 
			 resetType == MineResetType.tracer )
		{
			pBlock = PrisonBlock.AIR;
		}
		else if ( tBlock.getPrisonBlock() != null && 
				  tBlock.getPrisonBlock() instanceof PrisonBlock )
		{

			// MineResetType.normal and MineResetType.paged
			pBlock = (PrisonBlock) tBlock.getPrisonBlock();
		}
		else
		{
			pBlock = null;
		}
		
		return pBlock;
	}

}

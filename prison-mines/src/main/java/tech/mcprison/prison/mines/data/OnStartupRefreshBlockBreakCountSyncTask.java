package tech.mcprison.prison.mines.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Location;

public class OnStartupRefreshBlockBreakCountSyncTask
	implements PrisonRunnable {
	
	private static OnStartupRefreshBlockBreakCountSyncTask instance;
	
//	private List<MineReset> mines;

	private MineReset mine;
	
	private int jobId = 0;

	private List<Location> locations = null;
	private int position = 0;
	private int pages = 0;
	private int pagesStart = 0;
	
	private int airCount = 0;
	private long elapsedNanos = 0;
	private double elapsedMsTotal = 0;
	
	private int errorCount = 0;
	private StringBuilder sbErrors = new StringBuilder();
	
	
	private List<Mine> processedMines;
	private int countCurrentMine = 0;
	private int countTotalMines = 0;
	
//	private boolean jobRunning = false;
	
	
	private OnStartupRefreshBlockBreakCountSyncTask() {
		super();
		
		this.processedMines = new ArrayList<>();
	}
	
	public static OnStartupRefreshBlockBreakCountSyncTask getInstance() {
		if ( instance == null ) {
			synchronized ( OnStartupRefreshBlockBreakCountSyncTask.class ) {
				if ( instance == null ) {
					instance = new OnStartupRefreshBlockBreakCountSyncTask();
				}
			}
		}
		return instance;
	}
	
//	public OnStartupRefreshBlockBreakCountSyncTask(MineReset mine) {
//		this.mine = mine;
//	}
	
	public void submit( long delay ) {
		
		setJobId( PrisonTaskSubmitter.runTaskLater( this, delay) );
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.0" );
		
		String msg = String.format(
				"Startup Block Count Task Submitted to run in %s seconds.",
				dFmt.format(delay / 20)
				);
		
		Output.get().logInfo( msg );
		
	}
	
	private Mine getNextMine() {
		Mine mine = null;
		
		
		List<Mine> mines = PrisonMines.getInstance().getMineManager().getMines();
		
		if ( countTotalMines == 0 ) {
			
			countTotalMines = mines.size();
		}
		
		for (Mine m : mines ) {
			
			// Check to see if we can even submit the job:
			if ( !m.isVirtual() &&
					m.getResetCount() == 0 
					&& m.refreshAirCountSyncTaskCheckBeforeSubmit() ) {
				
				// Prevents the mine from being counted again:
				m.setResetCount( 1 );
				
				countCurrentMine++;
				
				mine = m;
				processedMines.add( m );
				
				break;
			}
		}
		
		if ( mine == null ) {
			// done processing:
			
			TreeSet<Mine> minesNotProcessed = new TreeSet<>( mines );
			minesNotProcessed.removeAll(processedMines);
			
			for (Mine m : minesNotProcessed) {
				countCurrentMine++;

				String msg = String.format( 
						"MineReset startup air-count: Mine [%3d of %3d]: %-10s " +
								" Skipped:  virtual: %b  resetCounts: %s", 
								countCurrentMine,
								countTotalMines,
								m.getName(),
								m.isVirtual(),
								m.getResetCount()
								
						);
				
				Output.get().logInfo( msg );
								
			}
			
			String message = String.format( 
					"MineReset startup air-count: Completed. [%3d of %3d]:  " +
							"Mines not processed: %d", 
							countCurrentMine,
							countTotalMines,
							minesNotProcessed.size() );
			
			Output.get().logInfo( message );
		}
		
		return mine;
	}
	
	private void resubmit() {

		long delay = 0;
		
		if ( mine == null ) {
			mine = getNextMine();
			locations = null;
			position = 0;
			
			pagesStart = pages;
			
			elapsedNanos = 0;
			
			delay = 2;
		}

		if ( mine != null ) {
			
			// Must run synchronously!!
			setJobId( PrisonTaskSubmitter.runTaskLater( this, delay) );
//			setJobId( mine.submitSyncTask( this, delay ) );
		}
	}
	
	@Override
	public void run() {
		
		
		if ( mine == null ) {
			mine = getNextMine();
			locations = null;
		}
		
		if ( locations == null ) {
			
			if ( mine != null ) {
				
				long nanoStart = System.nanoTime();
				locations = this.mine.refreshAirCountSyncTaskBuildLocations();
				long nanoEnd = System.nanoTime();
				long elpased = (nanoEnd - nanoStart );
				this.elapsedNanos += elpased;
				
//			resubmit();
			}
			
		}
//		else // The 'this.mine.refreshAirCountSyncTaskBuildLocations()' is trivial...
		
		if ( mine == null ) {
			// No mine is available, either they are all processed, or none exist on 
			// the server yet (new startup), so exit without resubmitting:
			return;
		}
		
		{
			pages++;
			
			long nanoStart = System.nanoTime();
			
			int start = position;
			for (int i = start; i < locations.size(); i++ ) {
				
				Location targetLocation = locations.get( i );
				
				mine.refreshAirCountSyncTaskSetLocation( targetLocation, this );
				position++;
		
				if ( i != start && (i - start) % 500 == 0 ) {
					
					long nanoEnd = System.nanoTime();
					long elpased = (nanoEnd - nanoStart );
					long elapsedMs = elpased / 1_000_000;
					if ( elapsedMs > 20 ) {
						
						// Check every 500 blocks and if been running longer than 20 ms then yield to prevent lag
						
						this.elapsedNanos += elpased;
//						pages++;
						
						// Need to yield and resubmit
						resubmit();
						return;
					}
				}
		
			}
			
			// It will only hit this point when done processing all of the locations:
			

			mine.setAirCount( getAirCount() );
			mine.setBlockBreakCount( mine.getBlockBreakCount() + getAirCount() );
			
			long nanoEnd = System.nanoTime();
			long elpased = (nanoEnd - nanoStart );
			this.elapsedNanos += elpased;
			
			double elapsedMs = ((double) elapsedNanos) / 1000000d;
			elapsedMsTotal += elapsedMs;
			
			mine.setAirCountElapsedTimeMs( (long) elapsedMs );
			mine.setAirCountTimestamp( System.currentTimeMillis() );
			
			if ( Output.get().isDebug() ) {
				
				DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
				DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
				String message = String.format( 
						"MineReset startup air-count: Mine [%3d of %3d]: %-10s " +
								" blocks: %10s  pages: [%3s: %3s]  [%9s: %9s ms]", 
								countCurrentMine,
								countTotalMines,
								mine.getName(), 
								iFmt.format( locations.size() ),
								iFmt.format( pages - pagesStart ),
								iFmt.format( pages ),
								dFmt.format(elapsedMs),
								dFmt.format(elapsedMsTotal) 
								);
				
				Output.get().logInfo( message );
				
			}

			if ( getErrorCount() > 0 ) {
				String message = String.format( 
						"MineReset.refreshAirCountAsyncTask: Error counting air blocks: Mine=%s: " +
								"errorCount=%d  blocks: %s : %s", mine.getName(), getErrorCount(),
								(getErrorCount() > 20 ? "(first 20)" : ""),
								getSbErrors().toString() );
				
				Output.get().logWarn( message );
			}
			
			// Finalize by setting mine and locations to null so it will reset properly the next time:
			locations = null;
			mine = null;
			
		}
		
		// Submit the next mine, which will be assigned in resubmit():
		resubmit();
	}

//	public List<MineReset> getMines() {
//		return mines;
//	}
//	public void setMines(List<MineReset> mines) {
//		this.mines = mines;
//	}

	public MineReset getMine() {
		return mine;
	}
	public void setMine(MineReset mine) {
		this.mine = mine;
	}

	public int getJobId() {
		return jobId;
	}
	public void setJobId( int jobId ) {
		this.jobId = jobId;
	}

	public void incrementAirCount() {
		airCount++;
	}
	public int getAirCount() {
		return airCount;
	}
	public void setAirCount( int airCount ) {
		this.airCount = airCount;
	}

	public void incrementErrorCount() {
		errorCount++;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount( int errorCount ) {
		this.errorCount = errorCount;
	}

	public StringBuilder getSbErrors() {
		return sbErrors;
	}
	public void setSbErrors( StringBuilder sbErrors ) {
		this.sbErrors = sbErrors;
	}
}

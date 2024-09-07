package tech.mcprison.prison.mines.data;

import java.text.DecimalFormat;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.util.Location;

public class OnStartupRefreshBlockBreakCountSyncTask
	implements PrisonRunnable {
		
	private MineReset mine;
	private int jobId = 0;

	private List<Location> locations = null;
	private int position = 0;
	private int pages = 0;
	
	private int airCount = 0;
	private long elapsedNanos = 0;
	private int errorCount = 0;
	private StringBuilder sbErrors = new StringBuilder();
	
	
	public OnStartupRefreshBlockBreakCountSyncTask(MineReset mine) {
		this.mine = mine;
	}
	
	public static void submit( MineReset mine, long delay ) {
		
		OnStartupRefreshBlockBreakCountSyncTask syncTask = 
						new OnStartupRefreshBlockBreakCountSyncTask( mine );

		// Check to see if we can even submit the job:
		if ( mine.refreshAirCountSyncTaskCheckBeforeSubmit() ) {
			
			// The first phase generates a List of Locations which
			// can be ran async...
			syncTask.setJobId( mine.submitAsyncTask( syncTask, delay ) );
		}
		
	}
	
	private void resubmit() {

		// Must run synchronously!!
		
		
		long submitDelay = 0;
		
		
		
		setJobId( mine.submitSyncTask( this, submitDelay ) );
	}
	
	@Override
	public void run() {
		
		if ( locations == null ) {
			
			long nanoStart = System.nanoTime();
			locations = this.mine.refreshAirCountSyncTaskBuildLocations();
			long nanoEnd = System.nanoTime();
			long elpased = (nanoEnd - nanoStart );
			this.elapsedNanos += elpased;
			
			resubmit();
		}
		else {
			
			long nanoStart = System.nanoTime();
			
			int start = position;
			for (int i = start; i < locations.size(); i++ ) {
				
				Location targetLocation = locations.get( i );
				
				mine.refreshAirCountSyncTaskSetLocation( targetLocation, this );
				position++;
		
				if ( (i - start) % 500 == 0 ) {
					
					long nanoEnd = System.nanoTime();
					long elpased = (nanoEnd - nanoStart );
					long elapsedMs = elpased / 1000000;
					if ( elapsedMs > 15 ) {
						
						// Check every 500 blocks and if been running longer than 15 ms then yield to prevent lag
						
						this.elapsedNanos += elpased;
						pages++;
						
						// Need to yield and resubmit
						resubmit();
						return;
					}
				}
		
			}
			
			// It will only hit this point when done processing all of the locations:
			
			pages++;

			mine.setAirCount( getAirCount() );
			mine.setBlockBreakCount( mine.getBlockBreakCount() + getAirCount() );
			
			long nanoEnd = System.nanoTime();
			long elpased = (nanoEnd - nanoStart );
			this.elapsedNanos += elpased;
			
			double elapsedMs = ((double) elapsedNanos) / 1000000d;
			
			mine.setAirCountElapsedTimeMs( (long) elapsedMs );
			mine.setAirCountTimestamp( System.currentTimeMillis() );
			
			if ( Output.get().isDebug() ) {
				DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
				DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
				String message = String.format( 
						"MineReset startup air-count: Mine: %-6s  " +
								"   blocks: %10s  pages: %s  elapsed %s ms", 
								mine.getName(), 
								iFmt.format( locations.size() ),
								iFmt.format( pages ),
								dFmt.format(elapsedMs) );
				
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
		}
		
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

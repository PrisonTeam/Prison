package tech.mcprison.prison.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

/**
 * <p>This is based upon the open source project EsstentialsX and how they calculate
 * the server's TPS status. 
 * Source code can be found here:
 * </p>
 * 
 * @see <a href="https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/com/earth2me/essentials/EssentialsTimer.java">
 * EssentialsX source code for TPS calculations</a>
 * 
 * <h3>An Overview of TPS Calculations</h3>
 * 
 * <p>Under ideal server loads, a bukkit server should be able to maintain 20 Ticks Per Second
 * (20 TPS), which will help ensure a smooth and "mostly" lag free experience. A tick is 
 * based upon 50 milliseconds, and if there is no load on the server, or very light load, 
 * during part of that tick, the server will have nothing to do since it was able to 
 * complete all of it's work before the 50 millisecond window has passed.  If the 
 * server cannot completes all tasks within that 50 ms window, then the window gets 
 * pushed back and that tick increases in time until it is able to complete all
 * of it's calculations.
 * </p>
 * 
 * <p>So if a server is having troubles keeping up,
 * a tick may grow length of time, from 50 ms to 52 ms as an example, or even higher 
 * such as 65 ms.  There are no upper 
 * boundaries on how long a tick can last for, but out of safety concerns with the server, if
 * the tick 
 * becomes too large or too many ticks are impacted, then the server may take drastic actions
 * to protect itself, which may include shutting down.
 * </p>
 * 
 * <p>As the length of a tick increases in time it takes to complete, then less ticks can
 * complete per second. Ideally, if the server load is light, it will do nothing until a tick 
 * has completed.  So it will maintain, on average, a tick length of 50 ms, which has a 20 TPS, 
 * or 20 <b>T</b>icks <b>P</b>er <b>S</b>econd. But when load increases and a tick takes longer
 * than 50 ms to complete, then TPS will drop.  If the tick is increased by only 5 ms, then 
 * the average tick would be 55 ms in length and it will be 18.18 TPS 
 * (1000 ms/sec / 55 ms/tick = 18.18 ticks/sec ).  For a 
 * tick average of 60 ms, it will result in a 16.6 TPS (1000 / 60).  So even with an average 
 * tick length of 51 ms the result will be 19.6 TPS.
 * </p>
 * 
 * <p>It should be noted that it is normal for ticks to exceed 50 ms once in a while.  If it 
 * happens occasionally, then the server is able to recover and go back to an average of 
 * 20 TPS.  The problems become more obvious when a server is unable to return to a 20 TPS
 * status, and that's when it may become more noticeable that there are delays or noticeable 
 * lag. What the usable threshold is where it becomes a problem is beyond the scope of this 
 * discussion, and will vary based upon many factors that will vary from server to server.
 * So for one server a value of 18 TPS may be perfectly fine, but on another it may 
 * cause troubles with the game play.  Other servers may start to have issues around 16 TPS.
 * So TPS values less than 20 may not always be a problem, but it may be a symptom 
 * of an underlying issue that may need to be
 * found and addressed.  If you see your server is not hitting 
 * 20 TPS, don't panic since it may be perfectly acceptable and playable.
 * </p>
 * 
 * <p>Something new that I just discovered on my own, through the writing of the 
 * TPS code for this class, is that the bukkit server will try to make up for
 * longer ticks.  As stated before, when tick lengths increase, the TPS drops.  
 * What I noticed by monitoring "live" ticks, is that after bukkit encounters some
 * long ticks (lower TPS values), then it will try to "recover" by shortening the
 * duration of the next few ticks after it recovers. As a result, the TPS may increase
 * to a rate of 40 to 60 TPS, or even higher.  The goal is that when taking the average
 * of all ticks from before the initial drop in TPS to after it is done recovering, 
 * that the average should work out to be 20 TPS.  This adjustment is very helpful 
 * when dealing with the internal bukkit scheduling that is based upon ticks 
 * in the future.  If the average TPS is not brought back to a 20 TPS, then many 
 * recurring tasks will run at the incorrect times.  
 * </p>
 * 
 * <p>On a whole, shorter duration ticks make a ton of sense to keep the server
 * eco system healthy, but it was something that was completely unexpected. 
 * </p>
 * 
 * <h3>The theory behind TPS calculations</h3>
 * 
 * <p>Since bukkit tries to maintain a TPS of 20, even under light loads, a tool to calculate
 * the TPS can take advantage of this fact by employing the bukkit scheduler.  The scheduler 
 * has the ability to regularly perform the same task every X ticks.  Therefore, if a 
 * process tries to run at a set interval (every tick, or every 5 ticks, or even every 20
 * ticks), it can measure how long it takes to from the beginning of one task, to the beginning
 * of the next time it runs.  This can then be used to "calculate" how long a tick is lasting
 * through an indirect means by observing it's own task's behavior.
 * </p>
 * 
 * <p>This type of a calculation is not accurate by far.  This is because when a task is 
 * scheduled to run there is not guarantee when it will run, or where within a tick 
 * that it will be ran, or if that "position" will be consistent between runs.  
 * The window of execution within a tick can slide around from one run to another.
 * Ideally, if it can be the first process to be ran when a new tick
 * starts it would be very accurate.  But if during one tick it is first, but the next
 * tick it is ran in the middle of all of the other tasks ran during that tick, then 
 * it will introduce a lot of variability that could result in wild fluctuations in the
 * reported tick durations.  Remember, a 2 ms variation will result in about a 0.8 TPS 
 * change.  So if the task is being ran at different "places" within each tick, 
 * then the results can be misleading.  Therefore, to help combat these possible 
 * fluctuations, it is advisable to take an average number of measurements to smooth out
 * the spikes.  Also by submitting the job to run in an asynchronous mode will help 
 * ensure it runs sooner (as soon as a thread is available). Async may need to wait for
 * other tasks to complete, but there are more threads it can run the tasks, so it may 
 * have a better chance to run near the start of the tick.
 * </p>
 * 
 * 
 * <p>Prison's TPS calculations are taking averaging to a more primitive level. 
 * Since prison does not need to know exactly how long each tick lasts, as the tick
 * is happening, Prison is sampling multiple ticks at one time. Internally, this is
 * represented within the constant SUBMIT_TICKS_INTERVAL.  Currently it's set to 
 * a value of 10, which represents 1/2 of a second, or 10 ticks.  So when Prison
 * takes readings on the system time, it does so at the first tick and then at the 
 * 10th tick.  So the duration actually spans 10 ticks.  It then divides that 
 * large number (measuring in nanos) by 10 to get the average tick duration for 
 * that window of ticks.  So the process of just calculating a TPS already is 
 * providing some degree of averaging.
 * </p>
 * 
 * <p>This kind of averaging is sufficient for prison's needs, but the side effects
 * since it spans 10 ticks, is that it will miss short lag conditions.  For example, 
 * if a server is running at 20 TPS, then at the start of a TPS calculation window, 
 * the server drops down to 10 TPS for 3 ticks, then bukkit recovers by shortening 
 * the next 4 ticks to bring the average back to 20 TPS.  Therefore, prison's TPS
 * calculations would be unable to detect that there was a fluctuation in TPS values,
 * and would report a TPS of 20.
 * </p>
 * 
 * <p>If in the future prison needs to monitor and react to each TPS, such as
 * a feedback-loop during very large mine resets, then the Prison TPS calculations
 * can shift to a single-tick calculation during the duration of when it is needed.
 * This dynamic adjusting the window size for TPS calculations could help 
 * Prison maintain a high degree of control over the monitoring of TPS and
 * provide live adjustments when needed.
 * </p>
 * 
 * <p>Since Prison has a multi-tick interval which averages the TPS value, and since
 * that interval is 30 seconds on average, the storage of those TPS readings in
 * a collection should be used with caution.  The collection is currently storing
 * 20 readings, which represents a total of 10 seconds.  If the reported 
 * average is taken with all readings, then the reported TPS will always be the 
 * average of the "last 10 seconds".  That could be misleading and will 
 * flatten possible spikes and dips.  Since the "live" TPS reading is already
 * averaged over 10 ticks, averaging over "more" readings should be held to
 * a minimum at best.
 * </p>
 * 
 * 
 * <p>Disclaimer: This is my theory, based upon my understanding of how TPS are structured and how 
 * they work, and respond to server loads.  This theory is also based upon understanding
 * of how the EssentialsX code works and filling in the gaps.
 * </p> 
 *
 */
public class PrisonTPS
		implements PrisonRunnable {
	
	/**
	 * Note: Normally the task would have to run each tick, but by specifying the
	 * SUBMIT_TICKS_INTERVAL it can skip a number of ticks to reduce the 
	 * processing overhead.
	 */
	public static final double SUBMIT_TICKS_INTERVAL = 10;
	public static final double SUBMIT_TICKS_INTERVAL_HIGH_RESOLUTION = 2;
	
	// When enabled for highResolution the resolution will be set to submit_ticks_interval == 1,
	// false will be the value of SUBMIT_TICKS_INTERVAL:
	public boolean highResolution = false;
	
	
	// TPS_AVERAGE_READINGS_TO_INCLUDE needs to include more than just a few TPS 
	// points to prevent wild fluxuations.  With interval at 10 and a total 
	// duration of 120 ticks, then the readings will be 12 data points. Less will 
	// reflect variations faster, more will smooth out the bumps and dips.
	public static final double TARGET_TPS_AVERAGE_DURATION_IN_TICKS = 120;
	public static final double TPS_AVERAGE_READINGS_TO_INCLUDE = 
							TARGET_TPS_AVERAGE_DURATION_IN_TICKS / SUBMIT_TICKS_INTERVAL;
	
	
	public static final long TICKS_PER_SECOND = 20;
	public static final long TICK_INTERVAL_MS = 50; // 1 tick = 50 ms
	public static final long NANOS_PER_MS = 1000000; // 1 ms = 1,000,000 nano
	public static final long TICK_INTERVAL_NANO = TICK_INTERVAL_MS * NANOS_PER_MS; // 1 tick = 50,000,000 nano
	
	public static final double NANOS_PER_SECOND = TICKS_PER_SECOND * TICK_INTERVAL_NANO;
	
	public static final double TPS_THRESHOLD_TO_RECORD = 25.0d;
	
	public static final int MAX_TPS_HISTORY_SIZE = 20;
	
	private final List<Double> tpsHistory = new ArrayList<>();
	private transient double tpsMin = 20.0d;
	private transient double tpsMax = 20.0d;
	private transient long tpsSamples = 0;
	
	private transient long lastPollNano = System.nanoTime();
	
	private final DecimalFormat tpsFmt = Prison.get().getDecimalFormat("#,##0.00");
	
	public static final Object tpsLock = new Object();
	
	
	// When submitted, taskId identifies the job.  A value of -1 indicates the job
	// failed to be submitted, or is not valid.
	private int taskId = -1;
	
	public void submitAsyncTPSTask() {
		lastPollNano = System.nanoTime();
		
		if ( taskId > -1 ) {
			PrisonTaskSubmitter.cancelTask( taskId );
			
			// reset the key values:
			tpsHistory.clear();;
			tpsMin = 20.0d;
			tpsMax = 20.0d;
			tpsSamples = 0;
			
			lastPollNano = System.nanoTime();
			
		}
		
		int submitInterval = isHighResolution() ? 1 : (int) SUBMIT_TICKS_INTERVAL;
		int taskId = PrisonTaskSubmitter.runTaskTimerAsync( this, 0, submitInterval );
		
		this.taskId = taskId;
	}
	
	
	public void run() {
		
		final long timeStartNano = System.nanoTime();
		final long timeSpentNano = timeStartNano - lastPollNano;
		
		if ( timeSpentNano > 0 ) {
			
			final double tps = NANOS_PER_SECOND * SUBMIT_TICKS_INTERVAL / timeSpentNano;
			
//			Output.get().logInfo( "### PrisonTPS:: tps= %s timeSpentNano= %s  SUBMIT_TICKS_INTERVAL= %s", 
//					tpsFmt.format( tps ), tpsFmt.format( timeSpentNano ), SUBMIT_TICKS_INTERVAL );
			
			// Note: Keep all TPS readings.  Values greater than TPS_THRESHOLD_TO_RECORD
			//       Will be excluded when calculating the average value. 
//			if ( tps <= TPS_THRESHOLD_TO_RECORD ) 
			
			
			// NOTE: Adding and deleting entries within the tpsHistory collection must
			//       be synchronized due to other threads trying to read from this 
			//       collection concurrently.  
			// Using synchronized blocks to minimize blocking.
			synchronized ( tpsLock ) {
				
				tpsHistory.add( tps );
				
				if ( tpsHistory.size() > MAX_TPS_HISTORY_SIZE ) {
					tpsHistory.remove( 0 );
				}
			}
			
			// Increment sample count:
			tpsSamples++;
			
			// Record min and max values:
			if ( tps < tpsMin ) {
				tpsMin = tps;
			}
			else if ( tps > tpsMax ) {
				// Max cap at 100 since it can go very high when recovering
				tpsMax = tps > 100 ? 100 : tps;
			}
		}
		
		lastPollNano = timeStartNano;
	}
	
	
	
	public boolean isHighResolution() {
		return highResolution;
	}
	
	/**
	 * <p>If changing the resolution, then resubmit this task so
	 * it will have the low resolution (10 ticks) or high resolution
	 * (1 tick).
	 * </p>
	 * 
	 * @param highResolution
	 */
	public void setHighResolution( boolean highResolution ) {
		this.highResolution = highResolution;
		
		submitAsyncTPSTask();
	}


	/**
	 * <p>Note that the individual TPS entries have already been
	 * averaged based upon the value of SUBMIT_TICKS_INTERVAL, which
	 * happens to be 10 ticks at the moment this doc has been
	 * written/updated. Therefore the number of additional readings
	 * to be included in the "average" should be minimal, or no
	 * more than the last actual reading.
	 * </p>
	 * 
	 * <p>Consideration should be given to how "long" of a duration
	 * the average should cover.  If it's more than just the last
	 * reading, such as for a duration of 1 second, then this
	 * function may need to dynamically calculate how many readings
	 * should be included.  Or better yet, it should probably be
	 * set as a constant to minimize on the fly calculations.
	 * </p>
	 * 
	 * @return
	 */
    public double getAverageTPS() {
    	double avg = 0d;
    	
    	int cnt = 0;
    	
    	// Start collecting readings from the tail-end of the tpsHistory collection
    	// since that is the most recent reading:
    	synchronized ( tpsLock ) {
    		
    		for ( int i = tpsHistory.size(); i > 0; i-- ) {
    			double reading = tpsHistory.get( i - 1 );
    			
    			// Ignore readings above TPS_THRESHOLD_TO_RECORD
    			if ( reading <= TPS_THRESHOLD_TO_RECORD ) {
    				avg += reading;
    				
    				// Once we get our target count, then break out of the for loop:
    				if ( cnt++ < TPS_AVERAGE_READINGS_TO_INCLUDE ) {
    					break;
    				}
    			}
    		}
    	}

    	// Do not divide avg by count if zero or one:
    	if ( cnt > 1 ) {
    		avg /= cnt;
    	}
    	return avg;

    	// The following was averaging all counts in the history, including very high values.
    	// This is no longer favorable.
//        for (final Double f : tpsHistory) {
//            if (f != null) {
//                avg += f;
//            }
//        }
//        return tpsHistory.size() == 0 ? 0 : avg / tpsHistory.size();
    }
    
    
    public String getAverageTPSFormatted() {
    	return tpsFmt.format( getAverageTPS() );
    }
    
    public String getTPSMinFormatted() {
    	return tpsFmt.format( getTpsMin() );
    }
    
    public String getTPSMaxFormatted() {
    	return tpsFmt.format( getTpsMax() );
    }
    
    
    public String getLastFewTPS() {
    	StringBuilder sb = new StringBuilder();
    	
    	int cnt = 0;
    	
    	synchronized ( tpsLock ) {
    		
    		for ( int i = tpsHistory.size(); i > 0 && cnt++ < 15; i-- ) {
    			sb.append( tpsFmt.format( tpsHistory.get( i - 1 ) ) ).append( "  " );
    		}
    	}
    	
    	return sb.toString();
    }


	public double getTpsMin() {
		return tpsMin;
	}
	public void setTpsMin( double tpsMin ) {
		this.tpsMin = tpsMin;
	}

	public double getTpsMax() {
		return tpsMax;
	}
	public void setTpsMax( double tpsMax ) {
		this.tpsMax = tpsMax;
	}

	public long getTpsSamples() {
		return tpsSamples;
	}
	public void setTpsSamples( long tpsSamples ) {
		this.tpsSamples = tpsSamples;
	}    

}

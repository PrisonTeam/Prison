package tech.mcprison.prison.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
 * a tick may grow from 50 ms to 52 ms, or even higher such as 65 ms.  There are no upper 
 * boundaries on how long a tick can last for, but out of safety concerns with the server, if it 
 * becomes too large or too many ticks are impacted, then the server may take drastic actions
 * to protect itself, which may include shutting down.
 * </p>
 * 
 * <p>As the length of a tick increases in time it takes to complete, then less ticks can
 * complete per second. Ideally, if the server load is light, it will do nothing until a tick 
 * has completed.  So it will maintain, on average, a tick length of 50 ms, which has a 20 TPS, 
 * or 20 <b>T</b>icks <b>P</b>er <b>S</b>econd. But when load increases and a tick takes longer
 * than 50 ms to complete, then TPS will drop.  If the tick is increased by only 5 ms, then 
 * the average tick would be 55 ms in length and it will be 18.18 TPS (1000 / 55).  For a 
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
 * So TPS values less than 20 may not be a problem, but it may be a symptom of an underlying
 * issue that may need to be found and addressed.  If you see your server is not hitting 
 * 20 TPS, don't panic since it may be perfectly acceptable and playable.
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
 * that it will be ran.  Ideally, if it can be the first process to be ran when a new tick
 * starts it would be very accurate.  But if during one tick it is first, but the next
 * tick it is ran in the middle of all of the other tasks ran during that tick, then 
 * it will introduce a lot of variability that could result in wild fluctuations in the
 * reported tick durations.  Remember, a 2 ms variation will result in about a 0.8 TPS 
 * change.  So if the task is being ran each time at different "places" within the tick, 
 * then the results can be misleading.  Therefore, to help combat these possible 
 * fluctuations, it is advisable to take an average number of measurements to smooth out
 * the spikes.  Also by submitting the job to run in an asynchronous mode will help 
 * ensure it runs sooner (as soon as a thread is available). Async may need to wait for
 * other tasks to complete, but there are more threads it can run the tasks, so it may 
 * have a better chance to run near the start of the tick.
 * </p>
 * 
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
	
	public static final long TICKS_PER_SECOND = 20;
	public static final long TICK_INTERVAL_MS = 50; // 1 tick = 50 ms
	public static final long NANOS_PER_MS = 1000000; // 1 ms = 1,000,000 nano
	public static final long TICK_INTERVAL_NANO = TICK_INTERVAL_MS * NANOS_PER_MS; // 1 tick = 50,000,000 nano
	
	public static final double NANOS_PER_SECOND = TICKS_PER_SECOND * TICK_INTERVAL_NANO;
	
	public static final double TPS_THRESHOLD_TO_RECORD = 25.0d;
	
	public static final int MAX_TPS_HISTORY_SIZE = 20;
	
	private final List<Double> tpsHistory = new ArrayList<>();
	
	private transient long lastPollNano = System.nanoTime();
	
	private final DecimalFormat tpsFmt = new DecimalFormat("#,##0.00");
	
	
	public void submitAsyncTPSTask() {
		lastPollNano = System.nanoTime();
		
		PrisonTaskSubmitter.runTaskTimerAsync( this, 0, (int) SUBMIT_TICKS_INTERVAL );
	}
	
	
	public void run() {
		
		final long timeStartNano = System.nanoTime();
		final long timeSpentNano = timeStartNano - lastPollNano;
		
		if ( timeSpentNano > 0 ) {
			
			final double tps = NANOS_PER_SECOND * SUBMIT_TICKS_INTERVAL / timeSpentNano;
			
//			Output.get().logInfo( "### PrisonTPS:: tps= %s timeSpentNano= %s  SUBMIT_TICKS_INTERVAL= %s", 
//					tpsFmt.format( tps ), tpsFmt.format( timeSpentNano ), SUBMIT_TICKS_INTERVAL );
			
			if ( tps <= TPS_THRESHOLD_TO_RECORD ) {
				
				tpsHistory.add( tps );
				
				if ( tpsHistory.size() > MAX_TPS_HISTORY_SIZE ) {
					tpsHistory.remove( 0 );
				}
			}
		}
		
		lastPollNano = timeStartNano;
	}
	
    public double getAverageTPS() {
        double avg = 0d;
        for (final Double f : tpsHistory) {
            if (f != null) {
                avg += f;
            }
        }
        return tpsHistory.size() == 0 ? 0 : avg / tpsHistory.size();
    }
    
    public String getAverageTPSFormatted() {
    	return tpsFmt.format( getAverageTPS() );
    }
    
    public String getLastTPS() {
    	StringBuilder sb = new StringBuilder();
    	
    	int cnt = 0;
    	for ( int i = tpsHistory.size(); i > 0 && cnt++ < 10; i-- ) {
    		sb.append( tpsFmt.format( tpsHistory.get( i - 1 ) ) ).append( "  " );
    	}
    	
    	return sb.toString();
    }
    
	
}

package tech.mcprison.prison.internal;

public class PrisonStatsElapsedTimeNanos
{
	
	private long elapsedTimeNanos;
	
	public PrisonStatsElapsedTimeNanos() {
		super();
		
	}
	
	public synchronized void addNanos( Long nanos ) {
		
		this.elapsedTimeNanos += nanos;
	}

	public long getElapsedTimeNanos()
	{
		return elapsedTimeNanos;
	}

	public void setElapsedTimeNanos( long elapsedTimeNanos )
	{
		this.elapsedTimeNanos = elapsedTimeNanos;
	}

}

package tech.mcprison.prison.placeholders;

public class PlaceholderStatsData {
	
	private String placeholderId;
	
	private PlaceHolderKey placeholderKey;
//	private PrisonPlaceHolders placeholder;
	
	private int hits = 0;
	
	private long totalDurationNanos = 0L;
	
	private transient final Object lock;
	
	public PlaceholderStatsData( String placeholderId ) {
		super();
		
		this.lock = new Object();
		
		this.placeholderId = placeholderId;
		
	}

	public void logHit( long nanoStart, long nanoEnd ) {
		long durationNano = nanoEnd - nanoStart;
		
		synchronized ( lock ) {
			hits++;
			totalDurationNanos += durationNano;
		}
	}
	
	public double getAverageDurationMs() {
		double avgMs = totalDurationNanos / hits / 1000000.0d;
		
		return avgMs;
	}
	
	public String getPlaceholderId() {
		return placeholderId;
	}
	public void setPlaceholderId(String placeholderId) {
		this.placeholderId = placeholderId;
	}

	public PlaceHolderKey getPlaceholderKey() {
		return placeholderKey;
	}
	public void setPlaceholderKey(PlaceHolderKey placeholderKey) {
		this.placeholderKey = placeholderKey;
	}

	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}

	public long getTotalDurationNanos() {
		return totalDurationNanos;
	}
	public void setTotalDurationNanos(long totalDurationNanos) {
		this.totalDurationNanos = totalDurationNanos;
	}

}

package tech.mcprison.prison.placeholders;

public class PlaceholderStatsData {
	
	private String placeholderId;
	
	private PlaceHolderKey placeholderKey;
//	private PrisonPlaceHolders placeholder;
	
	private int hits = 0;
	private int failHits = 0;
	
	private long totalDurationNanos = 0L;
	
	/**
	 * A failedMatch will be identified if a placeholder key cannot be located for
	 * the placeholder pattern.
	 */
	private boolean failedMatch;
	
	private transient final Object lock;
	
	public PlaceholderStatsData( String placeholderId ) {
		super();
		
		this.lock = new Object();
		
		this.placeholderId = placeholderId;
		
		this.failedMatch = false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("PlaceholderStatsData: ").append( placeholderId )
			.append( "  placeholderKey: " ).append(
					placeholderKey == null ? "-null-" :
						placeholderKey.getKey() + ":" + placeholderKey.getPlaceholder().name()
					);
		sb.append( " failedMatch: " ).append( failedMatch );
		
		return sb.toString();
	}
	
	/**
	 * <p>This function will update the placeholderStats for both successful and failed
	 * hits.  This function will update the cached stats' placeholderKey if 
	 * the PlaceholderIdentifier has been matched.
	 * </p>
	 * 
	 * <p>If a placeholderKey has not been found for the identifier, then this 
	 * cached stats entry will mark the identifier as a failed lookup so 
	 * on future hits, it will not waste time trying to lookup bad placeholders.
	 * </p> 
	 * 
	 * @param pId
	 * @param nanoStart
	 * @param nanoEnd
	 */
	public void updateStats(PlaceholderIdentifier pId, long nanoStart, long nanoEnd) {


		// If the stats placeholderKey is null, and one has been identified with the pId,
		// then set the placeholderKey.
		if ( getPlaceholderKey() == null && pId.getPlaceholderKey() != null ) {
			setPlaceholderKey( pId.getPlaceholderKey() );
		}
		
		// Since a match was not found, try to record that the match failed.
		// The success may vary between players, but as long as there is a placeholderKey
		// Associated with the stats and identifier, it will not become a perm failure.
		if ( !pId.isFoundAMatch() ) {

			logFailedHit(nanoStart, nanoEnd);
		}
		else {
			
			// Successfully found... so log it:
			logHit( nanoStart, nanoEnd );
		}
		
	}

	
	/**
	 * <p>This is logging a successful hit for the placeholder.
	 * </p>
	 * 
	 * @param nanoStart
	 * @param nanoEnd
	 */
	private void logHit( long nanoStart, long nanoEnd ) {
		long durationNano = nanoEnd - nanoStart;
		
		synchronized ( lock ) {
			hits++;
			totalDurationNanos += durationNano;
		}
	}
	
	/**
	 * <p>This function records that a match to find the correct placeholder key 
	 * failed and should be recored as a failed match, but ONLY if the
	 * placeholderKey is null (no associations).  
	 * A failed match bypasses look ups on future placeholder matches.
	 * </p>
	 * 
	 * <p>A placeholder cannot be marked as failed if it contains
	 * an actual placeholder key.  A placeholder key will be assigned even if 
	 * an associated player is not valid within prison.
	 * </p>
	 * 
	 */
	private void logFailedHit( long nanoStart, long nanoEnd ) {
		long durationNano = nanoEnd - nanoStart;
		
		synchronized ( lock ) {
			failHits++;
			totalDurationNanos += durationNano;

			if ( placeholderKey == null && !failedMatch ) {
				failedMatch = true;
			}
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
//	private void setHits(int hits) {
//		this.hits = hits;
//	}

	public int getFailHits() {
		return failHits;
	}
//	private void setFailHits(int failHits) {
//		this.failHits = failHits;
//	}

	public long getTotalDurationNanos() {
		return totalDurationNanos;
	}
	public void setTotalDurationNanos(long totalDurationNanos) {
		this.totalDurationNanos = totalDurationNanos;
	}


	public boolean isFailedMatch() {
		return failedMatch;
	}
//	private void setFailedMatch(boolean failedMatch) {
//		this.failedMatch = failedMatch;
//	}


}

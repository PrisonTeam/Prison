package tech.mcprison.prison.placeholders;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;

public class PlaceholdersStats {

	private static PlaceholdersStats stats; 
	
	TreeMap<String, PlaceholderStatsData> placeholders;
	
	private PlaceholdersStats() {
		super();
		
		
		this.placeholders = new TreeMap<>();
	}
	
	public static PlaceholdersStats getInstance() {
		
		if ( stats == null ) {
			synchronized ( PlaceholdersStats.class ) {
				if ( stats == null ) {
					stats = new PlaceholdersStats();
				}
			}
		}
		
		return stats;
	}
	
	
	public PlaceholderStatsData getStats( PlaceholderIdentifier pId ) {
		PlaceholderStatsData results = null;
		
		
		if ( pId != null ) {
			String key = pId.getIdentifier();
			
			if ( getPlaceholders().containsKey(key) ) {
				results = getPlaceholders().get( key );
				
				if ( results.getPlaceholderKey() != null ) {
					pId.setPlaceholderKey( results.getPlaceholderKey() );
				}
				
			}
			else {
				results = new PlaceholderStatsData( key );
				getPlaceholders().put( key, results );
				
				// Store this new stats object in the cache.  If there is a placeholder fail, then
				// this will help prevent going through all of the calculations for future
				// hits.
				getPlaceholders().put( key, results );
			}
			
		}
		
		return results;
	}
	
	
	/**
	 * <p>This function will record the stats, which is the nano runtime, and also store the 
	 * PlaceholderKey in the Stats if it's not null.  
	 * </p>
	 * 
	 * <p>The stats object has already been stored in the cache so there is no reason to 
	 * add it again.
	 * </p>
	 * 
	 * @param pId
	 * @param stats
	 * @param nanoStart
	 * @param nanoEnd
	 * @return
	 */
	public PlaceholderStatsData setStats( PlaceholderIdentifier pId, PlaceholderStatsData stats, 
				long nanoStart, long nanoEnd ) {
		PlaceholderStatsData results = null;
		
		
		if ( pId != null && stats != null ) {
			//String key = pId.getIdentifier();
			
			if ( stats.getPlaceholderKey() == null && pId.getPlaceholderKey() != null ) {
				stats.setPlaceholderKey( pId.getPlaceholderKey() );
			}
			
			stats.logHit( nanoStart, nanoEnd );
			
			
			if ( !pId.isFoundAMatch() ) {
				stats.setFailedMatch( true );
			}
			
			
//			// If it contains
//			if ( !getPlaceholders().containsKey(key) ) {
//			
//				
//			}
			
		}
		
		return results;
	}
	
	
	public ArrayList<String> generatePlaceholderReport() {
		ArrayList<String> results = new ArrayList<>();
		
		DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0000" );
		
		results.add( 
				"&7  Hits      Avg/Hit ms  Placeholder" );
		
		ArrayList<String> keys = new ArrayList<>( getPlaceholders().keySet() );
		Collections.sort( keys );
		for (String key : keys) {
			PlaceholderStatsData stats = getPlaceholders().get(key);
			
			int hits = stats.getHits();
			long totalDurationNano = stats.getTotalDurationNanos();
			double avgMs = totalDurationNano / (double) hits / 1000000d;
			
			boolean valid = stats.getPlaceholderKey() != null;
			
			String message = String.format( 
					"&3%10s  %10s  %s  %s",
					iFmt.format( hits ),
					dFmt.format( avgMs ),
					key,
					( valid ? "" :
						stats.isFailedMatch() ? 
								"&cInvalid: bypassing lookups." :
							"&cInvalid: No match.")
					);
			results.add( message );
			
		}
		
		return results;
	}
	
	public void clearCache(boolean resetCache, boolean removeErrors) {

		List<String> removeKeys = new ArrayList<>();
		
		ArrayList<String> keys = new ArrayList<>( getPlaceholders().keySet() );
		for (String key : keys) {
			PlaceholderStatsData stats = getPlaceholders().get(key);
			
			if ( resetCache || removeErrors && stats.isFailedMatch() ) {
				removeKeys.add( key );
			}
		}

		for (String key : removeKeys) {
			getPlaceholders().remove( key );
		}
		
		
		Output.get().logInfo( "PlaceholderStats: Cache was purged of %s placeholders. Removed: %s ", 
				resetCache ? "all" : 
					removeErrors ? "invalid" : "some",
				Prison.getDecimalFormatStaticInt().format( removeKeys.size() )
				);
		
	}
	

	public TreeMap<String, PlaceholderStatsData> getPlaceholders() {
		return placeholders;
	}
	public void setPlaceholders(TreeMap<String, PlaceholderStatsData> placeholders) {
		this.placeholders = placeholders;
	}
	
}

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
	
	
	/**
	 * <p>This function will take the initial PlaceholderIdentifier, which has not yet been
	 * mapped to a placeholderKey, and try to see if there is an entry within the 
	 * placeholder cache.  If there is, then it will use the cache version, and if that
	 * cache version contains a placeholderKey, then it will be copied to the pId.
	 * </p>
	 * 
	 * <p>If there is no cache entry, then one will be created, but it will be lacking
	 * a placeholderKey initially.
	 * </p>
	 * @param pId
	 * @return
	 */
	public PlaceholderStatsData getStats( PlaceholderIdentifier pId ) {
		PlaceholderStatsData results = null;
		
		
		if ( pId != null ) {
			String key = pId.getIdentifier();
			
			// If there is a placeholder cache entry, then get it from the cache:
			if ( getPlaceholders().containsKey(key) ) {
				results = getPlaceholders().get( key );

				// NOTE: the results may have a placeholderKey assigned, if it does, then 
				//       assign to the pId:
				if ( results != null && results.getPlaceholderKey() != null ) {
					pId.setPlaceholderKey( results.getPlaceholderKey() );
				}
			}
			else {
				// Else create a new cache entry:
				
				results = new PlaceholderStatsData( key );
//				getPlaceholders().put( key, results );
				
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
			
			// Update all of the stats details, including if it should mark the stats as a failure.
			stats.updateStats( pId, nanoStart, nanoEnd );
		}
		
		return results;
	}
	
	
	public ArrayList<String> generatePlaceholderReport() {
		ArrayList<String> results = new ArrayList<>();
		
		DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0000" );
		
		results.add( 
				"&7 &n     Hits&r  &n    Fails&r  &nAvg/Hit ms&r  &nPlaceholder used:internal           &r" );
		
		ArrayList<String> keys = new ArrayList<>( getPlaceholders().keySet() );
		Collections.sort( keys );
		for (String key : keys) {
			PlaceholderStatsData stats = getPlaceholders().get(key);
			
			int hits = stats.getHits();
			int fails = stats.getFailHits();
			long totalDurationNano = stats.getTotalDurationNanos();
			double avgMs = totalDurationNano / (double) (hits + fails) / 1000000d;
			
			boolean valid = stats.getPlaceholderKey() != null;
			
			String message = String.format( 
					"&3%10s %10s  %10s  &2%s%s &c%s",
					iFmt.format( hits ),
					iFmt.format( fails ),
					dFmt.format( avgMs ),
					key,
					stats.getPlaceholderKey() == null ? 
						"" : 
						"&7:&b" + stats.getPlaceholderKey().getPlaceholder().name(),
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

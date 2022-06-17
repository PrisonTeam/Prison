package tech.mcprison.prison.placeholders;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

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
			}
			
		}
		
		return results;
	}
	
	public PlaceholderStatsData setStats( PlaceholderIdentifier pId, PlaceholderStatsData stats, 
				long nanoStart, long nanoEnd ) {
		PlaceholderStatsData results = null;
		
		
		if ( pId != null && stats != null ) {
			String key = pId.getIdentifier();
			
			if ( stats.getPlaceholderKey() == null && pId.getPlaceholderKey() != null ) {
				stats.setPlaceholderKey( pId.getPlaceholderKey() );
			}
			
			stats.logHit( nanoStart, nanoEnd );
			
			// If it contains
			if ( !getPlaceholders().containsKey(key) ) {
			
				
			}
			
		}
		
		return results;
	}
	
	
	public ArrayList<String> generatePlaceholderReport() {
		ArrayList<String> results = new ArrayList<>();
		
		DecimalFormat iFmt = new DecimalFormat( "#,##0" );
		DecimalFormat dFmt = new DecimalFormat( "#,##0.0000" );
		
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
					( valid ? "" : "&cInvalid: No match.")
					);
			results.add( message );
			
		}
		
		return results;
	}

	public TreeMap<String, PlaceholderStatsData> getPlaceholders() {
		return placeholders;
	}
	public void setPlaceholders(TreeMap<String, PlaceholderStatsData> placeholders) {
		this.placeholders = placeholders;
	}
	
	
}

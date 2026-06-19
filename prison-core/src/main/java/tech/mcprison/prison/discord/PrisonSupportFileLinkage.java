package tech.mcprison.prison.discord;

import java.util.TreeMap;
import java.util.TreeSet;

import tech.mcprison.prison.output.Output;

public class PrisonSupportFileLinkage {

	public static final String LINKAGE_IDENTIFIER = "||";
	
	private TreeMap<PrimaryLinkages, TreeSet<String>> primaries = new TreeMap<>();
	
	public enum PrimaryLinkages {
		Ladder,
		Rank,
		Mine,
		Listeners,
		CommandStats,
		
		unknown
		;
		
		/**
		 * <p>This will take a raw linkage String, minus the 
		 * pipes, and extract a PrimaryLinkages from it.
		 * 
		 * 
		 * @param linkage
		 * @return
		 */
		public static PrimaryLinkages fromString( String linkage ) {
			PrimaryLinkages results = PrimaryLinkages.unknown;
			
			if ( linkage.contains( " " ) ) {
				String[] parts = linkage.split(" ");
				if ( parts != null && parts.length > 0 ) {
					linkage = parts[0];
				}
			}
			
			for ( PrimaryLinkages pl : values() ) {
				if ( pl.name().equalsIgnoreCase(linkage) ) {
					results = pl;
					break;
				}
			}
			
			return results;
		}
		
		
		public void scanForTOCEntries( String line ) {
			
			if ( line != null && line.startsWith( LINKAGE_IDENTIFIER ) ) {
				String[] parts = line.split(" ");
				
				PrimaryLinkages prime = PrimaryLinkages.fromString( parts[0] );
				if ( prime != PrimaryLinkages.unknown ) {
					
				}
				else {
					String msg = "Primary ID not setup for PrimaryLinkage: [" + line + "]";
					Output.get().logWarn(msg);
				}
			}
		}
		
	}
	
	public enum SecondaryLinkages {
		toc,
		listing,
		listingDetail,
		detail,
		config,
		file;
		
		public static SecondaryLinkages fromString( String secondary ) {
			SecondaryLinkages results = null;
			
			if ( secondary != null ) {
				
				for ( SecondaryLinkages scnd : values() ) {
					if ( secondary.toLowerCase().contains( scnd.name().toLowerCase() ) ) {
						results = scnd;
						break;
					}
				}
			}
			
			return results;
		}
	}
	
	private void addPrimaries( PrimaryLinkages pLink, String value ) {
		if ( pLink != null && pLink != PrimaryLinkages.unknown && value != null ) {
			
			if ( !getPrimaries().containsKey(pLink) ) {
				getPrimaries().put(pLink, new TreeSet<>() );
			}
			
			TreeSet<String> values = getPrimaries().get( pLink );
			
			if ( !values.contains( value ) ) {
				values.add( value );
			}
		}
	}

	public void addLinkage(String line) {
		
		if ( line != null && line.startsWith( LINKAGE_IDENTIFIER ) ) {
			
			String[] rawLinks = line.split("\\|\\|");
			
			for (String rawLink : rawLinks) {
				
				if ( rawLink != null && rawLink.length() > 0 ) {
					
					String[] parts = rawLink.split(" ");
					
					PrimaryLinkages pLink = PrimaryLinkages.fromString( parts[0] );
					
					String value = parts.length >= 2 ? parts[1] : null;
					
					addPrimaries( pLink, value );
				}
			}
		}
	}

	
	public TreeMap<PrimaryLinkages, TreeSet<String>> getPrimaries() {
		return primaries;
	}
	public void setPrimaries(TreeMap<PrimaryLinkages, TreeSet<String>> primaries) {
		this.primaries = primaries;
	}
	
}

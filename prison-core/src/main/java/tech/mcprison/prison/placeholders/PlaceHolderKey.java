package tech.mcprison.prison.placeholders;

import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;

public class PlaceHolderKey {
	
	private String key;
	private PrisonPlaceHolders placeholder;
	private String data;
	private boolean primary = true;
	private String aliasName;
	
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder ) {
		this(key, placeholder, true);
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, boolean primary ) {
		this.key = key;
		this.placeholder = placeholder;
		this.primary = primary;
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, String data ) {
		this(key, placeholder);
		this.data = data;
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, String data, boolean primary ) {
		this(key, placeholder, primary);
		this.data = data;
	}
	
	/**
	 * <p>This function will take a full <b>text</b> String and apply the placeholder that
	 * this PlaceHolderKey represents and tries to match it to the provided text.  If it 
	 * is found within the text, then this function will return the identifier, which is
	 * full text, including any placeholder attributes, but without the escape characters.
	 * </p>
	 * 
	 * @param text
	 * @return
	 */
	public PlaceholderResults getIdentifier( String text ) {
		PlaceholderResults results = new PlaceholderResults(this);

		String textLowercase = text.toLowerCase();
		String key = getKey().toLowerCase();
		
		checkIdentifier( key, text, textLowercase, "{", "}", results );
		
		if ( textLowercase.contains( key ) || 
				checkIdentifier( key, text, textLowercase, "{", "}", results ) ||
				checkIdentifier( key, text, textLowercase, "%", "%", results )
				) {
			
			// Nothing to do, since it was already done within this if statement.
			
			
//			// Performing all the String searching and indexing can be expensive, especially
//			// since there can be thousands of PlaceHolderKeys on a server.  So to provide
//			// a quick proof to see if additional, more complex calculations should be
//			// performed, we'll just see if the text input contains a hit on the key:  
//			
//			// Rank in to an issue with placeholders: prison_mbm_minename and prison_mbm_pm, 
//			// because the mine P has a placeholder prison_mbm_p which gets hit for the
//			// prison_mbm_pm.  So to zero in on the correct placeholder, but bracket the end
//			// of the placeholder with either } or :: to ensure the correct association.
//			String test1 = "{" + key + "}";
//			String test2 = "{" + key + 
//					PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR;
//			
//			if ( textLowercase.contains( test1 ) || textLowercase.contains( test2 ) ) {
//				
//				// The key1 and key2 helps ensure that the full placeholder, 
//				// including the attribute, is replaced:
//				String key1 = "{" + key;
//				String key2 = "}";
//				
//				int idx = textLowercase.indexOf( key1 );
//				int idx2 = ( idx == -1 ? -1 : textLowercase.indexOf( key2, idx + key1.length() - 1 ) );
//				if ( idx > -1 && idx2 > -1 ) {
//					
//					String identifier = text.substring( idx + 1, idx2 );
//					results.setIdentifier( identifier );
////					results = results.replace("{" + identifier + "}", 
////							pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, identifier ) );
//				}
//			}
			
		}

		
		return results;
	}
	
	
	private boolean checkIdentifier( String key, String text, String textLowercase,
				String escLeft, String escRight, PlaceholderResults results ) {
		boolean foundIdentifier = false;
		
		// Performing all the String searching and indexing can be expensive, especially
		// since there can be thousands of PlaceHolderKeys on a server.  So to provide
		// a quick proof to see if additional, more complex calculations should be
		// performed, we'll just see if the text input contains a hit on the key:  
		
		// Rank in to an issue with placeholders: prison_mbm_minename and prison_mbm_pm, 
		// because the mine P has a placeholder prison_mbm_p which gets hit for the
		// prison_mbm_pm.  So to zero in on the correct placeholder, but bracket the end
		// of the placeholder with either } or :: to ensure the correct association.
		String test1 = escLeft + key + escRight;
		String test2 = escLeft + key + 
				PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR;
		
		// Warning this is not case insensitive in the results:
		if ( textLowercase.contains( test1 ) ) {
			
			int idx = textLowercase.indexOf( test1 );
			int idxStart = idx + 1;
			int idxEnd = idx + test1.length() - 1;
			String identifier = text.substring( idxStart, idxEnd);
			
			results.setIdentifier( identifier, escLeft, escRight );
//			results.setIdentifier( key, escLeft, escRight );
			foundIdentifier = true;
		}
		else if ( text.contains( test2 ) ) {
			
			// The key1 and key2 helps ensure that the full placeholder, 
			// including the attribute, is replaced:
			String key1 = test2;
			String key2 = escRight;
			
			int idx = text.indexOf( key1 );
			int idx2 = ( idx == -1 ? -1 : text.indexOf( key2, idx + key1.length() - 1 ) );
			if ( idx > -1 && idx2 > -1 ) {
				
				String identifier = text.substring( idx + 1, idx2 );
				results.setIdentifier( identifier, escLeft, escRight );
				foundIdentifier = true;
//				results = results.replace("{" + identifier + "}", 
//						pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, identifier ) );
			}
		}
		
		return foundIdentifier;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "PlaceHolderKey: key=" ).append( getKey() )
				.append( "  placeholder=" ).append( getPlaceholder().name() )
				.append( "  isPriamary=" ).append( isPrimary() )
				.append( "  data=" ).append( getData() == null ? "" : getData() );
		
		return sb.toString();
	}

	public String getKey() {
		return key;
	}
	public void setKey( String key ) {
		this.key = key;
	}

	public PrisonPlaceHolders getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder( PrisonPlaceHolders placeholder ) {
		this.placeholder = placeholder;
	}

	public String getData() {
		return data;
	}
	public void setData( String data ) {
		this.data = data;
	}
	
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary( boolean primary ) {
		this.primary = primary;
	}
	
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName( String aliasName ) {
		this.aliasName = aliasName;
	}
}

package tech.mcprison.prison.placeholders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;

public class PlaceHolderKey {
	
	private String key;
	private PrisonPlaceHolders placeholder;
	private String data;
	private boolean primary = true;
	private String aliasName;
	
	
	// NOTE: Pattern is thread safe so make it static.  Matcher is not thead safe.
	public static Pattern PLACEHOLDER_SEQUENCE_PATTERN = Pattern.compile( "(\\_([0-9]+)\\_)" );
	
	
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
		PlaceholderResults results = new PlaceholderResults(this, text);

		
		String textLowercase = text.toLowerCase();
		String key = getKey().toLowerCase();
		
		
		// For placeholders with sequence numbers, such as _nnn_, will need to search
		// for 1 to 3 digits and replace the number in the "text" with _nnn_ and also
		// need to store that numeric value in the PlaceholderResults object.
		if ( getPlaceholder().hasSequence() ) {
			
			Matcher matcher = PLACEHOLDER_SEQUENCE_PATTERN.matcher( textLowercase );
			if ( matcher.find() ) {
				
				//String group0 = matcher.group( 0 );
				String group1 = matcher.group( 1 );
				String group2 = matcher.group( 2 );
				
				textLowercase = textLowercase.replace( group1, "_nnn_" );
				
//			Output.get().logInfo( "### PlaceHolderKey: seq pattern detected: " + placeholder.name() + 
//					"  group0= " + group0 + " group1= " + group1 + " group2= " + group2 + "  replacedText: " + textLowercase );
				
				results.setNumericSequencePattern( group1 );
				
				// a value of -1 indicates it was not able to be parsed:
				int parsed = -1;
				
				try {
					parsed = Integer.parseInt( group2 );
				}
				catch ( NumberFormatException e ) {
					// Not a number so ignore... but based upon matcher.find() it should be... Hmm...
				}
				
				results.setNumericSequence( parsed );
				
				
				// DO something more:
				
			}
		}
		
		
		// If the text is an exact match to the key (no escape characters):
		if ( textLowercase.equalsIgnoreCase( key ) ) {
			
			results.setIdentifier( textLowercase );
			results.setPlaceholder( this );
			
		}
		else {
			
			checkIdentifier( key, text, textLowercase, "{", "}", results );
			
			if ( textLowercase.equalsIgnoreCase( key ) ||
					textLowercase.contains( key ) || 
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
		
		int adjustment = 0;
		// If the text contains a sequence, then calculate the adjustment position based upon 
		// the length of '_nnn_' compared to the original value. 
		// These adjustments will align properly with 'text'. 
		if ( results.getNumericSequence() >= 0 && results.getNumericSequencePattern() != null ) {
			adjustment = 5 - results.getNumericSequencePattern().length();
		}

		// Warning this is not case insensitive in the results:
		if ( textLowercase.contains( test1 ) ) {
			
			int idx = textLowercase.indexOf( test1 );
			int idxStart = idx + 1;

			
			int idxEnd = idx + test1.length() - 1 - adjustment;
			String identifier = text.substring( idxStart, idxEnd);
			
			results.setIdentifier( identifier, escLeft, escRight );
			results.setPlaceholder( this );
			
//			results.setIdentifier( key, escLeft, escRight );
			foundIdentifier = true;
		}
		else if ( text.contains( test2 ) ) {
			
			// The key1 and key2 helps ensure that the full placeholder, 
			// including the attribute, is replaced:
			String key1 = test2;
			String key2 = escRight;
			
			int idx = text.indexOf( key1 );
			int idx2 = ( idx == -1 ? -1 : text.indexOf( key2, idx + key1.length() - 1 ) ) - adjustment;
			if ( idx > -1 && idx2 > -1 ) {
				
				String identifier = text.substring( idx + 1, idx2 );
				
				results.setIdentifier( identifier, escLeft, escRight );
				results.setPlaceholder( this );

				foundIdentifier = true;
//				results = results.replace("{" + identifier + "}", 
//						pm.getTranslatePlayerPlaceHolder( playerUuid, playerName, identifier ) );
			}
		}
//		else if ( textLowercase.contains( key ) ) {
//			
//			int idx = textLowercase.indexOf( key );
//			int idxStart = idx + 1;
//
//			
//			int idxEnd = idx + key.length() - 1 - adjustment;
//			String identifier = text.substring( idxStart, idxEnd);
//			
//			results.setIdentifier( identifier, "", "" );
//			results.setPlaceholder( this );
//			
////			results.setIdentifier( key, escLeft, escRight );
//			foundIdentifier = true;
//		}
		
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

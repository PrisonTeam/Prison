package tech.mcprison.prison.placeholders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceholderAttributePrefixes;

public class PlaceholderManagerUtils {
	
	private static PlaceholderManagerUtils instance = null;
	
	private PlaceholderManagerUtils() {
		super();
	}
	
	public static PlaceholderManagerUtils getInstance() {
		
		if ( instance == null ) {
			synchronized ( PlaceholderManagerUtils.class ) {
				if ( instance == null ) {
					instance = new PlaceholderManagerUtils();
				}
			}
		}
		
		return instance;
	}

	public String extractPlaceholder( String placeholder ) {
		String results = null;
		
		if ( placeholder != null ) {
			String[] parts = placeholder.split( PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR );
			
			// parts[0] will be the placeholder we need to return:
			if ( parts != null && parts.length > 0 ) {
				results = parts[0];
			}
		}
		
		return results;
	}

	
	private PlaceholderProgressBarConfig progressBarConfig;
	
	/**
	 * <p>This will extract attributes from dynamic placeholders and will return them either in an 
	 * empty array list (none), or one or more attributes. 
	 * </p>
	 * 
	 * <p>Planning on using : as separators.  :: for identifying each attribute, and then
	 * within each attribute : will separate the individual fields and values.  
	 * For example it a number format attribute could look like this: 
	 * </p>
	 * 
	 * NOTE: Not actual examples:
	 * 
	 * <pre>::nFormat:0.00{unit}</pre> for no spaces.  
	 * <pre>::nFormat:#,##0.0+{unit}</pre> for spaces since + will be converted to spaces.
	 * 
	 * @param placeholder
	 * @return
	 */
	public ArrayList<PlaceholderAttribute> extractPlaceholderAttributes( String placeholder ) {
		ArrayList<PlaceholderAttribute> attributes = new ArrayList<>();
		
		if ( placeholder != null ) {
			String[] parts = placeholder.split( PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR );
			
			// parts[0] will be the placeholder, so ignore:
			if ( parts != null && parts.length > 1 ) {
				for ( int i = 1; i < parts.length ; i++ ) {
					String rawAttribute = parts[i];
					
					if ( rawAttribute != null ) {
						PlaceholderAttribute attribute = attributeFactory( rawAttribute );
						
						if ( attribute != null ) {
							
							attributes.add( attribute );
						}
					}
				}
			}
		}
		
		return attributes;
	}
	
	private PlaceholderAttribute attributeFactory( String rawAttribute ) {
		PlaceholderAttribute attribute = null;
		
		if ( rawAttribute != null && !rawAttribute.isEmpty() ) {
			ArrayList<String> parts = new ArrayList<>();
			parts.addAll( Arrays.asList( 
						rawAttribute.split( PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR )) );
			
			if ( parts.size() >= 0 ) {
				PlaceholderAttributePrefixes pap = PlaceholderAttributePrefixes.fromString( parts.get( 0 ) );
				
				if ( pap != null ) {
					
					switch ( pap )
					{
					case nFormat:
						attribute = new PlaceholderAttributeNumberFormat( parts, rawAttribute );
						break;
						
					case bar:
						attribute = new PlaceholderAttributeBar( parts, getProgressBarConfig(), rawAttribute );
						break;
						
					case text:
						attribute = new PlaceholderAttributeText( parts, rawAttribute );
						break;
						
					case time:
						// Insert the dummy value "format" as the second element of 'parts' since Time does not use it:
						parts.add(1, "");
						attribute = new PlaceholderAttributeTime( parts, rawAttribute );
						break;
						
					default:
						break;
					}
					
				}
			}
			
		}
		
		return attribute;
	}
	
	public void reloadPlaceholderBarConfig() {
		setProgressBarConfig( loadPlaceholderBarConfig() );
	}
	
	public PlaceholderProgressBarConfig loadPlaceholderBarConfig() {
		PlaceholderProgressBarConfig config = null;
		
		Platform platform = Prison.get().getPlatform();
		
		if ( platform != null ) {
			
			String barSegmentsStr = platform.getConfigString( 
					"placeholder.bar-segments" );
			String barPositiveColor = Prison.get().getPlatform().getConfigString( 
					"placeholder.bar-positive-color" );
			String barPositiveSegment = Prison.get().getPlatform().getConfigString( 
					"placeholder.bar-positive-segment" );
			String barNegativeColor = Prison.get().getPlatform().getConfigString( 
					"placeholder.bar-negative-color" );
			String barNegativeSegment = Prison.get().getPlatform().getConfigString( 
					"placeholder.bar-negative-segment" );
			
			
			// All 5 must not be null:
			if ( barSegmentsStr != null && barPositiveColor != null && barPositiveSegment != null &&
					barNegativeColor != null && barNegativeSegment != null ) {
				
				int barSegments = 20;
				
				try {
					barSegments = Integer.parseInt( barSegmentsStr );
				}
				catch ( NumberFormatException e ) {
					Output.get().logWarn( 
							"IntegrationManager.loadPlaceholderBarConfigs(): Failure to convert the" +
									"/plugins/Prison/config.yml  prison-placeholder-configs.progress-bar.bar-segments " +
									"to a valid integer. Defaulting to a value of 20 " +
									"[" + barSegmentsStr + "] " + e.getMessage() );
					
				}
				
				config = new PlaceholderProgressBarConfig( barSegments, 
						barPositiveColor, barPositiveSegment,
						barNegativeColor, barNegativeSegment );
			}
		}
		
		if ( config == null ) {
			// go with default values because the config.yml is not up to date with
			// the default values
			
			config = new PlaceholderProgressBarConfig( 
					20, "&2", "#", "&4", "=" 
//					20, "&2", "▊", "&4", "▒" 
					);
			
			Output.get().logInfo( "The /plugins/Prison/config.yml does not contain the " +
					"default values for the Placeholder Progress Bar." );
			Output.get().logInfo( "Default values are " +
					"being used. To customize the bar, rename the config.yml and it will be " +
					"regenerated and then edit to restore prior values.");
			
		}

		return config;
	}
	
    public PlaceholderProgressBarConfig getProgressBarConfig() {
    	if ( progressBarConfig == null ) {
    		progressBarConfig = loadPlaceholderBarConfig();
    	}
		return progressBarConfig;
	}
	public void setProgressBarConfig( PlaceholderProgressBarConfig progressBarConfig ) {
		this.progressBarConfig = progressBarConfig;
	}

	/**
	 * <p>This function uses the settings within the config.yml to construct a progress
	 * bar.  It takes two numeric values and constructs it upon those parameters.
	 * The parameter <pre>value</pre> is the value that changes, and is the value that 
	 * sets where the bar changes.  The parameter <pre>valueTotal</pre> is the max value
	 * of where the <pre>value</pre> is increasing to.
	 * </p>
	 * 
	 * <p>The lowest range is always zero and <pre>value</pre> will be set to zero if 
	 * it is negative.   If <pre>value</pre> is greater than <pre>valueTotal</pre>
	 * then it will be set to that value.  The valid range for this function is only 0 percent 
	 * to 100 percent.
	 * </p>
	 * 
	 * <p>If the progress bar is moving in the wrong direction, then set the parameter
	 * <pre>reverse</pre> to true and then the <pre>value</pre> will be inverted by subtracting
	 * its value from <pre>valueTotal</pre>.
	 * </p>
	 * 
	 * @param value A value that is changing. Will be set to zero if negative. Will be 
	 * 				set to valueTotal if greater than that amount. 
	 * @param valueTotal The target value that is non-changing.
	 * @param reverse Changes the growth direction of the progress bar.
	 * @param attribute 
	 * @return
	 */
	public String getProgressBar( double value, double valueTotal, boolean reverse, 
															PlaceholderAttributeBar attributeBar ) {
		StringBuilder sb = new StringBuilder();
		
		// value cannot be greater than valueTotal:
		if ( value > valueTotal ) {
			value = valueTotal;
		}
		else if ( value < 0 ) {
			value = 0;
		}
		
		// If reverse, then the new value is subtracted from valueTotal:
		if ( reverse ) {
			value = valueTotal - value;
		}
		
    	double percent = valueTotal == 0 ? 100d : value / valueTotal * 100.0;
    	
//    	PlaceholderAttributeBar barAttribute = attribute == null || 
//    							!(attribute instanceof PlaceholderAttributeBar) ? null : 
//    								(PlaceholderAttributeBar) attribute;
    	
//    	Output.get().logInfo( "### @@@ ### getProgressBar: barAttribute: " + 
//    				( barAttribute != null ? "true" : "false"));
    	
    	PlaceholderProgressBarConfig barConfig = 
    						attributeBar != null ? attributeBar.getBarConfig() :
    									getProgressBarConfig();

		String lastColorCode = null;
		int segments = barConfig.getSegments();
		for ( int i = 0; i < segments; i++ ) {
			double pct = i / ((double)barConfig.getSegments()) * 100.0;
			
			// If the calculated percent is less than the threshold and as long as it's not the last 
			// segment, then show a positive.  If it's the last segment an it's still less than 
			// the percent, then show a negative no matter what to indicate it's not yet there.
			if ( pct < percent && (percent == 100d || percent < 100d && i < segments - 1)) {
				if ( lastColorCode == null || 
						!barConfig.getPositiveColor().equalsIgnoreCase( lastColorCode )) { 
					sb.append( barConfig.getPositiveColor() );
					lastColorCode = barConfig.getPositiveColor();
				}
				sb.append( barConfig.getPositiveSegment() );
			}
			else {
				if ( lastColorCode == null || 
						!barConfig.getNegativeColor().equalsIgnoreCase( lastColorCode )) { 
					sb.append( barConfig.getNegativeColor() );
					lastColorCode = barConfig.getNegativeColor();
				}
				sb.append( barConfig.getNegativeSegment() );
				
			}
		}

		
		if ( barConfig.isReverse() ) {
			sb.reverse();
		}
		
    	
    	return sb.toString();
	}
	
	protected void convertPlaceholderSequence( PlaceholderIdentifier pIdentifier ) {
		
		Matcher matcher = PlaceholderManager.PLACEHOLDER_SEQUENCE_PATTERN.matcher( pIdentifier.getIdentifier() );
		if ( matcher.find() ) {
			
			pIdentifier.setHasSequence( true );
			
			//String group0 = matcher.group( 0 );
			String group1 = matcher.group( 1 );
			String group2 = matcher.group( 2 );
			
			pIdentifier.setIdentifier( pIdentifier.getIdentifier().replace( group1, "_nnn_" ) );
			
//		Output.get().logInfo( "### PlaceHolderKey: seq pattern detected: " + placeholder.name() + 
//				"  group0= " + group0 + " group1= " + group1 + " group2= " + group2 + "  replacedText: " + textLowercase );
			
			pIdentifier.setSequencePattern( group1 );
			
			// a value of -1 indicates it was not able to be parsed:
			int parsed = -1;
			
			try {
				parsed = Integer.parseInt( group2 );
			}
			catch ( NumberFormatException e ) {
				// Not a number so ignore... but based upon matcher.find() it should be... Hmm...
			}
			
			pIdentifier.setSequence( parsed );
			
		}
	}
	
	/**
	 * <p>This function checks to see if the raw identifier (placeholder) has any escape characters.  If it does, 
	 * then it stores them within escapeLeft and escapeRight and removes them from the identifierRaw.
	 * </p>
	 * 
	 * <p>If the "original" raw identifier is needed, then the 
	 * @param pIdentifier
	 */
	protected void convertPlaceholderEscapeCharacters( PlaceholderIdentifier pIdentifier ) {
		
		if ( pIdentifier.getIdentifierRaw().startsWith( "%" ) ) {
			pIdentifier.setEscapeLeft( "%" );
			pIdentifier.setIdentifierRaw( pIdentifier.getIdentifierRaw().substring(1) );
			
			if ( pIdentifier.getIdentifierRaw().endsWith( "%" ) ) {
				pIdentifier.setEscapeRight( "%" );
				
				int len = pIdentifier.getIdentifierRaw().length();
				pIdentifier.setIdentifierRaw( pIdentifier.getIdentifierRaw().substring(0, len - 1) );
			}
		}
		
		else if ( pIdentifier.getIdentifierRaw().startsWith( "{" ) ) {
			pIdentifier.setEscapeLeft( "{" );
			pIdentifier.setIdentifierRaw( pIdentifier.getIdentifierRaw().substring(1) );
			
			if ( pIdentifier.getIdentifierRaw().endsWith( "}" ) ) {
				pIdentifier.setEscapeRight( "}" );
				
				int len = pIdentifier.getIdentifierRaw().length();
				pIdentifier.setIdentifierRaw( pIdentifier.getIdentifierRaw().substring(0, len - 1) );
			}
		}
		
		else if ( !pIdentifier.getIdentifierRaw().startsWith( "_" ) ) {
			Matcher matcher = PlaceholderManager.PLACEHOLDER_ESCAPE_CHARACTER_LEFT_PATTERN.matcher( pIdentifier.getIdentifierRaw() );
			
			if ( matcher.find() ) {
				
				String group0 = matcher.group( 0 );
				
				pIdentifier.setEscapeLeft( group0 );
				pIdentifier.setIdentifierRaw( pIdentifier.getIdentifierRaw().substring(1) );
				
				Matcher matcherRight = PlaceholderManager.PLACEHOLDER_ESCAPE_CHARACTER_RIGHT_PATTERN.matcher( pIdentifier.getIdentifierRaw() );

				if ( matcherRight.find() ) {

					String group0Right = matcherRight.group( 0 );
					
					pIdentifier.setEscapeRight( group0Right );
					
					int len = pIdentifier.getIdentifierRaw().length();
					pIdentifier.setIdentifierRaw( pIdentifier.getIdentifierRaw().substring(0, len - group0Right.length() ) );
				}

			}
			
		}
		
	}
	

}

package tech.mcprison.prison.placeholders;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholderManager.NumberTransformationUnitTypes;
import tech.mcprison.prison.placeholders.PlaceholderManager.TimeTransformationUnitTypes;
import tech.mcprison.prison.util.Text;

/**
 * <p>This takes the placeholder attribute for time formatting
 * and parses the dynamic content to setup this instance.
 * Then this class applies the formatting to the generated placeholder 
 * results to produce the requested output.
 * </p>
 * 
 * <p>Usage:
 * </p>
 * <pre>::time:spaces:unitType:hex:hex2:debug:player=<playerName></pre>
 * 
 * <p>NOTE: 'format' is not used, so just collapse it and provide nothing as shown here.
 *          If any value is provided, it will be ignored.
 * <pre>::nFormat::spaces:unitType:hex:hex2:debug:player=<playerName></pre>
 * 
 * <ul>
 *   <li><b>time</b>: the keyword to identify this attribute.</li>
 *
 *   <li><b>spaces</b>: number of spaces between format and unit of measure. 
 *   				Optional. Defaults to 1.</li>
 *   <li><b>unitType</b>: unit type to display or to use to transform the results.
 *   				Optional. Defaults to 'long'. The control of these values
 *                  can be found in the core language files.
 *   				
 *   	<ul>
 *        <li><b>long</b>: Uses the long form of time and date units. Uses both the
 *        			'core_text__time_units_singular' and 
 *        			'core_text__time_units_plural' language file settings.</li>
 *        <li><b>short</b>: Uses the short form of time and date units. These are 
 *        			generally a single character each. Uses the
 *        			'core_text__time_units_short' language file settings.</li>
 *        <li><b>colons</b>: Uses colons for the units, except for year, month,
 *        			and week, it uses the same settings as found in the 
 *        			'short' unit</li>
 *   	</ul>
 *   </li>
 *   
 *   <li><b>hex</b>: Optional. Case sensitive. Non-positional; can be placed anywhere.
 *   				Only valid value is "hex". When enabled it will translate
 *   				hex color codes, and other color codes before sending the placeholder
 *   				results back to the requestor. This is useful for plugins that
 *   				do not directly support hex color codes.
 *   <li><b>hex2</b>: Optional. Case sensitive. Non-positional; can be placed anywhere.
 *   				Only valid value is "hex2". When enabled it will translate
 *   				hex color codes to their intermediate state, which uses '&' color 
 *   				codes, sending the placeholder results back to the requestor. 
 *   				This is useful for plugins that do not directly support hex 
 *   				color codes and may work when 'hex' does not.
 *   <li><b>debug</b>: Optional. Case sensitive. Non-positional; can be placed anywhere.
 *   				Only valid value is "debug". When enabled it
 *    				will log to the console the status of this attribute, along with
 *    				any error messages that may occur when applying the attribute.
 *   </li>
 *   <li><b>player=&lt;playerName&gt;</b>: Optional. Case insensitive. Non-positional; can be
 *   				placed anywhere.  Provides a player for the placeholder when the 
 *   				plugin requesting the placeholder cannot request it based upon the player.
 *   				</li>
 * </ul>
 * 
 *
 */
public class PlaceholderAttributeTime
	extends PlaceholderAttributeNumberFormat {

	private TimeTransformationUnitTypes timeUnitType;
	
//	private ArrayList<String> parts;
//	private String raw;
//	
//	private boolean hex = false;
//	private boolean hex2 = false;
//	private boolean debug = false;
//	
//	private String player = null;
	
	
	/**
	 * <p>The constructor parameters are exactly the same as the nFormat.
	 * </p>
	 * 
	 * @param parts
	 * @param raw
	 */
	public PlaceholderAttributeTime( ArrayList<String> parts, String raw ) {
		super( parts, raw);
		
		
		// Override the unitType:
		
		// unitType:
		String unitTypeStr = parts.size() > 3 ? parts.get( 3 ) : null;
		TimeTransformationUnitTypes timeUnitType = 
									TimeTransformationUnitTypes.fromString( unitTypeStr );

		setTimeUnitType( timeUnitType );

	}
	
	@Override
	public String format(Long value) {
		String results = null;
		
		String spaces = StringUtils.repeat( " ", getSpaces() );

		try {
			// & will not work in the DecimalFormat so replace it with ^|^ then replace after formatting:
			//String fmt = getFormat();//.replace( "&", "^|^" );
			//DecimalFormat dFmt = Prison.get().getDecimalFormat( fmt );

			switch ( getTimeUnitType() )
			{
				case none:
					
					results = PlaceholdersUtil.formattedTime( value );
					break;
					
				case LONG:
					results = PlaceholdersUtil.formattedTime( value, spaces );
					break;
					
				case SHORT:
					results = PlaceholdersUtil.formattedShortTime( value, spaces );
					break;
					
				case colons:
					results = PlaceholdersUtil.formattedColonsTime( value, spaces );
					break;
					
				default:
					break;
			}
			
//			if ( results.contains( "^|^" ) ) {
//				//results = results.replace( "^|^", "&" );
//			}
			
		}
		catch (Exception e ) {
			// Ignore unless in debug mode:
			if ( isDebug() ) {
				Output.get().logError( 
						String.format( "Error formatting results. long value= %s " +
								"spaces=%d timeUnitType= %s  ERRROR: %s", 
								
								Double.toString( value ), getSpaces(), 
								getTimeUnitType(), e.getMessage()
								));
			}
		}
		
		
		if ( isHex2() ) {
			results = Text.translateAmpColorCodesAltHexCode( results );
		}
		else if ( isHex() ) {
			results = Text.translateAmpColorCodes( results );
		}
		
		if ( isDebug() ) {
			String rawResults = Text.escapeAmpCodes(results);
			String rawAttribute = Text.escapeAmpCodes(getRaw());
			
			String message = String.format( "Placeholder Attribute time: double value= %s " +
							"spaces=%d timeUnitType=%s  Results: [%s] " +
							"raw: &3[&7%s&3] &3[&7%s&3]" +
							"(remove :debug from placeholder to disable this message)", 
							
							Double.toString( value ), getSpaces(), 
							getTimeUnitType(), results, rawResults, rawAttribute
							);
			Output.get().logInfo( message );
		}
		
		return results;

	}
	
	/**
	 * Not used.
	 */
	@Override
	public String format(String value) {
		String results = null;

		try {
			long lValue = Long.parseLong( value );
			results = format( lValue );
		}
		catch (Exception e ) {
			// Ignore unless in debug mode:
			if ( isDebug() ) {
				Output.get().logError( 
						String.format( "Error parsing value to a long. String value= [%s] " +
								"spaces=%d timeUnitType= %s  ERRROR: %s", 
								
								value, getSpaces(), 
								getTimeUnitType(), e.getMessage()
								));
			}
		}
		
		return results;
	}

	
	
	@Override
	public String format(Double value) {
		String results = null;
		
		try {
			long lValue = value.longValue();
			
			results = format( lValue );
		}
		catch (Exception e ) {
			// Ignore unless in debug mode:
			if ( isDebug() ) {
				Output.get().logError( 
						String.format( "Error parsing value to a long. String value= [%s] " +
								"spaces=%d timeUnitType= %s  ERRROR: %s", 
								
								value, getSpaces(), 
								getTimeUnitType(), e.getMessage()
								));
			}
		}
		
		return results;
	}

	public TimeTransformationUnitTypes getTimeUnitType() {
		return timeUnitType;
	}
	public void setTimeUnitType(TimeTransformationUnitTypes timeUnitType) {
		this.timeUnitType = timeUnitType;
	}


}

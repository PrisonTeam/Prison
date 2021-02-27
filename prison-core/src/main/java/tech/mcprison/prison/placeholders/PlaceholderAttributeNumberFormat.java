package tech.mcprison.prison.placeholders;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholderManager.NumberTransformationUnitTypes;
import tech.mcprison.prison.util.Text;

/**
 * <p>This takes the placeholder attribute for number formatting
 * and parses the dynamic content to setup this instance.
 * Then this class applies the formatting to the generated placeholder 
 * results to produce the requested output.
 * </p>
 * 
 * <p>Usage:
 * </p>
 * <pre>::nFormat:format:spaces:unitType:hex:hex2:debug</pre>
 * 
 * <ul>
 *   <li><b>nFormat</b>: the keyword to identify this attribute.</li>
 *   <li><b>format</b>: formatting based upon Java's DecimalFormat class.
 *   					Required. Defaults to #,##0.00. 
 *      <ul>
 *        <li>#,###</li>
 *        <li>#,###.00</li>
 *        <li>#,###.00000</li>
 *     </ul>
 *   </li>
 *   <li><b>spaces</b>: number of spaces between format and unit of measure. 
 *   				Optional. Defaults to 1.</li>
 *   <li><b>unitType</b>: unit type to display or to use to transform the results.
 *   				Optional. Defaults to the placeholder type that is used.
 *   	<ul>
 *        <li><b>none</b>: No display of units. No transformations.</li>
 *        <li><b>kmg</b>: Uses one character units: kMGTPEZY. Transforms results by 
 *        			dividing by 1000.0 until value is less than 1000.0, and 
 *        			each time it increments the unit.</li>
 *        <li><b>kmbt</b>: Uses one character units: KMBTqQsS. Transforms results by 
 *     				dividing by 1000.0 until value is less than 1000.0, and 
 *     				each time it increments the unit character.  
 *     				k=1,000, M=1,000,000 and etc. These are non-standard codes.</li>
 *        <li><b>binary</b>: Uses a base-two divisor of 1024 along with the units: 
 *        			KB, MB, GB, TB, PB, EB, ZB, and YB.  
 *        			</li>
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
 * </ul>
 * 
 *
 */
public class PlaceholderAttributeNumberFormat
		implements PlaceholderAttributeNumber {

	public static final String FORMAT_DEFAULT = "#,##0.00";
	
	private ArrayList<String> parts;
	private String raw;
	
	private String format;
	private int spaces = 1;
	private NumberTransformationUnitTypes unitType;
	
	private boolean hex = false;
	private boolean hex2 = false;
	private boolean debug = false;
	
	public PlaceholderAttributeNumberFormat( ArrayList<String> parts, String raw ) {
		super();
		
		this.parts = parts;
		this.raw = raw;
		
		// ::nFormat:format:spaces:unitType:hex:debug
		
		// Extract hex and debug first, since they are non-positional
		this.hex = parts.remove( "hex" );
		this.hex2 = parts.remove( "hex2" );
		this.debug = parts.remove( "debug" );
		
		int len = 1;
		
		// format:
		String format = parts.size() > len ? parts.get( len++ ) : FORMAT_DEFAULT;
		
		// spaces:
		String spacesStr = parts.size() > len ? parts.get( len++ ) : "1";
		int spaces = 1;
		if ( spacesStr != null && !spacesStr.trim().isEmpty() ) {
			
			try {
				spaces = Integer.parseInt( spacesStr );
			}
			catch (NumberFormatException e ) {
				// invalid supplied format. 
				// Ignore unless in debug mode:
				if ( isDebug() ) {
					Output.get().logError( 
							String.format( "Error parsing spaces to an Integer. String value= [%s] " +
									"ERRROR: %s", 
									
									spacesStr, e.getMessage()
									));
				}

			}
		}
		
		// unitType:
		String unitTypeStr = parts.size() > len ? parts.get( len++ ) : null;
		NumberTransformationUnitTypes unitType = 
									NumberTransformationUnitTypes.fromString( unitTypeStr );
		
//		// Debug:
//		String debugStr = parts.size() > len ? parts.get( len++ ) : null;
//		boolean debug = debugStr != null && "debug".equalsIgnoreCase( debugStr );
		

		this.format = format;
		this.spaces = spaces;
		this.unitType = unitType;
//		this.debug = debug;
	}

	@Override
	public String format( String value ) {
		String results = null;

		try {
			double dValue = Double.parseDouble( value );
			results = format( dValue );
		}
		catch (Exception e ) {
			// Ignore unless in debug mode:
			if ( isDebug() ) {
				Output.get().logError( 
						String.format( "Error parsing value to a double. String value= [%s] " +
								"format=[%s] spaces=%d unitType= %s  ERRROR: %s", 
								
								value, getFormat(), getSpaces(), 
								getUnitType(), e.getMessage()
								));
			}
		}
		
		return results;
	}

	@Override
	public String format( Double value ) {
		String results = null;
		
		String spaces = StringUtils.repeat( " ", getSpaces() );

		try {
			// & will not work in the DecimalFormat so replace it with ^|^ then replace after formatting:
			String fmt = getFormat();//.replace( "&", "^|^" );
			DecimalFormat dFmt = new DecimalFormat( fmt );

			switch ( getUnitType() )
			{
				case none:
					results = dFmt.format( value );
					break;
					
				case kmg:
					results = PlaceholdersUtil.formattedMetricSISize( value, dFmt, spaces );
					break;
					
				case kmbt:
					results = PlaceholdersUtil.formattedKmbtSISize( value, dFmt, spaces );
					break;
					
				case binary:
					results = PlaceholdersUtil.formattedIPrefixBinarySize( value, dFmt, spaces );
					break;
					
				default:
					break;
			}
			
			if ( results.contains( "^|^" ) ) {
				//results = results.replace( "^|^", "&" );
			}
			
		}
		catch (Exception e ) {
			// Ignore unless in debug mode:
			if ( isDebug() ) {
				Output.get().logError( 
						String.format( "Error formatting results. double value= %s " +
								"format=[%s] spaces=%d unitType= %s  ERRROR: %s", 
								
								Double.toString( value ), getFormat(), getSpaces(), 
								getUnitType(), e.getMessage()
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
			Output.get().logInfo( 
					String.format( "Placeholder Attribute nFormat: double value= %s " +
							"format=[%s] spaces=%d unitType=%s  Results: [%s] " +
							"raw: &7[&3\\R%s\\E&7]" +
							"(remove :debug from placeholder to disable this message)", 
							
							Double.toString( value ), getFormat(), getSpaces(), 
							getUnitType(), results, getRaw()
							));
		}
		
		return results;
	}

	@Override
	public String format( Long value ) {

		return format( (double) value );
	}

	public ArrayList<String> getParts() {
		return parts;
	}
	public void setParts( ArrayList<String> parts ) {
		this.parts = parts;
	}

	public String getRaw() {
		return raw;
	}

	public String getFormat() {
		return format;
	}
	public void setFormat( String format ) {
		this.format = format;
	}

	public int getSpaces() {
		return spaces;
	}
	public void setSpaces( int spaces ) {
		this.spaces = spaces;
	}

	public NumberTransformationUnitTypes getUnitType() {
		return unitType;
	}
	public void setUnitType( NumberTransformationUnitTypes unitType ) {
		this.unitType = unitType;
	}

	public boolean isHex() {
		return hex;
	}
	public void setHex( boolean hex ) {
		this.hex = hex;
	}

	public boolean isHex2() {
		return hex2;
	}
	public void setHex2( boolean hex2 ) {
		this.hex2 = hex2;
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug( boolean debug ) {
		this.debug = debug;
	}

}

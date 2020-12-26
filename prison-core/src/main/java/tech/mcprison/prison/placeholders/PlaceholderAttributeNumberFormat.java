package tech.mcprison.prison.placeholders;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholderManager.NumberTransformationUnitTypes;

/**
 * <p>This takes the placeholder attribute for number formatting
 * and parses the dynamic content to setup this instance.
 * Then this class applies the formatting to the generated placeholder 
 * results to produce the requested output.
 * </p>
 * 
 * <p>Usage:
 * </p>
 * <pre>::nFormat:format:spaces:unitType:debug</pre>
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
 *        <li>none: No display of units. No transformations.</li>
 *        <li>kmg: Uses one character units: kMGTPEZY. Transforms results by 
 *        			dividing by 1000.0 until value is less than 1000.0, and 
 *        			each time it increments the unit.
 *   	</ul>
 *   </li>
 *   <li><b>debug</b>: Optional. Only valid value is "debug". When enabled it
 *   				will append to the placeholder results any error messages
 *   				that may occur when applying the attribute.
 *   </li>
 * </ul>
 * 
 * <p>The formatting of the number is based upon java's DecimalFormat class.
 * 
 * </p>
 * 
 *
 */
public class PlaceholderAttributeNumberFormat
		implements PlaceholderAttributeNumber {

	public static final String FORMAT_DEFAULT = "#,##0.00";
	
	private String[] parts;
	
	private String format;
	private int spaces = 1;
	private NumberTransformationUnitTypes unitType;
	
	private boolean debug = false;
	
	public PlaceholderAttributeNumberFormat( String[] parts ) {
		super();
		
		this.parts = parts;
		
		parseParts( parts );
	}

	private void parseParts( String[] parts ) {

		// ::nFormat:format:spaces:unitType:debug
		
		int len = 1;
		
		// format:
		String format = parts.length > len ? parts[len++] : FORMAT_DEFAULT;
		
		// spaces:
		String spacesStr = parts.length > len ? parts[len++] : "1";
		int spaces = 1;
		if ( spacesStr != null && !spacesStr.trim().isEmpty() ) {
			
			try {
				spaces = Integer.parseInt( spacesStr );
			}
			catch (NumberFormatException e ) {
				// invalid supplied format. Ignore.
			}
		}
		
		// unitType:
		String unitTypeStr = parts.length > len ? parts[len++] : null;
		NumberTransformationUnitTypes unitType = 
									NumberTransformationUnitTypes.fromString( unitTypeStr );
		
		// Debug:
		String debugStr = parts.length > len ? parts[len++] : null;
		boolean debug = debugStr != null && "debug".equalsIgnoreCase( debugStr );
		
		
		this.format = format;
		this.spaces = spaces;
		this.unitType = unitType;
		this.debug = debug;
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
					results = PlaceholdersUtil.formattedSize( value, dFmt, spaces );
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
		
		if ( isDebug() ) {
			Output.get().logInfo( 
					String.format( "Placeholder Attribute nFormat: double value= %s " +
							"format=[%s] spaces=%d unitType=%s  Results: [%s] " +
							"(remove :debug from placeholder to disable this message)", 
							
							Double.toString( value ), getFormat(), getSpaces(), 
							getUnitType(), results
							));
		}
		
		return results;
	}

	@Override
	public String format( Long value ) {

		return format( (double) value );
	}

	public String[] getParts() {
		return parts;
	}
	public void setParts( String[] parts ) {
		this.parts = parts;
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

	public boolean isDebug() {
		return debug;
	}
	public void setDebug( boolean debug ) {
		this.debug = debug;
	}

}

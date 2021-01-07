package tech.mcprison.prison.placeholders;

import java.util.ArrayList;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Text;

/**
 * <p>This takes the placeholder attribute for bar graph customization, and is then used
 * to apply to the generation of the bar graph based placeholders.
 * </p>
 * 
 * <p>All parameters are optional and default to the default configuration used for 
 * all bar graphs.
 * </p>
 *  
 * <p>Usage:
 * </p>
 * <pre>::bar:size:posColor:posSeg:negColor:negSeg:hex:hex2:debug</pre>
 * 
 * <ul>
 *   <li><b>bar</b>: the keyword to identify this attribute.</li>
 *   <li><b>size</b>: The number of segments to generate.</li>
 *   <li><b>Positive Color</b>: The color code to use for the positive segments. 
 *   						Color codes should start with an &.</li>
 *   <li><b>Positive Segment</b>: The value that will be used for the positive
 *   						segment.</li>
 *   <li><b>Negative Color</b>: The color code to use for the negative segments. 
 *   						Color codes should start with an &.</li>
 *   <li><b>Negative Segment</b>: The value that will be used for the negative
 *   						segment.</li>
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
 */
public class PlaceholderAttributeBar
		implements PlaceholderAttribute {
	
	private ArrayList<String> parts;
	private String raw;
	
	private PlaceholderProgressBarConfig barConfig;
	
	private boolean hex = false;
	private boolean hex2 = false;
	private boolean debug = false;
	
	public PlaceholderAttributeBar(ArrayList<String> parts, 
					PlaceholderProgressBarConfig defaultBarConfig, String raw ) {
		super();

		this.parts = parts;
		this.raw = raw;

		// ::bar:size:posColor:posSeg:negColor:negSeg:hex:debug
		
		// Extract hex and debug first, since they are non-positional
		this.hex = parts.remove( "hex" );
		this.hex2 = parts.remove( "hex2" );
		this.debug = parts.remove( "debug" );

		int len = 1;
		
		// format:
		String segStr = parts.size() > len ? parts.get( len++ ) : null;
		int seg = defaultBarConfig.getSegments();
		if ( segStr != null && !segStr.trim().isEmpty() ) {
			
			try {
				seg = Integer.parseInt( segStr );
			}
			catch (NumberFormatException e ) {
				// invalid supplied segments. 
				// Ignore unless in debug mode:
				if ( isDebug() ) {
					Output.get().logError( 
							String.format( "Error parsing segments to an Integer. String value= [%s] " +
									"ERRROR: %s", 
									
									segStr, e.getMessage()
									));
				}

			}
		}
		

		String pCol = parts.size() > len ? parts.get( len++ ) : defaultBarConfig.getPositiveColor();
		
		String pSeg = parts.size() > len ? parts.get( len++ ) : defaultBarConfig.getPositiveSegment();
		
		String nCol = parts.size() > len ? parts.get( len++ ) : defaultBarConfig.getNegativeColor();

		String nSeg = parts.size() > len ? parts.get( len++ ) : defaultBarConfig.getNegativeSegment();

		// If hex, then convert the color codes before storing in the config:
		if ( isHex2() ) {
			pCol = Text.translateAmpColorCodesAltHexCode( pCol );
			nCol = Text.translateAmpColorCodesAltHexCode( nCol );
		}
		else if ( isHex() ) {
			pCol = Text.translateAmpColorCodes( pCol );
			nCol = Text.translateAmpColorCodes( nCol );
		}
		
		
		this.barConfig = new PlaceholderProgressBarConfig( seg, pCol, pSeg, nCol, nSeg );
		
//		// Debug:
//		String debugStr = parts.length > len ? parts[len++] : null;
//		boolean debug = debugStr != null && "debug".equalsIgnoreCase( debugStr );
		
		
//		this.debug = debug;

		
		if ( isDebug() ) {

			Output.get().logInfo( 
					String.format( "Placeholder Attribute bar: " +
							"&7%s  &7Raw: &7[&3\\Q%s\\E&7] " +
							"(remove :debug from placeholder to disable this message)", 
							
							getBarConfig().toString(), getRaw()
							));
		}


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

	public PlaceholderProgressBarConfig getBarConfig() {
		return barConfig;
	}
	public void setBarConfig( PlaceholderProgressBarConfig barConfig ) {
		this.barConfig = barConfig;
	}

	@Override
	public String format( String value )
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

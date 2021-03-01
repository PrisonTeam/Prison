package tech.mcprison.prison.placeholders;

import java.util.ArrayList;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Text;

/**
 * <p>This placeholder attribute is for text formatting the placeholder results.
 * The only thing it does is to process the hex, hex2, and debug options.
 * </p>
 * 
 * <p>This placeholder attribute can be used with any placeholder, even for placeholders
 * that have other attributes to format their content.  Since this is strictly processing
 * the hex color codes, it will operate upon the final text output from the other
 * placeholders.
 * </p>
 * 
 * <p>Usage:
 * </p>
 * <pre>::text:hex:hex1:debug</pre>
 * 
 * <ul>
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
public class PlaceholderAttributeText
		implements PlaceholderAttribute {

	
	private ArrayList<String> parts;
	private String raw;
	
	private boolean hex = false;
	private boolean hex2 = false;
	private boolean debug = false;
	
	public PlaceholderAttributeText( ArrayList<String> parts, String raw ) {
		super();
		
		this.parts = parts;
		this.raw = raw;
		
		// ::text:hex:hex2:debug
		
		// Extract hex and debug first, since they are non-positional
		this.hex = parts.remove( "hex" );
		this.hex2 = parts.remove( "hex2" );
		this.debug = parts.remove( "debug" );
		
	}
	
	@Override
	public String format( String value )
	{
		String results = value;
		
		if ( isHex2() ) {
			results = Text.translateAmpColorCodesAltHexCode( results );
		}
		else if ( isHex() ) {
			results = Text.translateAmpColorCodes( results );
		}
		
		if ( isDebug() ) {
			Output.get().logInfo( 
					String.format( "Placeholder Attribute text: Results: [%s] " +
							"raw: &7[&3\\R%s\\E&7]" +
							"(remove :debug from placeholder to disable this message)", 
							
							results, getRaw()
							));
		}
		
		return results;

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


}

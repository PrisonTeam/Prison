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
 * <pre>::text:hex:hex1:debug:player=&lt;playerName&gt;</pre>
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
 *   <li><b>player=&lt;playerName&gt;</b>: Optional. Case insensitive. Non-positional; can be
 *   				placed anywhere.  Provides a player for the placeholder when the 
 *   				plugin requesting the placeholder cannot request it based upon the player.
 *   				</li>
 * </ul>
 * 
 * <p>Please note that there are a couple of different ways you can enter hex codes 
 * in prison, and in turn, placeholder attributes.
 * <p>
 * 
 * <p>If you use a hex code of `&#123456` then using 'hex', it will be translated as 
 * `&x&1&2&3&4&5&6`.  hex2 would get translated as `§x§1§2§3§4§5§5`.  This is actually
 * somewhat odd, since even `&x&1&2&3&4&5&6` will ultimately be passed to the raw 
 * underlying bukkit handler as `§x§1§2§3§4§5§5`, but what makes it odd is that sometimes
 * 'hex' works differently than 'hex2'.  Disclaimer.. I may have misread the source
 * code that I wrote a few years ago, and `hex2` may be something slightly different.
 * Too see the actual content, enable `debug` in your attribute and see exactly how 
 * its passed.
 * </p>
 * 
 * <p>There is another way to use hex codes too, and that is to use them as '#123456'
 * without the leading '&'.  Without using '&' color code, prison will allow the
 * raw '#123456' hex code to be passed along, unchanged by prison.  So the target
 * plugin, or bukkit/spigot/paper will have to be able to handle that natively.
 * </p>
 *
 */
public class PlaceholderAttributeText
		implements PlaceholderAttribute {

	
	private ArrayList<String> parts;
	private String raw;
	
	private boolean hex = false;
	private boolean hex2 = false;
	private boolean debug = false;
	
	private String player = null;
	
	public PlaceholderAttributeText( ArrayList<String> parts, String raw ) {
		super();
		
		this.parts = parts;
		this.raw = raw;
		
		// ::text:hex:hex2:debug
		
		// Extract hex and debug first, since they are non-positional
		this.hex = parts.remove( "hex" );
		this.hex2 = parts.remove( "hex2" );
		this.debug = parts.remove( "debug" );
		

		// Search for 'player=':
		for (String part : parts) {
			if ( part.toLowerCase().startsWith( "player=" ) ) {
				this.player = part;
				break;
			}
		}
		// extract the player's name:
		if ( this.player != null ) {
			if ( parts.remove( this.player ) ) {
				this.player = this.player.toLowerCase().replaceAll("player=", "");
			}
		}
		
		
	}
	

	@Override
	public String toString() {
		return getRaw();
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

	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}

}

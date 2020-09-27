package tech.mcprison.prison.autofeatures;

import java.util.regex.Pattern;

public class ValueNodeFactory {

	public Pattern LONG_PATTERN = Pattern.compile("^-?\\d{1,19}$");
	public Pattern DOUBLE_PATTERN = Pattern.compile("^[\\+\\-]{0,1}[0-9]+[\\.\\,]{1}[0-9]+$");
	
	
	public ValueNode getValueNode( String value ) {
		
		if ( value == null ) {
			return NullNode.getInstance();
		}
		
		// trim off leading and trailing spaces:
		value = value.trim();
		
		if ( value != null && 
				("true".equalsIgnoreCase( value.trim() ) || 
				 "false".equalsIgnoreCase( value.trim() ))) {
			
			try {
				boolean b = Boolean.parseBoolean( value.trim() );
				return BooleanNode.valueOf( b );
			}
			catch ( Exception e ) {
				// ignore since not a boolean... let another parser try to consume it.
			}
		}
			
		if ( LONG_PATTERN.matcher( value ).matches() ) {
			
			try {
				long l = Long.parseLong( value );
				return LongNode.valueOf( l );
			}
			catch ( NumberFormatException e ) {
				// ignore since not a long... let another parser try to consume it.
			}
			
		}
		
		
		// Note that the regex pattern does not match to scientific notations 
		// such as 123.45E2.
		if ( DOUBLE_PATTERN.matcher( value ).matches() ) {
			
			try {
				double d = Double.parseDouble( value );
				return DoubleNode.valueOf( d );
			}
			catch ( NumberFormatException e ) {
				// ignore since not a long... let another parser try to consume it.
			}
			
		}
		
		return TextNode.valueOf( value );
	}
	
}

package tech.mcprison.prison.placeholders;

public interface PlaceholderAttributeNumber
		extends PlaceholderAttribute {

	public String format( Double value );
	
	public String format( Long value );
	
}

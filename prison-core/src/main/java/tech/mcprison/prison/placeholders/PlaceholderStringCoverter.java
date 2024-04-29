package tech.mcprison.prison.placeholders;

public interface PlaceholderStringCoverter {
	
	/**
	 * <p>This returns a String list of all available placeholders that can be used.
	 * </p>
	 * @return
	 */
	public String getStringPlaceholders();
	
	public String convertStringPlaceholders( String source );

}

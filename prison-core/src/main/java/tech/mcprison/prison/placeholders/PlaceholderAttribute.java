package tech.mcprison.prison.placeholders;

public interface PlaceholderAttribute {
	
	public String format( String value );
	
	public String getPlayer();
	
	public void setPlayer( String player );
}

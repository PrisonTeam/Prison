package tech.mcprison.prison.placeholders;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceHolderFlags;

public interface Placeholders {
	
	public Map<PlaceHolderFlags, Integer> getPlaceholderDetailCounts();
	
	
	public int getPlaceholderCount();
	
	
	public int getPlaceholderRegistrationCount();
	
	
	public String placeholderTranslate(UUID playerUuid, String playerName, String identifier);
	
	
	public String placeholderTranslateText( String text);
	
	
	public String placeholderTranslateText( UUID playerUuid, String playerName, String text);
	
	
	public List<String> placeholderSearch( UUID playerUuid, String playerName, String[] patterns );


	public void reloadPlaceholders();


	public void printPlaceholderStats();
	
}

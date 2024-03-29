package tech.mcprison.prison.placeholders;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceholderFlags;

public interface Placeholders {
	
	
//	public void initializePlaceholderManagers();
	
	
	public Map<PlaceholderFlags, Integer> getPlaceholderDetailCounts();
	
	
	public int getPlaceholderCount();
	
	
	public int getPlaceholderRegistrationCount();
	
	
	public String placeholderTranslate(UUID playerUuid, String playerName, String identifier);
	
	
//	pu-blic String placeholderTranslateText( String text);
	
	
	public String placeholderTranslateText( UUID playerUuid, String playerName, String text);
	
	
	public List<String> placeholderSearch( UUID playerUuid, String playerName, String[] patterns );


	public void reloadPlaceholders();


	public void printPlaceholderStats();
	
}

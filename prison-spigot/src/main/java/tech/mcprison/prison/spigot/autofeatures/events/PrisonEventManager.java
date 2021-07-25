package tech.mcprison.prison.spigot.autofeatures.events;

import tech.mcprison.prison.spigot.SpigotPrison;

public interface PrisonEventManager
{

	public void registerEvents();
	
	
	public void initialize();
	
	
	public void unregisterListeners();
	
	
	public void dumpEventListeners();
	
}

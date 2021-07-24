package tech.mcprison.prison.spigot.autofeatures.events;

import tech.mcprison.prison.spigot.SpigotPrison;

public interface PrisonEventLManager
{

	public void registerEvents(SpigotPrison spigotPrison );
	
	
	public void unregisterListeners();
	
	
	public void dumpEventListeners();
	
}

package tech.mcprison.prison.spigot.autofeatures.events;

public interface PrisonEventManager
{

	public void registerEvents();
	
	
	public void initialize();
	
	
	public void unregisterListeners();
	
	
	public void dumpEventListeners();
	
}

package tech.mcprison.prison.spigot.game;

import org.bukkit.plugin.RegisteredListener;

public class SpigotHandlerList
	implements tech.mcprison.prison.internal.platform.HandlerList
{
	private org.bukkit.event.HandlerList handlerList; 
	
	public SpigotHandlerList( org.bukkit.event.HandlerList bukkitHandlerList ) {
		super();
		
		this.handlerList = bukkitHandlerList;
	}
	
	public RegisteredListener[] getRegisteredListeners() {
		return handlerList.getRegisteredListeners();
	}
	
}

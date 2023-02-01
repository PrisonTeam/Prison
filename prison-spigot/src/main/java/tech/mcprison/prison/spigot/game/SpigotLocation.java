package tech.mcprison.prison.spigot.game;

import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.Location;

public class SpigotLocation
	extends Location {

	private org.bukkit.Location bukkitLocation;
	
	public SpigotLocation( org.bukkit.Location bukkitLocation ) {
		super( SpigotUtil.bukkitLocationToPrison( bukkitLocation ) );
		
		this.bukkitLocation = bukkitLocation;
	}

	public org.bukkit.Location getBukkitLocation() {
		return bukkitLocation;
	}
	public void setBukkitLocation(org.bukkit.Location bukkitLocation) {
		this.bukkitLocation = bukkitLocation;
	}
}

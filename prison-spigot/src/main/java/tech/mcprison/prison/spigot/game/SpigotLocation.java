package tech.mcprison.prison.spigot.game;

import org.bukkit.Bukkit;

import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.Location;

public class SpigotLocation
	extends Location {

	private org.bukkit.Location bukkitLocation;
	
	public SpigotLocation( Location location ) {
		super( location );
	}
	
	public SpigotLocation( org.bukkit.Location bukkitLocation ) {
		super( SpigotUtil.bukkitLocationToPrison( bukkitLocation ) );
		
		this.bukkitLocation = bukkitLocation;
	}

	public org.bukkit.Location getBukkitLocation() {
		
		if ( bukkitLocation == null ) {
			bukkitLocation = createBukkitLocation();
		}
		
		return bukkitLocation;
	}
	
	private org.bukkit.Location createBukkitLocation() {
		
		org.bukkit.World world = Bukkit.getWorld( getWorld().getName() );
		
		org.bukkit.Location bLocation = new org.bukkit.Location(world, getBlockX(), getBlockY(), getBlockZ() );
		
		bLocation.setYaw(getYaw());
		
		bLocation.setPitch(getPitch());
		
		return bLocation;
	}

//	public void setBukkitLocation(org.bukkit.Location bukkitLocation) {
//		this.bukkitLocation = bukkitLocation;
//	}
	
	public org.bukkit.World getBukkitWorld() {
		return getBukkitLocation().getWorld();
	}
}

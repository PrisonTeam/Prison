package tech.mcprison.prison.spigot.game;

import org.bukkit.Bukkit;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.Location;

public class SpigotLocation
	extends Location {

	private org.bukkit.Location bukkitLocation;
	
	public SpigotLocation( Location location ) {
		super( location );
	}
	
	public SpigotLocation( World world, double x, double y, double z, float yaw, float pitch ) {
		super( new Location( world, x, y, z, yaw, pitch) );
	}
	
	public SpigotLocation( org.bukkit.Location bukkitLocation ) {
		super( SpigotUtil.bukkitLocationToPrison( bukkitLocation ) );
		
		this.bukkitLocation = bukkitLocation;
	}

	public org.bukkit.Location getBukkitLocation() {
		
		if ( bukkitLocation == null ) {
			bukkitLocation = getBukkitLocation( this );
		}
		
		return bukkitLocation;
	}
	
	public static org.bukkit.Location getBukkitLocation( Location location ) {
		
		org.bukkit.World world = Bukkit.getWorld( location.getWorld().getName() );
		
		org.bukkit.Location bLocation = new org.bukkit.Location(world, 
				location.getX(), location.getY(), location.getZ() );
		
		bLocation.setYaw( location.getYaw() );
		
		bLocation.setPitch( location.getPitch() );
		
		return bLocation;
	}
	
	
	public org.bukkit.World getBukkitWorld() {
		return getBukkitLocation().getWorld();
	}
}

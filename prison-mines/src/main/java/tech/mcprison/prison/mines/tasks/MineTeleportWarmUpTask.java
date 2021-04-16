package tech.mcprison.prison.mines.tasks;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

public class MineTeleportWarmUpTask
	implements PrisonRunnable
{
	private Player player;
	private Mine mine;
	private String target;
	
	private Location location;
	
	private double maxDistance;
	
	public MineTeleportWarmUpTask( Player player, Mine mine, String target, double maxDistance ) {
		super();
		
		this.player = player;
		this.mine = mine;
		this.target = target;
		
		this.location = player.getLocation();
		
		this.maxDistance = maxDistance;
	}
	
	@Override
	public void run() {
		
		
		Location locationNew = getPlayer().getLocation();
		
		Bounds bounds = new Bounds( getLocation(), locationNew );
		
		if ( bounds.getDistance3d() <= getMaxDistance() ) {
			mine.teleportPlayerOut( getPlayer(), getTarget() );
		}
		else {
			player.sendMessage( "&7You moved! Teleport canceled." );
		}
		
	}

	public Player getPlayer() {
		return player;
	}
	public void setPlayer( Player player ) {
		this.player = player;
	}

	public Mine getMine() {
		return mine;
	}
	public void setMine( Mine mine ) {
		this.mine = mine;
	}

	public String getTarget() {
		return target;
	}
	public void setTarget( String target ) {
		this.target = target;
	}

	public Location getLocation() {
		return location;
	}
	public void setLocation( Location location ) {
		this.location = location;
	}

	public double getMaxDistance() {
		return maxDistance;
	}
	public void setMaxDistance( double maxDistance ) {
		this.maxDistance = maxDistance;
	}
}

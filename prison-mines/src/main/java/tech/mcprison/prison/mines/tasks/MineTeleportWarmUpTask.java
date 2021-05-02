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
	
	private String messageSuccess;
	private String messageFailed;
	
	public MineTeleportWarmUpTask( Player player, Mine mine, String target, double maxDistance ) {
		super();
		
		this.player = player;
		this.mine = mine;
		this.target = target;
		
		this.location = player.getLocation();
		
		this.maxDistance = maxDistance;
		
		
		this.messageSuccess = null;
		this.messageFailed = "&7You moved! Teleport canceled.";
	}
	
	@Override
	public void run() {
		
		
		Location locationNew = getPlayer().getLocation();
		
		Bounds bounds = new Bounds( getLocation(), locationNew );
		
		if ( bounds.getDistance3d() <= getMaxDistance() ) {
			mine.teleportPlayerOut( getPlayer(), getTarget() );
			
    		// To "move" the player out of the mine, they are elevated by one block above the surface
    		// so need to remove the glass block if one is spawned under them.  If there is no glass
    		// block, then it will do nothing.
    		mine.submitTeleportGlassBlockRemoval();

    		if ( getMessageSuccess() != null && !getMessageSuccess().isEmpty() ) {
    			
    			player.sendMessage( getMessageSuccess() );
    		}
		}
		else {
			if ( getMessageFailed() != null && !getMessageFailed().isEmpty() ) {
				
				player.sendMessage( getMessageFailed() );
			}
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

	public String getMessageSuccess() {
		return messageSuccess;
	}
	public void setMessageSuccess( String messageSuccess ) {
		this.messageSuccess = messageSuccess;
	}

	public String getMessageFailed() {
		return messageFailed;
	}
	public void setMessageFailed( String messageFailed ) {
		this.messageFailed = messageFailed;
	}
}

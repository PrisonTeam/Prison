package tech.mcprison.prison.mines.tasks;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.util.Location;

public class MineChangeBlockTask
	implements PrisonRunnable
{
	private Location location;
	private PrisonBlock targetBlock;
	
	private Location topOfMineLocation;
	
	public MineChangeBlockTask( Location location, PrisonBlock targetBlock ) {
		super();
		
		this.location = location;
		this.targetBlock = targetBlock;
		
		this.topOfMineLocation = new Location( getLocation() );
    	this.topOfMineLocation.setY( topOfMineLocation.getBlockY() - 1 );
		
	}
	
	@Override
	public void run() {
		
    	if ( topOfMineLocation.getBlockAt().isEmpty() ) {
    		
    		// When there is no block under the glass block, spawn in a
    		// glass block so the player won't fall to their death.
    		// This block will be within the mine, so it will be replaced
    		// when the mine resets, or the players can break it too.
    		
    		topOfMineLocation.getBlockAt().setPrisonBlock( PrisonBlock.GLASS );
    	}

		getLocation().getBlockAt().setPrisonBlock( getTargetBlock() );
		
	}

	public Location getLocation() {
		return location;
	}
	public void setLocation( Location location ) {
		this.location = location;
	}

	public PrisonBlock getTargetBlock() {
		return targetBlock;
	}
	public void setTargetBlock( PrisonBlock targetBlock ) {
		this.targetBlock = targetBlock;
	}
}

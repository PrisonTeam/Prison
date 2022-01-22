package tech.mcprison.prison.mines.tasks;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.util.Location;

public class MineChangeBlockTask
	implements PrisonRunnable
{
	private Location location;
	private PrisonBlock targetBlock;
	private PrisonBlock checkBlock;
	
	private Location topOfMineLocation;
	
	public MineChangeBlockTask( Location location, 
					PrisonBlock targetBlock, PrisonBlock checkBlock ) {
		super();
		
		this.location = location;
		this.targetBlock = targetBlock;
		
		this.checkBlock = checkBlock;
		
		this.topOfMineLocation = new Location( getLocation() );
    	this.topOfMineLocation.setY( topOfMineLocation.getBlockY() - 1 );
		
	}
	
	@Override
	public void run() {
		
		// Replace the targetBlock if there is no checkBlock.  If there is a checkBlock,
		// then make sure the block that will be replaced is that type of a block.
		if ( checkBlock == null || 
				!topOfMineLocation.getBlockAt().isEmpty() &&
				getLocation().getBlockAt().getPrisonBlock().equals( getCheckBlock() ) ) {
			
			getLocation().getBlockAt().setPrisonBlock( getTargetBlock() );
		}
		
	}

	public Location getLocation() {
		return location;
	}

	public PrisonBlock getTargetBlock() {
		return targetBlock;
	}

	public PrisonBlock getCheckBlock()
	{
		return checkBlock;
	}
	
}

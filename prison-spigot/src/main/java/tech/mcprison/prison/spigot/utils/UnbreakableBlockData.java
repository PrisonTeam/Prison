package tech.mcprison.prison.spigot.utils;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.util.Location;

public class UnbreakableBlockData
{
	private Location key;
	private PrisonBlock block;
	private Mine mine;
	
	private PrisonBlock targetBlock;
	private long decayTimeTicks;
	
	private int taskId;
	
	public UnbreakableBlockData( Location key, PrisonBlock block, Mine mine ) {
		super();
		
		this.key = key;
		this.block = block;
		this.mine = mine;
		
		this.targetBlock = null;
		this.decayTimeTicks = 10;
		
		this.taskId = 0;
	}
	
	public UnbreakableBlockData( PrisonBlock block, Mine mine ) {
		this( block.getLocation(), block, mine );
	}

	public Location getKey() {
		return key;
	}
	public void setKey( Location key ) {
		this.key = key;
	}

	public PrisonBlock getBlock() {
		return block;
	}
	public void setBlock( PrisonBlock block ) {
		this.block = block;
	}

	public Mine getMine() {
		return mine;
	}
	public void setMine( Mine mine ) {
		this.mine = mine;
	}

	public PrisonBlock getTargetBlock() {
		return targetBlock;
	}
	public void setTargetBlock( PrisonBlock targetBlock ) {
		this.targetBlock = targetBlock;
	}

	public long getDecayTimeTicks() {
		return decayTimeTicks;
	}
	public void setDecayTimeTicks( long decayTimeTicks ) {
		this.decayTimeTicks = decayTimeTicks;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setJobId( int taskId ) {
		this.taskId = taskId;
	}
}

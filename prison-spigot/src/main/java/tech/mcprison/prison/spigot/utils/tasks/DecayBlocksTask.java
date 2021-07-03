package tech.mcprison.prison.spigot.utils.tasks;

import java.util.List;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.spigot.utils.UnbreakableBlockData;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class DecayBlocksTask
	implements PrisonRunnable
{
	private final UnbreakableBlockData data;
	
	private List<PrisonBlock> decayBlocksList;
	
	private long intervalPerBlock = 0;
	
	private int taskId = 0;
	
	public DecayBlocksTask( UnbreakableBlockData data, List<PrisonBlock> decayBlocksList ) {
		super();
		
		this.data = data;
		
		this.decayBlocksList = decayBlocksList;
		
		intervalPerBlock = getDecayBlocksList() == null || getDecayBlocksList().size() == 0 ?
					5 :data.getDecayTimeTicks() / getDecayBlocksList().size();
		if ( intervalPerBlock < 1 ) {
			intervalPerBlock = 1;
		}
		
		getDecayBlocksList().add( data.getTargetBlock() );

	}
	
	
	public void submit() {
		
		PrisonBlock initialBlock = getDecayBlocksList().remove( 0 );
		
		getData().getKey().getBlockAt().setPrisonBlock( initialBlock );
		
		taskId = PrisonTaskSubmitter.runTaskLater(this, intervalPerBlock );
		
		getData().setJobId( taskId );
	}
	
	
	@Override
	public void run()
	{
		
		// Change the block to targetBlock type
		getData().getKey().getBlockAt().setPrisonBlock( getData().getTargetBlock() );
		
		if ( getDecayBlocksList().size() > 0 ) {
			submit();
		}
		else {
			// done so shut down:
			BlockUtils.getInstance().removeUnbreakable( getData().getKey() );
		}
		
	}

	public List<PrisonBlock> getDecayBlocksList() {
		return decayBlocksList;
	}

	public UnbreakableBlockData getData() {
		return data;
	}

	public long getIntervalPerBlock() {
		return intervalPerBlock;
	}
	public void setIntervalPerBlock( long intervalPerBlock ) {
		this.intervalPerBlock = intervalPerBlock;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId( int taskId ) {
		this.taskId = taskId;
	}

}

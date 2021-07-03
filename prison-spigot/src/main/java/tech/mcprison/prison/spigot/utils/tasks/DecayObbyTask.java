package tech.mcprison.prison.spigot.utils.tasks;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.spigot.utils.UnbreakableBlockData;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class DecayObbyTask
	implements PrisonRunnable
{

	private PrisonBlock initialBlock;
	private final UnbreakableBlockData data;
	
	
	private int taskId = 0;
	
	public DecayObbyTask( XMaterial initialBlock, UnbreakableBlockData data ) {
		super();
		
		this.initialBlock = PrisonBlock.fromBlockName( initialBlock.name() );
		this.data = data;
	}
	public DecayObbyTask( PrisonBlock initialBlock, UnbreakableBlockData data ) {
		super();
		
		this.initialBlock = initialBlock;
		this.data = data;
	}
	
	
	public void submit() {
		
		// Set source block to the initialBlock (such as obby):
		getData().getKey().getBlockAt().setPrisonBlock( initialBlock );
		
		taskId = PrisonTaskSubmitter.runTaskLater(this, getData().getDecayTimeTicks() );
		
		getData().setJobId( taskId );
	}
	
	
	@Override
	public void run()
	{
		
		// Change the block to targetBlock type
		getData().getKey().getBlockAt().setPrisonBlock( getData().getTargetBlock() );
		
		BlockUtils.getInstance().removeUnbreakable( getData().getKey() );
		
	}

	public UnbreakableBlockData getData() {
		return data;
	}
}

package tech.mcprison.prison.spigot.utils.tasks;

import java.util.ArrayList;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.spigot.utils.UnbreakableBlockData;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class DecayRainbowTask
	implements PrisonRunnable
{

	private final UnbreakableBlockData data;
	
	private List<PrisonBlock> rainbowList;
	
	private long intervalPerBlock = 0;
	
	private int taskId = 0;
	
	public DecayRainbowTask( UnbreakableBlockData data ) {
		super();
		
		this.data = data;
		
		this.rainbowList = new ArrayList<>();
		
		addBlock( XMaterial.BLACK_WOOL );
		addBlock( XMaterial.PURPLE_WOOL );
		addBlock( XMaterial.BLUE_WOOL );
		addBlock( XMaterial.GREEN_WOOL );
		addBlock( XMaterial.YELLOW_WOOL );
		addBlock( XMaterial.ORANGE_WOOL );
		addBlock( XMaterial.RED_WOOL );
		addBlock( XMaterial.PINK_WOOL );
		addBlock( XMaterial.WHITE_WOOL );
		addBlock( XMaterial.DIAMOND_BLOCK );
		addBlock( XMaterial.AIR );
		
		intervalPerBlock = data.getDecayTimeTicks() / 10;
		if ( intervalPerBlock < 1 ) {
			intervalPerBlock = 1;
		}

	}
	
	
	public void submit() {
		
		PrisonBlock initialBlock = getRainbowList().remove( 0 );
		
		getData().getKey().getBlockAt().setPrisonBlock( initialBlock );
		
		taskId = PrisonTaskSubmitter.runTaskLater(this, intervalPerBlock );
		
		getData().setJobId( taskId );
	}
	
	
	@Override
	public void run()
	{
		
		// Change the block to targetBlock type
		getData().getKey().getBlockAt().setPrisonBlock( getData().getTargetBlock() );
		
		if ( getRainbowList().size() > 0 ) {
			submit();
		}
		else {
			// done so shut down:
			BlockUtils.getInstance().removeUnbreakable( getData().getKey() );
		}
		
	}

	private void addBlock( XMaterial xMat ) {
		getRainbowList().add( SpigotUtil.getPrisonBlock( xMat.name() ) );
	}
	
	public List<PrisonBlock> getRainbowList() {
		return rainbowList;
	}
	
	public UnbreakableBlockData getData() {
		return data;
	}

}

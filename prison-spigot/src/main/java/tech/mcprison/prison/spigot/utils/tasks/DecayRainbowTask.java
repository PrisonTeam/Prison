package tech.mcprison.prison.spigot.utils.tasks;

import java.util.ArrayList;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.spigot.utils.UnbreakableBlockData;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class DecayRainbowTask
	extends DecayBlocksTask
{

	
	public DecayRainbowTask( UnbreakableBlockData data ) {
		super( data, new ArrayList<>(10));
		
		getDecayBlocksList().clear();
		
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
		
		getDecayBlocksList().add( data.getTargetBlock() );

	}
	
	
	public void submit() {
		
		PrisonBlock initialBlock = getDecayBlocksList().remove( 0 );
		
		getData().getKey().getBlockAt().setPrisonBlock( initialBlock );
		
		setTaskId( PrisonTaskSubmitter.runTaskLater(this, getIntervalPerBlock() ) );
		
		getData().setJobId( getTaskId() );
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

	private void addBlock( XMaterial xMat ) {
		getDecayBlocksList().add( SpigotUtil.getPrisonBlock( xMat.name() ) );
	}

}

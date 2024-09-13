package tech.mcprison.prison.spigot.customblock;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.util.Location;

public class HeadsCustomBlocks
		extends CustomBlockIntegration {


	private HeadsCustomBlocksWrapper headsCustomBlocksWrapper;
	
	
	public HeadsCustomBlocks() {
		super("HeadsCustomBlocks", "HeadsCustomBlocks", PrisonBlockType.heads, "heads:" );
    }


	@Override
	public String getCustomBlockId( Block block )
	{
		return null;
	}


	@Override
	public PrisonBlock getCustomBlock( Block block )
	{
		return null;
	}


	@Override
	public Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate )
	{
		return null;
	}


	@Override
	public void setCustomBlockIdAsync( PrisonBlock prisonBlock, Location location )
	{
		
	}


	@Override
	public List<PrisonBlock> getCustomBlockList()
	{
		List<PrisonBlock> results = new ArrayList<>();
		
		return results;
	}


//	@Override
//	public List<? extends ItemStack> getDrops( PrisonBlock prisonBlock )
//	{
//		List<? extends ItemStack> results = new ArrayList<>();
//		
//		return results;
//	}


	@Override
	public List<? extends ItemStack> getDrops(Player player, PrisonBlock prisonBlock, ItemStack tool) {
		List<? extends ItemStack> results = new ArrayList<>();
		
		return results;
	}
	
}

package tech.mcprison.prison.integration;

import java.util.List;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;

public abstract class CustomBlockIntegration
		extends IntegrationCore {
	
	private PrisonBlockType blockType;
	
	private String blockPrefix;

	public CustomBlockIntegration( String keyName, String providerName, 
								PrisonBlockType blockType, String blockPrefix ) {
		super( keyName, providerName, IntegrationType.CUSTOMBLOCK );
		
		this.blockType = blockType;
		this.blockPrefix = blockPrefix;
	}
	
	public abstract String getCustomBlockId( Block block );
	
	/**
	 * <p>This function is supposed to identify if the given block is a custom block, and 
	 * if it is a custom block, then this function will return the correct PrisonBlock
	 * to match it's type. The PrisonBlock that will be returned, will come from the
	 * collection of valid blocks that were generated upon server startup.  
	 * </p>
	 * 
	 * <p>If there is no match, then this function will return a null.
	 * </p>
	 * 
	 * <p>It's also important to know that the original block that is retrieved from 
	 * PrisonBlockTypes.getBlockTypesByName() is cloned prior to returning it to this
	 * function, so it's safe to do anything you want with it.
	 * </p>
	 * 
	 * @param block
	 * @return The matched and cloned PrisonBlock, otherwise it will return a null if no match.
	 */
	public abstract PrisonBlock getCustomBlock( Block block );
	
	public abstract Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate );
	
	
	public abstract List<PrisonBlock> getCustomBlockList();

	
	public PrisonBlockType getBlockType() {
		return blockType;
	}

	public String getBlockPrefix() {
		return blockPrefix;
	}
	
}

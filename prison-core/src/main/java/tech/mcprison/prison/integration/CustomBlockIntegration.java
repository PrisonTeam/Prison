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
	
	
	public abstract Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate );
	
	
	public abstract List<PrisonBlock> getCustomBlockList();

	
	public PrisonBlockType getBlockType() {
		return blockType;
	}

	public String getBlockPrefix() {
		return blockPrefix;
	}
	
}

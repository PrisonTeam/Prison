package tech.mcprison.prison.autofeatures;

import java.util.TreeMap;

import tech.mcprison.prison.autofeatures.BlockConvertersFileConfig.BlockConverterTypes;
import tech.mcprison.prison.file.FileIOData;

public class BlockConvertersData 
	implements FileIOData
{
	
	private TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> blockConverters;
	private TreeMap<String, BlockConverterEventTrigger> blockConvertersEventTiggers;

	private TreeMap<String, Boolean> processAutoFeaturesAllBlocks = null;
	
	public BlockConvertersData() {
		super();
		
		this.blockConverters = new TreeMap<>();
		this.blockConvertersEventTiggers = new TreeMap<>();
		
		this.processAutoFeaturesAllBlocks = new TreeMap<>();
	}

	

	/**
	 * <p>Force the BlockConverterEventTriggers to BlockConverter so it can be
	 * processed by the core selectors that deal with BlockConverter objects 
	 * and the player's.
	 * </p>
	 * 
	 * @param blockName
	 * @return
	 */
	public TreeMap<String, BlockConverter> getBlockConvertersEventTiggers(String blockName) {
		TreeMap<String, BlockConverter> results = new TreeMap<>();
		
		if ( getBlockConvertersEventTiggers().containsKey( blockName ) ) {
			results.put( blockName, 
					(BlockConverter) getBlockConvertersEventTiggers().get( blockName )
					);
		}
		
		return results;
	}
	
		
	public TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> getBlockConverters() {
		return blockConverters;
	}
	public void setBlockConverters(TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> blockConverters) {
		this.blockConverters = blockConverters;
	}

	public TreeMap<String, BlockConverterEventTrigger> getBlockConvertersEventTiggers() {
		return blockConvertersEventTiggers;
	}
	public void setBlockConvertersEventTiggers(TreeMap<String, BlockConverterEventTrigger> blockConvertersEventTiggers) {
		this.blockConvertersEventTiggers = blockConvertersEventTiggers;
	}

	public TreeMap<String, Boolean> getProcessAutoFeaturesAllBlocks() {
		return processAutoFeaturesAllBlocks;
	}
	public void setProcessAutoFeaturesAllBlocks(TreeMap<String, Boolean> processAutoFeaturesAllBlocks) {
		this.processAutoFeaturesAllBlocks = processAutoFeaturesAllBlocks;
	}

}

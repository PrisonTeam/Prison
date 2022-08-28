package tech.mcprison.prison.autofeatures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.file.FileIOData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.ranks.data.RankPlayer;

public class BlockConvertersFileConfig
	implements FileIOData {

	public static final String FILE_NAME__BLOCK_CONVERTS_CONFIG_JSON = "/blockConvertersConfig.json";
	
	private transient File configFile;
	
	private TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> blockConverters;
	
	private TreeMap<String, Boolean> processAutoFeaturesAllBlocks = null;
	

	public enum BlockConverterTypes {
		aSample01,
		autoPickupFeatures,
		blockFeatures, 
		smeltFeatures
		;
	}
	
	public BlockConvertersFileConfig() {
		super();
		
		this.blockConverters = new TreeMap<>();
		
		this.processAutoFeaturesAllBlocks = new TreeMap<>();
		
	}
	
	public boolean processAutoFeaturesForBlock( RankPlayer player, String blockName ) {
		boolean results = isProcessAutoFeaturesAllBlocks( player );
		
		if ( !results ) {
			
		}

		return results;
	}

	public BlockConverterResults getBlockConverterItemStacks( RankPlayer player, 
			String blockName, int blockQuantity,
			BlockConverterTypes bcType ) {
		
		BlockConverterResults results = new BlockConverterResults( blockName, blockQuantity );

		if ( blockName != null && blockQuantity >= 1 ) {
			
			BlockConverter bc = getBlockConverter( player, blockName, bcType );
			
			if ( bc != null && blockQuantity >= bc.getKeyQuantity() ) {
				
				int multiplier = blockQuantity / bc.getKeyQuantity();
				
				// Make sure there is enough of a quantity match, which must be 1 or more.
				if ( multiplier >= 1 ) {

					results.setBlockConverter( bc );
					
					// getBlockConverterOutputs applies chance, perms, etc...
					List<BlockConverterOutput> outputs = getBlockConverterOutputs( player, bc );
					
					results.setResultsQuantityConsumed( bc.getKeyQuantity() * multiplier );
					
					for (BlockConverterOutput output : outputs) {
						
						String blkName = output.getBlockName();
						int blkQuanity = output.getQuantity();
						
						PrisonBlock pBlock = Prison.get().getPlatform().getPrisonBlock( blkName );
						if ( pBlock != null ) {
							
							ItemStack pItemStack = pBlock.getItemStack( blkQuanity );
							
							if ( pItemStack != null ) {
								results.getResultsItemStack().add( pItemStack );
							}
						}
					}
					
					results.setResultsSuccess( true );
				}
				
			}
		}
		
		return results;
	}


	/**
	 * <p>Using the RankPlayer, this will filter (remove) any BlockConverter for which a
	 * player would not have access to it.  It should be understood that a blockConverter
	 * may be accessible to a player, but it could contain a BlockConverterTarger that
	 * they do not have access to due to permissions restrictions.
	 * </p>
	 * 
	 * @param player
	 * @param blockName
	 * @return
	 */
	public BlockConverter getBlockConverter( RankPlayer player, String blockName, 
			BlockConverterTypes bcType ) {
		BlockConverter results = null;
		
		if ( player != null && blockName != null && bcType != null ) {
			
			BlockConverter temp = getBlockConverter( blockName, bcType );
			if ( temp != null && temp.getPermissions().size() == 0 ) {
				results = temp;
			}
			else if ( temp != null ) {
				for (String perm : temp.getPermissions() ) {
					if ( player.hasPermission( perm ) ) {
						results = temp;
						break;
					}
				}
			}
		}
		
		return results;
	}
	
	
	/**
	 * <p>This will return a BlockConverter if there is a match on the blockName. 
	 * The match will be based upon the blockName being converted to lowercase.
	 * </p>
	 * 
	 * @param blockName
	 * @param bcType
	 * @return
	 */
	public BlockConverter getBlockConverter( String blockName, BlockConverterTypes bcType ) {
		BlockConverter results = null;
		
		if ( blockName != null && bcType != null ) {
			
			TreeMap<String, BlockConverter> bConverters = getBlockConverters().get(bcType);
			
			if ( bConverters.containsKey( blockName.toLowerCase() ) ) {
				results = bConverters.get( blockName.toLowerCase() );
			}
		}
		
		return results;
	}
	
	
	/**
	 * <p>Based upon the block converter, extract all valid outputs for this player.
	 * </p>
	 * 
	 * @param player
	 * @param bc
	 * @return
	 */
	private List<BlockConverterOutput> getBlockConverterOutputs(RankPlayer player, BlockConverter bc) {
		
		List<BlockConverterOutput> outputs = new ArrayList<>();
		
		if ( bc.getOutputs() != null ) {
			
			for ( BlockConverterOutput bcOutput : bc.getOutputs() ) {
				
				if ( bcOutput.isEnabled() ) {
					
					// If chance, and the random number is greater than the chance, then skip this output:
					if ( bcOutput.getChance() != null && 
							bcOutput.getChance().doubleValue() > (Math.random() * 100d ) ) {
						continue;
					}
					
					// Confirm the player has perms:
					if ( bcOutput.getPermissions() != null ) {
						
						boolean hasPerm = false;
						for (String perm : bcOutput.getPermissions() ) {
							if ( player.hasPermission( perm ) ) {
								hasPerm = true;
								break;
							}
						}
						
						// If player does not have perms to use this output, then skip this output:
						if ( !hasPerm ) {
							continue;
						}
						
					}
					
					// Return the selected output:
					outputs.add( bcOutput );
				}
				
			}
		}
		
		
		return outputs;
	}
	
	
	
	private boolean initialConfig() {
		boolean dirty = false;
		
		BlockConvertersInitializer initializer = new BlockConvertersInitializer();
		
		dirty = initializer.checkConfigs( getBlockConverters() );
		
		
		if ( initializer.validateBlockConverters( getBlockConverters() ) ) {
			dirty = true;
		}
		
		return dirty;
	}
	
	
	public File getConfigFile() {
		if ( this.configFile == null ) {
			
			this.configFile = new File(
					Prison.get().getDataFolder() + FILE_NAME__BLOCK_CONVERTS_CONFIG_JSON);
		}
		return configFile;
	}
	
	public void reloadConfig() {
		JsonFileIO jfio = new JsonFileIO();
		
		BlockConvertersFileConfig temp = (BlockConvertersFileConfig) jfio.readJsonFile( getConfigFile(), this );
		
		if ( temp != null ) {
			setBlockConverters( temp.getBlockConverters() );
		}
		
		boolean dirty = initialConfig();
		
		if ( dirty ) {
			saveToJson();
		}
	}
	
	public void saveToJson() {
		JsonFileIO jfio = new JsonFileIO();

		jfio.saveJsonFile(getConfigFile(), this );
	}

	public TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> getBlockConverters() {
		return blockConverters;
	}
	public void setBlockConverters(TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> blockConverters) {
		this.blockConverters = blockConverters;
	}

	/**
	 * <p>This function will take a player, and check if that player should have the ability to process all
	 * blocks under auto features.  This is the "global" setting that bypasses checking individual block types.
	 * </p>
	 * 
	 * <p>If the auto features settings `options.autoFeatures.isAutoFeaturesEnabled: false` is 
	 * disabled (set to false), then this will apply to the normal drops if 
	 * `options.normalDrop.handleNormalDropsEvents: true' is enabled (set to true).
	 * If both of those are set to disabled, then no blocks will be processed.
	 * </p> 
	 * 
	 * @param player
	 * @return
	 */
	public Boolean isProcessAutoFeaturesAllBlocks( RankPlayer player ) {
		
		if ( !processAutoFeaturesAllBlocks.containsKey( player.getName() ) ) {
			BlockConverterResults allBlocksBCR = getBlockConverterItemStacks( player, "*all*", 1, BlockConverterTypes.autoPickupFeatures );
			
			BlockConverter allBlocks = allBlocksBCR.getBlockConverter();

			boolean playerAllBlocks = ( allBlocks != null && allBlocks.isEnabled() );
			
			processAutoFeaturesAllBlocks.put( player.getName(), playerAllBlocks );
		}
		
		return processAutoFeaturesAllBlocks.get( player.getName() );
	}


}

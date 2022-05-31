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

	public enum BlockConverterTypes {
		aSample01,
		smeltFeatures,
		blockFeatures
		;
	}
	
	public BlockConvertersFileConfig() {
		super();
		
		this.blockConverters = new TreeMap<>();
		
	}

	public BlockConverterResults getBlockConverterItemStacks( RankPlayer player, 
			String blockName, int blockQuantity,
			BlockConverterTypes bcType ) {
		
		BlockConverterResults results = new BlockConverterResults( blockName, blockQuantity );

		if ( blockName != null && blockQuantity >= 1 ) {
			
			BlockConverter bc = getBlockConverter( player, blockName, bcType );
			
			if ( bc != null && blockQuantity >= bc.getKeyQuantity() ) {
				
				// getBlockConverterTargets applies chance, perms, etc...
				List<BlockConverterTarget> targets = getBlockConverterTargets( player, bc );
				
				int multiplier = blockQuantity / bc.getKeyQuantity();
				
				results.setResultsQuantityConsumed( bc.getKeyQuantity() * multiplier );
				
				for (BlockConverterTarget target : targets) {
					
					String blkName = target.getBlockName();
					int blkQuanity = target.getQuantity();
					
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
	 * <p>Based upon the block converter, extract all valid targets for this player.
	 * </p>
	 * 
	 * @param player
	 * @param bc
	 * @return
	 */
	private List<BlockConverterTarget> getBlockConverterTargets(RankPlayer player, BlockConverter bc) {
		
		List<BlockConverterTarget> targets = new ArrayList<>();
		
		if ( bc.getTargets() != null ) {
			
			for ( BlockConverterTarget bcTarget : bc.getTargets() ) {
				
				if ( bcTarget.isEnabled() ) {
					
					// If chance, and the random number is greater than the chance, then skip this target:
					if ( bcTarget.getChance() != null && 
							bcTarget.getChance().doubleValue() > (Math.random() * 100d ) ) {
						continue;
					}
					
					// Confirm the player has perms:
					if ( bcTarget.getPermissions() != null ) {
						
						boolean hasPerm = false;
						for (String perm : bcTarget.getPermissions() ) {
							if ( player.hasPermission( perm ) ) {
								hasPerm = true;
								break;
							}
						}
						
						// If player does not have perms to use this target, then skip this target:
						if ( !hasPerm ) {
							continue;
						}
						
					}
					
					// Return the selected target:
					targets.add( bcTarget );
				}
				
			}
		}
		
		
		return targets;
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

}

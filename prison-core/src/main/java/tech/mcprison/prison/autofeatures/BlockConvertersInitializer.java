package tech.mcprison.prison.autofeatures;

import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.mcprison.prison.autofeatures.BlockConvertersFileConfig.BlockConverterTypes;
import tech.mcprison.prison.output.Output;

public class BlockConvertersInitializer {

	public boolean checkConfigs(TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> treeMap) {
		boolean isDirty = false;
		
		if ( treeMap != null ) {
			
			for ( BlockConverterTypes bcType : BlockConverterTypes.values() ) {
				
				if ( !treeMap.containsKey( bcType ) ) {
					
					TreeMap<String, BlockConverter> blockConverters = new TreeMap<>();
					
					loadDefaultBlockConverters( bcType, blockConverters );
					
					if ( blockConverters.size() > 0 ) {
						
						treeMap.put( bcType, blockConverters );
						isDirty = true;
					}
				}
			}
			
		}
		
		return isDirty;
	}
	
	
	private void loadDefaultBlockConverters( BlockConverterTypes bcType, TreeMap<String, BlockConverter> blockConverters ) {
		
		switch (bcType) {
		
		case aSample01:
			
			loadDefaultBlockConverterSample01( blockConverters );
			
			break;
			
		case blockFeatures:
			
			break;

		case smeltFeatures:
			
			loadDefaultBlockConverterSmelters( blockConverters );
			
			break;
			
		default:
			break;
		}
	}
	
    
    private void loadDefaultBlockConverterSample01(TreeMap<String, BlockConverter> blockConverters) {

    	BlockConverter bc1 = new BlockConverter( "coal_ore", 9 );
    	bc1.getTargets().add( new BlockConverterTarget( "diamond", 1 ));
    	
    	
    	BlockConverter bc2 = new BlockConverter( "ice_block" );
    	BlockConverterTarget bc2t1 = new BlockConverterTarget( "water_bucket", 2);
    	BlockConverterTarget bc2t2 = new BlockConverterTarget( "gravel", 1);
    	BlockConverterTarget bc2t3 = new BlockConverterTarget( "pufferfish", 1, 0.15 );
    	bc2t3.getPermissions().add("prison.not.used.sample.pufferfish");
    	bc2t3.getPermissions().add("prison.not.used.sample.admin");
    	bc2.getTargets().add( bc2t1 );
    	bc2.getTargets().add( bc2t2 );
    	bc2.getTargets().add( bc2t3 );
    	
    	
    	
    	blockConverters.put( bc1.getKeyBlockName(), bc1 );
    	blockConverters.put( bc2.getKeyBlockName(), bc2 );
		
	}


	public void loadDefaultBlockConverterSmelters( 
    		TreeMap<String, BlockConverter> blockConverters ) {

		
		addSmeltBlockConverter( "cobblestone", "stone", blockConverters );
		addSmeltBlockConverter( "cobbled_deepslate", "deepslate", "1.17", blockConverters );
		
		addSmeltBlockConverter( "gold_ore", "gold_ingot", blockConverters );
		addSmeltBlockConverter( "nether_gold_ore", "gold_ingot", "1.16", blockConverters );
		addSmeltBlockConverter( "deepslate_gold_ore", "gold_ingot", "1.17", blockConverters );
		addSmeltBlockConverter( "raw_gold", "gold_ingot", "1.17", blockConverters );
		
		addSmeltBlockConverter( "iron_ore", "iron_ingot", blockConverters );
		addSmeltBlockConverter( "deepslate_iron_ore", "iron_ingot", "1.17", blockConverters );
		addSmeltBlockConverter( "raw_iron", "iron_ingot", "1.17", blockConverters );
		
		addSmeltBlockConverter( "coal_ore", "coal", blockConverters );
		addSmeltBlockConverter( "deepslate_coal_ore", "coal", "1.17", blockConverters );
		
		addSmeltBlockConverter( "diamond_ore", "diamond", blockConverters );
		addSmeltBlockConverter( "deepslate_diamond_ore", "diamond", "1.17", blockConverters );
		
		addSmeltBlockConverter( "emerald_ore", "emerald", blockConverters );
		addSmeltBlockConverter( "deepslate_emerald_ore", "emerald", "1.17", blockConverters );
		
		addSmeltBlockConverter( "lapis_ore", "lapis_lazuli", blockConverters );
		addSmeltBlockConverter( "deepslate_lapis_ore", "lapis_lazuli", "1.17", blockConverters );
		
		// NOTE: redstone dust is called redstone within XMaterials:
		addSmeltBlockConverter( "redstone_ore", "redstone", blockConverters );
		addSmeltBlockConverter( "deepslate_redstone_ore", "redstone", "1.17", blockConverters );
		
		addSmeltBlockConverter( "nether_quartz_ore", "quartz", blockConverters );
		
		addSmeltBlockConverterDisabled( "ancient_debris", "netherite_scrap", "1.16", blockConverters );
		
		addSmeltBlockConverter( "copper_ore", "copper_ingot", "1.17", blockConverters );
		addSmeltBlockConverter( "deepslate_copper_ore", "copper_ingot", "1.17", blockConverters );
		addSmeltBlockConverter( "raw_copper", "copper_ingot", "1.17", blockConverters );
		
		
		addSmeltBlockConverter( "sand", "glass", blockConverters );
		addSmeltBlockConverter( "red_sand", "glass", blockConverters );
		
		addSmeltBlockConverter( "clay_ball", "brick", blockConverters );
		
		addSmeltBlockConverter( "netherrack", "nether_brick", blockConverters );
		addSmeltBlockConverter( "nether_brick", "cracked_nether_brick", blockConverters );
		
		
		addSmeltBlockConverterDisabled( "cactus", "green_dye", blockConverters );
		addSmeltBlockConverterDisabled( "sea_pickle", "lime_dye", "1.13", blockConverters );
		addSmeltBlockConverterDisabled( "chorus_fruit", "popped_chorus_fruit", "1.9", blockConverters );
		addSmeltBlockConverterDisabled( "wet_sponge", "sponge", blockConverters );
		
		
		addSmeltBlockConverterDisabled( "potato", "baked_potato", blockConverters );
		addSmeltBlockConverterDisabled( "kep", "dried_kelp", "1.13", blockConverters );
		
    	
	}

    private static void addSmeltBlockConverter( 
    				String keyBlockName, String targetName, 
    				TreeMap<String, BlockConverter> blockConverters ) {
    	
    	addSmeltBlockConverter( keyBlockName, targetName, null, true, blockConverters );
    }
    private static void addSmeltBlockConverterDisabled( 
    		String keyBlockName, String targetName, 
    		TreeMap<String, BlockConverter> blockConverters ) {
    	
    	addSmeltBlockConverter( keyBlockName, targetName, null, false, blockConverters );
    }
    
    private static void addSmeltBlockConverter( 
    		String keyBlockName, String targetName, 
    		String semanticVersion,
    		TreeMap<String, BlockConverter> blockConverters ) {
    	
    	addSmeltBlockConverter( keyBlockName, targetName, null, true, blockConverters );
    }
    private static void addSmeltBlockConverterDisabled( 
    		String keyBlockName, String targetName, 
    		String semanticVersion,
    		TreeMap<String, BlockConverter> blockConverters ) {
    	
    	addSmeltBlockConverter( keyBlockName, targetName, null, false, blockConverters );
    }
    	
   private static void addSmeltBlockConverter( 
    			String keyBlockName, String targetName, 
    			String semanticVersion,
    			boolean enabled,
    			TreeMap<String, BlockConverter> blockConverters ) {
    	
    	BlockConverter bc = new BlockConverter( keyBlockName, 1 );
    	bc.getTargets().add( new BlockConverterTarget( targetName, 1 ) );
    	
    	// All converters are auto-enabled, so only need to disable:
    	if ( !enabled ) {
    		bc.setEnabled( enabled );
    	}
    	
    	if ( semanticVersion != null && semanticVersion.trim().length() > 0 ) {
    		bc.setMininumSpigotSemanticVersion( semanticVersion );
    	}
    	
    	blockConverters.put( bc.getKeyBlockName(), bc );
    }

	
	public boolean validateBlockConverters( 
			TreeMap<BlockConverterTypes, TreeMap<String, BlockConverter>> blockConverters ) {
		boolean dirty = false;
		
		// The root nodes do not need to be validated since they have to be enums
		Set<BlockConverterTypes> typeKeys = blockConverters.keySet();
		for (BlockConverterTypes type : typeKeys) {
			
			TreeMap<String, BlockConverter> converters = blockConverters.get(type);
			
			TreeMap<String, String> convertersRemovable = new TreeMap<>();
			
			
			Set<String> converterKeys = converters.keySet();
			
			for (String converterKey : converterKeys) {
				
				BlockConverter converter = converters.get( converterKey );
				
				if ( !converterKey.equals( converterKey.toLowerCase() ) ) {
					Output.get().logInfo( 
							"BlockConverters: block converter key value must be all lowercase or they will never be "
							+ "accessible. Converting to lowercase and updating config. "
							+ "BlockConverterType: %s  converterKey: %s ",
							type.name(), converterKey );
					
					convertersRemovable.put( converterKey, converterKey.toLowerCase() );
					dirty = true;
				}
				
				if ( converter.getKeyQuantity() < 1 ) {
					
					Output.get().logInfo( 
							"BlockConverters: block converter keyQuantity must have a value of 1 or greater. "
							+ "This BlockConverter is being disabled. "
									+ "BlockConverterType: %s  converterKey: %s  keyQuantity: %d ",
									type.name(), converterKey, converter.getKeyQuantity() );
					converter.setEnabled( false );
					dirty = true;
				}
				
				if ( isSpigotVersionInvalid( converter.getMininumSpigotSemanticVersion() ) ) {
					
					
					Output.get().logInfo( 
							"BlockConverters: block converter MininumSpigotSemanticVersion is invalid. "
									+ "This BlockConverter may not work as expected. No changes applied. "
									+ "BlockConverterType: %s  converterKey: %s  mininumSpigotSemanticVersion: %s ",
									type.name(), converterKey, converter.getMininumSpigotSemanticVersion() );
//					converter.setEnabled( false );
//					dirty = true;
				}
				
				
				for ( BlockConverterTarget bcTarget : converter.getTargets() ) {
					
					if ( bcTarget.getChance() <= 0 ) {
						
						Output.get().logInfo( 
								"BlockConverters: block converter target has invalid chance: Cannot be zero or negative. "
										+ "This BlockConverter target has been disabled. "
										+ "BlockConverterType: %s  converterKey: %s  "
										+ "targetBlockName: %s  chance: %d",
										type.name(), converterKey,
										bcTarget.getBlockName(), bcTarget.getChance());
						
						bcTarget.setEnabled( false );
						dirty = true;
					}
					
					if ( bcTarget.getChance() > 100 ) {
						
						Output.get().logInfo( 
								"BlockConverters: block converter target has invalid chance: Cannot be greater than 100. "
										+ "This BlockConverter target has been disabled. "
										+ "BlockConverterType: %s  converterKey: %s  "
										+ "targetBlockName: %s  chance: %d",
										type.name(), converterKey,
										bcTarget.getBlockName(), bcTarget.getChance());
						
						bcTarget.setEnabled( false );
						dirty = true;
					}

					if ( bcTarget.getQuantity() <= 0 ) {
						
						Output.get().logInfo( 
								"BlockConverters: block converter target has invalid quatity: Cannot be zero or negative. "
										+ "This BlockConverter target has been disabled. "
										+ "BlockConverterType: %s  converterKey: %s  "
										+ "targetBlockName: %s  quantity: %d",
										type.name(), converterKey,
										bcTarget.getBlockName(), bcTarget.getQuantity() );
						
						bcTarget.setEnabled( false );
						dirty = true;
					}
					
					
				}
				
				
			}

			if ( convertersRemovable.size() > 0 ) {
				Set<String> keys = convertersRemovable.keySet();
				for ( String removeableKey : keys ) {
					
					String newKey = convertersRemovable.get( removeableKey );
					
					// Remove the old key, and add the BlockConverter back with the new key:
					BlockConverter converter = converters.remove( removeableKey );
					if ( converter != null ) {
						converter.setKeyBlockName( newKey );
						converters.put( newKey, converter );
					}
					
				}
			}
			
		}
		
		return dirty;
	}


	private boolean isSpigotVersionInvalid(String mininumSpigotSemanticVersion) {
		boolean invalid = false;
		
		if ( mininumSpigotSemanticVersion != null ) {
			
			// validate that the semantic value is valid
			String semVerRegEx = "^((([0-9]+)\\.([0-9]+)\\.([0-9]+)(?:-([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?)"
					+ "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?)$";
			Pattern versionPattern = Pattern.compile( semVerRegEx );
		    Matcher matcher = versionPattern.matcher( mininumSpigotSemanticVersion );
		    invalid = matcher.matches();
		    
		}
		
		return invalid;
	}

}

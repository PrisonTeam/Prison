package tech.mcprison.prison.autofeatures;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.jackson.JacksonYaml;
import tech.mcprison.prison.output.Output;

public class AutoFeaturesFileConfig {

	public static final String FILE_NAME__AUTO_FEATURES_CONFIG_YML = "/autoFeaturesConfig.yml";
	private File configFile;
//    private FileConfiguration config;
    
    private Map<String, ValueNode> config;

    public enum AutoFeatures {

    	
    	messages,
    	
	    	inventoryIsFull(messages, "&cWARNING! Your inventory's full!"),
	    	inventoryIsFullDroppingItems(messages, "&cWARNING! Your inventory's full and you're dropping items!"),
	    	inventoryIsFullLosingItems(messages, "&cWARNING! Your inventory's full and you're losing items!"),
    	
    	
    	options,
    	
	    	general(options),
	    	
		    	isAutoManagerEnabled(general, false),
		    	dropItemsIfInventoryIsFull(general, true),
				playSoundIfInventoryIsFull(general, true),
				hologramIfInventoryIsFull(general, true),

			lore(options),
				isLoreEnabled(lore, true),
				lorePickupValue(lore, "Pickup"),
				loreSmeltValue(lore, "Smelt"),
				loreBlockValue(lore, "Block"),
				
	    	autoPickup(options),
		    	autoPickupEnabled(autoPickup, true),
		    	autoPickupAllBlocks(autoPickup, true),
		    	
		    	autoPickupCobbleStone(autoPickup, true),
		    	autoPickupStone(autoPickup, true),
		    	autoPickupGoldOre(autoPickup, true),
		    	autoPickupIronOre(autoPickup, true),
		    	autoPickupCoalOre(autoPickup, true),
		    	autoPickupDiamondOre(autoPickup, true),
		    	autoPickupRedStoneOre(autoPickup, true),
		    	autoPickupEmeraldOre(autoPickup, true),
		    	autoPickupQuartzOre(autoPickup, true),
		    	autoPickupLapisOre(autoPickup, true),
		    	autoPickupSnowBall(autoPickup, true),
		    	autoPickupGlowstoneDust(autoPickup, true),
    	
    	
	    	autoSmelt(options),
		    	autoSmeltEnabled(autoSmelt, true),
		    	autoSmeltAllBlocks(autoSmelt, true),
		    	
		    	autoSmeltGoldOre(autoSmelt, true),
		    	autoSmeltIronOre(autoSmelt, true),
	   
	    	
	    	autoBlock(options),
		    	autoBlockEnabled(autoBlock, true),
		    	autoBlockAllBlocks(autoBlock, true),
		    	
		    	autoBlockGoldBlock(autoBlock, true),
		    	autoBlockIronBlock(autoBlock, true),
		    	autoBlockCoalBlock(autoBlock, true),
		    	autoBlockDiamondBlock(autoBlock, true),
		    	autoBlockRedstoneBlock(autoBlock, true),
		    	autoBlockEmeraldBlock(autoBlock, true),
		    	autoBlockQuartzBlock(autoBlock, true),
		    	autoBlockPrismarineBlock(autoBlock, true),
		    	autoBlockLapisBlock(autoBlock, true),
		    	autoBlockSnowBlock(autoBlock, true),
		    	autoBlockGlowstone(autoBlock, true),

    	;


    	private final boolean isSection;
    	private final boolean isBoolean;
    	private final boolean isMessage;
    	
    	private final String path;
    	private final String message;
    	private final Boolean value;
    	
    	private AutoFeatures() {
    		this.isSection = true;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.path = null;
    		this.message = null;
    		this.value = null;
    	}
    	private AutoFeatures(AutoFeatures section) {
    		this.isSection = true;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    	}
    	private AutoFeatures(AutoFeatures section, String message) {
    		this.isSection = false;
    		this.isBoolean = false;
    		this.isMessage = true;
    		this.path = section.getKey();
    		this.message = message;
    		this.value = null;
    	}
    	private AutoFeatures(AutoFeatures section, Boolean value) {
    		this.isSection = false;
    		this.isBoolean = true;
    		this.isMessage = false;
    		this.path = section.getKey();
    		this.message = null;
    		this.value = value == null ? Boolean.FALSE : value;
    	}
    	
		public boolean isSection() {
			return isSection;
		}
		public boolean isBoolean() {
			return isBoolean;
		}
		public boolean isMessage() {
			return isMessage;
		}
		
		public String getPath() {
			return path;
		}
		public String getMessage() {
			return message;
		}
		public Boolean getValue() {
			return value;
		}
    	public String getKey() {
    		return (path != null ? path + "." : "") + this.name();
    	}
    	
    	public AutoFeatures fromString( String autoFeature ) {
    		AutoFeatures results = null;
    		
    		for ( AutoFeatures af : values() ) {
				if ( af.getKey().equalsIgnoreCase( autoFeature )) {
					results = af;
					break;
				}
			}
    		return results;
    	}
		
    	public void setFileConfig( Map<String, ValueNode> conf ) {
    		if ( isSection() ) {
    			// Skip this. The full path will be resolved in each key value for leaf nodes;
    			
    		} else if ( getMessage() != null ) {
    			// create a message entry:
    			TextNode text = TextNode.valueOf( getMessage() );
    			conf.put(getKey(), text);
    			
    		} else if ( getValue() != null ) {
    			// create a boolean entry:
    			BooleanNode bool = BooleanNode.valueOf( getValue().booleanValue() );
    			conf.put( getKey(), bool );
    			
    		}
    	}
    	
		/**
		 * <p>Get a String message from the FileConfiguration, and if the key 
		 * does not exist, return the default value associated with the enum
		 * entry, if it does not exist, then it returns a blank string value.
		 * <p>
		 * 
		 * @param conf
		 * @return
		 */
    	public String getMessage( Map<String, ValueNode> conf ) {
    		String results = null;
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isTextual() ) {
    			TextNode text = (TextNode) conf.get( getKey() );
    			results = text.asText( getMessage() );
    		}
    		else if ( getMessage() != null ) {
    			results = getMessage();
    		}
    		
    		return results;
    	}
    	
    	/**
    	 * <p>Get's the boolean value from the FileConfiguration, and if the key
    	 * does not exist, then it returns the default value associated with the 
    	 * enum entry, if it does not exist, then it returns false.
    	 * @param conf
    	 * @return
    	 */
    	public boolean getBoolean( Map<String, ValueNode> conf ) {
    		boolean results = false;
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isBoolean() ) {
    			BooleanNode bool = (BooleanNode) conf.get( getKey() );
    			results = bool.asBoolean();
    		}
    		else if ( getValue() != null ) {
    			results = getValue().booleanValue();
    		}
    		
    		return results;
    	}

    
    }
    
    public AutoFeaturesFileConfig() {
        
        if(!getConfigFile().exists()){
            createConfigurationFile();
            
            Output.get().logWarn( 
            		String.format( "Notice: AutoManager config was just created. " +
            				"You must configure it to use it. File: %s", 
            				getConfigFile().getName()) );
        }

        
        
        // Load the configuration file:
        JacksonYaml jYaml = new JacksonYaml();
        Map<String, ValueNode> map = jYaml.loadYamlConfigFile( getConfigFile() );
        
        setConfig( map );
    }

	private void createConfigurationFile() {
		
		// Make sure file does not exist prior to trying to create it with the YAML tool since
		// it is unknown who it will react...
		if ( !getConfigFile().exists() ) {
			File dir = getConfigFile().getParentFile();

			if ( !dir.exists() ) {
				dir.mkdirs();
			}
			
			try {
				getConfigFile().createNewFile();
				
				JacksonYaml jYaml = new JacksonYaml();
				setConfig( jYaml.loadYamlConfigFile( getConfigFile()) );
				
//				FileConfiguration conf = YamlConfiguration.loadConfiguration(getConfigFile());
				
				// Not yet enabled... this will fully replace the line items below:
			    for ( AutoFeatures autoFeat : AutoFeatures.values() ) {
					autoFeat.setFileConfig( getConfig() );
				}
				
				
//				conf.createSection("Messages");
//				conf.createSection("Options");
//				
//				conf.set("Messages.InventoryIsFullDroppingItems", "&cWARNING! Your inventory's full and you're dropping items!");
//				conf.set("Messages.InventoryIsFullLosingItems", "&cWARNING! Your inventory's full and you're losing items!");
//				conf.set("Messages.InventoryIsFull", "&cWARNING! Your inventory's full!");
//				
//				conf.set("Options.General.AreEnabledFeatures", false);
//				conf.set("Options.General.DropItemsIfInventoryIsFull", true);
//				conf.set("Options.General.playSoundIfInventoryIsFull", true);
//				conf.set("Options.General.hologramIfInventoryIsFull", true);
//				
//				conf.set("Options.AutoPickup.AutoPickupEnabled", true);
//				conf.set("Options.AutoPickup.AutoPickupAllBlocks",true);
//				conf.set("Options.AutoPickup.AutoPickupCobbleStone",true);
//				conf.set("Options.AutoPickup.AutoPickupStone",true);
//				conf.set("Options.AutoPickup.AutoPickupGoldOre", true);
//				conf.set("Options.AutoPickup.AutoPickupIronOre", true);
//				conf.set("Options.AutoPickup.AutoPickupCoalOre", true);
//				conf.set("Options.AutoPickup.AutoPickupDiamondOre", true);
//				conf.set("Options.AutoPickup.AutoPickupRedstoneOre", true);
//				conf.set("Options.AutoPickup.AutoPickupEmeraldOre", true);
//				conf.set("Options.AutoPickup.AutoPickupQuartzOre", true);
//				conf.set("Options.AutoPickup.AutoPickupLapisOre", true);
//				conf.set("Options.AutoPickup.AutoPickupSnowBall", true);
//				conf.set("Options.AutoPickup.AutoPickupGlowstoneDust", true);
//				
//				conf.set("Options.AutoSmelt.AutoSmeltEnabled", true);
//				conf.set("Options.AutoSmelt.AutoSmeltAllBlocks", true);
//				conf.set("Options.AutoSmelt.AutoSmeltGoldOre", true);
//				conf.set("Options.AutoSmelt.AutoSmeltIronOre", true);
//				
//				conf.set("Options.AutoBlock.AutoBlockEnabled", true);
//				conf.set("Options.AutoBlock.AutoBlockAllBlocks", true);
//				conf.set("Options.AutoBlock.AutoBlockGoldBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockIronBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockCoalBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockDiamondBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockRedstoneBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockEmeraldBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockQuartzBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockPrismarineBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockLapisBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockSnowBlock", true);
//				conf.set("Options.AutoBlock.AutoBlockGlowstone", true);
				
			    jYaml.writeYamlConfigFile( getConfigFile(), getConfig() );
			    
//				conf.save(getConfigFile());
			} catch (IOException e) {
				
				Output.get().logError( 
						String.format( "Failure! Unable to initialize a new AutoFeatures config file. %s :: %s", 
								getConfigFile().getName(), e.getMessage()), e );
			}
			
		}
		
	}

	/**
	 * <p>This updates AutoFeatures with a String value.
	 * </p>
	 * 
	 * @param feature
	 * @param value
	 */
	public void setFeature( AutoFeatures feature, String value ) {
		
		if ( feature.isSection() ) {
			Output.get().logError( 
					String.format( "Error: AutoFeature %s is a section (path) and cannot be " +
							"used with a value. value = [%s]", 
							feature.getKey(), 
							( value == null ? "null" : value )) );
			
		}
		else if ( value == null || value.trim().length() == 0 ) {
			Output.get().logError( 
					String.format( "Error: AutoFeature %s value cannot be null or empty.", 
							feature.getKey()) );
		} 
		else if ( !feature.isMessage() ) {
			Output.get().logError( 
					String.format( "Error: AutoFeature %s value is not a message type so it " +
							"cannot be assigned a message.", 
							feature.getKey()) );
		}
		else {

			TextNode text = TextNode.valueOf( value );
			
			getConfig().put( feature.getKey(), text );
		}
		
	}
	
	/**
	 * <p>This updates AutoFeatures with a boolean value.
	 * </p>
	 * 
	 * @param feature
	 * @param value
	 */
	public void setFeature( AutoFeatures feature, boolean value ) {
		
		if ( feature.isSection() ) {
			Output.get().logError( 
					String.format( "Error: AutoFeature %s is a section (path) and cannot be " +
							"used with a value. value = [%s]", 
							feature.getKey(), 
							Boolean.valueOf( value ).toString() ) );
			
		} 
		else if ( !feature.isBoolean() ) {
			Output.get().logError( 
					String.format( "Error: AutoFeature %s value is not a boolean type so it " +
							"cannot be assigned a boolean value.", 
							feature.getKey()) );
		}
		else {
			
			BooleanNode bool = BooleanNode.valueOf( value  );
			
			getConfig().put( feature.getKey(), bool );
		}
		
	}
	

	/**
	 * This returns the config's feature value if it is a boolean.  If it is not
	 * a boolean config value, it will return a value false.
	 * 
	 * @param feature
	 * @return
	 */
	public boolean isFeatureBoolean( AutoFeatures feature ) {
		return feature.getBoolean( getConfig() );
	}
	
	public String getFeatureMessage( AutoFeatures feature ) {
		return feature.getMessage( getConfig() );
	}
	
	public boolean saveConf() {
		return saveConf( getConfig() );
	}

	/**
	 * <p>This function attempts to save the AutoFeatures configurations to the
	 * file system.  This function uses a temporary file to initially perform the save, 
	 * then when it is successfully finished, it then deletes the original file, and 
	 * renames the temp file to the correct file name.  This swapping of files 
	 * will prevent the loss of configuration data if something should go wrong in the 
	 * initial saving of the data since the original file will not be deleted first.
	 * </p>
	 * 
	 * @param afConfig
	 * @return
	 */
	private boolean saveConf( Map<String, ValueNode> config ) {
		boolean success = false;
		
		File cFile = getConfigFile();
		String tempFileName = cFile.getName() + ".temp";
		File tempFile = new File(cFile.getParentFile(), tempFileName);
		
		try {
			// First save to the temp file. If it fails it will throw an exception and 
			// will prevent the deletion of the existing file:
			JacksonYaml jYaml = new JacksonYaml();
			jYaml.writeYamlConfigFile( tempFile, getConfig() );
			
			if ( cFile.exists() ) {
				cFile.delete();
			}

			// The save cannot be considered successful until after the rename is complete.
			success = tempFile.renameTo( cFile );
		}
		catch ( Exception e ) {
			Output.get().logError( 
					String.format( "Failure! Unable to save AutoFeatures config file. %s :: %s", 
							cFile.getName(), e.getMessage()), e );
		}
		
		return success;
	}

	
	public File getConfigFile() {
		if ( this.configFile == null ) {
			
			this.configFile = new File(
					Prison.get().getDataFolder() + FILE_NAME__AUTO_FEATURES_CONFIG_YML);
		}
		return configFile;
	}
	public void setConfigFile( File configFile ) {
		this.configFile = configFile;
	}

	public Map<String, ValueNode> getConfig() {
		return config;
	}

	public void setConfig( Map<String, ValueNode> config ) {
		this.config = config;
	}

//	private FileConfiguration getConfig() {
//		return config;
//	}
//	private void setConfig( FileConfiguration config ) {
//		this.config = config;
//	}


}

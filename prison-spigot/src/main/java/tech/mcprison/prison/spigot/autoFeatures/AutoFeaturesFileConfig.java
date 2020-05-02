package tech.mcprison.prison.spigot.autoFeatures;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

public class AutoFeaturesFileConfig {

	public static final String FILE_NAME__AUTO_FEATURES_CONFIG_YML = "/autoFeaturesConfig.yml";
	private File configFile;
    private FileConfiguration config;

    
    public enum AutoFeatures {

    	
    	messages,
    	
	    	inventoryIsFull(messages, "&cWARNING! Your inventory's full!"),
	    	inventoryIsFullDroppingItems(messages, "&cWARNING! Your inventory's full and you're dropping items!"),
	    	inventoryIsFullLosingItems(messages, "&cWARNING! Your inventory's full and you're losing items!"),
    	
    	
    	options,
    	
	    	general(options),
	    	
		    	areEnabledFeatures(general, false),
		    	dropItemsIfInventoryIsFull(general, true),
				playSoundIfInventoryIsFull(general, true),
				hologramIfInventoryIsFull(general, true),
    	
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


    	private final boolean section;
    	private final String path;
    	private final String message;
    	private final Boolean value;
    	private AutoFeatures() {
    		this.section = true;
    		this.path = null;
    		this.message = null;
    		this.value = null;
    	}
    	private AutoFeatures(AutoFeatures section) {
    		this.section = true;
    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    	}
    	private AutoFeatures(AutoFeatures section, String message) {
    		this.section = false;
    		this.path = section.getKey();
    		this.message = message;
    		this.value = null;
    	}
    	private AutoFeatures(AutoFeatures section, Boolean value) {
    		this.section = false;
    		this.path = section.getKey();
    		this.message = null;
    		this.value = value == null ? Boolean.FALSE : value;
    	}
		public boolean isSection() {
			return section;
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
		
    	public void setFileConfig( FileConfiguration conf ) {
    		if ( isSection() ) {
    			// create a section entry:
    			conf.createSection( getKey() );
    		} else if ( getMessage() != null ) {
    			// create a message entry:
    			conf.set(getKey(), getMessage());
    		} else if ( getValue() != null ) {
    			// create a boolean entry:
    			conf.set(getKey(), getValue().booleanValue());
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
    	public String getMessage( FileConfiguration conf ) {
    		String message = conf.getString( getKey() );
    		return  ( message != null ? message : (getMessage() != null ? getMessage() : ""));
    	}
    	
    	/**
    	 * <p>Get's the boolean value from the FileConfiguration, and if the key
    	 * does not exist, then it returns the default value associated with the 
    	 * enum entry, if it does not exist, then it returns false.
    	 * @param conf
    	 * @return
    	 */
    	public boolean getBoolean( FileConfiguration conf ) {
    		
    		return (conf.contains(getKey()) ? conf.getBoolean(getKey()) : 
    			(getValue() != null ? getValue().booleanValue() : false) );
    	}

    	
    }
    
    public AutoFeaturesFileConfig() {
        
        if(!getConfigFile().exists()){
            createConfigurationFile();
        }

        // Load the configuration file:
        setConfig( YamlConfiguration.loadConfiguration(getConfigFile()) );
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
				
				FileConfiguration conf = YamlConfiguration.loadConfiguration(getConfigFile());
				
				// Not yet enabled... this will fully replace the line items below:
//			    for ( AutoFeatures autoFeat : AutoFeatures.values() ) {
//					autoFeat.setFileConfig( conf );
//				}
				
				
				conf.createSection("Messages");
				conf.createSection("Options");
				
				conf.set("Messages.InventoryIsFullDroppingItems", "&cWARNING! Your inventory's full and you're dropping items!");
				conf.set("Messages.InventoryIsFullLosingItems", "&cWARNING! Your inventory's full and you're losing items!");
				conf.set("Messages.InventoryIsFull", "&cWARNING! Your inventory's full!");
				
				conf.set("Options.General.AreEnabledFeatures", false);
				conf.set("Options.General.DropItemsIfInventoryIsFull", true);
				conf.set("Options.General.playSoundIfInventoryIsFull", true);
				conf.set("Options.General.hologramIfInventoryIsFull", true);
				
				conf.set("Options.AutoPickup.AutoPickupEnabled", true);
				conf.set("Options.AutoPickup.AutoPickupAllBlocks",true);
				conf.set("Options.AutoPickup.AutoPickupCobbleStone",true);
				conf.set("Options.AutoPickup.AutoPickupStone",true);
				conf.set("Options.AutoPickup.AutoPickupGoldOre", true);
				conf.set("Options.AutoPickup.AutoPickupIronOre", true);
				conf.set("Options.AutoPickup.AutoPickupCoalOre", true);
				conf.set("Options.AutoPickup.AutoPickupDiamondOre", true);
				conf.set("Options.AutoPickup.AutoPickupRedstoneOre", true);
				conf.set("Options.AutoPickup.AutoPickupEmeraldOre", true);
				conf.set("Options.AutoPickup.AutoPickupQuartzOre", true);
				conf.set("Options.AutoPickup.AutoPickupLapisOre", true);
				conf.set("Options.AutoPickup.AutoPickupSnowBall", true);
				conf.set("Options.AutoPickup.AutoPickupGlowstoneDust", true);
				
				conf.set("Options.AutoSmelt.AutoSmeltEnabled", true);
				conf.set("Options.AutoSmelt.AutoSmeltAllBlocks", true);
				conf.set("Options.AutoSmelt.AutoSmeltGoldOre", true);
				conf.set("Options.AutoSmelt.AutoSmeltIronOre", true);
				
				conf.set("Options.AutoBlock.AutoBlockEnabled", true);
				conf.set("Options.AutoBlock.AutoBlockAllBlocks", true);
				conf.set("Options.AutoBlock.AutoBlockGoldBlock", true);
				conf.set("Options.AutoBlock.AutoBlockIronBlock", true);
				conf.set("Options.AutoBlock.AutoBlockCoalBlock", true);
				conf.set("Options.AutoBlock.AutoBlockDiamondBlock", true);
				conf.set("Options.AutoBlock.AutoBlockRedstoneBlock", true);
				conf.set("Options.AutoBlock.AutoBlockEmeraldBlock", true);
				conf.set("Options.AutoBlock.AutoBlockQuartzBlock", true);
				conf.set("Options.AutoBlock.AutoBlockPrismarineBlock", true);
				conf.set("Options.AutoBlock.AutoBlockLapisBlock", true);
				conf.set("Options.AutoBlock.AutoBlockSnowBlock", true);
				conf.set("Options.AutoBlock.AutoBlockGlowstone", true);
				
				conf.save(getConfigFile());
			} catch (IOException e) {
				
				Output.get().logError( 
						String.format( "Failure! Unable to initialize a new AutoFeatures config file. %s :: %s", 
								getConfigFile().getName(), e.getMessage()), e );
			}
			
		}
		
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
	public boolean saveConf( FileConfiguration afConfig )
	{
		boolean success = false;
		
		File cFile = getConfigFile();
		String tempFileName = cFile.getName() + ".temp";
		File tempFile = new File(cFile.getParentFile(), tempFileName);
		
		try {
			// First save to the temp file. If it fails it will throw an exception and 
			// will prevent the deletion of the existing file:
			afConfig.save( tempFile );
			
			if ( cFile.exists() ) {
				cFile.delete();
			}

			// The save cannot be considered successful until after the rename is complete.
			success = tempFile.renameTo( cFile );
		}
		catch ( IOException e ) {
			Output.get().logError( 
					String.format( "Failure! Unable to save AutoFeatures config file. %s :: %s", 
							cFile.getName(), e.getMessage()), e );
		}
		
		return success;
	}

	public File getConfigFile() {
		if ( this.configFile == null ) {
			this.configFile = new File(
					SpigotPrison.getInstance().getDataFolder() + FILE_NAME__AUTO_FEATURES_CONFIG_YML);
		}
		return configFile;
	}
	public void setConfigFile( File configFile ) {
		this.configFile = configFile;
	}

	public FileConfiguration getConfig() {
		return config;
	}
	public void setConfig( FileConfiguration config ) {
		this.config = config;
	}

}

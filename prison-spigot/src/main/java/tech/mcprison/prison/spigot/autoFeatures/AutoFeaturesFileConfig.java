package tech.mcprison.prison.spigot.autoFeatures;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.spigot.SpigotPrison;

public class AutoFeaturesFileConfig {

	private File configFile;
    private FileConfiguration conf;

    
    public enum AutoFeatures {

    	
    	messages,
    	
	    	inventoryIsFull(messages, "&cWARNING! Your inventory's full!"),
	    	inventoryIsFullDroppingItems(messages, "&cWARNING! Your inventory's full and you're dropping items!"),
	    	inventoryIsFullLosingItems(messages, "&cWARNING! Your inventory's full and you're losing items!"),
    	
    	
    	options,
    	
	    	general(options),
	    	
		    	areEnabledFeatures(general, false),
		    	dropItemsIfInventoryIsFull(general, true),
    	
    	
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
        setConfigFile( new File(SpigotPrison.getInstance().getDataFolder() + "/autoFeaturesConfig.yml") );
        
        if(!getConfigFile().exists()){
            createConfigurationFile();
        }

        // Load the configuration file:
        setConf( YamlConfiguration.loadConfiguration(getConfigFile()) );
    }

	private void createConfigurationFile()
	{
		try {
		    File dir = getConfigFile().getParentFile();
		    dir.mkdirs();
		    
		    getConfigFile().createNewFile();
		    FileConfiguration conf = YamlConfiguration.loadConfiguration(getConfigFile());

		    // Not yet enabled... this will fully replace the line items below:
//		    for ( AutoFeatures autoFeat : AutoFeatures.values() ) {
//				autoFeat.setFileConfig( conf );
//			}
		    
		    
		    conf.createSection("Messages");
		    conf.createSection("Options");

		    conf.set("Messages.InventoryIsFullDroppingItems", "&cWARNING! Your inventory's full and you're dropping items!");
		    conf.set("Messages.InventoryIsFullLosingItems", "&cWARNING! Your inventory's full and you're losing items!");
		    conf.set("Messages.InventoryIsFull", "&cWARNING! Your inventory's full!");

		    conf.set("Options.General.AreEnabledFeatures", false);
		    conf.set("Options.General.DropItemsIfInventoryIsFull", true);

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
		    e.printStackTrace();
		}
	}

    @Deprecated
    public FileConfiguration getFile(){
        return conf;
    }

	public File getConfigFile() {
		return configFile;
	}
	public void setConfigFile( File configFile ) {
		this.configFile = configFile;
	}

	public FileConfiguration getConf() {
		return conf;
	}
	public void setConf( FileConfiguration conf ) {
		this.conf = conf;
	}

}

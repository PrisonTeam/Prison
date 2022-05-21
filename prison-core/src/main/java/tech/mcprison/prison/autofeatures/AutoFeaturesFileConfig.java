package tech.mcprison.prison.autofeatures;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.ValueNode.NodeType;
import tech.mcprison.prison.file.YamlFileIO;
import tech.mcprison.prison.output.Output;

public class AutoFeaturesFileConfig {

	public static final String FILE_NAME__AUTO_FEATURES_CONFIG_YML = "/autoFeaturesConfig.yml";
	private File configFile;
//    private FileConfiguration config;
    
    private Map<String, ValueNode> config;
    
    /**
     * 
     * <p>Pertaining to canceling a block break even, or just canceling the drops, the ability to 
     * cancel the drops was added in v1.12.x.  Therefore v1.8 through v1.11 cannot use that technique
     * and instead must use the even canceling.
     * </p>
     * 
     * <pre>BlockBreakEvent.setDropItems(false)</p>
     * 
     * <p>To cancel a BlockBreakEvent use:
     * </p>
     * 
     * <pre>cancelAllBlockBreakEvents: true</pre>
     * <pre>cancelAllBlockEventBlockDrops: false</pre>
     * 
     * <p>To cancel the drops, just reverse those two settings' values:
     * </p>
     * 
     * <pre>cancelAllBlockBreakEvents: false</pre>
     * <pre>cancelAllBlockEventBlockDrops: true</pre>
     * 
     *
     */
    public enum AutoFeatures {

    	
    	autoManager,
    	
	    	isAutoManagerEnabled(autoManager, true),
	    	isAutoManagerEnabled__ReadMe(autoManager, 
	    			"Set to 'false' to turn off all auto manager features.  " +
	    					"Otherwise this must be set to 'true' to allow any of the " +
	    			"options to work."),
	    	
// NOTE: Moved to the spigot language files:	    	
//    	messages,
//    	
//	    	inventoryIsFull(messages, "&cWARNING! Your inventory's full!"),
//	    	inventoryIsFullDroppingItems(messages, "&cWARNING! Your inventory's full and you're dropping items!"),
//	    	inventoryIsFullLosingItems(messages, "&cWARNING! Your inventory's full and you're losing items!"),
    	
    	
    	options,
    	
    		otherPlugins(options),
	    		
	    		isProcessMcMMOBlockBreakEvents(otherPlugins, false),
	    		isProcessEZBlocksBlockBreakEvents(otherPlugins, false),

	    		isProcessQuestsBlockBreakEvents(otherPlugins, false),
	    		
	    		otherPluginSupport__ReadMe(otherPlugins, 
	    				"NOTE: If you are using spigot v1.12.0 or higher, then do not use these " +
	    				"'forced' settings, instead adjust both 'cancelAllBlockBreakEvents' and " +
	    				"'cancelAllBlockEventBlockDrops' since that will help ensure it works better."),
	    		
	    		
	    		isUseCustomBlocksCustomItemsGetDrops( otherPlugins, true ),
	    		
    		
	    	blockBreakEvents(options),
	    	
	    		// Setting this to true will cancel the block break events (normal prison behavior):
	    		// Canceling events is mandatory for Spigot v1.8 through v1.11.x.
	    		cancelAllBlockBreakEvents(blockBreakEvents, true),
	    		
	    		cancelAllBlockBreakEvents__ReadMe(blockBreakEvents, 
	    				"NOTE: If spigot v1.8.0 through 1.11.x you must use 'true' for this setting, " +
	    				"otherwise if using Spigot v1.13.0, or higher, can use 'false'."),
	    		
	    		
	    		// Setting this to false will not zero out the block drops (normal prison behavior).
	    		// When set to true, it will zero it out so if the block break event is not canceled,
	    		// then it will prevent double drops:
	    		// Canceling the drops was added in Spigot v1.12.x.
	    		cancelAllBlockEventBlockDrops(blockBreakEvents, false),
	    		
	    		cancelAllBlockEventBlockDrops__ReadMe(blockBreakEvents, 
	    				"NOTE: If spigot v1.8.0 through 1.11.x you must use 'false' for this setting, " +
	    				"otherwise if using Spigot v1.13.0, or higher, can use 'true'. " +
	    				"This setting MUST be the opposit of 'cancelAllBlockBreakEvents'."),
	    		
	    		
	    		
	    		applyBlockBreaksThroughSyncTask(blockBreakEvents, true),

	    		
	    		blockBreakEventPriority(blockBreakEvents, "LOW"),
	    		
	    		ProcessPrisons_ExplosiveBlockBreakEventsPriority(blockBreakEvents, "LOW"),

	    		
	    		

		    	TokenEnchantBlockExplodeEventPriority(blockBreakEvents, "DISABLED"),
		    	
		    	CrazyEnchantsBlastUseEventPriority(blockBreakEvents, "DISABLED"),

		    	ZenchantmentsBlockShredEventPriority(blockBreakEvents, "DISABLED"),
	    	
		    	PrisonEnchantsExplosiveEventPriority(blockBreakEvents, "DISABLED"),
		    	
		    	
		    	
		    	blockBreakEvents__ReadMe(blockBreakEvents, 
		    			"Use the following event priorities with the blockBreakEvents: " +
		    			"DISABLED, LOWEST, LOW, NORMAL, HIGH, HIGHEST, BLOCKEVENTS, MONITOR" ),
		    	
		    	
		    	
	    	general(options),
	    	
		    	
	    		isCalculateFoodExhustion(general, true),
		    	
		    	isCalculateSilkEnabled(general, true),
		    	isCalculateDropAdditionsEnabled(general, true),
		    	
		    	isCalculateXPEnabled(general, true),
		    	givePlayerXPAsOrbDrops(general, false),
		    	
		    	
		    	
		    inventory(options),
		    	

				isAutoSellPerBlockBreakEnabled(inventory, false),
				permissionAutoSellPerBlockBreakEnabled(inventory, "prison.automanager.autosell"),
				
				permissionAutoSellPerBlockBreakEnabled__ReadMe(inventory, 
						"If OP then you cannot use this permission node since it would always " +
						"be enabled. Using a value of 'disable' will turn it off for everyone."),
//				
//				isAutoSellPerBlockBreakInlinedEnabled(general, false),
				
				isAutoSellIfInventoryIsFull(inventory, true),
				
				isAutoSellIfInventoryIsFullForBLOCKEVENTSPriority(inventory, false),
				
				
				dropItemsIfInventoryIsFull(inventory, true),
				
				playSoundIfInventoryIsFull(inventory, true),
				playSoundIfInventoryIsFullSound(inventory, "block_note_block_pling" ),
				playSoundIfInventoryIsFullSoundVolume(inventory, 4.0d ),
				playSoundIfInventoryIsFullSoundPitch(inventory, 1.0d ),
				playSoundIfInventoryIsFullSound__readme(inventory, 
						"The name of the sound must be valid for the server platform and " +
						"its version, and is case insensitive. To get a list of valid " +
						"sounds use the command: " +
						"'/prison utils sounds list <page>'. Page is optional. Use page " +
						"numbers to see all available sounds.  An invalid sound will " +
						"default to NOTE_PLING, BLOCK_NOTE_PLING, or BLOCK_NOTE_BLOCK_PLING, " +
						"as valid for your server."),
				
				actionBarMessageIfInventoryIsFull(inventory, true),
//				hologramIfInventoryIsFull(general, false),

				
			
			tokens(options),
				
				tokensEnabled( tokens, false ),
				tokensBlocksPerToken( tokens, 100 ),
				
				
			permissions(options),
			
				permissionAutoPickup(permissions, "prison.automanager.pickup"),
				permissionAutoSmelt(permissions, "prison.automanager.smelt"),
				permissionAutoBlock(permissions, "prison.automanager.block"),
			
				
			lore(options),
			
				isLoreEnabled(lore, true),
				
				lorePickupValue(lore, "&dAuto Pickup&7"),
				loreSmeltValue(lore, "&dAuto Smelt&7"),
				loreBlockValue(lore, "&dAuto Block&7"),
				
				loreTrackBlockBreakCount(lore, false),
				loreBlockBreakCountName(lore, "&dPrison Blocks Mined:&7 "),
				loreBlockExplosionCountName(lore, "&dPrison Blocks Exploded:&7 "),
				
				loreDurabiltyResistance(lore, false),
				loreDurabiltyResistanceName(lore, "&dDurability Resistance&7"),
				
				
			autoFeatures(options),
			
				isAutoFeaturesEnabled(autoFeatures, true),
			
				autoPickupEnabled(autoFeatures, true),
				autoSmeltEnabled(autoFeatures, true),
				autoBlockEnabled(autoFeatures, true),
				
			
			normalDrop(options),
			
    			handleNormalDropsEvents(normalDrop, true),
    			normalDropSmelt(normalDrop, true),
    			normalDropBlock(normalDrop, true),
    			

    		durability(options),
    		
    			isCalculateDurabilityEnabled(durability, false),
    			
    			preventToolBreakageThreshold__ReadMe(durability, 
    					"This option stops the tool from losing any more durability " +
    					"once it hits the number specified with the threshold"),
    			isPreventToolBreakage(durability, false),
    			preventToolBreakageThreshold(durability, 10),
    			
    			
    		fortuneFeature(options),
    			
	    		isCalculateFortuneEnabled(fortuneFeature, true),
	    		
	    		fortuneMultiplierMax(fortuneFeature, 0 ),

	    		
	    		isExtendBukkitFortuneCalculationsEnabled(fortuneFeature, true),
	    		extendBukkitFortuneFactorPercentRangeLow(fortuneFeature, 70 ),
	    		extendBukkitFortuneFactorPercentRangeHigh(fortuneFeature, 110 ),
	    		extendBukkitFortune__ReadMe(fortuneFeature, "To get fortune to work, First " +
	    				"try to use the extendedBukkitFortune (set it to true). If it won't " +
	    				"work, then you must disable it (set it to false), then enable " +
	    				"CalculateAltFortune. AltFortune will never work if " +
	    				"extendedBukkitFortune is enabled." ),
	    		
	    		
	    		isCalculateAltFortuneEnabled(fortuneFeature, false),
	    		isCalculateAltFortuneOnAllBlocksEnabled(fortuneFeature, false),
	    		
	    		

	    		
	    		
	    	pickupFeature(options),
		    	pickupLimitToMines(pickupFeature, true),
		    	pickupAllBlocks(pickupFeature, true),
			    	
		    	pickupBlockNameListEnabled( pickupFeature, false ),
		    	pickupBlockNameList(pickupFeature, NodeType.STRING_LIST,
			    				"coal_block", "iron_ore"),
			    	
		    	pickupCobbleStone(pickupFeature, true),
		    	pickupStone(pickupFeature, true),
		    	pickupGoldOre(pickupFeature, true),
		    	pickupIronOre(pickupFeature, true),
		    	pickupCoalOre(pickupFeature, true),
		    	pickupDiamondOre(pickupFeature, true),
		    	pickupRedStoneOre(pickupFeature, true),
		    	pickupEmeraldOre(pickupFeature, true),
		    	pickupQuartzOre(pickupFeature, true),
		    	pickupLapisOre(pickupFeature, true),
		    	pickupSnowBall(pickupFeature, true),
		    	pickupGlowstoneDust(pickupFeature, true),
	    	
    	
	    	smeltFeature(options),
		    	
		    	smeltLimitToMines(smeltFeature, true),
		    	smeltAllBlocks(smeltFeature, true),

		    	
		    	smeltCobblestone(smeltFeature, false),
		    	
		    	smeltGoldOre(smeltFeature, true),
		    	smeltIronOre(smeltFeature, true),
		    	smeltCoalOre(smeltFeature, true),
		    	smeltDiamondlOre(smeltFeature, true),
		    	smeltEmeraldOre(smeltFeature, true),
		    	smeltLapisOre(smeltFeature, true),
		    	smeltRedstoneOre(smeltFeature, true),
		    	smeltNetherQuartzOre(smeltFeature, true),
		    	smeltAncientDebris(smeltFeature, true),
		    	smeltCopperOre(smeltFeature, true),
	   
	    	
	    	blockFeature(options),
		    	blockLimitToMines(blockFeature, true),
		    	blockAllBlocks(blockFeature, true),

		    	
		    	blockGoldBlock(blockFeature, true),
		    	blockIronBlock(blockFeature, true),
		    	blockCoalBlock(blockFeature, true),
		    	blockDiamondBlock(blockFeature, true),
		    	blockRedstoneBlock(blockFeature, true),
		    	blockEmeraldBlock(blockFeature, true),
		    	blockQuartzBlock(blockFeature, true),
		    	blockPrismarineBlock(blockFeature, true),
		    	blockLapisBlock(blockFeature, true),
		    	blockSnowBlock(blockFeature, true),
		    	blockGlowstone(blockFeature, true),
		    	blockCopperBlock(blockFeature, true),
		   
		    	
//		examplesOnlyNotUsed,    	
//		    exampleOfBlockConversions(examplesOnlyNotUsed),
//		    
//		    	sampleBlockTransformer(exampleOfBlockConversions, NodeType.BLOCK_CONVERTER, "sample01" )
		    
		    	
		    	
//		    eventInjector(options),
//		    	eventInjectorEnabled(eventInjector, false),
//		    	
//		    	eventInjectorData(eventInjector),
		    	
		    
		    	
//	    	debug(options),
//	    		isDebugSupressOnBlockBreakEventCancels(debug, false),
//	    		isDebugSupressOnTEExplodeEventCancels(debug, false),
//	    		isDebugSupressOnCEBlastUseEventCancels(debug, false), 
//	    		isDebugSupressOnPEExplosiveEventCancels(debug, false),
//	    		isDebugSupressOnPrisonMinesBlockBreakEventCancels(debug, false)

    	;


    	private final AutoFeatures parent;
    	private final boolean isSection;
    	private final boolean isBoolean;
    	private final boolean isMessage;
    	private final boolean isInteger;
    	private final boolean isLong;
    	private final boolean isDouble;
    	private final boolean isStringList;
    	private final boolean isBlockConverter;
    	
    	private final String path;
    	private final String message;
    	private final Boolean value;
    	private final Integer intValue;
    	private final Long longValue;
    	private final Double doubleValue;
    	private final List<String> listValue;
    	private final TreeMap<String, BlockConverter> blockConverters;
    	
    	private AutoFeatures() {
    		this.parent = null;
    		this.isSection = true;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.isInteger = false;
    		this.isLong = false;
    		this.isDouble = false;
    		this.isStringList = false;
    		this.isBlockConverter = false;
    		
    		this.path = null;
    		this.message = null;
    		this.value = null;
    		this.intValue = null;
    		this.longValue = null;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section) {
    		this.parent = section;
    		this.isSection = true;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.isInteger = false;
    		this.isLong = false;
    		this.isDouble = false;
    		this.isStringList = false;
    		this.isBlockConverter = false;

    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    		this.intValue = null;
    		this.longValue = null;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section, String message) {
    		this.parent = section;
    		this.isSection = false;
    		this.isBoolean = false;
    		this.isMessage = true;
    		this.isInteger = false;
    		this.isLong = false;
    		this.isDouble = false;
    		this.isStringList = false;
    		this.isBlockConverter = false;

    		this.path = section.getKey();
    		this.message = message;
    		this.value = null;
    		this.intValue = null;
    		this.longValue = null;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section, Boolean value) {
    		this.parent = section;
    		this.isSection = false;
    		this.isBoolean = true;
    		this.isMessage = false;
    		this.isInteger = false;
    		this.isLong = false;
    		this.isDouble = false;
    		this.isStringList = false;
    		this.isBlockConverter = false;

    		this.path = section.getKey();
    		this.message = null;
    		this.value = value == null ? Boolean.FALSE : value;
    		this.intValue = null;
    		this.longValue = null;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section, int value) {
    		this.parent = section;
    		this.isSection = false;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.isInteger = true;
    		this.isLong = false;
    		this.isDouble = false;
    		this.isStringList = false;
    		this.isBlockConverter = false;
    		
    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    		this.intValue = value;
    		this.longValue = null;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section, long value) {
    		this.parent = section;
    		this.isSection = false;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.isInteger = false;
    		this.isLong = true;
    		this.isDouble = false;
    		this.isStringList = false;
    		this.isBlockConverter = false;
    		
    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    		this.intValue = null;
    		this.longValue = value;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section, double value) {
    		this.parent = section;
    		this.isSection = false;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.isInteger = false;
    		this.isLong = false;
    		this.isDouble = true;
    		this.isStringList = false;
    		this.isBlockConverter = false;
    		
    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    		this.intValue = null;
    		this.longValue = null;
    		this.doubleValue = value;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    	}
    	private AutoFeatures(AutoFeatures section, NodeType nodeType, String... values ) {
    		this.parent = section;
    		this.isSection = false;
    		this.isBoolean = false;
    		this.isMessage = false;
    		this.isInteger = false;
    		this.isLong = false;
    		this.isDouble = false;
    		this.isStringList = (nodeType == NodeType.STRING_LIST);
    		this.isBlockConverter = (nodeType == NodeType.BLOCK_CONVERTER);
    		
    		this.path = section.getKey();
    		this.message = null;
    		this.value = null;
    		this.intValue = null;
    		this.longValue = null;
    		this.doubleValue = null;
    		this.listValue = new ArrayList<>();
    		this.blockConverters = new TreeMap<>();
    		
    		if ( nodeType == NodeType.STRING_LIST ) {
    			
    			if ( values != null && values.length > 0 ) {
    				
    				this.listValue.addAll( Arrays.asList( values ));
    			}
    		}
    		else if ( nodeType == NodeType.BLOCK_CONVERTER ) {
    			loadSampleBlockConverters( blockConverters, Arrays.asList( values ) );
    		}
    	}
    	
		public AutoFeatures getParent() {
			return parent;
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
		public boolean isInteger() {
			return isInteger;
		}
		public boolean isLong() {
			return isLong;
		}
		public boolean isDouble() {
			return isDouble;
		}
		public boolean isStringList() {
			return isStringList;
		}
		public boolean isBlockConverter() {
			return isBlockConverter;
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
		
    	public Integer getIntValue() {
			return intValue;
		}
		public Long getLongValue() {
			return longValue;
		}
		public Double getDoubleValue() {
			return doubleValue;
		}
		public List<String> getListValue() {
			return listValue;
		}
		public TreeMap<String, BlockConverter> getBlockConverters() {
			return blockConverters;
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
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isTextNode() ) {
    			TextNode text = (TextNode) conf.get( getKey() );
    			results = text.getValue();
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
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isBooleanNode() ) {
    			BooleanNode bool = (BooleanNode) conf.get( getKey() );
    			results = bool.getValue();
    		}
    		else if ( getValue() != null ) {
    			results = getValue().booleanValue();
    		}
    		
    		return results;
    	}
    	
    	
    	public int getInteger( Map<String, ValueNode> conf ) {
    		int results = 0;
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isIntegerNode() ) {
    			IntegerNode intValue = (IntegerNode) conf.get( getKey() );
    			results = intValue.getValue();
    		}
    		else if ( getIntValue() != null ) {
    			results = getIntValue();
    		}
    		
    		return results;
    	}    	
    	
    	public double getDouble( Map<String, ValueNode> conf ) {
    		double results = 0d;
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isDoubleNode() ) {
    			DoubleNode doubleValue = (DoubleNode) conf.get( getKey() );
    			results = doubleValue.getValue();
    		}
    		else if ( getDoubleValue() != null ) {
    			results = getDoubleValue();
    		}
    		
    		return results;
    	}    	
    	
    	public List<String> getStringList( Map<String, ValueNode> conf  ) {
    		List<String> results = null;
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isStringListNode() ) {
    			StringListNode list = (StringListNode) conf.get( getKey() );
    			results = list.getValue();
    		}
    		else if ( getListValue() != null && getListValue().size() > 0 ) {
    			results = getListValue();
    		}
    		
    		if ( results == null ) {
    			results = new ArrayList<>();
    		}
    			
    		return results;
    	}
    	
    	
    	public TreeMap<String, BlockConverter> getBlockConverters( Map<String, ValueNode> conf ) {
    		TreeMap<String, BlockConverter> results = null;
    		
    		
    		if ( conf.containsKey(getKey()) && conf.get( getKey() ).isBlockConvertersNode() ) {
    			BlockConvertersNode blockConverters = (BlockConvertersNode) conf.get( getKey() );
    			results = blockConverters.getValue();
    		}
//    		else if ( getListValue() != null && getListValue().size() > 0 ) {
//    			results = getListValue();
//    		}
    		
    		if ( results == null ) {
    			results = new BlockConvertersNode().getValue();
    		}
    		
    		return results;
    	}
    	

    	/**
    	 * <p>Get the children nodes to the given item.
    	 * </p>
    	 * 
    	 * @return
    	 */
    	public List<AutoFeatures> getChildren() {
    		return getChildren(this);
    	}

    	/**
    	 * <p>Get the children for the specified node.
    	 * </p>
    	 * 
    	 * @param parent
    	 * @return
    	 */
    	public List<AutoFeatures> getChildren(AutoFeatures parent) {
    		List<AutoFeatures> results = new ArrayList<>();
    		
    		for ( AutoFeatures af : values() ) {
				if ( af.getParent() == parent ) {
					results.add( af );
				}
			}
    		
    		return results;
    	}
    
    }
    
    protected AutoFeaturesFileConfig() {
        
    	this.config = new LinkedHashMap<>();
    	
    	
    	// If an AutoFeatures defines a block converter, then the actual block 
    	// converter object, with all dependencies, needs to be stored in this
    	// TreeMap so it can be accessed later without having to regenerate 
    	// everything.  The enums of AutoFeatures, when they identify that the
    	// are of type BlockConverter, needs to store the complex data outside 
    	// of the enum.  This is it... with it being a TreeMap it will be 
    	// fast access to it, and as many nodes as desired, can be added.
    	// NOTE: ONE entry in this TreeMap is related to ONE entry in the enum.
//    	this.blockConverters = new TreeMap<>();
    	
		
		// The following is strictly not needed to ensure that the configs are
		// created, but this, somehow, does ensure that the order in which 
		// the sections are written are in the order in which they are defined
		// within the enum.
	    for ( AutoFeatures autoFeat : AutoFeatures.values() ) {
			autoFeat.setFileConfig( getConfig() );
		}
	    
	    

        // This may sound counter intuitive if the config file does not exist, 
        // but when trying to load the yaml with the
	    // config fully loaded with the default values will trigger a save if 
	    // anyone of them do not exist in the config file.
        // So do not perform any special first time processing here since it
        // is handled within the loadYamlAutoFeatures code.
        
		YamlFileIO yamlFileIO = Prison.get().getPlatform().getYamlFileIO( getConfigFile() );
		
		List<AutoFeatures> dne = yamlFileIO.loadYamlAutoFeatures( getConfig() );

		dne.size();
		
//		Set<String> keys = getConfig().keySet();
//		for ( String key : keys ) {
//			ValueNode value = getConfig().get( key );
//			Output.get().logInfo( "AutoFeaturesFileConfig: ### %s  %s", 
//					key, value.toString() );
//		}
		
//		List<String> blockNames = getFeatureStringList( AutoFeatures.autoPickupBlockNameList );
//		StringBuilder sbBlockName = new StringBuilder( String.join( ", ", blockNames ) ).insert( 0, "[" ).append( "]" );
//		Output.get().logInfo( "###--### AutoFeaturesFileConfig: test autoPickupBlockNameList: length = %d  value = %s ", 
//				blockNames.size(), sbBlockName.toString() );

    }

    
    public static void loadSampleBlockConverters( 
    		TreeMap<String, BlockConverter> blockConverters, List<String> sampleSets ) {

    	if ( sampleSets.contains( "sample01" ) ) {
    		BlockConverter bc1 = new BlockConverter( "coal_ore" );
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
    		
    		
    		
    		blockConverters.put( bc1.getKey(), bc1 );
    		blockConverters.put( bc2.getKey(), bc2 );
    		
    	}
	}


	public void reloadConfig() {
    	// First clear the configs:
    	getConfig().clear();
    	
    	
    	// Load from the config file:
    	YamlFileIO yamlFileIO = Prison.get().getPlatform().getYamlFileIO( getConfigFile() );
		List<AutoFeatures> dne = yamlFileIO.loadYamlAutoFeatures( getConfig() );
		
		dne.size();
		
		// need to reload the auto features event listeners:
		Prison.get().getPlatform().reloadAutoFeaturesEventListeners();
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
	
	public int getInteger( AutoFeatures feature ) {
		return feature.getInteger( getConfig() );
	}
	
	public double getDouble( AutoFeatures feature ) {
		return feature.getDouble( getConfig() );
	}
	
	public List<String> getFeatureStringList( AutoFeatures feature ) {
		
		return feature.getStringList( getConfig() );
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
	 * @param config
	 * @return
	 */
	private boolean saveConf( Map<String, ValueNode> config ) {
		
		YamlFileIO yamlFileIO = Prison.get().getPlatform().getYamlFileIO( getConfigFile() );
		return yamlFileIO.saveYamlAutoFeatures( config );
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

}

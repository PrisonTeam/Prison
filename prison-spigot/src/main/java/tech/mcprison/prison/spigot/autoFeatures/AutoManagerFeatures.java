package tech.mcprison.prison.spigot.autoFeatures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

/**
 * <p>This class controls the data and the basic functions for auto features.
 * It loads the settings only once, and uses them on each event that is raised
 * in order to reduce overhead in reloading all of them on each block break
 * event.  Then, if an external process should happen to save changes to 
 * these settings, then it will reload the settings so they are always
 * current.
 * </p>
 * 
 *
 */
public class AutoManagerFeatures
	extends OnBlockBreakEventListener {
	
	public enum ItemLoreCounters {
		
		// NOTE: the String value must include a trailing space!
		
		itemLoreBlockBreakCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Mined:" +
							ChatColor.GRAY + " "),
		
		itemLoreBlockExplodeCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Exploded:" +
							ChatColor.GRAY + " ");
		
		
		private final String lore;
		ItemLoreCounters( String lore ) {
			this.lore = lore;
		}
		public String getLore() {
			return lore;
		}
		
	}
	
	public enum ItemLoreEnablers {
		Pickup,
		Smelt,
		Block
		;
	}

	private Random random = new Random();
	
    private FileConfiguration autoFeaturesConfig = null;
	
	private Configuration autoConfigs;
	
	private boolean areEnabledFeatures = false;

	private boolean dropItemsIfInventoryIsFull = false;
	private boolean playSoundIfInventoryIsFull = false;
	private boolean hologramIfInventoryIsFull = false;

	private boolean autoPickupEnabled = false;
	private boolean autoPickupAllBlocks = false;
	private boolean autoPickupCobbleStone = false;
	private boolean autoPickupStone = false;
	private boolean autoPickupGoldOre = false;
	private boolean autoPickupIronOre = false;
	private boolean autoPickupCoalOre = false;
	private boolean autoPickupDiamondOre = false;
	private boolean autoPickupRedstoneOre = false;
	private boolean autoPickupEmeraldOre = false;
	private boolean autoPickupQuartzOre = false;
	private boolean autoPickupLapisOre = false;
	private boolean autoPickupSnowBall = false;
	private boolean autoPickupGlowstoneDust = false;
	
	// AutoSmelt booleans from configs
	private boolean autoSmeltEnabled = false;
	private boolean autoSmeltAllBlocks = false;
	private boolean autoSmeltGoldOre = false;
	private boolean autoSmeltIronOre = false;
	
	// AutoBlock booleans from configs
	private boolean autoBlockEnabled = false;
	private boolean autoBlockAllBlocks = false;
	private boolean autoBlockGoldBlock = false;
	private boolean autoBlockIronBlock = false;
	private boolean autoBlockCoalBlock = false;
	private boolean autoBlockDiamondBlock = false;
	private boolean autoBlockRedstoneBlock = false;
	private boolean autoBlockEmeraldBlock = false;
	private boolean autoBlockQuartzBlock = false;
	private boolean autoBlockPrismarineBlock = false;
	private boolean autoBlockLapisBlock = false;
	private boolean autoBlockSnowBlock = false;
	private boolean autoBlockGlowstone = false;

	
	public AutoManagerFeatures() {
		super();
		
		setup();
	}

	
	private void setup() {
		
		this.autoConfigs = getAutoFeaturesConfig();
//		this.autoConfigs = SpigotPrison.getInstance().getAutoFeaturesConfig();

		this.areEnabledFeatures = autoConfigs.getBoolean("Options.General.AreEnabledFeatures");
		
		//if (isAreEnabledFeatures()) 
		{
			
			this.dropItemsIfInventoryIsFull = autoConfigs.getBoolean("Options.General.DropItemsIfInventoryIsFull");
			this.playSoundIfInventoryIsFull = autoConfigs.getBoolean("Options.General.playSoundIfInventoryIsFull");
			this.hologramIfInventoryIsFull = autoConfigs.getBoolean("Options.General.hologramIfInventoryIsFull");
			
			// AutoPickup booleans from configs
			this.autoPickupEnabled = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupEnabled");
			this.autoPickupAllBlocks = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupAllBlocks");
			this.autoPickupCobbleStone = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupCobbleStone");
			this.autoPickupStone = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupStone");
			this.autoPickupGoldOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupGoldOre");
			this.autoPickupIronOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupIronOre");
			this.autoPickupCoalOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupCoalOre");
			this.autoPickupDiamondOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupDiamondOre");
			this.autoPickupRedstoneOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupRedstoneOre");
			this.autoPickupEmeraldOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupEmeraldOre");
			this.autoPickupQuartzOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupQuartzOre");
			this.autoPickupLapisOre = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupLapisOre");
			this.autoPickupSnowBall = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupSnowBall");
			this.autoPickupGlowstoneDust = autoPickupAllBlocks || autoConfigs.getBoolean("Options.AutoPickup.AutoPickupGlowstoneDust");
			
			// AutoSmelt booleans from configs
			this.autoSmeltEnabled = autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltEnabled");
			this.autoSmeltAllBlocks = autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltAllBlocks");
			this.autoSmeltGoldOre = autoSmeltAllBlocks || autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltGoldOre");
			this.autoSmeltIronOre = autoSmeltAllBlocks || autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltIronOre");
			
			// AutoBlock booleans from configs
			this.autoBlockEnabled = autoConfigs.getBoolean("Options.AutoBlock.AutoBlockEnabled");
			this.autoBlockAllBlocks = autoConfigs.getBoolean("Options.AutoBlock.AutoBlockAllBlocks");
			this.autoBlockGoldBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockGoldBlock");
			this.autoBlockIronBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockIronBlock");
			this.autoBlockCoalBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockCoalBlock");
			this.autoBlockDiamondBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockDiamondBlock");
			this.autoBlockRedstoneBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockRedstoneBlock");
			this.autoBlockEmeraldBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockEmeraldBlock");
			this.autoBlockQuartzBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockQuartzBlock");
			this.autoBlockPrismarineBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockPrismarineBlock");
			this.autoBlockLapisBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockLapisBlock");
			this.autoBlockSnowBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockSnowBlock");
			this.autoBlockGlowstone = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockGlowstone");
			
		}
	}
	
	

	/**
     * <p>This lazy loading of the FileConfiguration for the AutoFeatures will ensure
     * the file is loaded from the file system only one time and only when it is first
     * used.  This ensures that if it is never used, it is never loaded in to memory.
     * </p>
     * 
     * @return
     */
    public FileConfiguration getAutoFeaturesConfig() {
    	if ( this.autoFeaturesConfig == null ) {
    		AutoFeaturesFileConfig afc = new AutoFeaturesFileConfig();
    		this.autoFeaturesConfig = afc.getConfig();

    	}
        return autoFeaturesConfig;
    }
    
    
    /**
     * <p>This change in this function, to move it in to this class, allows the 
     * reloading of the parameters that controls all of the behaviors whenever
     * the data is saved.
     * </p>
     * 
     * @return
     */
    public boolean saveAutoFeaturesConfig() {
    	boolean success = false;
    	FileConfiguration afConfig = getAutoFeaturesConfig();
  
    	if ( afConfig != null ) {
    		AutoFeaturesFileConfig afc = new AutoFeaturesFileConfig();
    		success = afc.saveConf(afConfig);
    		
    		// reload the internal settings:
    		setup();
    	}
    	return success;
    }
	
	/**
	 * <p>If the fortune level is zero, then this function will always return a value of one.
	 * </p>
	 * 
	 * <p>If it is non-zero, then this function will return a value of one, plus an 
	 * equally distributed chance of returning an additional 0 to fortuneLevel 
	 * bonuses of that material.
	 * </p>
	 * 
	 * <p>This applies to "all" materials within the mine, including chests, shulkers,
	 * signs etc... 
	 * </p>
	 * 
	 * @param fortuneLevel
	 * @return
	 */
    public int getDropCount(int fortuneLevel) {
        int j = 
        		fortuneLevel == 0 ? 
        				1 : 
        				1 + getRandom().nextInt(fortuneLevel + 1);

        return j;
    }


    protected boolean workaroundSilkTouch (ItemStack itemInHand){
		return itemInHand.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
	}

	/*
	* Drops are wrong with old 1.14.4 releases of spigot
	* but got fixed in newer versions.
	*
	* For older versions, a good way to get the right drops would be to use BlockDropItemEvent.getItems(), but it's deprecated
	* */
	protected int autoPickup( boolean autoPickup, Player p, ItemStack itemInHand, BlockBreakEvent e ) {
		int count = 0;
		if (autoPickup) {
			Collection<ItemStack> drops = e.getBlock().getDrops(itemInHand);
			if (drops != null && drops.size() > 0 ) {

				// Add the item to the inventory
				for ( ItemStack itemStack : drops ) {
					count += itemStack.getAmount();
					dropExtra( p.getInventory().addItem(itemStack), p, e.getBlock() );
				}

				if ( count > 0 ) {
					// Set the broken block to AIR and cancel the event
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
				}
			}
		}
		return count;	
	}

	protected void autoSmelt( boolean autoSmelt, Material source, Material destination, Player p, Block block  ) {
		if (autoSmelt && p.getInventory().contains(source)) {
			int count = itemCount(source, p);
			if ( count > 0 ) {
				p.getInventory().removeItem(new ItemStack(source, count));
				dropExtra( p.getInventory().addItem(new ItemStack(destination, count)), p, block );
			}
		}
	}
	protected void autoBlock( boolean autoBlock, Material source, Material destination, Player p, Block block  ) {
		autoBlock(autoBlock, source, destination, 9, p, block );
	}
	
	protected void autoBlock( boolean autoBlock, Material source, Material destination, int targetCount, Player p, Block block  ) {
		if ( autoBlock ) {
			int count = itemCount(source, p);
			if ( count >= targetCount ) {
				int mult = count / targetCount;
				
				p.getInventory().removeItem(new ItemStack(source, mult * targetCount));
				dropExtra( p.getInventory().addItem(new ItemStack(destination, mult)), p, block);
			}
		}
	}

	/**
	 * <p>Lapis is really dyed ink sacks, so need to have special processing to ensure we process the 
	 * correct material, and not just any ink sack.
	 * </p>
	 * 
	 * <p><b>Warning:</b> this will not work with minecraft 1.15.x since magic numbers have been
	 * 						eliminated.
	 * </p>
	 * 
	 * @param autoBlock
	 * @param player
	 */
	protected void autoBlockLapis( boolean autoBlock, Player player, Block block  ) {
		if ( autoBlock ) {
			// ink_sack = 351:4 
			ItemStack lapisItemStack = new ItemStack(Material.INK_SACK, 1, (short) 4);
			
			int count = itemCount(lapisItemStack, player);
			if ( count >= 9 ) {
				int mult = count / 9;
				
				ItemStack removeLapisItemStack = new ItemStack(Material.INK_SACK,  mult * 9, (short) 4);
				player.getInventory().removeItem(removeLapisItemStack);
				dropExtra( player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, mult)), player, block );
				
			}
		}
	}
	
	private int itemCount(Material source, Player player) {
		int count = 0;
		PlayerInventory inv = player.getInventory();
		for (ItemStack is : inv.all(source).values()) {
			if (is != null && is.getType() == source) {
				count = count + is.getAmount();
			}
		}
		return count;
	}
	private int itemCount(ItemStack source, Player player) {
		int count = 0;
		PlayerInventory inv = player.getInventory();
		for (ItemStack is : inv.all(source).values()) {
			if (is != null) {
				count = count + is.getAmount();
			}
		}
		return count;
	}
	
	private void dropExtra( HashMap<Integer, ItemStack> extra, Player player, Block block ) {
		if ( extra != null && extra.size() > 0 ) {
			for ( ItemStack itemStack : extra.values() ) {
				player.getWorld().dropItem( player.getLocation(), itemStack );
				
				notifyPlayerThatInventoryIsFull( player, block );
			}
		}
	}

	private void notifyPlayerThatInventoryIsFull( Player player, Block block ) {

		String message = autoConfigs.getString( "Messages.InventoryIsFull" );
		
		// Play sound when full
		if ( isPlaySoundIfInventoryIsFull() ) {
			Prison.get().getMinecraftVersion() ;
			
			// This hard coding the Sound enum causes failures in spigot 1.8.8 since it does not exist:
			Sound sound;
			try {
			    sound = Sound.valueOf("ANVIL_USE"); // pre 1.9 sound
			} catch(IllegalArgumentException e) {
			    sound = Sound.valueOf("BLOCK_ANVIL_PLACE"); // post 1.9 sound
			}
			
			player.playSound(player.getLocation(), sound, 10F, 1F);
		}
		
		if ( isHologramIfInventoryIsFull() ) {
			displayMessageHologram( block, message , player);
		}
		else {
			actionBarVersion(player, message);
		}
	}

	private void actionBarVersion(Player player, String message) {
		if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0 ) {
			displayActionBarMessage(player, message);
		} 
		else {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(SpigotPrison.format(message)));
		}
	}

	private void displayMessageHologram(Block block, String message, Player p){
		ArmorStand as = (ArmorStand) block.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		as.setCustomName(SpigotPrison.format(message));
		Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), as::remove, (7L * 20L));
	}
	
	private void displayActionBarMessage(Player player, String message) {
		SpigotPlayer prisonPlayer = new SpigotPlayer( player );
		Prison.get().getPlatform().showActionBar( prisonPlayer, message, 80 );
	}

	
	/**
	 * <p>Using the ItemLoreEnables, check to see if the item in the hand has the
	 * specified Lore.  If so, then return 100.0 if just the lore, otherwise 
	 * if it has a number to the right of the Lore, convert it to a double and
	 * return it.
	 * </p>
	 * 
	 * <p>The only valid value to follow the lore, can only be a double number, 
	 * or an integer.  For example 1.234, 50, 75.567, 99.0, or 100.0.
	 * Any value less than 0 will be zero, which is the same as no lore (disabled).
	 * Any value greater than 100 will be 100.0. No values following the lore will
	 * be treated as 100.0 percent.  Do not include percent sign or unit of measure.
	 * </p>
	 * 
	 * @param loreEnabler
	 * @param player
	 * @return Percent chance of Lore enablement.
	 */
	protected double doesItemHaveAutoFeatureLore( ItemLoreEnablers loreEnabler, Player player ) {
		double results = 100.0;
		
		ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
		if ( itemInHand.hasItemMeta() ) {
			ItemMeta meta = itemInHand.getItemMeta();
			if ( meta.hasLore() ) {
				for ( String lore : meta.getLore() ) {
					if ( lore.startsWith( loreEnabler.name() )) {
						String value = lore.replace( loreEnabler.name(), "" ).trim();
						
						if ( value.length() > 0 ) {
							
							try {
								results = Double.parseDouble( value );
							}
							catch ( NumberFormatException e ) {
								
								// Error: Default to 100%
								// Do not generate log messages since there will be 1000's...
								results = 100.0;
							}
							
							if ( results < 0.0 ) {
								results = 0.0;
							}
							
							if ( results > 100.0 ) {
								results = 100.0;
							}
						}
					}
				}
			}
		}
		
		return results;
	}

	protected void autoFeaturePickup( BlockBreakEvent e, Player p )
	{
			Material brokenBlock = e.getBlock().getType();
			String blockName = brokenBlock.toString().toLowerCase();
			
			ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( p );
			
			@SuppressWarnings( "unused" )
			int count = 0;

			switch (blockName) {
				case "cobblestone":
					count += autoPickup( isAutoPickupCobbleStone(), p, itemInHand, e );
					break;
					
				case "stone":
					count += autoPickup( isAutoPickupStone(), p, itemInHand, e );
					break;
					
				case "gold_ore":
					count += autoPickup( isAutoPickupGoldOre(), p, itemInHand, e );
					break;
					
				case "iron_ore":
					count += autoPickup( isAutoPickupIronOre(), p, itemInHand, e );
					break;
					
				case "coal_ore":
					count += autoPickup( isAutoPickupCoalOre(), p, itemInHand, e );
					break;
					
				case "diamond_ore":
					count += autoPickup( isAutoPickupDiamondOre(), p, itemInHand, e );
					break;
					
				case "redstone_ore":
					count += autoPickup( isAutoPickupRedstoneOre(), p, itemInHand, e );
					break;
					
				case "emerald_ore":
					count += autoPickup( isAutoPickupEmeraldOre(), p, itemInHand, e );
					break;
					
				case "quartz_ore":
					count += autoPickup( isAutoPickupQuartzOre(), p, itemInHand, e );
					break;
					
				case "lapis_ore":
					count += autoPickup( isAutoPickupLapisOre(), p, itemInHand, e );
					break;
					
				case "snow_ball":
					count += autoPickup( isAutoPickupSnowBall(), p, itemInHand, e );
					break;
					
				case "glowstone_dust": // works 1.15.2
					count += autoPickup( isAutoPickupGlowstoneDust(), p, itemInHand, e );
					break;
					
				default:
					count += autoPickup( isAutoPickupAllBlocks(), p, itemInHand, e );
					break;
						
			}
			
	//				Output.get().logInfo( "In mine: %s  blockName= [%s] %s  drops= %s  count= %s  dropNumber= %s ", 
	//						mine.getName(), blockName, Integer.toString( dropNumber ),
	//						(e.getBlock().getDrops(itemInHand) != null ? e.getBlock().getDrops(itemInHand).size() : "-=null=-"), 
	//						Integer.toString( count ), Integer.toString( dropNumber )
	//						);
	//				
		}


	protected void autoFeatureSmelt( BlockBreakEvent e, Player p )
	{
		Block block = e.getBlock();
	
		autoSmelt( isAutoSmeltGoldOre(), Material.GOLD_ORE, Material.GOLD_INGOT, p, block );
	
		autoSmelt( isAutoSmeltIronOre(), Material.IRON_ORE, Material.IRON_INGOT, p, block );
	}


	protected void autoFeatureBlock( BlockBreakEvent e, Player p )
	{
		Block block = e.getBlock();
		
		// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
		// checks within each block type's function call.  So in one pass, could hit on more 
		// than one of these for multiple times too.
		autoBlock( isAutoBlockGoldBlock(), Material.GOLD_INGOT, Material.GOLD_BLOCK, p, block );
		
		autoBlock( isAutoBlockIronBlock(), Material.IRON_INGOT, Material.IRON_BLOCK, p, block );
	
		autoBlock( isAutoBlockCoalBlock(), Material.COAL, Material.COAL_BLOCK, p, block );
		
		autoBlock( isAutoBlockDiamondBlock(), Material.DIAMOND, Material.DIAMOND_BLOCK, p, block );
	
		autoBlock( isAutoBlockRedstoneBlock(), Material.REDSTONE, Material.REDSTONE_BLOCK, p, block );
		
		autoBlock( isAutoBlockEmeraldBlock(), Material.EMERALD, Material.EMERALD_BLOCK, p, block );
	
		autoBlock( isAutoBlockQuartzBlock(), Material.QUARTZ, Material.QUARTZ_BLOCK, 4, p, block );
	
		autoBlock( isAutoBlockPrismarineBlock(), Material.PRISMARINE_SHARD, Material.PRISMARINE, 4, p, block );
	
		autoBlock( isAutoBlockSnowBlock(), Material.SNOW_BALL, Material.SNOW_BLOCK, 4, p, block );
	
		autoBlock( isAutoBlockGlowstone(), Material.GLOWSTONE_DUST, Material.GLOWSTONE, 4, p, block );
		
		autoBlockLapis( isAutoBlockLapisBlock(), p, block );
	}



	protected void itemLoreCounter( ItemStack itemInHand, ItemLoreCounters itemLore, int blocks )
	{
		if ( itemInHand.hasItemMeta() ) {
	
			List<String> lore = new ArrayList<>();
			
			String prisonBlockBroken = itemLore.getLore();
			
			ItemMeta meta = itemInHand.getItemMeta();
			
			if ( meta.hasLore() ) {
				lore = meta.getLore();
				
				boolean found = false;
				for( int i = 0; i < lore.size(); i++ ) {
					if ( lore.get( i ).startsWith( prisonBlockBroken ) ) {
						String val = lore.get( i ).replace( prisonBlockBroken, "" ).trim();
						
						int count = blocks;
						try {
							count += Integer.parseInt( val );
						}
						catch ( NumberFormatException e1 ) {
							Output.get().logError( "AutoManager: tool counter failure. lore= [" +
									lore.get( i ) + "] val= [" + val + "] error: " + 
									e1.getMessage() );								}
						
						lore.set( i, prisonBlockBroken + count );
						
						found = true;
						break;
					}
				}
				
				if ( !found ) {
					lore.add( prisonBlockBroken + 1 );
				}
				
				
			} else {
				lore.add( prisonBlockBroken + 1 );
			}
			
			meta.setLore( lore );
			
			itemInHand.setItemMeta( meta );
			
			// incrementCounterInName( itemInHand, meta );
			
		}
	}


	protected void incrementCounterInName( ItemStack itemInHand, ItemMeta meta ) {
		String name = meta.getDisplayName();
		if ( !meta.hasDisplayName() || name == null || name.trim().length() == 0 ) {
			name = itemInHand.getType().name().toLowerCase().replace("_", " ").trim();
			name += " [1]";
		} else {
			
			int j = name.lastIndexOf( ']' );
			if ( j == -1 ) {
				name += " [1]";
			} else {
			
				if ( j != -1 ) {
					int i = name.lastIndexOf( '[', j );
					
					if ( i != -1 ) {
						String numStr = name.substring( i + 1, j );
						
//									Output.get().logInfo( String.format( "AutoManager: name:  %s : %s ",  
//											name, numStr) );
						
						
						try {
							int blocksMined = Integer.parseInt( numStr );
							name = name.substring( 0, i ).trim() + " [" + ++blocksMined + "]";
						}
						catch ( NumberFormatException e1 ) {
							Output.get().logError( "AutoManager: tool counter failure. tool name= [" +
									name + "] error: " + 
									e1.getMessage() );
						}
						
					}
				}
			}
			
		
		}
		if ( name != null ) {
			meta.setDisplayName( name );
			itemInHand.setItemMeta( meta );
			
		}
	}
	
	/**
	 * <p>This should calculate and apply the durability consumption on the tool.
	 * </p>
	 * 
	 * <p>The damage is calculated as a value of one durability, but all damage can be
	 * skipped if the tool has a durability enchantment.  If it does, then there is a
	 * percent chance of 1 in (1 + durabilityLevel).  So if a tool has a durability level
	 * of 1, then there is a 50% chance. Level of 2, then a 66.6% chance. Level of 3 has
	 * a 75% chance. And a level of 9 has a 90% chance. There are no upper limits on
	 * durability enchantment levels.
	 * </p>
	 * 
	 * <p>Some blocks may have a damage of greater than 1, but for now, this
	 * does not take that in to consideration. If hooking up in the future, just 
	 * set the initial damage to the correct value based upon block type that was mined.
	 * </p>
	 * 
	 * <p>Based upon the following URL.  See Tool Durability.
	 * </p>
	 * https://minecraft.gamepedia.com/Item_durability
	 * 
	 * @param itemInHand
	 */
	protected void calculateDurability( Player player, ItemStack itemInHand ) {
		short damage = 1;  // Generally 1 unless instant break block then zero.

		if ( itemInHand.containsEnchantment( Enchantment.DURABILITY ) ) {
			int durabilityLevel = itemInHand.getEnchantmentLevel( Enchantment.DURABILITY );

			// the chance of losing durability is 1 in (1+level)
			// So if the random int == 0, then take damage, otherwise none.
			if ( getRandom().nextInt( durabilityLevel ) > 0 ) {
				damage = 0;
			}
		}
		
		if ( damage > 0 ) {
			short maxDurability = itemInHand.getType().getMaxDurability();
			short durability = itemInHand.getDurability();
			
			int newDurability = durability + damage;
			
			if ( newDurability > maxDurability ) {
				// Item breaks! ;(
				SpigotPrison.getInstance().getCompatibility().breakItemInMainHand( player );
	        } 
			else {
				itemInHand.setDurability( (short) newDurability );
			}
			
			player.updateInventory();
			
		}
	}
	
	/**
	 * <p>This function is based upon the following wiki page. 
	 * </p>
	 * https://minecraft.gamepedia.com/Fortune
	 * 
	 * <p>"<b>Ore:</b> For coal ore, diamond ore, emerald ore, lapis lazuli ore, 
	 * nether gold ore,‌[upcoming: JE 1.16] and nether quartz ore, 
	 * Fortune I gives a 33% chance to multiply drops by 2 (averaging 33% increase), 
	 * Fortune II gives a chance to multiply drops by 2 or 3 (25% chance each, 
	 * averaging 75% increase), and Fortune III gives a chance to multiply drops 
	 * by 2, 3, or 4 (20% chance each, averaging 120% increase).
	 * </p>
	 * <p>"Generally speaking, Fortune gives a weight of 2 to a normal drop chance 
	 * and adds a weight of 1 for each extra drop multiplier. The drop multipliers 
	 * are the integers between 2 and Fortune Level + 1, inclusive.
	 * </p>
	 * <p>"The formula to calculate the average drops multiplier is 
	 * 1/(Fortune Level+2) + (Fortune Level+1)/2, which means 
	 * Fortune IV gives 2.67x drops on average, Fortune V gives 
	 * 3.14x drops on average, etc. "
	 * </p>
	 * 
	 * <p><b>Discrete random</b> Glowstone, melons, nether wart, redstone ore, 
	 * sea lanterns, and sweet berries use a discrete uniform distribution, 
	 * meaning each possible drop amount is equally likely to be dropped. 
	 * Fortune increases the maximum number of drops by 1 per level. However, 
	 * maximum drop limitations may apply: glowstone has a cap of 4 glowstone 
	 * dust, sea lanterns have a cap of 5 prismarine crystals, and melons have 
	 * a cap of 9 melon slices. If a drop higher than these maximums is rolled, 
	 * it is rounded down to the cap. 
	 * </p>
	 * 
	 * 
	 * @param blocks
	 * @param fortuneLevel
	 */
	protected void calculateFortune( ItemStack blocks, short fortuneLevel ) {
		
		int count = blocks.getAmount();
		
		int multiplier = 1;
		
		switch ( blocks.getType() ){
			
			case COAL:
			case DIAMOND:
			case EMERALD:
			case LAPIS_BLOCK:
			case GOLD_BLOCK:
			case QUARTZ_BLOCK:
			case COAL_ORE:
			case DIAMOND_ORE:
			case EMERALD_ORE:
			case LAPIS_ORE:
			case GOLD_ORE:
			case QUARTZ_ORE:
				
				int rnd = getRandom().nextInt( 100 );

				switch ( fortuneLevel )
				{
					case 0:
						break;
					case 1:
						if ( rnd <= 33 ) {
							multiplier = 2;
						}
						break;
						
					case 2:
						if ( rnd <= 25 ) {
							multiplier = 2;
						}
						else if ( rnd <= 50 ) {
							multiplier = 3;
						}
						break;
						
					case 3: 
						if ( rnd <= 20 ) {
							multiplier = 2;
						}
						else if ( rnd <= 40 ) {
							multiplier = 3;
						}
						else if ( rnd <= 60 ) {
							multiplier = 4;
						}
						break;
						
						
					case 4: 
						if ( rnd <= 16 ) {
							multiplier = 2;
						}
						else if ( rnd <= 32 ) {
							multiplier = 3;
						}
						else if ( rnd <= 48 ) {
							multiplier = 4;
						}
						else if ( rnd <= 64 ) {
							multiplier = 5;
						}
						break;
						
					default:
						// values of 5 or higher
						if ( rnd <= 16 ) {
							multiplier = 2;
						}
						else if ( rnd <= 32 ) {
							multiplier = 3;
						}
						else if ( rnd <= 48 ) {
							multiplier = 4;
						}
						else if ( rnd <= 64 ) {
							multiplier = 5;
						}
						else if ( rnd <= 74 ) {
							// Only 8% not 16% chance
							multiplier = 6;
						}
						break;
				}
				
				// multiply the multiplier:
				count *= multiplier;
				break;

			case GLOWSTONE:
			case GLOWSTONE_DUST:
			case REDSTONE:
			case SEA_LANTERN:
			case GLOWING_REDSTONE_ORE:
			case PRISMARINE:

			case BEETROOT_SEEDS:
			case CARROT:
			case MELON:
			case MELON_SEEDS:
			case NETHER_WARTS:
			case POTATO:
			case GRASS:
			case WHEAT:
			
				multiplier = getRandom().nextInt( fortuneLevel );
				
				switch ( blocks.getType() )
				{
					// limits slightly greater than standard:
					case GLOWSTONE:
						// standard: 4
						if ( multiplier > 5 ) {
							multiplier = 5;
						}
						break;
					case SEA_LANTERN:
						// standard: 5
						if ( multiplier > 6 ) {
							multiplier = 6;
						}
						break;
					case MELON:
						// standard: 9
						if ( multiplier > 11 ) {
							multiplier = 11;
						}

					default:
						break;
				}
				
				// add the multiplier to the count:
				count += multiplier;
				
			default:
				break;
		}
		
		// The count has the final value so set it as the amount:
		blocks.setAmount( count );
	}
	
	
	public Random getRandom() {
		return random;
	}

	public boolean isAreEnabledFeatures() {
		return areEnabledFeatures;
	}

	public boolean isDropItemsIfInventoryIsFull() {
		return dropItemsIfInventoryIsFull;
	}

	public boolean isPlaySoundIfInventoryIsFull() {
		return playSoundIfInventoryIsFull;
	}

	public boolean isHologramIfInventoryIsFull() {
		return hologramIfInventoryIsFull;
	}

	public Configuration getAutoConfigs() {
		return autoConfigs;
	}

	public boolean isAutoPickupEnabled() {
		return autoPickupEnabled;
	}

	public boolean isAutoPickupAllBlocks() {
		return autoPickupAllBlocks;
	}

	public boolean isAutoPickupCobbleStone() {
		return autoPickupCobbleStone;
	}

	public boolean isAutoPickupStone() {
		return autoPickupStone;
	}

	public boolean isAutoPickupGoldOre() {
		return autoPickupGoldOre;
	}

	public boolean isAutoPickupIronOre() {
		return autoPickupIronOre;
	}

	public boolean isAutoPickupCoalOre() {
		return autoPickupCoalOre;
	}

	public boolean isAutoPickupDiamondOre() {
		return autoPickupDiamondOre;
	}

	public boolean isAutoPickupRedstoneOre() {
		return autoPickupRedstoneOre;
	}

	public boolean isAutoPickupEmeraldOre() {
		return autoPickupEmeraldOre;
	}

	public boolean isAutoPickupQuartzOre() {
		return autoPickupQuartzOre;
	}

	public boolean isAutoPickupLapisOre() {
		return autoPickupLapisOre;
	}

	public boolean isAutoPickupSnowBall() {
		return autoPickupSnowBall;
	}

	public boolean isAutoPickupGlowstoneDust() {
		return autoPickupGlowstoneDust;
	}

	public boolean isAutoSmeltEnabled() {
		return autoSmeltEnabled;
	}

	public boolean isAutoSmeltAllBlocks() {
		return autoSmeltAllBlocks;
	}

	public boolean isAutoSmeltGoldOre() {
		return autoSmeltGoldOre;
	}

	public boolean isAutoSmeltIronOre() {
		return autoSmeltIronOre;
	}

	public boolean isAutoBlockEnabled() {
		return autoBlockEnabled;
	}

	public boolean isAutoBlockAllBlocks() {
		return autoBlockAllBlocks;
	}

	public boolean isAutoBlockGoldBlock() {
		return autoBlockGoldBlock;
	}

	public boolean isAutoBlockIronBlock() {
		return autoBlockIronBlock;
	}

	public boolean isAutoBlockCoalBlock() {
		return autoBlockCoalBlock;
	}

	public boolean isAutoBlockDiamondBlock() {
		return autoBlockDiamondBlock;
	}

	public boolean isAutoBlockRedstoneBlock() {
		return autoBlockRedstoneBlock;
	}

	public boolean isAutoBlockEmeraldBlock() {
		return autoBlockEmeraldBlock;
	}

	public boolean isAutoBlockQuartzBlock() {
		return autoBlockQuartzBlock;
	}

	public boolean isAutoBlockPrismarineBlock() {
		return autoBlockPrismarineBlock;
	}

	public boolean isAutoBlockLapisBlock() {
		return autoBlockLapisBlock;
	}

	public boolean isAutoBlockSnowBlock() {
		return autoBlockSnowBlock;
	}

	public boolean isAutoBlockGlowstone() {
		return autoBlockGlowstone;
	}
}

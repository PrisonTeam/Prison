package tech.mcprison.prison.spigot.autoFeatures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.spiget.BluesSemanticVersionData;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends OnBlockBreakEventListener
	implements Listener {
	
	private boolean dropItemsIfInventoryIsFull = false;
	private boolean playSoundIfInventoryIsFull = false;
	private boolean hologramIfInventoryIsFull = false;

	private Random random = new Random();
	
	private Configuration autoConfigs;
	
    public AutoManager() {
        super();
        
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

//    /**
//     * <p>Had to set to a EventPriorty.LOW so other plugins can work with the blocks.
//     * The other plugins were EZBlock & SellAll. This function was canceling the
//     * event after it auto picked it up, so the other plugins were not registering
//     * the blocks as being broken.
//     * </p>
//     * 
//     * @param e
//     */
//    @EventHandler(priority=EventPriority.LOW) 
//    public void onBlockBreak(BlockBreakEvent e) {
//    	
//    	if ( !e.isCancelled() && e.getBlock().getType() != null) {
//    		
//    		// Get the player objects: Spigot and the Prison player:
//    		Player p = e.getPlayer();
//    		// SpigotPlayer player = new SpigotPlayer( p );
//    		
//    		// Validate that the event is happening within a mine since the
//    		// onBlockBreak events here are only valid within the mines:
//    		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
//    		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
//    			PrisonMines mineManager = (PrisonMines) mmOptional.get();
//    			
//    			for ( Mine mine : mineManager.getMines() ) {
//    				SpigotBlock block = new SpigotBlock(e.getBlock());
//    				if ( mine.isInMine( block.getLocation() ) ) {
//    					
//    					applyAutoEvents( e, mine, p );
//    					break;
//    				}
//    			}
//    		}
//    	}
//    }

    /**
     * <p>The optimized logic on how an BlockBreakEvent is handled is within the OnBlockBreakEventListener
     * class and optimizes mine reuse.
     * </p>
     *
     * <p>Had to set to a EventPriorty.LOW so other plugins can work with the blocks.
     * The other plugins were EZBlock & SellAll. This function was canceling the
     * event after it auto picked it up, so the other plugins were not registering
     * the blocks as being broken.
     * </p>
     * 
     * 
     */
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onBlockBreak(BlockBreakEvent e) {
    	super.onBlockBreak(e);
    }
    
    @Override
	public void doAction( Mine mine, BlockBreakEvent e ) {
    	applyAutoEvents( e, mine );
	}
    
    // Prevents players from picking up armorStands (used for holograms), only if they're invisible
	@EventHandler
	public void manipulate(PlayerArmorStandManipulateEvent e) {
		if(!e.getRightClicked().isVisible()) {
			e.setCancelled(true);
		}
	}

	private void applyAutoEvents( BlockBreakEvent e, Mine mine ) {
		// Change this to true to enable these features
		
		Player p = e.getPlayer();

		this.autoConfigs = SpigotPrison.getInstance().getAutoFeaturesConfig();
		boolean areEnabledFeatures = autoConfigs.getBoolean("Options.General.AreEnabledFeatures");
		
		if (areEnabledFeatures) {
			
			this.dropItemsIfInventoryIsFull = autoConfigs.getBoolean("Options.General.DropItemsIfInventoryIsFull");
			this.playSoundIfInventoryIsFull = autoConfigs.getBoolean("Options.General.playSoundIfInventoryIsFull");
			this.hologramIfInventoryIsFull = autoConfigs.getBoolean("Options.General.hologramIfInventoryIsFull");
			
			// AutoPickup booleans from configs
			boolean autoPickupEnabled = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupEnabled");
			boolean autoPickupAllBlocks = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupAllBlocks");
			boolean autoPickupCobbleStone = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupCobbleStone");
			boolean autoPickupStone = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupStone");
			boolean autoPickupGoldOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupGoldOre");
			boolean autoPickupIronOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupIronOre");
			boolean autoPickupCoalOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupCoalOre");
			boolean autoPickupDiamondOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupDiamondOre");
			boolean autoPickupRedstoneOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupRedstoneOre");
			boolean autoPickupEmeraldOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupEmeraldOre");
			boolean autoPickupQuartzOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupQuartzOre");
			boolean autoPickupLapisOre = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupLapisOre");
			boolean autoPickupSnowBall = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupSnowBall");
			boolean autoPickupGlowstoneDust = autoConfigs.getBoolean("Options.AutoPickup.AutoPickupGlowstoneDust");
			
			// AutoSmelt booleans from configs
			boolean autoSmeltEnabled = autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltEnabled");
			boolean autoSmeltAllBlocks = autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltAllBlocks");
			boolean autoSmeltGoldOre = autoSmeltAllBlocks || autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltGoldOre");
			boolean autoSmeltIronOre = autoSmeltAllBlocks || autoConfigs.getBoolean("Options.AutoSmelt.AutoSmeltIronOre");
			
			// AutoBlock booleans from configs
			boolean autoBlockEnabled = autoConfigs.getBoolean("Options.AutoBlock.AutoBlockEnabled");
			boolean autoBlockAllBlocks = autoConfigs.getBoolean("Options.AutoBlock.AutoBlockAllBlocks");
			boolean autoBlockGoldBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockGoldBlock");
			boolean autoBlockIronBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockIronBlock");
			boolean autoBlockCoalBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockCoalBlock");
			boolean autoBlockDiamondBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockDiamondBlock");
			boolean autoBlockRedstoneBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockRedstoneBlock");
			boolean autoBlockEmeraldBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockEmeraldBlock");
			boolean autoBlockQuartzBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockQuartzBlock");
			boolean autoBlockPrismarineBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockPrismarineBlock");
			boolean autoBlockLapisBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockLapisBlock");
			boolean autoBlockSnowBlock = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockSnowBlock");
			boolean autoBlockGlowstone = autoBlockAllBlocks || autoConfigs.getBoolean("Options.AutoBlock.AutoBlockGlowstone");
			

			// Init variables
			Block block = e.getBlock();
			Material brokenBlock = block.getType();
			String blockName = brokenBlock.toString().toLowerCase();
			
			
			// Minecraft's formular for fortune: Should implement it to be fair.
			// https://minecraft.gamepedia.com/Fortune
			
			ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( p );
			
//			int fortuneLevel = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
//			int dropNumber = getDropCount(fortuneLevel);
			
//			// Check if the inventory's full
//			if (p.getInventory().firstEmpty() == -1){
//
//				// Play sound when full
//				if (playSoundIfInventoryIsFull) {
//					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 10F, 1F);
//				}
//
//				String message;
//
//				// Drop items when full
//				if (dropItemsIfInventoryIsFull) {
//
//					message = SpigotPrison.format(autoConfigs.getString("Messages.InventoryIsFullDroppingItems"));
//					
//					p.sendMessage(message);
//
//					hologram(e, message, hologramIfInventoryIsFull);
//
//				} else {  // Lose items when full
//
//					message = SpigotPrison.format(autoConfigs.getString("Messages.InventoryIsFullLosingItems"));
//					
//					p.sendMessage(message);
//
//					hologram(e, message, hologramIfInventoryIsFull);
//
//					// Set the broken block to AIR and cancel the event
//					e.setCancelled(true);
//					e.getBlock().setType(Material.AIR);
//				}
//				return;
//			}
			
			// AutoPickup
			if (autoPickupEnabled) {
				
				@SuppressWarnings( "unused" )
				int count = 0;
				
				switch (blockName) {
					case "cobblestone":
						count += autoPickup( autoPickupCobbleStone, p, itemInHand, e );
						break;
						
					case "stone":
						count += autoPickup( autoPickupStone, p, itemInHand, e );
						break;
						
					case "gold_ore":
						count += autoPickup( autoPickupGoldOre, p, itemInHand, e );
						break;
						
					case "iron_ore":
						count += autoPickup( autoPickupIronOre, p, itemInHand, e );
						break;
						
					case "coal_ore":
						count += autoPickup( autoPickupCoalOre, p, itemInHand, e );
						break;
						
					case "diamond_ore":
						count += autoPickup( autoPickupDiamondOre, p, itemInHand, e );
						break;
						
					case "redstone_ore":
						count += autoPickup( autoPickupRedstoneOre, p, itemInHand, e );
						break;
						
					case "emerald_ore":
						count += autoPickup( autoPickupEmeraldOre, p, itemInHand, e );
						break;
						
					case "quartz_ore":
						count += autoPickup( autoPickupQuartzOre, p, itemInHand, e );
						break;
						
					case "lapis_ore":
						count += autoPickup( autoPickupLapisOre, p, itemInHand, e );
						break;
						
					case "snow_ball":
						count += autoPickup( autoPickupSnowBall, p, itemInHand, e );
						break;
						
					case "glowstone_dust": // works 1.15.2
						count += autoPickup( autoPickupGlowstoneDust, p, itemInHand, e );
						break;
						
					default:
						count += autoPickup( autoPickupAllBlocks, p, itemInHand, e );
						break;
							
				}
				
//				Output.get().logInfo( "In mine: %s  blockName= [%s] %s  drops= %s  count= %s  dropNumber= %s ", 
//						mine.getName(), blockName, Integer.toString( dropNumber ),
//						(e.getBlock().getDrops(itemInHand) != null ? e.getBlock().getDrops(itemInHand).size() : "-=null=-"), 
//						Integer.toString( count ), Integer.toString( dropNumber )
//						);
//				

			}
			
			// AutoSmelt
			if (autoSmeltEnabled){
				
				autoSmelt( autoSmeltGoldOre, Material.GOLD_ORE, Material.GOLD_INGOT, p, block );

				autoSmelt( autoSmeltIronOre, Material.IRON_ORE, Material.IRON_INGOT, p, block );
			}
			
			// AutoBlock
			if (autoBlockEnabled) {
				// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
				// checks within each block type's function call.  So in one pass, could hit on more 
				// than one of these for multiple times too.
				autoBlock( autoBlockGoldBlock, Material.GOLD_INGOT, Material.GOLD_BLOCK, p, block );
				
				autoBlock( autoBlockIronBlock, Material.IRON_INGOT, Material.IRON_BLOCK, p, block );

				autoBlock( autoBlockCoalBlock, Material.COAL, Material.COAL_BLOCK, p, block );
				
				autoBlock( autoBlockDiamondBlock, Material.DIAMOND, Material.DIAMOND_BLOCK, p, block );

				autoBlock( autoBlockRedstoneBlock, Material.REDSTONE, Material.REDSTONE_BLOCK, p, block );
				
				autoBlock( autoBlockEmeraldBlock, Material.EMERALD, Material.EMERALD_BLOCK, p, block );

				autoBlock( autoBlockQuartzBlock, Material.QUARTZ, Material.QUARTZ_BLOCK, 4, p, block );

				autoBlock( autoBlockPrismarineBlock, Material.PRISMARINE_SHARD, Material.PRISMARINE, 4, p, block );

				autoBlock( autoBlockSnowBlock, Material.SNOW_BALL, Material.SNOW_BLOCK, 4, p, block );

				autoBlock( autoBlockGlowstone, Material.GLOWSTONE_DUST, Material.GLOWSTONE, 4, p, block );
				
				autoBlockLapis( autoBlockLapisBlock, p, block );
					
			}
			
		}
	}

	private int autoPickup( boolean autoPickup, Player p, ItemStack itemInHand, BlockBreakEvent e ) {
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

	private void autoSmelt( boolean autoSmelt, Material source, Material destination, Player p, Block block  ) {
		if (autoSmelt && p.getInventory().contains(source)) {
			int count = itemCount(source, p);
			if ( count > 0 ) {
				p.getInventory().removeItem(new ItemStack(source, count));
				dropExtra( p.getInventory().addItem(new ItemStack(destination, count)), p, block );
			}
		}
	}
	private void autoBlock( boolean autoBlock, Material source, Material destination, Player p, Block block  ) {
		autoBlock(autoBlock, source, destination, 9, p, block );
	}
	
	private void autoBlock( boolean autoBlock, Material source, Material destination, int targetCount, Player p, Block block  ) {
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
	private void autoBlockLapis( boolean autoBlock, Player player, Block block  ) {
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

	public Random getRandom() {
		return random;
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

}

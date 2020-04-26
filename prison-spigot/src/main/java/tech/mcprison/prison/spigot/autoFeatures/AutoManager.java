package tech.mcprison.prison.spigot.autoFeatures;

import java.util.Optional;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;


/**
 * @author GABRYCA
 */
public class AutoManager implements Listener {

	private SpigotPrison spigotPrison;
	
	private Random random = new Random();
	
    public AutoManager(SpigotPrison spigotPrison) {
        this.spigotPrison = spigotPrison;
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

    @EventHandler(priority=EventPriority.NORMAL) 
    public void onBlockBreak(BlockBreakEvent e) {
    	
    	if ( !e.isCancelled() && e.getBlock().getType() != null) {
    		
    		// Get the player objects: Spigot and the Prison player:
    		Player p = e.getPlayer();
    		// SpigotPlayer player = new SpigotPlayer( p );
    		
    		// Validate that the event is happening within a mine since the
    		// onBlockBreak events here are only valid within the mines:
    		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
    		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
    			PrisonMines mineManager = (PrisonMines) mmOptional.get();
    			
    			for ( Mine mine : mineManager.getMines() ) {
    				SpigotBlock block = new SpigotBlock(e.getBlock());
    				if ( mine.isInMine( block.getLocation() ) ) {
    					applyAutoEvents( e, p );
    					break;
    				}
    			}
    		}
    	}
    }

	private void applyAutoEvents( BlockBreakEvent e, Player p ) {
		// Change this to true to enable these features
		// For now they aren't tested and will be disabled by default
		// Config
		Configuration configThings = SpigotPrison.getAutoFeaturesConfig();
		boolean areEnabledFeatures = configThings.getBoolean("Options.General.AreEnabledFeatures");
		
		if (areEnabledFeatures) {
			
			boolean dropItemsIfInventoryIsFull = configThings.getBoolean("Options.General.DropItemsIfInventoryIsFull");
			
			// AutoPickup booleans from configs
			boolean autoPickupEnabled = configThings.getBoolean("Options.AutoPickup.AutoPickupEnabled");
			boolean autoPickupAllBlocks = configThings.getBoolean("Options.AutoPickup.AutoPickupAllBlocks");
			boolean autoPickupCobbleStone = configThings.getBoolean("Options.AutoPickup.AutoPickupCobbleStone");
			boolean autoPickupStone = configThings.getBoolean("Options.AutoPickup.AutoPickupStone");
			boolean autoPickupGoldOre = configThings.getBoolean("Options.AutoPickup.AutoPickupGoldOre");
			boolean autoPickupIronOre = configThings.getBoolean("Options.AutoPickup.AutoPickupIronOre");
			boolean autoPickupCoalOre = configThings.getBoolean("Options.AutoPickup.AutoPickupCoalOre");
			boolean autoPickupDiamondOre = configThings.getBoolean("Options.AutoPickup.AutoPickupDiamondOre");
			boolean autoPickupRedstoneOre = configThings.getBoolean("Options.AutoPickup.AutoPickupRedstoneOre");
			boolean autoPickupEmeraldOre = configThings.getBoolean("Options.AutoPickup.AutoPickupEmeraldOre");
			boolean autoPickupQuartzOre = configThings.getBoolean("Options.AutoPickup.AutoPickupQuartzOre");
			boolean autoPickupLapisOre = configThings.getBoolean("Options.AutoPickup.AutoPickupLapisOre");
			
			// AutoSmelt booleans from configs
			boolean autoSmeltEnabled = configThings.getBoolean("Options.AutoSmelt.AutoSmeltEnabled");
			boolean autoSmeltGoldOre = configThings.getBoolean("Options.AutoSmelt.AutoSmeltGoldOre");
			boolean autoSmeltIronOre = configThings.getBoolean("Options.AutoSmelt.AutoSmeltIronOre");
			
			// AutoBlock booleans from configs
			boolean autoBlockEnabled = configThings.getBoolean("Options.AutoBlock.AutoBlockEnabled");
			boolean autoBlockAllBlocks = configThings.getBoolean("Options.AutoBlock.AutoBlockAllBlocks");
			boolean autoBlockGoldBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockGoldBlock");
			boolean autoBlockIronBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockIronBlock");
			boolean autoBlockCoalBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockCoalBlock");
			boolean autoBlockDiamondBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockDiamondBlock");
			boolean autoBlockRedstoneBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockRedstoneBlock");
			boolean autoBlockEmeraldBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockEmeraldBlock");
			boolean autoBlockQuartzBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockQuartzBlock");
			boolean autoBlockLapisBlock = autoBlockAllBlocks || configThings.getBoolean("Options.AutoBlock.AutoBlockLapisBlock");
			

			// Init variables
			Material brokenBlock = e.getBlock().getType();
			String blockName = brokenBlock.toString().toLowerCase();
			
			ItemStack itemInHand = spigotPrison.getCompatibility().getItemInMainHand( p );
			int fortuneLevel = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			
//			int fortuneLevel = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
			
			int dropNumber = getDropCount(fortuneLevel);
			
			// Check if the inventory's full
			if (p.getInventory().firstEmpty() == -1){
				
				// Drop items when full
				if (dropItemsIfInventoryIsFull) {
					
					p.sendMessage(SpigotPrison.format(configThings.getString("Messages.InventoryIsFullDroppingItems")));
					
				} else if (!(dropItemsIfInventoryIsFull)){ // Lose items when full
					
					p.sendMessage(SpigotPrison.format(configThings.getString("Messages.InventoryIsFullLosingItems")));
					
					// Set the broken block to AIR and cancel the event
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					
				}
				return;
			}
			
			// AutoPickup
			if (autoPickupEnabled) {
				
				switch (blockName) {
					case "cobblestone":
						autoPickup( autoPickupCobbleStone, dropNumber, p, e );
						break;
						
					case "stone":
						autoPickup( autoPickupStone, dropNumber, p, e );
						break;
						
					case "gold_ore":
						autoPickup( autoPickupGoldOre, dropNumber, p, e );
						break;
						
					case "iron_ore":
						autoPickup( autoPickupIronOre, dropNumber, p, e );
						break;
						
					case "coal_ore":
						autoPickup( autoPickupCoalOre, dropNumber, p, e );
						break;
						
					case "diamond_ore":
						autoPickup( autoPickupDiamondOre, dropNumber, p, e );
						break;
						
					case "redstone_ore":
						autoPickup( autoPickupRedstoneOre, dropNumber, p, e );
						break;
						
					case "emerald_ore":
						autoPickup( autoPickupEmeraldOre, dropNumber, p, e );
						break;
						
					case "quartz_ore":
						autoPickup( autoPickupQuartzOre, dropNumber, p, e );
						break;
						
					case "lapis_ore":
						autoPickup( autoPickupLapisOre, dropNumber, p, e );
						break;
						
					default:
						autoPickup( autoPickupAllBlocks, dropNumber, p, e );
						break;
							
				}
			}
			
			// AutoSmelt
			if (autoSmeltEnabled){
				
				autoSmelt( autoSmeltGoldOre, Material.GOLD_ORE, Material.GOLD_INGOT, p );

				autoSmelt( autoSmeltIronOre, Material.IRON_ORE, Material.IRON_ORE, p );
			}
			
			// AutoBlock
			if (autoBlockEnabled) {
				// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
				// checks within each block type's function call.  So in one pass, could hit on more 
				// than one of these for multiple times too.
				autoBlock( autoBlockGoldBlock, Material.GOLD_INGOT, Material.GOLD_BLOCK, p );
				
				autoBlock( autoBlockIronBlock, Material.IRON_INGOT, Material.IRON_BLOCK, p );

				autoBlock( autoBlockCoalBlock, Material.COAL, Material.COAL_BLOCK, p );
				
				autoBlock( autoBlockDiamondBlock, Material.DIAMOND, Material.DIAMOND_BLOCK, p );

				autoBlock( autoBlockRedstoneBlock, Material.REDSTONE, Material.REDSTONE_BLOCK, p );
				
				autoBlock( autoBlockEmeraldBlock, Material.EMERALD, Material.EMERALD_BLOCK, p );

				autoBlock( autoBlockQuartzBlock, Material.QUARTZ, Material.QUARTZ_BLOCK, p );
				
				autoBlockLapis( autoBlockLapisBlock, p );
					
			}
			
		}
	}

	private void autoPickup( boolean autoPickup, int dropNumber, Player p, BlockBreakEvent e ) {
		if (autoPickup && e.getBlock().getDrops() != null && e.getBlock().getDrops().size() > 0 ) {
			
			// Add the item to the inventory
			for (int i = 0; i < dropNumber; i++) {
				p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
			}
			
			// Set the broken block to AIR and cancel the event
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
		}
	}

	private void autoSmelt( boolean autoSmelt, Material source, Material destination, Player p ) {
		if (autoSmelt && p.getInventory().contains(source)) {
			while (p.getInventory().contains(source)) {
				p.getInventory().removeItem(new ItemStack(source, 1));
				p.getInventory().addItem(new ItemStack(destination, 1));
			}
		}
	}
	
	private void autoBlock( boolean autoBlock, Material source, Material destination, Player p ) {
		if ( autoBlock ) {
			while (p.getInventory().contains(source, 9) ) {
				p.getInventory().removeItem(new ItemStack(source, 9));
				p.getInventory().addItem(new ItemStack(destination));
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
	private void autoBlockLapis( boolean autoBlock, Player player ) {
		if ( autoBlock ) {
			
			// ink_sack = 351:4 
			ItemStack lapisItemStack = new ItemStack(Material.INK_SACK, 9, (short) 4);
			
			while (player.getInventory().contains(lapisItemStack) ) {
				player.getInventory().removeItem(lapisItemStack);
				player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK));
			}
		}
	}
	
	public Random getRandom() {
		return random;
	}

}

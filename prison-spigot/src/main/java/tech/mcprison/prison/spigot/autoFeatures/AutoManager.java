package tech.mcprison.prison.spigot.autoFeatures;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import org.bukkit.configuration.Configuration;

import java.util.Random;


/**
 * @author GABRYCA
 */
public class AutoManager implements Listener {

    public int getDropCount(int fortuneLevel, Random random) {
        int j = random.nextInt(fortuneLevel + 2) - 1;

        if (j < 0) {
            j = 0;
        }

        return (j + 1);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        // Change this to true to enable these features
        // For now they aren't tested and will be disabled by default
        // Config
        Configuration configThings = SpigotPrison.getAutoFeaturesConfig();
        boolean AreEnabledFeatures = configThings.getBoolean("Options.General.AreEnabledFeatures");
        boolean DropItemsIfInventoryIsFull = configThings.getBoolean("Options.General.DropItemsIfInventoryIsFull");

        if (AreEnabledFeatures) {

            // Get the player
            Player p = e.getPlayer();

            // AutoPickup booleans from configs
            boolean AutoPickupEnabled = configThings.getBoolean("Options.AutoPickup.AutoPickupEnabled");
            boolean AutoPickupAllBlocks = configThings.getBoolean("Options.AutoPickup.AutoPickupAllBlocks");
            boolean AutoPickupGoldOre = configThings.getBoolean("Options.AutoPickup.AutoPickupGoldOre");
            boolean AutoPickupIronOre = configThings.getBoolean("Options.AutoPickup.AutoPickupIronOre");
            boolean AutoPickupCoalOre = configThings.getBoolean("Options.AutoPickup.AutoPickupCoalOre");
            boolean AutoPickupDiamondOre = configThings.getBoolean("Options.AutoPickup.AutoPickupDiamondOre");
            boolean AutoPickupRedstoneOre = configThings.getBoolean("Options.AutoPickup.AutoPickupRedstoneOre");
            boolean AutoPickupEmeraldOre = configThings.getBoolean("Options.AutoPickup.AutoPickupEmeraldOre");
            boolean AutoPickupQuartzOre = configThings.getBoolean("Options.AutoPickup.AutoPickupQuartzOre");
            boolean AutoPickupLapisOre = configThings.getBoolean("Options.AutoPickup.AutoPickupLapisOre");

            // AutoSmelt booleans from configs
            boolean AutoSmeltEnabled = configThings.getBoolean("Options.AutoSmelt.AutoSmeltEnabled");
            boolean AutoSmeltGoldOre = configThings.getBoolean("Options.AutoSmelt.AutoSmeltGoldOre");
            boolean AutoSmeltIronOre = configThings.getBoolean("Options.AutoSmelt.AutoSmeltIronOre");

            // AutoBlock booleans from configs
            boolean AutoBlockEnabled = configThings.getBoolean("Options.AutoBlock.AutoBlockEnabled");
            boolean AutoBlockGoldBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockGoldBlock");
            boolean AutoBlockIronBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockIronBlock");
            boolean AutoBlockCoalBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockCoalBlock");
            boolean AutoBlockDiamondBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockDiamondBlock");
            boolean AutoBlockRedstoneBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockRedstoneBlock");
            boolean AutoBlockEmeraldBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockEmeraldBlock");
            boolean AutoBlockQuartzBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockQuartzBlock");
            boolean AutoBlockLapisBlock = configThings.getBoolean("Options.AutoBlock.AutoBlockLapisBlock");

            // If the block is null somehow, return
            if (e.getBlock().getType() == null) {
                return;
            }

            // Init variables
            Material brokenBlock = e.getBlock().getType();
            String blockName = brokenBlock.toString().toLowerCase();
            int fortuneLevel = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            int dropNumber = getDropCount(fortuneLevel, new Random());

            // Check if the inventory's full
            if (p.getInventory().firstEmpty() == -1){

                // Drop items when full
                if (DropItemsIfInventoryIsFull) {

                    p.sendMessage(SpigotPrison.format(configThings.getString("Messages.InventoryIsFullDroppingItems")));

                } else if (!(DropItemsIfInventoryIsFull)){ // Lose items when full

                    p.sendMessage(SpigotPrison.format(configThings.getString("Messages.InventoryIsFullLosingItems")));

                    // Set the broken block to AIR and cancel the event
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);

                }
                return;
            }

            // AutoPickup
            if (AutoPickupEnabled) {

                switch (blockName) {
                    case "gold_ore":

                        if (AutoPickupGoldOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }

                        break;
                    case "iron_ore":

                        if (AutoPickupIronOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }

                        break;
                    case "coal_ore":

                        if (AutoPickupCoalOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }

                        break;
                    case "diamond_ore":

                        if (AutoPickupDiamondOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }

                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }
                        break;

                    case "redstone_ore":
                        if (AutoPickupRedstoneOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }

                        break;
                    case "emerald_ore":
                        if (AutoPickupEmeraldOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }
                        break;

                    case "quartz_ore":
                        if (AutoPickupQuartzOre) {

                            // Add the item to the inventory
                            for (int i = 0; i < dropNumber; i++) {
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }
                        break;
                    default:

                        if (AutoPickupAllBlocks) {
                            // Check if drops aren't null
                            if (!(e.getBlock().getDrops() == null)) {

                                // Add the items to the inventory
                                for (int i = 0; i < dropNumber; i++) {
                                    p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                                }

                                // Set the broken block to AIR and cancel the event
                                e.setCancelled(true);
                                e.getBlock().setType(Material.AIR);
                            }
                        }
                        break;
                }
            }

            // AutoSmelt
            if (AutoSmeltEnabled){
                if (p.getInventory().contains(Material.GOLD_ORE)){
                    if (AutoSmeltGoldOre) {
                        while (p.getInventory().contains(Material.GOLD_ORE)) {
                            p.getInventory().removeItem(new ItemStack(Material.GOLD_ORE, 1));
                            p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
                    }
                }
                } else if (p.getInventory().contains(Material.IRON_ORE)){
                    if (AutoSmeltIronOre){
                        while (p.getInventory().contains(Material.IRON_ORE)) {
                            p.getInventory().removeItem(new ItemStack(Material.IRON_ORE, 1));
                            p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
                        }
                    }
                }
            }

            // AutoBlock
            if (AutoBlockEnabled) {
                if (p.getInventory().contains(Material.GOLD_INGOT, 9)) {

                    if (AutoBlockGoldBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 9));
                        p.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK));
                    }

                } else if (p.getInventory().contains(Material.IRON_INGOT, 9)) {


                    if (AutoBlockIronBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 9));
                        p.getInventory().addItem(new ItemStack(Material.IRON_BLOCK));
                    }

                } else if (p.getInventory().contains(Material.COAL, 9)) {

                    if (AutoBlockCoalBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.COAL, 9));
                        p.getInventory().addItem(new ItemStack(Material.COAL_BLOCK));
                    }

                } else if (p.getInventory().contains(Material.DIAMOND, 9)) {

                    if (AutoBlockDiamondBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 9));
                        p.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK));
                    }

                } else if (p.getInventory().contains(Material.REDSTONE, 9)) {

                    if (AutoBlockRedstoneBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.REDSTONE, 9));
                        p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK));
                    }

                } else if (p.getInventory().contains(Material.EMERALD, 9)) {

                    if (AutoBlockEmeraldBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.EMERALD, 9));
                        p.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK));
                    }

                } else if (p.getInventory().contains(Material.QUARTZ, 9)) {

                    if (AutoBlockQuartzBlock) {
                        p.getInventory().removeItem(new ItemStack(Material.QUARTZ, 9));
                        p.getInventory().addItem(new ItemStack(Material.QUARTZ_BLOCK));
                    }

                }
            }

        }
    }

}

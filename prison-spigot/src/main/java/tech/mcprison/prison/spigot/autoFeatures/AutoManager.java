package tech.mcprison.prison.spigot.autoFeatures;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class AutoManager implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        // Change this to true to enable these features
        // For now they aren't tested and will be disabled by default
        // Maybe we'll add them in a config
        boolean AreEnabledFeatures = true;
        boolean DropItemsIfInventoryIsFull = false;

        if (AreEnabledFeatures) {

            // Get the player
            Player p = e.getPlayer();

            // AutoPickup booleans
            boolean AutoPickupEnabled = true;
            boolean AutoPickupAllBlocks = true;
            boolean AutoPickupGoldOre = true;
            boolean AutoPickupIronOre = true;
            boolean AutoPickupCoalOre = true;

            // AutoSmelt booleans
            boolean AutoSmeltEnabled = true;
            boolean AutoSmeltGoldOre = true;
            boolean AutoSmeltIronOre = true;

            // AutoBlock booleans
            boolean AutoBlockEnabled = true;
            boolean AutoBlockGoldBlock = true;
            boolean AutoBlockIronBlock = true;
            boolean AutoBlockCoalBlock = true;
            boolean AutoBlockDiamondBlock = true;
            boolean AutoBlockRedstoneBlock = true;
            boolean AutoBlockEmeraldBlock = true;
            boolean AutoBlockQuartzBlock = true;

            // If the block is null somehow, return
            if (e.getBlock().getType() == null) {
                return;
            }

            // Init variables
            Material brokenBlock = e.getBlock().getType();
            String blockName = brokenBlock.toString().toLowerCase();

            // Check if the inventory's full
            if (p.getInventory().firstEmpty() == -1){

                // Drop items when full
                if (DropItemsIfInventoryIsFull) {

                    p.sendMessage(SpigotPrison.format("&cWARNING! Your inventory's full and you're dropping items!"));

                } else if (!(DropItemsIfInventoryIsFull)){ // Lose items when full

                    p.sendMessage(SpigotPrison.format("&CWARNING! Your inventory's full and you're losing items!"));

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
                            p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));

                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }

                        break;
                    case "iron_ore":

                        if (AutoPickupIronOre) {

                            // Add the item to the inventory
                            p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));

                            // Set the broken block to AIR and cancel the event
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                        }

                        break;
                    case "coal_ore":

                        if (AutoPickupCoalOre) {

                            // Add the item to the inventory
                            p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));

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
                                p.getInventory().addItem(e.getBlock().getDrops().toArray(new ItemStack[0]));
                            }
                        }

                        break;
                }
            }

            // AutoSmelt
            if (AutoSmeltEnabled){
                if (AutoSmeltGoldOre){
                    while (p.getInventory().contains(Material.GOLD_ORE)){
                        p.getInventory().removeItem(new ItemStack(Material.GOLD_ORE, 1));
                        p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT,1));
                    }
                } else if (AutoSmeltIronOre){
                    while (p.getInventory().contains(Material.IRON_ORE)){
                        p.getInventory().removeItem(new ItemStack(Material.IRON_ORE, 1));
                        p.getInventory().addItem(new ItemStack(Material.IRON_INGOT,1));
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

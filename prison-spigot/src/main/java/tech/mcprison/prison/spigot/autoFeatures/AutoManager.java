package tech.mcprison.prison.spigot.autoFeatures;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author GABRYCA
 */
public class AutoManager implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        // Change this to true to enable these features
        // For now they aren't tested and will be disabled by default
        // Maybe we'll add them in a config
        boolean areEnabledFeatures = false;

        if (areEnabledFeatures) {
            // Get the player
            Player p = e.getPlayer();

            // If the block is null somehow, return
            if (e.getBlock().getType() == null) {
                return;
            }

            // Init variables
            Material brokenBlock = e.getBlock().getType();
            String blockName = brokenBlock.toString().toLowerCase();

            // AutoSmelt and autoPickup
            switch (blockName) {
                case "gold_ore":
                    // Add the item to the inventory
                    p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));

                    // Set the broken block to AIR and cancel the event
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    break;
                case "iron_ore":
                    // Add the item to the inventory
                    p.getInventory().addItem(new ItemStack(Material.IRON_INGOT));

                    // Set the broken block to AIR and cancel the event
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    break;
                case "coal_ore":
                    // Add the item to the inventory
                    p.getInventory().addItem(new ItemStack(Material.COAL));

                    // Set the broken block to AIR and cancel the event
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    break;
                default:

                    // Check if drops aren't null
                    if (!(e.getBlock().getDrops() == null)) {

                        // Add the dropped items in a collection
                        ItemStack[] drops = e.getBlock().getDrops().toArray(new ItemStack[0]);

                        // Add the items to the inventory
                        p.getInventory().addItem(drops);
                    }

                    break;
            }

            // AutoBlock
            if (p.getInventory().contains(Material.GOLD_INGOT, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 9));
                p.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK));

            } else if (p.getInventory().contains(Material.IRON_INGOT, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 9));
                p.getInventory().addItem(new ItemStack(Material.IRON_BLOCK));

            } else if (p.getInventory().contains(Material.COAL, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.COAL, 9));
                p.getInventory().addItem(new ItemStack(Material.COAL_BLOCK));

            } else if (p.getInventory().contains(Material.DIAMOND, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.DIAMOND, 9));
                p.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK));

            } else if (p.getInventory().contains(Material.REDSTONE, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.REDSTONE, 9));
                p.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK));

            } else if (p.getInventory().contains(Material.EMERALD, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.EMERALD, 9));
                p.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK));

            } else if (p.getInventory().contains(Material.QUARTZ, 9)) {

                p.getInventory().removeItem(new ItemStack(Material.QUARTZ, 9));
                p.getInventory().addItem(new ItemStack(Material.QUARTZ_BLOCK));

            }

        }
    }

}

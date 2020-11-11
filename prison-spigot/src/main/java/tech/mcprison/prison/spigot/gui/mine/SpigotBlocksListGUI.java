package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;
import tech.mcprison.prison.util.BlockType;

import java.util.List;

public class SpigotBlocksListGUI extends SpigotGUIComponents {

    private final Player p;
    private final String mineName;
    private int counter;

    private final Configuration messages = messages();

    public SpigotBlocksListGUI(Player p, String mineName, int counter){
        this.p = p;
        this.mineName = mineName;
        this.counter = counter;
    }

    public void open(){

        // Get the dimensions and if needed increases them
        int dimension = 54, inventorySlot = 0, secondCounter = 0;

        // Create the inventory
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> BlocksList"));

        // Lore of block setup
        List<String> blockLoreSetup = createLore(
                messages.getString("Gui.Lore.ClickToStartBlockSetup")
        );

        for (BlockType block : BlockType.values()){

            if (!block.getXMaterialName().equalsIgnoreCase("ignore")) {
                if (secondCounter >= counter) {
                    ItemStack button = createButton(Material.valueOf(block.getXMaterialName()), 1, blockLoreSetup, SpigotPrison.format("&a" + block.getXMaterialName().toUpperCase()));
                    inv.setItem(inventorySlot, button);
                }
            }

            secondCounter++;
            counter++;
            inventorySlot++;

            if (counter >= counter + 44){

                List<String> nextPageLore = createLore(
                        messages.getString("Gui.Lore.ClickToNextPage")
                );

                ItemStack nextPageButton = createButton(Material.BOOK, 1, nextPageLore, "Next " + mineName + " " + counter);
                inv.setItem(53, nextPageButton);

            }

        }

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }
}


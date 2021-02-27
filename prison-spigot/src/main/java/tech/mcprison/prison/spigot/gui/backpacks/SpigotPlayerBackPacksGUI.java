package tech.mcprison.prison.spigot.gui.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackPacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.io.File;
import java.util.Set;

public class SpigotPlayerBackPacksGUI extends SpigotGUIComponents {

    private final Player p;
    private final File backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
    private final FileConfiguration backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);
    private final BackPacksUtil backPacksUtil = BackPacksUtil.get();

    public SpigotPlayerBackPacksGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create the inventory
        int dimension = 54;
        Inventory inv = Bukkit.createInventory(p, dimension, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        if (guiBuilder(inv)) return;

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder(Inventory inv) {
        try {
            buttonsSetup(inv);
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Inventory inv) {

        // Get the Items config section
        Set<String> slots;
        try {
             slots = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {
                ItemStack finalItem = backPacksDataConfig.getItemStack("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEMSTACK");
                if (finalItem != null) {
                    inv.setItem(Integer.parseInt(slot), finalItem);
                }
            }
        }
    }
}

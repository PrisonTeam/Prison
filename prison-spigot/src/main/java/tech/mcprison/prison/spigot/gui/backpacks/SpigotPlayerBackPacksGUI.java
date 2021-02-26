package tech.mcprison.prison.spigot.gui.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpigotPlayerBackPacksGUI extends SpigotGUIComponents {

    private final Player p;
    private File backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
    private FileConfiguration backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);

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

        // CONFIG FORMAT:
        // Items:
        //     0: <--- Slot
        //       ITEM_ID: CHEST
        //       AMOUNT: amount

        // Get the Items config section
        Set<String> slots;
        try {
             slots = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items").getKeys(false);
        } catch (NullPointerException ex){
            return;
        }
        if (slots.size() != 0) {
            for (String slot : slots) {

                Material material = SpigotUtil.getXMaterial(backPacksDataConfig.getString("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ITEM_ID")).parseMaterial();
                int amount = Integer.parseInt(backPacksDataConfig.getString("Inventories. " + p.getUniqueId() +   ".Items." + slot  + ".AMOUNT"));
                String displayName = backPacksDataConfig.getString("Inventories. " + p.getUniqueId() + ".Items." + slot + ".DISPLAYNAME");
                List<String> itemLore = createLore();

                Set<String> lores = null;
                try {
                    lores = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items." + slot + ".LORES").getKeys(false);
                } catch (NullPointerException ignored){}
                if (lores != null && lores.size() != 0) {
                    for (String loreNumber : lores) {
                        itemLore.add("Inventories. " + p.getUniqueId() + ".Items." + slot + ".Lores." + loreNumber + ".LORE");
                    }
                }

                ItemStack finalItem = null;
                if (material != null) {
                    finalItem = new ItemStack(material, amount);
                }

                Set<String> enchantments = null;
                try {
                    enchantments = backPacksDataConfig.getConfigurationSection("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ENCHANTMENTS").getKeys(false);
                } catch (NullPointerException ignored){}
                if (enchantments != null){
                    for (String enchantNumber : enchantments){
                        String enchant = backPacksDataConfig.getString("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ENCHANTMENTS." + enchantNumber + ".ENCHANT");
                        int level = Integer.parseInt(backPacksDataConfig.getString("Inventories. " + p.getUniqueId() + ".Items." + slot + ".ENCHANTMENTS." + enchantNumber + ".LEVEL"));
                        if (enchant != null) {
                            NamespacedKey keyEnchant = NamespacedKey.minecraft(enchant);
                            Enchantment enchantment = Enchantment.getByKey(keyEnchant);
                            if (enchantment != null) {
                                try {
                                    finalItem.addEnchantment(enchantment, level);
                                } catch (NullPointerException ignored){}
                            }
                        }
                    }
                }

                if ((lores == null || lores.size() == 0) && displayName.equalsIgnoreCase("null")){
                    if (finalItem != null) {
                        inv.setItem(Integer.parseInt(slot), finalItem);
                    }
                } else {
                    finalItem = createButton(finalItem, itemLore, displayName);
                    inv.setItem(Integer.parseInt(slot), finalItem);
                }
            }
        }
    }
}

package tech.mcprison.prison.spigot.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackPacksListeners implements Listener {

    private Configuration backPacksConfig = SpigotPrison.getInstance().getBackPacksConfig();
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private File backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
    private FileConfiguration backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);

    @EventHandler
    public void createInventoryFirst(PlayerJoinEvent e){

        Player p = e.getPlayer();

        if (backPacksConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null){
            try {
                backPacksFile = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backPacksData.yml");
                backPacksDataConfig = YamlConfiguration.loadConfiguration(backPacksFile);
                backPacksDataConfig.set("Inventories." + p.getUniqueId() + ".PlayerName", p.getName());
                backPacksDataConfig.save(backPacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }
        }

        // TODO ERROR HERE CANNOT FIX.
        // Bukkit.getScheduler().runTaskLater(SpigotPrison.getInstance(), () -> backPackItem(p), 20);
    }

    @EventHandler
    public void onDeadBackPack(PlayerDeathEvent e){

        if (getBoolean(backPacksConfig.getString("Options.BackPack_Lose_Items_On_Death"))) {

            Player p = e.getEntity();

            try {
                backPacksDataConfig.set("Inventories. " + p.getUniqueId() + ".Items", null);
                backPacksDataConfig.save(backPacksFile);
            } catch (IOException ex){
                ex.printStackTrace();
                return;
            }
        }

    }

    private void backPackItem(Player p) {

        if (!getBoolean(backPacksConfig.getString("Options.Back_Pack_GUI_Opener_Item"))) {
            return;
        }

            boolean playerHasItemBackPack = false;

        Inventory inv = p.getInventory();

        for (ItemStack item : inv.getContents()) {
            if (item.getItemMeta() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(backPacksConfig.getString("Optionns.BackPack_Item_Title"))) {
                playerHasItemBackPack = true;
            }
        }

        if (!playerHasItemBackPack){

            ItemStack item;

            List<String> itemLore = createLore(
                    messages.getString("Lore.ClickToOpenBackpack")
            );

            item = createButton(SpigotUtil.getXMaterial(backPacksConfig.getString("Options.BackPack_Item")).parseItem(), itemLore, backPacksConfig.getString("Options.BackPack_Item_Title"));

            if (item != null) {
                inv.addItem(item);
            }
        }
    }

    /**
     * Create a Lore for an Item in the GUI.
     *
     * @param lores
     * */
    private List<String> createLore( String... lores ) {
        List<String> results = new ArrayList<>();
        for ( String lore : lores ) {
            results.add( SpigotPrison.format(lore) );
        }
        return results;
    }

    /**
     * Create a button for the GUI using ItemStack.
     *
     * @param item
     * @param lore
     * @param display
     * */
    private ItemStack createButton(ItemStack item, List<String> lore, String display) {

        if (item == null){
            item = XMaterial.BARRIER.parseItem();
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null){
            meta = XMaterial.BARRIER.parseItem().getItemMeta();
        }

        return getItemStack(item, lore, display, meta);
    }

    /**
     * Get ItemStack of an Item.
     *
     * @param item
     * @param lore
     * @param display
     * @param meta
     * */
    private ItemStack getItemStack(ItemStack item, List<String> lore, String display, ItemMeta meta) {
        if (meta != null) {
            meta.setDisplayName(SpigotPrison.format(display));
            try {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (NoClassDefFoundError ignored){}
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){

        if (string == null){
            return false;
        }

        if (string.equalsIgnoreCase("true")){
            return true;
        } else if (string.equalsIgnoreCase("false")){
            return false;
        }

        return false;
    }

}



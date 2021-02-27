package tech.mcprison.prison.spigot.gui;

import java.util.ArrayList;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * @author rbluer RoyalBlueRanger
 * @author GABRYCA
 */
public abstract class SpigotGUIComponents {

    public Configuration messages = getMessages();
    public Configuration guiConfig = getGuiConfig();
    public Configuration sellAllConfig = getSellAll();

    /**
     * Create a button for the GUI using Material.
     *
     * @param id
     * @param amount
     * @param lore
     * @param display
     * */
    protected ItemStack createButton(Material id, int amount, List<String> lore, String display) {

        if (id == null){
            id = XMaterial.BARRIER.parseMaterial();
        }

        ItemStack item = new ItemStack(id, amount);
        ItemMeta meta = item.getItemMeta();
        return getItemStack(item, lore, display, meta);
    }

    /**
     * Create a button for the GUI using ItemStack.
     *
     * @param item
     * @param lore
     * @param display
     * */
    protected ItemStack createButton(ItemStack item, List<String> lore, String display) {

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
     * Create a Lore for an Item in the GUI.
     *
     * @param lores
     * */
    protected List<String> createLore( String... lores ) {
        List<String> results = new ArrayList<>();
        for ( String lore : lores ) {
            results.add( SpigotPrison.format(lore) );
        }
        return results;
    }

    /**
     * Check if the Ranks module's enabled.
     *
     * @param p
     * */
    protected boolean checkRanks(Player p){
        Module module = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME ).orElse( null );
        if(!(module instanceof PrisonRanks)){
            Output.get().sendWarn(new SpigotPlayer(p), SpigotPrison.format("&c[ERROR] The GUI can't open because the &3Ranks module &cisn't loaded"));
            p.closeInventory();
        }
        return module instanceof PrisonRanks;
    }

    /**
     * Get Messages config.
     * */
    protected static Configuration getMessages(){
        return SpigotPrison.getInstance().getMessagesConfig();
    }

    /**
     * Get SellAll config.
     * */
    protected static Configuration getSellAll(){
        return SpigotPrison.getInstance().getSellAllConfig();
    }

    /**
     * Get GUI config.
     * */
    protected static Configuration getGuiConfig(){
        return SpigotPrison.getInstance().getGuiConfig();
    }

    /**
     * Get autoFeatures Config.
     * */
    public static AutoFeaturesFileConfig afConfig() {
        return SpigotPrison.getInstance().getAutoFeatures().getAutoFeaturesConfig();
    }

    /**
     * Open and register GUIs.
     *
     * @param p
     * @param inv
     * */
    protected void openGUI(Player p, Inventory inv){

        // Open the inventory
        p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }
}

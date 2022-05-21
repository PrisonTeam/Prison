package tech.mcprison.prison.spigot.gui.guiutility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.sellall.messages.SpigotVariousGuiMessages;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.util.Text;

/**
 * @author rbluer RoyalBlueRanger
 * @author GABRYCA
 */
public abstract class SpigotGUIComponents
	extends SpigotVariousGuiMessages {

    public static MessagesConfig messages = getMessages();
    public static Configuration guiConfig = getGuiConfig();
    public static Configuration sellAllConfig = getSellAll();

//    /**
//     * Bug: Cannot correctly create a button with Material variants with spigot versions less than 1.13:
//     * 
//     * Create a button for the GUI using Material.
//     *
//     * @param id
//     * @param amount
//     * @param lore
//     * @param display
//     * */
//    protected ItemStack createButton(Material id, int amount, List<String> lore, String display) {
//
//        if (id == null){
//            id = XMaterial.BARRIER.parseMaterial();
//        }
//
//        ItemStack item = new ItemStack(id, amount);
//        ItemMeta meta = item.getItemMeta();
//        return getItemStack(item, lore, SpigotPrison.format(display), meta);
//    }

    protected ItemStack createButton(ItemStack item, List<String> lore, String display) {
    	return createButton( item, 1, lore, display );
    }
    /**
     * Create a button for the GUI using ItemStack.
     *
     * @param item
     * @param lore
     * @param display
     * */
    protected ItemStack createButton(ItemStack item, int amount, List<String> lore, String display) {

        if (item == null){
            item = XMaterial.BARRIER.parseItem();
        }
    	
        item.setAmount( amount );
        
    	ItemMeta meta = item.getItemMeta();

        if (meta == null){
            meta = XMaterial.BARRIER.parseItem().getItemMeta();
        }

        return getItemStack(item, lore, display, meta);
    }

    /**
     * Get ItemStack of an Item.
     * 
     * Lore should not be converted from the Amp color codes, since they will all be
     * converted within this function.
     *
     * @param item
     * @param lore
     * @param display
     * @param meta
     * */
    private ItemStack getItemStack(ItemStack item, List<String> lore, String display, ItemMeta meta) {
        if (meta != null) {
            meta.setDisplayName( Text.translateAmpColorCodes(display) );
            try {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (NoClassDefFoundError ignored){}
            
            if ( lore != null ) {
            	
            	// Go through all of the lore and convert the & color codes to the native color codes:
            	for ( int i = 0; i < lore.size(); i++ ) {
            		String loreLine = lore.get(i);
            		String loreColor = Text.translateAmpColorCodes(loreLine);
            		if ( !loreLine.equals(loreColor) ) {
            			lore.set(i, loreColor);
            		}
            	}
            	
            	meta.setLore(lore);
            }
            
            meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
            meta.addItemFlags( ItemFlag.HIDE_ATTRIBUTES );
            meta.addItemFlags( ItemFlag.HIDE_POTION_EFFECTS );
            
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
            results.add( Text.translateAmpColorCodes(lore) );
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
            Output.get().sendWarn(new SpigotPlayer(p), "&c[ERROR] The GUI can't open because the &3Ranks module &cisn't loaded");
            p.closeInventory();
        }
        return module instanceof PrisonRanks;
    }

    /**
     * Get new Messages config.
     * */
    protected static MessagesConfig getmessages(){
        return SpigotPrison.getInstance().getMessagesConfig();
    }

    /**
     * Get Messages config.
     * */
    protected static MessagesConfig getMessages(){
        return SpigotPrison.getInstance().getMessagesConfig();
    }

    /**
     * Get SellAll config.
     * */
    protected static Configuration getSellAll(){
        return SpigotPrison.getInstance().updateSellAllConfig();
    }

    /**
     * Get GUI config.
     * */
    protected static Configuration getGuiConfig(){
        return SpigotPrison.getInstance().getGuiConfig();
    }

    // Investigating on NPEs here.
    /**
     * Get autoFeatures Config.
     * */
    public static AutoFeaturesFileConfig afConfig() {
        if (AutoFeaturesWrapper.getInstance() == null){
            return null;
        }
        try{
            AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig();
        } catch (NullPointerException ex){
            return null;
        }
        return AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig();
    }

    /**
     * Reload messages config for GUIs.
     * */
    public static void updateMessages(){
        MessagesConfig.get().reload();
        messages = MessagesConfig.get();
    }

    /**
     * Reload sellall config for GUIs.
     * */
    public static void updateSellAllConfig(){
        SellAllUtil util = SpigotPrison.getInstance().getSellAllUtil();
        util.updateConfig();
        sellAllConfig = util.sellAllConfig;
    }

    public static void updateGUIConfig(){
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");
        guiConfig = YamlConfiguration.loadConfiguration(file);
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

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){
        return string != null && string.equalsIgnoreCase("true");
    }
}

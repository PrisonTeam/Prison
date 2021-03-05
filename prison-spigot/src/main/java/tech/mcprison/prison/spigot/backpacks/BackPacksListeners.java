package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;

import java.util.ArrayList;
import java.util.List;

public class BackPacksListeners implements Listener {

    private static BackPacksListeners instance;
    private Configuration backPacksConfig = SpigotPrison.getInstance().getBackPacksConfig();
    private Configuration backPacksDataConfig = BackPacksUtil.get().getBackPacksDataConfig();
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private BackPacksUtil backPacksUtil = BackPacksUtil.get();
    public List<String> activeBackpack = new ArrayList<>();
    private int colorNumber = 1;

    /**
     * Get BackPacksListeners instance.
     * */
    public static BackPacksListeners get() {
        if (instance == null) {
            instance = new BackPacksListeners();
        }
        return instance;
    }

    @EventHandler
    public void createInventoryFirst(PlayerJoinEvent e){

        Player p = e.getPlayer();

        if (backPacksUtil.getInventory(p) == null || backPacksDataConfig == null || backPacksDataConfig.getString("Inventories." + p.getUniqueId() + ".PlayerName") == null) {
            backPacksUtil.setDefaultBackpackPlayer(p);
        }

        if (getBoolean(backPacksConfig.getString("Options.BackPack_Item_OnJoin"))) {
            Bukkit.dispatchCommand(p, "backpack item");
        }
    }

    @EventHandler
    public void onDeadBackPack(PlayerDeathEvent e){

        if (getBoolean(backPacksConfig.getString("Options.BackPack_Lose_Items_On_Death"))) {
            backPacksUtil.resetBackpack(e.getEntity());
        }

    }

    /*@EventHandler
    public void onDropEvent(PlayerDropItemEvent e){

    }*/

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e){

        Compatibility compat = SpigotPrison.getInstance().getCompatibility();
        String title = compat.getGUITitle(e);
        if (title != null){
            title = title.substring(2);
            if (title.equalsIgnoreCase(e.getWhoClicked().getName() + " -> Backpack")){
                Player p = (Player) e.getWhoClicked();
                if (!activeBackpack.contains(p.getName())){
                    activeBackpack.add(p.getName());
                }
            }
        }

    }

    @EventHandler
    public void closeBackpackEvent(InventoryCloseEvent e){

        if (activeBackpack.contains(e.getPlayer().getName())){
            Player p = (Player) e.getPlayer();
            backPacksUtil.setInventory(p, e.getInventory());
            activeBackpack.remove(p.getName());
            //TODO close backpack message if enabled and maybe sound.
        }
    }

    @EventHandler
    public void playerInteractEventBackpackItem(PlayerInteractEvent e){
        if (backPacksConfig != null){
            if (getBoolean(SpigotPrison.getInstance().getConfig().getString("backpacks")) && getBoolean(backPacksConfig.getString("Options.Back_Pack_GUI_Opener_Item"))){

                Compatibility compat = SpigotPrison.getInstance().getCompatibility();

                Player p = e.getPlayer();
                ItemStack inHandItem = compat.getItemInMainHand(p);
                ItemStack materialConf = SpigotUtil.getXMaterial(backPacksConfig.getString("Options.BackPack_Item")).parseItem();

                if (materialConf != null && inHandItem.getType() == materialConf.getType() && inHandItem.hasItemMeta() && inHandItem.getItemMeta().hasDisplayName() && inHandItem.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotPrison.format(backPacksConfig.getString("Options.BackPack_Item_Title")))){
                    Bukkit.dispatchCommand(p, "gui backpack");
                    e.setCancelled(true);
                }
            }
        }
    }

    public void addToBackpackActive(Player p){
        if (!activeBackpack.contains(p.getName())){
            activeBackpack.add(p.getName());
        }
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



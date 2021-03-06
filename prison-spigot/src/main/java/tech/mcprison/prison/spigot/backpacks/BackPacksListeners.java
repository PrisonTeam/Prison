package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
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
    public static List<String> activeBackpack = new ArrayList<>();
    public static List<String> hasEditedBackpack = new ArrayList<>();
    public static List<String> hasClosedBackpack = new ArrayList<>();

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

    @EventHandler
    public void onDropEvent(PlayerDropItemEvent e){
        if (hasClosedBackpack.contains(e.getPlayer().getName())){
            e.getPlayer().closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerClickInventory(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player && SpigotPrison.getInstance().getCompatibility().getGUITitle(e) != null
                && SpigotPrison.getInstance().getCompatibility().getGUITitle(e).substring(2).equalsIgnoreCase(e.getWhoClicked().getName() + " -> Backpack")) {

            Player p = (Player) e.getWhoClicked();
            if (!hasClosedBackpack.contains(p.getName())) {
                if (activeBackpack.contains(p.getName())) {
                    if (!hasEditedBackpack.contains(p.getName())) {
                        addToHasEditedBackpack(p);
                    }
                } else {
                    e.setCancelled(true);
                    p.closeInventory();
                }
            } else {
                e.setCancelled(true);
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void closeBackpackEvent(InventoryCloseEvent e){

        if (activeBackpack.contains(e.getPlayer().getName()) && !hasClosedBackpack.contains(e.getPlayer().getName())){
            removeFromActiveBackpack((Player) e.getPlayer());
            addToHasClosedBackpack((Player) e.getPlayer());
        }
        if (hasEditedBackpack.contains(e.getPlayer().getName()) && !hasClosedBackpack.contains(e.getPlayer().getName())){
            Player p = (Player) e.getPlayer();
            backPacksUtil.setInventory(p, e.getInventory());
            removeFromHasEditedBackpack(p);
            addToHasClosedBackpack(p);
            //TODO close backpack message if enabled and maybe sound.
        }
    }

    public void addToHasClosedBackpack(Player p){
        if (!hasClosedBackpack.contains(p.getName())){
            hasClosedBackpack.add(p.getName());
            p.sendMessage("Adding " + p.getName()  + " to closed backpacks");
        }
    }

    public void removeFromHasClosedBackpack(Player p){
        hasClosedBackpack.remove(p.getName());
        p.sendMessage("Removing " + p.getName() + " from closed backpacks");
    }

    public void addToHasEditedBackpack(Player p){
        if (!hasEditedBackpack.contains(p.getName())){
            hasEditedBackpack.add(p.getName());
            p.sendMessage("Adding "  + p.getName() + " to backpacks editors");
        }
    }

    public void removeFromHasEditedBackpack(Player p){
        hasEditedBackpack.remove(p.getName());
        p.sendMessage("Removing " + p.getName() + " from backpacks editors");
    }

    public void removeFromActiveBackpack(Player p){
        activeBackpack.remove(p.getName());
        p.sendMessage("Removing " + p.getName() + " from active backpacks");
    }

    public void addToBackpackActive(Player p){
        if (!activeBackpack.contains(p.getName())){
            activeBackpack.add(p.getName());
            p.sendMessage("Adding " + p.getName() + " to the active backpacks");
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



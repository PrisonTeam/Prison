package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;

public class BackpacksListeners implements Listener {

    Compatibility compat = SpigotPrison.getInstance().getCompatibility();

    @EventHandler
    public void onPlayerJoinBackpack(PlayerJoinEvent e){
        Player p = e.getPlayer();
        BackpacksUtil.get().setDefaultDataConfig();
        BackpacksUtil.get().giveBackpackItem(p);
    }

    @EventHandler
    public void onBackpackCloseEvent(InventoryCloseEvent e){
        if (BackpacksUtil.openBackpacks.contains(e.getPlayer().getName())){
            BackpacksUtil.get().removeFromOpenBackpacks((Player) e.getPlayer());
            BackpacksUtil.get().playBackpackCloseSound(((Player) e.getPlayer()));
            String title = compat.getGUITitle(e);
            String id = null;
            if (title != null){
                title = title.substring(2);
            }
            try {
                if (title != null) {
                    id = title.substring(e.getPlayer().getName().length() + 13);
                }
            } catch (IndexOutOfBoundsException ignored){}
            if (id != null){
                if (BackpacksUtil.backpackEdited.contains(e.getPlayer().getName())){
                    BackpacksUtil.get().setInventory((Player) e.getPlayer(), e.getInventory(), id);
                    BackpacksUtil.get().removeFromEditedBackpack((Player) e.getPlayer());
                }
            } else {
                if (BackpacksUtil.backpackEdited.contains(e.getPlayer().getName())) {
                    BackpacksUtil.get().setInventory((Player) e.getPlayer(), e.getInventory());
                    BackpacksUtil.get().removeFromEditedBackpack((Player) e.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onDeadBackPack(PlayerDeathEvent e){
        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Lose_Items_On_Death"))) {
            BackpacksUtil.get().resetBackpack(e.getEntity());
            if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))) {
                for (String id : BackpacksUtil.get().getBackpacksIDs(e.getEntity())) {
                    BackpacksUtil.get().resetBackpack(e.getEntity(), id);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBackpackEdit(InventoryClickEvent e){
        if (BackpacksUtil.openBackpacks.contains(e.getWhoClicked().getName())) {
            String title = compat.getGUITitle(e);
            String id = null;
            if (title != null){
                title = title.substring(2);
            }
            try {
                if (title != null) {
                    id = title.substring(e.getWhoClicked().getName().length() + 13);
                }
            } catch (IndexOutOfBoundsException ignored){}
            if (id != null){
                if (title.equalsIgnoreCase(e.getWhoClicked().getName() + " -> Backpack-" + id)){
                    BackpacksUtil.get().addToEditedBackpack((Player) e.getWhoClicked());
                }
            } else {
                if (title != null && title.equalsIgnoreCase(e.getWhoClicked().getName() + " -> Backpack")) {
                    BackpacksUtil.get().addToEditedBackpack((Player) e.getWhoClicked());
                }
            }
        }

    }

    @EventHandler
    public void onPlayerClickBackpackItem(PlayerInteractEvent e){

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        ItemStack inHandItem = e.getItem();
        ItemStack materialConf = SpigotUtil.getXMaterial(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Item")).parseItem();

        if (materialConf != null && inHandItem != null && inHandItem.getType() == materialConf.getType() && inHandItem.hasItemMeta() && inHandItem.getItemMeta().hasDisplayName()
                && inHandItem.getItemMeta().getDisplayName().equalsIgnoreCase(SpigotPrison.format(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Item_Title")))) {
            if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))){
                Bukkit.dispatchCommand(p, "gui backpackslist");
            } else {
                Bukkit.dispatchCommand(p, "gui backpack");
            }
            e.setCancelled(true);
        }
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){
        return string != null && string.equalsIgnoreCase("true");
    }

}

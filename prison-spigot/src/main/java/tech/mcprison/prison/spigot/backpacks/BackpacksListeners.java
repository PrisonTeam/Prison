package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;

public class BackpacksListeners implements Listener {

    @EventHandler
    public void onPlayerJoinBackpack(PlayerJoinEvent e){

        Player p = e.getPlayer();
        BackpacksUtil.get().setDefaultBackpackPlayer(p);

    }

    @EventHandler
    public void onBackpackCloseEvent(InventoryCloseEvent e){
        if (BackpacksUtil.get().getOpenBackpacks().contains(e.getPlayer().getName())){
            BackpacksUtil.get().setInventory((Player) e.getPlayer(), e.getInventory());
            BackpacksUtil.get().removeFromOpenBackpacks((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void onDeadBackPack(PlayerDeathEvent e){

        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Lose_Items_On_Death"))) {
            BackpacksUtil.get().resetBackpack(e.getEntity());
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
            BackpacksUtil.get().openBackpack(p);
            e.setCancelled(true);
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

package tech.mcprison.prison.spigot.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author rbluer RoyalBlueRanger
 * @author GABRYCA
 */
public abstract class SpigotGUIComponents {

    protected ItemStack createButton(Material id, int amount, List<String> lore, String display) {

        ItemStack item = new ItemStack(id, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    protected List<String> createLore( String... lores ) {
        List<String> results = new ArrayList<>();
        for ( String lore : lores ) {
            results.add( SpigotPrison.format(lore) );
        }
        return results;
    }

    protected boolean checkRanks(Player p){
        Module module = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME ).orElse( null );
        if(!(module instanceof PrisonRanks)){
            p.sendMessage(SpigotPrison.format("&cThe GUI can't open because the &3Rank module &cisn't loaded"));
            p.closeInventory();
            p.closeInventory();
        }
        return module instanceof PrisonRanks;
    }
}

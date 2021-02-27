package tech.mcprison.prison.spigot.backpacks;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
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

        if (getBoolean(backPacksConfig.getString("Options.BackPack_Item_OnJoin"))) {
            Bukkit.dispatchCommand(p, "backpack item");
        }
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



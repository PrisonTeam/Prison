package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;

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



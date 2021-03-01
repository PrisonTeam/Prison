package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.mcprison.prison.spigot.SpigotPrison;

public class BackPacksListeners implements Listener {

    private Configuration backPacksConfig = SpigotPrison.getInstance().getBackPacksConfig();
    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
    private BackPacksUtil backPacksUtil = BackPacksUtil.get();

    @EventHandler
    public void createInventoryFirst(PlayerJoinEvent e){

        Player p = e.getPlayer();

        backPacksUtil.setDefaultBackpackPlayer(p);
    }

    @EventHandler
    public void onDeadBackPack(PlayerDeathEvent e){

        if (getBoolean(backPacksConfig.getString("Options.BackPack_Lose_Items_On_Death"))) {
            backPacksUtil.resetBackpack(e.getEntity());
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



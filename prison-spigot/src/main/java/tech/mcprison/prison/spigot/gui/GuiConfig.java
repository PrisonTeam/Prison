package tech.mcprison.prison.spigot.gui;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author GABRYCA
 */
public class GuiConfig {

    private FileConfiguration conf;

    public GuiConfig() {

        if (!Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prison-gui-enabled")).equalsIgnoreCase("true")){
            return;
        }

        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");

        // Everything's here
        values();

        // Get the final config
        conf = YamlConfiguration.loadConfiguration(file);
    }

    private void dataConfig(String path, String string){

        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");

        // Check if the config exists
        if(!file.exists()){
            try {
                file.createNewFile();
                conf = YamlConfiguration.loadConfiguration(file);
                conf.set(path, SpigotPrison.format(string));
                conf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                boolean newValue = false;
                int editedItems = 0;
                conf = YamlConfiguration.loadConfiguration(file);
                if (getFileGuiConfig().getString(path) == null){
                    conf.set(path, SpigotPrison.format(string));
                    editedItems++;
                    newValue = true;
                }
                if (newValue) {
                    conf.save(file);
                    System.out.println(Color.AQUA + "[Prison - GuiConfig.yml]" + Color.GREEN + " Added " + editedItems + " new values to the GuiConfig.yml");
                }
            } catch (IOException e2){
                e2.printStackTrace();
            }
        }

        // Get the final config
        conf = YamlConfiguration.loadConfiguration(file);


    }

    private void values(){
        dataConfig("Options.Ranks.GUI_Enabled","true");
        dataConfig("Options.Ranks.Permission_GUI_Enabled","false");
        dataConfig("Options.Ranks.Permission_GUI","prison.gui.ranks");
        dataConfig("Options.Mines.GUI_Enabled","true");
        dataConfig("Options.Mines.Permission_GUI_Enabled","false");
        dataConfig("Options.Mines.Permission_GUI","prison.gui.mines");
        dataConfig("Options.Prestiges.GUI_Enabled","true");
        dataConfig("Options.Prestiges.Permission_GUI_Enabled","false");
        dataConfig("Options.Prestiges.Permission_GUI","prison.gui.prestiges");
        dataConfig("Options.Ranks.Ladder","default");
        dataConfig("Options.Ranks.Item_gotten_rank","TRIPWIRE_HOOK");
        dataConfig("Options.Ranks.Item_not_gotten_rank","REDSTONE_BLOCK");
        dataConfig("Options.Ranks.Enchantment_effect_current_rank","true");
        dataConfig("Options.Mines.PermissionWarpPlugin","mines.tp.");
        dataConfig("Options.Mines.CommandWarpPlugin","mines tp");
    }

    public FileConfiguration getFileGuiConfig(){
        return conf;
    }

}

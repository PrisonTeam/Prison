package tech.mcprison.prison.spigot.sellall;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author GABRYCA
 */
public class SellAllConfig {

    private FileConfiguration conf;

    public SellAllConfig(){

        if (!Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("sellall")).equalsIgnoreCase("true")){
            return;
        }

        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");

        // Everything's here
        values();

        // Get the final config
        conf = YamlConfiguration.loadConfiguration(file);
    }

    private void dataConfig(String path, String string){

        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");

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
                conf = YamlConfiguration.loadConfiguration(file);
                    if (getFileSellAllConfig().getString(path) == null){
                        conf.set(path, SpigotPrison.format(string));
                        newValue = true;
                    }
                    if (newValue) {
                        conf.save(file);
                    }
            } catch (IOException e2){
                e2.printStackTrace();
            }
        }

        // Get the final config
        conf = YamlConfiguration.loadConfiguration(file);

    }

    private void values(){
        dataConfig("Options.GUI_Enabled", "true");
        dataConfig("Options.GUI_Permission_Enabled", "true");
        dataConfig("Options.GUI_Permission","prison.admin");
        dataConfig("Options.Sell_Permission_Enabled","false");
        dataConfig("Options.Sell_Permission","prison.admin");
        dataConfig("Options.Add_Permission_Enabled","true");
        dataConfig("Options.Add_Permission","prison.admin");
        dataConfig("Options.Delete_Permission_Enabled","true");
        dataConfig("Options.Delete_Permission","prison.admin");
        dataConfig("Options.Player_GUI_Enabled","true");
        dataConfig("Options.Player_GUI_Permission_Enabled","false");
        dataConfig("Options.Player_GUI_Permission","prison.sellall.playergui");
        dataConfig("Options.Multiplier_Enabled", "false");
        dataConfig("Options.Multiplier_Default", "1");
        dataConfig("Options.Multiplier_Command_Permission_Enabled", "true");
        dataConfig("Options.Multiplier_Command_Permission", "prison.admin");
    }

    public FileConfiguration getFileSellAllConfig(){
        return conf;
    }

}

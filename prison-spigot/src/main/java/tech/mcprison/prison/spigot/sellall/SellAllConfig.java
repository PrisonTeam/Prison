package tech.mcprison.prison.spigot.sellall;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;

/**
 * @author GABRYCA
 */
public class SellAllConfig {

    private FileConfiguration conf;

    public SellAllConfig(){

        if (!SpigotPrison.getInstance().getConfig().getString("sellall").equals("true")){
            return;
        }

        // Get array class with Path and Objects strings
        String[] path = getPath();
        String[] object = getObject();
        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");

        // Call a method, this makes a new file or update the old one
        fileChecker(path, object, file);

        // Get the final config
        conf = YamlConfiguration.loadConfiguration(file);
    }

    // Check the config and makes a new one or update it
    private void fileChecker(String[] path, String[] object, File file) {
        // Check if the config exists
        if(!file.exists()){
            // Call method
            newFile(path, object, file);
        } else {
            // Call method
            fileUpdater(path, object, file);
        }
    }

    // Check if something's missing and update the config
    private void fileUpdater(String[] path, String[] object, File file) {
        try {
            conf = YamlConfiguration.loadConfiguration(file);
            for (int i = 0; path.length > i; i++) {
                if (getFileSellAllConfig().getString(path[i]) == null){
                    conf.set(path[i], SpigotPrison.format(object[i]));
                }
            }
            conf.save(file);
        } catch (IOException e2){
            e2.printStackTrace();
        }
    }

    // Make a new config if missing
    private void newFile(String[] path, String[] object, File file) {
        try {
            file.createNewFile();
            conf = YamlConfiguration.loadConfiguration(file);
            for(int i = 0; path.length>i; i++){
                conf.set(path[i], SpigotPrison.format(object[i]));
            }
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getObject() {
        return new String[]{
                "true",
                "true",
                "prison.admin",
                "false",
                "prison.admin",
                "true",
                "prison.admin",
                "true",
                "prison.admin",
                "true",
                "false",
                "prison.sellall.playergui"
        };
    }

    private String[] getPath() {
        return new String[]{
                "Options.GUI_Enabled",
                "Options.GUI_Permission_Enabled",
                "Options.GUI_Permission",
                "Options.Sell_Permission_Enabled",
                "Options.Sell_Permission",
                "Options.Add_Permission_Enabled",
                "Options.Add_Permission",
                "Options.Delete_Permission_Enabled",
                "Options.Delete_Permission",
                "Options.Player_GUI_Enabled",
                "Options.Player_GUI_Permission_Enabled",
                "Options.Player_GUI_Permission"
        };
    }

    public FileConfiguration getFileSellAllConfig(){
        return conf;
    }

}

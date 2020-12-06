package tech.mcprison.prison.spigot.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class SellAllConfig extends SpigotConfigComponents {

    private FileConfiguration conf;
    private int changeCount = 0;

    public SellAllConfig(){

    	//if ( Prison.get().getPlatform().getConfigBooleanFalse("sellall")) {
        initialize();
    	//}
    }

    private void initialize(){

        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");

        // Check if the config exists
        fileMaker(file);

        // Get the config
        conf = YamlConfiguration.loadConfiguration(file);

        // Call method
        values();

        if (changeCount > 0) {
            try {
                conf.save(file);
                Output.get().logInfo( "&aThere were &b%d &anew values added for the language files " + "used by the SellAllConfig.yml file located at &b%s", changeCount, file.getAbsoluteFile() );
            }
            catch (IOException e) {
                Output.get().logInfo( "&4Failed to save &b%d &4new values for the language files " + "used by the SellAllConfig.yml file located at &b%s&4. " + "&a %s", changeCount, file.getAbsoluteFile(), e.getMessage() );
            }
        }
    }

    public void dataConfig(String key, String value){
        if (conf.getString(key) == null) {
            conf.set(key, value);
            changeCount++;
        }
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

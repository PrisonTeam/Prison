package tech.mcprison.prison.spigot.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;

public class BackpacksConfig extends SpigotConfigComponents{

    private FileConfiguration conf;
    private int changeCount = 0;

    public BackpacksConfig(){

        initialize();
    }

    public void initialize(){

        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/backpacks/backpacksconfig.yml");

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

        conf = YamlConfiguration.loadConfiguration(file);
    }

    public void dataConfig(String key, String value){
        if (conf.getString(key) == null) {
            conf.set(key, value);
            changeCount++;
        }
    }

    private void values(){
        dataConfig("Options.BackPack_Use_Permission_Enabled", "false");
        dataConfig("Options.BackPack_Use_Permission", "prison.backpack");
        dataConfig("Options.BackPack_Default_Size", "54");
        dataConfig("Options.BackPack_AutoPickup_Usable", "false");
        dataConfig("Options.Back_Pack_GUI_Opener_Item", "true");
        dataConfig("Options.BackPack_Item", "CHEST");
        dataConfig("Options.BackPack_Item_Title", "&3Backpack");
        dataConfig("Options.BackPack_Item_OnJoin", "true");
        dataConfig("Options.BackPack_Lose_Items_On_Death", "false");
        dataConfig("Options.BackPack_Open_Sound_Enabled", "true");
        dataConfig("Options.BackPack_Open_Sound", "BLOCK_CHEST_OPEN");
        dataConfig("Options.BackPack_Close_Sound_Enabled", "true");
        dataConfig("Options.BackPack_Close_Sound", "BLOCK_CHEST_CLOSE");
        dataConfig("Options.Multiple-BackPacks-For-Player-Enabled", "false");
        dataConfig("Options.Multiple-BackPacks-For-Player", "2");
    }

    public FileConfiguration getFileBackpacksConfig(){
        return conf;
    }

}

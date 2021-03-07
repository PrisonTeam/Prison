package tech.mcprison.prison.spigot.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class SellAllConfig extends SpigotConfigComponents {

    private FileConfiguration conf;
    private int changeCount = 0;

    public SellAllConfig(){

        initialize();
    }

    public void initialize(){

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

        conf = YamlConfiguration.loadConfiguration(file);
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
        dataConfig("Options.Sell_Per_Block_Permission_Enabled", "false");
        dataConfig("Options.Sell_Per_Block_Permission", "prison.sellall.");
        dataConfig("Options.Sell_Delay_Enabled", "false");
        dataConfig("Options.Sell_Delay_Seconds", "5");
        dataConfig("Options.Sell_Notify_Enabled", "true");
        dataConfig("Options.Sell_Sound_Enabled", "true");
        dataConfig("Options.Sell_Sound_Success_Name", "ENTITY_PLAYER_LEVELUP");
        dataConfig("Options.Sell_Sound_Fail_Name", "BLOCK_ANVIL_PLACE");
        dataConfig("Options.Sell_Prison_BackPack_Items", "true");
        dataConfig("Options.Sell_MinesBackPacks_Plugin_Backpack", "true");
        dataConfig("Options.SellAll_Currency", "default");
        dataConfig("Options.SellAll_Sign_Enabled", "false");
        dataConfig("Options.SellAll_Sign_Use_Permission_Enabled", "false");
        dataConfig("Options.SellAll_Sign_Use_Permission", "prison.sign");
        dataConfig("Options.SellAll_By_Sign_Only", "false");
        dataConfig("Options.SellAll_By_Sign_Only_Bypass_Permission", "prison.admin");
        dataConfig("Options.SellAll_Sign_Notify", "false");
        dataConfig("Options.SellAll_Sign_Visible_Tag", "&7[&3SellAll&7]");
        dataConfig("Options.Player_GUI_Enabled","true");
        dataConfig("Options.Player_GUI_Permission_Enabled","false");
        dataConfig("Options.Player_GUI_Permission","prison.sellall.playergui");
        dataConfig("Options.Full_Inv_AutoSell", "false");
        dataConfig("Options.Full_Inv_AutoSell_Notification", "true");
        dataConfig("Options.Full_Inv_AutoSell_perUserToggleable", "false");
        dataConfig("Options.Full_Inv_AutoSell_perUserToggleable_Need_Perm", "false");
        dataConfig("Options.Full_Inv_AutoSell_PerUserToggleable_Permission", "prison.sellall.toggle");
        dataConfig("Options.Multiplier_Enabled", "false");
        dataConfig("Options.Multiplier_Default", "1");
        dataConfig("Options.Multiplier_Permission_Only_Higher", "false");
        dataConfig("Options.ShiftAndRightClickSellAll.Enabled", "false");
        dataConfig("Options.ShiftAndRightClickSellAll.PermissionEnabled", "false");
        dataConfig("Options.ShiftAndRightClickSellAll.Permission", "prison.player");
    }

    public FileConfiguration getFileSellAllConfig(){
        return conf;
    }
}

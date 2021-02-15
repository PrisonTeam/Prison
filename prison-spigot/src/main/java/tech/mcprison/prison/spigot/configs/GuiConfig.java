package tech.mcprison.prison.spigot.configs;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */
public class GuiConfig extends SpigotConfigComponents{

    // Declaring parameters and variables
    private FileConfiguration conf;
    private int changeCount = 0;

    // Check if the GuiConfig's enabled
    public GuiConfig() {
        // Will make ALWAYS the config even if GUIs are disabled
        initialize();
    }

    public void initialize() {

    	// Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");
    	fileMaker(file);
    	conf = YamlConfiguration.loadConfiguration(file);
    	
        // Values to write down into the config
        values();

        // Count and save
        if (changeCount > 0) {
        	try {
				conf.save(file);
				Output.get().logInfo("&aThere were &b%d &anew values added to the GuiConfig.yml file located at &b%s", changeCount, file.getAbsoluteFile());
			}
			catch (IOException e) {
				Output.get().logInfo("&4Failed to save &b%d &4new values to the GuiConfig.yml file located at " + "&b%s&4. " + "&a %s", changeCount, file.getAbsoluteFile(), e.getMessage());
			}
        }

        conf = YamlConfiguration.loadConfiguration(file);
    }

    private void dataConfig(String key, String value){
    	if (conf.getString(key) == null) {
    		conf.set(key, value);
    		changeCount++;
    	}
    }

    // All the strings of the config should be here
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
        dataConfig("Options.Setup.EnabledGUI", "true");
        dataConfig("Options.Titles.PlayerRanksGUI", "&3Player -> Ranks");
        dataConfig("Options.Titles.PlayerPrestigesGUI", "&3Player -> Prestiges");
        dataConfig("Options.Titles.PlayerMinesGUI", "&3Player -> Mines");
    }

    // Return method to call the config, you can use this or the global one in the main class
    public FileConfiguration getFileGuiConfig(){
        return conf;
    }
}

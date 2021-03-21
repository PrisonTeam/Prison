package tech.mcprison.prison.spigot.sellall;


import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;

/**
 * SellAllUtil class, this will replace the whole SellAll mess of a code of SellAllPrisonCommands.
 *
 * @author GABRYCA
 * */
public class SellAllUtil {

    private SellAllUtil instance;
    private boolean isEnabled = isEnabled();
    private File sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
    private Configuration sellAllConfig = YamlConfiguration.loadConfiguration(sellAllFile);

    /**
     * Check if SellAll's enabled.
     * */
    public boolean isEnabled(){
        return getBoolean(SpigotPrison.getInstance().getConfig().getString("sellall"));
    }

    /**
     * Get SellAll instance.
     * */
    public SellAllUtil get(){
        return getInstance();
    }

    /**
     * SellAll config updater.
     * */
    public void updateSellAllConfig(){
        sellAllFile = new File(SpigotPrison.getInstance().getDataFolder() + "/SellAllConfig.yml");
        sellAllConfig = YamlConfiguration.loadConfiguration(sellAllFile);
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){
        return string != null && string.equalsIgnoreCase("true");
    }

    private SellAllUtil getInstance() {
        if (isEnabled && instance == null){
            instance = new SellAllUtil();
        }

        return instance;
    }

}

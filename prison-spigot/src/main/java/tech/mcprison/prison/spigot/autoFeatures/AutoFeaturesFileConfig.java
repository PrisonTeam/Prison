package tech.mcprison.prison.spigot.autoFeatures;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;

public class AutoFeaturesFileConfig {

    private FileConfiguration conf;

    public AutoFeaturesFileConfig() {
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/module_conf/autoFeatures/autoFeaturesConfig.yml");
        if(!file.exists()){
            try {
                File dir = file.getParentFile();
                dir.mkdirs();
                file.createNewFile();
                conf = YamlConfiguration.loadConfiguration(file);

                conf.createSection("Messages");
                conf.createSection("Options");

                conf.set("Messages.InventoryIsFullDroppingItems", "&cWARNING! Your inventory's full and you're dropping items!");
                conf.set("Messages.InventoryIsFullLosingItems", "&cWARNING! Your inventory's full and you're losing items!");

                conf.set("Options.General.AreEnabledFeatures", false);
                conf.set("Options.General.DropItemsIfInventoryIsFull", true);

                conf.set("Options.AutoPickup.AutoPickupEnabled", true);
                conf.set("Options.AutoPickup.AutoPickupAllBlocks",true);
                conf.set("Options.AutoPickup.AutoPickupGoldOre", true);
                conf.set("Options.AutoPickup.AutoPickupIronOre", true);
                conf.set("Options.AutoPickup.AutoPickupCoalOre", true);
                conf.set("Options.AutoPickup.AutoPickupDiamondOre", true);
                conf.set("Options.AutoPickup.AutoPickupRedstoneOre", true);
                conf.set("Options.AutoPickup.AutoPickupEmeraldOre", true);
                conf.set("Options.AutoPickup.AutoPickupQuartzOre", true);
                conf.set("Options.AutoPickup.AutoPickupLapisOre", true);

                conf.set("Options.AutoSmelt.AutoSmeltEnabled", true);
                conf.set("Options.AutoSmelt.AutoSmeltGoldOre", true);
                conf.set("Options.AutoSmelt.AutoSmeltIronOre", true);

                conf.set("Options.AutoBlock.AutoBlockEnabled", true);
                conf.set("Options.AutoBlock.AutoBlockGoldBlock", true);
                conf.set("Options.AutoBlock.AutoBlockIronBlock", true);
                conf.set("Options.AutoBlock.AutoBlockCoalBlock", true);
                conf.set("Options.AutoBlock.AutoBlockDiamondBlock", true);
                conf.set("Options.AutoBlock.AutoBlockRedstoneBlock", true);
                conf.set("Options.AutoBlock.AutoBlockEmeraldBlock", true);
                conf.set("Options.AutoBlock.AutoBlockQuartzBlock", true);
                conf.set("Options.AutoBlock.AutoBlockLapisBlock", true);

                conf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        conf = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFile(){
        return conf;
    }

}

package tech.mcprison.prison.spigot.gui;

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
                conf = YamlConfiguration.loadConfiguration(file);
                if (getFileGuiConfig().getString(path) == null){
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
        dataConfig("Gui.Lore.ActivateWithinMode","&8Activate Within mode.");
        dataConfig("Gui.Lore.ActivateRadiusMode","&8Activate Radius mode.");
        dataConfig("Gui.Lore.AutoPickupGuiManager","&8AutoPickup Manager.");
        dataConfig("Gui.Lore.AutoSmeltGuiManager","&8AutoSmelt Manager.");
        dataConfig("Gui.Lore.AutoBlockGuiManager","&8AutoBlock Manager.");
        dataConfig("Gui.Lore.BlockType","&3BlockType: ");
        dataConfig("Gui.Lore.Blocks","&3Blocks:");
        dataConfig("Gui.Lore.Blocks2","&8Manage blocks of the Mine.");
        dataConfig("Gui.Lore.ClickToChoose","&8Click to choose.");
        dataConfig("Gui.Lore.ClickToConfirm","&8Click to confirm.");
        dataConfig("Gui.Lore.ClickToCancel","&8Click to cancel.");
        dataConfig("Gui.Lore.ClickToDecrease","&8Click to decrease.");
        dataConfig("Gui.Lore.ClickToIncrease","&8Click to increase.");
        dataConfig("Gui.Lore.ClickToManageRank","&8Manage this rank.");
        dataConfig("Gui.Lore.ClickToManageCommands","&8Manage RankUPCommands.");
        dataConfig("Gui.Lore.ClickToOpen","&8Click to open.");
        dataConfig("Gui.Lore.ClickToTeleport","&8Click to teleport.");
        dataConfig("Gui.Lore.ClickToUse","&8Click to use.");
        dataConfig("Gui.Lore.ClickToRankup","&8Click to rankup");
        dataConfig("Gui.Lore.Chance","&3Chance: ");
        dataConfig("Gui.Lore.Command","&3Command: &7");
        dataConfig("Gui.Lore.ContainsTheRank","&3The Rank ");
        dataConfig("Gui.Lore.ContainsNoCommands"," &3contains no commands.");
        dataConfig("Gui.Lore.DisableNotifications","&8Disable notifications.");
        dataConfig("Gui.Lore.EnabledAll","&aAll features ON");
        dataConfig("Gui.Lore.DisabledAll","&aAll features OFF");
        dataConfig("Gui.Lore.FullSoundEnabled","&aFull Inv., notify with sound ON");
        dataConfig("Gui.Lore.FullSoundDisabled","&cFull Inv., notify with sound OFF");
        dataConfig("Gui.Lore.FullHologramEnabled","&aFull Inv., notify with hologram ON");
        dataConfig("Gui.Lore.FullHologramDisabled","&cFull Inv., notify with hologram OFF");
        dataConfig("Gui.Lore.Id","&3Rank id: &7");
        dataConfig("Gui.Lore.Info","&8&l|&3Info&8|");
        dataConfig("Gui.Lore.IfYouHaveEnoughMoney","&8If you have enough money");
        dataConfig("Gui.Lore.LadderThereAre","&8There're &3");
        dataConfig("Gui.Lore.LadderCommands"," &3Commands at ladder:");
        dataConfig("Gui.Lore.LeftClickToConfirm","&aLeft-Click to confirm.");
        dataConfig("Gui.Lore.LeftClickToOpen","&8Left Click to open.");
        dataConfig("Gui.Lore.LeftClickToReset","&aLeft Click to reset");
        dataConfig("Gui.Lore.ManageResetTime","&8Manage the reset time of Mine.");
        dataConfig("Gui.Lore.MinesButton","&8Mines GUI manager.");
        dataConfig("Gui.Lore.Name","&3Rank Name: &7");
        dataConfig("Gui.Lore.Notifications","&8Edit Mines notifications.");
        dataConfig("Gui.Lore.PlayersWithTheRank","&3Players at rank: &7");
        dataConfig("Gui.Lore.Price","&3Price: &a$");
        dataConfig("Gui.Lore.Price2","&8Price: &a$");
        dataConfig("Gui.Lore.Price3","&3Rank Price: &a$");
        dataConfig("Gui.Lore.PrisonTasksButton","&8Prison Tasks Manager.");
        dataConfig("Gui.Lore.ResetTime","&3Reset time(s): &7");
        dataConfig("Gui.Lore.Radius","&8Radius: ");
        dataConfig("Gui.Lore.RankupCommands","&8&l|&3RankUPCommands&8| &8&l- &3");
        dataConfig("Gui.Lore.Rankup","&aRankup");
        dataConfig("Gui.Lore.RanksButton","&8Ranks GUI manager.");
        dataConfig("Gui.Lore.ResetButton","&8Resets the mine.");
        dataConfig("Gui.Lore.RightClickToCancel","&cRight-Click to cancel.");
        dataConfig("Gui.Lore.RightClickToEnable","&aRight click to enable");
        dataConfig("Gui.Lore.RightClickToToggle","&cRight click to toggle");
        dataConfig("Gui.Lore.SpawnPoint","&3Spawnpoint: &7");
        dataConfig("Gui.Lore.StatusLockedMine","&8Status: &cLocked");
        dataConfig("Gui.Lore.StatusUnlockedMine","&8Status: &aUnlocked");
        dataConfig("Gui.Lore.SpawnPoint2","&8Set the mine spawn point.");
        dataConfig("Gui.Lore.SizeOfMine","&3Size of Mine: &7");
        dataConfig("Gui.Lore.Selected","&3Selected");
        dataConfig("Gui.Lore.ShiftAndRightClickToDelete","&cShift + Right click to delete.");
        dataConfig("Gui.Lore.ShiftAndRightClickToDisable","&cShift + Right click to disable");
        dataConfig("Gui.Lore.ShiftAndRightClickToToggle","&cShift + Right click to toggle");
        dataConfig("Gui.Lore.StatusEnabled","&8Enabled");
        dataConfig("Gui.Lore.StatusDisabled","&8Disabled");
        dataConfig("Gui.Lore.SkipReset1","&8Skip the reset if ");
        dataConfig("Gui.Lore.SkipReset2","&8not enough blocks ");
        dataConfig("Gui.Lore.SkipReset3","&8have been mined.");
        dataConfig("Gui.Lore.Tp","&8Tp to the mine.");
        dataConfig("Gui.Lore.Tag","&3Tag: &8");
        dataConfig("Gui.Lore.Tag2","&3Rank Tag: &7");
        dataConfig("Gui.Lore.Time","&8Time: ");
        dataConfig("Gui.Lore.Volume","&3Volume: &7");
        dataConfig("Gui.Lore.Value", "&3Value: &a$");
        dataConfig("Gui.Lore.World","&3World: &7");
        dataConfig("Gui.Message.NoSellAllItems", "&cSorry but there aren't SellAll Items to show.");
        dataConfig("Gui.Message.EmptyGui","&cSorry, the GUI looks empty.");
        dataConfig("Gui.Message.TooManyBlocks","&cSorry, but there're too many Blocks and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyLadders","&cSorry, but there're too many ladders and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyMines","&cSorry, but there're too many mines and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyRankupCommands","&cSorry, but there're too many RankupCommands and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManySellAllItems", "&3[PRISON WARN] &cThere are too many items and the MAX for the GUI's 54!");
        dataConfig("Gui.Message.ZeroBlocksReset1","&8Set a mine's delay ");
        dataConfig("Gui.Message.ZeroBlocksReset2","&8before reset when it ");
        dataConfig("Gui.Message.ZeroBlocksReset3","&8reaches zero blocks.");
    }

    public FileConfiguration getFileGuiConfig(){
        return conf;
    }

}

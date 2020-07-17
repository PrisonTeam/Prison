package tech.mcprison.prison.spigot.gui;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;

/**
 * @author GABRYCA
 */
public class GuiConfig {

    private FileConfiguration conf;

    public GuiConfig() {

        // Get array class with Path and Objects strings
        String[] path = getPath();
        String[] object = getObject();
        // Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");

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
                if (getFileGuiConfig().getString(path[i]) == null){
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
                "false",
                "prison.gui.ranks",
                "true",
                "false",
                "prison.gui.mines",
                "true",
                "false",
                "prison.gui.prestiges",
                "default",
                "TRIPWIRE_HOOK",
                "REDSTONE_BLOCK",
                "true",
                "mines.tp.",
                "mines tp",
                "&8Activate Within mode.",
                "&8Activate Radius mode.",
                "&8AutoPickup Manager.",
                "&8AutoSmelt Manager.",
                "&8AutoBlock Manager.",
                "&3BlockType: ",
                "&3Blocks:",
                "&8Manage blocks of the Mine.",
                "&8Click to choose.",
                "&8Click to confirm.",
                "&8Click to cancel.",
                "&8Click to decrease.",
                "&8Click to increase.",
                "&8Manage this rank.",
                "&8Manage RankUPCommands.",
                "&8Click to open.",
                "&8Click to teleport.",
                "&8Click to use.",
                "&8Click to rankup",
                "&3Chance: ",
                "&3Command: &7",
                "&3The Rank ",
                " contains no commands.",
                "&8Disable notifications.",
                "&aAll features ON",
                "&aAll features OFF",
                "&aFull Inv., notify with sound ON",
                "&cFull Inv., notify with sound OFF",
                "&aFull Inv., notify with hologram ON",
                "&cFull Inv., notify with hologram OFF",
                "&3Rank id: &7",
                "&8&l|&3Info&8|",
                "&8If you have enough money",
                "&8There're &3",
                " &3Commands at ladder:",
                "&aLeft-Click to confirm.",
                "&8Left Click to open.",
                "&aLeft Click to reset",
                "&8Manage the reset time of Mine.",
                "&8Mines GUI manager.",
                "&3Rank Name: &7",
                "&8Edit Mines notifications.",
                "&3Players at rank: &7",
                "&3Price: &a$",
                "&8Price: &a$",
                "&3Rank Price: &a$",
                "&8Prison Tasks Manager.",
                "&3Reset time(s): &7",
                "&8Radius: ",
                "&8&l|&3RankUPCommands&8| &8&l- &3",
                "&aRankup",
                "&8Ranks GUI manager.",
                "&8Resets the mine.",
                "&cRight-Click to cancel.",
                "&aRight click to enable",
                "&cRight click to toggle",
                "&3Spawnpoint: &7",
                "&8Status: &cLocked",
                "&8Status: &aUnlocked",
                "&8Set the mine spawn point.",
                "&3Size of Mine: &7",
                "&3Selected",
                "&cShift + Right click to delete.",
                "&cShift + Right click to disable",
                "&cShift + Right click to toggle",
                "&8Enabled",
                "&8Disabled",
                "&8Skip the reset if ",
                "&8not enough blocks ",
                "&8have been mined.",
                "&8Tp to the mine.",
                "&3Tag: &8",
                "&3Rank Tag: &7",
                "&8Time: ",
                "&3Volume: &7",
                "&3World: &7",
                "&cSorry, the GUI looks empty.",
                "&cSorry, but there're too many Blocks and the max's 54 for the GUI",
                "&cSorry, but there're too many ladders and the max's 54 for the GUI",
                "&cSorry, but there're too many mines and the max's 54 for the GUI",
                "&cSorry, but there're too many RankupCommands and the max's 54 for the GUI",
                "&8Set a mine's delay ",
                "&8before reset when it ",
                "&8reaches zero blocks."};
    }

    private String[] getPath() {
        return new String[]{
                "Options.Ranks.GUI_Enabled",
                "Options.Ranks.Permission_GUI_Enabled",
                "Options.Ranks.Permission_GUI",
                "Options.Mines.GUI_Enabled",
                "Options.Mines.Permission_GUI_Enabled",
                "Options.Mines.Permission_GUI",
                "Options.Prestiges.GUI_Enabled",
                "Options.Prestiges.Permission_GUI_Enabled",
                "Options.Prestiges.Permission_GUI",
                "Options.Ranks.Ladder",
                "Options.Ranks.Item_gotten_rank",
                "Options.Ranks.Item_not_gotten_rank",
                "Options.Ranks.Enchantment_effect_current_rank",
                "Options.Mines.PermissionWarpPlugin",
                "Options.Mines.CommandWarpPlugin",
                "Gui.Lore.ActivateWithinMode",
                "Gui.Lore.ActivateRadiusMode",
                "Gui.Lore.AutoPickupGuiManager",
                "Gui.Lore.AutoSmeltGuiManager",
                "Gui.Lore.AutoBlockGuiManager",
                "Gui.Lore.BlockType",
                "Gui.Lore.Blocks",
                "Gui.Lore.Blocks2",
                "Gui.Lore.ClickToChoose",
                "Gui.Lore.ClickToConfirm",
                "Gui.Lore.ClickToCancel",
                "Gui.Lore.ClickToDecrease",
                "Gui.Lore.ClickToIncrease",
                "Gui.Lore.ClickToManageRank",
                "Gui.Lore.ClickToManageCommands",
                "Gui.Lore.ClickToOpen",
                "Gui.Lore.ClickToTeleport",
                "Gui.Lore.ClickToUse",
                "Gui.Lore.ClickToRankup",
                "Gui.Lore.Chance",
                "Gui.Lore.Command",
                "Gui.Lore.ContainsTheRank",
                "Gui.Lore.ContainsNoCommands",
                "Gui.Lore.DisableNotifications",
                "Gui.Lore.EnabledAll",
                "Gui.Lore.DisabledAll",
                "Gui.Lore.FullSoundEnabled",
                "Gui.Lore.FullSoundDisabled",
                "Gui.Lore.FullHologramEnabled",
                "Gui.Lore.FullHologramDisabled",
                "Gui.Lore.Id",
                "Gui.Lore.Info",
                "Gui.Lore.IfYouHaveEnoughMoney",
                "Gui.Lore.LadderThereAre",
                "Gui.Lore.LadderCommands",
                "Gui.Lore.LeftClickToConfirm",
                "Gui.Lore.LeftClickToOpen",
                "Gui.Lore.LeftClickToReset",
                "Gui.Lore.ManageResetTime",
                "Gui.Lore.MinesButton",
                "Gui.Lore.Name",
                "Gui.Lore.Notifications",
                "Gui.Lore.PlayersWithTheRank",
                "Gui.Lore.Price",
                "Gui.Lore.Price2",
                "Gui.Lore.Price3",
                "Gui.Lore.PrisonTasksButton",
                "Gui.Lore.ResetTime",
                "Gui.Lore.Radius",
                "Gui.Lore.RankupCommands",
                "Gui.Lore.Rankup",
                "Gui.Lore.RanksButton",
                "Gui.Lore.ResetButton",
                "Gui.Lore.RightClickToCancel",
                "Gui.Lore.RightClickToEnable",
                "Gui.Lore.RightClickToToggle",
                "Gui.Lore.SpawnPoint",
                "Gui.Lore.StatusLockedMine",
                "Gui.Lore.StatusUnlockedMine",
                "Gui.Lore.SpawnPoint2",
                "Gui.Lore.SizeOfMine",
                "Gui.Lore.Selected",
                "Gui.Lore.ShiftAndRightClickToDelete",
                "Gui.Lore.ShiftAndRightClickToDisable",
                "Gui.Lore.ShiftAndRightClickToToggle",
                "Gui.Lore.StatusEnabled",
                "Gui.Lore.StatusDisabled",
                "Gui.Lore.SkipReset1",
                "Gui.Lore.SkipReset2",
                "Gui.Lore.SkipReset3",
                "Gui.Lore.Tp",
                "Gui.Lore.Tag",
                "Gui.Lore.Tag2",
                "Gui.Lore.Time",
                "Gui.Lore.Volume",
                "Gui.Lore.World",
                "Gui.Message.EmptyGui",
                "Gui.Message.TooManyBlocks",
                "Gui.Message.TooManyLadders",
                "Gui.Message.TooManyMines",
                "Gui.Message.TooManyRankupCommands",
                "Gui.Message.ZeroBlocksReset1",
                "Gui.Message.ZeroBlocksReset2",
                "Gui.Message.ZeroBlocksReset3",
        };
    }

    public FileConfiguration getFileGuiConfig(){
        return conf;
    }

}

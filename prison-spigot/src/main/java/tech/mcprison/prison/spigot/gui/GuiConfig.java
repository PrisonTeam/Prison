package tech.mcprison.prison.spigot.gui;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.File;
import java.io.IOException;

public class GuiConfig {

    private FileConfiguration conf;

    public GuiConfig() {
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
                conf = YamlConfiguration.loadConfiguration(file);
                conf.createSection("Gui");
                conf.createSection("Options");
                conf.set("Options.Ranks.Ladder", SpigotPrison.format("default"));
                conf.set("Options.Mines.PermissionWarpPlugin", SpigotPrison.format("mines.tp."));
                conf.set("Options.Mines.CommandWarpPlugin", SpigotPrison.format("mines tp"));
                conf.set("Gui.Lore.ActivateWithinMode", SpigotPrison.format("&8Activate Within mode."));
                conf.set("Gui.Lore.ActivateRadiusMode", SpigotPrison.format("&8Activate Radius mode."));
                conf.set("Gui.Lore.AutoPickupGuiManager", SpigotPrison.format("&8AutoPickup GUI manager."));
                conf.set("Gui.Lore.AutoSmeltGuiManager", SpigotPrison.format("&8AutoSmelt GUI manager."));
                conf.set("Gui.Lore.AutoBlockGuiManager", SpigotPrison.format("&8AutoBlock GUI manager."));
                conf.set("Gui.Lore.BlockType", SpigotPrison.format("&3BlockType: "));
                conf.set("Gui.Lore.Blocks", SpigotPrison.format("&3Blocks:"));
                conf.set("Gui.Lore.Blocks2", SpigotPrison.format("&8Manage the blocks of the Mine."));
                conf.set("Gui.Lore.ClickToChoose", SpigotPrison.format("&8Click to choose."));
                conf.set("Gui.Lore.ClickToConfirm", SpigotPrison.format("&8Click to confirm."));
                conf.set("Gui.Lore.ClickToCancel", SpigotPrison.format("&8Click to cancel."));
                conf.set("Gui.Lore.ClickToDecrease", SpigotPrison.format("&8Click to decrease."));
                conf.set("Gui.Lore.ClickToIncrease", SpigotPrison.format("&8Click to increase."));
                conf.set("Gui.Lore.ClickToManageRank", SpigotPrison.format("&8Click to manage the rank."));
                conf.set("Gui.Lore.ClickToManageCommands", SpigotPrison.format("&8Click to manage RankUPCommands."));
                conf.set("Gui.Lore.ClickToOpen", SpigotPrison.format("&8Click to open."));
                conf.set("Gui.Lore.ClickToTeleport", SpigotPrison.format("&8Click to teleport."));
                conf.set("Gui.Lore.ClickToUse", SpigotPrison.format("&8Click to use."));
                conf.set("Gui.Lore.Chance", SpigotPrison.format("&3Chance: "));
                conf.set("Gui.Lore.Command", SpigotPrison.format("&3Command: &7"));
                conf.set("Gui.Lore.ContainsTheRank", SpigotPrison.format("&3The Rank "));
                conf.set("Gui.Lore.ContainsNoCommands", SpigotPrison.format(" contains no commands."));
                conf.set("Gui.Lore.DisableNotifications", SpigotPrison.format("&8Disable notifications."));
                conf.set("Gui.Lore.EnabledAll", SpigotPrison.format("&aAll features enabled"));
                conf.set("Gui.Lore.DisabledAll", SpigotPrison.format("&aAll features disabled"));
                conf.set("Gui.Lore.FullSoundEnabled", SpigotPrison.format("&aFull Inventory, notify with sound enabled"));
                conf.set("Gui.Lore.FullSoundDisabled", SpigotPrison.format("&cFull Inventory, notify with sound disabled"));
                conf.set("Gui.Lore.FullHologramEnabled", SpigotPrison.format("&aFull Inventory, notify with hologram enabled"));
                conf.set("Gui.Lore.FullHologramDisabled", SpigotPrison.format("&cFull Inventory, notify with hologram disabled"));
                conf.set("Gui.Lore.Id", SpigotPrison.format("&3Rank id: &7"));
                conf.set("Gui.Lore.Info", SpigotPrison.format("&8&l|&3Info&8|"));
                conf.set("Gui.Lore.LadderThereAre", SpigotPrison.format("&8There're &3"));
                conf.set("Gui.Lore.LadderCommands", SpigotPrison.format(" &3Commands &8in this ladder:"));
                conf.set("Gui.Lore.LeftClickToConfirm", SpigotPrison.format("&aLeft-Click to confirm."));
                conf.set("Gui.Lore.LeftClickToOpen", SpigotPrison.format("&8Left Click to open."));
                conf.set("Gui.Lore.LeftClickToReset", SpigotPrison.format("&aLeft Click to reset"));
                conf.set("Gui.Lore.ManageResetTime", SpigotPrison.format("&8Manage the reset time of the Mine."));
                conf.set("Gui.Lore.MinesButton", SpigotPrison.format("&8Mines GUI manager."));
                conf.set("Gui.Lore.Name", SpigotPrison.format("&3Rank Name: &7"));
                conf.set("Gui.Lore.Notifications", SpigotPrison.format("&8Change Mines notifications."));
                conf.set("Gui.Lore.PlayersWithTheRank", SpigotPrison.format("&3Players with this rank: &7"));
                conf.set("Gui.Lore.Price", SpigotPrison.format("&3Price: &a$"));
                conf.set("Gui.Lore.Price2", SpigotPrison.format("&8Price: &a$"));
                conf.set("Gui.Lore.Price3", SpigotPrison.format("&3Rank Price: &a$"));
                conf.set("Gui.Lore.PrisonTasksButton", SpigotPrison.format("&8Prison Tasks GUI manager."));
                conf.set("Gui.Lore.ResetTime", SpigotPrison.format("&3Reset time in seconds: &7"));
                conf.set("Gui.Lore.Radius", SpigotPrison.format("&8Radius: "));
                conf.set("Gui.Lore.RankupCommands", SpigotPrison.format("&8&l|&3RankUPCommands&8| &8&l- &3"));
                conf.set("Gui.Lore.RanksButton", SpigotPrison.format("&8Ranks GUI manager."));
                conf.set("Gui.Lore.ResetButton", SpigotPrison.format("&8Resets the mine."));
                conf.set("Gui.Lore.RightClickToCancel", SpigotPrison.format("&cRight-Click to cancel."));
                conf.set("Gui.Lore.RightClickToEnable", SpigotPrison.format("&aRight click to enable"));
                conf.set("Gui.Lore.RightClickToToggle", SpigotPrison.format("&cRight click to toggle"));
                conf.set("Gui.Lore.SpawnPoint", SpigotPrison.format("&3Spawnpoint: &7"));
                conf.set("Gui.Lore.StatusLockedMine", SpigotPrison.format("&8Status: &cLocked"));
                conf.set("Gui.Lore.StatusUnlockedMine", SpigotPrison.format("&8Status: &aUnlocked"));
                conf.set("Gui.Lore.SpawnPoint2", SpigotPrison.format("&8Set the mine spawn point at your location."));
                conf.set("Gui.Lore.SizeOfMine", SpigotPrison.format("&3Size of Mine: &7"));
                conf.set("Gui.Lore.Selected", SpigotPrison.format("&3Selected"));
                conf.set("Gui.Lore.ShiftAndRightClickToDelete", SpigotPrison.format("&cPress Shift + Right click to delete."));
                conf.set("Gui.Lore.ShiftAndRightClickToDisable", SpigotPrison.format("&cPress Shift + Right click to disable"));
                conf.set("Gui.Lore.ShiftAndRightClickToToggle", SpigotPrison.format("&cPress Shift + Right click to toggle"));
                conf.set("Gui.Lore.StatusEnabled", SpigotPrison.format("&8Enabled"));
                conf.set("Gui.Lore.StatusDisabled", SpigotPrison.format("&8Disabled"));
                conf.set("Gui.Lore.SkipReset1", SpigotPrison.format("&8Skip the reset if"));
                conf.set("Gui.Lore.SkipReset2", SpigotPrison.format("&8not enough blocks"));
                conf.set("Gui.Lore.SkipReset3", SpigotPrison.format("&8have been mined."));
                conf.set("Gui.Lore.Tp", SpigotPrison.format("&8Tp to the mine."));
                conf.set("Gui.Lore.Tag", SpigotPrison.format("&3Tag: &8"));
                conf.set("Gui.Lore.Tag2", SpigotPrison.format("&3Rank Tag: &7"));
                conf.set("Gui.Lore.Time", SpigotPrison.format("&8Time: "));
                conf.set("Gui.Lore.Volume", SpigotPrison.format("&3Volume in Blocks: &7"));
                conf.set("Gui.Lore.World", SpigotPrison.format("&3World: &7"));
                conf.set("Gui.Message.EmptyGui", SpigotPrison.format("&cSorry, but the GUI's &c&lempty&c and doesn't have any reason to exist or open"));
                conf.set("Gui.Message.TooManyBlocks", SpigotPrison.format("&cSorry, but there're too many Blocks and the max's 54 for the GUI"));
                conf.set("Gui.Message.TooManyLadders", SpigotPrison.format("&cSorry, but there're too many ladders and the max's 54 for the GUI"));
                conf.set("Gui.Message.TooManyMines", SpigotPrison.format("&cSorry, but there're too many mines and the max's 54 for the GUI"));
                conf.set("Gui.Message.TooManyRankupCommands", SpigotPrison.format("&cSorry, but there're too many RankupCommands and the max's 54 for the GUI"));
                conf.set("Gui.Message.ZeroBlocksReset1", SpigotPrison.format("&8Set a mine's delay"));
                conf.set("Gui.Message.ZeroBlocksReset2", SpigotPrison.format("&8before reset when it"));
                conf.set("Gui.Message.ZeroBlocksReset3", SpigotPrison.format("&8reaches zero blocks."));
                conf.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        conf = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileGuiConfig(){
        return conf;
    }

}

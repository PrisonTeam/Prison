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

        String[] path = new String[96];
        path[0] = "Options.Ranks.GUI_Enabled";
        path[1] = "Options.Ranks.Permission_GUI_Enabled";
        path[2] = "Options.Ranks.Permission_GUI";
        path[3] = "Options.Mines.GUI_Enabled";
        path[4] = "Options.Mines.Permission_GUI_Enabled";
        path[5] = "Options.Mines.Permission_GUI";
        path[6] = "Options.Ranks.Ladder";
        path[7] = "Options.Ranks.Item_gotten_rank";
        path[8] = "Options.Ranks.Item_not_gotten_rank";
        path[9] = "Options.Ranks.Enchantment_effect_current_rank";
        path[10] = "Options.Mines.PermissionWarpPlugin";
        path[11] = "Options.Mines.CommandWarpPlugin";
        path[12] = "Gui.Lore.ActivateWithinMode";
        path[13] = "Gui.Lore.ActivateRadiusMode";
        path[14] = "Gui.Lore.AutoPickupGuiManager";
        path[15] = "Gui.Lore.AutoSmeltGuiManager";
        path[16] = "Gui.Lore.AutoBlockGuiManager";
        path[17] = "Gui.Lore.BlockType";
        path[18] = "Gui.Lore.Blocks";
        path[19] = "Gui.Lore.Blocks2";
        path[20] = "Gui.Lore.ClickToChoose";
        path[21] = "Gui.Lore.ClickToConfirm";
        path[22] = "Gui.Lore.ClickToCancel";
        path[23] = "Gui.Lore.ClickToDecrease";
        path[24] = "Gui.Lore.ClickToIncrease";
        path[25] = "Gui.Lore.ClickToManageRank";
        path[26] = "Gui.Lore.ClickToManageCommands";
        path[27] = "Gui.Lore.ClickToOpen";
        path[28] = "Gui.Lore.ClickToTeleport";
        path[29] = "Gui.Lore.ClickToUse";
        path[30] = "Gui.Lore.ClickToRankup";
        path[31] = "Gui.Lore.Chance";
        path[32] = "Gui.Lore.Command";
        path[33] = "Gui.Lore.ContainsTheRank";
        path[34] = "Gui.Lore.ContainsNoCommands";
        path[35] = "Gui.Lore.DisableNotifications";
        path[36] = "Gui.Lore.EnabledAll";
        path[37] = "Gui.Lore.DisabledAll";
        path[38] = "Gui.Lore.FullSoundEnabled";
        path[39] = "Gui.Lore.FullSoundDisabled";
        path[40] = "Gui.Lore.FullHologramEnabled";
        path[41] = "Gui.Lore.FullHologramDisabled";
        path[42] = "Gui.Lore.Id";
        path[43] = "Gui.Lore.Info";
        path[44] = "Gui.Lore.IfYouHaveEnoughMoney";
        path[45] = "Gui.Lore.LadderThereAre";
        path[46] = "Gui.Lore.LadderCommands";
        path[47] = "Gui.Lore.LeftClickToConfirm";
        path[48] = "Gui.Lore.LeftClickToOpen";
        path[49] = "Gui.Lore.LeftClickToReset";
        path[50] = "Gui.Lore.ManageResetTime";
        path[51] = "Gui.Lore.MinesButton";
        path[52] = "Gui.Lore.Name";
        path[53] = "Gui.Lore.Notifications";
        path[54] = "Gui.Lore.PlayersWithTheRank";
        path[55] = "Gui.Lore.Price";
        path[56] = "Gui.Lore.Price2";
        path[57] = "Gui.Lore.Price3";
        path[58] = "Gui.Lore.PrisonTasksButton";
        path[59] = "Gui.Lore.ResetTime";
        path[60] = "Gui.Lore.Radius";
        path[61] = "Gui.Lore.RankupCommands";
        path[62] = "Gui.Lore.Rankup";
        path[63] = "Gui.Lore.RanksButton";
        path[64] = "Gui.Lore.ResetButton";
        path[65] = "Gui.Lore.RightClickToCancel";
        path[66] = "Gui.Lore.RightClickToEnable";
        path[67] = "Gui.Lore.RightClickToToggle";
        path[68] = "Gui.Lore.SpawnPoint";
        path[69] = "Gui.Lore.StatusLockedMine";
        path[70] = "Gui.Lore.StatusUnlockedMine";
        path[71] = "Gui.Lore.SpawnPoint2";
        path[72] = "Gui.Lore.SizeOfMine";
        path[73] = "Gui.Lore.Selected";
        path[74] = "Gui.Lore.ShiftAndRightClickToDelete";
        path[75] = "Gui.Lore.ShiftAndRightClickToDisable";
        path[76] = "Gui.Lore.ShiftAndRightClickToToggle";
        path[77] = "Gui.Lore.StatusEnabled";
        path[78] = "Gui.Lore.StatusDisabled";
        path[79] = "Gui.Lore.SkipReset1";
        path[80] = "Gui.Lore.SkipReset2";
        path[81] = "Gui.Lore.SkipReset3";
        path[82] = "Gui.Lore.Tp";
        path[83] = "Gui.Lore.Tag";
        path[84] = "Gui.Lore.Tag2";
        path[85] = "Gui.Lore.Time";
        path[86] = "Gui.Lore.Volume";
        path[87] = "Gui.Lore.World";
        path[88] = "Gui.Message.EmptyGui";
        path[89] = "Gui.Message.TooManyBlocks";
        path[90] = "Gui.Message.TooManyLadders";
        path[91] = "Gui.Message.TooManyMines";
        path[92] = "Gui.Message.TooManyRankupCommands";
        path[93] = "Gui.Message.ZeroBlocksReset1";
        path[94] = "Gui.Message.ZeroBlocksReset2";
        path[95] = "Gui.Message.ZeroBlocksReset3";

        String[] object = new String[96];
        object[0] = "true";
        object[1] = "false";
        object[2] = "prison.gui.ranks";
        object[3] = "true";
        object[4] = "false";
        object[5] = "prison.gui.mines";
        object[6] = "default";
        object[7] = "TRIPWIRE_HOOK";
        object[8] = "REDSTONE_BLOCK";
        object[9] = "true";
        object[10] = "mines.tp.";
        object[11] = "mines tp";
        object[12] = "&8Activate Within mode.";
        object[13] = "&8Activate Radius mode.";
        object[14] = "&8AutoPickup GUI manager.";
        object[15] = "&8AutoSmelt GUI manager.";
        object[16] = "&8AutoBlock GUI manager.";
        object[17] = "&3BlockType: ";
        object[18] = "&3Blocks:";
        object[19] = "&8Manage the blocks of the Mine.";
        object[20] = "&8Click to choose.";
        object[21] = "&8Click to confirm.";
        object[22] = "&8Click to cancel.";
        object[23] = "&8Click to decrease.";
        object[24] = "&8Click to increase.";
        object[25] = "&8Click to manage the rank.";
        object[26] = "&8Click to manage RankUPCommands.";
        object[27] = "&8Click to open.";
        object[28] = "&8Click to teleport.";
        object[29] = "&8Click to use.";
        object[30] = "&8Click to rankup";
        object[31] = "&3Chance: ";
        object[32] = "&3Command: &7";
        object[33] = "&3The Rank ";
        object[34] = " contains no commands.";
        object[35] = "&8Disable notifications.";
        object[36] = "&aAll features enabled";
        object[37] = "&aAll features disabled";
        object[38] = "&aFull Inventory, notify with sound enabled";
        object[39] = "&cFull Inventory, notify with sound disabled";
        object[40] = "&aFull Inventory, notify with hologram enabled";
        object[41] = "&cFull Inventory, notify with hologram disabled";
        object[42] = "&3Rank id: &7";
        object[43] = "&8&l|&3Info&8|";
        object[44] = "&8If you have enough money";
        object[45] = "&8There're &3";
        object[46] = " &3Commands &8in this ladder:";
        object[47] = "&aLeft-Click to confirm.";
        object[48] = "&8Left Click to open.";
        object[49] = "&aLeft Click to reset";
        object[50] = "&8Manage the reset time of the Mine.";
        object[51] = "&8Mines GUI manager.";
        object[52] = "&3Rank Name: &7";
        object[53] = "&8Change Mines notifications.";
        object[54] = "&3Players with this rank: &7";
        object[55] = "&3Price: &a$";
        object[56] = "&8Price: &a$";
        object[57] = "&3Rank Price: &a$";
        object[58] = "&8Prison Tasks GUI manager.";
        object[59] = "&3Reset time in seconds: &7";
        object[60] = "&8Radius: ";
        object[61] = "&8&l|&3RankUPCommands&8| &8&l- &3";
        object[62] = "&aRankup";
        object[63] = "&8Ranks GUI manager.";
        object[64] = "&8Resets the mine.";
        object[65] = "&cRight-Click to cancel.";
        object[66] = "&aRight click to enable";
        object[67] = "&cRight click to toggle";
        object[68] = "&3Spawnpoint: &7";
        object[69] = "&8Status: &cLocked";
        object[70] = "&8Status: &aUnlocked";
        object[71] = "&8Set the mine spawn point at your location.";
        object[72] = "&3Size of Mine: &7";
        object[73] = "&3Selected";
        object[74] = "&cPress Shift + Right click to delete.";
        object[75] = "&cPress Shift + Right click to disable";
        object[76] = "&cPress Shift + Right click to toggle";
        object[77] = "&8Enabled";
        object[78] = "&8Disabled";
        object[79] = "&8Skip the reset if ";
        object[80] = "&8not enough blocks ";
        object[81] = "&8have been mined.";
        object[82] = "&8Tp to the mine.";
        object[83] = "&3Tag: &8";
        object[84] = "&3Rank Tag: &7";
        object[85] = "&8Time: ";
        object[86] = "&3Volume in Blocks: &7";
        object[87] = "&3World: &7";
        object[88] = "&cSorry, but the GUI's &c&lempty&c and doesn't have any reason to exist or open";
        object[89] = "&cSorry, but there're too many Blocks and the max's 54 for the GUI";
        object[90] = "&cSorry, but there're too many ladders and the max's 54 for the GUI";
        object[91] = "&cSorry, but there're too many mines and the max's 54 for the GUI";
        object[92] = "&cSorry, but there're too many RankupCommands and the max's 54 for the GUI";
        object[93] = "&8Set a mine's delay ";
        object[94] = "&8before reset when it ";
        object[95] = "&8reaches zero blocks.";

        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");

        if(!file.exists()){
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
        } else {
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

        conf = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFileGuiConfig(){
        return conf;
    }

}

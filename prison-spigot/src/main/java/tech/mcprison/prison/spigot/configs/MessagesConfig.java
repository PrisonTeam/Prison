package tech.mcprison.prison.spigot.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 **/

public class MessagesConfig extends SpigotConfigComponents{

    // Initialize parameters and variables
    private FileConfiguration conf;
    private int changeCount = 0;

    public MessagesConfig() {
        initialize();
    }

    public void initialize() {

    	// Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/module_conf/lang/" + SpigotPrison.getInstance().getConfig().getString("default-language") + ".yml");

        // Check if the config exists
        fileMaker(file);

        // Get the config
    	conf = YamlConfiguration.loadConfiguration(file);
    	
        // Call method
        values();

        if (changeCount > 0) {
        	try {
				conf.save(file);
				Output.get().logInfo("&aThere were &b%d &anew values added for the language files " + "used by the GuiConfig.yml file located at &b%s", changeCount, file.getAbsoluteFile());
			}
			catch (IOException e) {
				Output.get().logInfo("&4Failed to save &b%d &4new values for the language files " + "used by the GuiConfig.yml file located at &b%s&4. " + "&a %s", changeCount, file.getAbsoluteFile(), e.getMessage());
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

    // All the strings should be here
    private void values(){

        String prisonTag = conf.getString("Tag.PrisonTag");
        if (prisonTag == null) {
            prisonTag = "&7[&3PRISON&7] &3";
        }

        String prisonWarnTag = conf.getString("Tag.PrisonWarnTag");
        if (prisonWarnTag == null) {
            prisonWarnTag = "&7[&3PRISON WARN&7] &3";
        }

        String prisonErrorTag = conf.getString("Tag.PrisonErrorTag");
        if (prisonErrorTag == null) {
            prisonErrorTag = "&7[&3PRISON ERROR&7] &3";
        }

        dataConfig("Tag.PrisonTag", prisonTag);
        dataConfig("Tag.PrisonWarnTag", prisonWarnTag);
        dataConfig("Tag.PrisonErrorTag", prisonErrorTag);

        dataConfig("Lore.ActivateWithinMode","&8Activate Within mode.");
        dataConfig("Lore.ActivateRadiusMode","&8Activate Radius mode.");
        dataConfig("Lore.AutoPickupGuiManager","&8AutoPickup Manager.");
        dataConfig("Lore.AutoSmeltGuiManager","&8AutoSmelt Manager.");
        dataConfig("Lore.AutoBlockGuiManager","&8AutoBlock Manager.");
        dataConfig("Lore.BlockType","&3BlockType: ");
        dataConfig("Lore.Blocks","&3Blocks:");
        dataConfig("Lore.Blocks2","&8Manage blocks of the Mine.");
        dataConfig("Lore.ClickToChoose","&8Click to choose.");
        dataConfig("Lore.ClickToConfirm","&8Click to confirm.");
        dataConfig("Lore.ClickToCancel","&8Click to cancel.");
        dataConfig("Lore.ClickToDecrease","&8Click to decrease.");
        dataConfig("Lore.ClickToIncrease","&8Click to increase.");
        dataConfig("Lore.ClickToManageRank","&8Manage this rank.");
        dataConfig("Lore.ClickToManageCommands","&8Manage RankUPCommands.");
        dataConfig("Lore.ClickToOpen","&8Click to open.");
        dataConfig("Lore.ClickToRename", "&8Click to rename.");
        dataConfig("Lore.ClickToTeleport","&8Click to teleport.");
        dataConfig("Lore.ClickToUse","&8Click to use.");
        dataConfig("Lore.ClickToRankup","&8Click to rankup");
        dataConfig("Lore.ClickToEditBlock", "&8Click to edit percentage.");
        dataConfig("Lore.ClickToClose", "&cClick to close the GUI.");
        dataConfig("Lore.ClickToPriorPage", "&aClick to see the prior page.");
        dataConfig("Lore.ClickToNextPage", "&aClick to see the next page.");
        dataConfig("Lore.ClickToStartBlockSetup", "&aClick to setup block.");
        dataConfig("Lore.ClickToAddBlock", "&aClick to add a block.");
        dataConfig("Lore.ClickToEnable", "&aClick to enable.");
        dataConfig("Lore.ClickToDisable", "&cClick to disable.");
        dataConfig("Lore.Chance","&3Chance: ");
        dataConfig("Lore.Command","&3Command: &7");
        dataConfig("Lore.ContainsTheRank","&3The Rank ");
        dataConfig("Lore.ContainsNoCommands"," &3contains no commands.");
        dataConfig("Lore.DelaySellAll", "&3Delay: &8");
        dataConfig("Lore.DisableNotifications","&8Disable notifications.");
        dataConfig("Lore.EnabledAll","&aAll features ON");
        dataConfig("Lore.DisabledAll","&aAll features OFF");
        dataConfig("Lore.FullSoundEnabled","&aFull Inv., notify with sound ON");
        dataConfig("Lore.FullSoundDisabled","&cFull Inv., notify with sound OFF");
        dataConfig("Lore.FullHologramEnabled","&aFull Inv., notify with hologram ON");
        dataConfig("Lore.FullHologramDisabled","&cFull Inv., notify with hologram OFF");
        dataConfig("Lore.Id","&3Rank id: &7");
        dataConfig("Lore.Info","&8&l|&3Info&8|");
        dataConfig("Lore.IfYouHaveEnoughMoney","&8If you have enough money");
        dataConfig("Lore.LadderThereAre","&8There're &3");
        dataConfig("Lore.LadderCommands"," &3Commands at ladder:");
        dataConfig("Lore.LeftClickToConfirm","&aLeft-Click to confirm.");
        dataConfig("Lore.LeftClickToOpen","&8Left Click to open.");
        dataConfig("Lore.LeftClickToReset","&aLeft Click to reset");
        dataConfig("Lore.LeftClickToEdit", "&aLeft-Click to edit value");
        dataConfig("Lore.ManageResetTime","&8Manage the reset time of Mine.");
        dataConfig("Lore.MinesButton","&8Mines GUI manager.");
        dataConfig("Lore.MineName", "&3Mine Name: &f");
        dataConfig("Lore.Name","&3Rank Name: &7");
        dataConfig("Lore.Notifications","&8Edit Mines notifications.");
        dataConfig("Lore.PlayersWithTheRank","&3Players at rank: &7");
        dataConfig("Lore.PrestigeWarning", "&3Prestige will reset: ");
        dataConfig("Lore.PrestigeWarning2", "&3 - &bRank");
        dataConfig("Lore.PrestigeWarning3", "&3 - &bBalance");
        dataConfig("Lore.Price","&3Price: &a$");
        dataConfig("Lore.Price2","&8Price: &a$");
        dataConfig("Lore.Price3","&3Rank Price: &a$");
        dataConfig("Lore.Percentage", "&8Percentage: ");
        dataConfig("Lore.PrisonTasksButton","&8Prison Tasks Manager.");
        dataConfig("Lore.ResetTime","&3Reset time(s): &7");
        dataConfig("Lore.Radius","&8Radius: ");
        dataConfig("Lore.RankupCommands","&8&l|&3RankUPCommands&8| &8&l- &3");
        dataConfig("Lore.Rankup","&aRankup");
        dataConfig("Lore.RanksButton","&8Ranks GUI manager.");
        dataConfig("Lore.ResetButton","&8Resets the mine.");
        dataConfig("Lore.RightClickToCancel","&cRight-Click to cancel.");
        dataConfig("Lore.RightClickToEnable","&aRight-Click to enable.");
        dataConfig("Lore.RightClickToDisable", "&cRight-Click to disable.");
        dataConfig("Lore.RightClickToToggle","&cRight click to toggle.");
        dataConfig("Lore.RightClickToDelete", "&cRight-Click to delete.");
        dataConfig("Lore.SpawnPoint","&3Spawnpoint: &7");
        dataConfig("Lore.StatusLockedMine","&8Status: &cLocked");
        dataConfig("Lore.StatusUnlockedMine","&8Status: &aUnlocked");
        dataConfig("Lore.SpawnPoint2","&8Set the mine spawn point.");
        dataConfig("Lore.SizeOfMine","&3Size of Mine: &7");
        dataConfig("Lore.Selected","&3Selected");
        dataConfig("Lore.SellAllDelayInfo", "&8Short delay before using again");
        dataConfig("Lore.SellAllDelayInfo2", "&8the &3/sellall sell &8command.");
        dataConfig("Lore.ShiftAndRightClickToDelete","&cShift + Right click to delete.");
        dataConfig("Lore.ShiftAndRightClickToDisable","&cShift + Right click to disable");
        dataConfig("Lore.ShiftAndRightClickToToggle","&cShift + Right click to toggle");
        dataConfig("Lore.StatusEnabled","&8Enabled");
        dataConfig("Lore.StatusDisabled","&8Disabled");
        dataConfig("Lore.SkipReset1","&8Skip the reset if ");
        dataConfig("Lore.SkipReset2","&8not enough blocks ");
        dataConfig("Lore.SkipReset3","&8have been mined.");
        dataConfig("Lore.Tp","&8Tp to the mine.");
        dataConfig("Lore.Tag","&3Tag: &8");
        dataConfig("Lore.Tag2","&3Rank Tag: &7");
        dataConfig("Lore.Time","&8Time: ");
        dataConfig("Lore.Volume","&3Volume: &7");
        dataConfig("Lore.Value", "&3Value: &a$");
        dataConfig("Lore.World","&3World: &7");
        dataConfig("Lore.noRanksFoundSetup", "&3There aren't Ranks!");
        dataConfig("Lore.noRanksFoundSetup2", "&3If you want continue the setup.");
        dataConfig("Lore.noRanksFoundSetup3", "&3All Ranks and Mines from A to Z will be made");
        dataConfig("Lore.noRanksFoundSetup4", "&3With &adefault &3values!");
        dataConfig("Lore.noRanksFoundSetup5", "&3You can do the same by command:");
        dataConfig("Lore.noRanksFoundSetup6", "&1/ranks autoConfigure full <price=x> <mult=x>!");
        dataConfig("Lore.noRanksFoundSetup7", "&3Please replace the X with the starting price and");
        dataConfig("Lore.noRanksFoundSetup8", "&3multiplier, default price = 50000, multiplier = 1.5.");
        dataConfig("Lore.ZeroBlocksReset1","&8Set a mine's delay ");
        dataConfig("Lore.ZeroBlocksReset2","&8before reset when it ");
        dataConfig("Lore.ZeroBlocksReset3","&8reaches zero blocks.");
        dataConfig("Message.CantGetRanksAdmin",  prisonWarnTag + "&3Can't get Ranks, there might be &cno ranks&3 or the Ranks module's &cdisabled&3.");
        dataConfig("Message.CantRunGUIFromConsole", prisonTag + "&3You cannot run the GUI from the console.");
        dataConfig("Message.DefaultLadderEmpty", prisonErrorTag + "&3The default ladder is &cempty&3.");
        dataConfig("Message.NoSellAllItems", prisonWarnTag + "&3Sorry but &cthere aren't&3 SellAll Items to show.");
        dataConfig("Message.EmptyGui", prisonWarnTag + "&3Sorry, the GUI's &cempty.");
        dataConfig("Message.EnableAutoSellToUse", prisonWarnTag + "&3Sorry, but AutoSell's &cdisabled&3, please enable it!");
        dataConfig("Message.EnableSellDelayToUse", prisonWarnTag + "Sorry, but the SellAll Delay's &cdisabled&3, please enable it!");
        dataConfig("Message.InvalidBooleanInput", prisonErrorTag + "Sorry, you should type &a-true-&3 or &c-false-&3 here.");
        dataConfig("Message.MissingPermission", prisonWarnTag + "Sorry but you don't have the &cpermission&3 to use that!");
        dataConfig("Message.NoBlocksMine", prisonWarnTag + "Sorry but this Mine's &cempty&3.");
        dataConfig("Message.NoMines", prisonWarnTag + "Sorry but &cthere aren't &3Mines to show.");
        dataConfig("Message.NoRankupCommands", prisonWarnTag + "Sorry, but there &caren't rankUpCommands&3 for this Rank!");
        dataConfig("Message.NoLadders", prisonWarnTag + "Sorry but &cthere aren't &3ladders to show.");
        dataConfig("Message.NoRanksPrestigesLadder", prisonWarnTag + "There &caren't ranks&3 in the -prestiges- ladder!");
        dataConfig("Message.NoRanksFoundAdmin", prisonWarnTag + "Sorry, the Ladder's &cempty&3!");
        dataConfig("Message.NoRanksFound", prisonWarnTag + "Sorry, but this Ladder's &cempty&3!");
        dataConfig("Message.NoRanksFoundHelp1", prisonWarnTag + "Sorry, the Ladder's &cempty&3 or &3[");
        dataConfig("Message.NoRanksFoundHelp2", "] &cdoesn't exists!");
        dataConfig("Message.LadderPrestigesNotFound", prisonWarnTag + "Ladder -prestiges- &cnot found&3!");
        dataConfig("Message.TooManyBlocks", prisonWarnTag + "Sorry, but there're &ctoo many&3 Blocks and the max's 54 for the GUI");
        dataConfig("Message.TooManyLadders", prisonWarnTag + "Sorry, but there're &ctoo many&3 Ladders and the max's 54 for the GUI");
        dataConfig("Message.TooManyMines", prisonWarnTag + "Sorry, but there're &ctoo many&3 Mines and the max's 54 for the GUI");
        dataConfig("Message.TooManyRankupCommands", prisonWarnTag + "Sorry, but there're &ctoo many&3 RankupCommands and the max's 54 for the GUI");
        dataConfig("Message.TooManyRanks", prisonWarnTag + "Sorry, but there're &ctoo many&3 Ranks and the max's 54 for the GUI");
        dataConfig("Message.TooManySellAllItems", prisonWarnTag + "There are &ctoo many&3 Items and the MAX for the GUI's 54!");
        dataConfig("Message.mineNameRename", prisonTag + "Please write the &6mineName &3you'd like to use and &6submit&3.");
        dataConfig("Message.mineNameRenameClose", prisonTag + "Input &cclose &3to cancel or wait &c30 seconds&3.");
        dataConfig("Message.mineNameRenameClosed", prisonTag + "Rename Mine &cclosed&3, nothing got changed!");
        dataConfig("Message.mineOrGuiDisabled", prisonTag + "GUI and/or GUI Mines is &cdisabled&3. Check GuiConfig.yml.");
        dataConfig("Message.mineMissingGuiPermission", prisonTag + "You lack the &cpermissions&3 to use GUI Mines");
        dataConfig("Message.OutOfTimeNoChanges", prisonTag + "You ran out of time, &cnothing changed&3.");
        dataConfig("Message.PrestigeCancelled", prisonTag + "Prestige &ccancelled&3!");
        dataConfig("Message.PrestigeCancelledWrongKeyword", prisonTag + "Prestige &ccancelled&3, you didn't type the word: &aconfirm&3.");
        dataConfig("Message.PrestigeRanOutOfTime", prisonTag + "You ran out of time, &cPrestige cancelled&3.");
        dataConfig("Message.PrestigesDisabledDefault", prisonTag + "Prestiges are &cdisabled&3 by default, please edit it in your config.yml!");
        dataConfig("Message.ConfirmPrestige", prisonTag + "&aConfirm&3: Type the word &aconfirm&3 to confirm");
        dataConfig("Message.CancelPrestige", prisonTag + "&cCancel&3: Type the word &ccancel&3 to cancel, &cyou've 30 seconds.");
        dataConfig("Message.PrestigesAreDisabled", prisonTag + "Prestiges are &cdisabled&3. Check config.yml.");
        dataConfig("Message.GuiOrPrestigesDisabled", prisonTag + "GUI and/or GUI Prestiges is &cdisabled&3. Check GuiConfig.yml.");
        dataConfig("Message.CantFindPrestiges", prisonErrorTag + "The prestige ladder has &cno prestiges&3!");
        dataConfig("Message.missingGuiPrestigesPermission", prisonTag + "You lack the &cpermissions&3 to use GUI prestiges");
        dataConfig("Message.rankTagRename", prisonTag + "Please write the &6tag &3you'd like to use and &6submit&3.");
        dataConfig("Message.rankTagRenameClose", prisonTag + "Input &cclose &3to cancel or wait &c30 seconds&3.");
        dataConfig("Message.rankTagRenameClosed", prisonTag + "Rename tag &cclosed&3, nothing got changed!");
        dataConfig("Message.rankGuiDisabledOrAllGuiDisabled", prisonTag + "GUI and/or GUI Ranks is &cdisabled&3. Check GuiConfig.yml (%s %s)");
        dataConfig("Message.rankGuiMissingPermission", prisonTag + "You lack the &cpermissions&3 to use the Ranks GUI.");
        dataConfig("Message.SellAllAutoSellMissingPermission", prisonErrorTag + "You don't have the &cpermission&3 to edit AutoSell.");
        dataConfig("Message.SellAllAutoSellEnabled", prisonTag + "Autosell has been &aenabled&3.");
        dataConfig("Message.SellAllAutoSellDisabled", prisonTag + "Autosell has been &cdisabled&3.");
        dataConfig("Message.SellAllAutoSellAlreadyEnabled", prisonErrorTag + "AutoSell has already been &aenabled&3!");
        dataConfig("Message.SellAllAutoSellAlreadyDisabled", prisonErrorTag + "AutoSell has already been &cdisabled&3!");
        dataConfig("Message.SellAllAutoPerUserToggleableAlreadyEnabled", prisonErrorTag + "AutoSell perUserToggleable's already &aenabled&3!");
        dataConfig("Message.SellAllAutoPerUserToggleableAlreadyDisabled", prisonErrorTag + "AutoSell perUserToggleable's already &cdisabled&3!");
        dataConfig("Message.SellAllAutoPerUserToggleableEnabled", prisonTag + "SellAll PerUserToggleable &aEnabled&3!");
        dataConfig("Message.SellAllAutoPerUserToggleableDisabled", prisonTag + "SellAll PerUserToggleable &cDisabled&3!");
        dataConfig("Message.SellAllCurrencyEditedSuccess", prisonTag + "SellAll Currency edited with success!");
        dataConfig("Message.SellAllIsDisabled", prisonErrorTag + "Sorry but the SellAll Feature's &cdisabled&3 in the config.yml");
        dataConfig("Message.SellAllEditedWithSuccess", "&3] edited with &asuccess&3!");
        dataConfig("Message.SellAllSubCommandNotFound", prisonWarnTag + "Sub-command &cnot found&3, please use /sellall help for help!");
        dataConfig("Message.SellAllMultipliersAreDisabled", prisonWarnTag + "Multipliers are &cdisabled&3 in the SellAll config!");
        dataConfig("Message.SellAllMultiplierWrongFormat", prisonWarnTag + "&cWrong format&3, please use /sellall multiplier add/delete <Prestige> <Multiplier>");
        dataConfig("Message.SellAllMissingPermission", prisonWarnTag + "Sorry, but you don't have the &cpermission&3 [");
        dataConfig("Message.SellAllMissingPermissionToToggleAutoSell", prisonWarnTag + "Sorry but you're missing the &cpermission&3 to use that! ");
        dataConfig("Message.SellAllRanksDisabled", prisonErrorTag + "The Ranks module's &cdisabled&3 or not found!");
        dataConfig("Message.SellAllPrestigeLadderNotFound", prisonWarnTag + "&cCan't find&3 the Prestiges Ladder, they might be &cdisabled&3 in the config.yml!");
        dataConfig("Message.SellAllCantFindPrestigeOrRank", prisonWarnTag + "&cCan't find&3 the Prestige/Rank: ");
        dataConfig("Message.SellAllRankNotFoundInPrestigeLadder", prisonWarnTag + "The -prestiges- ladder &cdoesn't contain&3 the Rank: ");
        dataConfig("Message.SellAllMultiplierNotANumber", prisonWarnTag + "Sorry but the multiplier &cisn't a number&3!");
        dataConfig("Message.SellAllMultiplierNotNumber2", " Here-> ");
        dataConfig("Message.SellAllConfigSaveFail", prisonErrorTag + "Sorry, &csomething went wrong&3 while saving the config!");
        dataConfig("Message.SellAllMultiplierEditSaveSuccess", prisonTag + "Multiplier got added or edited with &asuccess&3!");
        dataConfig("Message.SellAllMultiplierFormat", prisonWarnTag + "Please use this format: /sellall multiplier delete <Prestige>.");
        dataConfig("Message.SellAllCantFindMultiplier", prisonWarnTag + "&cCan't find&3 the Multiplier of the prestige ");
        dataConfig("Message.SellAllCantFindMultiplier2", " &3in the sellallconfig.yml");
        dataConfig("Message.SellAllMultiplierDeleteSuccess", prisonWarnTag + "Multiplier &cdeleted&3 with &asuccess&3!");
        dataConfig("Message.SellAllWrongFormatCommand", prisonWarnTag + "&cWrong format&3, use /sellall help for help.");
        dataConfig("Message.SellAllPleaseAddItem", prisonWarnTag + "Please &aadd&3 an ITEM_ID [example: /sellall add COAL_ORE <price>]");
        dataConfig("Message.SellAllAddPrice", prisonWarnTag + "Please &aadd&3 a price or value for the item [example: /sellall add COAL_ORE 100]");
        dataConfig("Message.SellAllWrongID", prisonWarnTag + "Sorry but the &cITEM_ID's wrong&3, please check it!");
        dataConfig("Message.SellAllValueNotNumber", prisonWarnTag + "Sorry but the value &cisn't a number&3!");
        dataConfig("Message.SellAllMissingID", prisonWarnTag + "Please &aadd&3 an ITEM_ID [example: /sellall delete COAL_ORE]");
        dataConfig("Message.SellAllNotFoundStringConfig", " not found in the config or got already &cdeleted&3.");
        dataConfig("Message.SellAllDeletedSuccess", " &cDeleted&3 with &asuccess&3!");
        dataConfig("Message.SellAllAddSuccess", "] &aadded&3 with &asuccess&3!");
        dataConfig("Message.SellAllCommandEditSuccess", "] &aedited&3 with &asuccess&3!");
        dataConfig("Message.SellAllYouArentPlayer", prisonErrorTag + "You &caren't&a a player");
        dataConfig("Message.SellAllNothingToSell", prisonTag + "You have &cnothing&3 to sell!");
        dataConfig("Message.SellAllYouGotMoney", prisonTag + "You got &a$");
        dataConfig("Message.SellAllGUIDisabled", prisonErrorTag + "&7[&3PRISON ERROR&7] &aSorry but the GUI's &cdisabled&3 in the SellAllConfig.yml.");
        dataConfig("Message.SellAllAutoSell", prisonTag + "Your inventory's full, &aAutoSell activated&3!");
        dataConfig("Message.SellAllSignNotify", prisonTag + "Using &aSellAll&3 from a Sign with &asuccess&3!");
        dataConfig("Message.SellAllEmpty", prisonErrorTag + "&7[&3PRISON ERROR&7] &cThere aren't&3 items in the sellall config,\n please add one and/or restart the server!");
        dataConfig("Message.SellAllAutoEnabled", prisonTag + "SellAll-Auto &aenabled &3with &asuccess&3!");
        dataConfig("Message.SellAllAutoDisabled", prisonTag + "SellAll-Auto &cdisabled &3with &asuccess&3!");
        dataConfig("Message.SellAllWaitDelay", prisonTag + "Please &cwait&3 before using this command again!");
        dataConfig("Message.SellAllDelayAlreadyEnabled", prisonTag + "SellAll Delay already &aenabled&3!");
        dataConfig("Message.SellAllDelayAlreadyDisabled", prisonTag + "SellAll Delay already &cdisabled&3!");
        dataConfig("Message.SellAllDelayEnabled", prisonTag + "&7[&3PRISON] &3SellAll Delay &aenabled &3with &asuccess&3!");
        dataConfig("Message.SellAllDelayDisabled", prisonTag + "SellAll Delay &cdisabled &3with &asuccess&3!");
        dataConfig("Message.SellAllDelayEditedWithSuccess", prisonTag + "SellAll Delay edited with &asuccess&3!");
        dataConfig("Message.SellAllDelayNotNumber", prisonTag + "Delay value &cisn't&3 a number!");
        dataConfig("Message.SellAllGUIEmpty", prisonTag + "Sorry but there &caren't&3 Blocks in the SellAll Shop!");
        dataConfig("Message.SellAllGUIEmpty2", "&7[&cTIP&7] &3You can &aadd&3 one using the command /sellall add!");
        dataConfig("Setup.Message.MissingPermission", prisonErrorTag + "Sorry but you don't have the &cpermission&3 [-prison.setup- or -prison.admin-]!");
        dataConfig("Setup.Message.WrongFormat", prisonErrorTag + "You're &cmissing&3 the last argument -mines- or -ranks-, /<command> setup -mines- or -ranks- !");
        dataConfig("Setup.Message.WelcomeToRanksSetup", prisonTag + "Hi and welcome to the Ranks Setup, please &cwait&3 until it'll be completed!");
        dataConfig("Setup.Message.SuccessRanksSetup", prisonTag + "The Ranks Setup got &acompleted&3 with &asuccess&3 and the Ranks got &aadded&3 to the default ladder, please check the logs if something's missing!");
        dataConfig("Setup.Message.Aborted", prisonTag + "Prison Setup &cCancelled&3.");
    }

    public FileConfiguration getFileGuiMessagesConfig(){
        return conf;
    }
}

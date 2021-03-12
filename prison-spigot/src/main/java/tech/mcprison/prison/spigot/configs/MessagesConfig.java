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

        String originalString = conf.getString(key);

        if (originalString == null) {
            conf.set(key, value);
            changeCount++;
        }
    }

    // All the strings should be here
    private void values(){
        dataConfig("Lore.ActivateWithinMode","&8Activate Within mode.");
        dataConfig("Lore.ActivateRadiusMode","&8Activate Radius mode.");
        dataConfig("Lore.AutoPickupGuiManager","&8AutoPickup Manager.");
        dataConfig("Lore.AutoSmeltGuiManager","&8AutoSmelt Manager.");
        dataConfig("Lore.AutoBlockGuiManager","&8AutoBlock Manager.");
        dataConfig("Lore.BlockType","&3BlockType: ");
        dataConfig("Lore.Blocks","&3Blocks:");
        dataConfig("Lore.Blocks2","&8Manage blocks of the Mine.");
        dataConfig("Lore.ClickToAddBackpack", "&8Click to add backpack.");
        dataConfig("Lore.ClickToAddBackpackInst0", "&3Instructions: ");
        dataConfig("Lore.ClickToAddBackpackInst1", "&8Remember to add an item");
        dataConfig("Lore.ClickToAddBackpackInst2", "&8Inside the backpack to");
        dataConfig("Lore.ClickToAddBackpackInst3", "&8Save it");
        dataConfig("Lore.ClickToChoose","&8Click to choose.");
        dataConfig("Lore.ClickToConfirm","&8Click to confirm.");
        dataConfig("Lore.ClickToCancel","&8Click to cancel.");
        dataConfig("Lore.ClickToDecrease","&8Click to decrease.");
        dataConfig("Lore.ClickToIncrease","&8Click to increase.");
        dataConfig("Lore.ClickToManageRank","&8Manage this rank.");
        dataConfig("Lore.ClickToManageCommands","&8Manage RankUPCommands.");
        dataConfig("Lore.ClickToOpen","&8Click to open.");
        dataConfig("Lore.ClickToOpenBackpack", "&8Click to open Prison Backpack.");
        dataConfig("Lore.ClickToRename", "&8Click to rename.");
        dataConfig("Lore.ClickToTeleport","&8Click to teleport.");
        dataConfig("Lore.ClickToUse","&8Click to use.");
        dataConfig("Lore.ClickToRankup","&8Click to rankup");
        dataConfig("Lore.ClickToEditBlock", "&8Click to edit percentage.");
        dataConfig("Lore.ClickToEdit", "&8Click to edit.");
        dataConfig("Lore.ClickToClose", "&8Click to close the GUI.");
        dataConfig("Lore.ClickToPriorPage", "&aClick to see the prior page.");
        dataConfig("Lore.ClickToNextPage", "&aClick to see the next page.");
        dataConfig("Lore.ClickToStartBlockSetup", "&aClick to setup block.");
        dataConfig("Lore.ClickToSelect", "&7Click to select.");
        dataConfig("Lore.ClickToAddBlock", "&aClick to add a block.");
        dataConfig("Lore.ClickToEnable", "&aClick to enable.");
        dataConfig("Lore.ClickToDisable", "&cClick to disable.");
        dataConfig("Lore.Chance","&3Chance: ");
        dataConfig("Lore.Command","&3Command: &7");
        dataConfig("Lore.ContainsTheRank","&3The Rank ");
        dataConfig("Lore.ContainsNoCommands"," &3contains no commands.");
        dataConfig("Lore.DelaySellAll", "&3Delay: &8");
        dataConfig("Lore.DisableNotifications","&8Disable notifications.");
        dataConfig("Lore.DisabledAll","&3All features &cOFF&3.");
        dataConfig("Lore.EnabledAll","&aAll features ON");
        dataConfig("Lore.EmptyMultiplier", "&c[!] There aren't multipliers!");
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
        dataConfig("Lore.Multiplier", "&3Multiplier: &f");
        dataConfig("Lore.Name","&3Rank Name: &7");
        dataConfig("Lore.Notifications","&8Edit Mines notifications.");
        dataConfig("Lore.Permission", "&3Permission: ");
        dataConfig("Lore.PlayersWithTheRank","&3Players at rank: &7");
        dataConfig("Lore.PrestigeWarning", "&3Prestige will reset: ");
        dataConfig("Lore.PrestigeWarning2", "&3 - &bRank");
        dataConfig("Lore.PrestigeWarning3", "&3 - &bBalance");
        dataConfig("Lore.PrestigeName", "&8Prestige name: &3");
        dataConfig("Lore.PrestigeMultiplier", "&8Multiplier: &3");
        dataConfig("Lore.PrestigeMultiplierInfoGUI", "&3Manage multipliers.");
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
        dataConfig("Lore.RightClickToEnable","&cRight-Click to &aenable&c.");
        dataConfig("Lore.RightClickToDisable", "&cRight-Click to disable.");
        dataConfig("Lore.RightClickToToggle","&3Right click to toggle.");
        dataConfig("Lore.RightClickToDelete", "&cRight-Click to delete.");
        dataConfig("Lore.RightClickToEdit", "&8Right click to edit.");
        dataConfig("Lore.SpawnPoint","&3Spawnpoint: &7");
        dataConfig("Lore.StatusLockedMine","&8Status: &cLocked");
        dataConfig("Lore.StatusUnlockedMine","&8Status: &aUnlocked");
        dataConfig("Lore.SpawnPoint2","&8Set the mine spawn point.");
        dataConfig("Lore.SizeOfMine","&3Size of Mine: &7");
        dataConfig("Lore.Selected","&3Selected");
        dataConfig("Lore.ShowItem", "&3Show Item: &7");
        dataConfig("Lore.ShowItemDescription", "&7This's the item");
        dataConfig("Lore.ShowItemDescription2", "&7Shown in the Player's GUI");
        dataConfig("Lore.ShowItemDescription3", "&7Or /mines GUI.");
        dataConfig("Lore.SellAllDelayInfo", "&8Short delay before using again");
        dataConfig("Lore.SellAllDelayInfo2", "&8the &3/sellall sell &8command.");
        dataConfig("Lore.SellAllCurrencyInfo", "&3Edit SellAll Currency.");
        dataConfig("Lore.SellAllActiveCurrency", "&3Currency: &a");
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
        dataConfig("Message.BackPackNeedPlayer", "Please add a playername.");
        dataConfig("Message.BackPackDeleteOperationSuccess", "The backpack should've been deleted with success!");
        dataConfig("Message.BackPackDeleteOperationFail", "Can't find the backpack or something went wrong...?");
        dataConfig("Message.BackPackPlayerNotFound", "Player not found.");
        dataConfig("Message.BackPackDoNotOwnAny", "Sorry but you don't own any inventory, please use /backpack <AnIdYouWant> to make one.");
        dataConfig("Message.BackPackOwnLimitReached", "Sorry but you already have the max amount of backpacks allowed for Player!");
        dataConfig("Message.CantGetRanksAdmin", "Can't get Ranks, there might be &cno ranks&7 or the Ranks module's &cdisabled&7.");
        dataConfig("Message.CantRunGUIFromConsole", "You cannot run the GUI from the console.");
        dataConfig("Message.CantGiveItemFromConsole", "You can't get an item as the console.");
        dataConfig("Message.DefaultLadderEmpty", "The default ladder is &cempty&7.");
        dataConfig("Message.NoSellAllItems", "Sorry but &cthere aren't&7 SellAll Items to show.");
        dataConfig("Message.EmptyGui", "Sorry, the GUI's &cempty.");
        dataConfig("Message.EnableAutoSellToUse", "Sorry, but AutoSell's &cdisabled&7, please enable it!");
        dataConfig("Message.EnableSellDelayToUse", "Sorry, but the SellAll Delay's &cdisabled&7, please enable it!");
        dataConfig("Message.EventCancelled", "&cEvent cancelled.");
        dataConfig("Message.InvalidBooleanInput", "Sorry, you should type &a-true-&7 or &c-false-&7 here.");
        dataConfig("Message.MissingPermission", "Sorry but you don't have the &cpermission&7 to use that!");
        dataConfig("Message.NoBlocksMine", "Sorry but this Mine's &cempty&7.");
        dataConfig("Message.NoMines", "Sorry but &cthere aren't &7Mines to show.");
        dataConfig("Message.NoRankupCommands", "Sorry, but there &caren't rankUpCommands&7 for this Rank!");
        dataConfig("Message.NoLadders", "Sorry but &cthere aren't &7ladders to show.");
        dataConfig("Message.NoRanksPrestigesLadder", "There &caren't ranks&7 in the -prestiges- ladder!");
        dataConfig("Message.NoRanksFoundAdmin", "Sorry, the Ladder's &cempty&7!");
        dataConfig("Message.NoRanksFound", "Sorry, but this Ladder's &cempty&7!");
        dataConfig("Message.NoRanksFoundHelp1", "Sorry, the Ladder's &cempty&7 or &7[");
        dataConfig("Message.NoRanksFoundHelp2", "] &cdoesn't exists!");
        dataConfig("Message.LadderPrestigesNotFound", "Ladder -prestiges- &cnot found&7!");
        dataConfig("Message.TooManyBlocks", "Sorry, but there're &ctoo many&7 Blocks and the max's 54 for the GUI");
        dataConfig("Message.TooManyLadders", "Sorry, but there're &ctoo many&7 Ladders and the max's 54 for the GUI");
        dataConfig("Message.TooManyMines", "Sorry, but there're &ctoo many&7 Mines and the max's 54 for the GUI");
        dataConfig("Message.TooManyRankupCommands", "Sorry, but there're &ctoo many&7 RankupCommands and the max's 54 for the GUI");
        dataConfig("Message.TooManyRanks", "Sorry, but there're &ctoo many&7 Ranks and the max's 54 for the GUI");
        dataConfig("Message.TooManySellAllItems", "There are &ctoo many&7 Items and the MAX for the GUI's 54!");
        dataConfig("Message.mineNameRename", "Please write the &6mineName &7you'd like to use and &6submit&7.");
        dataConfig("Message.mineNameRenameClose", "Input &cclose &7to cancel or wait &c30 seconds&7.");
        dataConfig("Message.mineNameRenameClosed", "Rename Mine &cclosed&7, nothing got changed!");
        dataConfig("Message.mineOrGuiDisabled", "GUI and/or GUI Mines is &cdisabled&7. Check GuiConfig.yml.");
        dataConfig("Message.mineMissingGuiPermission", "You lack the &cpermissions&7 to use GUI Mines");
        dataConfig("Message.MineShowItemEditSuccess", "Mine show item edited with success.");
        dataConfig("Message.OutOfTimeNoChanges", "You ran out of time, &cnothing changed&7.");
        dataConfig("Message.PrestigeCancelled", "Prestige &ccancelled&7!");
        dataConfig("Message.PrestigeCancelledWrongKeyword", "Prestige &ccancelled&7, you didn't type the word: &aconfirm&7.");
        dataConfig("Message.PrestigeRanOutOfTime", "You ran out of time, &cPrestige cancelled&7.");
        dataConfig("Message.PrestigesDisabledDefault", "Prestiges are &cdisabled&7 by default, please edit it in your config.yml!");
        dataConfig("Message.ConfirmPrestige", "&aConfirm&7: Type the word &aconfirm&7 to confirm");
        dataConfig("Message.CancelPrestige", "&cCancel&7: Type the word &ccancel&7 to cancel, &cyou've 30 seconds.");
        dataConfig("Message.PrestigesAreDisabled", "Prestiges are &cdisabled&7. Check config.yml.");
        dataConfig("Message.GuiOrPrestigesDisabled", "GUI and/or GUI Prestiges is &cdisabled&7. Check GuiConfig.yml.");
        dataConfig("Message.CantFindPrestiges", "The prestige ladder has &cno prestiges&7!");
        dataConfig("Message.missingGuiPrestigesPermission", "You lack the &cpermissions&7 to use GUI prestiges");
        dataConfig("Message.rankTagRename", "Please write the &6tag &7you'd like to use and &6submit&7.");
        dataConfig("Message.rankTagRenameClose", "Input &cclose &7to cancel or wait &c30 seconds&7.");
        dataConfig("Message.rankTagRenameClosed", "Rename tag &cclosed&7, nothing got changed!");
        dataConfig("Message.rankGuiDisabledOrAllGuiDisabled", "GUI and/or GUI Ranks is &cdisabled&7. Check GuiConfig.yml (%s %s)");
        dataConfig("Message.rankGuiMissingPermission", "You lack the &cpermissions&7 to use the Ranks GUI.");
        dataConfig("Message.SellAllAutoSellMissingPermission", "You don't have the &cpermission&7 to edit AutoSell.");
        dataConfig("Message.SellAllAutoSellEnabled", "Autosell has been &aenabled&7.");
        dataConfig("Message.SellAllAutoSellDisabled", "Autosell has been &cdisabled&7.");
        dataConfig("Message.SellAllAutoSellAlreadyEnabled", "AutoSell has already been &aenabled&7!");
        dataConfig("Message.SellAllAutoSellAlreadyDisabled", "AutoSell has already been &cdisabled&7!");
        dataConfig("Message.SellAllAutoPerUserToggleableAlreadyEnabled", "AutoSell perUserToggleable's already &aenabled&7!");
        dataConfig("Message.SellAllAutoPerUserToggleableAlreadyDisabled", "AutoSell perUserToggleable's already &cdisabled&7!");
        dataConfig("Message.SellAllAutoPerUserToggleableEnabled", "SellAll PerUserToggleable &aEnabled&7!");
        dataConfig("Message.SellAllAutoPerUserToggleableDisabled", "SellAll PerUserToggleable &cDisabled&7!");
        dataConfig("Message.SellAllCurrencyChat1", "&3Started setup of new currency for SellAll!");
        dataConfig("Message.SellAllCurrencyChat2", "Type &ccancel &7to cancel.");
        dataConfig("Message.SellAllCurrencyChat3", "Type &3default &7to set to default currency.");
        dataConfig("Message.SellAllCurrencyChat4", "Type the &aCurrency name &7to set the new currency.");
        dataConfig("Message.SellAllCurrencyEditedSuccess", "SellAll Currency edited with success!");
        dataConfig("Message.SellAllCurrencyEditCancelled", "SellAll edit currency &ccancelled&7.");
        dataConfig("Message.SellAllCurrencyNotFound", "No active economy supports the currency named '%s'.");
        dataConfig("Message.SellAllDefaultSuccess", "Default Values added with &asuccess&7!");
        dataConfig("Message.SellAllDefaultMissingPermission", "Sorry but you're missing the &cpermission!");
        dataConfig("Message.SellAllIsDisabled", "Sorry but the SellAll Feature's &cdisabled&7 in the config.yml");
        dataConfig("Message.SellAllEditedWithSuccess", "&7] edited with &asuccess&7!");
        dataConfig("Message.SellAllSubCommandNotFound", "Sub-command &cnot found&7, please use /sellall help for help!");
        dataConfig("Message.SellAllMultipliersAreDisabled", "Multipliers are &cdisabled&7 in the SellAll config!");
        dataConfig("Message.SellAllMultiplierWrongFormat", "&cWrong format&7, please use /sellall multiplier add/delete <Prestige> <Multiplier>");
        dataConfig("Message.SellAllMissingPermission", "Sorry, but you don't have the &cpermission&7");
        dataConfig("Message.SellAllMissingPermissionToToggleAutoSell", "Sorry but you're missing the &cpermission&7 to use that! ");
        dataConfig("Message.SellAllRanksDisabled", "The Ranks module's &cdisabled&7 or not found!");
        dataConfig("Message.SellAllPrestigeLadderNotFound", "&cCan't find&7 the Prestiges Ladder, they might be &cdisabled&7 in the config.yml!");
        dataConfig("Message.SellAllCantFindPrestigeOrRank", "&cCan't find&7 the Prestige/Rank: ");
        dataConfig("Message.SellAllRankNotFoundInPrestigeLadder", "The -prestiges- ladder &cdoesn't contain&7 the Rank: ");
        dataConfig("Message.SellAllMultiplierNotANumber", "Sorry but the multiplier &cisn't a number&7!");
        dataConfig("Message.SellAllMultiplierNotNumber2", " Here-> ");
        dataConfig("Message.SellAllConfigSaveFail", "Sorry, &csomething went wrong&7 while saving the config!");
        dataConfig("Message.SellAllMultiplierEditSaveSuccess", "Multiplier got added or edited with &asuccess&7!");
        dataConfig("Message.SellAllMultiplierFormat", "Please use this format: /sellall multiplier delete <Prestige>.");
        dataConfig("Message.SellAllCantFindMultiplier", "&cCan't find&7 the Multiplier of the prestige ");
        dataConfig("Message.SellAllCantFindMultiplier2", " &7in the sellallconfig.yml");
        dataConfig("Message.SellAllMultiplierDeleteSuccess", "Multiplier &cdeleted&7 with &asuccess&7!");
        dataConfig("Message.SellAllWrongFormatCommand", "&cWrong format&7, use /sellall help for help.");
        dataConfig("Message.SellAllPleaseAddItem", "Please &aadd&7 an ITEM_ID [example: /sellall add COAL_ORE <price>]");
        dataConfig("Message.SellAllAddPrice", "Please &aadd&7 a price or value for the item [example: /sellall add COAL_ORE 100]");
        dataConfig("Message.SellAllWrongID", "Sorry but the &cITEM_ID's wrong&7, please check it!");
        dataConfig("Message.SellAllValueNotNumber", "Sorry but the value &cisn't a number&7!");
        dataConfig("Message.SellAllMissingID", "Please &aadd&7 an ITEM_ID [example: /sellall delete COAL_ORE]");
        dataConfig("Message.SellAllNotFoundStringConfig", " not found in the config or got already &cdeleted&7.");
        dataConfig("Message.SellAllNotFoundEdit", " not found in the config!");
        dataConfig("Message.SellAllDeletedSuccess", " &cDeleted&7 with &asuccess&7!");
        dataConfig("Message.SellAllAddSuccess", "] &aadded&7 with &asuccess&7!");
        dataConfig("Message.SellAllAlreadyAdded", " &cgot already added before&7, to edit it please use the /sellall edit command!");
        dataConfig("Message.SellAllCommandEditSuccess", "] &aedited&7 with &asuccess&7!");
        dataConfig("Message.SellAllYouArentPlayer", "You &caren't&a a player");
        dataConfig("Message.SellAllNothingToSell", "You have &cnothing&7 to sell!");
        dataConfig("Message.SellAllYouGotMoney", "You got &a$");
        dataConfig("Message.SellAllGUIDisabled", "Sorry but the GUI's &cdisabled&7 in the SellAllConfig.yml.");
        dataConfig("Message.SellAllAutoSell", "Your inventory's full, &aAutoSell activated&7!");
        dataConfig("Message.SellAllSignOnly", "Sorry but you can use SellAll only by touching a sign (or with the ByPass permission).");
        dataConfig("Message.SellAllSignNotify", "Using &aSellAll&7 from a Sign with &asuccess&7!");
        dataConfig("Message.SellAllSignMissingPermission", "Sorry, but you don't have the &cpermission&7 to use this!");
        dataConfig("Message.SellAllEmpty", "&cThere aren't&7 items in the sellall config,\n please add one and/or restart the server!");
        dataConfig("Message.SellAllAutoEnabled", "SellAll-Auto &aenabled &7with &asuccess&7!");
        dataConfig("Message.SellAllAutoDisabled", "SellAll-Auto &cdisabled &7with &asuccess&7!");
        dataConfig("Message.SellAllWaitDelay", "Please &cwait&7 before using this command again!");
        dataConfig("Message.SellAllDelayAlreadyEnabled", "SellAll Delay already &aenabled&7!");
        dataConfig("Message.SellAllDelayAlreadyDisabled", "SellAll Delay already &cdisabled&7!");
        dataConfig("Message.SellAllDelayEnabled", "SellAll Delay &aenabled &7with &asuccess&7!");
        dataConfig("Message.SellAllDelayDisabled", "SellAll Delay &cdisabled &7with &asuccess&7!");
        dataConfig("Message.SellAllDelayEditedWithSuccess", "SellAll Delay edited with &asuccess&7!");
        dataConfig("Message.SellAllDelayNotNumber", "Delay value &cisn't&7 a number!");
        dataConfig("Message.SellAllGUIEmpty", "Sorry but there &caren't&7 Blocks in the SellAll Shop!");
        dataConfig("Message.SellAllGUIEmpty2", "&7[&cTIP&7] &7You can &aadd&7 one using the command /sellall add!");
        dataConfig("Message.SellAllTriggerAlreadyEnabled", "SellAll trigger's already &aenabled&7.");
        dataConfig("Message.SellAllTriggerAlreadyDisabled", "SellAll trigger's already &cdisabled&7.");
        dataConfig("Message.SellAllTriggerEnabled", "SellAll trigger &aEnabled&7.");
        dataConfig("Message.SellAllTriggerDisabled", "SellAll trigger &cDisabled&7.");
        dataConfig("Message.SellAllTriggerIsDisabled", "SellAll trigger's &cdisabled&7 in the SellAllConfig.yml.");
        dataConfig("Message.SellAllTriggerMissingItem", "Please add a valid Item ID for SellAll Shift+Right Click Trigger.");
        dataConfig("Message.SellAllTriggerItemAddSuccess", "SellAll trigger Item added with &asuccess&7!");
        dataConfig("Message.SellAllTriggerItemEditSuccess", "SellAll trigger Item edited with &asuccess&7!");
        dataConfig("Message.SellAllTriggerItemDeleteSuccess", "SellAll trigger Item deleted with &asuccess&7!");
        dataConfig("Message.SellAllTriggerMissingItem", "There isn't an item in the SellAllConfig.yml trigger like that.");
        dataConfig("Message.TooLowValue", "&cToo low value.");
        dataConfig("Message.TooHighValue", "&cToo high value.");
        dataConfig("Setup.Message.MissingPermission", "Sorry but you don't have the &cpermission&7 [-prison.setup- or -prison.admin-]!");
        dataConfig("Setup.Message.WrongFormat", "You're &cmissing&7 the last argument -mines- or -ranks-, /<command> setup -mines- or -ranks- !");
        dataConfig("Setup.Message.WelcomeToRanksSetup", "Hi and welcome to the Ranks Setup, please &cwait&7 until it'll be completed!");
        dataConfig("Setup.Message.SuccessRanksSetup", "The Ranks Setup got &acompleted&7 with &asuccess&7 and the Ranks got &aadded&7 to the default ladder, please check the logs if something's missing!");
        dataConfig("Setup.Message.Aborted", "Prison Setup &cCancelled&7.");
    }

    public FileConfiguration getFileGuiMessagesConfig(){
        return conf;
    }
}
